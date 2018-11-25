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

    void saveState(String nomeFicheiro)throws IOException;

    static InterfCalcDateTimeScheduleModel pushState(String nomeFicheiro) {
        try {
            FileInputStream fis = new FileInputStream(nomeFicheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);
            InterfCalcDateTimeScheduleModel scheduleModel = (InterfCalcDateTimeScheduleModel) ois.readObject();
            ois.close();
            fis.close();
            return scheduleModel;
        }
        catch(IOException | ClassNotFoundException e){
            System.out.println("Problemas a trazer o tree set");
            return CalcDateTimeScheduleModel.of();

        }
    }
    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    boolean removeSlot(Slot slot,Collection c);

    void editSlot(Slot s, EnumEditSlotInfo e,String edit);

    Slot editDurationSlot(Slot s, Duration d);

    Slot editDateSLot(Slot s, Temporal data);

    Slot getSlot(String infoSlot,Collection c);

}

