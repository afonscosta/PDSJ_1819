package View.Class;

import View.Interface.InterfCalcDateTimeView;
import Utilities.Menu;
import Utilities.Menus;
import Utilities.Opcao;

import java.util.Arrays;
import java.util.List;

public class CalcDateTimeView implements InterfCalcDateTimeView {

    private Menus menuMainViewTxt;

    public CalcDateTimeView() {
        menuMainViewTxt = initView();
    }

    private Menus initView() {
        Menus menuMainTxt = new Menus();
        Opcao op1,op3,op4;

        //------------------------
        // Menu principal
        //------------------------
        op1 = new Opcao("Calculadora DateTime Local ... ", "L");
        op3 = new Opcao("Agenda de Reunioes............ ","A");
        op4 = new Opcao("Sair da Aplicação >>>>>>>>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1, op3,op4);
        Menu menuPrincipal = new Menu(linhas, "Menu Principal");
        menuMainTxt.addMenu(0, menuPrincipal);
        return menuMainTxt;
    }

    public Menu getMenu(int id) {
        return menuMainViewTxt.getMenu(id);
    }
}
