package View.Interface;

import Utilities.Menu;

import java.util.List;

public interface InterfCalcDateTimeLocalView {

    Menu getMenu(int i);

    Menu getDynamicMenu(int id, String statusMessage, String errorMessage, List des);
}
