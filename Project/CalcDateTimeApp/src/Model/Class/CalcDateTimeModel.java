package Model.Class;

import Utilities.EnumDateTimeShiftMode;
import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Set;

public class CalcDateTimeModel implements InterfCalcDateTimeModel {

    InterfCalcDateTimeLocalModel modelLocal;
    InterfCalcDateTimeZoneModel modelZone;
    InterfCalcDateTimeScheduleModel modelSchedule;

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

    public void saveState(String nomeFicheiro) throws FileNotFoundException, IOException{
        modelSchedule.saveState(nomeFicheiro);
    }
}
