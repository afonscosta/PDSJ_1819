package Model.Interface;

public interface InterfCalcDateTimeZoneModel extends InterfCalcDateTimeLocalModel{

    void withZone(String zoneIdTxt);

    void changeToCurrentDateInZone(String zoneIdTxt);
}
