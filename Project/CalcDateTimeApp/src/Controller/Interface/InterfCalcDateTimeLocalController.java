package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeLocalView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface InterfCalcDateTimeLocalController {

    void flowLocal();

    void setModel(InterfCalcDateTimeModel model);

    void setView(InterfCalcDateTimeLocalView viewLocal);

    void withZone(String zid);
}
