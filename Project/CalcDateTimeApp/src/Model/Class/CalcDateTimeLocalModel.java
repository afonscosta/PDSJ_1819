package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Utilities.Utils;
import Utilities.Configs;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;

import static Utilities.Utils.*;

public class CalcDateTimeLocalModel implements InterfCalcDateTimeLocalModel {

    private ZonedDateTime ldt;

    protected CalcDateTimeLocalModel() {
        this.ldt = ZonedDateTime.now();
    }

    public static CalcDateTimeLocalModel of () {
        return new CalcDateTimeLocalModel();
    }

    @Override
    public void loadConfigs(Configs configs) {
        this.setZoneId(configs.getZoneId());
        // Reset do buffer para data atual
        this.ldt = ZonedDateTime.now(this.ldt.getZone());
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
        ldt = (ZonedDateTime) Utils.shiftDateTime(ldt, n, cu);
    }

    @Override
    public void shiftWorkDaysDateTime(int n) {
        ZonedDateTime newLDT = (ZonedDateTime) shiftWorkDays(ldt, n);
        if (newLDT != null)
            ldt = newLDT;
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
            // Se a zona não for válida, não efetuar a conversão
        }
    }

    @Override
    public void setZoneId(ZoneId zoneId) {
        ldt = ldt.withZoneSameLocal(zoneId);
    }
}
