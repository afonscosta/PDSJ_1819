package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.ControllerUtils;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static Utilities.ControllerUtils.flowShowAllAvailableTimezonesAndGetNZoneIds;
import static Utilities.ControllerUtils.getDateTimeFromInput;
import static Utilities.ControllerUtils.shift;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;

/*TODO:
Edgar
    1.resolver todos os pontos em que entro no navegador de zoned e quero sair logo sem ter que escolher nada
    flowConvertZone() -> está com try catch (falaste em usar Optional)
    há mais métodos para além deste que estão com o mesmo problema e não pus try catch
    ao testar para ser em todos os fluxos que envolvam o navegador
    2. Diferenca entre dois fusos -> adicionar uma mensagem qd o utilziador introduzir o primeiro. Se não parece que foi inválido
    3. Diferenca entre datas -> inserir data fim -> tb escolho o zoned e dps não acontece nada
    */

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZoneTxt;
    private DateTimeFormatter dateTimeFormatter;

    public static CalcDateTimeZoneController of() {
        return new CalcDateTimeZoneController();
    }

    public static CalcDateTimeZoneController of(DateTimeFormatter dtf) {
        if (dtf != null)
            return new CalcDateTimeZoneController(dtf);
        else
            return new CalcDateTimeZoneController();
    }

    private CalcDateTimeZoneController() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n [VV]");
    }

    private CalcDateTimeZoneController(DateTimeFormatter dtf) {
        this.dateTimeFormatter = dtf;
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

    @Override
    public void setDateTimeFormatter(DateTimeFormatter dtf) {
        this.dateTimeFormatter = dtf;
    }

    private String buildZoneDateTimeTitle() {
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeZone();
        return zoneDateTimeToString(zdt, dateTimeFormatter);
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
        List<String> zoneIdsTxt = flowShowAllAvailableTimezonesAndGetNZoneIds(2, viewZoneTxt.getMenu(4));

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
            toDateTime = getZoneDateTimeFromInput();
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
            newLDT = getZoneDateTimeFromInput();
        }
        model.fromDateTimeZone(newLDT);
    }

    private void diffDateTimeLocal() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getZoneDateTimeFromInput();
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
                case "M" : model.shiftWorkDaysDateTimeZone(shift("dias")); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void setDateTimeZone() {
        ZonedDateTime zdt = getZoneDateTimeFromInput();
        model.fromDateTimeZone(zdt);
    }

    private ZonedDateTime getZoneDateTimeFromInput() {
        String zoneIdString = flowShowAllAvailableTimezonesAndGetNZoneIds(1, viewZoneTxt.getMenu(4)).get(0);
        ZoneId zoneId = ZoneId.of(zoneIdString);

        return getDateTimeFromInput((ZonedDateTime) model.getDateTimeZone(), zoneId);
    }

    //------------------------
    // FlowShowCurrentTimeInZone
    //------------------------
    // Saber que data atual é numa certa região
    private void flowShowCurrentTimeInZone() {
        String answerZone = flowShowAllAvailableTimezonesAndGetNZoneIds(1, viewZoneTxt.getMenu(4)).get(0);

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
                case "ANO" : model.shiftDateTimeZone(shift("anos"),         YEARS);   break;
                case "MES" : model.shiftDateTimeZone(shift("meses"),        MONTHS);  break;
                case "SEM" : model.shiftDateTimeZone(shift("semanas"),      WEEKS);   break;
                case "DIA" : model.shiftDateTimeZone(shift("dias"),         DAYS);    break;
                case "HOR" : model.shiftDateTimeZone(shift("horas"),        HOURS);   break;
                case "MIN" : model.shiftDateTimeZone(shift("minutos"),      MINUTES); break;
                case "SEG" : model.shiftDateTimeZone(shift("segundos"),     SECONDS); break;
                case "NAN" : model.shiftDateTimeZone(shift("nanosegundos"), NANOS);   break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
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
            String answerZone = flowShowAllAvailableTimezonesAndGetNZoneIds(1, viewZoneTxt.getMenu(4)).get(0);

            if (!answerZone.equals(("S"))) {
                model.withZoneZone(answerZone);
            }
        }
        catch (IndexOutOfBoundsException e){}
    }


    //------------------------
    // FlowShowAllAvailableTimeZones
    //------------------------
    // Buscar todos os ZoneIds alfabeticamente e fazer display por páginas

}
