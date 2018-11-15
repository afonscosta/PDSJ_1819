package Model.Class;

import Model.Interface.InterfCalcDateTimeZoneModel;
import Enum.EnumDateTimeShiftMode;
import Utilities.BusinessUtils;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class CalcDateTimeZoneModel implements InterfCalcDateTimeZoneModel {

    ZonedDateTime zdt;

    public CalcDateTimeZoneModel() {
        this.zdt = ZonedDateTime.now();
    }

    @Override
    public Temporal getDateTimeZone() {
        return zdt;
    }

    @Override
    public void shiftDateTimeZone(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        zdt = (ZonedDateTime) BusinessUtils.shiftDateTime(zdt, n, cu, mode);
    }
}
