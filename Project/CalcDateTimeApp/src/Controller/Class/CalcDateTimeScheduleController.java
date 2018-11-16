package Controller.Class;

import Controller.Interface.InterfCalcDateTimeScheduleController;
import Model.Interface.InterfCalcDateTimeModel;
import Utilities.Input;
import Utilities.Menu;
import View.Interface.InterfCalcDateTimeScheduleView;

public class CalcDateTimeScheduleController implements InterfCalcDateTimeScheduleController {

    private InterfCalcDateTimeModel model;
    private InterfCalcDateTimeScheduleView viewScheduleTxt;

    @Override
    public void setView(InterfCalcDateTimeScheduleView viewSchedule) {
        this.viewScheduleTxt = viewSchedule;
    }

    @Override
    public void setModel(InterfCalcDateTimeModel model) {
        this.model = model;
    }

    public CalcDateTimeScheduleController() {

    }

    public void flowSchedule(){
        Menu menu = viewScheduleTxt.getMenu(0);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "I" : flowAddSlot(); break;
                case "R" : flowRemoveSlot(); break;
                case "A" : flowEditSlot(); break;
                case "V" : flowGetBusySlotsFor(); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    // Adicionar um slot de trabalho
    private void flowAddSlot(){
        System.out.println("Add slot");
    }

    // Remover um slot de trabalho
    private void flowRemoveSlot(){
        System.out.println("Remove slot");
    }

    // Editar um slot de trabalho
    private void flowEditSlot(){
        System.out.println("Edit slot");
    }

    // Mostrar todos os slots do proximo/dia/semana/mes, dependendo da escolha que vai ser efetuada
    // dia -> resto do dia de hoje + opcoes de next, previous e select
    // semana -> resto da semana atual + opcoes de next, previous e select
    // mês -> resto de mes igual + opcoes de next, previous e select
    // reminder : imprimir identificador antes de cada slot, para ser usado no select
    private void flowGetBusySlotsFor(){
        Menu menu = viewScheduleTxt.getMenu(1);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P" : flowResultsBusySlots("P"); break;
                case "D" : flowResultsBusySlots("D"); break;
                case "Sem" : flowResultsBusySlots("Sem"); break;
                case "M" : flowResultsBusySlots("M"); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }

    private void flowResultsBusySlots(String op ){
        //chamada ao metodo que irá devolver as slots pedidas
        //apresentar as slots pedidas
        Menu menu = viewScheduleTxt.getMenu(2);
        String opcao;
        do {
            menu.show();
            opcao = Input.lerString();
            opcao = opcao.toUpperCase();
            switch(opcao) {
                case "P" : flowResultsBusySlots("P"); break;
                case "A" : flowResultsBusySlots("D"); break;
                case "D" : slotDetails(); break;
                case "M" : flowResultsBusySlots("M"); break;
                case "S": break;
                default: System.out.println("Opcão Inválida !"); break;
            }
        }
        while(!opcao.equals("S"));
    }
    private void slotDetails(){}

}
