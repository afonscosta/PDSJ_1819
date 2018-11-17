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
        op1 = new Opcao("Inserir reuniao....................... ", "I");
        op2 = new Opcao("Remover reuniao....................... ", "R");
        op3 = new Opcao("Alterar reuniao...... ................ ", "A");
        op4 = new Opcao("Visualizar reunioes agendadas..... ... ", "V");
        op5 = new Opcao("Voltar ao Menu Principal >>>>>>>>>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuDateTimeSchedule = new Menu(linhas, "   Agenda Reunioes ");
        menusScheduleTxt.addMenu(0, menuDateTimeSchedule);

        //------------------------
        // Menu Busy slots
        //------------------------
        op1 = new Opcao("Próxima reuniao ......... ", "P");
        op2 = new Opcao("Reunioes diária ......... ", "D");
        op3 = new Opcao("Reunioes semanais ....... ", "Sem");
        op4 = new Opcao("Reunioes mensais ........ ", "M");
        op5 = new Opcao("Voltar ao Menu Agenda >>> ", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuBusySlots = new Menu(linhas, "   Visualizar ");
        menusScheduleTxt.addMenu(1, menuBusySlots);

        //------------------------
        // Menu Shift busy slots
        //------------------------
        op1 = new Opcao("Próxima ............................... ", "P");
        op2 = new Opcao("Anterior .............................. ", "A");
        op3 = new Opcao("Ver detalhes da reuniao ............... ", "D");
        op4 = new Opcao("Voltar ao Menu reunioes agendadas >>>>> ", "S");
        linhas = Arrays.asList(op1, op2,op3,op4);
        Menu menuShiftBusySlots = new Menu(linhas, "   Reunioes ");
        menusScheduleTxt.addMenu(2, menuShiftBusySlots);

        //------------------------
        // Menu date choice
        //------------------------
        op1 = new Opcao("Usar a data da calculadora data/tempo local ......... ", "L");
        op2 = new Opcao("Usar a data da calculadora data/tempo com fusos ..... ", "Z");
        op3 = new Opcao("Inserir data manualmente ............................ ", "M");
        op4 = new Opcao("Voltar ao Menu Agenda >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ", "S");
        linhas = Arrays.asList(op1, op2,op3,op4);
        Menu menuDateChoice = new Menu(linhas, "   Data da reuniao a inserir ");
        menusScheduleTxt.addMenu(3, menuDateChoice);

        return menusScheduleTxt;
    }

    public Menu getMenu(int id) {
        return menuScheduleViewTxt.getMenu(id);
    }
}
