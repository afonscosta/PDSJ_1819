package Model.Interface;

import Model.Class.RestrictSlot;
import Model.Class.Slot;
import Utilities.Configs;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

public interface InterfCalcDateTimeModel {

    //------------------------
    // Métodos Model Facade
    //------------------------
    long getIdSlot();

    void incNIdSlot(int n);

    long getIdRestrictSlot();

    void incNIdRestrictSlot(int n);

    void setZoneDateTimeFormat(String zoneDateTimeFormat);

    void setLocalDateTimeFormat(String localDateTimeFormat);

    void saveConfigs();

    String getLocalDateTimeFormat();

    String getZoneDateTimeFormat();

    Configs getConfigs();

    //------------------------
    // Métodos Model Local
    //------------------------

    Temporal getDateTimeLocal();

    ZoneId getLocalZone();

    void shiftDateTimeLocal(int n, ChronoUnit cu);

    void shiftWorkDaysDateTimeLocal(int n);

    String diffDateTimeLocal(ZonedDateTime toDateTime);

    String diffWorkDaysDateTimeLocal(ZonedDateTime toDateTime);

    void shiftWorkDaysDateTimeZone(int n);

    String diffDateTimeZone(ZonedDateTime toDateTime);

    String diffWorkDaysDateTimeZone(ZonedDateTime toDateTime);

    void fromDateTimeLocal(ZonedDateTime ldt);

    void withZoneLocal(String zid);

    //------------------------
    // Métodos Model Zone
    //------------------------

    String getZoneZone();

    Temporal getDateTimeZone();

    void shiftDateTimeZone(int n, ChronoUnit cu);

    void withZoneZone(String zid);

    void changeToCurrentDateInZone(String zoneId);

    void fromDateTimeZone(ZonedDateTime zdt);

    //------------------------
    // Métodos Model Schedule
    //------------------------
    boolean addSlot(Slot newSlot);

    int addRestrictSlot(RestrictSlot newSlot);

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want, ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);


    boolean removeSlot(Long s);

    boolean removeRestrictSlot(Long s);

    void editLocalSlot(Long idSlot, String edit);

    void editDescSlot(Long idSlot, String edit);

    boolean editDurationSlot(Long idSlot, Duration d);

    boolean editDateSLot(Long idSlot, Temporal data);

    Slot getSlot(Long infoSlot);

    RestrictSlot getRestrictSlot(Long infoSlot);

    Collection getSchedule();

    Collection getScheduleRestrictions();

    boolean existSlot(Long idSlot);

    boolean existRestrictSlot(Long idSlot);
}
