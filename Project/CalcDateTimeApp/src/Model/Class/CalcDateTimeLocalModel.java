package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Utilities.BusinessUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;

import static Utilities.BusinessUtils.*;

public class CalcDateTimeLocalModel implements InterfCalcDateTimeLocalModel {

    private ZonedDateTime ldt;
    private String localDateTimeFormat;

    public static CalcDateTimeLocalModel of () {
        return new CalcDateTimeLocalModel();
    }

    protected CalcDateTimeLocalModel() {
        // Iniciar variável
        this.ldt = ZonedDateTime.now();
        readConfFileAndLoadLocalRelated();
        // Colocar como default o "now" mas nesta nova zona
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
        ldt = (ZonedDateTime) BusinessUtils.shiftDateTime(ldt, n, cu);
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
            // Zona inexistente? Ignorar..
        }
    }

    @Override
    public void setLocalDateTimeFormat(String localDateTimeFormat) {
        this.localDateTimeFormat = localDateTimeFormat;

    }

    @Override
    public void setZoneId(ZoneId zoneId) {
        ldt = ldt.withZoneSameLocal(zoneId);
    }

    @Override
    public String getLocalDateTimeFormat() {
        return this.localDateTimeFormat;
    }

    @Override
    public ZoneId getZone() {
       return ldt.getZone();
    }

    private void readConfFileAndLoadLocalRelated() {

        String pathToConfFile = "./date_dict_conf.json";

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(pathToConfFile));

            JSONObject jsonObj = (JSONObject) obj;

            String localDateTimeFormat = (String) jsonObj.get("localDateTimeFormat");

            String zoneIdTxt = ((String) jsonObj.get("zoneId"));

            // Json character escape nos ficheiros, portanto temos de os remover
            zoneIdTxt = zoneIdTxt.replaceAll("\\\\","");

            // Variavel vem no formato "palavra", temos de remover pelicas
            zoneIdTxt = zoneIdTxt.replaceAll("\"","");

            ZoneId zoneId = ZoneId.of(zoneIdTxt);

            this.setLocalDateTimeFormat(localDateTimeFormat);
            this.setZoneId(zoneId);

            // Caso de erro, adicionar o default
        } catch (Exception e) {
            this.setLocalDateTimeFormat("yyyy/MM/dd H:m:s:n");
            this.setZoneId(ZoneId.systemDefault());
        }
    }
}
