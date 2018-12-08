package Controller.Class;

import Controller.Interface.InterfCalcDateTimeController;
import Model.Class.RestrictSlot;
import Model.Class.Slot;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeView;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Utilities.Utils.*;
import static Utilities.ConsoleColors.*;
import static Utilities.ControllerUtils.*;
import static java.lang.System.out;
import static java.util.Arrays.asList;

public class CalcDateTimeController implements InterfCalcDateTimeController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeView viewMainTxt;
    private InterfCalcDateTimeController controlLocal;
    private InterfCalcDateTimeController controlZone;
    private InterfCalcDateTimeController controlSchedule;

    public static CalcDateTimeController of(InterfCalcDateTimeController controlLocal,
                                            InterfCalcDateTimeController controlZone,
                                            InterfCalcDateTimeController controlSchedule) {
        return new CalcDateTimeController(controlLocal, controlZone, controlSchedule);
    }

    private CalcDateTimeController(InterfCalcDateTimeController controlLocal,
                                   InterfCalcDateTimeController controlZone,
                                   InterfCalcDateTimeController controlSchedule) {
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
        Menu menu;
        String opcao;
        String errorMessage = "n/a";
        do {
            menu = viewMainTxt.getDynamicMenu(0,"n/a",errorMessage);
            errorMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "L" : controlLocal.startFlow(); break;
                case "Z" : controlZone.startFlow(); break;
                case "A" : controlSchedule.startFlow(); break;
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
        Menu menu;
        String opcao;
        boolean configChanged = false;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            menu = viewMainTxt.getDynamicMenu(1,statusMessage,errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "FL": flowSetDateFormatLocal(); configChanged = true; break;
                case "FF": flowSetDateFormatZoned(); configChanged = true; break;
                case "F": flowSetZone(); configChanged = true; statusMessage = "Fuso local modificado"; break;
                case "R": flowRestrictSchedule(); configChanged = true; break;
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
        Menu menu;
        String opcao;
        Boolean res=false;
        String statusMessage = "n/a" ;
        String errorMessage = "Ira definir uma periodo de tempo para o qual nao sera permitido agendar reunioes" ;
        do{
            menu = viewMainTxt.getDynamicMenu(7,statusMessage,errorMessage);
            statusMessage = "n/a" ;
            errorMessage = "n/a" ;
            menu.show();
            opcao= Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "E":  res = addRestrictSchedule("pontual"); break;
                case "G": flowGlobalRestrictSchedule(); break;
                case "V": flowShowRestrictSchedule(); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
            if(opcao.equals("E")){
                if(res)
                    statusMessage = "Restricao adicionada com sucesso!";
                else
                    errorMessage = "Restricao em conflito com reunioes ja agendadas!";

            }
        }
        while(!opcao.equals("S"));
    }

    private void flowShowRestrictSchedule(){
        Menu menu;
        String opcao;
        List<String> description;
        String errorMessage= "n/a";
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

            menu = viewMainTxt.getDynamicMenu(9,"n/a",errorMessage, description);
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "<":
                case ">":
                case"S":
                default:
                    if (opcao.matches("=.*")) {
                        try {
                            long idSelect = Long.valueOf(opcao.substring(1)); // Remover o "="
                            errorMessage = "Nao existe um evento com esse identificador";

                            for (String infoSlot : model.getRestrictSlots(referenceZone,dtfLocal,dtfZone)) {
                                Long idSlot = getIdSlot(infoSlot);
                                if(idSlot>=0 & idSlot==idSelect){
                                   if(model.existRestrictSlot(idSlot)) {
                                        flowSelectRestrictSchedule(idSlot);
                                        errorMessage ="n/a";
                                        break;
                                    }
                                }
                            }
                        }
                        catch (NumberFormatException e){
                            errorMessage = "Opcao Invalida, introduza o identificador!";
                            break;
                        }

                    }
                    break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void flowSelectRestrictSchedule(Long idSelectSlot){
        Menu menu;
        String opcao;
        String statusMessage = "n/a" ;
        String errorMessage = "n/a" ;
        ZoneId referenceZone = ZonedDateTime.from(model.getDateTimeLocal()).getZone();
        DateTimeFormatter dtfLocal= DateTimeFormatter.ofPattern(model.getLocalDateTimeFormat());
        DateTimeFormatter dtfZone = DateTimeFormatter.ofPattern(model.getZoneDateTimeFormat());
        Slot s;
        do{
            s = model.getRestrictSlot(idSelectSlot);
            List<String> dataToShow = slotToString(s, referenceZone, dtfLocal, dtfZone, true);
            menu = viewMainTxt.getDynamicMenu(10,statusMessage,errorMessage,Arrays.asList(dataToShow.get(0),
                                                                                             dataToShow.get(2),
                                                                                             dataToShow.get(3)));
            statusMessage = "n/a" ;
            errorMessage = "n/a" ;
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "R": model.removeRestrictSlot(idSelectSlot);break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;

            }
        }
        while(!(opcao.equals("S")| opcao.equals("R")));
    }

    private boolean flowGlobalRestrictSchedule(){
        Menu menu;
        boolean res= false;
        String opcao;
        String statusMessage = "n/a" ;
        String errorMessage = "n/a" ;
        do{
            menu = viewMainTxt.getDynamicMenu(8,statusMessage,errorMessage);
            statusMessage = "n/a" ;
            errorMessage = "n/a" ;
            menu.show();
            opcao= Input.lerString();
            opcao = opcao.toUpperCase();
            switch (opcao){
                case "DIA": res = addRestrictSchedule("diaria"); break;
                case "SEM": res = addRestrictSchedule("semanal"); break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
            if(opcao.equals("DIA") | opcao.equals("SEM")){
                if(res)
                    statusMessage = "Restricao adicionada com sucesso!";
                else
                    errorMessage="Restricao em conflito com reunioes ja agendadas!";
            }
        }
        while(!(opcao.equals("S")));
        return false;
    }

    private boolean addRestrictSchedule(String mode){
        boolean res = false;
        ZonedDateTime zdt = ZonedDateTime.from(model.getDateTimeLocal());
        Temporal date = getDateTimeFromInput(zdt, zdt.getZone());
        Duration duration = getDurationFromInput();
        String desc = getDescFromInput();
        RestrictSlot newSlot = RestrictSlot.of(model.getIdRestrictSlot(), date, duration, null, desc, mode);
        int numAdded = model.addRestrictSlot(newSlot);
        if (numAdded > 0) { res = true; model.incNIdRestrictSlot(numAdded); }
        return res;
    }

    private void flowSetZone() {
        String zone = flowGetNZoneIds(1,viewMainTxt, 2, ZoneId.systemDefault().toString()).get(0);
        model.withZoneLocal(zone);
        model.withZoneZone(zone);
    }

    private void flowSetDateFormatLocal(){
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            menu = viewMainTxt.getDynamicMenu(4,statusMessage,errorMessage);
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
        Menu menu;
        String opcao;
        String statusMessage = "n/a" ;
        String errorMessage = "n/a";
        do {
            menu = viewMainTxt.getDynamicMenu(5, statusMessage, errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "1":
                    model.setLocalDateTimeFormat("dd-MM-yyyy HH:mm");
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "2":
                    model.setLocalDateTimeFormat("dd-MM-yyyy HH:mm:ss");
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "3":
                    model.setLocalDateTimeFormat("dd-MM-yyyy HH:mm:ss:nnnnnnnnn");
                    statusMessage = "Formato de apresentacao local modificado";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void flowSetDateFormatZoned(){
        Menu menu;
        String opcao;
        String statusMessage = "n/a" ;
        String errorMessage = "n/a";
        do {
            menu = viewMainTxt.getDynamicMenu(4,statusMessage,errorMessage);
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
                case "?": helpFormatZone(); break;
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
        Menu menu;
        String opcao;
        String statusMessage = "n/a";
        String errorMessage = "n/a";
        do {
            menu = viewMainTxt.getDynamicMenu(6, statusMessage, errorMessage);
            errorMessage = "n/a";
            statusMessage = "n/a";
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "1":
                    model.setZoneDateTimeFormat("dd-MM-yyyy HH:mm VV");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "2":
                    model.setZoneDateTimeFormat("dd-MM-yyyy HH:mm:ss VV");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "3":
                    model.setZoneDateTimeFormat("dd-MM-yyyy HH:mm:ss O");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "4":
                    model.setZoneDateTimeFormat("dd-MM-yyyy HH:mm VV O");
                    statusMessage = "Formato de apresentacao de datas com zonas modificado";
                    break;
                case "S": break;
                default: errorMessage = "Opcao Invalida!"; break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void helpConfig() {
        List<String> l = asList(
                BLACK_BOLD + "Opcao FL:" + RESET + " permite ao utilizador definir o formato",
                "          de apresentacao das datas locais.",
                " ",
                BLACK_BOLD + "Opcao FF:" + RESET + " permite ao utilizador definir o formato",
                "          de apresentacao das datas com fusos.",
                " ",
                BLACK_BOLD + "Opcao F:" + RESET + " permite ao utilizador definir o seu ",
                "         fuso local.",
                " ",
                BLACK_BOLD + "Opcao R:" + RESET + " permite ao utilizador definir restricoes",
                "         que queira impor sobre a agenda. Podera ser",
                "         uma restricao numa data especifica, ou uma",
                "         restricao global a agenda, desde diaria a semanal.",
                " ",
                BLACK_BOLD + "Opcao ?:" + RESET + " permite ao utilizador visualizar este menu.",
                " ",
                BLACK_BOLD + "Opcao S:" + RESET + " permite ao utilizador voltar para o menu principal.");
        flowHelp(viewMainTxt.getMenu(3), l);
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
                BLACK_BOLD + "Opcao C:" + RESET + " permite ao utilizador definir o formato",
                "         da apresentacao das datas, o seu fuso local e",
                "         restricoes que queira impor sobre a agenda.",
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
                "   M: mes formato numero (12)",
                "   L: mes formato textual (dezembro)",
                "   d: dia do mes (24)",
                "   D: dia do ano (189)",
                "",
                "   W: Semana do mes com base no primeiro dia da semana",
                "   E : dia da semana formato textual (Tuesday)",
                "",
                "   a: am ou pm",
                "   h: horas apresentadas (1-12)",
                "   k: horas apresentadas (1-24)",
                "   H: horas de um dia (0-23)",
                "   m: minutos de uma hora",
                "   s: segundos de um minuto",
                "   A: mili de um dia",
                "   n: nano-segundos",
                "   N: nano de um dia",
                "",
                "   VV: identificador do fuso (Europe/Lisbon)",
                "   z: nome do fuso (Portugal)",
                "   O: zone-offset (GMT+0)"
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
                "   M: mes formato numero (12)",
                "   L: mes formato textual (dezembro)",
                "   d: dia do mes (24)",
                "   D: dia do ano (189)",
                "",
                "   W: Semana do mes com base no primeiro dia da semana",
                "   E : dia da semana formato textual (Tuesday)",
                "",
                "   a: am ou pm",
                "   h: horas apresentadas (1-12)",
                "   k: horas apresentadas (1-24)",
                "   H: horas de um dia (0-23)",
                "   m: minutos de uma hora",
                "   s: segundos de um minuto",
                "   A: mili de um dia",
                "   n: nano-segundos",
                "   N: nano de um dia"
        );
        flowHelp(viewMainTxt.getMenu(3), l);
    }
}
