package Utilities;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
        LocalDateTime ldt = zdt.toLocalDateTime();

        String dt = ldt.getDayOfMonth() + "/" +
                    ldt.getMonth().getValue() + "/" +
                    ldt.getYear() + " " +
                    ldt.getHour() + ":" +
                    ldt.getMinute() + ":" +
                    ldt.getSecond() + ":" +
                    ldt.getNano() +
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

    // Retornar todos os zoneIds disponiveis, X em cada página
    public static List<List<String>> getAvailableTimeZoneIdsByPage(int zoneIdsPerPage) {
        List<List<String>> ret = new ArrayList<>();
        List<String> allZoneIds = new ArrayList<>(ZoneId.getAvailableZoneIds());
        allZoneIds.sort(Comparator.naturalOrder());
        int zoneIdIndex = 0;
        int zoneIdPerPageCounter = 0;


        while (zoneIdIndex < allZoneIds.size()) {

            List<String> zoneIdPage = new ArrayList<>();
            zoneIdPerPageCounter = 0;

            while ((zoneIdPerPageCounter < zoneIdsPerPage) && (zoneIdIndex < allZoneIds.size())) {
                zoneIdPage.add(allZoneIds.get(zoneIdIndex++));
                zoneIdPerPageCounter++;
            }

            ret.add(zoneIdPage);

        }


        return ret;
    }


    public static void clearConsole() {
        //Só deve funcionar para linux
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
