package Model.Class;

import Utilities.EnumDateTimeShiftMode;
import Model.Interface.InterfCalcDateTimeLocZonModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Set;

public class CalcDateTimeModel implements InterfCalcDateTimeModel {

    private InterfCalcDateTimeLocZonModel modelLocZon;
    private InterfCalcDateTimeScheduleModel modelSchedule;

    public CalcDateTimeModel(InterfCalcDateTimeLocZonModel modelLocZon,
                             InterfCalcDateTimeScheduleModel modelSchedule) {
        this.modelLocZon = modelLocZon;
        this.modelSchedule = modelSchedule;
    }

    @Override
    public Set<String> getKeys() {
        return modelLocZon.getKeys();
    }

    @Override
    public Temporal getDateTime(String k) {
        return modelLocZon.getDateTime(k);
    }

    @Override
    public void convertZoneDateTimeToZone(String k, String zoneId) {
        modelLocZon.convertZoneDateTimeToZone(k, zoneId);
    }

    @Override
    public void changeToCurrentDateInZone(String k, String zoneId) {
        modelLocZon.changeToCurrentDateInZone(k, zoneId);

    }

    @Override
    public void shiftDateTime(String k, int n, ChronoUnit cu) {
        modelLocZon.shiftDateTime(k, n, cu);
    }

    @Override
    public void shiftWorkDaysDateTime(String k, int n) {
        modelLocZon.shiftWorkDaysDateTime(k, n);
    }

    @Override
    public String diffDateTime(String k, ZonedDateTime toDateTime) {
        return modelLocZon.diffDateTime(k, toDateTime);
    }

    @Override
    public String diffWorkDaysDateTime(String k, ZonedDateTime toDateTime) {
        return modelLocZon.diffWorkDaysDateTime(k, toDateTime);
    }

    @Override
    public void fromDateTime(String k, ZonedDateTime zdt) {
        modelLocZon.fromDateTime(k, zdt);
    }

    @Override
    public boolean addSlot(Slot newSlot){
        return modelSchedule.addSlot(newSlot);
    }
}
