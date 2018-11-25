package Model.Interface;

import Model.Class.CalcDateTimeScheduleModel;
import Model.Class.Slot;
import Utilities.Configs;
import Utilities.EnumEditSlotInfo;

import java.io.*;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Set;

public interface InterfCalcDateTimeScheduleModel {

    void loadConfigs(Configs configs);

    Set<Slot> getAgenda();

    boolean addSlot(Slot newSlot);

    void saveState(String nomeFicheiro) throws IOException;

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean removeSlot(Slot slot);

    void editSlot(Slot s, EnumEditSlotInfo e,String edit);

    Slot editDurationSlot(Slot s, Duration d);

    Slot editDateSLot(Slot s, Temporal data);

    Slot getSlot(String infoSlot);

    List<String> getRestrictSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);
}

