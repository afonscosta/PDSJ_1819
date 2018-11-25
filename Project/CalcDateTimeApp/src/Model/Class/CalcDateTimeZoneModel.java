package Model.Class;

import Model.Interface.InterfCalcDateTimeZoneModel;
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
        readConfFileAndLoadZonedRelated();
    }

    public static CalcDateTimeZoneModel of () {
        return new CalcDateTimeZoneModel();
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

    private void readConfFileAndLoadZonedRelated() {
        String pathToConfFile = "./date_dict_conf.json";

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(pathToConfFile));

            JSONObject jsonObj = (JSONObject) obj;

            String zoneDateTimeFormat = (String) jsonObj.get("zoneDateTimeFormat");

            this.setZoneDateTimeFormat(zoneDateTimeFormat);

            // Caso de erro, adicionar o default
        } catch (IOException | ParseException e) {
            this.setZoneDateTimeFormat("yyyy/MM/dd H:m:s:n - VV");
        }
    }
}
