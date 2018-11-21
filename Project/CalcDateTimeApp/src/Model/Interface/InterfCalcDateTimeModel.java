package Model.Interface;

import Model.Class.Slot;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public interface InterfCalcDateTimeModel {
        //extends InterfCalcDateTimeLocalModel, InterfCalcDateTimeZoneModel, InterfCalcDateTimeScheduleModel {
    Temporal getDateTimeLocal();

    Temporal getDateTimeZone();

    void shiftDateTimeLocal(int n, ChronoUnit cu);

    void shiftWorkDaysDateTimeLocal(int n);

    String diffDateTimeLocal(ZonedDateTime toDateTime);

    String diffWorkDaysDateTimeLocal(ZonedDateTime toDateTime);

    void fromDateTimeLocal(ZonedDateTime ldt);

    void shiftDateTimeZone(int n, ChronoUnit cu);

    void shiftWorkDaysDateTimeZone(int n);

    String diffDateTimeZone(ZonedDateTime toDateTime);

    String diffWorkDaysDateTimeZone(ZonedDateTime toDateTime);

    void fromDateTimeZone(ZonedDateTime zdt);

    void withZone(String zoneId);

    void changeToCurrentDateInZone(String zoneId);

    boolean addSlot(Slot newSlot);
}
