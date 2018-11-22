package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeScheduleView;

import java.time.format.DateTimeFormatter;

public interface InterfCalcDateTimeScheduleController {

    void setDateTimeFormatterLocal(DateTimeFormatter dtf);

    void setDateTimeFormatterZoned(DateTimeFormatter dtf);

    // Mostrar opções que podem ser feitas na agenda
    public void flowSchedule();

    void setView(InterfCalcDateTimeScheduleView viewSchedule);

    void setModel(InterfCalcDateTimeModel model);
    void saveState();

    /*

    // Adicionar um slot de trabalho
    private void flowAddSlot();

    // Remover um slot de trabalho
    private void flowRemoveSlot();

    // Editar um slot de trabalho
    private void flowEditSlot();

    // Mostrar todos os slots do proximo/dia/semana/mes, dependendo da escolha que vai ser efetuada
    // dia -> resto do dia de hoje + opcoes de next, previous e select
    // semana -> resto da semana atual + opcoes de next, previous e select
    // mês -> resto de mes igual + opcoes de next, previous e select
    // reminder : imprimir identificador antes de cada slot, para ser usado no select
    private void flowGetBusySlotsFor();
    */
}
