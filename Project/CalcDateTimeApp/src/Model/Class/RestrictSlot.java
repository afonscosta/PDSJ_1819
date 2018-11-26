package Model.Class;

import java.time.Duration;
import java.time.temporal.Temporal;

public class RestrictSlot extends Slot {

    private String period;

    public static RestrictSlot of (Temporal data, Duration duration, String local, String description,String period) {
        return new RestrictSlot(data,duration,local,description,period);
    }

    private RestrictSlot(Temporal data, Duration duration, String local, String description,String period) {
        super(data,duration,local,description);
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
