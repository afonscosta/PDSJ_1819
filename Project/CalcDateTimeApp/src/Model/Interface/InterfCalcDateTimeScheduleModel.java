package Model.Interface;

import Model.Class.Slot;
import java.io.*;
import java.util.List;

public interface InterfCalcDateTimeScheduleModel {

    boolean addSlot(Slot newSlot);

    void saveState(String nomeFicheiro)throws IOException;

    static InterfCalcDateTimeScheduleModel pushState(String nomeFicheiro) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(nomeFicheiro);
        ObjectInputStream ois = new ObjectInputStream(fis);
        InterfCalcDateTimeScheduleModel scheduleModel = (InterfCalcDateTimeScheduleModel) ois.readObject();
        ois.close();
        fis.close();
        return scheduleModel;
    }
    List<String> getMainInfoSlots();

    boolean removeSlot(String idSlot);

    boolean editSlot(String idSlot);

}

