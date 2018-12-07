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
        op3 = new Opcao("Agenda de Eventos","A");
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
        op3 = new Opcao("Definir o fuso", "F");
        op4 = new Opcao("Restricoes no agendamento de eventos","R");
        op5 = new Opcao("Ajuda", "?");
        op6 = new Opcao("Voltar ao Menu Principal", "S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5, op6);
        Menu menuConfig = new Menu(linhas, "Menu de Configuracoes");
        menuMainTxt.addMenu(1, menuConfig);

        //------------------------
        // Menu Navegador de ZoneIds
        //------------------------
        op1 = new Opcao("Pagina anterior", "<");
        op2 = new Opcao("Procurar", "/palavra_a_procurar");
        op3 = new Opcao("Pagina seguinte", ">");
        op4 = new Opcao("Selecionar", "=fuso_pretendido");
        op5 = new Opcao("Selecionar fuso atual", "S");
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

        //------------------------
        // Menu Configurações
        //------------------------
        op1 = new Opcao("Escolher pre-definidos", "P");
        op2 = new Opcao("Manualmente", "M");
        op3 = new Opcao("Ajuda","?");
        op4 = new Opcao("Voltar ao Menu Configuracoes","S");
        linhas = Arrays.asList(op1, op2,op3,op4);
        Menu menuAlterFormatter = new Menu(linhas, "Alterar formato");
        menuMainTxt.addMenu(4, menuAlterFormatter);

        //------------------------
        // Menu para a escolha do formato a apresentar das datas LocalDateTime
        //------------------------
        op1 = new Opcao("dd-MM-yyy HH:mm", "1");
        op2 = new Opcao("dd-MM-yyy HH:mm:ss", "2");
        op3 = new Opcao("dd-MM-yyy HH:mm:ss:nn","3");
        op4 = new Opcao("Voltar ao Menu Configuracoes","S");
        linhas = Arrays.asList(op1, op2, op3, op4);
        Menu menuLocalFormatter = new Menu(linhas, "Escolha do formato da data local");
        menuMainTxt.addMenu(5, menuLocalFormatter);

        //------------------------
        // Menu para a escolha do formato a apresentar das datas ZonedDateTime
        //------------------------
        op1 = new Opcao("dd-MM-yyy HH:mm VV", "1");
        op2 = new Opcao("dd-MM-yyy HH:mm:ss VV", "2");
        op3 = new Opcao("dd-MM-yyy HH:mm:ss O", "3");
        op4 = new Opcao("dd-MM-yyy HH:mm VV O", "4");
        op5 = new Opcao("Voltar ao Menu Configuracoes","S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuZonedFormatter = new Menu(linhas, "Escolha do formato da data com fusos");
        menuMainTxt.addMenu(6, menuZonedFormatter);

        //------------------------
        // Menu restricoes
        //------------------------
        op1 = new Opcao("Adicionar restricao especifica", "E");
        op2 = new Opcao("Adicionar restricao global sobre a agenda", "G");
        op3 = new Opcao("Visualizar restricoes definidas","V");
        op4 = new Opcao("Voltar ao Menu Configuracoes", "S");
        linhas = Arrays.asList(op1,op2,op3,op4);
        Menu menuRetrictSlots = new Menu(linhas, "Restricao");
        menuMainTxt.addMenu(7, menuRetrictSlots);

        //------------------------
        // Menu adicionar uma restricao
        //------------------------
        op1 = new Opcao("Restricao diaria", "Dia");
        op2 = new Opcao("Restricao semanal", "Sem");
        op3 = new Opcao("Voltar ao Menu Configuracoes", "S");
        linhas = Arrays.asList(op1,op2,op3);
        Menu menuGlobalRetrictSlots = new Menu(linhas, "Mascara da restricao");
        menuMainTxt.addMenu(8, menuGlobalRetrictSlots);

        //------------------------
        // Menu visualizar uma restricao
        //------------------------
        op1 = new Opcao("Pagina anterior", "<");
        op2 = new Opcao("Pagina seguinte", ">");
        op3 = new Opcao("Selecionar", "=id_restricao");
        op4 = new Opcao("Voltar ao Menu Restricoes", "S");
        linhas = Arrays.asList(op1,op2,op3,op4);
        Menu menuRestrictNavigator = new Menu(linhas, "Restricoes definidas");
        menuMainTxt.addMenu(9, menuRestrictNavigator);


        //------------------------
        // Menu remover uma restricao
        //------------------------
        op1 = new Opcao("Remover", "R");
        op2 = new Opcao("Voltar ao Menu Restricoes", "S");
        linhas = Arrays.asList(op1,op2);
        Menu menuDeleteRestrict = new Menu(linhas, "Remover restricao");
        menuMainTxt.addMenu(10, menuDeleteRestrict);

        return menuMainTxt;
    }

    @Override
    public Menu getMenu(int id) {
        return menuMainViewTxt.getMenu(id);
    }

    @Override
    public Menu getDynamicMenu(int id, String statusMessage, String errorMessage, List des) {
        Menu menu = menuMainViewTxt.getMenu(id);
        menu.addDescToTitle(des);
        menu.addStatusMessage(statusMessage);
        menu.addErrorMessage(errorMessage);
        return menu;
    }

    @Override
    public Menu getDynamicMenu(int id, String statusMessage, String errorMessage){
        Menu menu = menuMainViewTxt.getMenu(id);
        menu.addStatusMessage(statusMessage);
        menu.addErrorMessage(errorMessage);
        return menu;
    }
}
