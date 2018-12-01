package Utilities;

import Model.Class.Slot;

import java.text.DateFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utilities.ConsoleColors.BLACK_BOLD;
import static Utilities.ConsoleColors.RESET;
import static Utilities.ControllerUtils.prettyPrintDuration;
import static java.lang.Math.abs;
import static java.time.DayOfWeek.*;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.*;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;

// Metodos estáticos aqui
public class BusinessUtils {

    /*
     * O temp ou é uma ZonedDateTime ou uma LocalDateTime.
     * Temporal -> String
     * Formato de saída: dd/mm/aaaa
     */
    public static String tempToLocalDateStr(TemporalAccessor tacs) {
        LocalDate ld;
        try { ld = LocalDate.from(tacs); }
        catch (DateTimeException e) { return null; }
        return ld.getDayOfMonth() + "/" +
               ld.getMonth().getValue() + "/" +
               ld.getYear();
    }

    /*
     * O temp ou é uma ZonedDateTime ou uma LocalDateTime.
     * Temporal -> String
     * Formato de saída: dd/mm/aaaa  hh:mm:ss
     */
    public static String localDateTimeToString(Temporal temp, DateTimeFormatter localDateTimeFormatter) {
        LocalDateTime ldt;
        try { ldt = LocalDateTime.from(temp); }
        catch (DateTimeException e) { return null; }
        return ldt.format(localDateTimeFormatter);
    }

    /*
     * ZonedDateTime -> String
     * Formato de saída: dd/mm/aaaa  hh:mm:ss [zona]
     */
    public static String zoneDateTimeToString(ZonedDateTime zdt, DateTimeFormatter zonedDateTimeFormatter) {
        return zdt.format(zonedDateTimeFormatter);
    }

    // Dada uma ZonedDateTime, soma ou subtrai, dependendo do mode, n ChronoUnits.
    public static Temporal shiftDateTime(Temporal temp, int n, ChronoUnit cu) {
        return temp.plus(n, cu);
    }

    public static boolean isWeekend(TemporalAccessor tacs) {
        LocalDate ld;
        ld = LocalDate.from(tacs);
        return ld.getDayOfWeek().equals(SATURDAY) || ld.getDayOfWeek().equals(SUNDAY);
    }

    /*
     * Temporal, int, EnumDateTimeShiftMode -> LocalDateTime
     * Soma ou subtrai, dependendo do valor do mode, n dias úteis ao ldt.
     * Retorna null se o argumento temp não tiver uma componente Date.
     */
    public static Temporal shiftWorkDays(Temporal temp, int n) {
        int conta = 0;  // conta dias úteis
        try {
            if (temp.query(BusinessUtils::isWeekend)) conta = 1;
            while (conta < abs(n)) {
                if (!temp.query(BusinessUtils::isWeekend)) conta++;
                temp = temp.plus(n / abs(n), DAYS);
            }
            // Ajustar o dia final para não ficar num fim de semana.
            if (temp.query(BusinessUtils::isWeekend)) {
                if (n > 0) {
                    temp = nextMondayN(temp, 1);
                } else if (n < 0) {
                    temp = prevFridayN(temp, 1);
                }
            }
            return temp;
        } catch (DateTimeException e) { return null; }
    }

    /*
     * LocalDateTime, LocalDateTime -> String
     * Calcula a diferença temporal entre dois temporais desde que estejam na mesma zona.
     * Formato do output: X anos Y meses Z dias W horas V minutos U segundos D nanosegundos
     * Nota: o segundo argumento é modificado.
     */
    public static String diffBetweenDateTime(ZonedDateTime start, ZonedDateTime stop) {
        StringBuilder sb = new StringBuilder();

        ZonedDateTime stopWithSameZone = stop.withZoneSameInstant(start.getZone());

        long years = start.until( stopWithSameZone, YEARS);
        start = start.plus(years, YEARS);
        sb.append(abs(years)).append(" anos ");

        long months = start.until( stopWithSameZone, MONTHS);
        start = start.plus( months, MONTHS );
        sb.append(abs(months)).append(" meses ");

        long days = start.until( stopWithSameZone, DAYS);
        start = start.plus( days, DAYS );
        sb.append(abs(days)).append(" dias ");

        long hours = start.until( stopWithSameZone, HOURS);
        start = start.plus( hours, HOURS );
        sb.append(abs(hours)).append(" horas ");

        long minutes = start.until( stopWithSameZone, MINUTES);
        start = start.plus( minutes, MINUTES );
        sb.append(abs(minutes)).append(" minutos ");

        long seconds = start.until( stopWithSameZone, SECONDS);
        start = start.plus( seconds, SECONDS );
        sb.append(abs(seconds)).append(" segundos ");

        long nanos = start.until( stopWithSameZone, NANOS);
        sb.append(abs(nanos)).append(" nanosegundos");

        return sb.toString();
    }

    /*
     * Temporal, Temporal -> long
     * Calcula o número de dias úteis entre dois Temporal's.
     */
    public static long countWorkDays(Temporal start, Temporal stop) {
        long count;
        long extra = 0;
        if (start.query(BusinessUtils::isWeekend)) {
            start = nextMondayN(start, 1);
            extra += 1;
        }
        if (stop.query(BusinessUtils::isWeekend)) {
            stop = nextMondayN(stop, 1);
            extra -= 1;
        }
        final DayOfWeek startW = start.query(BusinessUtils::getDayOfWeek);
        final DayOfWeek stopW = stop.query(BusinessUtils::getDayOfWeek);

        final long days = ChronoUnit.DAYS.between( start , stop );
        final long daysWithoutWeekends = days - 2 * ( ( days + startW.getValue() ) / 7 );

        //adjust for starting and ending on a Sunday:
        count = daysWithoutWeekends + ( startW == SUNDAY ? 1 : 0 ) + ( stopW == SUNDAY ? 1 : 0 );

        return abs(count)+extra;
    }

    /*
     * Lê um inteiro positivo (0 inclusive) com um número de digitos entre [1, 9]
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validatePosNumber(String str, Integer def) {
        Integer num = null;
        if (str.matches("^\\d{1,9}$")) {
            num = Integer.parseInt(str);
        }
        if (str.isEmpty())
            num = def;
        return num;
    }

    /*
     * Lê um valor entre 1 e 12
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validateMonth(String str, Integer def) {
        Integer num = null;
        if (str.matches("^([1-9]|1[0-2])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty())
            num = def;
        return num;
    }

    /* Lê uma o número da semana de um dado ano e mês
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     * Exemplo:
     *      str = "5"
     *      def = null
     *      year = 2018
     *      month = 1
     *   Valida se, em 2018, Janeiro tem a 5ª semana.
     */
    public static Integer validateNumWeek(String str, Integer def, int year, int month) {
        Integer num = null;
        int nweeks = getNumWeeksInMonth(year, month);
        if (str.matches("^(\\d)$")) {
            num = Integer.parseInt(str);
            if (num <= 0 || num > nweeks)
                return null;
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    /*
     * Lê um dia que esteja presente num dado ano e mês.
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validateDay(String str, Integer def, int year, int month) {
        int end = YearMonth.of(year, month).lengthOfMonth();
        Integer num = null;
        if (str.matches("^([1-9]|1[0-9]|2[0-9]|3[01])$")) {
            num = Integer.parseInt(str);
            if (num > end) // O dia escolhido não existe no mês
                num = null;
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    /*
     * Lê um dia que esteja presente num dado ano e mês.
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validateNumDay(String str, Integer def, int year, int month, int nweek) {
        Integer num = null;
        int ndays = getNumDaysInWeek(year, month, nweek);
        if (str.matches("^([1234567])$")) {
            num = Integer.parseInt(str);
            if (num <= 0 || num > ndays)
                return null;
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    /*
     * Lê um valor entre 0 e 23
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validateHour(String str, Integer def) {
        Integer num = null;
        if (str.matches("^([0-9]|1[0-9]|2[0-3])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    /*
     * Lê um valor entre 0 e 59
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validateMinSec(String str, Integer def) {
        Integer num = null;
        if (str.matches("^([0-9]|[1-5][0-9])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    /*
     * Ano, Mes -> Número de semanas
     * Uma semana começa sempre à segunda, exceto a primeira que pode variar.
     */
    public static int getNumWeeksInMonth(int year, int month) {
        int n = 0;
        TemporalAdjuster nextMonday = next(MONDAY);
        LocalDate ld = LocalDate.of(year, month, 1);
        while (ld.getMonthValue() == month) {
            ld = ld.with(nextMonday);
            n++;
        }
        return n;
    }

    /*
     * Ano, Mes, NWeek -> Número de dias
     * Número de dias numa semana
     */
    public static int getNumDaysInWeek(int year, int month, int nweek) {
        int n = 1;
        TemporalAdjuster nextMonday = next(MONDAY);
        LocalDate ld = LocalDate.of(year, month, 1);
        while (n < nweek) {
            ld = ld.with(nextMonday);
            n++;
        }
        int start = ld.getDayOfWeek().getValue();
        while (ld.getMonthValue() == month && ld.getDayOfWeek().getValue() < 7) {
            ld = ld.plusDays(1);
        }
        if (ld.getMonthValue() != month)
            ld = ld.minusDays(1);
        return ld.getDayOfWeek().getValue() - start + 1;
    }


    /*
     * Temporal -> DayOfWeek
     * Dado um temporal dá o dia da semana.
     * Caso não seja um ZonedDateTime ou LocalDateTime devolve null.
     */
    public static DayOfWeek getDayOfWeek(TemporalAccessor tacs) {
        LocalDate ld;
        try { ld = LocalDate.from(tacs); }
        catch (DateTimeException e) { return null; }
        return ld.getDayOfWeek();
    }

    /*
     * Temporal -> DayOfWeek
     * Dado um temporal dá o dia da semana.
     * Caso não seja um ZonedDateTime ou LocalDateTime devolve null.
     */
    public static Integer getDayOfMonth(TemporalAccessor tacs) {
        LocalDate ld;
        try { ld = LocalDate.from(tacs); }
        catch (DateTimeException e) { return null; }
        return ld.getDayOfMonth();
    }

    /*
     * Temporal, int -> Temporal
     * Transita para a sexta feira anterior n vezes
     */
    public static Temporal prevFridayN(Temporal temp, int n) {
        for (int i = 0; i < n; i++) {
            temp = temp.with(previous(FRIDAY));
        }
        return temp;
    }

    /*
     * LocalDateTime, int -> LocalDateTime
     * Transita para a próxima segunda feira n vezes.
     */
    public static Temporal nextMondayN(Temporal temp, int n) {
        for (int i = 0; i < n; i++) {
            temp = temp.with(next(MONDAY));
        }
        return temp;
    }

    /*
     * LocalDateTime, int -> LocalDateTime
     * Incrementa o dia n vezes.
     */
    public static Temporal nextDayN(Temporal temp, int n) {
        for (int i = 0; i < n; i++) {
            temp = temp.plus(1, DAYS);
        }
        return temp;
    }

    public static <T> List<List<T>> partitionIntoPages(List<T> list, int elementsPerPage) {
        List<List<T>> ret = new ArrayList<>();

        int totalListIndex = 0;
        int pageListIndex = 0;

        while (totalListIndex < list.size()) {
            List<T> currentPage = new ArrayList<>();
            pageListIndex = 0;

            while ((pageListIndex < elementsPerPage) && (totalListIndex < list.size())) {
                currentPage.add(list.get(totalListIndex++));
                pageListIndex++;
            }
            ret.add(currentPage);
        }
        return ret;
    }

    public static List<String> getSortedAvailableZoneIdsAndOffset() {
        List<String> zoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        List<String> ret = new ArrayList<>();

        LocalDateTime today = LocalDateTime.now();
        for (String zone : zoneIds) {
            ZonedDateTime zdt = today.atZone(ZoneId.of(zone));
            String offset = zdt.getOffset().getId().replaceAll("Z","+00:00");
            ret.add(String.format("(%s) %s", offset, zone));
        }

        // Comparar p.e. "(-01:00) Europe/Lisbon" e "(+05:00) Asia/Tokyo"
        Comparator<String> zoneIdComparator =
                ((String z1, String z2) -> {
                    if (z1.charAt(1) == '+' && z2.charAt(1) == '-') {
                        return 1;
                    } else if (z1.charAt(1) == '-' && z2.charAt(1) == '+') {
                        return -1;
                    } else {
                        // Couldn't compare by signal, compare by number
                        String num1str = z1.substring(1,7).replace(":","");
                        String num2str = z2.substring(1,7).replace(":","");;

                        int num1 = Integer.parseInt(num1str);
                        int num2 = Integer.parseInt(num2str);

                        return num1-num2;
                    }
                });

        ret.sort(zoneIdComparator);

        return ret;
    }

    public static List<String> getSortedAvailableZoneIds() {
        List<String> ret = new ArrayList<>(ZoneId.getAvailableZoneIds());
        ret.sort(Comparator.naturalOrder());

        return ret;
    }


    /*
     * StringBuilder, int -> StringBuilder
     * Adiciona ao res o dom normalizado.
     * Exemplo:
     *      dom = 4  -> ' 4'
     *      dom = 16 -> '16'
     */
    public static StringBuilder normDay(StringBuilder res, int dom) {
        String strDom = Integer.toString(dom);
        if (strDom.length() == 1)
            res.append(" ");
        res.append(strDom);
        return res;
    }

    /*
     * LocalDateTime -> String
     * Dada uma ldt constrói uma string normalizada com todos
     * os dias dessa semana a partir do dia presente na ldt.
     * ldt: 2018/10/1 -> ' 1  2  3  4  5  6  7'
     * ldt: 2018/11/5 -> ' 5  6  7  8  9 10 11'
     */
    public static String organizeDays(TemporalAccessor tacs) {
        StringBuilder res = new StringBuilder();
        LocalDate ld;
        try { ld = LocalDate.from(tacs); }
        catch (DateTimeException e) { return null; }
        int dow = ld.getDayOfWeek().getValue();
        int dom = ld.getDayOfMonth();
        res.append(repeatStringN(" ", 3*(dow-1)));
        res = normDay(res, dom);
        res.append(" ");
        dow++; dom++;
        while(ld.getMonthValue() == ld.plus(dow-1, DAYS).getMonthValue() && dow < 7) {
            res = normDay(res, dom);
            res.append(" ");
            dow++; dom++;
        }
        if(ld.getMonthValue() == ld.plusDays(dow-1).getMonthValue()) {
            res = normDay(res, dom);
        }
        return res.toString();
    }

    /*
     * String, int -> String
     * Repete str n vezes.
     */
    public static String repeatStringN(String str, int n) {
        return String.join("", Collections.nCopies(n, str));
    }

    /*
     * int -> String
     * Dado o número de um mês devolve o seu nome na língua local.
     */
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private static List<String> putSubtitles(Slot s, ZonedDateTime date, DateTimeFormatter dtf) {
        return Arrays.asList(BLACK_BOLD + "Data: " + RESET + date.format(dtf),
                             BLACK_BOLD + "Local: " + RESET + s.getLocal(),
                             BLACK_BOLD + "Duracao: " + RESET + prettyPrintDuration(s.getDuration()),
                             BLACK_BOLD + "Descricao: " + RESET + s.getDescription());
    }

    //------------------------
    // Business utils referentes ao slot
    // Se a zoned for a de referencia, não quero imprimir a zoned
    // A zoned de referencia é a dada no ficheiro de configuração
    //------------------------
    public static List<String> slotToString(Slot s, ZoneId referenceZone, DateTimeFormatter dtfLocal,
                                            DateTimeFormatter dtfZone, boolean subtitles){
        ZonedDateTime date = ZonedDateTime.from(s.getData());
        boolean isLocal = isSlotfromReferenceZone(s,referenceZone);
        if(isLocal) {
            if (subtitles)
                return putSubtitles(s, date, dtfLocal);
            return Arrays.asList(date.format(dtfLocal), s.getLocal(), prettyPrintDuration(s.getDuration()), s.getDescription());
        }
        else{
            if (subtitles)
                return putSubtitles(s, date, dtfZone);
            return Arrays.asList(date.format(dtfZone), s.getLocal(), prettyPrintDuration(s.getDuration()), s.getDescription());
        }
    }

    public static boolean isSlotfromReferenceZone(Slot s, ZoneId referenceZone){
        ZonedDateTime date = ZonedDateTime.from(s.getData());
        return date.getZone().equals(referenceZone);
    }
    public static ZonedDateTime convertToZone (Temporal data,ZoneId referenceZone) {
        ZonedDateTime zoneData= ZonedDateTime.from(data);
        return zoneData.withZoneSameInstant(referenceZone);
    }

    //------------------------
    // Dado a caracterização da reunião(id, data, local) devolve apenas o seu identificador gerado ao nivel da interface
    // -1 caso de erro
    //------------------------
    public static long getIdSlot(String infoSlot){
        Pattern p = Pattern.compile("^[0-9]+");
        Matcher m = p.matcher(infoSlot);
        if(m.find()){
            return Long.valueOf(m.group(0));
        }
        else return -1;
    }

    /*
     * Realiza o parse de uma string para um DateTimeFormatter.
     * Em caso de exceção devolve null.
     */
    public static DateTimeFormatter parseFormat(String format) {
        try {
            return DateTimeFormatter.ofPattern(format);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static ZonedDateTime getNowOfZone(ZoneId zid) {
        return ZonedDateTime.now(zid);
    }

    public static long gmtDifference(String zoneId1, String zoneId2) {
        LocalDateTime today = LocalDateTime.now();
        ZonedDateTime z1 = today.atZone(ZoneId.of(zoneId1));
        ZonedDateTime z2 = today.atZone(ZoneId.of(zoneId2));

        long diffAbs =  ChronoUnit.HOURS.between(z1,z2);

        // Queremos representar ao contrario, de A -> B é preciso adicionar X horas
        return (diffAbs * (-1));

    }
}
