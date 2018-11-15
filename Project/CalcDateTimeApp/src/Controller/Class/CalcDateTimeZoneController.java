package Controller.Class;

import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeZoneView;

public class CalcDateTimeZoneController implements InterfCalcDateTimeZoneController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeZoneView viewZone;

    public CalcDateTimeZoneController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    @Override
    public void setView(InterfCalcDateTimeZoneView viewZone) {
        this.viewZone = viewZone;
    }

    @Override
    public void flowZone() {

    }
}
