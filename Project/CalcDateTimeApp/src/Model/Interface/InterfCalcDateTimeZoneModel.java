package Model.Interface;

import Utilities.EnumDateTimeShiftMode;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public interface InterfCalcDateTimeZoneModel {


    Temporal getDateTimeZone();

    void shiftDateTimeZone(int n, ChronoUnit cu, EnumDateTimeShiftMode mode);

    void convertZoneDateTimeToZone(String zoneId);

    void changeZoneDateTimeToCurrentDateInZone(String zoneId);
}
