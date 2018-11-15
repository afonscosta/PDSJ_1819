package Controller.Interface;

import Model.Interface.InterfCalcDateTimeModel;
import View.Interface.InterfCalcDateTimeView;

public interface InterfCalcDateTimeController  {
    // Apresentar três menus iniciais e delegar o respetivo controller do menu selecionado
    void startFlow();

    void setView(InterfCalcDateTimeView view);
}
