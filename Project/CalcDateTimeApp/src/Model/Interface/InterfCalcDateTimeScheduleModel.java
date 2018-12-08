package Model.Interface;

import Model.Class.Slot;
import Utilities.Configs;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface InterfCalcDateTimeScheduleModel {

    void loadConfigs(Configs configs);

    void initNextAvailableID();

    Collection getSchedule();

    void setSchedule(Set<Slot> schedule);

    Collection getScheduleRestrictions();

    boolean addSlot(Slot newSlot, Collection c, ZoneId zoneId);

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean removeSlot(Slot s,Collection c);

    boolean editDurationSlot(Long idSelectSlot, Duration newDuration, ZoneId zoneId);

    boolean editDateSLot(Long idSelectSlot, Temporal data, ZoneId zoneId);

    Slot getSlot(Long infoSlot);

    Slot getRestrictSlot(Long infoSlot);

    void editLocalSlot(Long idSlot, String edit);

    void editDescSlot(Long idSlot, String edit);

    boolean existSlot(Long idSlot);

    boolean existRestrictSlot(Long idSlot);

}

