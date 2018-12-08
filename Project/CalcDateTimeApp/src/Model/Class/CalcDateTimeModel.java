package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.Configs;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;

public class CalcDateTimeModel implements InterfCalcDateTimeModel {

    private InterfCalcDateTimeLocalModel modelLocal;
    private InterfCalcDateTimeZoneModel modelZone;
    private InterfCalcDateTimeScheduleModel modelSchedule;
    private Configs configs;

    public static CalcDateTimeModel of() {
        return new CalcDateTimeModel();
    }

    private CalcDateTimeModel() {
        this.modelLocal = CalcDateTimeLocalModel.of();
        this.modelZone = CalcDateTimeZoneModel.of();
        this.modelSchedule = CalcDateTimeScheduleModel.of();
        this.configs = Configs.of("./Configs");

        this.modelLocal.loadConfigs(configs);
        this.modelZone.loadConfigs(configs);
        this.modelSchedule.loadConfigs(configs);
    }

    @Override
    public void setLocalDateTimeFormat(String localDateTimeFormat) {
        configs.setLocalDateTimeFormat(localDateTimeFormat);
    }

    @Override
    public String getLocalDateTimeFormat() {
        return configs.getLocalDateTimeFormat();
    }

    @Override
    public void setZoneDateTimeFormat(String zoneDateTimeFormat) {
        configs.setZoneDateTimeFormat(zoneDateTimeFormat);
    }

    @Override
    public String getZoneDateTimeFormat() {
        return configs.getZoneDateTimeFormat();
    }

    @Override
    public void saveConfigs() {
        configs.setSchedule(modelSchedule.getSchedule());
        configs.setScheduleRestrictions(modelSchedule.getScheduleRestrictions());
        configs.saveConfigs("./Configs");
    }

    @Override
    public long getIdSlot(){
        return configs.getIdSlot();
    }

    @Override
    public void incNIdSlot(int n){
        configs.incNIdSlot(n);
    }

    @Override
    public long getIdRestrictSlot(){
        return configs.getIdRestrictSlot();
    }

    @Override
    public void incNIdRestrictSlot(int n){
        configs.incNIdRestrictSlot(n);
    }

    //------------------------
    // Métodos Model Local
    //------------------------
    @Override
    public Temporal getDateTimeLocal() {
        return modelLocal.getDateTime();
    }

    @Override
    public ZoneId getLocalZone() {
        return configs.getZoneId();
    }

    @Override
    public void fromDateTimeLocal(ZonedDateTime ldt) {
        modelLocal.fromDateTime(ldt);
    }

    @Override
    public void withZoneLocal(String zoneId) {
        configs.setZoneId(ZoneId.of(zoneId));
        modelLocal.withZone(zoneId);
    }

    //------------------------
    // Métodos Model Zone
    //------------------------
    @Override
    public String getZoneZone() {
        return modelZone.getZone().toString();
    }

    @Override
    public Temporal getDateTimeZone() {
        return modelZone.getDateTime();
    }

    @Override
    public void fromDateTimeZone(ZonedDateTime zdt) {
        modelZone.fromDateTime(zdt);
    }

    @Override
    public void withZoneZone(String zoneId) {
        modelZone.withZone(zoneId);
    }

    //------------------------
    // Métodos Model Schedule
    //------------------------
    @Override
    public boolean addSlot(Slot newSlot){
        return modelSchedule.addSlot(newSlot,configs.getZoneId() );
    }

    @Override
    public int addRestrictSlot(RestrictSlot newSlot){
        return modelSchedule.addRestrictSlot(newSlot,configs.getZoneId());
    }

    @Override
    public List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        return modelSchedule.getMainInfoSlots(referenceZoneId,dtfLocal,dtfZone);
    }

    @Override
    public List<String> getModeSlots(String modeNormalized, int want, ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        return modelSchedule.getModeSlots(modeNormalized,want,referenceZone,dtfLocal,dtfZone);
    }

    @Override
    public List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        return modelSchedule.getRestrictSlots(referenceZone,dtfLocal,dtfZone);
    }

    @Override
    public Slot getSlot(Long infoSlot){ return modelSchedule.getSlot(infoSlot);}

    @Override
    public RestrictSlot getRestrictSlot(Long infoSlot){ return modelSchedule.getRestrictSlot(infoSlot);}

    @Override
    public boolean removeSlot(Long s){
        return modelSchedule.removeSlot(s);
    }

    @Override
    public boolean removeRestrictSlot(Long s){
        return modelSchedule.removeRestrictSlot(s);
    }

    @Override
    public void editLocalSlot(Long idSlot, String edit) {
        modelSchedule.editLocalSlot(idSlot, edit);
    }

    @Override
    public void editDescSlot(Long idSlot, String edit) {
        modelSchedule.editDescSlot(idSlot, edit);
    }

    @Override
    public boolean editDurationSlot(Long idSlot, Duration d) { return modelSchedule.editDurationSlot(idSlot,d,configs.getZoneId());
    }

    @Override
    public boolean editDateSLot(Long idSlot, Temporal data) {
        return modelSchedule.editDateSlot(idSlot,data,configs.getZoneId());
    }

    @Override
    public boolean existSlot(Long idSlot){return modelSchedule.existSlot(idSlot);}

    @Override
    public boolean existRestrictSlot(Long idSlot){return modelSchedule.existRestrictSlot(idSlot);}
}
