package Model.Interface;

import Utilities.Configs;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public interface InterfCalcDateTimeLocalModel {

    void loadConfigs(Configs configs);

    Temporal getDateTime();

    void fromDateTime(ZonedDateTime zdt);

    void shiftDateTime(int n, ChronoUnit cu);

    void shiftWorkDaysDateTime(int n);

    String diffDateTime(ZonedDateTime toDateTime);

    String diffWorkDaysDateTime(ZonedDateTime toDateTime);

    void withZone(String zid);

    void setZoneId(ZoneId zoneId);
}
