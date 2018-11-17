package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeZoneView;

import static Utilities.EnumDateTimeShiftMode.ADD;
import static Utilities.EnumDateTimeShiftMode.SUB;
import static Utilities.BusinessUtils.zoneDateTimeToString;
import static Utilities.BusinessUtils.getAvailableTimeZoneIdsByPage;
import static Utilities.BusinessUtils.clearConsole;

import static java.time.temporal.ChronoUnit.*;

import static java.lang.Math.abs;
import static java.lang.System.out;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZoneTxt;

    public CalcDateTimeZoneController() {
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
        ZonedDateTime temp = (ZonedDateTime) model.getDateTimeZone();
        String ld = zoneDateTimeToString(temp);
        return ld;
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
        flowShowAllAvailableTimezones();
        out.print("Zona a escolher(S para Sair): ");
        String answerZone = Input.lerString();

        if (!answerZone.equals(("S"))) {
            model.changeZoneDateTimeToCurrentDateInZone(answerZone);
        }
    }

    private void flowShiftZoneDateTime() {
        String ld;
        Menu menu = viewZoneTxt.getMenu(1);
        String opcao;
        do {
            ld = buildZoneDateTimeTitle();
            menu.addDateTimeToTitle(ld);
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
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
        if (n >= 0)
            model.shiftDateTimeZone(abs(n), DAYS, ADD);
        else
            model.shiftDateTimeZone(abs(n), DAYS, SUB);
    }

    private void shiftWeeks() {
        out.print("Número de semanas: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeZone(abs(n), WEEKS, ADD);
        else
            model.shiftDateTimeZone(abs(n), WEEKS, SUB);
    }

    private void shiftMonths() {
        out.print("Número de meses: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeZone(abs(n), MONTHS, ADD);
        else
            model.shiftDateTimeZone(abs(n), MONTHS, SUB);
    }

    private void shiftYears() {
        out.print("Número de anos: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeZone(abs(n), YEARS, ADD);
        else
            model.shiftDateTimeZone(abs(n), YEARS, SUB);
    }


    //------------------------
    // FlowConvertZone
    //------------------------
    // Pedir para que zona queremos mudar a data
    private void flowConvertZone() {
        flowShowAllAvailableTimezones();
        out.print("Zona para qual converter(S para sair): ");
        String answerZone = Input.lerString();

        if (!answerZone.equals(("S"))) {
            model.convertZoneDateTimeToZone(answerZone);
        }
    }


    //------------------------
    // FlowShowAllAvailableTimeZones
    //------------------------
    // Buscar todos os ZoneIds alfabeticamente e fazer display por páginas
    private void flowShowAllAvailableTimezones() {
        List<List<String>> allZoneidsByPage = getAvailableTimeZoneIdsByPage(45);
        int pageIndex = 0;

        String opcao;
        do {
            clearConsole();
            allZoneidsByPage.get(pageIndex).forEach((String s) -> out.println("." + s));
            out.println("Pagina (" + (pageIndex+1) + "/" + allZoneidsByPage.size() + ")");
            out.println("(+) Proxima pagina (-) Previa pagina (S) Sair da listagem");
            opcao = Input.lerString().toUpperCase();
            switch(opcao) {
                case "+": if ((pageIndex + 1) < allZoneidsByPage.size()) { pageIndex++; } break;
                case "-": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case "S": break;
                default: out.println("Opcao Invalida!"); break;
            }

        } while(!opcao.equals("S"));
    }
}
