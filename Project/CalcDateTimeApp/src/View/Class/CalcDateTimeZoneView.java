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
        Opcao op1, op2, op3, op4, op5, op6, op7, op8, op9, op10, op11;

        //------------------------
        // CaldDateTimeZone
        //------------------------
        op1 = new Opcao("Definir data", "C");
        op2 = new Opcao("Reset para data atual", "R");
        op3 = new Opcao("Aritmetica de datas", "A");
        op4 = new Opcao("Aritmetica de dias uteis", "AU");
        op5 = new Opcao("Diferenca entre datas", "D");
        op6 = new Opcao("Diferenca entre datas (dias uteis)", "DU");
        op7 = new Opcao("Converter para fuso", "F");
        op8 = new Opcao("Diferenca entre dois fusos", "DF");
        op9 = new Opcao("Ajuda", "?");
        op10 = new Opcao("Sair da aplicacao", "S");
        List<Opcao> linhas = Arrays.asList(op1,op2,op3,op4,op5,op6,op7,op8,op9,op10);
        Menu menuCalcDateTimeZone = new Menu(linhas,"Menu Calculadora de Zonas");
        menusZoneTxt.addMenu(0,menuCalcDateTimeZone);

        //------------------------
        // Menu Shift ZoneDateTime
        //------------------------
        op1 = new Opcao("Aritmetica de anos", "ANO");
        op2 = new Opcao("Aritmetica de meses", "MES");
        op3 = new Opcao("Aritmetica de semanas", "SEM");
        op4 = new Opcao("Aritmetica de dias", "DIA");
        op5 = new Opcao("Aritmetica de horas", "HOR");
        op6 = new Opcao("Aritmetica de minutos", "MIN");
        op7 = new Opcao("Aritmetica de segundos", "SEG");
        op8 = new Opcao("Aritmetica de nanosegundos", "NAN");
        op9 = new Opcao("Voltar a Calc. de Zonas", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6, op7, op8, op9);
        Menu menuShiftDateTime = new Menu(linhas, "Aritmetica de Datas");
        menusZoneTxt.addMenu(1, menuShiftDateTime);

        //------------------------
        // Menu Shift WorkDays DateTimeZone
        //------------------------
        op1 = new Opcao("Aritmetica de dias uteis", "M");
        op2 = new Opcao("Voltar a Calc. Local", "S");
        linhas = Arrays.asList(op1, op2);
        Menu menuShiftWorkDaysDateTime = new Menu(linhas, "Aritmetica de Dias Uteis");
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
        op3 = new Opcao("Ver lista completa", "/");
        op4 = new Opcao("Pagina seguinte", ">");
        op5 = new Opcao("Selecionar", "=<palavra>");
        op6 = new Opcao("Selecionar zoneID atual", "=");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6);
        Menu menuZoneIdNavigator = new Menu(linhas, "Navegador de ZoneIds");
        menusZoneTxt.addMenu(4, menuZoneIdNavigator);

        op1 = new Opcao("Sair", "S");
        linhas = Arrays.asList(op1);
        Menu menuAjuda = new Menu(linhas, "Menu ajuda");
        menusZoneTxt.addMenu(5, menuAjuda);

        return menusZoneTxt;
    }


    public Menu getMenu(int id) {
        return menuZoneViewTxt.getMenu(id);
    }
}
