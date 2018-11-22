package Model.Interface;

import Model.Class.CalcDateTimeScheduleModel;
import Model.Class.Slot;
import Utilities.EnumEditSlotInfo;

import java.io.*;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.List;

public interface InterfCalcDateTimeScheduleModel {

    boolean addSlot(Slot newSlot);

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
    List<String> getMainInfoSlots();

    boolean removeSlot(Slot slot);

    void editSlot(Slot s, EnumEditSlotInfo e,String edit);

    Slot editDurationSlot(Slot s, Duration d);

    Slot editDateSLot(Slot s, Temporal data);

    Slot getSlot(String infoSlot);

    List<String> getRestrictSlots(String modeNormalized, int want);
}

