package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import static Enum.EnumDateTimeShiftMode.ADD;
import static Enum.EnumDateTimeShiftMode.SUB;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeLocalView;
import View.Menu;

import static Utilities.BusinessUtils.clearConsole;
import static java.lang.Math.abs;
import java.time.ZonedDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;

public class CalcDateTimeLocalController implements InterfCalcDateTimeLocalController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeLocalView viewLocalTxt;

    public CalcDateTimeLocalController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;

    }

    @Override
    public void setView(InterfCalcDateTimeLocalView viewLocal) {
        this.viewLocalTxt = viewLocal;

    }

    @Override
    public void flowLocal() {
        // Início do fluxo de execução
        Menu menu = viewLocalTxt.getMenu(0);
        String ld;
        String opcao;
        do {
            ld = buildDateTimeTitle();
            menu.addDateTimeToTitle(ld);
            clearConsole();
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "A" : flowShiftDateTime(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private String buildDateTimeTitle() {
        ZonedDateTime zld = model.getZonedDateTime();
        String ld = zld.getDayOfMonth() + "/" +
            zld.getMonth() + "/" +
            zld.getYear() + "  " +
            zld.getHour() + ":" +
            zld.getMinute() + ":" +
            zld.getSecond();
        return ld;
    }

    // Apresentar opções relativas a somar ou subtrair espaço de tempo a uma data
    private void flowShiftDateTime() {
        String ld = null;
        Menu menu = viewLocalTxt.getMenu(1);
        String opcao;
        do {
            ld = buildDateTimeTitle();
            menu.addDateTimeToTitle(ld);
            clearConsole();
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "DIA" : shiftDays(); break;
                case "SEM" : shiftWeeks(); break;
                case "MES" : shiftMonths(); break;
                case "ANO" : shiftYears(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void shiftDays() {
        System.out.print("(+|-) número de dias: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTime(abs(n), DAYS, ADD);
        else
            model.shiftDateTime(abs(n), DAYS, SUB);
    }

    private void shiftWeeks() {
        System.out.print("Número de semanas: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTime(abs(n), WEEKS, ADD);
        else
            model.shiftDateTime(abs(n), WEEKS, SUB);
    }

    private void shiftMonths() {
        System.out.print("Número de meses: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTime(abs(n), MONTHS, ADD);
        else
            model.shiftDateTime(abs(n), MONTHS, SUB);
    }

    private void shiftYears() {
        System.out.print("Número de anos: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTime(abs(n), YEARS, ADD);
        else
            model.shiftDateTime(abs(n), YEARS, SUB);
    }
}
