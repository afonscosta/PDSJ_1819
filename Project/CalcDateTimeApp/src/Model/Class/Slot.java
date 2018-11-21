package Model.Class;

import java.time.Duration;
import java.time.temporal.Temporal;

public class Slot {
    private Temporal data;
    private Duration duration;
    private String local;
    private String description;

    public Slot(Temporal data, Duration duration, String local, String description) {
        this.data = data;
        this.duration = duration;
        this.local = local;
        this.description = description;
    }

    public Temporal getData() {
        return data;
    }

    public void setData(Temporal data) {
        this.data = data;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Slot clone(){
        Slot res = new Slot(data,duration,local,description);
        return res;
    }
}
