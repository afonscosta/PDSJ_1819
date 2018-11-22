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
        Opcao op1, op2, op3, op4, op5, op6, op7, op8, op9;


        //------------------------
        // CalcDateTimeLocal
        //------------------------
        op1 = new Opcao("Definir data", "C");
        op2 = new Opcao("Aritmetica de datas", "A");
        op3 = new Opcao("Aritmetica de dias uteis", "AU");
        op4 = new Opcao("Diferenca entre datas", "D");
        op5 = new Opcao("Diferenca entre datas (dias uteis)", "DU");
        op6 = new Opcao("Obter um mes, semana ou dia", "O");
        op7 = new Opcao("Ajuda", "?");
        op8 = new Opcao("Voltar ao Menu Principal", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5, op6, op7, op8);
        Menu menuCalcDateTime = new Menu(linhas, "Menu Calculadora Local");
        menusLocalTxt.addMenu(0, menuCalcDateTime);

        //------------------------
        // Menu Help
        //------------------------
        op1 = new Opcao("Sair", "S");
        linhas = Arrays.asList(op1);
        Menu menuHelp = new Menu(linhas, "Menu Ajuda");
        menusLocalTxt.addMenu(1, menuHelp);

        //------------------------
        // Menu Shift DateTime
        //------------------------
        op1 = new Opcao("Aritmetica de anos", "ANO");
        op2 = new Opcao("Aritmetica de meses", "MES");
        op3 = new Opcao("Aritmetica de semanas", "SEM");
        op4 = new Opcao("Aritmetica de dias", "DIA");
        op5 = new Opcao("Aritmetica de horas", "HOR");
        op6 = new Opcao("Aritmetica de minutos", "MIN");
        op7 = new Opcao("Aritmetica de segundos", "SEG");
        op8 = new Opcao("Aritmetica de nanosegundos", "NAN");
        op9 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6, op7, op8, op9);
        Menu menuShiftDateTime = new Menu(linhas, "Aritmetica de Datas");
        menusLocalTxt.addMenu(2, menuShiftDateTime);

        //------------------------
        // Menu Shift WorkDays DateTime
        //------------------------
        op1 = new Opcao("Aritmetica de dias uteis", "A");
        op2 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2);
        Menu menuShiftWorkDaysDateTime = new Menu(linhas, "Aritmetica de Dias Uteis");
        menusLocalTxt.addMenu(3, menuShiftWorkDaysDateTime);

        //------------------------
        // Menu Diff DateTime
        //------------------------
        op1 = new Opcao("Alterar data de inicio", "I");
        op2 = new Opcao("Inserir data de fim", "F");
        op3 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3);
        Menu menuDiffDateTime = new Menu(linhas, "Diferenca entre Datas");
        menusLocalTxt.addMenu(4, menuDiffDateTime);

        return menusLocalTxt;
    }

    public Menu getMenu(int id) {
        return menuLocalViewTxt.getMenu(id);
    }
}
