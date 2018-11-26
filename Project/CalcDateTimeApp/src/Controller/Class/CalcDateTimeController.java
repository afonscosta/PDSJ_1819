package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Controller.Interface.InterfCalcDateTimeLocalController;
import Controller.Interface.InterfCalcDateTimeScheduleController;
import Controller.Interface.InterfCalcDateTimeZoneController;
import Model.Class.RestrictSlot;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.BusinessUtils;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utilities.BusinessUtils.getIdSlot;
import static Utilities.BusinessUtils.partitionIntoPages;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.util.Arrays.asList;

public class CalcDateTimeController implements InterfCalcDateTimeController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeView viewMainTxt;
    private InterfCalcDateTimeLocalController controlLocal;
    private InterfCalcDateTimeZoneController controlZone;
    private InterfCalcDateTimeScheduleController controlSchedule;

    public static CalcDateTimeController of(InterfCalcDateTimeLocalController controlLocal,
                                            InterfCalcDateTimeZoneController controlZone,
                                            InterfCalcDateTimeScheduleController controlSchedule) {
        return new CalcDateTimeController(controlLocal, controlZone, controlSchedule);
    }

    private CalcDateTimeController(InterfCalcDateTimeLocalController controlLocal,
                                   InterfCalcDateTimeZoneController controlZone,
                                   InterfCalcDateTimeScheduleController controlSchedule) {
        this.controlLocal = controlLocal;
        this.controlZone = controlZone;
        this.controlSchedule = controlSchedule;
    }

    @Override
    public void setView(InterfCalcDateTimeView view) {
        this.viewMainTxt = view;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }


    //------------------------
    // Fluxo inicial do programa
    //------------------------
    @Override
    public void startFlow() {
        // Início do fluxo de execução
        Menu menu = viewMainTxt.getMenu(0);
        String opcao;
        String errorMessage = "n/a";
        do {
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : controlLocal.flowLocal(); break;
                case "Z" : controlZone.flowZone(); break;
                case "A" : controlSchedule.flowSchedule(); break;
                case "C" : flowConfig(); break;
                case "?" : helpMain(); break;
                case "S": model.saveConfigs(); break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    //------------------------
    // Menu de configuração
    //------------------------
    private void flowConfig() {
        Menu menu = viewMainTxt.getMenu(1);
        String opcao;
        boolean configChanged = false;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "FL": flowSetDateFormatLocal(); configChanged = true; break;
                case "FF": flowSetDateFormatZoned(); configChanged = true; break;
                case "F": flowSetZone(); configChanged = true; statusMessage = "Fuso local modificado"; break;
                case "H": flowRestrictSchedule(); break;
                case "?": helpConfig(); statusMessage = "n/a"; break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));

        if (configChanged) {
            model.saveConfigs();
        }
    }

    private void flowRestrictSchedule(){
        Menu menu = viewMainTxt.getMenu(7);
        String opcao;
        Boolean configChanged;
        String statusMessage = "n/a" ;
        Boolean flowDone= false;
        do{
            menu.addDescToTitle(Arrays.asList(RED_BOLD + "Ira definir uma periodo de tempo para o qual nao sera permitido agendar reunioes" + RESET));
            menu.addStatusMessage(statusMessage);
            menu.show();
            opcao= Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "E":  configChanged=addRestrictSchedule("pontual");
                            //flowDone=true;
                            if(configChanged==true)
                                statusMessage = "Limitacao de horario adicionada!";
                            else
                                statusMessage=(RED_BOLD +"Restricao em conflito com reunioes ja definidas!"+ RESET);
                            break;
                case "G": configChanged = flowGlobalRestrictSchedule();
                           // flowDone = true;
                            if(configChanged==true)
                                statusMessage = "Limitacao de horario adicionada!";
                            else
                                statusMessage=(RED_BOLD +"Restricao em conflito com reunioes ja definidas!"+ RESET);
                            break;
                case "V": flowShowRestrictSchedule(); /*flowDone= true; */break;
                case "S": flowDone = true; break;
            }
        }
        while(!flowDone);
    }

    private void flowShowRestrictSchedule(){
        Menu menu = viewMainTxt.getMenu(9);
        String opcao;
        List<String> description;
        ZoneId referenceZone = ZonedDateTime.from(model.getDateTimeLocal()).getZone();
        DateTimeFormatter dtfLocal= DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat());
        DateTimeFormatter dtfZone = DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat());
        int pageIndex = 0;
        do{
            List<List<String>> restrictSlots = partitionIntoPages(model.getRestrictSlots(referenceZone,dtfLocal,dtfZone),25);
            int totalPages = restrictSlots.size();
            try {
                description = new ArrayList(restrictSlots.get(pageIndex));
            } catch (IndexOutOfBoundsException e) {
                description = new ArrayList<>();
            }

            description.add(""); // Linha branca na descrição
            int pageIndexToDisplay = (totalPages == 0) ? 0 : pageIndex + 1;
            description.add(String.format("Pagina (%s/%s)", pageIndexToDisplay, totalPages));

            menu.addDescToTitle(description);
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "<":
                case ">":
                case"S":
                default:
                    if (opcao.matches("=.*")) {
                        opcao = opcao.substring(1); // Remover o "="

                        for (String infoSlot : model.getRestrictSlots(referenceZone,dtfLocal,dtfZone)) {
                            String idSlot = getIdSlot(infoSlot);
                            if(idSlot!=null & idSlot.equals(opcao)){
                                Slot s= model.getSlot(idSlot,model.getScheduleRestrictions());
                                if(s!=null) {
                                    flowSelectRestrictSchedule(s);
                                    break;
                                }
                            }
                        }

                    }
                    break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void flowSelectRestrictSchedule(Slot s){
        Menu menu = viewMainTxt.getMenu(10);
        String opcao;
        boolean flowDone=false;
        ZoneId referenceZone = ZonedDateTime.from(model.getDateTimeLocal()).getZone();
        DateTimeFormatter dtfLocal= DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat());
        DateTimeFormatter dtfZone = DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat());
        do{
            String dataToShow = BusinessUtils.DateSlotToString(s,referenceZone,dtfLocal,dtfZone);
            menu.addDescToTitle(Arrays.asList(dataToShow,
                                            s.getDuration().toString(),
                                            s.getDescription())
            );
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "R": model.removeSlot(s,model.getScheduleRestrictions()); flowDone = true; break;
                case "S": flowDone = true; break;

            }
        }
        while(!flowDone);
    }

    private boolean flowGlobalRestrictSchedule(){
        Menu menu = viewMainTxt.getMenu(8);
        String opcao;

        do{
            menu.show();
            opcao= Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "DIA": return addRestrictSchedule("diaria");
                case "SEM": return addRestrictSchedule("semanal");
                case "S": break;
            }
        }
        while(!(opcao.equals("S")));
        return false;
    }

    private boolean addRestrictSchedule(String mode){
        ZonedDateTime zdt = ZonedDateTime.from(model.getDateTimeLocal());
        ZoneId referenceZoneID = zdt.getZone();
        out.println("Data da restricao:");
        Temporal date = getDateTimeFromInput((ZonedDateTime) model.getDateTimeLocal(), referenceZoneID);
        out.println("Duracao");
        out.print("Horas ira demorar: ");
        int horas = Input.lerInt();
        out.print("Minutos ira demorar: ");
        int minutos = Input.lerInt();
        Duration duration = Duration.of(horas, ChronoUnit.HOURS);
        duration = duration.plus(minutos, ChronoUnit.MINUTES);

        out.print("Introduza uma descricao da restricao: ");
        String desc = Input.lerString();

        RestrictSlot newSlot = RestrictSlot.of(date, duration, null, desc, mode);
        return model.addSlot(newSlot,model.getScheduleRestrictions());

    }

    private void flowSetZone() {
        String zone = flowGetNZoneIds(1,viewMainTxt.getMenu(2), ZoneId.systemDefault().toString()).get(0);
        model.withZoneLocal(zone);
        model.withZoneZone(zone);
    }

    private void flowSetDateFormatLocal(){
        Menu menu = viewMainTxt.getMenu(4);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P": flowPreDefinedDateFormatLocal(); break;
                case "M":
                    model.setLocalDateTimeFormat(setDinamicDateFormatLocal());
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "?": helpFormatLocal(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }


    private String setDinamicDateFormatLocal(){
        out.println("Insira o formato pretendido para datas locais: ");
        return getDateTimeFormatterFromInput();
    }

    private void flowPreDefinedDateFormatLocal(){
        Menu menu = viewMainTxt.getMenu(5);
        String opcao;
        String statusMessage = "n/a" ;
        String errorMessage = "n/a";
        do {
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "1":
                    model.setLocalDateTimeFormat("dd-MM-yyy HH:mm");
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "2":
                    model.setLocalDateTimeFormat("dd-MM-yyy HH:mm:ss");
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "3":
                    model.setLocalDateTimeFormat("dd-MM-yyy HH:mm:ss:nn");
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void flowSetDateFormatZoned(){
        Menu menu = viewMainTxt.getMenu(4);
        String opcao;
        String statusMessage = "n/a" ;
        String errorMessage = "n/a";
        do {
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P": flowPreDefinedDateFormatZoned(); break;
                case "M":
                    model.setZoneDateTimeFormat(setDinamicDateFormatZoned());
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private String setDinamicDateFormatZoned(){
        out.println("Insira o formato pretendido para datas com fuso: ");
        return getDateTimeFormatterFromInput();
    }

    private void flowPreDefinedDateFormatZoned(){
        Menu menu = viewMainTxt.getMenu(6);
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            menu.addStatusMessage(statusMessage);
            menu.addErrorMessage(errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "1":
                    model.setZoneDateTimeFormat("dd-MM-yyy HH:mm VV");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "2":
                    model.setZoneDateTimeFormat("dd-MM-yyy HH:mm:ss VV");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "3":
                    model.setZoneDateTimeFormat("dd-MM-yyy HH:mm:ss O");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "4":
                    model.setZoneDateTimeFormat("dd-MM-yyy HH:mm VV O");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void setLocal() {
        String zone = flowGetNZoneIds(1,viewMainTxt.getMenu(2), ZoneId.systemDefault().toString()).get(0);
        controlLocal.withZone(zone);
    }

    private void helpConfig() {
        out.println("Menu de ajuda da configuracao.");
        Input.lerString();
    }

    //------------------------
    // Menu de ajuda do menu principal
    //------------------------
    private void helpMain() {
        List<String> l = asList(
                BLACK_BOLD + "Opcao L:" + RESET + " permite ao utilizador aceder ao menu ",
                "         responsavel por interagir com datas e tempos locais.",
                " ",
                BLACK_BOLD + "Opcao Z:" + RESET + " permite ao utilizador aceder ao menu ",
                "         responsavel por interagir com datas e ",
                "         e tempos, associados a fusos.",
                " ",
                BLACK_BOLD + "Opcao A:" + RESET + " permite ao utilizador aceder ao menu ",
                "         onde se encontram as funcionalidades da agenda.",
                " ",
                BLACK_BOLD + "Opcao ?:" + RESET + " permite ao utilizador visualizar este menu.",
                " ",
                BLACK_BOLD + "Opcao S:" + RESET + " permite ao utilizador sair da aplicacao.");
        flowHelp(viewMainTxt.getMenu(3), l);
    }

    //------------------------
    // Breve explicação das opções do menu de visualização de reuniões agendadas
    //------------------------
    private void helpFormatZone() {
        List<String> l = asList(
                "Pode ser feita qualquer combinacao com:",
                "   G: era formato textual",
                "   y: ano (2018)",
                "   M: mes formato numero(12)",
                "   L: mes formato textual(Dezembro)",
                "   d: dia do mes(24)",
                "   D: dia do ano(189)",
                "",
                "   W: Semana do mes com base no primeiro dia da semana",
                "   E : dia da semana formato textual(Tuesday)",
                "",
                "   a: am ou pm",
                "   h: horas apresentadas(1-12)",
                "   k: horas apresentadas(1-24)",
                "   H: horas de um dia(0-23)",
                "   m: minutos de uma hora",
                "   s: segundos de um minuto",
                "   A: mili de um dia",
                "   n: nano-segundos",
                "   N: nano de um dia",
                "",
                "   VV: identificador do fuso (Europe/Lisbon)",
                "   z: nome do fuso (Portugal)",
                "   O: zone-offset(GMT+0)"
            );
        flowHelp(viewMainTxt.getMenu(3), l);
    }

    //------------------------
    // Breve explicação das opções do menu de visualização de reuniões agendadas
    //------------------------
    private void helpFormatLocal() {
        List<String> l = asList(
                "Pode ser feita qualquer combinacao com:",
                "   G: era formato textual",
                "   y: ano (2018)",
                "   M: mes formato numero(12)",
                "   L: mes formato textual(Dezembro)",
                "   d: dia do mes(24)",
                "   D: dia do ano(189)",
                "",
                "   W: Semana do mes com base no primeiro dia da semana",
                "   E : dia da semana formato textual(Tuesday)",
                "",
                "   a: am ou pm",
                "   h: horas apresentadas(1-12)",
                "   k: horas apresentadas(1-24)",
                "   H: horas de um dia(0-23)",
                "   m: minutos de uma hora",
                "   s: segundos de um minuto",
                "   A: mili de um dia",
                "   n: nano-segundos",
                "   N: nano de um dia"
        );
        flowHelp(viewMainTxt.getMenu(3), l);
    }
}
