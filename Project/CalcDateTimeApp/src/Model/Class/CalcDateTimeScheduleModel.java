package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;
import Utilities.BusinessUtils;
import Utilities.EnumEditSlotInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel,Serializable {
    private Set<Slot> schedule;
    private List<RestrictSlot> scheduleRestrictions;
    static final long serialVersionUID = 1L;

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
                                ZoneId referenceZone = getReferenceZone();

                                if (data1.equals(data2)) return 0;
                                else {
                                    if (data1.getClass().getSimpleName().equals("ZonedDateTime")) {
                                        ldt1 = BusinessUtils.convertZoneDateTimeToSpecificZone(data1, referenceZone).toLocalDateTime();
                                    }
                                    if (data2.getClass().getSimpleName().equals("ZonedDateTime")) {
                                        ldt2 = BusinessUtils.convertZoneDateTimeToSpecificZone(data2,referenceZone).toLocalDateTime();
                                    }
                                    //System.out.println("ldt1->" + ldt1.toString());
                                    //System.out.println("ldt2->" + ldt2.toString());

                                    if (ldt1.isBefore(ldt2)) {
                                        Duration d1 = s1.getDuration();
                                        LocalDateTime data1Final = ldt1.plus(d1);
                                        //System.out.println("data1Final->" + data1Final);
                                        if (data1Final.isBefore(ldt2))
                                            return -1;
                                        else {
                                            return 0;
                                        }
                                    } else {
                                        Duration d2 = s2.getDuration();
                                        LocalDateTime data2Final = ldt2.plus(d2);
                                        //System.out.println("data2Final->" + data2Final);
                                        if (data2Final.isBefore(ldt1))
                                            return 1;
                                        else {
                                            return 0;
                                        }
                                    }
                                }
                };
                this.schedule= new TreeSet<>(compDateSlots);
                this.scheduleRestrictions= new ArrayList<>();
    }

    //------------------------
    // Retorna o ZonedId de definido no ficheiro de configurações
    //------------------------
    private ZoneId getReferenceZone(){
        String pathToConfFile = "./date_dict_conf.json";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(pathToConfFile));
            JSONObject jsonObj = (JSONObject) obj;
            String zoneIdTxt = ((String) jsonObj.get("zoneId"));
            // Json character escape nos ficheiros, portanto temos de os remover
            zoneIdTxt = zoneIdTxt.replaceAll("\\\\","");
            ZoneId zoneId = ZoneId.of(zoneIdTxt);
            //System.out.println(zoneId);
            return zoneId;

        } catch (Exception e) {
            return ZoneId.systemDefault();

        }
    }

    public Set<Slot> getSchedule() {
        return schedule;
    }

    public List<RestrictSlot> getScheduleRestrictions() {
        return scheduleRestrictions;
    }

    public void setschedule(Set<Slot> schedule) {
        this.schedule = schedule;
    }

    //------------------------
    // Devolve todas as reuniões existentes
    // É apresentada cada reunião de forma aglutinada, pela sua data e local.
    // Se a reunião estiver na zoned default, não é apresentada a zoned
    //------------------------
    public List<String> getMainInfoSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        int index =0;
        for(Slot s : schedule) {
            res.add(index + ": " + BusinessUtils.DateSlotToString(s,referenceZone,dtfLocal,dtfZone) + " || " + s.getLocal());
            index ++;
        }
        return res;
    }

    public List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        int index =0;
        for(RestrictSlot s: scheduleRestrictions){
            res.add(index + ": " + BusinessUtils.DateSlotToString(s,referenceZone,dtfLocal,dtfZone) + " || " + s.getPeriod());
            index ++;
        }
        return res;
    }

    //------------------------
    // ModeNormalized: diaria, semanal, mensal
    // want -> a igualdade a verficar na schedule
    // Por exemplo, utilizador escolhe diaria -> want diz-me o dia referente
    //------------------------

    public List<String> getModeSlots(String modeNormalized, int want, ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        int index =0;
        for(Slot s : schedule) {
            ZonedDateTime date = ZonedDateTime.from(s.getData());
            switch (modeNormalized) {
                case "diaria":
                    if(date.getDayOfMonth()==want){
                        res.add(index + ": " + BusinessUtils.DateSlotToString(s,referenceZone,dtfLocal,dtfZone) + " || " + s.getLocal());
                        index ++;
                    }
                    break;
                case "semanal":
                    TemporalField woy = WeekFields.ISO.weekOfYear();
                    int weekNumber = date.get(woy);
                    if(weekNumber==want){
                        res.add(index + ": " + BusinessUtils.DateSlotToString(s,referenceZone,dtfLocal, dtfZone) + " || " + s.getLocal());
                        index ++;
                    }
                    break;
                case "mensal":
                    if(date.getMonthValue()==want){
                        res.add(index + ": " + BusinessUtils.DateSlotToString(s,referenceZone,dtfLocal,dtfZone) + " || " + s.getLocal());
                        index ++;
                    }
                    break;
            }
        }
        return res;
    }

    //------------------------
    // Adicionar uma reunião à schedule
    //------------------------
    public boolean addSlot(Slot newSlot, Collection c){
        return c.add(newSlot);
    }

    //------------------------
    // Remover uma reunião da schedule
    //------------------------
    public boolean removeSlot(Slot s, Collection c){
        return c.remove(s);
    }

    //------------------------
    // Adicionar uma reunião à schedule
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
        removeSlot(s,schedule);
        Slot newSlot = Slot.of(temp.getData(),newDuration,temp.getLocal(),temp.getDescription());
        boolean add = addSlot(newSlot,schedule);
        if(add==false) {
            addSlot(temp,schedule);
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
        removeSlot(s,schedule);
        Slot newSlot = Slot.of(data, temp.getDuration(),temp.getLocal(),temp.getDescription());
        boolean add = addSlot(newSlot,schedule);
        if(add==false){
            System.out.println("No model não adicionou direito.");
            addSlot(temp,schedule);
            return temp;
        }
        System.out.println("No model adicionou direito.");
        return newSlot;
    }

    //------------------------
    // Dado o idenficador gerado ao nivel da interface ao percorrer a collection(schedule ou sch
    // Este identificador pode ser visto como temporário
    // Devolver o objecto que o identifica
    //------------------------
    public Slot getSlot(String infoSlot, Collection c){
        int id = Integer.parseInt(infoSlot);
        int index =0;
        for(Object s : c){
            if(index==id){
                return (Slot)s;
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
