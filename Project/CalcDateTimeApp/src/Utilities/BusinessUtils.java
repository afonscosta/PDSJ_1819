package Utilities;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.lang.Math.abs;
import static java.time.DayOfWeek.*;
import static java.time.temporal.ChronoUnit.DAYS;

// Metodos estáticos aqui
public class BusinessUtils {

    // O temp ou é uma ZonedDateTime ou uma LocalDateTime.
    // Temporal -> String
    // Formato de saída: dd/mm/aaaa  hh:mm:ss
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

    // ZonedDateTime -> String
    // Formato de saída: dd/mm/aaaa  hh:mm:ss [zona]
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

    // LocalDateTime, int, EnumDateTimeShiftMode -> LocalDateTime
    // Soma ou subtrai, dependendo do valor do mode, n dias úteis ao ldt.
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
                    TemporalAdjuster nextMonday = TemporalAdjusters.next(MONDAY);
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

    // LocalDateTime, LocalDateTime -> String
    // Calcula a diferença temporal entre duas LocalDateTime's.
    // Formato do output: X anos Y meses Z dias W horas V minutos U segundos D nanosegundos
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

    // LocalDateTime, LocalDateTime -> long
    // Calcula o número de dias úteis entre duas LocalDateTime's.
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

    // Lê um inteiro positivo (0 inclusive)
    // Devolve def caso seja lida a string vazia
    // Devolve null caso tenha lido
    public static Integer validatePosNumber(String str, int def) {
        Integer num = null;
        if (str.matches("^\\d+$")) {
            num = Integer.parseInt(str);
        }
        if (str.isEmpty())
            num = def;
        return num;
    }

    // Lê um valor entre 1 e 12
    // Devolve def caso seja lida a string vazia
    // Devolve null caso tenha lido
    public static Integer validateMonth(String str, int def) {
        Integer num = null;
        if (str.matches("^([1-9]|1[0-2])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty())
            num = def;
        return num;
    }

    // Lê um dia que esteja presente num dado ano e mês.
    // Devolve def caso seja lida a string vazia
    // Devolve null caso tenha lido
    public static Integer validateDay(String str, int def, int year, int month) {
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

    // Lê um valor entre 0 e 23
    // Devolve def caso seja lida a string vazia
    // Devolve null caso tenha lido
    public static Integer validateHour(String str, int def) {
        Integer num = null;
        if (str.matches("^([0-9]|1[0-9]|2[0-3])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    // Lê um valor entre 0 e 59
    // Devolve def caso seja lida a string vazia
    // Devolve null caso tenha lido
    public static Integer validateMinSec(String str, int def) {
        Integer num = null;
        if (str.matches("^([0-9]|[1-5][0-9])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }
}
