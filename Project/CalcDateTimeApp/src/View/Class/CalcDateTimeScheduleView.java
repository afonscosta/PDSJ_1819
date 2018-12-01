package View.Class;

import Utilities.Menu;
import Utilities.Menus;
import Utilities.Opcao;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.util.Arrays;
import java.util.List;

public class CalcDateTimeScheduleView implements InterfCalcDateTimeScheduleView {
    private Menus menuScheduleViewTxt;

    public CalcDateTimeScheduleView() {
        menuScheduleViewTxt = initView();
    }

    private Menus initView() {
        Menus menusScheduleTxt = new Menus();
        Opcao op1, op2, op3, op4, op5,op6,op7,op8;

        //------------------------
        // Menu CalcDateTimeSchedule
        //------------------------
        op1 = new Opcao("Inserir evento", "I");
        op2 = new Opcao("Visualizar eventos agendados", "V");
        op3 = new Opcao("Voltar ao Menu Principal", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3);
        Menu menuDateTimeSchedule = new Menu(linhas, "Agenda Eventos");
        menusScheduleTxt.addMenu(0, menuDateTimeSchedule);

        //------------------------
        // Menu Busy slots navigator
        //------------------------
        op1 = new Opcao("Selecionar", "=id_evento_pretendido");
        op2 = new Opcao("Pagina anterior", "<");
        op3 = new Opcao("Pagina seguinte", ">");
        op4 = new Opcao("Vista(diaria/semanal/mensal)", "/vista_pretendida");
        op5 = new Opcao("Avancar (dia/semana/mes) na vista", ">>");
        op6 = new Opcao("Recuar (dia/semana/mes) na vista", "<<");
        op7 = new Opcao("Ajuda", "?");
        op8 = new Opcao("Voltar ao Menu Agenda", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5,op6,op7,op8);
        Menu menuBusySlotsNavigator = new Menu(linhas, "Eventos Agendados");
        menusScheduleTxt.addMenu(1, menuBusySlotsNavigator);

        //------------------------
        // Menu select busy slots
        //------------------------
        op1 = new Opcao("Alterar", "A");
        op2 = new Opcao("Remover", "R");
        op3 = new Opcao("Voltar ao Menu eventos agendados", "S");
        linhas = Arrays.asList(op1, op2,op3);
        Menu menuSelectBusySlot = new Menu(linhas, "Evento");
        menusScheduleTxt.addMenu(2, menuSelectBusySlot);

        //------------------------
        // Menu date choice
        //------------------------
        op1 = new Opcao("Usar a data da calculadora data/tempo local", "L");
        op2 = new Opcao("Usar a data da calculadora data/tempo com fusos", "Z");
        op3 = new Opcao("Inserir manualmente uma evento local", "ML");
        op4 = new Opcao("Inserir manualmente uma evento com fusos", "MF");
        op5 = new Opcao("Voltar ao Menu Agenda", "S");
        linhas = Arrays.asList(op1, op2,op3,op4,op5);
        Menu menuDateChoice = new Menu(linhas, "Dados do evento a inserir");
        menusScheduleTxt.addMenu(3, menuDateChoice);

        //------------------------
        // Menu alter slot
        //------------------------
        op1 = new Opcao("Alterar data", "Data");
        op2 = new Opcao("Alterar duracao", "D");
        op3 = new Opcao("Alterar local", "L");
        op4 = new Opcao("Alterar descricao", "Desc");
        op5 = new Opcao("Voltar ao Menu Agenda", "S");
        linhas = Arrays.asList(op1, op2,op3,op4,op5);
        Menu menuAlterSlot = new Menu(linhas, "Alterar evento");
        menusScheduleTxt.addMenu(4, menuAlterSlot);

        //------------------------
        // Menu alter data
        //------------------------

        op1 = new Opcao("Alterar manualmente", "M");
        op2 = new Opcao("Voltar ao Menu evento", "S");
        linhas = Arrays.asList(op1,op2);
        Menu menuShiftDateTime = new Menu(linhas, "Alterar Data");
        menusScheduleTxt.addMenu(5, menuShiftDateTime);

        //------------------------
        // Menu details slot
        //------------------------

        op1 = new Opcao("Voltar ao Menu evento", "S");
        linhas = Arrays.asList(op1);
        Menu menuDetailsSlot = new Menu(linhas, "Alterar Data");
        menusScheduleTxt.addMenu(6, menuDetailsSlot);

        //------------------------
        // Menu Help
        //------------------------
        op1 = new Opcao("Voltar ao Menu Eventos Agendados", "S");
        linhas = Arrays.asList(op1);
        Menu menuHelp = new Menu(linhas, "Menu Ajuda");
        menusScheduleTxt.addMenu(7, menuHelp);

        //------------------------
        // Menu Navegador de ZoneIds
        //------------------------
        op1 = new Opcao("Pagina anterior", "<");
        op2 = new Opcao("Procurar", "/palavra_a_procurar");
        op3 = new Opcao("Pagina seguinte", ">");
        op4 = new Opcao("Selecionar", "=palavra_a_selecionar");
        op5 = new Opcao("Selecionar fuso atual", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuZoneIdNavigator = new Menu(linhas, "Navegador de ZoneIds");
        menusScheduleTxt.addMenu(8, menuZoneIdNavigator);

        return menusScheduleTxt;
    }

    public Menu getMenu(int id) {
        return menuScheduleViewTxt.getMenu(id);
    }
}
