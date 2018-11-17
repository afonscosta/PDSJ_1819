package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel,Serializable  {
    private Set<Slot> agenda;
    static final long serialVersionUID = 1L;

    public static CalcDateTimeScheduleModel of() {
        return new CalcDateTimeScheduleModel();
    }

    private CalcDateTimeScheduleModel() {
        this.agenda= new TreeSet<>(new MySlotComparator());
    }

    public boolean addSlot(Slot newSlot){
        boolean add = agenda.add(newSlot);
        for(Slot s : agenda) System.out.println(s.getData() + " ");
        return add;
    }


    public void saveState(String nomeFicheiro) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(nomeFicheiro);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this); //guarda-se todo o objecto de uma só vez
        oos.close();
        fos.close();
    }
}
