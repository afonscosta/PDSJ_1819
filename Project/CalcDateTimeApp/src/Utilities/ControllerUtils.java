package Utilities;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utilities.Utils.*;
import static Utilities.ConsoleColors.*;
import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class ControllerUtils {

    public static void clearConsole() {
        //Só deve funcionar para linux
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

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

        return ZonedDateTime.of(year, month, day, hour, minute, second, nano, zid);
    }

    public static ZonedDateTime getZoneDateTimeFromInput(Menu menu, String zid, ZonedDateTime zdt) {
        String zoneIdString = flowGetNZoneIds(1, menu, zid).get(0);
        ZoneId zoneId = ZoneId.of(zoneIdString);

        return getDateTimeFromInput(zdt, zoneId);
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
        String errorMessage = "n/a";
        do {
            menu.addDescToTitle(l);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    /*
     * Pede ao utilizador um DateTimeFormatter
     */
    public static String getDateTimeFormatterFromInput() {
        String format = Input.lerString();
        DateTimeFormatter dtf = parseFormat(format);
        while (dtf == null) {
            out.println("[!] Formato invalido.");
            out.print("Formato: ");
            format = Input.lerString();
            dtf = parseFormat(format);
        }
        return format;
    }

    public static List<String> flowGetNZoneIds(int zoneIdsWanted, Menu menu, String defaultZoneid) {
        List<String> zoneIdList = new ArrayList<>();
        Boolean flowDone = false;
        List<List<String>> chosenZoneIdsByPage = partitionIntoPages(getSortedAvailableZoneIdsAndOffset(),25); // If someone looks for "europe", place matches it here

        int pageIndex = 0;
        int totalPages = chosenZoneIdsByPage.size();
        List<String> description;
        String opcao;
        Boolean previousZoneWrong = false;
        Boolean previousInputIncorrect = false;
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
            description.add(String.format(CYAN_BOLD + "ZoneIds adicionados: (%d/%d)" + RESET, zoneIdList.size(), zoneIdsWanted));

            menu.addDescToTitle(description);

            if (previousZoneWrong) {
                menu.addErrorMessage("Zona nao existe! (Nota: O parametro distingue maiusculas de minusculas)");
                previousZoneWrong = false;
            }
            if (previousInputIncorrect) {
                menu.addErrorMessage("Opcao invalida!");
                previousInputIncorrect = false;
            }

            menu.show();
            opcao = Input.lerString();
            switch (opcao) {
                case ">": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "<": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                default:
                    if (opcao.matches("\\/.*")) { // Procurando por zoneId com "/<palavra>"
                        List<String> matches = new ArrayList<>();
                        pageIndex = 0;
                        String searchedWordNormalized = opcao.substring(1).toLowerCase(); // Remover o "/" e lowercase

                        for (String zoneId : getSortedAvailableZoneIdsAndOffset()) {
                            if (zoneId.toLowerCase().contains(searchedWordNormalized)) {
                                matches.add(zoneId);
                            }
                        }

                        chosenZoneIdsByPage = partitionIntoPages(matches,25);
                        totalPages = chosenZoneIdsByPage.size();
                    } else if (opcao.matches("=.+")) { // Selecionando zoneId com "=<palavra>"
                        opcao = opcao.substring(1); // Remover o "="
                        // Procuramos na lista SEM zoneid porque o user coloca "Europe/Lisbon" e nao "(+00:00) Europe/Lisbon"
                        if (getSortedAvailableZoneIds().contains(opcao)) {
                            zoneIdList.add(opcao);
                            if (zoneIdList.size() == zoneIdsWanted) {
                                flowDone = true;
                            }
                        } else {
                            previousZoneWrong = true;
                        }
                    } else if (opcao.matches("[Ss]")) {
                        zoneIdList.add(defaultZoneid);
                        if (zoneIdList.size() == zoneIdsWanted) {
                            flowDone = true;
                        }
                    } else {
                        previousInputIncorrect = true;
                    }
                    break;
            }
        } while(!flowDone);

        return zoneIdList;
    }

    /*
     * Faz print do header de um dado mês
     *             outubro
     *      se te qu qu se sá do
     */
    public static void printHeader(int month) {
        out.println();
        String prefix = repeatStringN(" ", (20 - getMonth(month).length())/2);
        out.println(prefix + getMonth(month));
        out.println("se te qu qu se sa do");
    }

    /*
     * Faz print do mês no formato:
     *             outubro
     *      se te qu qu se sá do
     *       1  2  3  4  5  6  7
     *       8  9 10 11 12 13 14
     *      15 16 17 18 19 20 21
     *      22 23 24 25 26 27 28
     *      29 30 31
     */
    public static String printMonth(TemporalAccessor tacs) {
        LocalDate ld;
        LocalDate start;
        try {
            ld = LocalDate.from(tacs);
            start = LocalDate.from(tacs);
            printHeader(ld.getMonthValue());
            while (ld.getMonthValue() == start.getMonthValue()) {
                out.println(ld.query(Utils::organizeDays));
                ld = (LocalDate) nextMondayN(ld, 1);
            }
            out.println();
            return null;
        }
        catch (DateTimeException ignored) { return null; }
    }

    /*
     * Faz print da semana no formato:
     *            outubro
     *     se te qu qu se sá do
     *      1  2  3  4  5  6  7
     */
    public static String printWeek(TemporalAccessor tacs) {
        LocalDate ld;
        try {
            ld = LocalDate.from(tacs);
            printHeader(ld.getMonthValue());
            out.println(ld.query(Utils::organizeDays));
            out.println();
            return null;
        }
        catch (DateTimeException ignored) { return null; }
    }

    public static String prettyPrintDuration(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public static Duration getDurationFromInput() {
        Pattern pattern;
        Matcher matcher;

        out.print("Duracao (hh:mm): ");
        String dur = Input.lerString();
        pattern = Pattern.compile("^(\\d+):(0?[0-9]|[1-5][0-9])$");
        matcher = pattern.matcher(dur);
        while (!matcher.find()) {
            out.println(RED_BOLD + "[!] Sintaxe Invalida." + RESET);
            out.print("Duracao (hh:mm): ");
            dur = Input.lerString();
            pattern = Pattern.compile("^(\\d+):(0?[0-9]|[1-5][0-9])$");
            matcher = pattern.matcher(dur);
        }

        Duration newDuration = Duration.of(parseInt(matcher.group(1)), HOURS);
        return newDuration.plus(parseInt(matcher.group(2)), MINUTES);
    }

    //------------------------
    // Recebe o local de um evento do input
    //------------------------
    public static String getLocalFromInput() {
        out.print("Local: ");
        String local = Input.lerString();
        while (local.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchido." + RESET);
            out.print("Local: ");
            local = Input.lerString();
        }
        return local;
    }

    //------------------------
    // Recebe a descrição de um evento do input
    //------------------------
    public static String getDescFromInput() {
        out.print("Descricao: ");
        String desc= Input.lerString();
        while (desc.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchida." + RESET);
            out.print("Descricao: ");
            desc= Input.lerString();
        }
        return desc;
    }

    //------------------------
    // Recebe nova descrição de um evento do input
    //------------------------
    public static String getNewDescFromInput() {
        out.print("Nova descricao: ");
        String newDesc = Input.lerString();
        while (newDesc.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchida." + RESET);
            out.print("Nova descricao: ");
            newDesc = Input.lerString();
        }
        return newDesc;
    }

    //------------------------
    // Recebe novo local de um evento do input
    //------------------------
    public static String getNewLocalFromInput() {
        out.print("Novo local: ");
        String newLocal = Input.lerString();
        while (newLocal.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchido." + RESET);
            out.print("Novo local: ");
            newLocal = Input.lerString();
        }
        return newLocal;
    }

}
