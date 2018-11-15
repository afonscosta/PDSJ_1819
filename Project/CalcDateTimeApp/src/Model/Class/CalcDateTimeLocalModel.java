package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Utilities.BusinessUtils;
import Utilities.EnumDateTimeShiftMode;

import static Utilities.BusinessUtils.diffBetweenLocalDateTime;
import static Utilities.BusinessUtils.shiftWorkDaysLocal;
import static Utilities.BusinessUtils.countWorkDays;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import static java.time.temporal.ChronoUnit.DAYS;

public class CalcDateTimeLocalModel implements InterfCalcDateTimeLocalModel {

    LocalDateTime ldt;

    public CalcDateTimeLocalModel() {
        this.ldt = LocalDateTime.now();
    }

    @Override
    public Temporal getDateTimeLocal() {
        return ldt;
    }

    @Override
    public void fromDateTimeLocal(LocalDateTime newLDT) {
        this.ldt = LocalDateTime.from(newLDT);
    }

    @Override
    public void shiftDateTimeLocal(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        ldt = (LocalDateTime) BusinessUtils.shiftDateTime(ldt, n, cu, mode);
    }

    @Override
    public void shiftWorkDaysDateTimeLocal(int n, EnumDateTimeShiftMode mode) {
        ldt = shiftWorkDaysLocal(ldt, n, mode);
    }

    @Override
    public String diffDateTimeLocal(LocalDateTime toDateTime) {
        return diffBetweenLocalDateTime(ldt, toDateTime);
    }

    @Override
    public String diffWorkDaysDateTimeLocal(LocalDateTime toDateTime) {
        return countWorkDays(ldt, toDateTime) + " dias úteis";
    }
}
