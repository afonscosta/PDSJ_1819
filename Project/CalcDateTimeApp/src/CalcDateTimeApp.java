import Controller.Class.CalcDateTimeController;
import Controller.Class.CalcDateTimeLocalController;
import Controller.Class.CalcDateTimeScheduleController;
import Controller.Class.CalcDateTimeZoneController;
import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Class.CalcDateTimeLocalModel;
import Model.Class.CalcDateTimeModel;
import Model.Class.CalcDateTimeScheduleModel;
import Model.Class.CalcDateTimeZoneModel;
import Model.Interface.InterfCalcDateTimeLocalModel;
import Model.Interface.InterfCalcDateTimeModel;
import Model.Interface.InterfCalcDateTimeScheduleModel;
import Model.Interface.InterfCalcDateTimeZoneModel;
import View.Class.CalcDateTimeLocalView;
import View.Class.CalcDateTimeScheduleView;
import View.Class.CalcDateTimeView;
import View.Class.CalcDateTimeZoneView;
import View.Interface.InterfCalcDateTimeLocalView;
import View.Interface.InterfCalcDateTimeScheduleView;
import View.Interface.InterfCalcDateTimeView;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class CalcDateTimeApp {

    /*
    TODO Global:

    ClearConsole cross-platform

    TODO Zone:
    "Esta é a data, quer guardar no buffer?"
     */

    public static void main(String[] args) {

        DateTimeFormatter dateTimeFormatterLocal = DateTimeFormatter.ofPattern("yyyy/MM/dd k:m:s:n");
        DateTimeFormatter dateTimeFormatterZoned = DateTimeFormatter.ofPattern("yyyy/MM/dd k:m:s:n - VV");
        ZoneId localZone = ZoneId.systemDefault();

        //--------------------------------
        InterfCalcDateTimeLocalModel modelLocal = CalcDateTimeLocalModel.of();
        InterfCalcDateTimeZoneModel modelZone = CalcDateTimeZoneModel.of();
        InterfCalcDateTimeScheduleModel modelSchedule = InterfCalcDateTimeScheduleModel.pushState("AgendaReunioes");
        InterfCalcDateTimeModel model = new CalcDateTimeModel(modelLocal, modelZone, modelSchedule);

        //--------------------------------
        InterfCalcDateTimeLocalView viewLocal = new CalcDateTimeLocalView();
        InterfCalcDateTimeZoneView viewZone = new CalcDateTimeZoneView();
        InterfCalcDateTimeScheduleView viewSchedule = new CalcDateTimeScheduleView();
        InterfCalcDateTimeView view = new CalcDateTimeView();

        //--------------------------------
        InterfCalcDateTimeLocalController controlLocal = CalcDateTimeLocalController.of(dateTimeFormatterLocal, localZone);
        controlLocal.setModel(model);
        controlLocal.setView(viewLocal);

        InterfCalcDateTimeZoneController controlZone = CalcDateTimeZoneController.of(dateTimeFormatterZoned);
        controlZone.setModel(model);
        controlZone.setView(viewZone);

        InterfCalcDateTimeScheduleController controlSchedule = CalcDateTimeScheduleController.of(dateTimeFormatterLocal, dateTimeFormatterZoned, localZone);
        controlSchedule.setModel(model);
        controlSchedule.setView(viewSchedule);

        InterfCalcDateTimeController control = CalcDateTimeController.of(controlLocal, controlZone, controlSchedule);
        // Aqui não fiz 'control.setModel(model)' porque o Controller principal não acede ao model.
        control.setView(view);
        control.startFlow();

        //--------------------------------
        //System.out.println("Fim da Aplicação >> "
        //    + java.time.LocalDateTime.now());

        System.exit(0);
    }
}
