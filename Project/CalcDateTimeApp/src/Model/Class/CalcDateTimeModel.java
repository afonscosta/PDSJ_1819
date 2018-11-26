package Model.Class;

import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import Utilities.Configs;
import Utilities.EnumEditSlotInfo;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class CalcDateTimeModel implements InterfCalcDateTimeModel {

    private InterfCalcDateTimeLocalModel modelLocal;
    private InterfCalcDateTimeZoneModel modelZone;
    private InterfCalcDateTimeScheduleModel modelSchedule;


    public CalcDateTimeModel(InterfCalcDateTimeLocalModel modelLocal,
                             InterfCalcDateTimeZoneModel modelZone,
                             InterfCalcDateTimeScheduleModel modelSchedule,
                             Configs configs) {
        this.modelLocal = modelLocal;
        this.modelZone = modelZone;
        this.modelSchedule = modelSchedule;

        this.modelLocal.loadConfigs(configs);
        this.modelZone.loadConfigs(configs);
        this.modelSchedule.loadConfigs(configs);
    }

    //------------------------
    // Métodos Model Local
    //------------------------
    @Override
    public Temporal getDateTimeLocal() {
        return modelLocal.getDateTime();
    }

    @Override
    public String getLocalDateTimeFormat() {
        return modelLocal.getLocalDateTimeFormat();
    }

    @Override
    public void setLocalDateTimeFormat(String localDateTimeFormat) {
        modelLocal.setLocalDateTimeFormat(localDateTimeFormat);
    }

    @Override
    public void saveConfigs() {
        Configs configs = new Configs();

        configs.setLocalDateTimeFormat(this.getLocalDateTimeFormat());
        configs.setZoneDateTimeFormat(this.getZoneDateTimeFormat());
        configs.setZoneId(this.getLocalZone().toString());
        configs.setAgenda(new TreeSet(this.getSchedule()));
        configs.setScheduleRestrictions(new ArrayList(this.getScheduleRestrictions()));

        // re-writes over old file
        configs.saveConfigs("./Configs");
    }

    @Override
    public ZoneId getLocalZone() {
        return modelLocal.getZone();
    }

    @Override
    public void setZoneId(ZoneId zoneId) {
        modelLocal.setZoneId(zoneId);
    }

    @Override
    public void shiftDateTimeLocal(int n, ChronoUnit cu) {
        modelLocal.shiftDateTime(n, cu);
    }

    @Override
    public void shiftWorkDaysDateTimeLocal(int n) {
        modelLocal.shiftWorkDaysDateTime(n);
    }

    @Override
    public String diffDateTimeLocal(ZonedDateTime toDateTime) {
        return modelLocal.diffDateTime(toDateTime);
    }

    @Override
    public String diffWorkDaysDateTimeLocal(ZonedDateTime toDateTime) {
        return modelLocal.diffWorkDaysDateTime(toDateTime);
    }

    @Override
    public void fromDateTimeLocal(ZonedDateTime ldt) {
        modelLocal.fromDateTime(ldt);
    }

    @Override
    public void withZoneLocal(String zoneId) {
        modelLocal.withZone(zoneId);
    }

    //------------------------
    // Métodos Model Zone
    //------------------------

    @Override
    public String getZoneDateTimeFormat() {
        return modelZone.getZoneDateTimeFormat();
    }

    @Override
    public String getZoneZone() {
        return modelZone.getZone().toString();
    }

    @Override
    public void setZoneDateTimeFormat(String zoneDateTimeFormat) {
        modelZone.setZoneDateTimeFormat(zoneDateTimeFormat);
    }

    @Override
    public Temporal getDateTimeZone() {
        return modelZone.getDateTime();
    }

    @Override
    public void shiftDateTimeZone(int n, ChronoUnit cu) {
        modelZone.shiftDateTime(n, cu);
    }

    @Override
    public void shiftWorkDaysDateTimeZone(int n) {
        modelZone.shiftWorkDaysDateTime(n);
    }

    @Override
    public String diffDateTimeZone(ZonedDateTime toDateTime) {
        return modelZone.diffDateTime(toDateTime);
    }

    @Override
    public String diffWorkDaysDateTimeZone(ZonedDateTime toDateTime) {
        return modelZone.diffWorkDaysDateTime(toDateTime);
    }

    @Override
    public void fromDateTimeZone(ZonedDateTime zdt) {
        modelZone.fromDateTime(zdt);
    }

    @Override
    public void withZoneZone(String zoneId) {
        modelZone.withZone(zoneId);
    }

    @Override
    public void changeToCurrentDateInZone(String zoneId) {
        modelZone.changeToCurrentDateInZone(zoneId);

    }

    //------------------------
    // Métodos Model Schedule
    //------------------------
    @Override
    public boolean addSlot(Slot newSlot, Collection c){
        return modelSchedule.addSlot(newSlot,c);
    }

    @Override
    public void saveState(String nomeFicheiro) throws IOException {
        modelSchedule.saveState(nomeFicheiro);
    }

    @Override
    public List<String> getMainInfoSlots(ZoneId referenceZoneId, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        return modelSchedule.getMainInfoSlots(referenceZoneId,dtfLocal,dtfZone);
    }

    @Override
    public List<String> getModeSlots(String modeNormalized, int want, ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        return modelSchedule.getModeSlots(modeNormalized,want,referenceZone,dtfLocal,dtfZone);
    }

    public List<String> getRestrictSlots(ZoneId referenceZone, DateTimeFormatter dtfLocal, DateTimeFormatter dtfZone){
        return modelSchedule.getRestrictSlots(referenceZone,dtfLocal,dtfZone);
    }

    @Override
    public Slot getSlot(String infoSlot,Collection c){ return modelSchedule.getSlot(infoSlot,c);}


    public Collection getSchedule(){
        return modelSchedule.getSchedule();
    }

    public Collection getScheduleRestrictions(){
        return modelSchedule.getScheduleRestrictions();
    }

    @Override
    public boolean removeSlot(Slot slot, Collection c){
        return modelSchedule.removeSlot(slot,c);
    }

    @Override
    public void editSlot(Slot s, EnumEditSlotInfo e, String edit) {
        modelSchedule.editSlot(s,e,edit);
    }

    @Override
    public Slot editDurationSlot(Slot s, Duration d) {
        return modelSchedule.editDurationSlot(s,d);
    }

    @Override
    public Slot editDateSLot(Slot s, Temporal data) {
        return modelSchedule.editDateSLot(s,data);
    }

}
