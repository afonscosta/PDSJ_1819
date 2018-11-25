package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.ZoneId;
import java.util.List;

import static Utilities.ConsoleColors.BLACK_BOLD;
import static Utilities.ConsoleColors.RESET;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.util.Arrays.asList;

public class CalcDateTimeController implements InterfCalcDateTimeController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeView viewMainTxt;
    private InterfCalcDateTimeLocalController controlLocal;
    private InterfCalcDateTimeZoneController controlZone;
    private InterfCalcDateTimeScheduleController controlSchedule;

    public static CalcDateTimeController of(InterfCalcDateTimeLocalController controlLocal,
                                            InterfCalcDateTimeZoneController controlZone,
                                            InterfCalcDateTimeScheduleController controlSchedule) {
        return new CalcDateTimeController(controlLocal, controlZone, controlSchedule);
    }

    private CalcDateTimeController(InterfCalcDateTimeLocalController controlLocal,
                                   InterfCalcDateTimeZoneController controlZone,
                                   InterfCalcDateTimeScheduleController controlSchedule) {
        this.controlLocal = controlLocal;
        this.controlZone = controlZone;
        this.controlSchedule = controlSchedule;
    }

    @Override
    public void setView(InterfCalcDateTimeView view) {
        this.viewMainTxt = view;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }


    //------------------------
    // Fluxo inicial do programa
    //------------------------
    @Override
    public void startFlow() {
        // Início do fluxo de execução
        Menu menu = viewMainTxt.getMenu(0);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : controlLocal.flowLocal(); break;
                case "Z" : controlZone.flowZone(); break;
                case "A" : controlSchedule.flowSchedule(); break;
                case "C" : flowConfig(); break;
                case "?" : helpMain(); break;
                case "S": controlSchedule.saveState(); break; //É aqui que se guarda os DateTimeFormatter's e o localZone
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    //------------------------
    // Menu de configuração
    //------------------------
    private void flowConfig() {
        Menu menu = viewMainTxt.getMenu(1);
        String opcao;
        Boolean configChanged = false;
        String statusMessage = "n/a" ;
        do {
            menu.addStatusMessage(statusMessage);
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "FL": flowSetDateFormatLocal(); configChanged = true; statusMessage = "Formato de apresentacao local modificado"; break;
                case "FF": flowSetDateFormatZoned(); configChanged = true; statusMessage = "Formato de apresentacao de datas com zonas modificado"; break;
                case "F": flowSetZone(); configChanged = true; statusMessage = "Fuso local modificado"; break;
                case "H": setSchedule(); configChanged = true; statusMessage = "Limitacoes de horario adicionadas"; break;
                case "?": helpConfig(); statusMessage = "n/a"; break;
                case "S": break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!opcao.equals("S"));

        if (configChanged) {
            model.saveConfigs();

        }
    }

    private void flowSetZone() {
        String zone = flowShowAllAvailableTimezonesAndGetNZoneIds(1,viewMainTxt.getMenu(2), ZoneId.systemDefault().toString()).get(0);
        model.withZoneLocal(zone);
        model.withZoneZone(zone);
    }

    private void flowSetDateFormatLocal(){
        Menu menu = viewMainTxt.getMenu(4);
        String opcao;
        boolean flowDone=false;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P": flowPreDefinedDateFormatLocal(); flowDone=true; break;
                case "M": model.setLocalDateTimeFormat(setDinamicDateFormatLocal()); flowDone=true; break;
                case "?": helpFormatLocal(); break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private void flowSetDateFormatZoned(){
        Menu menu = viewMainTxt.getMenu(4);
        String opcao;
        boolean flowDone = false;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P": flowPreDefinedDateFormatZoned(); flowDone=true; break;
                case "M": model.setLocalDateTimeFormat(setDinamicDateFormatZoned()); flowDone=true; break;
                case "?": helpFormatZone(); break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private String setDinamicDateFormatLocal(){
        out.println("Insira o formato pretendido para datas locais: ");
        return getDateTimeFormatterFromInput();
    }

    private void flowPreDefinedDateFormatLocal(){
        Menu menu = viewMainTxt.getMenu(5);
        String opcao;
        Boolean flowDone=false;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "1": model.setLocalDateTimeFormat("dd-MM-yyy HH:mm"); flowDone=true; break;
                case "2": model.setLocalDateTimeFormat("dd-MM-yyy HH:mm:ss"); flowDone=true; break;
                case "3": model.setLocalDateTimeFormat("dd-MM-yyy HH:mm:ss:nn"); flowDone=true; break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private String setDinamicDateFormatZoned(){
        out.println("Insira o formato pretendido para datas com fuso: ");
        return getDateTimeFormatterFromInput();
    }

    private void flowPreDefinedDateFormatZoned(){
        Menu menu = viewMainTxt.getMenu(6);
        String opcao;
        Boolean flowDone = false;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "1": model.setZoneDateTimeFormat("dd-MM-yyy HH:mm VV"); flowDone=true; break;
                case "2": model.setZoneDateTimeFormat("dd-MM-yyy HH:mm:ss VV"); flowDone=true; break;
                case "3": model.setZoneDateTimeFormat("dd-MM-yyy HH:mm:ss O"); flowDone=true; break;
                case "4": model.setZoneDateTimeFormat("dd-MM-yyy HH:mm VV O"); flowDone=true; break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private void setLocal() {
        String zone = flowShowAllAvailableTimezonesAndGetNZoneIds(1,viewMainTxt.getMenu(2), ZoneId.systemDefault().toString()).get(0);
        controlLocal.withZone(zone);
    }

    private void setSchedule() {
        Menu menu = viewMainTxt.getMenu(7);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "S": break;
                default: addRestrictSchedule();
            }
        }
        while(!opcao.equals("S"));
    }

    private void addRestrictSchedule(){

    }

    private void helpConfig() {
        out.println("Menu de ajuda da configuracao.");
        Input.lerString();
    }

    //------------------------
    // Menu de ajuda do menu principal
    //------------------------
    private void helpMain() {
        List<String> l = asList(
                BLACK_BOLD + "Opcao L:" + RESET + " permite ao utilizador aceder ao menu ",
                "         responsavel por interagir com datas e tempos locais.",
                " ",
                BLACK_BOLD + "Opcao Z:" + RESET + " permite ao utilizador aceder ao menu ",
                "         responsavel por interagir com datas e ",
                "         e tempos, associados a fusos.",
                " ",
                BLACK_BOLD + "Opcao A:" + RESET + " permite ao utilizador aceder ao menu ",
                "         onde se encontram as funcionalidades da agenda.",
                " ",
                BLACK_BOLD + "Opcao ?:" + RESET + " permite ao utilizador visualizar este menu.",
                " ",
                BLACK_BOLD + "Opcao S:" + RESET + " permite ao utilizador sair da aplicacao.");
        flowHelp(viewMainTxt.getMenu(3), l);
    }

    //------------------------
    // Breve explicação das opções do menu de visualização de reuniões agendadas
    //------------------------
    private void helpFormatZone() {
        List<String> l = asList(
                "Pode ser feita qualquer combinacao com:",
                "   G: era formato textual",
                "   y: ano (2018)",
                "   M: mes formato numero(12)",
                "   L: mes formato textual(Dezembro)",
                "   d: dia do mes(24)",
                "   D: dia do ano(189)",
                "",
                "   W: Semana do mes com base no primeiro dia da semana",
                "   E : dia da semana formato textual(Tuesday)",
                "",
                "   a: am ou pm",
                "   h: horas apresentadas(1-12)",
                "   k: horas apresentadas(1-24)",
                "   H: horas de um dia(0-23)",
                "   m: minutos de uma hora",
                "   s: segundos de um minuto",
                "   A: mili de um dia",
                "   n: nano-segundos",
                "   N: nano de um dia",
                "",
                "   VV: identificador do fuso (Europe/Lisbon)",
                "   z: nome do fuso (Portugal)",
                "   O: zone-offset(GMT+0)"
            );
        flowHelp(viewMainTxt.getMenu(3), l);
    }

    //------------------------
    // Breve explicação das opções do menu de visualização de reuniões agendadas
    //------------------------
    private void helpFormatLocal() {
        List<String> l = asList(
                "Pode ser feita qualquer combinacao com:",
                "   G: era formato textual",
                "   y: ano (2018)",
                "   M: mes formato numero(12)",
                "   L: mes formato textual(Dezembro)",
                "   d: dia do mes(24)",
                "   D: dia do ano(189)",
                "",
                "   W: Semana do mes com base no primeiro dia da semana",
                "   E : dia da semana formato textual(Tuesday)",
                "",
                "   a: am ou pm",
                "   h: horas apresentadas(1-12)",
                "   k: horas apresentadas(1-24)",
                "   H: horas de um dia(0-23)",
                "   m: minutos de uma hora",
                "   s: segundos de um minuto",
                "   A: mili de um dia",
                "   n: nano-segundos",
                "   N: nano de um dia"
        );
        flowHelp(viewMainTxt.getMenu(3), l);
    }
}
