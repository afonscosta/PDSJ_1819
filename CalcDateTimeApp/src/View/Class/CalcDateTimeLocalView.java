package View.Class;

import View.Interface.InterfCalcDateTimeView;
import Utilities.Menu;
import Utilities.Menus;
import Utilities.Opcao;

import java.util.Arrays;
import java.util.List;

public class CalcDateTimeLocalView implements InterfCalcDateTimeView {
    private Menus menuLocalViewTxt;

    public static CalcDateTimeLocalView of() {
        return new CalcDateTimeLocalView();
    }

    private CalcDateTimeLocalView() {
        menuLocalViewTxt = initView();
    }

    private Menus initView() {
        Menus menusLocalTxt = new Menus();
        Opcao op1, op2, op3, op4, op5, op6, op7, op8, op9, op10;


        //------------------------
        // CalcDateTimeLocal
        //------------------------
        op1 = new Opcao("Definir data", "C");
        op2 = new Opcao("Reset para data atual", "R");
        op3 = new Opcao("Aritmetica de datas", "A");
        op4 = new Opcao("Aritmetica de dias uteis", "AU");
        op5 = new Opcao("Diferenca entre datas", "D");
        op6 = new Opcao("Diferenca entre datas (dias uteis)", "DU");
        op7 = new Opcao("Obter um dia", "O");
        op8 = new Opcao("Ajuda", "?");
        op9 = new Opcao("Voltar ao Menu Principal", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5, op6, op7, op8, op9);
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
        op9 = new Opcao("Guardar data e voltar a Calc. Local", "G");
        op10 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6, op7, op8, op9, op10);
        Menu menuShiftDateTime = new Menu(linhas, "Aritmetica de Datas");
        menusLocalTxt.addMenu(2, menuShiftDateTime);

        //------------------------
        // Menu Shift WorkDays DateTime
        //------------------------
        op1 = new Opcao("Aritmetica de dias uteis", "A");
        op2 = new Opcao("Guardar data e voltar a Calc. Local", "G");
        op3 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3);
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

        //------------------------
        // Menu Obter DateTime
        //------------------------
        op1 = new Opcao("Obter um dia", "O");
        op2 = new Opcao("Ajuda", "?");
        op3 = new Opcao("Guardar data e voltar a Calc. Local", "G");
        op4 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3, op4);
        Menu menuObterDateTime = new Menu(linhas, "Obter um dia");
        menusLocalTxt.addMenu(5, menuObterDateTime);

        return menusLocalTxt;
    }

    @Override
    public Menu getMenu(int id) {
        return menuLocalViewTxt.getMenu(id);
    }

    @Override
    public Menu getDynamicMenu(int id, String statusMessage, String errorMessage, List des) {
        Menu menu = menuLocalViewTxt.getMenu(id);
        menu.addDescToTitle(des);
        menu.addStatusMessage(statusMessage);
        menu.addErrorMessage(errorMessage);
        return menu;
    }

    @Override
    public Menu getDynamicMenu(int id, String statusMessage, String errorMessage) {
        Menu menu = menuLocalViewTxt.getMenu(id);
        menu.addStatusMessage(statusMessage);
        menu.addErrorMessage(errorMessage);
        return menu;
    }
}
