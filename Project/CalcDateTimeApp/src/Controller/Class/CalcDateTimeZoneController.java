package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZoneTxt;

    public CalcDateTimeZoneController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
        // Utilizar o zoneId do ficheiro de configuração
        this.model.fromDateTimeZone(ZonedDateTime.now());
    }

    @Override
    public void setView(InterfCalcDateTimeZoneView viewZone) {
        this.viewZoneTxt = viewZone;
    }

    private String buildZoneDateTimeTitle() {
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        return zoneDateTimeToString(zdt);
    }

    //------------------------
    // FlowLocal
    //------------------------
    @Override
    public void flowZone() {
        Menu menu = viewZoneTxt.getMenu(0);
        String zld;
        String opcao;
        do {
            zld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data: " + zld));
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "A": setDateTimeZone(); break;
                case "M": flowShiftZoneDateTime(); break;
                case "MU": flowShiftWorkDaysDateTimeZone(); break;
                case "D": flowDiffDateTimeZone(); break;
                case "DU": flowDiffWorkDaysDateTimeZone();
                case "T": flowShowCurrentTimeInZone(); break;
                case "C": flowConvertZone(); break;
                case "F": flowDiffInTimeZones(); break;
                case "S": break;
                default: out.println("Opcao Invalida!"); break;
            }

        } while(!opcao.equals("S"));
    }

    private void flowDiffInTimeZones() {
        List<String> zoneIdsTxt = flowShowAllAvailableTimezonesAndGetNZoneIds(2);

        ZoneId zoneId1 = ZoneId.of(zoneIdsTxt.get(0));
        ZoneId zoneId2 = ZoneId.of(zoneIdsTxt.get(1));

        LocalDateTime today = LocalDateTime.now();

        long zoneIdDifferenceHours = ChronoUnit.HOURS.between(today.atZone(zoneId1),today.atZone(zoneId2));

        out.println(zoneId1.toString() + " -> " + zoneId2.toString() + String.format(" (%d)", zoneIdDifferenceHours));
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    private void flowDiffWorkDaysDateTimeZone() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(3);
        String opcao;
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); break;
                case "F" : diffWorkDaysDateTimeZone(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void diffWorkDaysDateTimeZone() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getLocalDateTimeZoneFromInput();
        }
        String resDiff = model.diffWorkDaysDateTimeZone(toDateTime);

        out.println("\nResultado: " + resDiff);
        out.print("Prima Enter para continuar.");
        Input.lerString();

    }

    private void flowDiffDateTimeZone() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(3);
        String opcao;
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); break;
                case "F" : diffDateTimeLocal(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }


    private void fromDateTimeLocal() {
        ZonedDateTime newLDT = null;
        while(newLDT == null) {
            newLDT = getLocalDateTimeZoneFromInput();
        }
        model.fromDateTimeZone(newLDT);
    }

    private void diffDateTimeLocal() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getLocalDateTimeZoneFromInput();
        }
        String resDiff = model.diffDateTimeZone(toDateTime);

        out.println("\nResultado: " + resDiff);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    private void flowShiftWorkDaysDateTimeZone() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(2);
        String opcao;
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data: " + ld));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "M" : shiftWorkDays(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }


    private void shiftWorkDays() {
        out.print("(+|-) número de dias: ");
        int n = Input.lerInt();
        model.shiftWorkDaysDateTimeZone(n);
    }

    private void setDateTimeZone() {
        ZonedDateTime zdt = getLocalDateTimeZoneFromInput();
        model.fromDateTimeZone(zdt);
    }

    private ZonedDateTime getLocalDateTimeZoneFromInput() {

        ZonedDateTime ldt = (ZonedDateTime) model.getDateTimeZone();
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        String str;

        while (year == null) {
            out.print("Ano (default: " + ldt.getYear() + "): ");
            str = Input.lerString();
            year = validatePosNumber(str, ldt.getYear());
            if (year == null)
                out.println("[!] Ano invalido.");
        }

        while (month == null) {
            out.print("Mes (default: " + ldt.getMonthValue() + "): ");
            str = Input.lerString();
            month = validateMonth(str, ldt.getMonthValue());
            if (month == null)
                out.println("[!] Mes invalido.");
        }

        while (day == null) {
            out.print("Dia (default: " + ldt.getDayOfMonth() + "): ");
            str = Input.lerString();
            day = validateDay(str, ldt.getDayOfMonth(), year, month);
            if (day == null)
                out.println("[!] Dia invalido.");
        }

        while (hour == null) {
            out.print("Hora (default: " + ldt.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, ldt.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");
        }

        while (minute == null) {
            out.print("Minutos (default: " + ldt.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, ldt.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (default: " + ldt.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, ldt.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (default: " + ldt.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, ldt.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");
        }

        String zoneIdString = flowShowAllAvailableTimezonesAndGetNZoneIds(1).get(0);
        ZoneId zoneId = ZoneId.of(zoneIdString);

        return ZonedDateTime.of(year, month, day, hour, minute, second, nano, zoneId);
    }

    //------------------------
    // FlowShowCurrentTimeInZone
    //------------------------
    // Saber que data atual é numa certa região
    private void flowShowCurrentTimeInZone() {
        String answerZone = flowShowAllAvailableTimezonesAndGetNZoneIds(1).get(0);

        if (!answerZone.equals(("S"))) {
            model.changeToCurrentDateInZone(answerZone);
        }
    }

    private void flowShiftZoneDateTime() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(1);
        String opcao;
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "DIA" : shiftDays(); break;
                case "SEM" : shiftWeeks(); break;
                case "MES" : shiftMonths(); break;
                case "ANO" : shiftYears(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }


    private void shiftDays() {
        out.print("(+|-) número de dias: ");
        int n = Input.lerInt();
        model.shiftDateTimeZone(n, DAYS);
    }

    private void shiftWeeks() {
        out.print("Número de semanas: ");
        int n = Input.lerInt();
        model.shiftDateTimeZone(n, WEEKS);
    }

    private void shiftMonths() {
        out.print("Número de meses: ");
        int n = Input.lerInt();
        model.shiftDateTimeZone(n, MONTHS);
    }

    private void shiftYears() {
        out.print("Número de anos: ");
        int n = Input.lerInt();
        model.shiftDateTimeZone(n, YEARS);
    }


    //------------------------
    // FlowConvertZone
    //------------------------
    // Pedir para que zona queremos mudar a data
    private void flowConvertZone() {
        String answerZone = flowShowAllAvailableTimezonesAndGetNZoneIds(1).get(0);

        if (!answerZone.equals(("S"))) {
            model.withZone(answerZone);
        }
    }


    //------------------------
    // FlowShowAllAvailableTimeZones
    //------------------------
    // Buscar todos os ZoneIds alfabeticamente e fazer display por páginas
    private List<String> flowShowAllAvailableTimezonesAndGetNZoneIds(int zoneIdsWanted) {
        List<String> zoneIdList = new ArrayList<>();
        Boolean flowDone = false;
        List<List<String>> chosenZoneIdsByPage = partitionIntoPages(getSortedAvailableZoneIds(),25); // If someone looks for "europe", place matches it here

        int pageIndex = 0;
        int totalPages = chosenZoneIdsByPage.size();
        Menu menu = viewZoneTxt.getMenu(4);
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
            description.add(String.format("Pagina (%s/%s)", pageIndex+1, totalPages));

            menu.addDescToTitle(description);

            menu.show();
            opcao = Input.lerString();
            switch (opcao) {
                case ">": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "<": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case "S": flowDone = true; break;
                case "s": flowDone = true; break;
                default:
                    if (opcao.matches("\\/.*")) {
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
                    } else if (opcao.matches("=.*")) {
                        opcao = opcao.substring(1); // Remover o "="
                        if (getSortedAvailableZoneIds().contains(opcao)) {
                            zoneIdList.add(opcao);
                            if (zoneIdList.size() == zoneIdsWanted) {
                                flowDone = true;
                            }
                        }
                    }
                    break;
            }
        } while(!flowDone);

        return zoneIdList;
    }

}
