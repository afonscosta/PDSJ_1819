package Model.Class;

import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.Configs;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class CalcDateTimeZoneModel extends CalcDateTimeLocalModel implements InterfCalcDateTimeZoneModel {

    public static CalcDateTimeZoneModel of () {
        return new CalcDateTimeZoneModel();
    }

    private CalcDateTimeZoneModel() {
        super();
    }

    public void loadConfigs(Configs configs) {
        super.loadConfigs(configs);
    }

    @Override
    public Temporal getDateTime() {
        return super.getDateTime();
    }

    @Override
    public ZoneId getZone() {
        return ((ZonedDateTime) super.getDateTime()).getZone();
    }
}
