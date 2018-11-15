package View;
import java.util.HashMap;

public class Menus {
    HashMap<Integer, Menu> menus;

    public Menus () {
        menus = new HashMap<Integer,Menu>();
    }


    public void addMenu(int codigo, Menu menu) {
        menus.put(codigo,menu);
    }

    public Menu getMenu(int codigo) {
        return menus.get(codigo);
    }
}
