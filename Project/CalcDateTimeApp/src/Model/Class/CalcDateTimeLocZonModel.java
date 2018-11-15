package Model.Class;

import Model.Interface.InterfCalcDateTimeLocZonModel;
import Enum.EnumDateTimeShiftMode;
import Utilities.BusinessUtils;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class CalcDateTimeLocZonModel implements InterfCalcDateTimeLocZonModel {

    ZonedDateTime zdt;

    public CalcDateTimeLocZonModel() {
        this.zdt = ZonedDateTime.now();
    }

    public ZonedDateTime getZonedDateTime() {
        return zdt;
    }

    @Override
    public void shiftDateTime(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        zdt = BusinessUtils.shiftDateTime(zdt, n, cu, mode);
    }
}
