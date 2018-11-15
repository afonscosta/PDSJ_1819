package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeLocalView;
import Utilities.Menu;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

import static Utilities.EnumDateTimeShiftMode.ADD;
import static Utilities.EnumDateTimeShiftMode.SUB;
import static Utilities.BusinessUtils.localDateTimeToString;
import static java.lang.Math.abs;
import static java.time.temporal.ChronoUnit.*;
import static java.lang.System.out;

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
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "A" : flowShiftDateTime(); break;
                case "AU" : flowShiftWorkDaysDateTime(); break;
                case "D" : flowDiffDateTime(); break;
                case "DU" : flowDiffWorkDaysDateTime(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
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
            model.shiftDateTimeLocal(abs(n), DAYS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), DAYS, SUB);
    }

    private void shiftWeeks() {
        out.print("Número de semanas: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), WEEKS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), WEEKS, SUB);
    }

    private void shiftMonths() {
        out.print("Número de meses: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), MONTHS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), MONTHS, SUB);
    }

    private void shiftYears() {
        out.print("Número de anos: ");
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
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "DIA" : shiftWorkDays(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void shiftWorkDays() {
        out.print("(+|-) número de dias: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftWorkDaysDateTimeLocal(abs(n), ADD);
        else
            model.shiftWorkDaysDateTimeLocal(abs(n), SUB);
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
        LocalDateTime newLDT = null;
        while(newLDT == null) {
            newLDT = getLocalDateTimeFromInput();
        }
        model.fromDateTimeLocal(newLDT);
    }


    private void diffDateTimeLocal() {
        LocalDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getLocalDateTimeFromInput();
        }
        String resDiff = model.diffDateTimeLocal(toDateTime);

        out.println("\nResultado: " + resDiff);
        out.print("Prima Enter para continuar.");
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
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); break;
                case "F" : diffWorkDaysDateTime(); break;
                case "S": break;
                default: out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void diffWorkDaysDateTime() {
        LocalDateTime toDateTime = null;
        while(toDateTime == null) {
             toDateTime = getLocalDateTimeFromInput();
        }
        String resDiff = model.diffWorkDaysDateTimeLocal(toDateTime);

        out.println("\nResultado: " + resDiff);
        out.print("Prima Enter para continuar.");
        String str = Input.lerString();
    }


    //------------------------
    // Métodos adicionais
    //------------------------

    // Pede ao utilizador uma string.
    // Se a string for vazia então devolve o valor passado como argumento.
    private int getIntFromInput(int def) {
        String str = Input.lerString();
        int num = def;
        if (!str.isEmpty())
            num = Integer.parseInt(str);
        return num;
    }

    // Lê um inteiro positivo (0 inclusive)
    private Integer getPosNumberFromInput(int def) {
        String str = Input.lerString();
        Integer num = null;
        if (str.matches("^\\d+$")) {
            num = Integer.parseInt(str);
        }
        if (str.isEmpty())
            num = def;
        return num;
    }

    // Lê um valor entre 1 e 12
    private Integer getMonthFromInput(int def) {
        String str = Input.lerString();
        Integer num = null;
        if (str.matches("^([1-9]|1[0-2])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty())
            num = def;
        return num;
    }

    // Lê um dia que esteja presente num dado ano e mês.
    private Integer getDayFromInput(int def, int year, int month) {
        String str = Input.lerString();
        int end = YearMonth.of(year, month).lengthOfMonth();
        Integer num = null;
        if (str.matches("^([1-9]|1[0-9]|2[0-9]|3[01])$")) {
            num = Integer.parseInt(str);
            if (num > end) // O dia escolhido não existe no mês
                num = null;
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    // Lê um valor entre 0 e 23
    private Integer getHourFromInput(int def) {
        String str = Input.lerString();
        Integer num = null;
        if (str.matches("^([0-9]|1[0-9]|2[0-3])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    // Lê um valor entre 0 e 59
    // Devolve def caso seja lida a string vazia
    // Devolve null caso tenha lido
    private Integer getMinSecFromInput(int def) {
        String str = Input.lerString();
        Integer num = null;
        if (str.matches("^([0-9]|[1-5][0-9])$")) {
            num = Integer.parseInt(str);
        }
        else if (str.isEmpty()) {
            num = def;
        }
        return num;
    }

    // Pede ao utilizador uma data.
    // Se não disser nada fica a que se encontra no modelLocal.
    // Devolve null caso dê uma exceção.
    private LocalDateTime getLocalDateTimeFromInput() {
        LocalDateTime ldt = (LocalDateTime) model.getDateTimeLocal();
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;

        while (year == null) {
            out.print("Ano (default: " + ldt.getYear() + "): ");
            year = getPosNumberFromInput(ldt.getYear());
            if (year == null)
                out.println("[!] Ano invalido.");
        }

        while (month == null) {
            out.print("Mês (default: " + ldt.getMonthValue() + "): ");
            month = getMonthFromInput(ldt.getMonthValue());
        }

        while (day == null) {
            out.print("Dia (default: " + ldt.getDayOfMonth() + "): ");
            day = getDayFromInput(ldt.getDayOfMonth(), year, month);
        }

        while (hour == null) {
            out.print("Hora (default: " + ldt.getHour() + "): ");
            hour = getHourFromInput(ldt.getHour());
        }

        while (minute == null) {
            out.print("Minutos (default: " + ldt.getMinute() + "): ");
            minute = getMinSecFromInput(ldt.getMinute());
        }

        while (second == null) {
            out.print("Segundos (default: " + ldt.getSecond() + "): ");
            second = getMinSecFromInput(ldt.getSecond());
        }

        while (nano == null) {
            out.print("Nanosegundos (default: " + ldt.getNano() + "): ");
            nano = getPosNumberFromInput(ldt.getNano());
        }

        LocalDateTime newLDT = LocalDateTime.of(year, month, day, hour, minute, second, nano);
        return newLDT;
    }



}
