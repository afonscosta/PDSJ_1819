import Controller.Class.CalcDateTimeController;
import Controller.Class.CalcDateTimeLocalController;
import Controller.Class.CalcDateTimeScheduleController;
import Controller.Class.CalcDateTimeZoneController;
import Controller.Interface.InterfCalcDateTimeController;
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

    public static void main(String[] args) {
        //--------------------------------
        InterfCalcDateTimeModel model = CalcDateTimeModel.of();

        //--------------------------------
        InterfCalcDateTimeView viewLocal = CalcDateTimeLocalView.of();
        InterfCalcDateTimeView viewZone = CalcDateTimeZoneView.of();
        InterfCalcDateTimeView viewSchedule = CalcDateTimeScheduleView.of();
        InterfCalcDateTimeView view = CalcDateTimeView.of();

        //--------------------------------
        InterfCalcDateTimeController controlLocal = CalcDateTimeLocalController.of();
        controlLocal.setModel(model);
        controlLocal.setView(viewLocal);

        InterfCalcDateTimeController controlZone = CalcDateTimeZoneController.of();
        controlZone.setModel(model);
        controlZone.setView(viewZone);

        InterfCalcDateTimeController controlSchedule = CalcDateTimeScheduleController.of();
        controlSchedule.setModel(model);
        controlSchedule.setView(viewSchedule);

        InterfCalcDateTimeController control = CalcDateTimeController.of(controlLocal, controlZone, controlSchedule);
        control.setModel(model);
        control.setView(view);
        control.startFlow();

        System.exit(0);
    }
}
