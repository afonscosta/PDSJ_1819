package Model.Class;

import Model.Interface.InterfCalcDateTimeZoneModel;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;

public class CalcDateTimeZoneModel extends CalcDateTimeLocalModel implements InterfCalcDateTimeZoneModel {

    private CalcDateTimeZoneModel() {
        super();
    }

    public static CalcDateTimeZoneModel of () {
        return new CalcDateTimeZoneModel();
    }

    @Override
    public Temporal getDateTime() {
        return super.getDateTime();
    }

    @Override
    public void withZone(String zoneIdTxt) {
        try {
            ZoneId chosenZoneId = ZoneId.of(zoneIdTxt);
            super.fromDateTime(((ZonedDateTime) super.getDateTime()).withZoneSameInstant(chosenZoneId));
        } catch (ZoneRulesException e) {
            // Zona inexistente? Ignorar..
        }
    }

    @Override
    public void changeToCurrentDateInZone(String zoneIdTxt) {
        try {
            ZoneId chosenZoneId = ZoneId.of(zoneIdTxt);

            super.fromDateTime(ZonedDateTime.now().withZoneSameInstant(chosenZoneId));
        } catch (ZoneRulesException e) {
            // Zona inexistente? Ignorar..
        }

    }
}
