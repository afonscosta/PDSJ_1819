package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeView;
import Utilities.Menu;

import java.util.List;

import static Utilities.ConsoleColors.BLACK_BOLD;
import static Utilities.ConsoleColors.RED_BOLD;
import static Utilities.ConsoleColors.RESET;
import static Utilities.ControllerUtils.flowHelp;
import static java.util.Arrays.asList;
import static java.lang.System.out;

public class CalcDateTimeController implements InterfCalcDateTimeController {

    private InterfCalcDateTimeView viewMainTxt;
    private InterfCalcDateTimeLocalController controlLocal;
    private InterfCalcDateTimeZoneController controlZone;
    private InterfCalcDateTimeScheduleController controlSchedule;

    public CalcDateTimeController(InterfCalcDateTimeLocalController controlLocal,
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
                case "?" : help(); break;
                case "S": controlSchedule.saveState(); break;
                default: System.out.println("Opcao Invalida!"); break;
            }
        }
        while(!opcao.equals("S"));

    }

    private void help() {
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
        flowHelp(viewMainTxt.getMenu(1), l);
    }
}
