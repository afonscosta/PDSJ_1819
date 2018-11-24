package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.time.format.DateTimeFormatter;

public interface InterfCalcDateTimeScheduleController {

    void setDateTimeFormatterLocal(DateTimeFormatter dtf);

    void setDateTimeFormatterZoned(DateTimeFormatter dtf);

    void withZone(String zid);

    void flowSchedule();

    void setView(InterfCalcDateTimeScheduleView viewSchedule);

    void setModel(InterfCalcDateTimeModel model);

    void saveState();
}
