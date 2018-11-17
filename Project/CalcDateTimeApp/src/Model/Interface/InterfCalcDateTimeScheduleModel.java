package Model.Interface;

import Model.Class.Slot;
import java.io.*;

public interface InterfCalcDateTimeScheduleModel {

    boolean addSlot(Slot newSlot);

    public void saveState(String nomeFicheiro)throws FileNotFoundException, IOException;

    public static InterfCalcDateTimeScheduleModel pushState(String nomeFicheiro) throws FileNotFoundException,
        IOException,
        ClassNotFoundException {
        FileInputStream fis = new FileInputStream(nomeFicheiro);
        ObjectInputStream ois = new ObjectInputStream(fis);
        InterfCalcDateTimeScheduleModel scheduleModel = (InterfCalcDateTimeScheduleModel) ois.readObject();
        ois.close();
        fis.close();
        return scheduleModel;
    }

}

