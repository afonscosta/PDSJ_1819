package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Utilities.Utils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.time.temporal.ChronoUnit.*;
import static java.util.Arrays.asList;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeView viewScheduleTxt;

    public static CalcDateTimeScheduleController of() {
        return new CalcDateTimeScheduleController();
    }

    private CalcDateTimeScheduleController() { }

    @Override
    public void setView(InterfCalcDateTimeView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    //------------------------
    // Retorna o formato para LocalDateTime definido no ficheiro de configurações
    //------------------------
    private DateTimeFormatter getDateTimeFormatterLocal(){
        return DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat());
    }

    //------------------------
    // Retorna o formato para ZonedDateTime definido no ficheiro de configurações
    //------------------------
    private DateTimeFormatter getDateTimeFormatterZoned(){
        return DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat());
    }

    //------------------------
    // Retorna o zonedId definido no ficheiro de configurações
    //------------------------
    private ZoneId getRefereceZoneId(){
        ZonedDateTime zdt = ZonedDateTime.from(model.getDateTimeLocal());
        return zdt.getZone();
    }

    //------------------------
    // Fluxo inicial do menu agenda
    //------------------------
    @Override
    public void startFlow(){
        String opcao;
        String errorMessage = "n/a";
        Menu menu;
        do {
            menu = viewScheduleTxt.getDynamicMenu(0,"n/a", errorMessage);
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
    // Fluxo de adicionar um novo evento
    // Pode-se optar por usar a data da calculora local, de zona ou inserir uma data manual com fusos ou sem fusos
    //------------------------
    private void flowAddSlot(){
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        boolean res=false;
        do {
            Temporal tempLocal = model.getDateTimeLocal();
            Temporal tempZone = model.getDateTimeZone();
            String ldt = localDateTimeToString(tempLocal, getDateTimeFormatterLocal());
            String zdt = zoneDateTimeToString((ZonedDateTime)tempZone, getDateTimeFormatterZoned());
            menu = viewScheduleTxt.getDynamicMenu(3, statusMessage, errorMessage, asList("Data calc. local: " + ldt,
                                                                                             "Data calc. com fusos: " + zdt));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : res = addSlot(tempLocal);break;
                case "Z" : res = addSlot(tempZone); break;
                case "ML": Temporal date = getManualDateTime("ML"); res = addSlot(date); break;
                case "MF":  date = getManualDateTime("MF"); res = addSlot(date); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida !"; break;
            }
            if(opcao.equals("L")| opcao.equals("Z") | opcao.equals("ML") | opcao.equals("MF")){
                if(res){
                    statusMessage="Reuniao adicionada com sucesso!";
                }
                else{
                    errorMessage="Sobreposicao com eventos ja agendados ou restricoes...";
                }
            }
        }
        while(!(opcao.equals("S")));
    }

    //------------------------
    // Introdução manual por parte do utilizador de uma datetime com fusos ou sem fusos
    //------------------------
    private Temporal getManualDateTime(String typeOfDate){
        ZoneId zonedId = getRefereceZoneId();
        if(typeOfDate.equals("MF")){
            String zoneIdString = flowGetNZoneIds(1, viewScheduleTxt, 8, model.getZoneZone()).get(0);
            zonedId = ZoneId.of(zoneIdString);
        }
        return  getDateTimeFromInput((ZonedDateTime) model.getDateTimeLocal(), zonedId);
    }

    //------------------------
    // Adicionar um novo evento
    //------------------------
    private boolean addSlot(Temporal date){
        Duration duration = getDurationFromInput();
        String local = getLocalFromInput();
        String desc = getDescFromInput();
        Slot newSlot = Slot.of(model.getIdSlot(), date, duration, local, desc);
        boolean added = model.addSlot(newSlot);
        if (added) { System.out.println("Entrou aqui!"); model.incNIdSlot(1); }
        return added;
    }

    //------------------------
    // Fluxo de visualizar eventos agendados
    // currentDateMode -> identifica a localdate que está a ser usada nas vistas
    // modeNormalized -> identifica a vista selecionada
    //------------------------
    private void flowGetBusySlots() {
        LocalDate currentDateMode = LocalDate.now();
        Menu menu;
        List<String> description;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        String modeNormalized = ""; //identifica a vista geral
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
                    default: errorMessage = "Vista existentes sao /diaria /semanal /mensal"; modeNormalized=""; break;
            }
            try {
                description = new ArrayList(slotsOfMode.get(pageIndex));
            } catch (IndexOutOfBoundsException e) {
                description = new ArrayList<>();
            }
            description.add(""); // Linha branca na descrição
            int pageIndexToDisplay = (totalPages == 0) ? 0 : pageIndex + 1;
            description.add(String.format("Pagina (%s/%s)", pageIndexToDisplay, totalPages));

            menu = viewScheduleTxt.getDynamicMenu(1,statusMessage,errorMessage,description);
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
                case "S": break;
                default:
                    if (opcao.matches("\\/.*")) {
                        pageIndex = 0;
                        modeNormalized = opcao.substring(1).toLowerCase(); // Remover o "/" e lowercase
                        currentDateMode = LocalDate.now(); //ao atualizar o modo, volta ao now
                    }
                    else if (opcao.matches("=.*")) {
                         try {
                            long idSelect = Long.valueOf(opcao.substring(1)); // Remover o "="
                             errorMessage = "Nao existe um evento com esse identificador";
                             for (String infoSlot : model.getMainInfoSlots(referenceZonedId, dtfLocal, dtfZoned)) {
                                long idSlot = getIdSlot(infoSlot);
                                if (idSlot >= 0 & idSlot == idSelect) {
                                    if (model.existSlot(idSlot)) {
                                        flowSelectBusySlot(idSlot);
                                        errorMessage ="n/a";
                                        break;
                                    }
                                }
                            }
                        }
                        catch (NumberFormatException e) {
                             errorMessage = "Opcao Invalida, introduza o identificador!";
                            break;
                        }
                    }
                    else {
                        errorMessage = "Opcao Invalida!";
                    }
                    break;
            }
        } while(!(opcao.equals("S")));
    }

    //------------------------
    // Dado modo, a data atual do modo e a operação desejada pelo utilizador, atualiza a data do modo.
    //------------------------
    private LocalDate changeDataMode(LocalDate currentDateMode, String mode, int n){
        switch (mode){
            case "":
                return currentDateMode;
            case "diaria":
                return (LocalDate) shiftDateTime(currentDateMode,n,DAYS);
            case"semanal":
                return (LocalDate) shiftDateTime(currentDateMode,n,WEEKS);
            case"mensal":
                return (LocalDate) shiftDateTime(currentDateMode,n,MONTHS);
        }
        return currentDateMode;
    }

    //------------------------
    // Fluxo ao selecionar um evento
    // Pode-se optar por alterar, remover, ver detalhes desse evento
    //------------------------
    private void flowSelectBusySlot(Long idSelectSlot) {
        Menu menu;
        String opcao;
        String errorMessage = "n/a";
        String statusMessage= "n/a";
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZone = getDateTimeFormatterZoned();
        Slot s;
        do {
            s = model.getSlot(idSelectSlot);
            menu = viewScheduleTxt.getDynamicMenu(2,statusMessage,errorMessage,slotToString(s, getRefereceZoneId(), dtfLocal, dtfZone, true));
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao) {
                case "A":
                        flowEditSlot(idSelectSlot);
                        break;
                case "R":
                    model.removeSlot(idSelectSlot);
                    break;
                case "S":
                    break;
                default:
                    errorMessage = "Opcao Invalida !";
                    break;
            }
        }
        while (!(opcao.equals("S") | opcao.equals("R")));
    }

    //------------------------
    // Fluxo de editar um evento
    // Pode-se optar por alterar a data, duracao, local e a descrição
    //------------------------
    private void flowEditSlot(Long idSelectSlot){
        Menu menu;
        String opcao;
        String errorMessage = "n/a";
        String statusMessage = "n/a";
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZone = getDateTimeFormatterZoned();
        Slot s;
        boolean res=false;
        do {
            s = model.getSlot(idSelectSlot);
            menu = viewScheduleTxt.getDynamicMenu(4,statusMessage,errorMessage,slotToString(s,getRefereceZoneId(),dtfLocal,dtfZone, true));
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
    // Editar descrição de um evento
    //------------------------
    private void editDesc(Long idSelectSlot) {
        String newDesc = getNewDescFromInput();
        model.editDescSlot(idSelectSlot, newDesc);
    }

    //------------------------
    // Editar local de um evento
    //------------------------
    private void editLocal(Long idSelectSlot) {
        String newLocal = getNewLocalFromInput();
        model.editLocalSlot(idSelectSlot, newLocal);
    }

    //------------------------
    // Editar duracao de um evento
    // É confirmado se a alteração da duração não provoca sobreposição com outros eventos
    //------------------------
    private boolean editDuration(Long idSelectSlot) {
        Duration newDuration = getDurationFromInput();
        return model.editDurationSlot(idSelectSlot, newDuration);
    }

    //------------------------
    // Fluxo para editar data de um evento
    //------------------------
    private void flowEditDataSlot(Long idSelectSlot) {
        Menu menu;
        String opcao;
        String errorMessage = "n/a";
        String statusMessage = "n/a";
        DateTimeFormatter dtfLocal = getDateTimeFormatterLocal();
        DateTimeFormatter dtfZone = getDateTimeFormatterZoned();
        Slot s;
        boolean res=false;
        do {
            s = model.getSlot(idSelectSlot);
            Temporal data;
            menu = viewScheduleTxt.getDynamicMenu(5, statusMessage, errorMessage, Collections.singletonList(slotToString(s, getRefereceZoneId(), dtfLocal, dtfZone, true).get(0)));
            errorMessage = "n/a";
            statusMessage= "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao) {
                case "M":
                    if (isSlotfromReferenceZone(s, model.getLocalZone())) {
                        data = getDateTimeFromInput((ZonedDateTime) s.getDate(), model.getLocalZone());
                        res =model.editDateSLot(idSelectSlot, data);
                    }
                    else {
                        //Utilizar o próprio para o zone
                        data = getZoneDateTimeFromInput(viewScheduleTxt, 8, ((ZonedDateTime) s.getDate()).getZone().getId(), (ZonedDateTime) s.getDate());
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
    // Breve explicação das opções do menu de visualização de eventos agendados
    //------------------------
    private void help() {
        List<String> l = asList(
                BLACK_BOLD + "Opcoes:" + RESET,
                BLACK_BOLD + "=id_evento" + RESET +
                        "   Cada reuniao contem antes da sua descricao",
                "         um identificador.",
                "         Pretendendo selecionar, por exemplo, a reuniao",
                "         com o identifcador 0, o utilizador devera introduzir",
                "         a opcao "+ RESET + BLACK_BOLD + "=0" + RESET +".",
                "         No novo menu apresentado podera alterar qualquer ",
                "         dado da reuniao, remover ou ver detalhes da mesma.",
                " ",
                BLACK_BOLD + "<" + RESET +
                        "        Recuar a pagina que esta ser apresentada.",
                BLACK_BOLD + ">" + RESET +
                        "        Avancar a pagina que esta ser apresentada.",
                " ",
                BLACK_BOLD + "/<vista>" + RESET +
                        " Permite ter uma visao diaria, semanal ou mensal",
                "         das reunioes. Se pretender uma visao diaria devera ",
                "         introduzir" + RESET + BLACK_BOLD + " /diaria " + RESET + "onde lhe sera apresentado",
                "         inicialmente as reunioes do dia corrente.",
                "         Caso pretenda visualizar todas as reunioes",
                "         agendadas devera introduzir apenas" + RESET + BLACK_BOLD + " /" + RESET + ".",
                "         O principio e o mesmo para os restantes",
                "         modos de visualizacao."+ RESET,
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
                "         apresentacao no mesmo intervalo."
                );
        flowHelp(viewScheduleTxt.getMenu(7),l);
    }
}
