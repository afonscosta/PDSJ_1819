package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;
import Utilities.Configs;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

import static Utilities.BusinessUtils.convertToZone;
import static Utilities.BusinessUtils.slotToString;
import static java.time.temporal.ChronoUnit.DAYS;

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel,Serializable {
    private Set<Slot> schedule;
    private List<RestrictSlot> scheduleRestrictions;

    private Comparator<Slot> slotComparator;
    static final long serialVersionUID = 1L;

    private CalcDateTimeScheduleModel() {
        slotComparator =
                (Comparator<Slot> & Serializable)(Slot s1, Slot s2) -> {
                    Temporal data1 = s1.getData();
                    Temporal data2 = s2.getData();
                    LocalDateTime ldt1 = LocalDateTime.from(data1);
                    LocalDateTime ldt2 = LocalDateTime.from(data2);
                    ZoneId referenceZone = getReferenceZone();

                    if (data1.equals(data2)) return 0;
                    else {
                        if (data1.getClass().getSimpleName().equals("ZonedDateTime")) {
                            ldt1 = convertToZone(data1, referenceZone).toLocalDateTime();
                        }
                        if (data2.getClass().getSimpleName().equals("ZonedDateTime")) {
                            ldt2 = convertToZone(data2,referenceZone).toLocalDateTime();
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
        this.schedule= new TreeSet<>(slotComparator);
        this.scheduleRestrictions= new ArrayList<>();
    }

    public static CalcDateTimeScheduleModel of() {
        return new CalcDateTimeScheduleModel();
    }
    @Override
    public void loadConfigs(Configs configs) {
        if (configs.getSchedule() != null) {
            this.schedule.addAll(configs.getSchedule());
        }
        if (configs.getScheduleRestrictions() != null) {
            this.scheduleRestrictions = configs.getScheduleRestrictions();
        }
        long nSlot;
        long nRestrictSlot;

        if(schedule.size()>0) {
            nSlot = schedule.stream().mapToLong(x -> x.getIdSlot()).max().getAsLong() + 1;
        }
        else
            nSlot=0;
        Slot.loadAvailableId(nSlot);
        if(scheduleRestrictions.size()>0) {
            nRestrictSlot = scheduleRestrictions.stream().mapToLong(x -> x.getIdSlot()).max().getAsLong() + 1;
        }
        else
            nRestrictSlot=0;
        RestrictSlot.loadAvailableId(nRestrictSlot);
    }

    //------------------------
    // Retorna o ZonedId de definido no ficheiro de configurações
    //------------------------
    private ZoneId getReferenceZone(){
        try {
            FileInputStream fis = new FileInputStream("./Configs");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Configs configs = (Configs) ois.readObject();
            ois.close();
            fis.close();
            return ZoneId.of(configs.getZoneId());

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
        for(Slot s : schedule) {
            res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " || " + s.getLocal());
        }
        return res;
    }

    //------------------------
    // Devolve todas as restrições existentes
    // É apresentada cada restrição de forma aglutinada, pela sua data e tipo de restrição definida(diaria, semanal ou pontual).
    //------------------------

    public List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        for(RestrictSlot s: scheduleRestrictions){
            res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " || " + s.getPeriod());
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
        for(Slot s : schedule) {
            ZonedDateTime date = ZonedDateTime.from(s.getData());
            switch (modeNormalized) {
                case "diaria":
                    if(date.getDayOfMonth()==want){
                        res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " || " + s.getLocal());
                    }
                    break;
                case "semanal":
                    TemporalField woy = WeekFields.ISO.weekOfYear();
                    int weekNumber = date.get(woy);
                    if(weekNumber==want){
                        res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " || " + s.getLocal());
                    }
                    break;
                case "mensal":
                    if(date.getMonthValue()==want){
                        res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " || " + s.getLocal());
                    }
                    break;
                default: break;
            }
        }
        return res;
    }

    //------------------------
    //Restrições que sejam definidas em que abrangem mais que uma, teram de ser separadas, para cada dia ser tratado individulamente
    //------------------------
    public List<Slot> partitionSlot(Slot newSlot) {
        List<Slot> slotsToAdd = new ArrayList<>();

        Duration durationNewSlot = newSlot.getDuration();
        LocalDateTime ldtNewSlot = convertToZone(newSlot.getData(), getReferenceZone()).toLocalDateTime();
        System.out.println("ldt1->" + ldtNewSlot);
        Temporal finalDate = ldtNewSlot.plus(durationNewSlot);
        System.out.println("finalDate->" + finalDate);
        //A duracao definida faz com passe da meia noite
        if (!LocalDate.from(ldtNewSlot).equals(LocalDate.from(finalDate))) {
            Duration durationFromMidnight;
            do {
                LocalDateTime ldt2 = LocalDateTime.from(ldtNewSlot);
                //defino o limite superior do dia
                ldt2 = ldt2.withHour(23);
                ldt2 = ldt2.withMinute(59);
                //duracao até ao limite deste dia
                Duration durationUntilMidnight = Duration.between(ldtNewSlot, ldt2);
                Slot slotUntilMidnight = newSlot.clone();
                slotUntilMidnight.setDuration(durationUntilMidnight);
                slotsToAdd.add(slotUntilMidnight);

                //duracao que ainda falta considerar
                durationFromMidnight = durationNewSlot.minus(durationUntilMidnight);
                Slot slotFromMidnight = newSlot.clone();
                //avanço no dia e defino no limite inferior do dia
                Temporal dateFromMidnight = slotFromMidnight.getData();
                dateFromMidnight= dateFromMidnight.plus(1,DAYS);
                dateFromMidnight = dateFromMidnight.with(ChronoField.HOUR_OF_DAY,0);
                dateFromMidnight = dateFromMidnight.with(ChronoField.MINUTE_OF_DAY,0);
                slotFromMidnight.setData(dateFromMidnight);
                slotFromMidnight.setDuration(durationFromMidnight);
                if(durationFromMidnight.toHours() < 24) {
                    //posso adicionar porque a sua duracao já foi restrita a menos de 24 horas
                    slotFromMidnight.setIdSlot(RestrictSlot.getAndSetNextAvailableId(1));
                    slotsToAdd.add(slotFromMidnight);
                }
                else {
                    newSlot = slotFromMidnight.clone();
                    newSlot.setIdSlot(RestrictSlot.getAndSetNextAvailableId(1));
                    ldtNewSlot = convertToZone(newSlot.getData(), getReferenceZone()).toLocalDateTime();
                    durationNewSlot = newSlot.getDuration();
                }
            }
            while (durationFromMidnight.toHours() > 24);
        }
        else {
            System.out.println("Não há problemas no slot");
            slotsToAdd.add(newSlot);
        }
        System.out.println("-------SLOTS TO ADD:");
        for (Slot s : slotsToAdd) {
            System.out.println(s.getData() + "," + s.getDuration());
        }
        return slotsToAdd;
    }

    //------------------------
    // compara em termos de LocalTime dois slots
    //------------------------
    public boolean compareTime(LocalTime ldt1, LocalTime ldt2, Slot newSlot, Slot s){
        if (ldt1.isBefore(ldt2)) {
            Duration d1 = newSlot.getDuration();
            LocalTime data1Final = ldt1.plus(d1);
            if (data1Final.isBefore(ldt2))
                return true;
            else {
                return false;
            }
        } else {
            Duration d2 = s.getDuration();
            LocalTime data2Final = ldt2.plus(d2);
            if (data2Final.isBefore(ldt1))
                return true;
            else {
                return false;
            }
        }
    }

    //------------------------
    // compara em termos de LocalDateTime dois slots
    //------------------------
    public boolean compareLocalDateTime(LocalDateTime ldt1, LocalDateTime ldt2, Slot newSlot, Slot s){
        if (ldt1.isBefore(ldt2)) {
            Duration d1 = newSlot.getDuration();
            LocalDateTime data1Final = ldt1.plus(d1);
            if (data1Final.isBefore(ldt2))
                return true;
            else {
                return false;
            }
        } else {
            Duration d2 = s.getDuration();
            LocalDateTime data2Final = ldt2.plus(d2);
            if (data2Final.isBefore(ldt1))
                return true;
            else {
                return false;
            }
        }
    }

    //------------------------
    // Só posso agendar uma reunião se não houver nenhuma restrição definida que sobreponha
    //------------------------
    public boolean doesNotBreakAnyRestrict(Slot newSlot){
        boolean res;
        LocalDateTime ldtNewSlot = convertToZone(newSlot.getData(), getReferenceZone()).toLocalDateTime();
        for(RestrictSlot s : scheduleRestrictions){
            LocalDateTime ldtRestrictSlot= LocalDateTime.from(s.getData());
            if(s.getPeriod().equals("semanal")){
               if(ldtNewSlot.getDayOfWeek().equals(ldtRestrictSlot.getDayOfWeek())){
                   res = compareTime(LocalTime.from(ldtNewSlot),LocalTime.from(ldtRestrictSlot),newSlot,s);
                   if(res==false) {
                        System.out.println("SOBREPOSICAO COM A RESTRICAO: " + ldtRestrictSlot);
                       return false;
                   }
               }
            }
            else if(s.getPeriod().equals("pontual")){
                res = compareLocalDateTime(ldtNewSlot,ldtRestrictSlot,newSlot,s);
                if(res==false) {
                    System.out.println("SOBREPOSICAO COM A RESTRICAO: " + ldtRestrictSlot);
                    return false;
                }
            }
            else if(s.getPeriod().equals("diaria")){
                res = compareTime(LocalTime.from(ldtNewSlot),LocalTime.from(ldtRestrictSlot),newSlot,s);
                if(res==false) {
                    System.out.println("SOBREPOSICAO COM A RESTRICAO: " + ldtRestrictSlot);
                    return false;
                }
            }
        }
        return true;
    }

    //------------------------
    // Só posso adicionar uma restrição se não houver nenhuma reunião já agendada que quebra a restrição que se pretenda adicionar
    //------------------------

    public boolean doesNotOverlapAnySchedule(List<Slot> slotsToAdd,Collection c){
        boolean state=true;
        boolean res;
        for(Slot sToAdd : slotsToAdd) {
            c.add(sToAdd);
            for (Slot s : schedule) {
                if (!doesNotBreakAnyRestrict(s)) {
                    System.out.println("SOBREPOSICAO COM A REUNIAO AGENDADA->" + s.getData());
                    c.remove(sToAdd);
                    RestrictSlot.abortLastUpdateToNextId();
                    state = false;
                    break;
                }
            }
            if(!state)
                break;

        }
        if(!state) {
            for (Slot sToRemove : slotsToAdd) {
                res = c.remove(sToRemove);
                if(res){
                    RestrictSlot.abortLastUpdateToNextId();
                }

            }
        }
            return state;
    }

    //------------------------
    // Adicionar uma reunião ao schedule ou adicionar uma restrição ao scheduleRestrictions
    // Só posso agendar uma reunião se não houver nenhuma restrição definida que sobreponha
    // Só posso adicionar uma restrição se não houver nenhuma reunião já agendada que quebra a restrição que se pretenda adicionar
    //------------------------
    public boolean addSlot(Slot newSlot, Collection c) {
        boolean res;
        if(newSlot.getClass().getSimpleName().equals("Slot")){
            if(doesNotBreakAnyRestrict(newSlot)){
                  res = c.add(newSlot);
                  if(!res)
                      Slot.abortLastUpdateToNextId();
                  return res;
            }
            else{
                Slot.abortLastUpdateToNextId();
            }
        }
        else {
            List<Slot> slotstoAdd = partitionSlot(newSlot);
            return doesNotOverlapAnySchedule(slotstoAdd, c);
        }
        return false;
    }

    //------------------------
    // Remover uma reunião da schedule
    //------------------------
    public boolean removeSlot(Slot s, Collection c){
        return c.remove(s);
    }

    //------------------------
    // Altera o local de um slot
    //------------------------
    public void editLocalSlot(Long idSelectSlot, String newLocal) {
        Slot s= getSlot(idSelectSlot);
        s.setLocal(newLocal);
    }

    //------------------------
    // Altera a descrição de um slot
    //------------------------
    public void editDescSlot(Long idSelectSlot, String newDesc) {
        Slot s = getSlot(idSelectSlot);
        s.setDescription(newDesc);
    }

    //------------------------
    // Alterar a duração de uma reuniao
    // É necessário garantir que a nova duração não provoca sobreposições
    // Remover o slot antigo -> adicionar um novo slot, com a mesma info excepto a duração.
    //------------------------
    public boolean editDurationSlot(Long idSelectSlot, Duration newDuration) {
        Slot s = getSlot(idSelectSlot);
        Slot temp = s.clone();
        removeSlot(s,schedule);
        Slot newSlot = Slot.of(temp.getIdSlot(),temp.getData(),newDuration,temp.getLocal(),temp.getDescription());
        boolean add = addSlot(newSlot,schedule);
        if(add==false) {
            addSlot(temp,schedule);
            Slot.setNextAvailableId(1); //como o add falhou anteriormente, ele retrocede. Neste caso, não devia porque é um calculo intermédio.
        }
        return add;
    }
    //------------------------
    // Alterar a data de uma reuniao
    // É necessário garantir que a nova data não provoca sobreposições
    // Remover o slot antigo -> adicionar um novo slot, com a mesma info excepto a data.
    //------------------------
    public boolean editDateSLot(Long idSelectSlot, Temporal data){
        Slot s = getSlot(idSelectSlot);
        Slot temp = s.clone();
        removeSlot(s,schedule);
        Slot newSlot = Slot.of(temp.getIdSlot(),data, temp.getDuration(),temp.getLocal(),temp.getDescription());
        boolean add = addSlot(newSlot,schedule);
        if(add==false){
            addSlot(temp,schedule);
            Slot.setNextAvailableId(1); //como o add falhou anteriormente, ele retrocede. Neste caso, não devia porque é um calculo intermédio.
        }
        return add;
    }

    //------------------------
    // Dado o idenficador gerado ao nivel da interface ao percorrer a collection(schedule ou sch
    // Este identificador pode ser visto como temporário
    // Devolver o objecto que o identifica
    //------------------------
    public Slot getSlot(Long idSlot){
        for(Slot s: schedule){
            if(s.getIdSlot()==idSlot)
                return s;
        }
        return null;
    }

    public Slot getRestrictSlot(Long idSlot){
        for(Slot s: scheduleRestrictions){
            if(s.getIdSlot()==idSlot)
                return s;
        }
        return null;
    }

    public boolean existSlot(Long idSlot){
        for(Slot s: schedule){
            if(s.getIdSlot()==idSlot)
               return true;
        }
        return false;
    }

    public boolean existRestrictSlot(Long idSlot){
        for(Slot s: scheduleRestrictions){
            if(s.getIdSlot()==idSlot)
                return true;
        }
        return false;
    }
}
