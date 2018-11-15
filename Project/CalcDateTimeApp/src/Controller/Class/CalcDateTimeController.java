package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import static Utilities.BusinessUtils.clearConsole;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeView;
import View.Menu;

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
            clearConsole();
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : controlLocal.flowLocal(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));

    }
}
