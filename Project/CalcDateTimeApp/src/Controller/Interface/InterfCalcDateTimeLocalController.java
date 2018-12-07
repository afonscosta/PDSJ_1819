package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeView;

public interface InterfCalcDateTimeLocalController {

    void flowLocal();

    void setModel(InterfCalcDateTimeModel model);

    void setView(InterfCalcDateTimeView viewLocal);

    void withZone(String zid);
}
