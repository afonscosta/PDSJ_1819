package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static Utilities.ConsoleColors.BLACK_BOLD;
import static Utilities.ConsoleColors.RESET;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Arrays.binarySearch;

public class CalcDateTimeController implements InterfCalcDateTimeController {

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
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "FL": flowSetDateFormatLocal(); break;
                case "FF": flowSetDateFormatZoned(); break;
                case "L": setLocal(); break;
                case "H": setSchedule(); break;
                case "?": helpConfig(); break;
                case "S": break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!opcao.equals("S"));
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
                case "M": setDateFormatLocal(setDinamicDateFormatLocal()); flowDone=true; break;
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
                case "M": setDateFormatZoned(setDinamicDateFormatZoned()); flowDone=true; break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private DateTimeFormatter setDinamicDateFormatLocal(){
        out.println("Insira o formato pretendido para datas locais: ");
        return getDateTimeFormatterFromInput();
    }

    private void setDateFormatLocal(DateTimeFormatter dtf) {
        controlLocal.setDateTimeFormatter(dtf);
        controlSchedule.setDateTimeFormatterLocal(dtf);
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
                case "1": setDateFormatLocal(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm")); flowDone=true; break;
                case "2": setDateFormatLocal(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss")); flowDone=true; break;
                case "3": setDateFormatLocal(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss:nn")); flowDone=true; break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private DateTimeFormatter setDinamicDateFormatZoned(){
        out.println("Insira o formato pretendido para datas com fuso: ");
        return getDateTimeFormatterFromInput();
    }
    private void setDateFormatZoned(DateTimeFormatter dtf) {
        controlZone.setDateTimeFormatter(dtf);
        controlSchedule.setDateTimeFormatterZoned(dtf);
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
                case "1": setDateFormatZoned(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm VV")); flowDone=true; break;
                case "2": setDateFormatZoned(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss VV")); flowDone=true; break;
                case "3": setDateFormatZoned(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss O")); flowDone=true; break;
                case "4": setDateFormatZoned(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm VV O")); flowDone=true; break;
                case "S": flowDone=true; break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!flowDone);
    }

    private void setLocal() {
        String zone = flowShowAllAvailableTimezonesAndGetNZoneIds(1,viewMainTxt.getMenu(2), ZoneId.systemDefault().toString()).get(0);
        controlLocal.withZone(zone);
        controlSchedule.withZone(zone);
    }

    private void setSchedule() {
        out.println("Menu de definicao do horario da agenda");
        Input.lerString();
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
}
