package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.*;

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel,Serializable  {
    private Set<Slot> agenda;
    static final long serialVersionUID = 1L;

    public static CalcDateTimeScheduleModel of() {
        return new CalcDateTimeScheduleModel();
    }

    private CalcDateTimeScheduleModel() {
        Comparator<Slot> compDateSlots =
                (Comparator<Slot> & Serializable)(Slot s1, Slot s2) -> {
                                                    Temporal data1 = s1.getData();
                                                    Temporal data2 = s2.getData();

                                                    if (data1.equals(data2)) return 0;
                                                    else if (data1.getClass().getSimpleName().equals("LocalDateTime")) {
                                                        if (data2.getClass().getSimpleName().equals("LocalDateTime")) {
                                                            System.out.println("AS DUAS DATAS SÃO LOCALDATETIME");
                                                            LocalDateTime ldt1 = LocalDateTime.from(data1);
                                                            LocalDateTime ldt2 = LocalDateTime.from(data2);
                                                            System.out.println("ldt1->" + ldt1.toString());
                                                            System.out.println("ldt2->" + ldt2.toString());

                                                            if (ldt1.isBefore(ldt2)) {
                                                                Duration d1 = s1.getDuration();
                                                                LocalDateTime data1Final = ldt1.plus(d1);
                                                                System.out.println("data1Final->" + data1Final);
                                                                if (data1Final.isBefore(ldt2))
                                                                    return -1;
                                                                else {
                                                                    System.out.println("Retornei 0 no mesmo dia!!");
                                                                    return 0;
                                                                }
                                                            } else {
                                                                Duration d2 = s2.getDuration();
                                                                LocalDateTime data2Final = ldt2.plus(d2);
                                                                System.out.println("data2Final->" + data2Final);
                                                                if (data2Final.isBefore(ldt1))
                                                                    return 1;
                                                                else {
                                                                    System.out.println("Retornei 0 no mesmo dia!!");
                                                                    return 0;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    System.out.println("Se estou aqui é porque não são LocalDate Time");
                                                    return 0;
                                                };
                this.agenda= new TreeSet<>(compDateSlots);
    }
    public List<String> getMainInfoSlots(){
        List<String> res= new ArrayList();
        int index =0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
        for(Slot s : agenda) {
            LocalDateTime date = LocalDateTime.from(s.getData());
            String formattedDate = date.format(formatter);
            res.add(index + ": " + formattedDate + " || " + s.getLocal());
            index ++;
        }
        return res;
    }

    public Set<Slot> getAgenda() {
        return agenda;
    }

    public void setAgenda(Set<Slot> agenda) {
        this.agenda = agenda;
    }

    public boolean addSlot(Slot newSlot){
        boolean add = agenda.add(newSlot);

        return add;
    }
    public boolean removeSlot(String idSlot){
        Slot s = getSlot(idSlot);
        boolean res= false;
        if(s!=null){
            res =agenda.remove(s);
        }
        return res;
    }

    public boolean editSlot(String idSlot){
        return true;
    }

    public Slot getSlot(String infoSlot){
        int id = Integer.parseInt(infoSlot);
        int index =0;
        for(Slot s : agenda){
            if(index==id){
                return s;
            }
            if(index>id)
                return null;
        }
        return  null;
    }

    public void saveState(String nomeFicheiro) throws IOException {
        FileOutputStream fos = new FileOutputStream(nomeFicheiro);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);//guarda-se o objecto de uma só vez
        oos.close();
        fos.close();
    }
}
