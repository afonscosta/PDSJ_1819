package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static Utilities.Utils.*;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.asList;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeView viewZoneTxt;

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
    public void setView(InterfCalcDateTimeView viewZone) {
        this.viewZoneTxt = viewZone;
    }

    //------------------------
    // FlowLocal
    //------------------------
    @Override
    public void flowZone() {
        Menu menu;
        String zd;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        String prefix = "Data inicial: ";
        resetDateTimeZone();
        do {
            zd = zoneDateTimeToString((ZonedDateTime) model.getDateTimeZone(), DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
            menu = viewZoneTxt.getDynamicMenu(0,statusMessage,errorMessage,Arrays.asList(BLUE_BOLD + prefix + zd + RESET));
            statusMessage = "n/a";
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "C":
                    setDateTimeZone();
                    statusMessage = "Data modificada com sucesso";
                    prefix = "Data inicial: ";
                    break;
                case "R":
                    resetDateTimeZone();
                    statusMessage = "Data modificada com sucesso";
                    prefix = "Data inicial: ";
                    break;
                case "A":
                    prefix = flowShiftZoneDateTime(prefix);
                    break;
                case "AU":
                    prefix = flowShiftWorkDaysDateTimeZone(prefix);
                    break;
                case "D": flowDiffDateTimeZone(); break;
                case "DU": flowDiffWorkDaysDateTimeZone(); break;
                case "F":
                    flowConvertZone();
                    statusMessage = "Data convertida para o fuso escolhido";
                    prefix = "Data acumulada: ";
                    break;
                case "DF": flowDiffInTimeZones(); break;
                case "?" : help(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        } while(!opcao.equals("S"));
    }

    //------------------------
    // FlowSetDateTime
    //------------------------
    private void setDateTimeZone() {
        ZonedDateTime zdt = getZoneDateTimeFromInput(viewZoneTxt, 4, model.getZoneZone(), (ZonedDateTime) model.getDateTimeZone());
        model.fromDateTimeZone(zdt);
    }

    //------------------------
    // FlowResetDateTime
    //------------------------
    private void resetDateTimeZone() {
        model.fromDateTimeZone(getNowOfZone(ZoneId.of(model.getZoneZone())));
    }

    //------------------------
    // FlowShiftDateTime
    //------------------------
    private String flowShiftZoneDateTime(String prefix) {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        String prefixTemp = prefix;
        do {
            ld = zoneDateTimeToString(zdt, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
            menu = viewZoneTxt.getDynamicMenu(1,statusMessage,errorMessage,Arrays.asList(BLUE_BOLD + prefixTemp + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "ANO" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("anos"), YEARS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "MES" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("meses"), MONTHS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "SEM" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("semanas"), WEEKS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "DIA" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("dias"), DAYS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "HOR" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("horas"), HOURS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "MIN" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("minutos"), MINUTES);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "SEG" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("segundos"), SECONDS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "NAN" :
                    zdt = (ZonedDateTime) shiftDateTime(zdt, shift("nanosegundos"), NANOS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "G":
                    model.fromDateTimeZone(zdt);
                    prefix = prefixTemp;
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!(opcao.equals("S") || opcao.equals("G")));
        return prefix;
    }

    //------------------------
    // FlowShiftWorkDaysDateTime
    //------------------------
    private String flowShiftWorkDaysDateTimeZone(String prefix) {
        String ld;
        Menu menu;
        String opcao;
        String errorMessage = "n/a";
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        String prefixTemp = prefix;
        do {
            ld = zoneDateTimeToString(zdt, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
            menu = viewZoneTxt.getDynamicMenu(2,"n/a",errorMessage,Arrays.asList(BLUE_BOLD + prefixTemp + ld + RESET));
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "M" :
                    zdt = (ZonedDateTime) shiftWorkDays(zdt, shift("dias uteis"));
                    prefixTemp = "Data acumulada: ";
                    break;
                case "G":
                    model.fromDateTimeZone(zdt);
                    prefix = prefixTemp;
                    break;
                case "S": break;
                default: errorMessage = "Opcão Inválida !"; break;
            }
        }
        while(!(opcao.equals("S") || opcao.equals("G")));
        return prefix;
    }

    //------------------------
    // FlowDiffDateTime
    //------------------------
    private void flowDiffDateTimeZone() {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        do {
            ld = zoneDateTimeToString(zdt, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
            menu = viewZoneTxt.getDynamicMenu(3,statusMessage,errorMessage,Arrays.asList(BLUE_BOLD + "Data inicio: " + ld + RESET));
            statusMessage = "n/a";
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I":
                    zdt = getZoneDateTimeFromInput(viewZoneTxt,4, zdt.getZone().getId(), zdt);
                    statusMessage = "Data de inicio modificada com sucesso";
                    break;
                case "F": diffDateTimeZone(zdt); break;
                case "S": break;
                default: errorMessage = "Opcão Inválida !"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void diffDateTimeZone(ZonedDateTime start) {
        ZonedDateTime stop = getZoneDateTimeFromInput(viewZoneTxt, 4, start.getZone().getId(), start);
        String resDiff = diffBetweenDateTime(start, stop);

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    //------------------------
    // FlowDiffWorkDaysDateTime
    //------------------------
    private void flowDiffWorkDaysDateTimeZone() {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        do {
            ld = zoneDateTimeToString(zdt, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
            menu = viewZoneTxt.getDynamicMenu(3,statusMessage,errorMessage,Arrays.asList(BLUE_BOLD + "Data inicio: " + ld + RESET));
            statusMessage = "n/a";
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" :
                    zdt = getZoneDateTimeFromInput(viewZoneTxt, 4, zdt.getZone().getId(), zdt);
                    statusMessage = "Data de inicio modificada com sucesso";
                    break;
                case "F" : diffWorkDaysDateTimeZone(zdt); break;
                case "S": break;
                default: errorMessage = "Opcão Inválida !"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void diffWorkDaysDateTimeZone(ZonedDateTime start) {
        ZonedDateTime stop = getZoneDateTimeFromInput(viewZoneTxt, 4, start.getZone().getId(), start);
        String resDiff = countWorkDays(start, stop) + " dias úteis";

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    //------------------------
    // FlowConvertZone
    //------------------------
    private void flowConvertZone() {
        try {
            String answerZone = flowGetNZoneIds(1, viewZoneTxt, 4, model.getZoneZone()).get(0);
            model.withZoneZone(answerZone);
        }
        catch (IndexOutOfBoundsException e){}
    }

    //------------------------
    // FlowDiffInTimeZones
    //------------------------
    private void flowDiffInTimeZones() {
        List<String> zoneIdsTxt = flowGetNZoneIds(2, viewZoneTxt, 4, model.getZoneZone());

        String zoneId1 = zoneIdsTxt.get(0);
        String zoneId2 = zoneIdsTxt.get(1);

        long zoneIdDifferenceHours = gmtDifference(zoneId1,zoneId2);
        String zoneIdDifferenceHoursTxt = String.valueOf(zoneIdDifferenceHours);

        if (zoneIdDifferenceHours > 0) {
            zoneIdDifferenceHoursTxt = "+" + zoneIdDifferenceHours;
        }

        out.println(GREEN_BOLD + zoneId1 + " -- (" + zoneIdDifferenceHoursTxt + " horas) --> " + zoneId2 + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    //------------------------
    // FlowHelp
    //------------------------
    private void help() {
        ZonedDateTime ld = (ZonedDateTime) model.getDateTimeLocal();
        String sld = zoneDateTimeToString(ld, DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat()));
        List<String> l = asList(
                RED_BOLD + "Data: " + sld + RESET,
                BLACK_BOLD + "^^^^" + RESET + " - A data presente no registo e usada por omissao.",
                "       No final de cada calculo o registo e atualizado.",
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

        flowHelp(viewZoneTxt.getMenu(5), l);
    }
}
