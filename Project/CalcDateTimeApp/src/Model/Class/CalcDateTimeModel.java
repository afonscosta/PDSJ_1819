package Model.Class;

import Model.Interface.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

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
    public void withZone(String zoneId) {
        modelZone.withZone(zoneId);
    }

    @Override
    public void changeToCurrentDateInZone(String zoneId) {
        modelZone.changeToCurrentDateInZone(zoneId);

    }

    //------------------------
    // Métodos Model Schedule
    //------------------------
    @Override
    public boolean addSlot(Slot newSlot){
        return modelSchedule.addSlot(newSlot);
    }
}
