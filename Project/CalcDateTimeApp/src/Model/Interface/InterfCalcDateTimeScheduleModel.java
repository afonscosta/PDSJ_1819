package Model.Interface;

import Model.Class.RestrictSlot;
import Model.Class.Slot;
import Utilities.Configs;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Set;

public interface InterfCalcDateTimeScheduleModel {

    void loadConfigs(Configs configs);

    Set<Slot> getSchedule();

    void setSchedule(Set<Slot> schedule);

    List<RestrictSlot> getScheduleRestrictions();

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean addSlot(Slot newSlot, ZoneId zoneId);

    int addRestrictSlot(RestrictSlot newSlot, ZoneId zoneId);

    boolean removeSlot(Long idSlot);

    boolean removeRestrictSlot(Long idRestrictSlot);

    boolean editDurationSlot(Long idSelectSlot, Duration newDuration, ZoneId zoneId);

    boolean editDateSlot(Long idSlot, Temporal data, ZoneId zoneId);

    Slot getSlot(Long infoSlot);

    RestrictSlot getRestrictSlot(Long infoSlot);

    void editLocalSlot(Long idSlot, String edit);

    void editDescSlot(Long idSlot, String edit);

    boolean existSlot(Long idSlot);

    boolean existRestrictSlot(Long idSlot);

}

