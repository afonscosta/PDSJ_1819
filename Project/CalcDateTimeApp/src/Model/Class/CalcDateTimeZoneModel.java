package Model.Class;

import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.Configs;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;
import java.util.Comparator;

public class CalcDateTimeZoneModel extends CalcDateTimeLocalModel implements InterfCalcDateTimeZoneModel {

    private String zoneDateTimeFormat;


    private CalcDateTimeZoneModel() {
        super();
        this.zoneDateTimeFormat = "yyyy/MM/dd HH:mm - VV";
    }

    public static CalcDateTimeZoneModel of () {
        return new CalcDateTimeZoneModel();
    }


    public void loadConfigs(Configs configs) {
        super.loadConfigs(configs);
        this.setZoneDateTimeFormat(configs.getZoneDateTimeFormat());
    }

    @Override
    public Temporal getDateTime() {
        return super.getDateTime();
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

/*    @Override
    public String getLocalDateTimeFormat() {
        return null;
    }*/

    @Override
    public void setZoneDateTimeFormat(String zoneDateTimeFormat) {
        this.zoneDateTimeFormat = zoneDateTimeFormat;
    }

    @Override
    public String getZoneDateTimeFormat() {
        return this.zoneDateTimeFormat;
    }
}
