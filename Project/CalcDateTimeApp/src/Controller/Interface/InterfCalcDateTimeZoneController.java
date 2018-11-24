package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.format.DateTimeFormatter;

public interface InterfCalcDateTimeZoneController {

    void flowZone();

    void setModel(InterfCalcDateTimeModel model);

    void setView(InterfCalcDateTimeZoneView viewZone);
}
