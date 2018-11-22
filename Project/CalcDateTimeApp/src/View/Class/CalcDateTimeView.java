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
        Opcao op1, op2, op3, op4, op5, op6;

        //------------------------
        // Menu Principal
        //------------------------
        op1 = new Opcao("Calculadora DateTime Local", "L");
        op2 = new Opcao("Calculadora DateTime com Zonas", "Z");
        op3 = new Opcao("Agenda de Reunioes","A");
        op4 = new Opcao("Configuracoes","C");
        op5 = new Opcao("Ajuda", "?");
        op6 = new Opcao("Sair da Aplicacao", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5, op6);
        Menu menuPrincipal = new Menu(linhas, "Menu Principal");
        menuMainTxt.addMenu(0, menuPrincipal);

        //------------------------
        // Menu Configurações
        //------------------------
        op1 = new Opcao("Definir o formato das datas locais", "FL");
        op2 = new Opcao("Definir o formato das datas com fusos", "FF");
        op3 = new Opcao("Definir o fuso local", "L");
        op4 = new Opcao("Definir horario da agenda","H");
        op5 = new Opcao("Ajuda", "?");
        op6 = new Opcao("Sair da Aplicacao", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6);
        Menu menuConfig = new Menu(linhas, "Menu de Configuracoes");
        menuMainTxt.addMenu(1, menuConfig);

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
        menuMainTxt.addMenu(2, menuZoneIdNavigator);

        //------------------------
        // Menu Help
        //------------------------
        op1 = new Opcao("Sair", "S");
        linhas = Arrays.asList(op1);
        Menu menuHelp = new Menu(linhas, "Menu Ajuda");
        menuMainTxt.addMenu(3, menuHelp);

        return menuMainTxt;
    }

    public Menu getMenu(int id) {
        return menuMainViewTxt.getMenu(id);
    }
}
