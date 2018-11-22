package Model.Interface;

import Model.Class.Slot;
import Utilities.EnumEditSlotInfo;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
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

    boolean addSlot(Slot newSlot);

    void saveState(String nomeFicheiro) throws IOException;

    List<String> getMainInfoSlots();

    boolean removeSlot(Slot slot);

    void editSlot(Slot s, EnumEditSlotInfo e, String edit);

    Slot editDurationSlot(Slot s, Duration d);


    Slot editDateSLot(Slot s, Temporal data);

    Slot getSlot(String infoSlot);

    List<String> getRestrictSlots(String modeNormalized, int want);
}
