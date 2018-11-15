package Controller.Class;

import Controller.Interface.InterfCalcDateTimeLocalController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import View.Interface.InterfCalcDateTimeLocalView;
import View.Menu;
import Enum.EnumDateTimeShiftMode;

public class CalcDateTimeLocalController implements InterfCalcDateTimeLocalController {
    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeLocalView viewLocalTxt;

    public CalcDateTimeLocalController() {
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;

    }

    @Override
    public void setView(InterfCalcDateTimeLocalView viewLocal) {
        this.viewLocalTxt = viewLocal;

    }

    @Override
    public void flowLocal() {
        // Início do fluxo de execução
        Menu menu = viewLocalTxt.getMenu(0);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "A" : flowShiftDateTime(EnumDateTimeShiftMode.ADD); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    // Apresentar opções relativas a somar ou subtrair espaço de tempo a uma data
    private void flowShiftDateTime(EnumDateTimeShiftMode mode) {
        Menu menu = viewLocalTxt.getMenu(1);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "DIA" : shiftDays(); break;
                case "SEM" : shiftWeeks(); break;
                case "MES" : shiftMonths(); break;
                case "ANO" : shiftYears(); break;
                case "C"   : calc(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void shiftDays() {
        System.out.println("ShiftDays");
    }

    private void shiftWeeks() {
        System.out.println("ShiftWeeks");
    }

    private void shiftMonths() {
        System.out.println("ShiftMonths");
    }

    private void shiftYears() {
        System.out.println("ShiftYears");
    }

    private void calc() {
        System.out.println("Calc");
    }
}
