package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeLocalView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;

import static Utilities.BusinessUtils.*;
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
                case "A" : setDateTimeLocal(); break;
                case "M" : flowShiftDateTime(); break;
                case "MU" : flowShiftWorkDaysDateTime(); break;
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
    // FlowSetDateTime
    //------------------------
    private void setDateTimeLocal() {
        ZonedDateTime ldt = getLocalDateTimeFromInput();
        model.fromDateTimeLocal(ldt);
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
        model.shiftDateTimeLocal(n, DAYS);
    }

    private void shiftWeeks() {
        out.print("(+|-) número de semanas: ");
        int n = Input.lerInt();
        model.shiftDateTimeLocal(n, WEEKS);
    }

    private void shiftMonths() {
        out.print("(+|-) número de meses: ");
        int n = Input.lerInt();
        model.shiftDateTimeLocal(n, MONTHS);
    }

    private void shiftYears() {
        out.print("(+|-) número de anos: ");
        int n = Input.lerInt();
        model.shiftDateTimeLocal(n, YEARS);
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
        model.shiftWorkDaysDateTimeLocal(n);
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
        ZonedDateTime newLDT = null;
        while(newLDT == null) {
            newLDT = getLocalDateTimeFromInput();
        }
        model.fromDateTimeLocal(newLDT);
    }


    private void diffDateTimeLocal() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            toDateTime = getLocalDateTimeFromInput();
        }
        String resDiff = model.diffDateTimeLocal(toDateTime);

        out.println("\nResultado: " + resDiff);
        out.print("Prima Enter para continuar.");
        Input.lerString();
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
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
             toDateTime = getLocalDateTimeFromInput();
        }
        String resDiff = model.diffWorkDaysDateTimeLocal(toDateTime);

        out.println("\nResultado: " + resDiff);
        out.print("Prima Enter para continuar.");
        Input.lerString();
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

        ZonedDateTime ldt = ZonedDateTime.now();
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
                    ldt = (ZonedDateTime) nextMondayN(ldt, nweeks-1);
                    while (ndays == null) {
                        out.print("Dia: ");
                        str = Input.lerString();
                        ndays = validateNumDay(str, -1, year, month, nweeks);
                        if (ndays == null)
                            out.println("[!] Numero do dia invalido.");
                    }
                    if (ndays != -1) {
                        //Imprime a data do dia em questão
                        ldt = (ZonedDateTime) nextDayN(ldt, ndays-1); // -1 porque o atual conta
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
        Input.lerString();
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
    private void printMonth(ZonedDateTime ldt) {
        printHeader(ldt.getMonthValue());
        ZonedDateTime start = ZonedDateTime.from(ldt);
        while(start.getMonthValue() == ldt.getMonthValue()) {
            out.println(organizeDays(ldt));
            ldt = (ZonedDateTime) nextMondayN(ldt, 1);
        }
        out.println();
    }

    /*
     * Faz print da semana no formato:
     *            outubro
     *     se te qu qu se sá do
     *      1  2  3  4  5  6  7
     */
    private void printWeek(ZonedDateTime ldt) {
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
    private ZonedDateTime getLocalDateTimeFromInput() {
        ZonedDateTime zdt = (ZonedDateTime) model.getDateTimeLocal();
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        String str;

        while (year == null) {
            out.print("Ano (default: " + zdt.getYear() + "): ");
            str = Input.lerString();
            year = validatePosNumber(str, zdt.getYear());
            if (year == null)
                out.println("[!] Ano invalido.");
        }

        while (month == null) {
            out.print("Mes (default: " + zdt.getMonthValue() + "): ");
            str = Input.lerString();
            month = validateMonth(str, zdt.getMonthValue());
            if (month == null)
                out.println("[!] Mes invalido.");
        }

        while (day == null) {
            out.print("Dia (default: " + zdt.getDayOfMonth() + "): ");
            str = Input.lerString();
            day = validateDay(str, zdt.getDayOfMonth(), year, month);
            if (day == null)
                out.println("[!] Dia invalido.");
        }

        while (hour == null) {
            out.print("Hora (default: " + zdt.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, zdt.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");
        }

        while (minute == null) {
            out.print("Minutos (default: " + zdt.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, zdt.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (default: " + zdt.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, zdt.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (default: " + zdt.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, zdt.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");
        }

        // Colocar o zone do ficheiro de configuração
        return ZonedDateTime.of(year, month, day, hour, minute, second, nano, ZoneId.systemDefault());
    }



}
