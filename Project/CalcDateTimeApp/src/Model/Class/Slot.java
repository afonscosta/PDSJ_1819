package Model.Class;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.Temporal;

public class Slot implements Serializable {
    private static long nextAvailableId=0;
    private long idSlot;
    private Temporal data;
    private Duration duration;
    private String local;
    private String description;
    static final long serialVersionUID = 2L;

    public static Slot of (Temporal data, Duration duration, String local, String description) {
        return new Slot(data,duration,local,description);
    }
    public static Slot of (Long idSlot,Temporal data, Duration duration, String local, String description) {
        return new Slot(idSlot,data,duration,local,description);
    }
    public static Slot of (Slot s) {
        return new Slot(s);
    }

    protected Slot(Temporal data, Duration duration, String local, String description) {
        this.idSlot = nextAvailableId;
        this.data = data;
        this.duration = duration;
        this.local = local;
        this.description = description;
        setNextAvailableId(1);
    }

    protected Slot(Long idSlot, Temporal data, Duration duration, String local, String description) {
        this.idSlot = idSlot;
        this.data = data;
        this.duration = duration;
        this.local = local;
        this.description = description;
    }

    protected Slot(Slot s) {
        this.idSlot= s.getIdSlot();
        this.data = s.getData();
        this.duration = s.getDuration();
        this.local = s.getLocal();
        this.description = s.getDescription();
    }

    public static long getNextAvailableId() {
        return nextAvailableId;
    }

    public static void setNextAvailableId(long nextAvailableId) {
        System.out.println("Estou a alterar o next id do slot!");
        Slot.nextAvailableId += nextAvailableId;
    }

    public long getIdSlot() {
        return idSlot;
    }

    public void setIdSlot(long idSlot) {
        this.idSlot = idSlot;
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
        Slot res = of(this);
        return res;
    }
}
