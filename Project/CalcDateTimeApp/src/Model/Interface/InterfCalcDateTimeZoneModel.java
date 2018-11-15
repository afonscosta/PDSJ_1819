package Model.Interface;

import Enum.EnumDateTimeShiftMode;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public interface InterfCalcDateTimeZoneModel {


    Temporal getDateTimeZone();

    void shiftDateTimeZone(int n, ChronoUnit cu, EnumDateTimeShiftMode mode);

}
