package Utilities;

import static java.time.ZoneId.systemDefault;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static Utilities.BusinessUtils.*;
import static java.lang.System.out;

public class ControllerUtils {

    // Pede ao utilizador uma data.
    // Se não disser nada fica a que se encontra no modelLocal.
    // Devolve null caso dê uma exceção.
    public static ZonedDateTime getDateTimeFromInput(ZonedDateTime zdt, ZoneId zid) {
        Integer year = null;
        Integer month = null;
        Integer day = null;
        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer nano = null;
        String str;

        while (year == null) {
            out.print("Ano (default: " + zdt.getYear() + "): ");
            str = Input.lerString();
            year = validatePosNumber(str, zdt.getYear());
            if (year == null)
                out.println("[!] Ano invalido.");
        }

        while (month == null) {
            out.print("Mes (default: " + zdt.getMonthValue() + "): ");
            str = Input.lerString();
            month = validateMonth(str, zdt.getMonthValue());
            if (month == null)
                out.println("[!] Mes invalido.");
        }

        while (day == null) {
            out.print("Dia (default: " + zdt.getDayOfMonth() + "): ");
            str = Input.lerString();
            day = validateDay(str, zdt.getDayOfMonth(), year, month);
            if (day == null)
                out.println("[!] Dia invalido.");
        }

        while (hour == null) {
            out.print("Hora (default: " + zdt.getHour() + "): ");
            str = Input.lerString();
            hour = validateHour(str, zdt.getHour());
            if (hour == null)
                out.println("[!] Hora invalida.");
        }

        while (minute == null) {
            out.print("Minutos (default: " + zdt.getMinute() + "): ");
            str = Input.lerString();
            minute = validateMinSec(str, zdt.getMinute());
            if (minute == null)
                out.println("[!] Minutos invalidos.");
        }

        while (second == null) {
            out.print("Segundos (default: " + zdt.getSecond() + "): ");
            str = Input.lerString();
            second = validateMinSec(str, zdt.getSecond());
            if (second == null)
                out.println("[!] Segundos invalidos.");
        }

        while (nano == null) {
            out.print("Nanosegundos (default: " + zdt.getNano() + "): ");
            str = Input.lerString();
            nano = validatePosNumber(str, zdt.getNano());
            if (nano == null)
                out.println("[!] Nanosegundos invalidos.");
        }

        if (zid != null)
            return ZonedDateTime.of(year, month, day, hour, minute, second, nano, zid);
        // Colocar o que está no ficheiro de configuração
        return ZonedDateTime.of(year, month, day, hour, minute, second, nano, systemDefault());
    }
}
