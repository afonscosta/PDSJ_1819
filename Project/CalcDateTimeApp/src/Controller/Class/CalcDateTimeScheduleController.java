package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utilities.BusinessUtils.*;
import static Utilities.BusinessUtils.validateMinSec;
import static Utilities.BusinessUtils.validatePosNumber;
import static java.lang.System.out;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewScheduleTxt;

    @Override
    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    public CalcDateTimeScheduleController() {

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
                case "V" : flowGetBusySlots(); break;
                case "S": break;
                default: out.println("Opcao Invalida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    //Escolha da data a inserir
    private void flowAddSlot(){
        //isto vai transitar para o menu depois
        Menu menu = viewScheduleTxt.getMenu(3);
        String opcao;
        do {
            Temporal tempLocal = model.getDateTimeLocal();
            Temporal tempZone = model.getDateTimeZone();
            String ldt = localDateTimeToString(tempLocal);
            String zdt = zoneDateTimeToString((ZonedDateTime)tempZone);
            menu.addDescToTitle(Arrays.asList("Data calc. local: " + ldt,
                                              "Data calc. zona: " + zdt));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : addSlot(tempLocal); break;
                case "Z" : addSlot(tempZone); break;
                case "M" : addSlot(null); break;
                case "S": break;
                default: System.out.println("Opcao Invalida !"); break;
            }
        }
        //Come back to init menu of Agenda
        while(!(opcao.equals("S")| opcao.equals("L") | opcao.equals("Z") | opcao.equals("M")));
    }

    private void addSlot(Temporal date){
        if(date==null){
            out.println("Introduza a data da nova reuniao:");
            date = getLocalDateTimeFromInput();
        }
        out.println("Duracao da nova reuniao:");
        out.print("Horas ira demorar: ");
        int horas = Input.lerInt();
        out.print("Minutos ira demorar: ");
        int minutos = Input.lerInt();
        Duration duration = Duration.of(horas, ChronoUnit.HOURS);
        duration = duration.plus(minutos,ChronoUnit.MINUTES);

        out.print("Introduza o local da nova reuniao: ");
        String local = Input.lerString();

        out.print("Introduza uma descricao da nova reuniao:");
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
        LocalDateTime ldt = (LocalDateTime) model.getDateTimeLocal();
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
            out.print("Mes (default: " + ldt.getMonthValue() + "): ");
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

    private String flowGetBusySlots() {
        Boolean flowDone = false;
        List<List<String>> slotsOfMode = partitionIntoPages(model.getMainInfoSlots(),25); // If someone looks for "europe", place matches it here

        int pageIndex = 0;
        int totalPages = slotsOfMode.size();
        Menu menu = viewScheduleTxt.getMenu(1);
        List<String> description;
        String opcao;
        do {

            // Mais complexo do que necessário para o caso em que a lista de procuras está vazia,
            // e assim não acontece indexOuto
            try {
                description = new ArrayList(slotsOfMode.get(pageIndex));
            } catch (IndexOutOfBoundsException e) {
                description = new ArrayList<>();
            }

            description.add(""); // Linha branca na descrição
            description.add(String.format("Pagina (%s/%s)", pageIndex+1, totalPages));

            menu.addDescToTitle(description);

            menu.show();
            opcao = Input.lerString();
            switch (opcao) {
                case "+": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "-": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case "s": flowDone = true; break;
                default:
                    if (opcao.matches("\\/.*")) {
                        List<String> matches = new ArrayList<>();
                        pageIndex = 0;
                        String searchedWordNormalized = opcao.substring(1).toLowerCase(); // Remover o "?" e lowercase



                        slotsOfMode = partitionIntoPages(matches,25);
                        totalPages = slotsOfMode.size();
                    } else if (opcao.matches("=.*")) {
                        opcao = opcao.substring(1); // Remover o "="
                        for (String infoSlot : model.getMainInfoSlots()) {
                            String idSlot = getIdSlot(infoSlot);
                            System.out.println(idSlot);
                            System.out.println(opcao);
                            if(idSlot!=null & idSlot.equals(opcao)){
                                flowSelectBusySlot(idSlot);
                                break;
                            }

                        }
                        flowDone = true;
                    }
                    break;
            }
        } while(!flowDone);

        return opcao;
    }

    public String getIdSlot(String infoSlot){
        Pattern p = Pattern.compile("^[0-9]+");
        Matcher m = p.matcher(infoSlot);
        if(m.find()){
            return m.group(0);
        }
        else return null;
    }

    public void flowSelectBusySlot(String idSlot) {
        Menu menu = viewScheduleTxt.getMenu(2);
        String opcao;
            do {
                menu.addDescToTitle(Arrays.asList("ID escolhido: ",idSlot));
                menu.show();
                opcao = Input.lerString();
                switch (opcao) {
                    case "A":
                        editSlot(idSlot);
                        break;
                    case "R":
                        removeSlot(idSlot);
                        break;
                    case "D":
                        slotDetails(idSlot);
                        break;
                    case "S":
                }
            }
            while (!opcao.equals("S"));
        }



        // Remover um slot de trabalho
        private void removeSlot(String idSlot){
                boolean res =model.removeSlot(idSlot);
                //para adicioanar ao cabeçalho
                if(res== true){
                    out.println("Removido com sucesso!");
                }
                else
                    out.println("Ups,tente outra vez!");
        }

        // Editar um slot de trabalho
        private void editSlot(String infoSlot){
            System.out.println("Edit slot");
        }
        private void slotDetails(String infoSlot){

        }


    /*
    // Mostrar todos os slots do proximo/dia/semana/mes, dependendo da escolha que vai ser efetuada
    // dia -> resto do dia de hoje + opcoes de next, previous e select
    // semana -> resto da semana atual + opcoes de next, previous e select
    // mês -> resto de mes igual + opcoes de next, previous e select
    // reminder : imprimir identificador antes de cada slot, para ser usado no select
    //private void flowGetBusySlotsFor(){
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
    */

    public void saveState(){
        try{
            model.saveState("AgendaReunioes");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problemas a guardar o estado");
        }

    }

}
