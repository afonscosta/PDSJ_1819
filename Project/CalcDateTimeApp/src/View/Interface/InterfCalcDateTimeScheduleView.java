package View.Interface;

import Utilities.Menu;

import java.util.List;

public interface InterfCalcDateTimeScheduleView {

    Menu getMenu(int i);

    Menu getDynamicMenu(int id, String statusMessage, String errorMessage, List des);

    Menu getDynamicMenu(int id, String statusMessage, String errorMessage);
}
