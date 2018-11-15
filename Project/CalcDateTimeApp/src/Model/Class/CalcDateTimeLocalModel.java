package Model.Class;

import Enum.EnumDateTimeShiftMode;
import Model.Interface.InterfCalcDateTimeLocalModel;
import Utilities.BusinessUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

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
    public void shiftDateTimeLocal(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        ldt = (LocalDateTime) BusinessUtils.shiftDateTime(ldt, n, cu, mode);
    }

    @Override
    public String diffDateTimeLocal(LocalDateTime toDateTime) {
        StringBuilder sb = new StringBuilder();

        LocalDateTime tempLDT = LocalDateTime.from(ldt);

        long years = tempLDT.until( toDateTime, ChronoUnit.YEARS);
        tempLDT = tempLDT.plusYears( years );
        sb.append(years + " anos ");

        long months = tempLDT.until( toDateTime, ChronoUnit.MONTHS);
        tempLDT = tempLDT.plusMonths( months );
        sb.append(months + " meses ");

        long days = tempLDT.until( toDateTime, ChronoUnit.DAYS);
        tempLDT = tempLDT.plusDays( days );
        sb.append(days + " dias ");

        long hours = tempLDT.until( toDateTime, ChronoUnit.HOURS);
        tempLDT = tempLDT.plusHours( hours );
        sb.append(hours + " horas ");

        long minutes = tempLDT.until( toDateTime, ChronoUnit.MINUTES);
        tempLDT = tempLDT.plusMinutes( minutes );
        sb.append(minutes + " minutos ");

        long seconds = tempLDT.until( toDateTime, ChronoUnit.SECONDS);
        tempLDT = tempLDT.plusSeconds( seconds );
        sb.append(seconds + " segundos ");

        long nanos = tempLDT.until( toDateTime, ChronoUnit.NANOS);
        tempLDT = tempLDT.plusNanos( nanos );
        sb.append(nanos + " nanosegundos ");

        return sb.toString();
    }

    @Override
    public void fromDateTimeLocal(LocalDateTime newLDT) {
        this.ldt = LocalDateTime.from(newLDT);
    }
}
