package Utilities;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

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
}
