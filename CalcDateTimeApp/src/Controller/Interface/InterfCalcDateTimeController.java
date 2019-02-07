package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeView;

public interface InterfCalcDateTimeController  {

    void startFlow();

    void setView(InterfCalcDateTimeView view);

    void setModel(InterfCalcDateTimeModel model);
}
