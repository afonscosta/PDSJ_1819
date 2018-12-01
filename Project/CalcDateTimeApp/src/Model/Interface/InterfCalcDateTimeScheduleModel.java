package Model.Interface;

import Model.Class.Slot;
import Utilities.Configs;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

public interface InterfCalcDateTimeScheduleModel {

    void loadConfigs(Configs configs);

    Collection getSchedule();

    Collection getScheduleRestrictions();

    boolean addSlot(Slot newSlot, Collection c);

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean removeSlot(Slot slot,Collection c);

    boolean editDurationSlot(Slot s, Duration d);

    boolean editDateSLot(Slot s, Temporal data);

    Slot getSlot(Long infoSlot);

    Slot getRestrictSlot(Long infoSlot);

    void editLocalSlot(Slot s, String edit);

    void editDescSlot(Slot s, String edit);
}

