package View.Class;

import View.Interface.InterfCalcDateTimeView;
import View.Menu;
import View.Menus;
import View.Opcao;

import java.util.Arrays;
import java.util.List;

public class CalcDateTimeView implements InterfCalcDateTimeView {

    private Menus menuMainViewTxt = new Menus();

    public CalcDateTimeView() {
        menuMainViewTxt = initView();
    }

    private Menus initView() {
        Menus menuMainTxt = new Menus();
        Opcao op1, op2;

        //------------------------
        // Menu principal
        //------------------------
        op1 = new Opcao("Calculadora DateTime Local ........... ", "L");
        op2 = new Opcao("Sair da Aplicação >>>>>>>>>>>>>>>>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2);
        Menu menuPrincipal = new Menu(linhas, "   Menu Principal");
        menuMainTxt.addMenu(0, menuPrincipal);
        return menuMainTxt;
    }

    public Menu getMenu(int id) {
        return menuMainViewTxt.getMenu(id);
    }
}
