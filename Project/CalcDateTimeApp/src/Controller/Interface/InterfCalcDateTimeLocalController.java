package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeLocalView;

public interface InterfCalcDateTimeLocalController {

    // Apresentar opções que podem ser feitas na calculadora local (somar, subtrair, diferença)
    public void flowLocal();

    void setModel(InterfCalcDateTimeModel model);

    void setView(InterfCalcDateTimeLocalView viewLocal);

    /*
    // Apresentar opções relativas a somar ou subtrair espaço de tempo a uma data
    private void flowShiftDateTime(EnumDateTimeShiftMode mode);

    // Pedir data de fim e efetuar diferença
    private void flowDiffBetweenDateTimes();

    // Retirar datas a partir de descrição ordinal
    private void flowAdjustDateTime();

    // Adicionar ou subtrair dias úteis a uma data
    private void flowShiftWorkdays(EnumDateTimeShiftMode mode);

    // Mostrar datetime em horas, minutos, segundos... totais
    private void flowReduceTo();
    */
}
