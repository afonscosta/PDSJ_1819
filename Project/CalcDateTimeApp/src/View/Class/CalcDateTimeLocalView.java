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
        Opcao op1, op2, op3, op4, op5, op6, op7;

        //------------------------
        // CalcDateTimeLocal
        //------------------------
        op1 = new Opcao("Alterar data", "A");
        op2 = new Opcao("Manipular data", "M");
        op3 = new Opcao("Manipular data (dias uteis)", "MU");
        op4 = new Opcao("Diferenca entre datas", "D");
        op5 = new Opcao("Diferenca entre datas (dias uteis)", "DU");
        op6 = new Opcao("Obter um mes, semana ou dia", "O");
        op7 = new Opcao("Voltar ao Menu Principal", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5, op6, op7);
        Menu menuCalcDateTime = new Menu(linhas, "Menu Calculadora Local");
        menusLocalTxt.addMenu(0, menuCalcDateTime);

        //------------------------
        // Menu Shift DateTime
        //------------------------
        op1 = new Opcao("Manipular dia", "DIA");
        op2 = new Opcao("Manipular semana", "SEM");
        op3 = new Opcao("Manipular mes", "MES");
        op4 = new Opcao("Manipular ano", "ANO");
        op5 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuShiftDateTime = new Menu(linhas, "Alterar Data");
        menusLocalTxt.addMenu(1, menuShiftDateTime);

        //------------------------
        // Menu Shift WorkDays DateTime
        //------------------------
        op1 = new Opcao("Manipular dia", "M");
        op2 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2);
        Menu menuShiftWorkDaysDateTime = new Menu(linhas, "Alterar Data - Dias Uteis");
        menusLocalTxt.addMenu(2, menuShiftWorkDaysDateTime);

        //------------------------
        // Menu Diff DateTime
        //------------------------
        op1 = new Opcao("Alterar data de inicio", "I");
        op2 = new Opcao("Inserir data de fim", "F");
        op3 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3);
        Menu menuDiffDateTime = new Menu(linhas, "Diferenca entre Datas");
        menusLocalTxt.addMenu(3, menuDiffDateTime);

        return menusLocalTxt;
    }

    public Menu getMenu(int id) {
        return menuLocalViewTxt.getMenu(id);
    }
}
