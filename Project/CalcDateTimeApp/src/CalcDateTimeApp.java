import Controller.Class.CalcDateTimeController;
import Controller.Class.CalcDateTimeLocalController;
import Controller.Class.CalcDateTimeScheduleController;
import Controller.Class.CalcDateTimeZoneController;
import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Class.CalcDateTimeLocalModel;
import Model.Class.CalcDateTimeZoneModel;
import Model.Class.CalcDateTimeModel;
import Model.Class.CalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import View.Class.CalcDateTimeLocalView;
import View.Class.CalcDateTimeScheduleView;
import View.Class.CalcDateTimeView;
import View.Class.CalcDateTimeZoneView;
import View.Interface.InterfCalcDateTimeLocalView;
import View.Interface.InterfCalcDateTimeScheduleView;
import View.Interface.InterfCalcDateTimeView;
import View.Interface.InterfCalcDateTimeZoneView;

public final class CalcDateTimeApp {

    /*
    TODO Global:

    ClearConsole cross-platform

    TODO Zone:

    Validar input incorreto para Sair de paginação
    Set data
     */

    public static void main(String[] args) {

        //--------------------------------
        InterfCalcDateTimeLocalModel modelLocal = CalcDateTimeLocalModel.of();
        InterfCalcDateTimeZoneModel modelZone = CalcDateTimeZoneModel.of();
        InterfCalcDateTimeScheduleModel modelSchedule = CalcDateTimeScheduleModel.of();
        InterfCalcDateTimeModel model = new CalcDateTimeModel(modelLocal, modelZone, modelSchedule);

        //--------------------------------
        InterfCalcDateTimeLocalView viewLocal = new CalcDateTimeLocalView();
        InterfCalcDateTimeZoneView viewZone = new CalcDateTimeZoneView();
        InterfCalcDateTimeScheduleView viewSchedule = new CalcDateTimeScheduleView();
        InterfCalcDateTimeView view = new CalcDateTimeView();

        //--------------------------------
        InterfCalcDateTimeLocalController controlLocal = new CalcDateTimeLocalController();
        controlLocal.setModel(model);
        controlLocal.setView(viewLocal);

        InterfCalcDateTimeZoneController controlZone = new CalcDateTimeZoneController();
        controlZone.setModel(model);
        controlZone.setView(viewZone);

        InterfCalcDateTimeScheduleController controlSchedule = new CalcDateTimeScheduleController();
        controlSchedule.setModel(model);
        controlSchedule.setView(viewSchedule);

        InterfCalcDateTimeController control = new CalcDateTimeController(controlLocal, controlZone, controlSchedule);
        // Aqui não fiz 'control.setModel(model)' porque o Controller principal não acede ao model.
        control.setView(view);
        control.startFlow();

        //--------------------------------
        //System.out.println("Fim da Aplicação >> "
        //    + java.time.LocalDateTime.now());

        System.exit(0);
    }
}
