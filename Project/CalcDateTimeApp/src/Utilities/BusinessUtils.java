package Utilities;

import java.text.DateFormatSymbols;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.time.DayOfWeek.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.next;

// Metodos estáticos aqui
public class BusinessUtils {

    /*
     * O temp ou é uma ZonedDateTime ou uma LocalDateTime.
     * Temporal -> String
     * Formato de saída: dd/mm/aaaa
     */
    public static String localDateToString(Temporal temp) {
        LocalDateTime ldt;
        if (temp.getClass().getSimpleName().equals("ZonedDateTime")) {
            ldt = ((ZonedDateTime) temp).toLocalDateTime();
        }
        else
            ldt = (LocalDateTime) temp;
        String dt = ldt.getDayOfMonth() + "/" +
            ldt.getMonth().getValue() + "/" +
            ldt.getYear();
        return dt;
    }

    /*
     * O temp ou é uma ZonedDateTime ou uma LocalDateTime.
     * Temporal -> String
     * Formato de saída: dd/mm/aaaa  hh:mm:ss
     */
    public static String localDateTimeToString(Temporal temp) {
        LocalDateTime ldt;
        if (temp.getClass().getSimpleName().equals("ZonedDateTime")) {
            ldt = ((ZonedDateTime) temp).toLocalDateTime();
        }
        else
            ldt = (LocalDateTime) temp;
        String dt = ldt.getDayOfMonth() + "/" +
                    ldt.getMonth().getValue() + "/" +
                    ldt.getYear() + " " +
                    ldt.getHour() + ":" +
                    ldt.getMinute() + ":" +
                    ldt.getSecond() + ":" +
                    ldt.getNano();
        return dt;
    }

    /*
     * ZonedDateTime -> String
     * Formato de saída: dd/mm/aaaa  hh:mm:ss [zona]
     */
    public static String zoneDateTimeToString(ZonedDateTime zdt) {
        String dt = zdt.getDayOfMonth() + "/" +
                    zdt.getMonth().getValue() + "/" +
                    zdt.getYear() + " " +
                    zdt.getHour() + ":" +
                    zdt.getMinute() + ":" +
                    zdt.getSecond() + ":" +
                    zdt.getNano() +
                    " [" + zdt.getZone() + "]";
        return dt;
    }

    // Dada uma ZonedDateTime, soma ou subtrai, dependendo do mode, n ChronoUnits.
    public static Temporal shiftDateTime(Temporal temp, int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        //Não sei se é boa ideia este método depender do EnumDateTimeShiftMode.
        switch (mode) {
            case ADD: temp = temp.plus(n, cu); break;
            case SUB: temp = temp.minus(n, cu); break;
        }
        return temp;
    }

    /*
     * LocalDateTime, int, EnumDateTimeShiftMode -> LocalDateTime
     * Soma ou subtrai, dependendo do valor do mode, n dias úteis ao ldt.
     */
    public static LocalDateTime shiftWorkDaysLocal(LocalDateTime ldt, int n, EnumDateTimeShiftMode mode) {
        int conta = 0;  // conta dias úteis
        DayOfWeek dia;
        while (conta < n) {
            System.out.println(localDateTimeToString(ldt));
            dia = ldt.getDayOfWeek();
            if (!(dia.equals(SATURDAY) || dia.equals(SUNDAY))) conta++;
            switch (mode) {
                case ADD: ldt = ldt.plus(1, DAYS); break;
                case SUB: ldt = ldt.minus(1, DAYS); break;
            }
        }
        // Ajustar o dia final para não ficar num fim de semana.
        dia = ldt.getDayOfWeek();
        switch (mode) {
            case ADD:
                if (dia.equals(SATURDAY) || dia.equals(SUNDAY)) {
                    TemporalAdjuster nextMonday = next(MONDAY);
                    ldt = ldt.with(nextMonday);
                }
                break;
            case SUB:
                if (dia.equals(SATURDAY) || dia.equals(SUNDAY)) {
                    TemporalAdjuster prevFriday = TemporalAdjusters.previous(FRIDAY);
                    ldt = ldt.with(prevFriday);
                }
                break;
        }
        return ldt;
    }

    /*
     * LocalDateTime, LocalDateTime -> String
     * Calcula a diferença temporal entre duas LocalDateTime's.
     * Formato do output: X anos Y meses Z dias W horas V minutos U segundos D nanosegundos
     */
    public static String diffBetweenLocalDateTime(LocalDateTime start, LocalDateTime stop) {
        StringBuilder sb = new StringBuilder();

        LocalDateTime tempLDT = LocalDateTime.from(start);

        long years = tempLDT.until( stop, ChronoUnit.YEARS);
        tempLDT = tempLDT.plusYears( years );
        sb.append(abs(years)).append(" anos ");

        long months = tempLDT.until( stop, ChronoUnit.MONTHS);
        tempLDT = tempLDT.plusMonths( months );
        sb.append(abs(months)).append(" meses ");

        long days = tempLDT.until( stop, DAYS);
        tempLDT = tempLDT.plusDays( days );
        sb.append(abs(days)).append(" dias ");

        long hours = tempLDT.until( stop, ChronoUnit.HOURS);
        tempLDT = tempLDT.plusHours( hours );
        sb.append(abs(hours)).append(" horas ");

        long minutes = tempLDT.until( stop, ChronoUnit.MINUTES);
        tempLDT = tempLDT.plusMinutes( minutes );
        sb.append(abs(minutes)).append(" minutos ");

        long seconds = tempLDT.until( stop, ChronoUnit.SECONDS);
        tempLDT = tempLDT.plusSeconds( seconds );
        sb.append(abs(seconds)).append(" segundos ");

        long nanos = tempLDT.until( stop, ChronoUnit.NANOS);
        sb.append(abs(nanos)).append(" nanosegundos ");

        return sb.toString();
    }

    /*
     * LocalDateTime, LocalDateTime -> long
     * Calcula o número de dias úteis entre duas LocalDateTime's.
     */
    public static long countWorkDays(LocalDateTime start, LocalDateTime stop) {
        // Code taken from Answer by Roland.
        // https://stackoverflow.com/a/44942039/642706
        long count = 0;
        // Faz-se +1 dia para não incluir o dia atual.
        final DayOfWeek startW = start.plusDays(1).getDayOfWeek();
        final DayOfWeek stopW = stop.getDayOfWeek();

        final long days = ChronoUnit.DAYS.between( start , stop );
        final long daysWithoutWeekends = days - 2 * ( ( days + startW.getValue() ) / 7 );

        //adjust for starting and ending on a Sunday:
        count = daysWithoutWeekends + ( startW == DayOfWeek.SUNDAY ? 1 : 0 ) + ( stopW == DayOfWeek.SUNDAY ? 1 : 0 );

        return abs(count);
    }

    /*
     * Lê um inteiro positivo (0 inclusive)
     * Devolve def caso seja lida a string vazia
     * Devolve null caso não seja validado
     */
    public static Integer validatePosNumber(String str, Integer def) {
        Integer num = null;
        if (str.matches("^\\d+$")) {
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
        if (str.matches("^(1|2|3|4|5|6|7)$")) {
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
     * LocalDateTime, int -> LocalDateTime
     * Transita para a próxima segunda feira n vezes.
     */
    public static LocalDateTime nextMondayN(LocalDateTime ldt, int n) {
        for (int i = 0; i < n; i++) {
            ldt = ldt.with(next(MONDAY));
        }
        return ldt;
    }

    /*
     * LocalDateTime, int -> LocalDateTime
     * Incrementa o dia n vezes.
     */
    public static LocalDateTime nextDayN(LocalDateTime ldt, int n) {
        for (int i = 0; i < n; i++) {
            ldt = ldt.plusDays(1);
        }
        return ldt;
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
    public static String organizeDays(LocalDateTime ldt) {
        StringBuilder res = new StringBuilder();
        int dow = ldt.getDayOfWeek().getValue();
        int dom = ldt.getDayOfMonth();
        res.append(repeateStringN(" ", 3*(dow-1)));
        res = normDay(res, dom);
        res.append(" ");
        dow++; dom++;
        while(ldt.getMonthValue() == ldt.plusDays(dow-1).getMonthValue() && dow < 7) {
            res = normDay(res, dom);
            res.append(" ");
            dow++; dom++;
        }
        if(ldt.getMonthValue() == ldt.plusDays(dow-1).getMonthValue()) {
            res = normDay(res, dom);
        }
        return res.toString();
    }

    /*
     * String, int -> String
     * Repete str n vezes.
     */
    public static String repeateStringN(String str, int n) {
        return String.join("", Collections.nCopies(n, str));
    }

    /*
     * int -> String
     * Dado o número de um mês devolve o seu nome na língua local.
     */
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
