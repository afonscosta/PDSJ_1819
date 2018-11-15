package Model.Interface;

import Utilities.EnumDateTimeShiftMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public interface InterfCalcDateTimeLocalModel {

    Temporal getDateTimeLocal();

    void shiftDateTimeLocal(int n, ChronoUnit cu, EnumDateTimeShiftMode mode);

    String diffDateTimeLocal(LocalDateTime toDateTime);

    void fromDateTimeLocal(LocalDateTime ldt);

    void shiftWorkDaysDateTimeLocal(int n, EnumDateTimeShiftMode mode);
}
