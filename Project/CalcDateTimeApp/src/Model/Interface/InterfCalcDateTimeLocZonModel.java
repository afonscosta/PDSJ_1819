package Model.Interface;

import Enum.EnumDateTimeShiftMode;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public interface InterfCalcDateTimeLocZonModel {


    ZonedDateTime getZonedDateTime();

    void shiftDateTime(int n, ChronoUnit cu, EnumDateTimeShiftMode mode);
}
