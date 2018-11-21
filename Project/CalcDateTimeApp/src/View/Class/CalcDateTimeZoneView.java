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
        Opcao op1, op2, op3, op4, op5, op6, op7, op8, op9;

        //------------------------
        // CaldDateTimeZone
        //------------------------
        op1 = new Opcao("Alterar data", "A");
        op2 = new Opcao("Manipular data", "M");
        op3 = new Opcao("Manipular data (dias uteis)", "MU");
        op4 = new Opcao("Diferenca entre datas", "D");
        op5 = new Opcao("Diferenca entre datas (dias uteis)", "DU");
        op6 = new Opcao("Tempo atual numa região", "T");
        op7 = new Opcao("Converter para fuso", "C");
        op8 = new Opcao("Diferenca entre dois fusos", "F");
        op9 = new Opcao("Sair da aplicacao", "S");
        List<Opcao> linhas = Arrays.asList(op1,op2,op3,op4,op5,op6,op7,op8,op9);
        Menu menuCalcDateTimeZone = new Menu(linhas,"Menu Calculadora de Zonas");
        menusZoneTxt.addMenu(0,menuCalcDateTimeZone);

        //------------------------
        // Menu Shift ZoneDateTime
        //------------------------
        op1 = new Opcao("Manipular dia", "DIA");
        op2 = new Opcao("Manipular semana", "SEM");
        op3 = new Opcao("Manipular mes", "MES");
        op4 = new Opcao("Manipular ano", "ANO");
        op5 = new Opcao("Voltar a Calc. de Zonas", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuShiftDateTime = new Menu(linhas, "Alterar Data");
        menusZoneTxt.addMenu(1, menuShiftDateTime);

        //------------------------
        // Menu Shift WorkDays DateTimeZone
        //------------------------
        op1 = new Opcao("Manipular dia", "M");
        op2 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2);
        Menu menuShiftWorkDaysDateTime = new Menu(linhas, "Alterar Data - Dias Uteis");
        menusZoneTxt.addMenu(2, menuShiftWorkDaysDateTime);

        //------------------------
        // Menu Diff DateTimeZone
        //------------------------
        op1 = new Opcao("Alterar data de inicio", "I");
        op2 = new Opcao("Inserir data de fim", "F");
        op3 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2, op3);
        Menu menuDiffDateTime = new Menu(linhas, "Diferenca entre Datas");
        menusZoneTxt.addMenu(3, menuDiffDateTime);

        //------------------------
        // Menu Navegador de ZoneIds
        //------------------------
        op1 = new Opcao("Pagina anterior", "<");
        op2 = new Opcao("Procurar", "/<palavra>");
        op3 = new Opcao("Pagina seguinte", ">");
        op4 = new Opcao("Selecionar", "=<palavra>");
        op5 = new Opcao("Voltar para Calc. de Zonas", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuZoneIdNavigator = new Menu(linhas, "Navegador de ZoneIds");
        menusZoneTxt.addMenu(4, menuZoneIdNavigator);
        return menusZoneTxt;
    }


    public Menu getMenu(int id) {
        return menuZoneViewTxt.getMenu(id);
    }
}
