package Model.Class;

import Model.Interface.InterfCalcDateTimeScheduleModel;
import Utilities.Configs;
import java.io.Serializable;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import static Utilities.Utils.convertToZone;
import static Utilities.Utils.slotToString;
import static java.time.temporal.ChronoUnit.DAYS;

public class CalcDateTimeScheduleModel implements InterfCalcDateTimeScheduleModel,Serializable {
    private Set<Slot> schedule;
    private List<RestrictSlot> scheduleRestrictions;

    private final Comparator<Slot> slotComparator;
    static final long serialVersionUID = 1L;

    private CalcDateTimeScheduleModel() {
        slotComparator =
                (Comparator<Slot> & Serializable)(Slot s1, Slot s2) -> {
                    Temporal data1 = s1.getDate();
                    Temporal data2 = s2.getDate();
                    LocalDateTime ldt1 = LocalDateTime.from(data1);
                    LocalDateTime ldt2 = LocalDateTime.from(data2);

                    if (data1.equals(data2)) return 0;
                    else {
                        if (data1.getClass().getSimpleName().equals("ZonedDateTime")) {
                            ldt1 = convertToZone(data1, ZoneId.systemDefault()).toLocalDateTime();
                        }
                        if (data2.getClass().getSimpleName().equals("ZonedDateTime")) {
                            ldt2 = convertToZone(data2, ZoneId.systemDefault()).toLocalDateTime();
                        }
                        if (ldt1.isBefore(ldt2)) {
                            Duration d1 = s1.getDuration();
                            LocalDateTime data1Final = ldt1.plus(d1);
                            if (data1Final.isBefore(ldt2))
                                return -1;
                            else {
                                return 0;
                            }
                        } else {
                            Duration d2 = s2.getDuration();
                            LocalDateTime data2Final = ldt2.plus(d2);
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
    }

    @Override
    public Set<Slot> getSchedule() {
        return schedule;
    }

    @Override
    public List<RestrictSlot> getScheduleRestrictions() {
        return scheduleRestrictions;
    }

    @Override
    public void setSchedule(Set<Slot> schedule) {
        this.schedule = schedule;
    }

    //------------------------
    // Devolve todos os eventos existentes
    // É apresentado cada evento de uma forma aglutinada, pela sua data e local.
    // Se o evento estiver na zoned local, é seguido o formato para date time local.
    //------------------------
    @Override
    public List<String> getMainInfoSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        for(Slot s : schedule) {
            res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " [" + s.getLocal() + "]");
        }
        return res;
    }

    //------------------------
    // Devolve todas as restrições existentes
    // É apresentada cada restrição de forma aglutinada, pela sua data e tipo de restrição definida(diaria, semanal ou pontual).
    //------------------------
    @Override
    public List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        for(RestrictSlot s: scheduleRestrictions){
            res.add(s.getIdSlot() + ": " + slotToString(s, referenceZone, dtfLocal, dtfZone, false).get(0) + " [" + s.getPeriod() + "]");
        }
        return res;
    }

    //------------------------
    // ModeNormalized: diaria, semanal, mensal
    // want -> a igualdade a verficar no schedule
    // Por exemplo, utilizador escolhe diaria -> want diz-me o dia referente
    //------------------------
    @Override
    public List<String> getModeSlots(String modeNormalized, int want, ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        List<String> res= new ArrayList();
        for(Slot s : schedule) {
            ZonedDateTime date = ZonedDateTime.from(s.getDate());
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
    //Restrições que sejam definidas em que abrangem mais que um dia, teram de ser separadas, para cada dia ser tratado individualmente
    //------------------------
    private List<Slot> partitionSlot(Slot newSlot, ZoneId zoneId) {
        List<Slot> slotsToAdd = new ArrayList<>();
        long nextAvailableId = newSlot.getIdSlot() + 1;

        Duration durationNewSlot = newSlot.getDuration();
        LocalDateTime ldtNewSlot = convertToZone(newSlot.getDate(), zoneId).toLocalDateTime();
        Temporal finalDate = ldtNewSlot.plus(durationNewSlot);
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
                Temporal dateFromMidnight = slotFromMidnight.getDate();
                dateFromMidnight= dateFromMidnight.plus(1,DAYS);
                dateFromMidnight = dateFromMidnight.with(ChronoField.HOUR_OF_DAY,0);
                dateFromMidnight = dateFromMidnight.with(ChronoField.MINUTE_OF_DAY,0);
                slotFromMidnight.setDate(dateFromMidnight);
                slotFromMidnight.setDuration(durationFromMidnight);
                if(durationFromMidnight.toHours() < 24) {
                    //posso adicionar porque a sua duracao já foi restrita a menos de 24 horas
                    slotFromMidnight.setIdSlot(nextAvailableId++);
                    slotsToAdd.add(slotFromMidnight);
                }
                else {
                    newSlot = slotFromMidnight.clone();
                    newSlot.setIdSlot(nextAvailableId++);
                    ldtNewSlot = convertToZone(newSlot.getDate(), zoneId).toLocalDateTime();
                    durationNewSlot = newSlot.getDuration();
                }
            }
            while (durationFromMidnight.toHours() > 24);
        }
        else {
            slotsToAdd.add(newSlot);
        }
        return slotsToAdd;
    }

    //------------------------
    // compara em termos de LocalTime, dois slots
    //------------------------
    private boolean compareTime(LocalTime ldt1, LocalTime ldt2, Slot s1, Slot s2){
        if (ldt1.isBefore(ldt2)) {
            Duration d1 = s1.getDuration();
            LocalTime data1Final = ldt1.plus(d1);
            if (data1Final.isBefore(ldt2))
                return true;
            else {
                return false;
            }
        } else {
            Duration d2 = s2.getDuration();
            LocalTime data2Final = ldt2.plus(d2);
            if (data2Final.isBefore(ldt1))
                return true;
            else {
                return false;
            }
        }
    }

    //------------------------
    // compara em termos de LocalDateTime, dois slots
    //------------------------
    private boolean compareLocalDateTime(LocalDateTime ldt1, LocalDateTime ldt2, Slot newSlot, Slot s){
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
    // Só posso agendar um evento se não houver nenhuma restrição definida que sobreponha
    //------------------------
    private boolean eventDoesNotBreakAnyRestrict(Slot newSlot, ZoneId zoneId){
        boolean res;
        LocalDateTime ldtNewSlot = convertToZone(newSlot.getDate(), zoneId).toLocalDateTime();
        for(RestrictSlot s : scheduleRestrictions){
            LocalDateTime ldtRestrictSlot= LocalDateTime.from(s.getDate());
            if(s.getPeriod().equals("semanal")){
               if(ldtNewSlot.getDayOfWeek().equals(ldtRestrictSlot.getDayOfWeek())){
                   res = compareTime(LocalTime.from(ldtNewSlot),LocalTime.from(ldtRestrictSlot),newSlot,s);
                   if(!res) {
                       return false;
                   }
               }
            }
            else if(s.getPeriod().equals("pontual")){
                res = compareLocalDateTime(ldtNewSlot,ldtRestrictSlot,newSlot,s);
                if(!res) {
                    return false;
                }
            }
            else if(s.getPeriod().equals("diaria")){
                res = compareTime(LocalTime.from(ldtNewSlot),LocalTime.from(ldtRestrictSlot),newSlot,s);
                if(!res) {
                    return false;
                }
            }
        }
        return true;
    }

    //------------------------
    // Só posso agendar uma restrição se não houver nenhum evento definido que sobreponha
    //------------------------
    private boolean restrictDoesNotOverlapAnyEvent(RestrictSlot newRestrictSlot,ZoneId zoneId){
        boolean res;
        LocalDateTime ldtRestrictSlot = convertToZone(newRestrictSlot.getDate(),zoneId).toLocalDateTime();
        for(Slot s : schedule){
            LocalDateTime ldtSlot= LocalDateTime.from(s.getDate());
            if(newRestrictSlot.getPeriod().equals("semanal")){
                if(ldtSlot.getDayOfWeek().equals(ldtRestrictSlot.getDayOfWeek())){
                    res = compareTime(LocalTime.from(ldtRestrictSlot), LocalTime.from(ldtSlot), newRestrictSlot, s);
                    if(!res) {
                        return false;
                    }
                }
            }
            else if(newRestrictSlot.getPeriod().equals("pontual")){
                res = compareLocalDateTime(ldtRestrictSlot, ldtSlot, newRestrictSlot,s);
                if(!res) {
                    return false;
                }
            }
            else if(newRestrictSlot.getPeriod().equals("diaria")){
                res = compareTime(LocalTime.from(ldtRestrictSlot), LocalTime.from(ldtSlot), newRestrictSlot, s);
                if(!res) {
                    return false;
                }
            }
        }
        return true;
    }

    //------------------------
    // Adicionar um evento ao schedule ou adicionar uma restrição ao scheduleRestrictions
    // Só posso agendar um evento se não houver nenhuma restrição definida que sobreponha
    // Só posso adicionar uma restrição se não houver nenhum evento já agendado que quebra a restrição que se pretenda adicionar
    //------------------------
    @Override
    public boolean addSlot(Slot newSlot, ZoneId zoneId) {
        if (eventDoesNotBreakAnyRestrict(newSlot,zoneId)) {
            return schedule.add(newSlot);
        }
        return false;
    }

    @Override
    public int addRestrictSlot(RestrictSlot newSlot, ZoneId zoneId) {
        int numAdded = 0;
        List<Slot> slotsToAdd = partitionSlot(newSlot,zoneId);
        for (Slot s : slotsToAdd) {
            if (!restrictDoesNotOverlapAnyEvent((RestrictSlot) s,zoneId)) {
                numAdded = -1;
                break;
            }
        }
        if (numAdded == 0)
            for (Slot s : slotsToAdd) {
                scheduleRestrictions.add((RestrictSlot) s);
                numAdded++;
            }
        return numAdded;
    }

    //------------------------
    // Remove um evento
    //------------------------
    @Override
    public boolean removeSlot(Long idSlot){
        return schedule.remove(getSlot(idSlot));
    }

    //------------------------
    // Remove uma restrição
    //------------------------
    @Override
    public boolean removeRestrictSlot(Long idRestrictSlot){
        return scheduleRestrictions.remove(getRestrictSlot(idRestrictSlot));
    }

    //------------------------
    // Altera o local de um slot
    //------------------------
    @Override
    public void editLocalSlot(Long idSelectSlot, String newLocal) {
        Slot s= getSlot(idSelectSlot);
        s.setLocal(newLocal);
    }

    //------------------------
    // Altera a descrição de um slot
    //------------------------
    @Override
    public void editDescSlot(Long idSelectSlot, String newDesc) {
        Slot s = getSlot(idSelectSlot);
        s.setDescription(newDesc);
    }

    //------------------------
    // Alterar a duração de um evento
    // É necessário garantir que a nova duração não provoca sobreposições
    // Remover o slot antigo -> adicionar um novo slot, com a mesma info excepto a duração.
    //------------------------
    @Override
    public boolean editDurationSlot(Long idSelectSlot, Duration newDuration, ZoneId zoneId) {
        Slot s = getSlot(idSelectSlot);
        removeSlot(idSelectSlot);
        Slot temp = s.clone();
        temp.setDuration(newDuration);
        boolean added = addSlot(temp, zoneId);
        if (!added) {
            addSlot(s, zoneId);
        }
        return added;
    }

    //------------------------
    // Alterar a data de um evento
    // É necessário garantir que a nova data não provoca sobreposições
    // Remover o slot antigo -> adicionar um novo slot, com a mesma info excepto a data.
    //------------------------
    @Override
    public boolean editDateSlot(Long idSelectSlot, Temporal date, ZoneId zoneId){
        Slot s = getSlot(idSelectSlot);
        removeSlot(idSelectSlot);
        Slot temp = s.clone();
        temp.setDate(date);
        boolean added = addSlot(temp, zoneId);
        if (!added) {
            addSlot(s, zoneId);
        }
        return added;
    }

    @Override
    public Slot getSlot(Long idSlot){
        for(Slot s: schedule){
            if(s.getIdSlot()==idSlot)
                return s;
        }
        return null;
    }

    @Override
    public RestrictSlot getRestrictSlot(Long idSlot){
        for(RestrictSlot s: scheduleRestrictions){
            if(s.getIdSlot()==idSlot)
                return s;
        }
        return null;
    }

    @Override
    public boolean existSlot(Long idSlot){
        for(Slot s: schedule){
            if(s.getIdSlot()==idSlot)
               return true;
        }
        return false;
    }

    @Override
    public boolean existRestrictSlot(Long idSlot){
        for(Slot s: scheduleRestrictions){
            if(s.getIdSlot()==idSlot)
                return true;
        }
        return false;
    }
}
