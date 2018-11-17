package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;
import java.util.Set;
import java.util.TreeSet;

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel {
    private Set<Slot> agenda;

    public static CalcDateTimeScheduleModel of() {
        return new CalcDateTimeScheduleModel();
    }

    private CalcDateTimeScheduleModel() {
        this.agenda = new TreeSet<>(new MySlotComparator());
    }
    public boolean addSlot(Slot newSlot){
        boolean add = agenda.add(newSlot);
        for(Slot s : agenda) System.out.println(s.getData() + " ");
        return add;
    }

}
