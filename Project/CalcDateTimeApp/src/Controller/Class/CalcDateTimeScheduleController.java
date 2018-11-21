package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.*;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utilities.BusinessUtils.*;
import static Utilities.EnumEditSlotInfo.DESC;
import static Utilities.EnumEditSlotInfo.DURACAO;
import static Utilities.EnumEditSlotInfo.LOCAL;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.time.temporal.ChronoUnit.YEARS;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewScheduleTxt;

    public CalcDateTimeScheduleController() {

    }
    @Override
    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    
    //------------------------
    // Fluxo inicial do menu agenda
    //------------------------
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

    //------------------------
    // Fluxo de adicionar uma nova reunião
    // Pode-se optar por usar a data da calculora local, de zona ou inserir uma data manual.
    //------------------------
    private void flowAddSlot(){
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

    //------------------------
    // Adicionar uma nova reunião
    //------------------------
    private void addSlot(Temporal date){
        System.out.println(date);
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

        out.print("Introduza uma descricao da nova reuniao: ");
        String description= Input.lerString();
        Slot newSlot = new Slot(date,duration,local,description);
        boolean res =model.addSlot(newSlot);
        //vai transitar para a descrição do menu
        if(res == true){
            System.out.println("Reuniao adicionada com sucesso!");
        }
        else
            System.out.println("Já existe uma reunião agendada ou a decorrer nesse data");
    }

    //------------------------
    // Fluxo de visualizar reuniões agendadas
    //------------------------
private void flowGetBusySlots() {
        Boolean flowDone = false;
        LocalDate currentDateMode = LocalDate.now();
        Menu menu = viewScheduleTxt.getMenu(1);
        List<String> description;
        String opcao;
        String modeNormalized="";//identifca o modo geral
        int pageIndex = 0;
        List<List<String>> slotsOfMode = partitionIntoPages(model.getMainInfoSlots(),25);
        int totalPages = slotsOfMode.size();
        do {
            switch (modeNormalized){
                case "":
                    slotsOfMode = partitionIntoPages(model.getMainInfoSlots(),25);
                    totalPages =slotsOfMode.size();
                    break;
                case "diario":
                    slotsOfMode = partitionIntoPages(model.getRestrictSlots(modeNormalized,currentDateMode.getDayOfMonth()),25);
                    totalPages = slotsOfMode.size();
                    break;
                case"semanal":
                    TemporalField woy = WeekFields.ISO.weekOfMonth();
                    int weekNumber = currentDateMode.get(woy);
                    System.out.println(weekNumber);
                    slotsOfMode = partitionIntoPages(model.getRestrictSlots(modeNormalized,weekNumber),25);
                    totalPages = slotsOfMode.size();
                    break;
                case"mensal":
                    slotsOfMode = partitionIntoPages(model.getRestrictSlots(modeNormalized,currentDateMode.getMonthValue()),25);
                    totalPages = slotsOfMode.size();
                    break;

            }
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
                case ">": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "<": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case ">>": currentDateMode = changeDataMode(currentDateMode,modeNormalized,1); break;
                case "<<": currentDateMode = changeDataMode(currentDateMode,modeNormalized,-1); break;
                case "S": flowDone = true; break;
                default:
                    if (opcao.matches("\\/.*")) {
                        List<String> matches = new ArrayList<>();
                        pageIndex = 0;
                        modeNormalized = opcao.substring(1).toLowerCase(); // Remover o "?" e lowercase
                        currentDateMode = LocalDate.now(); //ao atualizar o modo, volta ao now
                    }
                    else if (opcao.matches("=.*")) {
                        opcao = opcao.substring(1); // Remover o "="
                        for (String infoSlot : model.getMainInfoSlots()) {
                            String idSlot = getIdSlot(infoSlot);
                            System.out.println(idSlot);
                            System.out.println(opcao);
                            if(idSlot!=null & idSlot.equals(opcao)){
                                Slot s= model.getSlot(idSlot);
                                if(s!=null) {
                                    flowSelectBusySlot(s);
                                    break;
                                }
                            }

                        }
                    }
                    break;
            }
        } while(!flowDone);
    }

    //------------------------
    // Dado modo, a data atual do modo e a operação desejada pelo utilizador, atualiza a data do modo.
    //------------------------
    public LocalDate changeDataMode(LocalDate currentDateMode, String mode, int n){
            switch (mode){
                case "":
                    return currentDateMode;
                case "diario":
                    return (LocalDate)BusinessUtils.shiftDateTime(currentDateMode,n,DAYS);
                case"semanal":
                    return (LocalDate)BusinessUtils.shiftDateTime(currentDateMode,n,WEEKS);
                case"mensal":
                    return (LocalDate)BusinessUtils.shiftDateTime(currentDateMode,n,MONTHS);

            }
            return currentDateMode;
    }
    //------------------------
    // Dado a caracterização da reunião(id, data, local) devolve apenas o seu identificador gerado ao nivel da interface
    // null caso de erro
    //------------------------
    public String getIdSlot(String infoSlot){
        Pattern p = Pattern.compile("^[0-9]+");
        Matcher m = p.matcher(infoSlot);
        if(m.find()){
            return m.group(0);
        }
        else return null;
    }

    //------------------------
    // Fluxo ao selecionar uma reunião
    // Pode-se optar por alterar, remover, ver detalhes dessa reunião
    //------------------------
    public void flowSelectBusySlot(Slot s) {
        Menu menu = viewScheduleTxt.getMenu(2);
        String opcao;
            do {
                menu.addDescToTitle(Arrays.asList("Data: " + s.getData()));
                menu.show();
                opcao = Input.lerString();
                switch (opcao) {
                    case "A":
                        s= flowEditSlot(s);
                        break;
                    case "R":
                        removeSlot(s);
                        break;
                    case "D":
                        slotDetails(s);
                        break;
                    case "S":
                        break;
                }
            }
            while (!(opcao.equals("S") | opcao.equals("R")));
        }

    //------------------------
    // Remover uma reunião da agenda
    //------------------------
    private void removeSlot(Slot s){
                boolean res =model.removeSlot(s);
                //para adicioanar ao cabeçalho
                if(res== true){
                    out.println("Removido com sucesso!");
                }
                else
                    out.println("Ups,tente outra vez!");
    }

    //------------------------
    // Fluxo de editar uma reunião
    // Pode-se optar por alterar a data, duracao, local e a descrição
    // Devolve o slot alterado ou o slot original
    //------------------------
    private Slot flowEditSlot(Slot s){
            Menu menu = viewScheduleTxt.getMenu(4);
            String opcao="S";
            do {
                menu.addDescToTitle(Arrays.asList(localDateTimeToString(s.getData()),
                        s.getLocal(), s.getDuration().toString(),
                        s.getDescription()));
                menu.show();
                opcao = Input.lerString();
                switch (opcao) {
                    case "Data":
                        s= flowEditDataSlot(s);
                        break;
                    case "D":
                        s= editSlot(s,DURACAO);
                        break;
                    case "L":
                        s= editSlot(s,LOCAL);
                        break;
                    case "Desc":
                        s= editSlot(s,DESC);
                        break;
                    case "S":
                        break;
                }

            }
            while (!opcao.equals("S"));
            return s;
    }

    //------------------------
    // Editar duracao, descrição ou local de uma reunião
    // Devolve o slot com a informação alterada caso seja possivel alterar
    //------------------------
    private Slot editSlot(Slot s, EnumEditSlotInfo e){
            switch (e){
                case DURACAO:
                    out.print("Horas da nova duracao: ");
                    int horas = Input.lerInt();
                    out.print("Minutos da nova duracao: ");
                    int min = Input.lerInt();
                    Duration newDuration = Duration.of(horas, ChronoUnit.HOURS);
                    newDuration= newDuration.plus(min,ChronoUnit.MINUTES);
                    s =model.editDurationSlot(s,newDuration);
                    break;
                case DESC:
                    out.print("Nova descricao: ");
                    String desc = Input.lerString();
                    model.editSlot(s,e,desc);
                    break;
                case LOCAL:
                    out.print("Novo local: ");
                    String local = Input.lerString();
                    model.editSlot(s,e,local);
                    break;
            }
            return s;
        }

    //------------------------
    // Fluxo para editar data de uma reunião
    // Avançar ou recuar dias, semanas, meses ou anos
    // Devolve o slot com a informação alterada caso seja possivel alterar
    //------------------------
     private Slot flowEditDataSlot(Slot s) {
            Menu menu = viewScheduleTxt.getMenu(5);
            String opcao;
            do {
                Temporal data = s.getData();
                out.println(data);
                menu.addDescToTitle(Arrays.asList(localDateTimeToString(data)));
                menu.show();
                opcao = Input.lerString();
                switch (opcao) {
                    case "D":
                        data= shiftDays(data);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "Sem":
                        data =shiftWeeks(data);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "M":
                        data = shiftMonths(data);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "A":
                        data = shiftYears(data);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                    case "T":
                        data = shitTime(data);
                        out.println(data);
                        s = model.editDateSLot(s,data);
                    case "S":
                        break;
                }
            }
            while (!opcao.equals("S")) ;
            return s;
    }
    //------------------------
    // Adicionar ou recuar dias
    //------------------------
    private Temporal shiftDays(Temporal data) {
        out.print("(+|-) número de dias: ");
        int n = Input.lerInt();
        return BusinessUtils.shiftDateTime(data,n, DAYS);
    }

    //------------------------
    // Adicionar ou recuar semanas
    //------------------------
    private Temporal shiftWeeks(Temporal data) {
        out.print("(+|-) número de semanas: ");
        int n = Input.lerInt();
        return BusinessUtils.shiftDateTime(data,n, WEEKS);

    }

    //------------------------
    // Adicionar ou recuar meses
    //------------------------
    private Temporal shiftMonths(Temporal data) {
        out.print("(+|-) número de meses: ");
        int n = Input.lerInt();
        return BusinessUtils.shiftDateTime(data,n, MONTHS);
    }

    //------------------------
    // Adicionar ou recuar anos
    //------------------------
    private Temporal shiftYears(Temporal data) {
        out.print("(+|-) número de anos: ");
        int n = Input.lerInt();
        return BusinessUtils.shiftDateTime(data,n, YEARS);
    }
    //------------------------
    // Alterar Time do Temporal da reunião
    //------------------------
    private Temporal shitTime(Temporal data) {
        LocalDateTime dataConvert = LocalDateTime.from(data);
        String str;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        while (hour == null) {
            out.print("Hora (inicial: " + dataConvert.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, dataConvert.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");

        }

        while (minute == null) {
            out.print("Minutos (inicial: " + dataConvert.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, dataConvert.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (inicial: " + dataConvert.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, dataConvert.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (inicial: " + dataConvert.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, dataConvert.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");

        }
        LocalDateTime newLDT = LocalDateTime.of(dataConvert.getYear(), dataConvert.getMonth(), dataConvert.getDayOfMonth(), hour, minute, second, nano);
        return newLDT;
    }



    //------------------------
    // Detalhes do slot selecionado
    //------------------------
    private void slotDetails(Slot s){
            String opcao;
            Menu menu = viewScheduleTxt.getMenu(6);
            do{
                menu.addDescToTitle(Arrays.asList(zoneDateTimeToString((ZonedDateTime)s.getData()),
                                                    s.getDuration().toString(),
                                                    s.getDescription(),
                                                    s.getLocal()));
                menu.show();
                opcao = Input.lerString();
            }
            while(!opcao.equals("S"));
    }

    //------------------------
    // Pede ao utilizador uma data.
    // Se não disser nada fica a data do momento.
    // Devolve null caso dê uma exceção.
    //------------------------
    private LocalDateTime getLocalDateTimeFromInput() {
        LocalDateTime ldt = LocalDateTime.now();
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        String str;

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

        return LocalDateTime.of(year, month, day, hour, minute, second, nano);
    }

    //------------------------
    // Persistência ao nivel do model das reuniões
    //------------------------
    public void saveState(){
        try{
            model.saveState("AgendaReunioes");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problemas a guardar o estado");
        }
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
