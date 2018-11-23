package Utilities;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static java.lang.System.out;
import static java.time.ZoneId.systemDefault;

public class ControllerUtils {

    // Pede ao utilizador uma data.
    // Se não disser nada fica a que se encontra no modelLocal.
    // Devolve null caso dê uma exceção.
    public static ZonedDateTime getDateTimeFromInput(ZonedDateTime zdt, ZoneId zid) {
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        String str;

        while (year == null) {
            out.print("Ano (default: " + zdt.getYear() + "): ");
            str = Input.lerString();
            year = validatePosNumber(str, zdt.getYear());
            if (year == null)
                out.println("[!] Ano invalido.");
        }

        while (month == null) {
            out.print("Mes (default: " + zdt.getMonthValue() + "): ");
            str = Input.lerString();
            month = validateMonth(str, zdt.getMonthValue());
            if (month == null)
                out.println("[!] Mes invalido.");
        }

        while (day == null) {
            out.print("Dia (default: " + zdt.getDayOfMonth() + "): ");
            str = Input.lerString();
            day = validateDay(str, zdt.getDayOfMonth(), year, month);
            if (day == null)
                out.println("[!] Dia invalido.");
        }

        while (hour == null) {
            out.print("Hora (default: " + zdt.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, zdt.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");
        }

        while (minute == null) {
            out.print("Minutos (default: " + zdt.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, zdt.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (default: " + zdt.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, zdt.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (default: " + zdt.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, zdt.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");
        }

        if (zid != null)
            return ZonedDateTime.of(year, month, day, hour, minute, second, nano, zid);
        // Colocar o que está no ficheiro de configuração
        return ZonedDateTime.of(year, month, day, hour, minute, second, nano, systemDefault());
    }

    //------------------------
    // Alterar Apenas o Time de uma ZonedDateTime
    //------------------------
    public static ZonedDateTime shitTime(ZonedDateTime zdt) {
        String str;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        while (hour == null) {
            out.print("Hora (inicial: " + zdt.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, zdt.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");

        }

        while (minute == null) {
            out.print("Minutos (inicial: " + zdt.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, zdt.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (inicial: " + zdt.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, zdt.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (inicial: " + zdt.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, zdt.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");

        }
        return ZonedDateTime.of(zdt.getYear(),
                zdt.getMonthValue(),
                zdt.getDayOfMonth(),
                hour,
                minute,
                second,
                nano,
                zdt.getZone());
    }


    //substitui o shitfDays shiftWeeks shiftMonth shitfYears e shiftWorkDays
    public static int shift(String type) {
        out.print("(+|-) numero de " + type + ": ");
        return Input.lerInt();
    }

    /*
     * Flow responsável pelo menu de ajuda.
     */
    public static void flowHelp(Menu menu, List<String> l)  {
        String opcao;
        do {
            menu.addDescToTitle(l);
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "S": break;
                default: out.println("Opcao Invalida!"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    /*
     * Pede ao utilizador um DateTimeFormatter
     */
    public static DateTimeFormatter getDateTimeFormatterFromInput() {
        String format = Input.lerString();
        DateTimeFormatter dtf = parseFormat(format);
        while (dtf == null) {
            out.println("[!] Formato invalido.");
            out.print("Formato: ");
            format = Input.lerString();
            dtf = parseFormat(format);
        }
        return dtf;
    }

    public static List<String> flowShowAllAvailableTimezonesAndGetNZoneIds(int zoneIdsWanted, Menu menu, String defaultZoneid) {
        List<String> zoneIdList = new ArrayList<>();
        Boolean flowDone = false;
        List<List<String>> chosenZoneIdsByPage = partitionIntoPages(getSortedAvailableZoneIds(),25); // If someone looks for "europe", place matches it here

        int pageIndex = 0;
        int totalPages = chosenZoneIdsByPage.size();
        List<String> description;
        String opcao;
        do {

            // Mais complexo do que necessário para o caso em que a lista de procuras está vazia,
            // e assim não acontece indexOuto
            try {
                description = new ArrayList(chosenZoneIdsByPage.get(pageIndex));
            } catch (IndexOutOfBoundsException e) {
                description = new ArrayList<>();
            }

            description.add(""); // Linha branca na descrição
            int pageIndexToDisplay = (totalPages == 0) ? 0 : pageIndex + 1;
            description.add(String.format("Pagina (%s/%s)", pageIndexToDisplay, totalPages));
            description.add(String.format("ZoneIds adicionados: (%d/%d)", zoneIdList.size(), zoneIdsWanted));

            menu.addDescToTitle(description);

            menu.show();
            opcao = Input.lerString();
            switch (opcao) {
                case ">": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "<": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                default:
                    if (opcao.matches("\\/.*")) { // Procurando por zoneId com "/<palavra>"
                        List<String> matches = new ArrayList<>();
                        pageIndex = 0;
                        String searchedWordNormalized = opcao.substring(1).toLowerCase(); // Remover o "?" e lowercase

                        for (String zoneId : getSortedAvailableZoneIds()) {
                            if (zoneId.toLowerCase().contains(searchedWordNormalized)) {
                                matches.add(zoneId);
                            }
                        }

                        chosenZoneIdsByPage = partitionIntoPages(matches,25);
                        totalPages = chosenZoneIdsByPage.size();
                    } else if (opcao.matches("=.+")) { // Selecionando zoneId com "=<palavra>"
                        opcao = opcao.substring(1); // Remover o "="
                        if (getSortedAvailableZoneIds().contains(opcao)) {
                            zoneIdList.add(opcao);
                            if (zoneIdList.size() == zoneIdsWanted) {
                                flowDone = true;
                            }
                        }
                    } else if (opcao.matches("=")) { // Selecionando o zoneId por defeito com "="
                        zoneIdList.add(defaultZoneid);
                        if (zoneIdList.size() == zoneIdsWanted) {
                            flowDone = true;
                        }
                    }
                    break;
            }
        } while(!flowDone);

        return zoneIdList;
    }
}
