package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeLocalView;
import View.Menu;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

import static Enum.EnumDateTimeShiftMode.ADD;
import static Enum.EnumDateTimeShiftMode.SUB;
import static Utilities.BusinessUtils.clearConsole;
import static Utilities.BusinessUtils.localDateTimeToString;
import static java.lang.Math.abs;
import static java.time.temporal.ChronoUnit.*;

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

    private String buildDateTimeTitle() {
        Temporal temp = model.getDateTimeLocal();
        String ld = localDateTimeToString(temp);
        return ld;
    }

    //------------------------
    // FlowLocal
    //------------------------
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
                case "AU" : flowShiftWorkDaysDateTime(); break;
                case "D" : flowDiffDateTime(); break;
                case "DU" : flowDiffWorkDaysDateTime(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }


    //------------------------
    // FlowShiftDateTime
    //------------------------
    // Apresentar opções relativas a somar ou subtrair espaço de tempo a uma data
    private void flowShiftDateTime() {
        String ld;
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
            model.shiftDateTimeLocal(abs(n), DAYS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), DAYS, SUB);
    }

    private void shiftWeeks() {
        System.out.print("Número de semanas: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), WEEKS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), WEEKS, SUB);
    }

    private void shiftMonths() {
        System.out.print("Número de meses: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), MONTHS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), MONTHS, SUB);
    }

    private void shiftYears() {
        System.out.print("Número de anos: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), YEARS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), YEARS, SUB);
    }

    //------------------------
    // FlowShiftWorkDaysDateTime
    //------------------------
    private void flowShiftWorkDaysDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(2);
        String opcao;
        do {
            ld = buildDateTimeTitle();
            menu.addDateTimeToTitle(ld);
            clearConsole();
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "DIA" : shiftWorkDays(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void shiftWorkDays() {
        System.out.println("Funcionalidade ShiftWorkDays em desenvolvimento! (Enter para continuar)");
        String str = Input.lerString();
    }

    //------------------------
    // FlowDiffDateTime
    //------------------------
    private void flowDiffDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(3);
        String opcao;
        do {
            ld = buildDateTimeTitle();
            menu.addDateTimeToTitle(ld);
            clearConsole();
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); break;
                case "F" : calcDiffDateTime(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void fromDateTimeLocal() {
        LocalDateTime ldt = (LocalDateTime) model.getDateTimeLocal();

        System.out.print("Ano (default: " + ldt.getYear() + "): ");
        int year = getIntFromInput(ldt.getYear());

        System.out.print("Mês (default: " + ldt.getMonthValue() + "): ");
        int month = getIntFromInput(ldt.getMonthValue());

        System.out.print("Dia (default: " + ldt.getDayOfMonth() + "): ");
        int day = getIntFromInput(ldt.getDayOfMonth());

        System.out.print("Hora (default: " + ldt.getHour() + "): ");
        int hour = getIntFromInput(ldt.getHour());

        System.out.print("Minutos (default: " + ldt.getMinute() + "): ");
        int minute = getIntFromInput(ldt.getMinute());

        System.out.print("Segundos (default: " + ldt.getSecond() + "): ");
        int second = getIntFromInput(ldt.getSecond());

        System.out.print("Nanosegundos (default: " + ldt.getNano() + "): ");
        int nano = getIntFromInput(ldt.getNano());

        LocalDateTime newLDT = LocalDateTime.of(year, month, day, hour, minute, second, nano);
        model.fromDateTimeLocal(newLDT);
    }

    private void calcDiffDateTime() {
        LocalDateTime ldt = (LocalDateTime) model.getDateTimeLocal();

        System.out.print("Ano (default: " + ldt.getYear() + "): ");
        int year = getIntFromInput(ldt.getYear());

        System.out.print("Mês (default: " + ldt.getMonthValue() + "): ");
        int month = getIntFromInput(ldt.getMonthValue());

        System.out.print("Dia (default: " + ldt.getDayOfMonth() + "): ");
        int day = getIntFromInput(ldt.getDayOfMonth());

        System.out.print("Hora (default: " + ldt.getHour() + "): ");
        int hour = getIntFromInput(ldt.getHour());

        System.out.print("Minutos (default: " + ldt.getMinute() + "): ");
        int minute = getIntFromInput(ldt.getMinute());

        System.out.print("Segundos (default: " + ldt.getSecond() + "): ");
        int second = getIntFromInput(ldt.getSecond());

        System.out.print("Nanosegundos (default: " + ldt.getNano() + "): ");
        int nano = getIntFromInput(ldt.getNano());

        LocalDateTime toDateTime = LocalDateTime.of(year, month, day, hour, minute, second, nano);
        String resDiff = model.diffDateTimeLocal(toDateTime);

        System.out.println("Resultado: " + resDiff);
        System.out.print("Prima Enter para continuar.");
        String str = Input.lerString();
    }

    //------------------------
    // FlowDiffWorkDaysDateTime
    //------------------------
    private void flowDiffWorkDaysDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(3);
        String opcao;
        do {
            ld = buildDateTimeTitle();
            menu.addDateTimeToTitle(ld);
            clearConsole();
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); break;
                case "F" : calcDiffWorkDaysDateTime(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void calcDiffWorkDaysDateTime() {
        System.out.println("Funcionalidade DiffWorkDays em desenvolvimento! (Enter para continuar)");
        String str = Input.lerString();
    }

    // Pede ao utilizador uma string.
    // Se a string for vazia então devolve o valor passado como argumento.
    private int getIntFromInput(int def) {
        String str = Input.lerString();
        int num = def;
        if (!str.isEmpty())
            num = Integer.parseInt(str);
        return num;
    }
}
