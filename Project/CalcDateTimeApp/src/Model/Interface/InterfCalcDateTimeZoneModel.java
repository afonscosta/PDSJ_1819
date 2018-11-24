package Model.Interface;

public interface InterfCalcDateTimeZoneModel extends InterfCalcDateTimeLocalModel {

    void changeToCurrentDateInZone(String zoneIdTxt);

    void setZoneDateTimeFormat(String zoneDateTimeFormat);

    String getZoneDateTimeFormat();
}
