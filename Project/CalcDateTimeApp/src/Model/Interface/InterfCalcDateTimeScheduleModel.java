package Model.Interface;

import Model.Class.RestrictSlot;
import Model.Class.Slot;
import Utilities.Configs;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface InterfCalcDateTimeScheduleModel {

    void loadConfigs(Configs configs);

    void initNextAvailableID();

    Collection getSchedule();

    void setSchedule(Set<Slot> schedule);

    Collection getScheduleRestrictions();

    List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getModeSlots(String modeNormalized, int want,ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone);

    //------------------------
    // Adicionar um evento ao schedule ou adicionar uma restrição ao scheduleRestrictions
    // Só posso agendar um evento se não houver nenhuma restrição definida que sobreponha
    // Só posso adicionar uma restrição se não houver nenhum evento já agendado que quebra a restrição que se pretenda adicionar
    //------------------------
    boolean addSlot(Slot newSlot);

    int addRestrictSlot(RestrictSlot newSlot);

    boolean removeSlot(Long idSlot);

    boolean removeRestrictSlot(Long idRestrictSlot);

    boolean editDurationSlot(Long idSlot, Duration d);

    boolean editDateSlot(Long idSlot, Temporal data);

    Slot getSlot(Long infoSlot);

    RestrictSlot getRestrictSlot(Long infoSlot);


    void editLocalSlot(Long idSlot, String edit);

    void editDescSlot(Long idSlot, String edit);

    boolean existSlot(Long idSlot);

    boolean existRestrictSlot(Long idSlot);

}

