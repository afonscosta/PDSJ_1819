package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeView;
import Utilities.Menu;

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
                case "S": controlSchedule.saveState(); break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));

    }
}
