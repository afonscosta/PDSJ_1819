package View.Class;

import View.Interface.InterfCalcDateTimeLocalView;
import Utilities.Menu;
import Utilities.Menus;
import Utilities.Opcao;

import java.util.Arrays;
import java.util.List;

public class CalcDateTimeLocalView implements InterfCalcDateTimeLocalView {
    private Menus menuLocalViewTxt;

    public CalcDateTimeLocalView() {
        menuLocalViewTxt = initView();
    }

    private Menus initView() {
        Menus menusLocalTxt = new Menus();
        Opcao op1, op2, op3, op4, op5;

        //------------------------
        // CalcDateTimeLocal
        //------------------------
        op1 = new Opcao("Alterar data ......................... ", "A");
        op2 = new Opcao("Alterar data (dias uteis)............. ", "AU");
        op3 = new Opcao("Diferenca entre datas ................ ", "D");
        op4 = new Opcao("Diferenca entre datas (dias uteis) ... ", "DU");
        op5 = new Opcao("Sair da Aplicacao >>>>>>>>>>>>>>>>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuCalcDateTime = new Menu(linhas, "   Data: ");
        menusLocalTxt.addMenu(0, menuCalcDateTime);

        //------------------------
        // Menu Shift DateTime
        //------------------------
        op1 = new Opcao("Deslocar dia ........ ", "Dia");
        op2 = new Opcao("Deslocar semana ..... ", "Sem");
        op3 = new Opcao("Deslocar mes ........ ", "Mes");
        op4 = new Opcao("Deslocar ano ........ ", "Ano");
        op5 = new Opcao("Sair da Aplicacao >>> ", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuShiftDateTime = new Menu(linhas, "   Data: ");
        menusLocalTxt.addMenu(1, menuShiftDateTime);

        //------------------------
        // Menu Shift WorkDays DateTime
        //------------------------
        op1 = new Opcao("Deslocar dia ........ ", "Dia");
        op2 = new Opcao("Sair da Aplicacao >>> ", "S");
        linhas = Arrays.asList(op1, op2);
        Menu menuShiftWorkDaysDateTime = new Menu(linhas, "   Data: ");
        menusLocalTxt.addMenu(2, menuShiftWorkDaysDateTime);

        //------------------------
        // Menu Diff DateTime
        //------------------------
        op1 = new Opcao("Alterar data de inicio ... ", "I");
        op2 = new Opcao("Inserir data de fim ...... ", "F");
        op3 = new Opcao("Sair da Aplicacao >>>>>>>> ", "S");
        linhas = Arrays.asList(op1, op2, op3);
        Menu menuDiffDateTime = new Menu(linhas, "   Data de inicio: ");
        menusLocalTxt.addMenu(3, menuDiffDateTime);

        return menusLocalTxt;
    }

    public Menu getMenu(int id) {
        return menuLocalViewTxt.getMenu(id);
    }
}
