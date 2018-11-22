package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeZoneView;

import java.time.format.DateTimeFormatter;

public interface InterfCalcDateTimeZoneController {

    void setDateTimeFormatter(DateTimeFormatter dtf);

    // Apresentar opções que podem ser feitas na calculadora local (conversão entre timeZones, somar e subtrair, diferença entre tempos)
    public void flowZone();

    void setModel(InterfCalcDateTimeModel model);

    void setView(InterfCalcDateTimeZoneView viewZone);

    /*
    // Mostra diferença de tempo entre o tempo do buffer e o tempo a pedir
    private void flowConvertZone();

    // Apresentar opções relativas a somar ou subtrair espaço de tempo a uma data 
    private void flowShiftZoneDateTime(EnumDateTimeShiftMode mode);

    // Pedir data de fim e efetuar diferença
    private void flowDiffBetweenDateTimes();

    // Adicionar ou subtrair dias úteis a uma data
    private void flowShiftWorkdays(EnumDateTimeShiftMode mode);

    // Mostrar datetime em horas, minutos, segundos... totais
    private void flowReduceTo();

    // Saber que dateTime é numa certa região
    private void flowGetTimeIn();
    */

}
