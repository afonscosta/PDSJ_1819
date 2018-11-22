package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Utilities.BusinessUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;

import static Utilities.BusinessUtils.*;

public class CalcDateTimeLocalModel implements InterfCalcDateTimeLocalModel {

    private ZonedDateTime ldt;

    public static CalcDateTimeLocalModel of (ZoneId localZone) {
        return new CalcDateTimeLocalModel(localZone);
    }

    public static CalcDateTimeLocalModel of () {
        return new CalcDateTimeLocalModel();
    }

    // Está public para poder usar na subclasse
    public CalcDateTimeLocalModel(ZoneId localZone) {
        this.ldt = ZonedDateTime.now(localZone);
    }

    // Está public para poder usar na subclasse
    public CalcDateTimeLocalModel() {
        this.ldt = ZonedDateTime.now();
    }

    @Override
    public Temporal getDateTime() {
        return ldt;
    }

    @Override
    public void fromDateTime(ZonedDateTime newLDT) {
        this.ldt = ZonedDateTime.from(newLDT);
    }

    @Override
    public void shiftDateTime(int n, ChronoUnit cu) {
        ldt = (ZonedDateTime) BusinessUtils.shiftDateTime(ldt, n, cu);
    }

    @Override
    public void shiftWorkDaysDateTime(int n) {
        ldt = (ZonedDateTime) shiftWorkDays(ldt, n);
    }

    @Override
    public String diffDateTime(ZonedDateTime toDateTime) {
        return diffBetweenDateTime(ldt, toDateTime);
    }

    @Override
    public String diffWorkDaysDateTime(ZonedDateTime toDateTime) {
        return countWorkDays(ldt, toDateTime) + " dias úteis";
    }

    @Override
    public void withZone(String zid) {
        try {
            ldt = ldt.withZoneSameInstant(ZoneId.of(zid));
        } catch (ZoneRulesException e) {
            // Zona inexistente? Ignorar..
        }
    }
}
