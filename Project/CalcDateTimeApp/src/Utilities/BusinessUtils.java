package Utilities;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import static java.lang.Math.abs;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.ChronoUnit.DAYS;

// Metodos estáticos aqui
public class BusinessUtils {

    // Dada uma ZonedDateTime, soma ou subtrai, dependendo do mode, n ChronoUnits.
    public static Temporal shiftDateTime(Temporal temp, int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        //Não sei se é boa ideia este método depender do EnumDateTimeShiftMode.
        switch (mode) {
            case ADD: temp = temp.plus(n, cu); break;
            case SUB: temp = temp.minus(n, cu); break;
        }
        return temp;
    }

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

    // LocalDateTime, int, EnumDateTimeShiftMode -> LocalDateTime
    // Soma ou subtrai, dependendo do valor do mode, n dias úteis ao ldt.
    public static LocalDateTime shiftWorkDaysLocal(LocalDateTime ldt, int n, EnumDateTimeShiftMode mode) {
        int conta = 0;  // conta dias úteis
        while (conta < n) {
            DayOfWeek dia = ldt.getDayOfWeek();
            if (!(dia.equals(SATURDAY) || dia.equals(SUNDAY))) conta++;
            switch (mode) {
                case ADD: ldt = ldt.plus(1, DAYS); break;
                case SUB: ldt = ldt.minus(1, DAYS); break;
            }
        }
        return ldt;
    }

    // LocalDateTime, LocalDateTime -> long
    // Calcula o número de dias úteis entre duas LocalDateTime's.
    public static long countWorkDays(LocalDateTime start, LocalDateTime stop) {
        // Code taken from Answer by Roland.
        // https://stackoverflow.com/a/44942039/642706
        long count = 0;
        final DayOfWeek startW = start.getDayOfWeek();
        final DayOfWeek stopW = stop.getDayOfWeek();

        final long days = ChronoUnit.DAYS.between( start , stop );
        final long daysWithoutWeekends = days - 2 * ( ( days + startW.getValue() ) / 7 );

        //adjust for starting and ending on a Sunday:
        count = daysWithoutWeekends + ( startW == DayOfWeek.SUNDAY ? 1 : 0 ) + ( stopW == DayOfWeek.SUNDAY ? 1 : 0 );

        return count;
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
}
