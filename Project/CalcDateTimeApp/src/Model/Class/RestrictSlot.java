package Model.Class;

import java.time.Duration;
import java.time.temporal.Temporal;

public class RestrictSlot extends Slot {

    private static long nextAvailableId = 0;
    private String period; // "pontual", "semanal" , "diaria"

    public static RestrictSlot of (Temporal data, Duration duration, String local, String description,String period) {
        return new RestrictSlot(data,duration,local,description,period);
    }

    public static RestrictSlot of (RestrictSlot s) {
        return new RestrictSlot(s);
    }


    private RestrictSlot(Temporal data, Duration duration, String local, String description,String period) {
        super(nextAvailableId,data,duration,local,description);
        RestrictSlot.setNextAvailableId(1);
        this.period = period;
    }

    private RestrictSlot(RestrictSlot s) {
        super(s);
        this.period = s.getPeriod();
    }

    public static long getNextAvailableId() {
        return nextAvailableId;
    }

    public static void setNextAvailableId(long nextAvailableId) {
        RestrictSlot.nextAvailableId += nextAvailableId;
    }

    public static long getAndSetNextAvailableId(long nextAvailableId){
        long res = getNextAvailableId();
        RestrictSlot.nextAvailableId += nextAvailableId;
        return res;
    }

    public static void abortLastUpdateToNextId(){
        if(nextAvailableId>0)
            nextAvailableId --;
    }

    public static void loadAvailableId(long initValue){
        nextAvailableId = initValue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public RestrictSlot clone(){
        RestrictSlot res = of(this);
        return res;
    }
}
