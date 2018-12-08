package Model.Interface;

import Utilities.Configs;

import java.time.ZoneId;

public interface InterfCalcDateTimeZoneModel extends InterfCalcDateTimeLocalModel {

    void loadConfigs(Configs configs);

    ZoneId getZone();
}
