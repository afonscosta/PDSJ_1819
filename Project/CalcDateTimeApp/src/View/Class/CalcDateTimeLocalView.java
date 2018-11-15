package View.Class;

import View.Interface.InterfCalcDateTimeLocalView;
import View.Menu;
import View.Menus;
import View.Opcao;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CalcDateTimeLocalView implements InterfCalcDateTimeLocalView {
    private Menus menuLocalViewTxt = new Menus();

    public CalcDateTimeLocalView() {
        menuLocalViewTxt = initView();
    }

    private Menus initView() {
        Menus menusLocalTxt = new Menus();
        Opcao op1, op2, op3, op4, op5;

        //------------------------
        // CalcDateTimeLocal
        //------------------------
        op1 = new Opcao("Alterar data ........... ", "A");
        op2 = new Opcao("Sair da Aplicação >>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2);
        Menu menuCalcDateTime = new Menu(linhas, "   Data: ");
        menusLocalTxt.addMenu(0, menuCalcDateTime);

        //------------------------
        // Menu Shift DateTime
        //------------------------
        op1 = new Opcao("Deslocar dia ............. ", "Dia");
        op2 = new Opcao("Deslocar semana .......... ", "Sem");
        op3 = new Opcao("Deslocar mês ............. ", "Mes");
        op4 = new Opcao("Deslocar ano ............. ", "Ano");
        op5 = new Opcao("Sair da Aplicação >> ", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuShiftDateTime = new Menu(linhas, "   Data: ");
        menusLocalTxt.addMenu(1, menuShiftDateTime);

        return menusLocalTxt;
    }

    public Menu getMenu(int id) {
        return menuLocalViewTxt.getMenu(id);
    }
}
