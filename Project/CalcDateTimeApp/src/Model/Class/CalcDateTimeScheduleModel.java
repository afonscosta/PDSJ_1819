package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;
import Utilities.EnumEditSlotInfo;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.Set;
import java.util.TreeSet;

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel {
    private Set<Slot> agenda;
    static final long serialVersionUID = 1L;
    //do ficheiro de configuração
    private final ZoneId referenceZone= ZoneId.of("Europe/Lisbon");

    public static CalcDateTimeScheduleModel of() {
        return new CalcDateTimeScheduleModel();
    }

    private CalcDateTimeScheduleModel() {
        Comparator<Slot> compDateSlots =
                (Comparator<Slot> & Serializable)(Slot s1, Slot s2) -> {
                                Temporal data1 = s1.getData();
                                Temporal data2 = s2.getData();
                                LocalDateTime ldt1 = LocalDateTime.from(data1);
                                LocalDateTime ldt2 = LocalDateTime.from(data2);

                                if (data1.equals(data2)) return 0;
                                else {
                                    if (data1.getClass().getSimpleName().equals("ZonedDateTime")) {
                                        ldt1 = convertZoneDateTimeToSpecificZone(data1, this.referenceZone).toLocalDateTime();
                                    }
                                    if (data2.getClass().getSimpleName().equals("ZonedDateTime")) {
                                        ldt2 = convertZoneDateTimeToSpecificZone(data2,this.referenceZone).toLocalDateTime();
                                    }
                                    System.out.println("ldt1->" + ldt1.toString());
                                    System.out.println("ldt2->" + ldt2.toString());

                                    if (ldt1.isBefore(ldt2)) {
                                        Duration d1 = s1.getDuration();
                                        LocalDateTime data1Final = ldt1.plus(d1);
                                        System.out.println("data1Final->" + data1Final);
                                        if (data1Final.isBefore(ldt2))
                                            return -1;
                                        else {
                                            return 0;
                                        }
                                    } else {
                                        Duration d2 = s2.getDuration();
                                        LocalDateTime data2Final = ldt2.plus(d2);
                                        System.out.println("data2Final->" + data2Final);
                                        if (data2Final.isBefore(ldt1))
                                            return 1;
                                        else {
                                            return 0;
                                        }
                                    }
                                }
                };
                this.agenda= new TreeSet<>(compDateSlots);
    }
    //pode ir para o utils
    public ZonedDateTime convertZoneDateTimeToSpecificZone (Temporal data,ZoneId referenceZone) {
        System.out.println(data);
        ZonedDateTime zoneData= ZonedDateTime.from(data);
        zoneData = zoneData.withZoneSameInstant(referenceZone);
        System.out.println(zoneData);
        return zoneData;
    }

    public Set<Slot> getAgenda() {
        return agenda;
    }

    public void setAgenda(Set<Slot> agenda) {
        this.agenda = agenda;
    }

    public boolean isSlotfromReferenceZone(Slot s, ZoneId referenceZone){
        ZonedDateTime date = ZonedDateTime.from(s.getData());
        if(date.getZone().equals(referenceZone))
            return true;
        else
            return false;
    }

    //------------------------
    // Se a reunião estiver na zoned default, não é apresentada a informação referente a Zoned
    //------------------------
    public String DateSlotToString(Slot s,ZoneId referenceZone){
        DateTimeFormatter formatterToShowLocalDateTime = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");
        DateTimeFormatter formatterToShowZonedDateTime = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm VV");
        ZonedDateTime date = ZonedDateTime.from(s.getData());
        boolean temp = isSlotfromReferenceZone(s,referenceZone);
        if(temp==true) {
             return date.format(formatterToShowLocalDateTime);
        }
        else{
            return date.format(formatterToShowZonedDateTime);
        }
    }

    //------------------------
    // Devolve todas as reuniões existentes
    // É apresentada cada reunião de forma aglutinada, pela sua data e local.
    // Se a reunião estiver na zoned default, não é apresentada a zoned
    //------------------------
    public List<String> getMainInfoSlots(){
        List<String> res= new ArrayList();
        int index =0;
        for(Slot s : agenda) {
            res.add(index + ": " + DateSlotToString(s,this.referenceZone) + " || " + s.getLocal());
            index ++;
        }
        return res;
    }

    //------------------------
    // ModeNormalized: diario, semanal, mensal
    // want -> a igualdade a verficar na agenda
    // Por exemplo, utilizador escolhe diario -> want diz-me o dia referente
    //------------------------

    public List<String> getRestrictSlots(String modeNormalized, int want){
        List<String> res= new ArrayList();
        int index =0;
        for(Slot s : agenda) {
            ZonedDateTime date = ZonedDateTime.from(s.getData());
            switch (modeNormalized) {
                case "diario":
                    System.out.print(date.getDayOfMonth() +"==" + want);
                    if(date.getDayOfMonth()==want){
                        System.out.println("YES");
                        res.add(index + ": " + DateSlotToString(s,this.referenceZone) + " || " + s.getLocal());
                        index ++;
                    }
                    break;
                case "semanal":
                    TemporalField woy = WeekFields.ISO.weekOfMonth();
                    int weekNumber = date.get(woy);
                    System.out.println(weekNumber);
                    if(weekNumber==want){
                        res.add(index + ": " + DateSlotToString(s,this.referenceZone) + " || " + s.getLocal());
                        index ++;
                    }
                    break;
                case "mensal":
                    if(date.getMonthValue()==want){
                        res.add(index + ": " + DateSlotToString(s,this.referenceZone) + " || " + s.getLocal());
                        index ++;
                    }
                    break;
            }
        }
        System.out.println(res);
        return res;
    }

    //------------------------
    // Adicionar uma reunião à agenda
    //------------------------
    public boolean addSlot(Slot newSlot){
        boolean add = agenda.add(newSlot);

        return add;
    }

    //------------------------
    // Remover uma reunião da agenda
    //------------------------
    public boolean removeSlot(Slot s){
        return agenda.remove(s);
    }

    //------------------------
    // Adicionar uma reunião à agenda
    //------------------------
    public void editSlot(Slot s, EnumEditSlotInfo e, String edit){
            switch (e){
                case LOCAL:
                    s.setLocal(edit);
                    break;
                case DESC:
                    s.setDescription(edit);
                    break;
            }
    }

    //------------------------
    // Alterar a duração de uma reuniao
    // É necessário garantir que a nova duração não provoca sobreposições
    // Remover o slot antigo -> adicionar um novo slot, com a mesma info excepto a duração.
    //------------------------
    public Slot editDurationSlot(Slot s, Duration newDuration) {
        Slot temp = s.clone();
        boolean res =removeSlot(s);
        System.out.println("Remover o slot que quero substituir:" + res);
        Slot newSlot = new Slot(temp.getData(),newDuration,temp.getLocal(),temp.getDescription());
        boolean add = addSlot(newSlot);
        if(add==false) {
            addSlot(temp);
            return temp;
        }
        return newSlot;
    }
    //------------------------
    // Alterar a data de uma reuniao
    // É necessário garantir que a nova data não provoca sobreposições
    // Remover o slot antigo -> adicionar um novo slot, com a mesma info excepto a data.
    //------------------------
    public Slot editDateSLot(Slot s, Temporal data){
        Slot temp = s.clone();
        boolean res =removeSlot(s);
        System.out.println("Remover o slot que quero substituir:" + res);
        Slot newSlot = new Slot(data, temp.getDuration(),temp.getLocal(),temp.getDescription());
        boolean add = addSlot(newSlot);
        if(add==false){
            System.out.println("NAO DEU PARA ALTERAR DATA");
            addSlot(temp);
            return temp;
        }
        return newSlot;
    }

    //------------------------
    // Dado o idenficador gerado ao nivel da interface ao percorrer a agenda
    // Este identificador pode ser visto como temporário
    // Devolver o objecto que o identifica
    //------------------------
    public Slot getSlot(String infoSlot){
        int id = Integer.parseInt(infoSlot);
        int index =0;
        for(Slot s : agenda){
            if(index==id){
                return s;
            }
            if(index>id)
                return null;
        index ++;
        }
        return  null;
    }


    //------------------------
    // Guarda o estado do model pois este tem de ser persistente
    //------------------------
    public void saveState(String nomeFicheiro) throws IOException {
        FileOutputStream fos = new FileOutputStream(nomeFicheiro);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();
    }
}
