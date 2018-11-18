package Model.Class;

import Model.Interface.InterfCalcDateTimeLocZonModel;
import Utilities.BusinessUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static Utilities.BusinessUtils.*;

public class CalcDateTimeLocZonModel implements InterfCalcDateTimeLocZonModel {

    private Map<String, ZonedDateTime> buffers;

    public CalcDateTimeLocZonModel() {
        this.buffers = new HashMap<>();
    }

    public Set<String> getKeys() {
        return buffers.keySet();
    }

    @Override
    public Temporal getDateTime(String k) {
        return buffers.get(k);
    }

    @Override
    public void fromDateTime(String k, ZonedDateTime newLDT) {
        this.buffers.put(k, ZonedDateTime.from(newLDT));
    }

    @Override
    public void shiftDateTime(String k, int n, ChronoUnit cu) {
        buffers.put(k, (ZonedDateTime) BusinessUtils.shiftDateTime(buffers.get(k), n, cu));
    }

    @Override
    public void shiftWorkDaysDateTime(String k, int n) {
        buffers.put(k, (ZonedDateTime) shiftWorkDays(buffers.get(k), n));
    }

    @Override
    public String diffDateTime(String k, ZonedDateTime toDateTime) {
        return diffBetweenDateTime(ZonedDateTime.from(buffers.get(k)), toDateTime);
    }

    @Override
    public String diffWorkDaysDateTime(String k, ZonedDateTime toDateTime) {
        return countWorkDays(ZonedDateTime.from(buffers.get(k)), toDateTime) + " dias úteis";
    }

    @Override
    public void convertZoneDateTimeToZone(String k, String zoneIdTxt) {
        try {
            ZoneId chosenZoneId = ZoneId.of(zoneIdTxt);

            buffers.put(k, buffers.get(k).withZoneSameInstant(chosenZoneId));
        } catch (ZoneRulesException e) {
            // Zona inexistente? Ignorar..
        }
    }

    @Override
    public void changeToCurrentDateInZone(String k, String zoneIdTxt) {
        try {
            ZoneId chosenZoneId = ZoneId.of(zoneIdTxt);

            buffers.put(k, ZonedDateTime.now().withZoneSameInstant(chosenZoneId));

        } catch (ZoneRulesException e) {
            // Zona inexistente? Ignorar..
        }

    }
}
