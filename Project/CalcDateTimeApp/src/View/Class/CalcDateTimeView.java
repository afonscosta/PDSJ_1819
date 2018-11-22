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
        Opcao op1, op2, op3, op4, op5,op6,op7;

        //------------------------
        // Menu Principal
        //------------------------
        op1 = new Opcao("Calculadora DateTime Local", "L");
        op2 = new Opcao("Calculadora DateTime com Zonas", "Z");
        op3 = new Opcao("Agenda de Reunioes","A");
        op4 = new Opcao("Ajuda", "?");
        op5 = new Opcao("Sair da Aplicacao", "S");
        List<Opcao> linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuPrincipal = new Menu(linhas, "Menu Principal");
        menuMainTxt.addMenu(0, menuPrincipal);


        //------------------------
        // Menu Help
        //------------------------
        op1 = new Opcao("Sair", "S");
        linhas = Arrays.asList(op1);
        Menu menuHelp = new Menu(linhas, "Menu Ajuda");
        menuMainTxt.addMenu(1, menuHelp);

        //------------------------
        // Menu para a escolha do formato a apresentar das datas LocalDateTime
        //------------------------
        op1 = new Opcao("dd-MM-yyy HH:mm", "1");
        op2 = new Opcao("dd-MM-yyy HH:mm:ss", "2");
        op3 = new Opcao("dd-MM-yyy HH:mm:ss:nn","3");
        op4 = new Opcao("Voltar ao menu configuracoes","S");
        linhas = Arrays.asList(op1, op2, op3, op4);
        Menu menuLocalFormatter = new Menu(linhas, "Escolha do formato da data local");
        menuMainTxt.addMenu(2, menuLocalFormatter);

        //------------------------
        // Menu para a escolha do formato a apresentar das datas ZonedDateTime
        //------------------------
        op1 = new Opcao("dd-MM-yyy HH:mm VV", "1");
        op2 = new Opcao("dd-MM-yyy HH:mm:ss VV", "2");
        op3 = new Opcao("dd-MM-yyy HH:mm:ss O", "3");
        op4 = new Opcao("dd-MM-yyy HH:mm VV O", "4");
        op5 = new Opcao("Voltar ao menu configuracoes","S");
        linhas = Arrays.asList(op1, op2, op3, op4, op5);
        Menu menuZonedFormatter = new Menu(linhas, "Escolha do formato da data com fusos");
        menuMainTxt.addMenu(2, menuZonedFormatter);

        return menuMainTxt;
    }

    public Menu getMenu(int id) {
        return menuMainViewTxt.getMenu(id);
    }
}
