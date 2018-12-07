package View.Interface;

import Utilities.Menu;

import java.util.List;

public interface InterfCalcDateTimeView {

    Menu getMenu(int id);

    Menu getDynamicMenu(int id, String statusMessage, String errorMessage, List des);

    Menu getDynamicMenu(int id, String statusMessage, String errorMessage);
}
