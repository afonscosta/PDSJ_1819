package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Utilities.Configs;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;

public class CalcDateTimeLocalModel implements InterfCalcDateTimeLocalModel {

    private ZonedDateTime ldt;

    public static CalcDateTimeLocalModel of () {
        return new CalcDateTimeLocalModel();
    }

    CalcDateTimeLocalModel() {
        this.ldt = ZonedDateTime.now();
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
