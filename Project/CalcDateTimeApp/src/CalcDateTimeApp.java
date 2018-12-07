import Controller.Class.CalcDateTimeController;
import Controller.Class.CalcDateTimeLocalController;
import Controller.Class.CalcDateTimeScheduleController;
import Controller.Class.CalcDateTimeZoneController;
import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Class.CalcDateTimeLocalModel;
import Model.Class.CalcDateTimeModel;
import Model.Class.CalcDateTimeScheduleModel;
import Model.Class.CalcDateTimeZoneModel;
import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.Configs;
import View.Class.CalcDateTimeLocalView;
import View.Class.CalcDateTimeScheduleView;
import View.Class.CalcDateTimeView;
import View.Class.CalcDateTimeZoneView;
import View.Interface.InterfCalcDateTimeView;


public final class CalcDateTimeApp {

    /*
    TODO Global:

    ClearConsole cross-platform

    TODO Zone:
    "Esta é a data, quer guardar no buffer?"
     */

    public static void main(String[] args) {
        Configs configs = Configs.of("./Configs");

        //--------------------------------
        InterfCalcDateTimeLocalModel modelLocal = CalcDateTimeLocalModel.of();
        InterfCalcDateTimeZoneModel modelZone = CalcDateTimeZoneModel.of();
        InterfCalcDateTimeScheduleModel modelSchedule = CalcDateTimeScheduleModel.of();
        InterfCalcDateTimeModel model = new CalcDateTimeModel(modelLocal, modelZone, modelSchedule, configs);

        //--------------------------------
        InterfCalcDateTimeView viewLocal = new CalcDateTimeLocalView();
        InterfCalcDateTimeView viewZone = new CalcDateTimeZoneView();
        InterfCalcDateTimeView viewSchedule = new CalcDateTimeScheduleView();
        InterfCalcDateTimeView view = new CalcDateTimeView();

        //--------------------------------
        InterfCalcDateTimeLocalController controlLocal = CalcDateTimeLocalController.of();
        controlLocal.setModel(model);
        controlLocal.setView(viewLocal);

        InterfCalcDateTimeZoneController controlZone = CalcDateTimeZoneController.of();
        controlZone.setModel(model);
        controlZone.setView(viewZone);

        InterfCalcDateTimeScheduleController controlSchedule = CalcDateTimeScheduleController.of();
        controlSchedule.setModel(model);
        controlSchedule.setView(viewSchedule);

        InterfCalcDateTimeController control = CalcDateTimeController.of(controlLocal, controlZone, controlSchedule);
        control.setModel(model);
        control.setView(view);
        control.startFlow();

        System.exit(0);
    }
}
