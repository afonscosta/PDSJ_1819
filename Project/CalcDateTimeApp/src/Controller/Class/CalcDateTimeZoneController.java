package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static Utilities.EnumDateTimeShiftMode.ADD;
import static Utilities.EnumDateTimeShiftMode.SUB;
import static java.lang.Math.abs;
import static java.lang.System.getSecurityManager;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZoneTxt;
    private final String bufferCode = "zona";

    public CalcDateTimeZoneController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
        // Utilizar o zoneId do ficheiro de configuração
        this.model.fromDateTime(bufferCode, ZonedDateTime.now());
    }

    @Override
    public void setView(InterfCalcDateTimeZoneView viewZone) {
        this.viewZoneTxt = viewZone;
    }

    private String buildZoneDateTimeTitle() {
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTime(bufferCode);
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
                case "D": flowShiftZoneDateTime(); break;
                case "A": flowShowCurrentTimeInZone(); break;
                case "C": flowConvertZone(); break;
                case "S": break;
                default: out.println("Opcao Invalida!"); break;
            }

        } while(!opcao.equals("S"));
    }

    //------------------------
    // FlowShowCurrentTimeInZone
    //------------------------
    // Saber que data atual é numa certa região
    private void flowShowCurrentTimeInZone() {
        String answerZone = flowShowAllAvailableTimezonesAndGetZoneId();

        if (!answerZone.equals(("S"))) {
            model.changeToCurrentDateInZone(bufferCode, answerZone);
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
        model.shiftDateTime(bufferCode, n, DAYS);
    }

    private void shiftWeeks() {
        out.print("Número de semanas: ");
        int n = Input.lerInt();
        model.shiftDateTime(bufferCode, n, WEEKS);
    }

    private void shiftMonths() {
        out.print("Número de meses: ");
        int n = Input.lerInt();
        model.shiftDateTime(bufferCode, n, MONTHS);
    }

    private void shiftYears() {
        out.print("Número de anos: ");
        int n = Input.lerInt();
        model.shiftDateTime(bufferCode, n, YEARS);
    }


    //------------------------
    // FlowConvertZone
    //------------------------
    // Pedir para que zona queremos mudar a data
    private void flowConvertZone() {
        String answerZone = flowShowAllAvailableTimezonesAndGetZoneId();

        if (!answerZone.equals(("S"))) {
            model.convertZoneDateTimeToZone(bufferCode, answerZone);
        }
    }


    //------------------------
    // FlowShowAllAvailableTimeZones
    //------------------------
    // Buscar todos os ZoneIds alfabeticamente e fazer display por páginas
    private String flowShowAllAvailableTimezonesAndGetZoneId() {
        Boolean flowDone = false;
        List<List<String>> chosenZoneIdsByPage = partitionIntoPages(getSortedAvailableZoneIds(),25); // If someone looks for "europe", place matches it here

        int pageIndex = 0;
        int totalPages = chosenZoneIdsByPage.size();
        Menu menu = viewZoneTxt.getMenu(2);
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
            description.add(String.format("Page (%s/%s)", pageIndex+1, totalPages));

            menu.addDescToTitle(description);

            menu.show();
            opcao = Input.lerString();
            switch (opcao) {
                case "+": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "-": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
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
                            flowDone = true;
                        }
                    }
                    break;
            }
        } while(!flowDone);

        return opcao;
    }

}
