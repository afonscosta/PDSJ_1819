package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.asList;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZoneTxt;

    public static CalcDateTimeZoneController of() {
        return new CalcDateTimeZoneController();
    }

    private CalcDateTimeZoneController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    @Override
    public void setView(InterfCalcDateTimeZoneView viewZone) {
        this.viewZoneTxt = viewZone;
    }


    private String buildZoneDateTimeTitle() {
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        return zoneDateTimeToString(zdt, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
    }

    //------------------------
    // FlowLocal
    //------------------------
    @Override
    public void flowZone() {
        Menu menu = viewZoneTxt.getMenu(0);
        String zld;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            zld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data: " + zld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            statusMessage = "n/a";
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "C": setDateTimeZone(); statusMessage = "Data modificada com sucesso"; break;
                case "R": resetDateTimeZone(); statusMessage = "Data modificada com sucesso";  break;
                case "A": flowShiftZoneDateTime(); break;
                case "AU": flowShiftWorkDaysDateTimeZone(); break;
                case "D": flowDiffDateTimeZone(); break;
                case "DU": flowDiffWorkDaysDateTimeZone(); break;
                case "T": flowShowCurrentTimeInZone(); statusMessage = "Data convertida para tempo atual no fuso escolhido"; break;
                case "F": flowConvertZone(); statusMessage = "Data convertida para o fuso escolhido"; break;
                case "DF": flowDiffInTimeZones(); break;
                case "?" : help(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        } while(!opcao.equals("S"));
    }

    private void resetDateTimeZone() {
        model.fromDateTimeZone(getNowOfZone(ZoneId.of(model.getZoneZone())));
    }

    private void help() {
        ZonedDateTime ld = (ZonedDateTime) model.getDateTimeLocal();
        String sld = zoneDateTimeToString(ld, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
        Menu menu = viewZoneTxt.getMenu(5);
        List<String> l = asList(
                RED_BOLD + "Data: " + sld + RESET,
                BLACK_BOLD + "^^^^" + RESET + " - A data presente no registo e usada por omissao ",
                "       nos diferentes calculos possiveis neste menu. No ",
                "       final de cada calculo o registo e atualizado de ",
                "       forma a conter o resultado da operacao efetuada.",
                " ",
                BLACK_BOLD + "Opcao C:" + RESET + " permite ao utilizador alterar a data ",
                "         que se encontra no registo 'Data' a cima.",
                " ",
                BLACK_BOLD + "Opcao A:" + RESET + " permite ao utilizador somar ou subtrair ",
                "         anos, meses, semanas, dias, horas, minutos, ",
                "         segundos ou nanosegundos, a data que se ",
                "         encontra no registo.",
                " ",
                BLACK_BOLD + "Opcao AU:" + RESET + " permite ao utilizador somar ou subtrair ",
                "          dias uteis a data que se encontra no registo.",
                " ",
                BLACK_BOLD + "Opcao D:" + RESET + " permite ao utilizador realizar a diferenca ",
                "         entre datas, sendo que o resultado e ",
                "         apresentado em anos, meses, dias, horas, ",
                "         minutos, segundos e nanosegundos.",
                " ",
                BLACK_BOLD + "Opcao DU:" + RESET + " permite ao utilizador realizar a diferenca ",
                "          entre datas, sendo que o resultado e apresentado ",
                "          em dias uteis.",
                " ",
                BLACK_BOLD + "Opcao T:" + RESET + "permite ao utilizador saber a data",
                "         atual num certo fuso horario",
                " ",
                BLACK_BOLD + "Opcao F:" + RESET + "permite ao utilizador converter",
                "         a data em cima para o fuso escolhido",
                " ",
                BLACK_BOLD + "Opcao DF:" + RESET + "permite ao utilizador saber",
                "         a diferenca temporal entre dois fusos",
                " ",
                BLACK_BOLD + "Opcao ?:" + RESET + " permite ao utilizador visualizar este menu.",
                " ",
                BLACK_BOLD + "Opcao S:" + RESET + " permite ao utilizador voltar ao Menu Principal.");

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

    private void flowDiffInTimeZones() {
        List<String> zoneIdsTxt = flowGetNZoneIds(2, viewZoneTxt.getMenu(4), model.getZoneZone());

        String zoneId1 = zoneIdsTxt.get(0);
        String zoneId2 = zoneIdsTxt.get(1);

        long zoneIdDifferenceHours = gmtDifference(zoneId1,zoneId2);
        String zoneIdDifferenceHoursTxt = String.valueOf(zoneIdDifferenceHours);

        if (zoneIdDifferenceHours > 0) {
            zoneIdDifferenceHoursTxt = "+" + zoneIdDifferenceHours;
        }

        out.println(GREEN_BOLD + zoneId1 + " --(" + zoneIdDifferenceHoursTxt + " horas)--> " + zoneId2 + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    private void flowDiffWorkDaysDateTimeZone() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(3);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            statusMessage = "n/a";
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "F" : diffWorkDaysDateTimeZone(); break;
                case "S": break;
                default: errorMessage = "Opcão Inválida !"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void diffWorkDaysDateTimeZone() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getZoneDateTimeFromInput(viewZoneTxt.getMenu(4), model.getZoneZone(), (ZonedDateTime) model.getDateTimeZone());
        }
        String resDiff = model.diffWorkDaysDateTimeZone(toDateTime);

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();

    }

    private void flowDiffDateTimeZone() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(3);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            statusMessage = "n/a";
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "F" : diffDateTimeLocal(); break;
                case "S": break;
                default: errorMessage = "Opcão Inválida !"; break;
            }
        }
        while(!opcao.equals("S"));
    }


    private void fromDateTimeLocal() {
        ZonedDateTime newLDT = null;
        while(newLDT == null) {
            newLDT = getZoneDateTimeFromInput(viewZoneTxt.getMenu(4), model.getZoneZone(), (ZonedDateTime) model.getDateTimeZone());
        }
        model.fromDateTimeZone(newLDT);
    }

    private void diffDateTimeLocal() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getZoneDateTimeFromInput(viewZoneTxt.getMenu(4), model.getZoneZone(), (ZonedDateTime) model.getDateTimeZone());
        }
        String resDiff = model.diffDateTimeZone(toDateTime);

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    private void flowShiftWorkDaysDateTimeZone() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(2);
        String opcao;
        String errorMessage = "n/a";
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data: " + ld));
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "M" : model.shiftWorkDaysDateTimeZone(shift("dias")); break;
                case "S": break;
                default: errorMessage = "Opcão Inválida !"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void setDateTimeZone() {
        ZonedDateTime zdt = getZoneDateTimeFromInput(viewZoneTxt.getMenu(4), model.getZoneZone(), (ZonedDateTime) model.getDateTimeZone());
        model.fromDateTimeZone(zdt);
    }

    //------------------------
    // FlowShowCurrentTimeInZone
    //------------------------
    // Saber que data atual é numa certa região
    private void flowShowCurrentTimeInZone() {
        String answerZone = flowGetNZoneIds(1, viewZoneTxt.getMenu(4), model.getZoneZone()).get(0);
        model.changeToCurrentDateInZone(answerZone);
    }

    private void flowShiftZoneDateTime() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(1);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "ANO" :
                    model.shiftDateTimeZone(shift("anos"), YEARS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "MES" :
                    model.shiftDateTimeZone(shift("meses"), MONTHS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "SEM" :
                    model.shiftDateTimeZone(shift("semanas"), WEEKS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "DIA" :
                    model.shiftDateTimeZone(shift("dias"), DAYS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "HOR" :
                    model.shiftDateTimeZone(shift("horas"), HOURS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "MIN" :
                    model.shiftDateTimeZone(shift("minutos"), MINUTES);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "SEG" :
                    model.shiftDateTimeZone(shift("segundos"), SECONDS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "NAN" :
                    model.shiftDateTimeZone(shift("nanosegundos"), NANOS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    //------------------------
    // FlowConvertZone
    //------------------------
    // Pedir para que zona queremos mudar a data
    private void flowConvertZone() {
        try {
            String answerZone = flowGetNZoneIds(1, viewZoneTxt.getMenu(4), model.getZoneZone()).get(0);
            model.withZoneZone(answerZone);
        }
        catch (IndexOutOfBoundsException e){}
    }
}
