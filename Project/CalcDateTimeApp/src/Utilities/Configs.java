package Utilities;

import Model.Class.RestrictSlot;
import Model.Class.Slot;

import java.io.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Configs implements Serializable{

    private Set<Slot> agenda;
    private List<RestrictSlot> scheduleRestrictions;
    private String zoneDateTimeFormat;
    private String localDateTimeFormat;
    private String zoneId;

    public static Configs of (String pathToFile) {
        try {
            FileInputStream fis = new FileInputStream(pathToFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Configs configs = (Configs) ois.readObject();
            ois.close();
            fis.close();

            return configs;
        } catch (IOException | ClassNotFoundException e) {
            return of();
        }
    }

    public static Configs of () {
        return new Configs();
    }

    public Configs() {
        this.agenda = new TreeSet<>();
        this.scheduleRestrictions = new ArrayList<>();
        this.zoneDateTimeFormat = "yyyy/MM/dd H:m:s:n - VV";
        this.localDateTimeFormat = "yyyy/MM/dd H:m:s:n";
        this.zoneId = ZoneId.systemDefault().toString();
    }

    public Set<Slot> getSchedule() {
        return agenda;
    }

    public List<RestrictSlot> getScheduleRestrictions() {
        return scheduleRestrictions;
    }

    public String getZoneDateTimeFormat() {
        return zoneDateTimeFormat;
    }

    public String getLocalDateTimeFormat() {
        return localDateTimeFormat;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void saveConfigs(String pathToFile) {
        try {
            FileOutputStream fos = new FileOutputStream(pathToFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAgenda(Set<Slot> agenda) {
        this.agenda = agenda;
    }

    public void setScheduleRestrictions(List<RestrictSlot> list) {
        this.scheduleRestrictions = list;
    }

    public void setZoneDateTimeFormat(String zoneDateTimeFormat) {
        this.zoneDateTimeFormat = zoneDateTimeFormat;
    }

    public void setLocalDateTimeFormat(String localDateTimeFormat) {
        this.localDateTimeFormat = localDateTimeFormat;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

}
