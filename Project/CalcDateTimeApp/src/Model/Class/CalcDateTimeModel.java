package Model.Class;

import Utilities.EnumDateTimeShiftMode;
import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.EnumEditSlotInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Set;

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

    @Override
    public Temporal getDateTimeLocal() {
        return modelLocal.getDateTimeLocal();
    }

    @Override
    public Temporal getDateTimeZone() {
        return modelZone.getDateTimeZone();
    }

    public void convertZoneDateTimeToZone(String zoneId) {
        modelZone.convertZoneDateTimeToZone(zoneId);
    }

    @Override
    public void changeZoneDateTimeToCurrentDateInZone(String zoneId) {
        modelZone.changeZoneDateTimeToCurrentDateInZone(zoneId);

    }

    @Override
    public void shiftDateTimeLocal(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        modelLocal.shiftDateTimeLocal(n, cu, mode);
    }

    @Override
    public void shiftWorkDaysDateTimeLocal(int n, EnumDateTimeShiftMode mode) {
        modelLocal.shiftWorkDaysDateTimeLocal(n, mode);
    }

    @Override
    public String diffDateTimeLocal(LocalDateTime toDateTime) {
        return modelLocal.diffDateTimeLocal(toDateTime);
    }

    @Override
    public String diffWorkDaysDateTimeLocal(LocalDateTime toDateTime) {
        return modelLocal.diffWorkDaysDateTimeLocal(toDateTime);
    }

    @Override
    public void fromDateTimeLocal(LocalDateTime ldt) {
        modelLocal.fromDateTimeLocal(ldt);
    }

    @Override
    public void shiftDateTimeZone(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        modelZone.shiftDateTimeZone(n, cu, mode);
    }

    public boolean addSlot(Slot newSlot){
        return modelSchedule.addSlot(newSlot);
    }

    public void saveState(String nomeFicheiro) throws IOException{
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
