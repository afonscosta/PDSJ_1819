package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeScheduleView;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewSchedule;

    public CalcDateTimeScheduleController() {

    }

    @Override
    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewSchedule = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    @Override
    public void flowSchedule() {

    }
}
