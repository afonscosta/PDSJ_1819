package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.*;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utilities.BusinessUtils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.flowHelp;
import static Utilities.EnumEditSlotInfo.DESC;
import static Utilities.EnumEditSlotInfo.DURACAO;
import static Utilities.EnumEditSlotInfo.LOCAL;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.util.Arrays.asList;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewScheduleTxt;
    //do ficheiro de configuração
    private DateTimeFormatter dateTimeFormatterLocal;
    private DateTimeFormatter dateTimeFormatterZoned;
    private ZoneId referenceZone;

    public static CalcDateTimeScheduleController of() {
        return new CalcDateTimeScheduleController();
    }

    public static CalcDateTimeScheduleController of(DateTimeFormatter dtfl, DateTimeFormatter dtfz,ZoneId referenceZone) {
        if(referenceZone != null){
            if (dtfl != null && dtfz != null)
                return new CalcDateTimeScheduleController(dtfl, dtfz, referenceZone);
            else if (dtfl != null)
                return new CalcDateTimeScheduleController(dtfl, "local", referenceZone);
            else if (dtfz != null)
                return new CalcDateTimeScheduleController(dtfz, "zoned",referenceZone);
            else
                return new CalcDateTimeScheduleController(referenceZone);
        }
        else {
            if (dtfl != null && dtfz != null)
                return new CalcDateTimeScheduleController(dtfl, dtfz);
            else if (dtfl != null)
                return new CalcDateTimeScheduleController(dtfl, "local");
            else if (dtfz != null)
                return new CalcDateTimeScheduleController(dtfz, "zoned");
            else
                return new CalcDateTimeScheduleController();
        }
    }

    private CalcDateTimeScheduleController(DateTimeFormatter dtf, String mode) {
        if (mode.equals("local")) {
            this.dateTimeFormatterLocal = dtf;
            this.dateTimeFormatterZoned = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n [VV]");
        }
        else if (mode.equals("zoned")) {
            this.dateTimeFormatterLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n");
            this.dateTimeFormatterZoned = dtf;
        }
        this.referenceZone = ZoneId.systemDefault();
    }

    private CalcDateTimeScheduleController(DateTimeFormatter dtf, String mode,ZoneId referenceZone) {
        if (mode.equals("local")) {
            this.dateTimeFormatterLocal = dtf;
            this.dateTimeFormatterZoned = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n [VV]");
        }
        else if (mode.equals("zoned")) {
            this.dateTimeFormatterLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n");
            this.dateTimeFormatterZoned = dtf;
        }
        this.referenceZone = referenceZone;
    }

    private CalcDateTimeScheduleController() {
        this.dateTimeFormatterLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n");
        this.dateTimeFormatterZoned = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n [VV]");
        this.referenceZone = ZoneId.systemDefault();
    }

    private CalcDateTimeScheduleController(ZoneId referenceZone) {
        this.dateTimeFormatterLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n");
        this.dateTimeFormatterZoned = DateTimeFormatter.ofPattern("dd/MM/yyyy k:m:s:n [VV]");
        this.referenceZone = referenceZone;
    }

    private CalcDateTimeScheduleController(DateTimeFormatter dtfl, DateTimeFormatter dtfz) {
        this.dateTimeFormatterLocal = dtfl;
        this.dateTimeFormatterZoned = dtfz;
        this.referenceZone= ZoneId.systemDefault();
    }

    private CalcDateTimeScheduleController(DateTimeFormatter dtfl, DateTimeFormatter dtfz, ZoneId referenceZone) {
        this.dateTimeFormatterLocal = dtfl;
        this.dateTimeFormatterZoned = dtfz;
        this.referenceZone= referenceZone;
    }

    @Override
    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    @Override
    public void setDateTimeFormatterLocal(DateTimeFormatter dtf) {
        this.dateTimeFormatterLocal = dtf;
    }

    @Override
    public void setDateTimeFormatterZoned(DateTimeFormatter dtf) {
        this.dateTimeFormatterZoned = dtf;
    }

    public void withZone(String zid) {
        model.withZoneLocal(zid);
    }

    //------------------------
    // Fluxo inicial do menu agenda
    //------------------------
    public void flowSchedule(){
        Menu menu = viewScheduleTxt.getMenu(0);
        out.println(dateTimeFormatterLocal.toString());
        out.println(dateTimeFormatterZoned.toString());
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
            String ldt = localDateTimeToString(tempLocal, dateTimeFormatterLocal);
            String zdt = zoneDateTimeToString((ZonedDateTime)tempZone, dateTimeFormatterZoned);
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
            date = ControllerUtils.getDateTimeFromInput((ZonedDateTime) model.getDateTimeLocal(), referenceZone);
        }
        out.println("Duracao da nova reuniao");
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
            System.out.println(GREEN_BOLD +"Reuniao adicionada com sucesso!" + RESET);
        }
        else
            System.out.println(RED_BOLD +"Ja existe uma reuniao agendada ou a decorrer nesse data..." + RESET);
        out.print("Prima Enter para continuar.");
        Input.lerString();
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
                case "diaria":
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
            int pageIndexToDisplay = (totalPages == 0) ? 0 : pageIndex + 1;
            description.add(String.format("Pagina (%s/%s)", pageIndexToDisplay, totalPages));

            menu.addDescToTitle(description);
            menu.show();
            opcao = Input.lerString();
            switch (opcao) {
                case ">": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "<": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case ">>": currentDateMode = changeDataMode(currentDateMode,modeNormalized,1); break;
                case "<<": currentDateMode = changeDataMode(currentDateMode,modeNormalized,-1); break;
                case "?" : help(); break;
                case "S": flowDone = true; break;
                case "s": flowDone = true; break;
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
                case "diaria":
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
                String dataToShow = BusinessUtils.DateSlotToString(s,referenceZone);
                menu.addDescToTitle(Arrays.asList("Data: " + dataToShow));
                menu.show();
                opcao = Input.lerString();
                opcao = opcao.toUpperCase();
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
                    out.println(GREEN_BOLD +"Removido com sucesso!" + RESET);
                }
                else
                    out.println(RED_BOLD + "De momento, não e possivel remover o slot!" + RESET);
                out.print("Prima Enter para continuar.");
                Input.lerString();
    }

    //------------------------
    // Fluxo de editar uma reunião
    // Pode-se optar por alterar a data, duracao, local e a descrição
    // Devolve o slot alterado ou o slot original
    //------------------------
    private Slot flowEditSlot(Slot s){
            Menu menu = viewScheduleTxt.getMenu(4);
            String opcao;
            do {
                String dataToShow = BusinessUtils.DateSlotToString(s,referenceZone);
                menu.addDescToTitle(Arrays.asList(dataToShow,
                        s.getLocal(), s.getDuration().toString(),
                        s.getDescription()));
                menu.show();
                opcao = Input.lerString();
                opcao = opcao.toUpperCase();
                switch (opcao) {
                    case "DATA":
                        s= flowEditDataSlot(s);
                        break;
                    case "D":
                        s= editSlot(s,DURACAO);
                        break;
                    case "L":
                        s= editSlot(s,LOCAL);
                        break;
                    case "DESC":
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
                    if(s.getDuration().equals(newDuration)){
                        out.println(GREEN_BOLD + "Duracao alterada com sucesso!"+ RESET);
                    }
                    else{
                        out.println(RED_BOLD + "Sobreposicao com outras reunioes ja agendadas!"+ RESET);

                    }
                    break;
                case DESC:
                    out.print("Nova descricao: ");
                    String desc = Input.lerString();
                    model.editSlot(s,e,desc);
                    out.println(GREEN_BOLD + "Descricao alterada com sucesso!"+ RESET);
                    break;
                case LOCAL:
                    out.print("Novo local: ");
                    String local = Input.lerString();
                    model.editSlot(s,e,local);
                    out.println(GREEN_BOLD +"Local alterado com sucesso!" + RESET);
                    break;
            }
            out.print("Prima Enter para continuar.");
            Input.lerString();
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
                String dataToShow = BusinessUtils.DateSlotToString(s,referenceZone);
                menu.addDescToTitle(Arrays.asList(dataToShow));
                menu.show();
                opcao = Input.lerString();
                opcao = opcao.toUpperCase();
                switch (opcao) {
                    case "D":
                        data= BusinessUtils.shiftDateTime(data,ControllerUtils.shift("dias"), DAYS);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "SEM":
                        data = BusinessUtils.shiftDateTime(data,ControllerUtils.shift("semanas"), WEEKS);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "M":
                        data = BusinessUtils.shiftDateTime(data,ControllerUtils.shift("meses"), MONTHS);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "A":
                        data = BusinessUtils.shiftDateTime(data,ControllerUtils.shift("anos"), YEARS);
                        out.println(data);
                        s = model.editDateSLot(s, data);
                        break;
                    case "T":
                        data = ControllerUtils.shitTime(ZonedDateTime.from(data));
                        out.println(data);
                        s = model.editDateSLot(s,data);
                        break;
                    case "S":
                        break;
                }
                if(opcao.equals("D") | opcao.equals("SEM") | opcao.equals("M") | opcao.equals("A") | opcao.equals("T")) {
                    if (s.getData().equals(data)) {
                        out.println(GREEN_BOLD + "Alteracao efetuada com sucesso!" + RESET);
                    } else {
                        out.println(RED_BOLD + "Sobreposicao de reunioes!" + RESET);
                    }
                    out.print("Prima Enter para continuar.");
                    Input.lerString();
                }
            }
            while (!opcao.equals("S")) ;
            return s;
    }

    //------------------------
    // Detalhes do slot selecionado
    //------------------------
    private void slotDetails(Slot s){
            String opcao;
            Menu menu = viewScheduleTxt.getMenu(6);
            do{
                String dataToShow = BusinessUtils.DateSlotToString(s,referenceZone);
                menu.addDescToTitle(Arrays.asList(dataToShow,
                                                s.getDuration().toString(),
                                                s.getDescription(),
                                                s.getLocal()));
                menu.show();
                opcao = Input.lerString();
            }
            while(!opcao.equals("S"));
    }

    private void help() {
        Menu menu = viewScheduleTxt.getMenu(7);
        List<String> l = asList(
                BLACK_BOLD + "Opcoes:" + RESET,
                BLACK_BOLD + "/<vista>" + RESET +
                        " Permite ter uma visao diaria, semanal ou mensal",
                "         das reunioes. Se pretender uma visao diaria devera ",
                "         introduzir" + RESET + BLACK_BOLD + " /diaria " + RESET + "onde lhe sera apresentado",
                "         inicialmente as reunioes do dia corrente.",
                "         Caso pretenda visualizar todas as reunioes",
                "         agendadas devera introduzir apenas" + RESET + BLACK_BOLD + " /" + RESET + ".",
                "         O principio e o mesmo para os restantes",
                "         modos de visualizacao."+ RESET,
                " ",
                BLACK_BOLD + "<" + RESET +
                        "        Recuar a pagina que esta ser apresentada.",
                BLACK_BOLD + ">" + RESET +
                        "        Avancar a pagina que esta ser apresentada.",
                " ",
                BLACK_BOLD + ">>" + RESET +
                        "       Caso prentenda avancar no dia/semana/mes referente",
                "         ao modo de visualizacao. Por exemplo, foi selecionada",
                "         a vista diaria em que inicialmente mostra as reunioes",
                "         do dia corrente. Caso pretenda ver do dia seguinte,",
                "         devera usar esta opcao, e assim sucessivamente.",
                "         Para a vista semanal, ira apresentar da proxima semana",
                "         e o mesmo principio para a vista mensal.",
                BLACK_BOLD + "<<" + RESET +
                        "       Da mesma forma que a anterior, esta opcao permite recuar um",
                "         dia, semana ou mes de acordo com o modo de visualizacao.",
                "         Estas duas ultimas opcoes permitem navegar sempre com a",
                "         apresentacao no mesmo intervalo.",
                " ",
                BLACK_BOLD + "=<id>" + RESET +
                        "   Cada reuniao contem antes da sua descricao",
                "         um identificador.",
                "         Pretendendo selecionar, por exemplo, a reuniao",
                "         com o identifcador 0, o utilizador devera introduzir",
                "         a opcao "+ RESET + BLACK_BOLD + "=0" + RESET +".",
                "         No novo menu apresentado podera alterar qualquer ",
                "         dado da reuniao, remover ou ver detalhes da mesma."
        );

        String opcao;
        do {
            menu.addDescToTitle(l);
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "S": break;
                default: out.println("Opcao Invalida!"); break;
            }
        }
        while(!opcao.equals("S"));
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
}
