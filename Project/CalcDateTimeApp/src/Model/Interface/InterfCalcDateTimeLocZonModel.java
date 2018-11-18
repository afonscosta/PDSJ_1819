package Model.Interface;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Set;

public interface InterfCalcDateTimeLocZonModel {

    Temporal getDateTime(String k);

    void fromDateTime(String k, ZonedDateTime newLDT);

    void shiftDateTime(String k, int n, ChronoUnit cu);

    void shiftWorkDaysDateTime(String k, int n);

    String diffDateTime(String k, ZonedDateTime toDateTime);

    String diffWorkDaysDateTime(String k, ZonedDateTime toDateTime);

    void convertZoneDateTimeToZone(String k, String zoneIdTxt);

    void changeToCurrentDateInZone(String k, String zoneIdTxt);

    Set<String> getKeys();
}
