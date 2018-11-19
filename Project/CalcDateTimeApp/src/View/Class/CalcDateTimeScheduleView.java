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
        Opcao op1, op2, op3, op4, op5;

        //------------------------
        // Menu CalcDateTimeSchedule
        //------------------------
        op1 = new Opcao("Inserir reuniao", "I");
        op2 = new Opcao("Visualizar reunioes agendadas", "V");
        op3 = new Opcao("Voltar ao Menu Principal", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3);
        Menu menuDateTimeSchedule = new Menu(linhas, "Agenda Reunioes");
        menusScheduleTxt.addMenu(0, menuDateTimeSchedule);

        //------------------------
        // Menu Busy slots navigator
        //------------------------
        op1 = new Opcao("Modo", "/<modo>");
        op2 = new Opcao("Pagina anterior", "-");
        op3 = new Opcao("Pagina seguinte", "+");
        op4 = new Opcao("Selecionar", "=<id reuniao>");
        op5 = new Opcao("Voltar ao Menu Agenda", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuBusySlotsNavigator = new Menu(linhas, "Reunioes Agendadas");
        menusScheduleTxt.addMenu(1, menuBusySlotsNavigator);

        //------------------------
        // Menu select busy slots
        //------------------------
        op1 = new Opcao("Alterar", "A");
        op2 = new Opcao("Remover", "R");
        op3 = new Opcao("Detalhes", "D");
        op4 = new Opcao("Voltar ao Menu reunioes agendadas", "S");
        linhas = Arrays.asList(op1, op2,op3,op4);
        Menu menuSelectBusySlot = new Menu(linhas, "Reuniao");
        menusScheduleTxt.addMenu(2, menuSelectBusySlot);

        //------------------------
        // Menu date choice
        //------------------------
        op1 = new Opcao("Usar a data da calculadora data/tempo local", "L");
        op2 = new Opcao("Usar a data da calculadora data/tempo com fusos", "Z");
        op3 = new Opcao("Inserir data manualmente", "M");
        op4 = new Opcao("Voltar ao Menu Agenda", "S");
        linhas = Arrays.asList(op1, op2,op3,op4);
        Menu menuDateChoice = new Menu(linhas, "Data da reuniao a inserir");
        menusScheduleTxt.addMenu(3, menuDateChoice);

        //------------------------
        // Menu alter slot
        //------------------------
        op1 = new Opcao("Alterar data", "Data");
        op2 = new Opcao("Alterar duracao", "D");
        op3 = new Opcao("Alterar local", "L");
        op4 = new Opcao("Alterar descricao", "Desc");
        op5 = new Opcao("Voltar ao Menu reuniao", "S");
        linhas = Arrays.asList(op1, op2,op3,op4,op5);
        Menu menuAlterSlot = new Menu(linhas, "Alterar reuniao");
        menusScheduleTxt.addMenu(4, menuAlterSlot);

        //------------------------
        // Menu alter data
        //------------------------

        op1 = new Opcao("Manipular dia", "D");
        op2 = new Opcao("Manipular semana", "Sem");
        op3 = new Opcao("Manipular mes", "M");
        op4 = new Opcao("Manipular ano", "A");
        op5 = new Opcao("Voltar ao Menu reuniao", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuShiftDateTime = new Menu(linhas, "Alterar Data");
        menusScheduleTxt.addMenu(5, menuShiftDateTime);

        //------------------------
        // Menu details slot
        //------------------------

        op1 = new Opcao("Voltar ao Menu reuniao", "S");
        linhas = Arrays.asList(op1);
        Menu menuDetailsSlot = new Menu(linhas, "Alterar Data");
        menusScheduleTxt.addMenu(6, menuDetailsSlot);

        return menusScheduleTxt;

    }

    public Menu getMenu(int id) {
        return menuScheduleViewTxt.getMenu(id);
    }
}
