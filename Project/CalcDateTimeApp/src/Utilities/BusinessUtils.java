package Utilities;

import Enum.EnumDateTimeShiftMode;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

// Metodos estáticos aqui
public class BusinessUtils {

    // Dada uma ZonedDateTime, soma ou subtrai, dependendo do mode, n ChronoUnits.
    public static ZonedDateTime shiftDateTime(ZonedDateTime zdt, int n, ChronoUnit cu, EnumDateTimeShiftMode mode) {
        //Não sei se é boa ideia este método depender do EnumDateTimeShiftMode.
        switch (mode) {
            case ADD: zdt = zdt.plus(n, cu); break;
            case SUB: zdt = zdt.minus(n, cu); break;
        }
        return zdt;
    }

    public static void clearConsole() {
        //Só deve funcionar para linux
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
