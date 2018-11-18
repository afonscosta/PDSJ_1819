package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static Utilities.BusinessUtils.*;
import static java.lang.System.out;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewScheduleTxt;
    private Set<String> bufferCodes;

    public CalcDateTimeScheduleController() {
    }

    @Override
    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
        this.bufferCodes = model.getKeys();
    }

    public void flowSchedule(){
        Menu menu = viewScheduleTxt.getMenu(0);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : flowAddSlot(); break;
                case "R" : flowRemoveSlot(); break;
                case "A" : flowEditSlot(); break;
                case "V" : flowGetBusySlotsFor(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    //Escolha da data a inserir
    private void flowAddSlot(){
        //isto vai transitar para o menu depois
        Boolean flowDone = false;
        Menu menu = viewScheduleTxt.getMenu(3);
        String opcao;
        do {
            Temporal temp;
            List<String> dts = new ArrayList<>();
            for (String c : bufferCodes) {
                temp = model.getDateTime(c);
                dts.add("Data calc. " + c + ": " + zoneDateTimeToString((ZonedDateTime) temp));
            }
            menu.addDescToTitle(dts);
            menu.show();
            opcao = Input.lerString();
            if (opcao.toUpperCase().equals("M")) {
                addSlot(null); flowDone = true;
            }
            else if (opcao.toUpperCase().equals("S")) {
                flowDone = true;
            }
            else {
                if (opcao.matches("=.*")) {
                    opcao = opcao.substring(1); // Remover o "="
                    if (bufferCodes.contains(opcao)) {
                        System.out.println(opcao);
                        addSlot(model.getDateTime(opcao));
                        flowDone = true;
                    }
                }
            }
        }
        //Come back to init menu of Agenda
        while(!flowDone);
    }
    private void addSlot(Temporal date){
        System.out.println(date);
        if(date==null){
            System.out.println("->Introduza a data da nova reuniao:");
            date = getLocalDateTimeFromInput();
        }
        System.out.println("->Duracao da nova reuniao:");
        System.out.println("Horas irá demorar:");
        int horas = Input.lerInt();
        System.out.println("Minutos irá demorar:");
        int minutos = Input.lerInt();
        Duration duration = Duration.of(horas, ChronoUnit.HOURS);
        duration = duration.plus(minutos,ChronoUnit.MINUTES);

        System.out.println("->Introduza o local da nova reuniao:");
        String local = Input.lerString();

        System.out.println("->Introduza uma descrição da nova reuniao:");
        String description= Input.lerString();
        Slot newSlot = new Slot(date,duration,local,description);
        boolean res =model.addSlot(newSlot);
        if(res == true){
            System.out.println("Reuniao adicionada com sucesso!");
        }
        else
            System.out.println("Já existe uma reunião agendada ou a decorrer nesse data");
    }

    // Pede ao utilizador uma data.
    // Se não disser nada fica a que se encontra no modelLocal.
    // Devolve null caso dê uma exceção.
    private LocalDateTime getLocalDateTimeFromInput() {
        LocalDateTime ldt = (LocalDateTime) model.getDateTime((String) bufferCodes.toArray()[0]);
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        String str = null;

        while (year == null) {
            out.print("Ano (default: " + ldt.getYear() + "): ");
            str = Input.lerString();
            year = validatePosNumber(str, ldt.getYear());
            if (year == null)
                out.println("[!] Ano invalido.");
        }

        while (month == null) {
            out.print("Mês (default: " + ldt.getMonthValue() + "): ");
            str = Input.lerString();
            month = validateMonth(str, ldt.getMonthValue());
            if (month == null)
                out.println("[!] Mes invalido.");
        }

        while (day == null) {
            out.print("Dia (default: " + ldt.getDayOfMonth() + "): ");
            str = Input.lerString();
            day = validateDay(str, ldt.getDayOfMonth(), year, month);
            if (day == null)
                out.println("[!] Dia invalido.");
        }

        while (hour == null) {
            out.print("Hora (default: " + ldt.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, ldt.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");
        }

        while (minute == null) {
            out.print("Minutos (default: " + ldt.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, ldt.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (default: " + ldt.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, ldt.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (default: " + ldt.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, ldt.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");
        }

        LocalDateTime newLDT = LocalDateTime.of(year, month, day, hour, minute, second, nano);
        return newLDT;
    }

    // Remover um slot de trabalho
    private void flowRemoveSlot(){
        System.out.println("Remove slot");
    }

    // Editar um slot de trabalho
    private void flowEditSlot(){
        System.out.println("Edit slot");
    }

    // Mostrar todos os slots do proximo/dia/semana/mes, dependendo da escolha que vai ser efetuada
    // dia -> resto do dia de hoje + opcoes de next, previous e select
    // semana -> resto da semana atual + opcoes de next, previous e select
    // mês -> resto de mes igual + opcoes de next, previous e select
    // reminder : imprimir identificador antes de cada slot, para ser usado no select
    private void flowGetBusySlotsFor(){
        Menu menu = viewScheduleTxt.getMenu(1);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P" : flowResultsBusySlots("P"); break;
                case "D" : flowResultsBusySlots("D"); break;
                case "Sem" : flowResultsBusySlots("Sem"); break;
                case "M" : flowResultsBusySlots("M"); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void flowResultsBusySlots(String op){
        //chamada ao metodo que irá devolver as slots pedidas
        //apresentar as slots pedidas
        Menu menu = viewScheduleTxt.getMenu(2);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P" : flowResultsBusySlots("P"); break;
                case "A" : flowResultsBusySlots("D"); break;
                case "D" : slotDetails(); break;
                case "M" : flowResultsBusySlots("M"); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }
    private void slotDetails(){}

}
