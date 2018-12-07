package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeView;

public interface InterfCalcDateTimeScheduleController {

    void flowSchedule();

    void setView(InterfCalcDateTimeView viewSchedule);

    void setModel(InterfCalcDateTimeModel model);
}
