package Model.Class;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.Temporal;

public class Slot implements Serializable {
    private long idSlot;
    private Temporal date;
    private Duration duration;
    private String local;
    private String description;
    static final long serialVersionUID = 2L;

    public static Slot of (Long idSlot,Temporal date, Duration duration, String local, String description) {
        return new Slot(idSlot,date,duration,local,description);
    }

    public static Slot of (Slot s) {
        return new Slot(s);
    }

    protected Slot(Long idSlot, Temporal date, Duration duration, String local, String description) {
        this.idSlot = idSlot;
        this.date = date;
        this.duration = duration;
        this.local = local;
        this.description = description;
    }

    protected Slot(Slot s) {
        this.idSlot= s.getIdSlot();
        this.date = s.getDate();
        this.duration = s.getDuration();
        this.local = s.getLocal();
        this.description = s.getDescription();
    }

    public long getIdSlot() {
        return idSlot;
    }

    public void setIdSlot(long idSlot) {
        this.idSlot = idSlot;
    }

    public Temporal getDate() {
        return date;
    }

    public void setDate(Temporal date) {
        this.date = date;
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
        Slot res = of(this);
        return res;
    }
}
