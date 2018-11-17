package View.Class;

import Utilities.Menu;
import Utilities.Menus;
import Utilities.Opcao;
import View.Interface.InterfCalcDateTimeZoneView;

import java.util.Arrays;
import java.util.List;

public class CalcDateTimeZoneView implements InterfCalcDateTimeZoneView {
    private Menus menuZoneViewTxt;

    public CalcDateTimeZoneView() {
        menuZoneViewTxt = initView();
    }

    private Menus initView() {
        Menus menusZoneTxt = new Menus();
        Opcao op1, op2, op3, op4, op5;

        //------------------------
        // CaldDateTimeZone
        //------------------------
        op1 = new Opcao("Deslocar data ............. ", "D");
        op2 = new Opcao("Converter para fuso ....... ", "C");
        op3 = new Opcao("Sair da aplicacao >>>>>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1,op2,op3);
        Menu menuCalcDateTimeZone = new Menu(linhas,"   Data: ");
        menusZoneTxt.addMenu(0,menuCalcDateTimeZone);

        //------------------------
        // Menu Shift ZoneDateTime
        //------------------------
        op1 = new Opcao("Deslocar dia ........... ", "Dia");
        op2 = new Opcao("Deslocar semana ........ ", "Sem");
        op3 = new Opcao("Deslocar mes ........... ", "Mes");
        op4 = new Opcao("Deslocar ano ........... ", "Ano");
        op5 = new Opcao("Voltar a Calc. Local >>> ", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuShiftDateTime = new Menu(linhas, "   Data: ");
        menusZoneTxt.addMenu(1, menuShiftDateTime);

        return menusZoneTxt;
    }


    public Menu getMenu(int id) {
        return menuZoneViewTxt.getMenu(id);
    }
}
