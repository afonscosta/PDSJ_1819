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
        op1 = new Opcao("Converter para fuso", "C");
        op2 = new Opcao("Sair da aplicacao", "S");
        List<Opcao> linhas = Arrays.asList(op1,op2);
        Menu menuCalcDateTimeZone = new Menu(linhas,"Menu Calculadora Zona");
        menusZoneTxt.addMenu(0,menuCalcDateTimeZone);

        return menusZoneTxt;
    }


    public Menu getMenu(int id) {
        return menuZoneViewTxt.getMenu(id);
    }
}
