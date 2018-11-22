package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.EnumEditSlotInfo;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

public class CalcDateTimeModel implements InterfCalcDateTimeModel {

    private InterfCalcDateTimeLocalModel modelLocal;
    private InterfCalcDateTimeZoneModel modelZone;
    private InterfCalcDateTimeScheduleModel modelSchedule;

    public CalcDateTimeModel(InterfCalcDateTimeLocalModel modelLocal,
                             InterfCalcDateTimeZoneModel modelZone,
                             InterfCalcDateTimeScheduleModel modelSchedule) {
        this.modelLocal = modelLocal;
        this.modelZone = modelZone;
        this.modelSchedule = modelSchedule;
    }

    //------------------------
    // Métodos Model Local
    //------------------------
    @Override
    public Temporal getDateTimeLocal() {
        return modelLocal.getDateTime();
    }

    @Override
    public void shiftDateTimeLocal(int n, ChronoUnit cu) {
        modelLocal.shiftDateTime(n, cu);
    }

    @Override
    public void shiftWorkDaysDateTimeLocal(int n) {
        modelLocal.shiftWorkDaysDateTime(n);
    }

    @Override
    public String diffDateTimeLocal(ZonedDateTime toDateTime) {
        return modelLocal.diffDateTime(toDateTime);
    }

    @Override
    public String diffWorkDaysDateTimeLocal(ZonedDateTime toDateTime) {
        return modelLocal.diffWorkDaysDateTime(toDateTime);
    }

    @Override
    public void fromDateTimeLocal(ZonedDateTime ldt) {
        modelLocal.fromDateTime(ldt);
    }

    @Override
    public void withZoneLocal(String zoneId) {
        modelLocal.withZone(zoneId);
    }

    //------------------------
    // Métodos Model Zone
    //------------------------
    @Override
    public Temporal getDateTimeZone() {
        return modelZone.getDateTime();
    }

    @Override
    public void shiftDateTimeZone(int n, ChronoUnit cu) {
        modelZone.shiftDateTime(n, cu);
    }

    @Override
    public void shiftWorkDaysDateTimeZone(int n) {
        modelZone.shiftWorkDaysDateTime(n);
    }

    @Override
    public String diffDateTimeZone(ZonedDateTime toDateTime) {
        return modelZone.diffDateTime(toDateTime);
    }

    @Override
    public String diffWorkDaysDateTimeZone(ZonedDateTime toDateTime) {
        return modelZone.diffWorkDaysDateTime(toDateTime);
    }

    @Override
    public void fromDateTimeZone(ZonedDateTime zdt) {
        modelZone.fromDateTime(zdt);
    }

    @Override
    public void withZoneZone(String zoneId) {
        modelZone.withZone(zoneId);
    }

    @Override
    public void changeToCurrentDateInZone(String zoneId) {
        modelZone.changeToCurrentDateInZone(zoneId);

    }

    //------------------------
    // Métodos Model Schedule
    //------------------------
    public boolean addSlot(Slot newSlot){
        return modelSchedule.addSlot(newSlot);
    }

    public void saveState(String nomeFicheiro) throws IOException {
        modelSchedule.saveState(nomeFicheiro);
    }
    public List<String> getMainInfoSlots(){
        return modelSchedule.getMainInfoSlots();
    }

    public boolean removeSlot(Slot slot){
        return modelSchedule.removeSlot(slot);
    }

    @Override
    public void editSlot(Slot s, EnumEditSlotInfo e, String edit) {
        modelSchedule.editSlot(s,e,edit);
    }

    @Override
    public Slot editDurationSlot(Slot s, Duration d) {
        return modelSchedule.editDurationSlot(s,d);
    }

    @Override
    public Slot editDateSLot(Slot s, Temporal data) {
        return modelSchedule.editDateSLot(s,data);
    }

    public Slot getSlot(String infoSlot){ return modelSchedule.getSlot(infoSlot);}

    public List<String> getRestrictSlots(String modeNormalized, int want){
        return modelSchedule.getRestrictSlots(modeNormalized,want);
    }
}
