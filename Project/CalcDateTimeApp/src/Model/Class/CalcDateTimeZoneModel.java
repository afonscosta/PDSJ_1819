package Model.Class;

import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.EnumDateTimeShiftMode;
import Utilities.BusinessUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.zone.ZoneRulesException;
import java.util.Set;

public class CalcDateTimeZoneModel implements InterfCalcDateTimeZoneModel {

    ZonedDateTime zdt;

    public CalcDateTimeZoneModel() {
        this.zdt = ZonedDateTime.now();
    }

    @Override
    public Temporal getDateTimeZone() {
        return zdt;
    }

    @Override
    public void shiftDateTimeZone(int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        zdt = (ZonedDateTime) BusinessUtils.shiftDateTime(zdt, n, cu, mode);
    }

    @Override
    public void convertZoneDateTimeToZone(String zoneIdtxt) {
        System.out.println("Before " + zdt);
        try {
            ZoneId chosenZoneId = ZoneId.of(zoneIdtxt);
            LocalDateTime currentZoneTime = zdt.toLocalDateTime();
            zdt = zdt.withZoneSameInstant(chosenZoneId);
        } catch (ZoneRulesException e) {
        }
        System.out.println("After " + zdt);
    }
}
