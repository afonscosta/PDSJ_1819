package Model.Interface;

import Model.Class.Slot;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

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

    void withZoneLocal(String zid);

    void withZoneZone(String zid);

    void changeToCurrentDateInZone(String zoneId);

    String getLocalDateTimeFormat();

    String getZoneDateTimeFormat();

    String getZoneZone();

    void setZoneDateTimeFormat(String zoneDateTimeFormat);

    void setLocalDateTimeFormat(String localDateTimeFormat);

    void saveConfigs();

    ZoneId getLocalZone();

/*
    Métodos de acesso ao Model Schedule
 */
    boolean addSlot(Slot newSlot,Collection c);

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want, ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean removeSlot(Slot slot,Collection c);

    void editLocalSlot(Slot s, String edit);

    void editDescSlot(Slot s, String edit);

    boolean editDurationSlot(Slot s, Duration d);

    boolean editDateSLot(Slot s, Temporal data);

    Slot getSlot(Long infoSlot);

    Slot getRestrictSlot(Long infoSlot);

    Collection getSchedule();

    Collection getScheduleRestrictions();
}
