package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.BusinessUtils;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Utilities.BusinessUtils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.asList;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewScheduleTxt;

    public static CalcDateTimeScheduleController of() {
        return new CalcDateTimeScheduleController();
    }

    private CalcDateTimeScheduleController() { }

    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    //------------------------
    // Retorna o formato para LocalDateTime definido no ficheiro de configurações
    //------------------------
    public DateTimeFormatter getDateTimeFormatterLocal(){
        return DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat());
    }

    //------------------------
    // Retorna o formato para ZonedDateTime definido no ficheiro de configurações
    //------------------------
    public DateTimeFormatter getDateTimeFormatterZoned(){
        return DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat());
    }

    //------------------------
    // Retorna o zonedId definido no ficheiro de configurações
    //------------------------
    public ZoneId getRefereceZoneId(){
        ZonedDateTime zdt = ZonedDateTime.from(model.getDateTimeLocal());
        return zdt.getZone();
    }

    //------------------------
    // Fluxo inicial do menu agenda
    //------------------------
    public void flowSchedule(){
        Menu menu = viewScheduleTxt.getMenu(0);
        String opcao;
        String errorMessage = "n/a";
        do {
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : flowAddSlot(); break;
                case "V" : flowGetBusySlots(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida !"; break;
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
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        boolean res=false;
        do {
            Temporal tempLocal = model.getDateTimeLocal();
            Temporal tempZone = model.getDateTimeZone();
            String ldt = localDateTimeToString(tempLocal, getDateTimeFormatterLocal());
            String zdt = zoneDateTimeToString((ZonedDateTime)tempZone, getDateTimeFormatterZoned());
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.addDescToTitle(Arrays.asList("Data calc. local: " + ldt,
                                              "Data calc. com fusos: " + zdt));
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : res = addSlot(tempLocal);break;
                case "Z" : res = addSlot(tempZone); break;
                case "ML": Temporal date = getDateFromInput("ML");res = addSlot(date); break;
                case "MF":  date = getDateFromInput("MF"); res = addSlot(date); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida !"; break;
            }
            if(opcao.equals("L")| opcao.equals("Z") | opcao.equals("ML") | opcao.equals("MF")){
                if(res){
                    statusMessage="Reuniao adicionada com sucesso!";
                }
                else{
                    errorMessage="Sobreposicao com reunioes ou restricoes...";
                }
            }
        }
        while(!(opcao.equals("S")));
    }

    public Temporal getDateFromInput(String typeOfDate){
        ZoneId zonedId = getRefereceZoneId();
        if(typeOfDate.equals("MF")){
            String zoneIdString = flowGetNZoneIds(1, viewScheduleTxt.getMenu(8), model.getZoneZone()).get(0);
            zonedId = ZoneId.of(zoneIdString);
        }

        return  getDateTimeFromInput((ZonedDateTime) model.getDateTimeLocal(), zonedId);
    }

    //------------------------
    // Adicionar uma nova reunião
    //------------------------
    private boolean addSlot(Temporal date){
        String local;
        String desc;
        Duration duration = getDurationFromInput();

        out.print("Local: ");
        local = Input.lerString();
        while (local.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchido." + RESET);
            out.print("Local: ");
            local = Input.lerString();
        }

        out.print("Descricao: ");
        desc= Input.lerString();
        while (desc.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchida." + RESET);
            out.print("Descricao: ");
            desc= Input.lerString();
        }

        Slot newSlot = Slot.of(date, duration, local, desc);
        return model.addSlot(newSlot, model.getSchedule());
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
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        String modeNormalized = "";//identifca o modo geral
        int pageIndex = 0;
        ZoneId referenceZonedId = getRefereceZoneId();
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZoned = getDateTimeFormatterZoned();
        List<List<String>> slotsOfMode = partitionIntoPages(model.getMainInfoSlots(referenceZonedId,dtfLocal,dtfZoned),25);
        int totalPages = slotsOfMode.size();
        do {
            switch (modeNormalized){
                case "":
                    slotsOfMode = partitionIntoPages(model.getMainInfoSlots(referenceZonedId,dtfLocal,dtfZoned),25);
                    totalPages =slotsOfMode.size();
                    break;
                case "diaria":
                    slotsOfMode = partitionIntoPages(model.getModeSlots(modeNormalized,currentDateMode.getDayOfMonth(),referenceZonedId,dtfLocal,dtfZoned),25);
                    totalPages = slotsOfMode.size();
                    break;
                case"semanal":
                    TemporalField woy = WeekFields.ISO.weekOfYear();
                    int weekNumber = currentDateMode.get(woy);
                    slotsOfMode = partitionIntoPages(model.getModeSlots(modeNormalized,weekNumber,referenceZonedId,dtfLocal,dtfZoned),25);
                    totalPages = slotsOfMode.size();
                    break;
                case"mensal":
                    slotsOfMode = partitionIntoPages(model.getModeSlots(modeNormalized,currentDateMode.getMonthValue(),referenceZonedId,dtfLocal,dtfZoned),25);
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
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao) {
                case ">": if ((pageIndex + 1) < totalPages) { pageIndex++; } break;
                case "<": if ((pageIndex - 1) >= 0) { pageIndex--; } break;
                case ">>": currentDateMode = changeDataMode(currentDateMode,modeNormalized,1); break;
                case "<<": currentDateMode = changeDataMode(currentDateMode,modeNormalized,-1); break;
                case "?" : help(); break;
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
                        for (String infoSlot : model.getMainInfoSlots(referenceZonedId,dtfLocal,dtfZoned)) {
                            long idSlot = getIdSlot(infoSlot);
                            if(idSlot>=0 & idSlot==Long.valueOf(opcao)){
                                if(model.existSlot(idSlot)){
                                    flowSelectBusySlot(idSlot);
                                    break;
                                }
                            }

                        }
                    }
                    else {
                        errorMessage = "Opcao Invalida!";
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
    // Fluxo ao selecionar uma reunião
    // Pode-se optar por alterar, remover, ver detalhes dessa reunião
    //------------------------
    public void flowSelectBusySlot(Long idSelectSlot) {
        Menu menu = viewScheduleTxt.getMenu(2);
        String opcao="";
        String errorMessage = "n/a";
        String statusMessage= "n/a";
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZone = getDateTimeFormatterZoned();
        Slot s;
        boolean res= false;
        do {
            s = model.getSlot(idSelectSlot);
            if(opcao.equals("R")){
                menu.addDescToTitle(Arrays.asList(""));
            }
            else{
                menu.addDescToTitle(slotToString(s, getRefereceZoneId(), dtfLocal, dtfZone, true));
            }
            menu.addErrorMessage(errorMessage);
            menu.addStatusMessage(statusMessage);
            errorMessage="n/a";
            statusMessage="n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao) {
                case "A": flowEditSlot(idSelectSlot); break;
                case "R": res= model.removeSlot(model.getSlot(idSelectSlot),model.getSchedule()); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida !"; break;
            }
            if(opcao.equals("R")){
                    if(res){
                        statusMessage="Removido com sucesso!";
                    }
                    else{
                        errorMessage= "De momento, não e possivel remover o slot!";
                    }
            }
        }
        while (!(opcao.equals("S")));
    }

    //------------------------
    // Fluxo de editar uma reunião
    // Pode-se optar por alterar a data, duracao, local e a descrição
    // Devolve o slot alterado ou o slot original
    //------------------------
    private void flowEditSlot(Long idSelectSlot){
        Menu menu = viewScheduleTxt.getMenu(4);
        String opcao;
        String errorMessage = "n/a";
        String statusMessage = "n/a";
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZone = getDateTimeFormatterZoned();
        Slot s;
        boolean res=false;
        do {
            s = model.getSlot(idSelectSlot);
            menu.addDescToTitle(slotToString(s,getRefereceZoneId(),dtfLocal,dtfZone, true));
            menu.addErrorMessage(errorMessage);
            menu.addStatusMessage(statusMessage);
            errorMessage="n/a";
            statusMessage="n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao) {
                case "DATA": flowEditDataSlot(idSelectSlot); break;
                case "D": res = editDuration(idSelectSlot); break;
                case "L": editLocal(idSelectSlot); statusMessage="Local alterado com sucesso!"; break;
                case "DESC": editDesc(idSelectSlot); statusMessage="Descricao alterada com sucesso!"; break;
                case "S": break;
                default: errorMessage = "Opcao Invalida !"; break;
            }
            if(opcao.equals("D")){
                if (res) {
                    statusMessage = "Alteracao efetuada com sucesso!";
                } else {
                    errorMessage = "Sobreposicao de reunioes!";
                }
            }

        }
        while (!opcao.equals("S"));
    }

    //------------------------
    // Editar descrição de uma reunião
    // Devolve o slot com a informação alterada caso seja possivel alterar
    //------------------------
    private void editDesc(Long idSelectSlot) {
        out.print("Nova descricao: ");
        String newDesc = Input.lerString();
        while (newDesc.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchida." + RESET);
            out.print("Nova descricao: ");
            newDesc = Input.lerString();
        }
        model.editDescSlot(idSelectSlot, newDesc);
    }

    //------------------------
    // Editar local de uma reunião
    // Devolve o slot com a informação alterada caso seja possivel alterar
    //------------------------
    private void editLocal(Long idSelectSlot) {
        out.print("Novo local: ");
        String newLocal = Input.lerString();
        while (newLocal.equals("")) {
            out.println(RED_BOLD + "[!] Tem de ser preenchido." + RESET);
            out.print("Novo local: ");
            newLocal = Input.lerString();
        }
        model.editLocalSlot(idSelectSlot, newLocal);
    }

    //------------------------
    // Editar duracao de uma reunião
    // Devolve o slot com a informação alterada caso seja possivel alterar
    //------------------------
    private boolean editDuration(Long idSelectSlot) {
        Duration newDuration = getDurationFromInput();
        return model.editDurationSlot(idSelectSlot, newDuration);
    }


    //------------------------
    // Fluxo para editar data de uma reunião
    // Avançar ou recuar dias, semanas, meses ou anos
    // Devolve o slot com a informação alterada caso seja possivel alterar
    //------------------------
    private void flowEditDataSlot(Long idSelectSlot) {
        Menu menu = viewScheduleTxt.getMenu(5);
        String opcao;
        String errorMessage = "n/a";
        String statusMessage = "n/a";
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZone = getDateTimeFormatterZoned();
        Slot s;
        boolean res=false;
        System.out.println("IdSelect -> " + idSelectSlot);
        do {
            s = model.getSlot(idSelectSlot);
            Temporal data;
            menu.addDescToTitle(Collections.singletonList(slotToString(s, getRefereceZoneId(), dtfLocal, dtfZone, true).get(0)));
            menu.addErrorMessage(errorMessage);
            menu.addStatusMessage(statusMessage);
            errorMessage = "n/a";
            statusMessage= "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao) {
                case "M":
                    if (isSlotfromReferenceZone(s, model.getLocalZone())) {
                        data = getDateTimeFromInput((ZonedDateTime) s.getData(), model.getLocalZone());
                        res =model.editDateSLot(idSelectSlot, data);
                    }
                    else {
                        //Utilizar o próprio para o zone
                        data = getZoneDateTimeFromInput(viewScheduleTxt.getMenu(8), ((ZonedDateTime) s.getData()).getZone().getId(), (ZonedDateTime) s.getData());
                        res = model.editDateSLot(idSelectSlot, data);
                    }
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
            if(opcao.equals("M")){
                if (res) {
                    statusMessage = "Alteracao efetuada com sucesso!";
                } else {
                    errorMessage = "Sobreposicao de reunioes!";
                }
            }
        }
        while (!opcao.equals("S")) ;
    }

    //------------------------
    // Breve explicação das opções do menu de visualização de reuniões agendadas
    //------------------------
    private void help() {
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
        flowHelp(viewScheduleTxt.getMenu(7),l);
    }
}
