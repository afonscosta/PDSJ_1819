package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeLocalView;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.asList;

public class CalcDateTimeLocalController implements InterfCalcDateTimeLocalController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeLocalView viewLocalTxt;

    public static CalcDateTimeLocalController of() {
        return new CalcDateTimeLocalController();
    }

    private CalcDateTimeLocalController () {

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
    public void withZone(String zid) {
        model.withZoneLocal(zid);
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
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = localDateTimeToString(model.getDateTimeLocal(), DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu.addDescToTitle(asList("Data: " + ld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "C": setDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "R": resetDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "A": flowShiftDateTime(); break;
                case "AU": flowShiftWorkDaysDateTime(); break;
                case "D": flowDiffDateTime(); break;
                case "DU": flowDiffWorkDaysDateTime(); break;
                case "O": getDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "?": help(); statusMessage = "n/a"; break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void resetDateTimeLocal() {
        model.fromDateTimeLocal(getNowOfZone(model.getLocalZone()));
    }

    private void help() {
        ZonedDateTime ld = (ZonedDateTime) model.getDateTimeLocal();
        String sld = localDateTimeToString(ld, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
        Menu menu = viewLocalTxt.getMenu(1);
        List<String> l = asList(
            RED_BOLD + "Data: " + sld + RESET,
            BLACK_BOLD + "^^^^" + RESET + " - A data presente no registo e usada por omissao.",
            "       No final de cada calculo o registo e atualizado.",
            " ",
            BLACK_BOLD + "Opcao C:" + RESET + " permite ao utilizador" + BLUE_BOLD + " alterar a data ",
            "         que se encontra no registo." + RESET,
            " ",
            BLACK_BOLD + "Opcao R:" + RESET + " permite ao utilizador" + BLUE_BOLD + " alterar a data ",
            "         que se encontra no registo" + RESET + " para a data ",
            "         atual tendo em conta o fuso local.",
            " ",
            BLACK_BOLD + "Opcao A:" + RESET + " permite ao utilizador somar ou subtrair ",
            "         anos, meses, semanas, dias, horas, minutos, ",
            "         segundos ou nanosegundos, a " + BLUE_BOLD + "data que se ",
            "         encontra no registo." + RESET,
            " ",
            BLACK_BOLD + "Opcao AU:" + RESET + " permite ao utilizador somar ou subtrair ",
            "          dias uteis a " + BLUE_BOLD + "data que se encontra no registo." + RESET,
            " ",
            BLACK_BOLD + "Opcao D:" + RESET + " permite ao utilizador realizar a diferenca ",
            "         entre datas.",
            " ",
            BLACK_BOLD + "Opcao DU:" + RESET + " permite ao utilizador realizar a diferenca, ",
            "          em dias uteis, entre datas. A data inicial não ",
            "          entra para os calculos.",
            " ",
            BLACK_BOLD + "Opcao O:" + RESET + " permite ao utilizador saber a data dando um ano, ",
            "         mes, numero da semana nesse mes e numero do dia ",
            "         nessa semana.",
            BLUE_BOLD + "         Altera a data que se encontra no registo!" + RESET,
            " ",
            BLACK_BOLD + "Opcao ?:" + RESET + " permite ao utilizador visualizar este menu.",
            " ",
            BLACK_BOLD + "Opcao S:" + RESET + " permite ao utilizador voltar ao Menu Principal.");

        String opcao;
        String errorMessage = "n/a";
        do {
            menu.addDescToTitle(l);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "O": helpOpcaoO();
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void helpOpcaoO() {
        List<String> l = asList(
            "A opcao 'O' permite ao utilizador saber a data dando um",
            "ano, mes, numero da semana nesse mes e numero do dia nessa",
            "semana.",
            " ",
            "Assim sendo, caso o utilizador insira as seguintes opcoes:",
            "   Ano: 2018",
            "   Mes: 11",
            "   Semana: 5",
            "   Dia: 3",
            "Obtem-se como resultado: o dia 28 de novembro de 2018.",
            "Este, " + RED_BOLD + "substituiu o valor que se encontra no registo da ",
            "data" + RESET + " na seccao superior do menu da calculadora.",
            " ",
            "Isto porque o mes 11 de 2018 esta dividido nas seguintes",
            "semanas:",
            BLACK_BOLD + "                novembro 2018   ",
            "            se te qu qu se sá do",
            "                      1  2  3  4 (Semana 1)",
            "             5  6  7  8  9 10 11 (Semana 2)",
            "            12 13 14 15 16 17 18 (Semana 3)",
            "            19 20 21 22 23 24 25 (Semana 4)",
            "            26 27 28 29 30       (Semana 5)" + RESET,
            " ",
            "Desta forma, a quinta semana contem os seguintes dias:",
            BLACK_BOLD + "                novembro 2018   ",
            "            se te qu qu se sá do",
            "            26 27 28 29 30      " + RESET,
            "O primeiro dia da semana é o 26. O segundo dia é o 27. O",
            "terceiro é o 28 e assim sucessivamente.",
            " ",
            "Por fim, importa referir que casa seja introduzida uma",
            BLACK_BOLD + "semana em branco" + RESET + " é apresentado o " + BLACK_BOLD + "mes completo" + RESET + " no forma",
            "apresentado a cima. O mesmo acontece para a semana, caso",
            "o " + BLACK_BOLD + "dia" + RESET + " seja inserido em" + BLACK_BOLD + " branco" + RESET + " é apresentada a " + BLACK_BOLD + "semana ",
            "inteira" + RESET + " no formato apresentado em cima.");
        flowHelp(viewLocalTxt.getMenu(2), l);
    }

    //------------------------
    // FlowSetDateTime
    //------------------------
    private void setDateTimeLocal() {
        ZonedDateTime currentLDT = (ZonedDateTime) model.getDateTimeLocal();
        ZonedDateTime ldt = getDateTimeFromInput(currentLDT, currentLDT.getZone());
        model.fromDateTimeLocal(ldt);
    }

    //------------------------
    // FlowShiftDateTime
    //------------------------
    private void flowShiftDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(3);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = localDateTimeToString(model.getDateTimeLocal(), DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu.addDescToTitle(asList("Data: " + ld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "ANO" :
                    model.shiftDateTimeLocal(shift("anos"), YEARS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "MES" :
                    model.shiftDateTimeLocal(shift("meses"), MONTHS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "SEM" :
                    model.shiftDateTimeLocal(shift("semanas"), WEEKS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "DIA" :
                    model.shiftDateTimeLocal(shift("dias"), DAYS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "HOR" :
                    model.shiftDateTimeLocal(shift("horas"), HOURS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "MIN" :
                    model.shiftDateTimeLocal(shift("minutos"), MINUTES);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "SEG" :
                    model.shiftDateTimeLocal(shift("segundos"), SECONDS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "NAN" :
                    model.shiftDateTimeLocal(shift("nanosegundos"), NANOS);
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    //------------------------
    // FlowShiftWorkDaysDateTime
    //------------------------
    private void flowShiftWorkDaysDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(4);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = localDateTimeToString(model.getDateTimeLocal(), DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu.addDescToTitle(asList("Data: " + ld));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "A" :
                    model.shiftWorkDaysDateTimeLocal(shift("dias uteis"));
                    statusMessage = "Data modificada com sucesso";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }

        }
        while(!opcao.equals("S"));
    }

    //------------------------
    // FlowDiffDateTime
    //------------------------
    private void flowDiffDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(5);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = localDateTimeToString(model.getDateTimeLocal(), DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.addDescToTitle(asList("Data inicial: " + ld));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "F" : diffDateTimeLocal(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    //------------------------
    // FlowFromDateTime
    //------------------------
    private void fromDateTimeLocal() {
        ZonedDateTime newLDT = null;
        while(newLDT == null) {
            ZonedDateTime currentLDT = (ZonedDateTime) model.getDateTimeLocal();
            newLDT = getDateTimeFromInput(currentLDT, currentLDT.getZone());
        }
        model.fromDateTimeLocal(newLDT);
    }


    private void diffDateTimeLocal() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            ZonedDateTime currentLDT = (ZonedDateTime) model.getDateTimeLocal();
            toDateTime = getDateTimeFromInput(currentLDT, currentLDT.getZone());
        }
        String resDiff = model.diffDateTimeLocal(toDateTime);

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    //------------------------
    // FlowDiffWorkDaysDateTime
    //------------------------
    private void flowDiffWorkDaysDateTime() {
        String ld;
        Menu menu = viewLocalTxt.getMenu(5);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            ld = localDateTimeToString(model.getDateTimeLocal(), DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.addDescToTitle(asList("Data inicial: " + ld));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : fromDateTimeLocal(); statusMessage = "Data modificada com sucesso"; break;
                case "F" : diffWorkDaysDateTime(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void diffWorkDaysDateTime() {
        ZonedDateTime toDateTime = null;
        while(toDateTime == null) {
            ZonedDateTime currentLDT = (ZonedDateTime) model.getDateTimeLocal();
            toDateTime = getDateTimeFromInput(currentLDT, currentLDT.getZone());
        }
        String resDiff = model.diffWorkDaysDateTimeLocal(toDateTime);

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
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

        ZonedDateTime zdt = ZonedDateTime.now();
        zdt = zdt.withDayOfMonth(1);
        zdt = zdt.withHour(0);
        zdt = zdt.withMinute(0);
        zdt = zdt.withSecond(0);
        zdt = zdt.withNano(0);

        while (year == null) {
            out.print("Ano: ");
            str = Input.lerString();
            year = validatePosNumber(str, null);
            if (year == null)
                out.println(RED_BOLD + "[!] Ano invalido." + RESET);
        }
        if (year != null) {
            zdt = zdt.withYear(year);
            while (month == null) {
                out.print("Mes do ano [1,2,..]: ");
                str = Input.lerString();
                month = validateMonth(str, null);
                if (month == null)
                    out.println(RED_BOLD + "[!] Mes invalido." + RESET);
            }
            if (month != null) {
                zdt = zdt.withMonth(month);
                while (nweeks == null) {
                    out.print("Semana do mes [1,2,..]: ");
                    str = Input.lerString();
                    nweeks = validateNumWeek(str, -1, year, month);
                    if (nweeks == null)
                        out.println(RED_BOLD + "[!] Numero da semana invalido." + RESET);
                    else if (nweeks == -1) {
                        // Imprime o mês inteiro
                        nweeks = null;
                        printMonth(zdt);
                    }
                }
                zdt = (ZonedDateTime) nextMondayN(zdt, nweeks-1);
                while (ndays == null) {
                    out.print("Dia da semana [1,2..]: ");
                    str = Input.lerString();
                    ndays = validateNumDay(str, -1, year, month, nweeks);
                    if (ndays == null)
                        out.println(RED_BOLD + "[!] Numero do dia invalido." + RESET);
                    else if (ndays == -1) {
                        ndays = null;
                        printWeek(zdt);
                    }
                }
                //Imprime a data do dia em questão
                zdt = (ZonedDateTime) nextDayN(zdt, ndays-1); // -1 porque o atual conta
                model.fromDateTimeLocal(zdt);
                out.println("\n" + GREEN_BOLD + "Resultado: " + localDateToString(zdt) + RESET);
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
    private void printMonth(ZonedDateTime zdt) {
        printHeader(zdt.getMonthValue());
        LocalDateTime start = LocalDateTime.from(zdt);
        while(start.getMonthValue() == zdt.getMonthValue()) {
            out.println(organizeDays(zdt));
            zdt = (ZonedDateTime) nextMondayN(zdt, 1);
        }
        out.println();
    }

    /*
     * Faz print da semana no formato:
     *            outubro
     *     se te qu qu se sá do
     *      1  2  3  4  5  6  7
     */
    private void printWeek(ZonedDateTime zdt) {
        printHeader(zdt.getMonthValue());
        out.println(organizeDays(zdt));
        out.println();
    }
}
