package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.ControllerUtils;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;

import static Utilities.Utils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.asList;

public class CalcDateTimeLocalController implements InterfCalcDateTimeController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeView viewLocalTxt;

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
    public void setView(InterfCalcDateTimeView viewLocal) {
        this.viewLocalTxt = viewLocal;
    }

    //------------------------
    // FlowLocal
    //------------------------
    @Override
    public void startFlow() {
        Menu menu;
        String ld;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        String prefix = "Data inicial: ";
        resetDateTimeLocal();
        do {
            ld = localDateTimeToString(model.getDateTimeLocal(), DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu = viewLocalTxt.getDynamicMenu(0,statusMessage,errorMessage,asList(BLUE_BOLD + prefix + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "C":
                    setDateTimeLocal();
                    statusMessage = "Data modificada com sucesso";
                    prefix = "Data inicial: ";
                    break;
                case "R":
                    resetDateTimeLocal();
                    statusMessage = "Data modificada com sucesso";
                    prefix = "Data inicial: ";
                    break;
                case "A":
                    prefix = flowShiftDateTime(prefix);
                    break;
                case "AU":
                    prefix = flowShiftWorkDaysDateTime(prefix);
                    break;
                case "D": flowDiffDateTime(); break;
                case "DU": flowDiffWorkDaysDateTime(); break;
                case "O":
                    prefix = flowGetDateTime(prefix);
                    break;
                case "?": help(); statusMessage = "n/a"; break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }
    private void help() {
        ZonedDateTime ld = (ZonedDateTime) model.getDateTimeLocal();
        String sld = localDateTimeToString(ld, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
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
                " ",
                BLACK_BOLD + "Opcao ?:" + RESET + " permite ao utilizador visualizar este menu.",
                " ",
                BLACK_BOLD + "Opcao S:" + RESET + " permite ao utilizador voltar ao Menu Principal.");
        flowHelp(viewLocalTxt.getMenu(1), l);
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
    // FlowResetDateTime
    //------------------------
    private void resetDateTimeLocal() {
        model.fromDateTimeLocal(getNowOfZone(model.getLocalZone()));
    }

    //------------------------
    // FlowShiftDateTime
    //------------------------
    private String flowShiftDateTime(String prefix) {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        Temporal ldt = model.getDateTimeLocal();
        String prefixTemp = prefix;
        do {
            ld = localDateTimeToString(ldt, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu = viewLocalTxt.getDynamicMenu(2,statusMessage,errorMessage,asList(BLUE_BOLD + prefixTemp + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "ANO" :
                    ldt = shiftDateTime(ldt, shift("anos"), YEARS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "MES" :
                    ldt = shiftDateTime(ldt, shift("meses"), MONTHS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "SEM" :
                    ldt = shiftDateTime(ldt, shift("semanas"), WEEKS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "DIA" :
                    ldt = shiftDateTime(ldt, shift("dias"), DAYS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "HOR" :
                    ldt = shiftDateTime(ldt, shift("horas"), HOURS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "MIN" :
                    ldt = shiftDateTime(ldt, shift("minutos"), MINUTES);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "SEG" :
                    ldt = shiftDateTime(ldt, shift("segundos"), SECONDS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "NAN" :
                    ldt = shiftDateTime(ldt, shift("nanosegundos"), NANOS);
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "G":
                    model.fromDateTimeLocal((ZonedDateTime) ldt);
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
    private String flowShiftWorkDaysDateTime(String prefix) {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        Temporal ldt = model.getDateTimeLocal();
        String prefixTemp = prefix;
        do {
            ld = localDateTimeToString(ldt, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu= viewLocalTxt.getDynamicMenu(3, statusMessage, errorMessage, asList(BLUE_BOLD + prefixTemp + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "A" :
                    ldt = shiftWorkDays(ldt, shift("dias uteis"));
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "G":
                    model.fromDateTimeLocal((ZonedDateTime) ldt);
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
    // FlowDiffDateTime
    //------------------------
    private void flowDiffDateTime() {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        ZonedDateTime ldt = (ZonedDateTime) model.getDateTimeLocal();
        do {
            ld = localDateTimeToString(ldt, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu = viewLocalTxt.getDynamicMenu(4,statusMessage,errorMessage,asList(BLUE_BOLD + "Data inicio: " + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" :
                    ldt = getDateTimeFromInput(ldt, ldt.getZone());
                    statusMessage = "Data de inicio modificada com sucesso";
                    break;
                case "F" : diffDateTimeLocal(ldt); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }
    private void diffDateTimeLocal(ZonedDateTime start) {
        ZonedDateTime stop = getDateTimeFromInput(start, start.getZone());
        String resDiff = diffBetweenDateTime(start, stop);

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    //------------------------
    // FlowDiffWorkDaysDateTime
    //------------------------
    private void flowDiffWorkDaysDateTime() {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        ZonedDateTime ldt = (ZonedDateTime) model.getDateTimeLocal();
        do {
            ld = localDateTimeToString(ldt, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu = viewLocalTxt.getDynamicMenu(4,statusMessage,errorMessage,asList(BLUE_BOLD + "Data inicio: " + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" :
                    ldt = getDateTimeFromInput(ldt, ldt.getZone());
                    statusMessage = "Data de inicio modificada com sucesso";
                    break;
                case "F" : diffWorkDaysDateTime(ldt); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }
    private void diffWorkDaysDateTime(ZonedDateTime start) {
        ZonedDateTime stop = getDateTimeFromInput(start, start.getZone());
        String resDiff = countWorkDays(start, stop) + " dias úteis";

        out.println(GREEN_BOLD + "\nResultado: " + resDiff + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
    }

    //------------------------
    // FlowGetDateTime
    //------------------------
    private String flowGetDateTime(String prefix) {
        String ld;
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        Temporal ldt = model.getDateTimeLocal();
        String prefixTemp = prefix;
        do {
            ld = localDateTimeToString(ldt, DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat()));
            menu = viewLocalTxt.getDynamicMenu(5,statusMessage,errorMessage,asList(BLUE_BOLD + prefixTemp + ld + RESET));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "O" :
                    ldt = getDateTimeLocal();
                    statusMessage = "Data modificada com sucesso";
                    prefixTemp = "Data acumulada: ";
                    break;
                case "?": helpOpcaoO(); break;
                case "G":
                    model.fromDateTimeLocal((ZonedDateTime) ldt);
                    prefix = prefixTemp;
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }

        }
        while(!(opcao.equals("S") || opcao.equals("G")));
        return prefix;
    }
    private ZonedDateTime getDateTimeLocal() {
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
                out.print("Mes do ano (1, 2, ...): ");
                str = Input.lerString();
                month = validateMonth(str, null);
                if (month == null)
                    out.println(RED_BOLD + "[!] Mes invalido." + RESET);
            }
            if (month != null) {
                zdt = zdt.withMonth(month);
                while (nweeks == null) {
                    out.print("Semana do mes (1, 2, ...): ");
                    str = Input.lerString();
                    nweeks = validateNumWeek(str, -1, year, month);
                    if (nweeks == null)
                        out.println(RED_BOLD + "[!] Numero da semana invalido." + RESET);
                    else if (nweeks == -1) {
                        // Imprime o mês inteiro
                        nweeks = null;
                        zdt.query(ControllerUtils::printMonth);
                    }
                }
                zdt = (ZonedDateTime) nextMondayN(zdt, nweeks-1);
                while (ndays == null) {
                    out.print("Dia da semana (1, 2, ...): ");
                    str = Input.lerString();
                    ndays = validateNumDay(str, -1, year, month, nweeks);
                    if (ndays == null)
                        out.println(RED_BOLD + "[!] Numero do dia invalido." + RESET);
                    else if (ndays == -1) {
                        ndays = null;
                        zdt.query(ControllerUtils::printWeek);
                    }
                }
                //Imprime a data do dia em questão
                zdt = (ZonedDateTime) nextDayN(zdt, ndays-1); // -1 porque o atual conta
            }
        }
        return zdt;
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
                " ",
                "Isto porque o mes 11 de 2018 esta dividido nas seguintes",
                "semanas:",
                BLACK_BOLD + "                novembro 2018   ",
                "            se te qu qu se sa do",
                "                      1  2  3  4 (Semana 1)",
                "             5  6  7  8  9 10 11 (Semana 2)",
                "            12 13 14 15 16 17 18 (Semana 3)",
                "            19 20 21 22 23 24 25 (Semana 4)",
                "            26 27 28 29 30       (Semana 5)" + RESET,
                " ",
                "Desta forma, a quinta semana contem os seguintes dias:",
                BLACK_BOLD + "                novembro 2018   ",
                "            se te qu qu se sa do",
                "            26 27 28 29 30      " + RESET,
                "O primeiro dia da semana e o 26. O segundo dia e o 27. O",
                "terceiro e o 28 e assim sucessivamente.",
                " ",
                "Por fim, importa referir que caso seja introduzida uma",
                BLACK_BOLD + "semana em branco" + RESET + " e apresentado o " + BLACK_BOLD + "mes completo" + RESET + " no formato",
                "apresentado a cima. O mesmo acontece para a semana, caso",
                "o " + BLACK_BOLD + "dia" + RESET + " seja inserido em" + BLACK_BOLD + " branco" + RESET + " e apresentada a " + BLACK_BOLD + "semana ",
                "inteira" + RESET + " no formato apresentado em cima.");
        flowHelp(viewLocalTxt.getMenu(1), l);
    }
}
