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
        Opcao op1, op2, op3, op4, op5, op6;

        //------------------------
        // CalcDateTimeLocal
        //------------------------
        op1 = new Opcao("Somar a uma data ........... ", "A");
        op2 = new Opcao("Sair da Aplicação >>>>>>>>>> ", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2);
        Menu menuCalcDateTime = new Menu(linhas, "   Data:" + LocalDateTime.now().toString());
        menusLocalTxt.addMenu(0, menuCalcDateTime);

        //------------------------
        // Menu Shift DateTime
        //------------------------
        op1 = new Opcao("um dia ............. ", "Dia");
        op2 = new Opcao("uma semana ......... ", "Sem");
        op3 = new Opcao("um mês ............. ", "Mes");
        op4 = new Opcao("um ano ............. ", "Ano");
        op5 = new Opcao("Calcular ........... ", "C");
        op6 = new Opcao("Sair da Aplicação >> ", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6);
        Menu menuShiftDateTime = new Menu(linhas, "   Data:" + LocalDateTime.now().toString());
        menusLocalTxt.addMenu(1, menuShiftDateTime);

        return menusLocalTxt;
    }

    public Menu getMenu(int id) {
        return menuLocalViewTxt.getMenu(id);
    }
}
