package Model.Interface;

import Model.Class.CalcDateTimeScheduleModel;
import Model.Class.RestrictSlot;
import Model.Class.Slot;
import Utilities.EnumEditSlotInfo;

import java.io.*;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

public interface InterfCalcDateTimeScheduleModel {

    Collection getSchedule();

    Collection getScheduleRestrictions();

    boolean addSlot(Slot newSlot, Collection c);

    void saveState(String nomeFicheiro) throws IOException;

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean removeSlot(Slot slot,Collection c);

    void editSlot(Slot s, EnumEditSlotInfo e,String edit);

    Slot editDurationSlot(Slot s, Duration d);

    Slot editDateSLot(Slot s, Temporal data);

    Slot getSlot(String infoSlot,Collection c);

}

