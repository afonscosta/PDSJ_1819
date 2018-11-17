package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeLocalView;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;

import static Utilities.BusinessUtils.*;
import static Utilities.EnumDateTimeShiftMode.ADD;
import static Utilities.EnumDateTimeShiftMode.SUB;
import static java.lang.Math.abs;
import static java.lang.System.out;
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
            menu.addDescToTitle(Arrays.asList("Data: " + ld));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "A" : flowShiftDateTime(); break;
                case "AU" : flowShiftWorkDaysDateTime(); break;
                case "D" : flowDiffDateTime(); break;
                case "DU" : flowDiffWorkDaysDateTime(); break;
                case "O" : getDateTimeLocal(); break;
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
            menu.addDescToTitle(Arrays.asList("Data: " + ld));
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
        out.print("(+|-) número de semanas: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), WEEKS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), WEEKS, SUB);
    }

    private void shiftMonths() {
        out.print("(+|-) número de meses: ");
        int n = Input.lerInt();
        if (n >= 0)
            model.shiftDateTimeLocal(abs(n), MONTHS, ADD);
        else
            model.shiftDateTimeLocal(abs(n), MONTHS, SUB);
    }

    private void shiftYears() {
        out.print("(+|-) número de anos: ");
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
            menu.addDescToTitle(Arrays.asList("Data: " + ld));
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
            menu.addDescToTitle(Arrays.asList("Data inicial: " + ld));
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
    // FlowGetDateTime
    //------------------------
    private void getDateTimeLocal() {
        Integer year = null;
        Integer month = null;
        Integer nweeks = null;
        Integer ndays = null;
        String str;

        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.withDayOfMonth(1);
        ldt = ldt.withHour(0);
        ldt = ldt.withMinute(0);
        ldt = ldt.withSecond(0);
        ldt = ldt.withNano(0);

        while (year == null) {
            out.print("Ano: ");
            str = Input.lerString();
            year = validatePosNumber(str, null);
            if (year == null)
                out.println("[!] Ano invalido.");
        }
        if (year != null) {
            ldt = ldt.withYear(year);
            while (month == null) {
                out.print("Mes: ");
                str = Input.lerString();
                month = validateMonth(str, null);
                if (month == null)
                    out.println("[!] Mes invalido.");
            }
            if (month != null) {
                ldt = ldt.withMonth(month);
                while (nweeks == null) {
                    out.print("Semana: ");
                    str = Input.lerString();
                    nweeks = validateNumWeek(str, -1, year, month);
                    if (nweeks == null)
                        out.println("[!] Numero da semana invalido.");
                }
                if (nweeks != -1) {
                    ldt = (LocalDateTime) nextMondayN(ldt, nweeks-1);
                    while (ndays == null) {
                        out.print("Dia: ");
                        str = Input.lerString();
                        ndays = validateNumDay(str, -1, year, month, nweeks);
                        if (ndays == null)
                            out.println("[!] Numero do dia invalido.");
                    }
                    if (ndays != -1) {
                        //Imprime a data do dia em questão
                        ldt = (LocalDateTime) nextDayN(ldt, ndays-1); // -1 porque o atual conta
                        model.fromDateTimeLocal(ldt);
                        out.println(localDateToString(ldt));
                    }
                    else {
                        // Imprime semana inteira
                        printWeek(ldt);
                    }
                } else {
                    // Imprime o mês inteiro
                    printMonth(ldt);
                }
            }
        }
        out.print("Prima Enter para continuar.");
        String dummy = Input.lerString();
    }

    /*
     * Faz print do header de um dado mês
     *             outubro
     *      se te qu qu se sá do
     */
    private void printHeader(int month) {
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
    private void printMonth(LocalDateTime ldt) {
        printHeader(ldt.getMonthValue());
        LocalDateTime start = LocalDateTime.from(ldt);
        while(start.getMonthValue() == ldt.getMonthValue()) {
            out.println(organizeDays(ldt));
            ldt = (LocalDateTime) nextMondayN(ldt, 1);
        }
        out.println();
    }

    /*
     * Faz print da semana no formato:
     *            outubro
     *     se te qu qu se sá do
     *      1  2  3  4  5  6  7
     */
    private void printWeek(LocalDateTime ldt) {
        printHeader(ldt.getMonthValue());
        out.println(organizeDays(ldt));
        out.println();
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

        LocalDateTime newLDT = LocalDateTime.of(year, month, day, hour, minute, second, nano);
        return newLDT;
    }



}
