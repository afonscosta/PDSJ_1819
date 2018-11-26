package Model.Interface;

import Utilities.Configs;

public interface InterfCalcDateTimeZoneModel extends InterfCalcDateTimeLocalModel {

    void loadConfigs(Configs configs);

    void changeToCurrentDateInZone(String zoneIdTxt);

    void setZoneDateTimeFormat(String zoneDateTimeFormat);

    String getZoneDateTimeFormat();
}
