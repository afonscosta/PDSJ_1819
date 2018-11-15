package Model.Class;

import Model.Interface.InterfCalcDateTimeLocZonModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Enum.EnumDateTimeShiftMode;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class CalcDateTimeModel implements InterfCalcDateTimeModel {

    InterfCalcDateTimeLocZonModel modelLocZon;
    InterfCalcDateTimeScheduleModel modelSchedule;

    public CalcDateTimeModel(InterfCalcDateTimeLocZonModel modelLocZon,
                             InterfCalcDateTimeScheduleModel modelSchedule) {
        this.modelLocZon = modelLocZon;
        this.modelSchedule = modelSchedule;
    }

    @Override
    public ZonedDateTime getZonedDateTime() {
        return modelLocZon.getZonedDateTime();
    }

    @Override
    public void shiftDateTime(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        System.out.println("Calling modelLocZon - ShiftDays");
        modelLocZon.shiftDateTime(n, cu, mode);
    }
}
