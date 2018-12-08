package Model.Class;

import java.time.Duration;
import java.time.temporal.Temporal;

public class RestrictSlot extends Slot {

    private String period; // "pontual", "semanal" , "diaria"

    public static RestrictSlot of (long idRestrictSlot, Temporal data, Duration duration, String local, String description,String period) {
        return new RestrictSlot(idRestrictSlot, data,duration,local,description,period);
    }

    public static RestrictSlot of (RestrictSlot s) {
        return new RestrictSlot(s);
    }

    private RestrictSlot(long idRestrictSlot, Temporal data, Duration duration, String local, String description,String period) {
        super(idRestrictSlot,data,duration,local,description);
        this.period = period;
    }

    private RestrictSlot(RestrictSlot s) {
        super(s);
        this.period = s.getPeriod();
    }

    public String getPeriod() {
        return period;
    }

    public RestrictSlot clone(){
        RestrictSlot res = of(this);
        return res;
    }
}
