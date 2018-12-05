package Utilities;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Utils.repeatStringN;
import static Utilities.ConsoleColors.*;

public class Menu {

    private List<Opcao> linhas;
    private String titulo;
    private List<String> desc;
    private String statusMessage;
    private String errorMessage;

    public Menu(List<Opcao> linhas, String titulo) {
        this.statusMessage = "n/a";
        this.errorMessage = "n/a";
        this.linhas = linhas;
        this.titulo = titulo;
        this.desc = new ArrayList<>();
    }

    public Menu() {

    }

    /*
     * ESTÁ REPETIDO NO UTILS. DEPOIS DE VER SE PODE FICAR LÁ
     * TEM QUE SE TIRAR DAQUI
     */
    private void clearConsole() {
        //Só deve funcionar para linux
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void show() {
        String tag;
        String txt;
        clearConsole();
        int max = biggestString();
        buildHeader();

        for (Opcao o : linhas) {
            tag = stripColors(o.getTag());
            txt = stripColors(o.getTexto());
            System.out.println("| " + txt + " " +
                               repeatStringN(".", max-2-txt.length()-tag.length()) +
                               " " + tag + " |");
        }
        System.out.println("+" + repeatStringN("=", max+2) + "+");
        // Print da mensagem de sucesso
        if (!statusMessage.equals("n/a")) {
            System.out.println(GREEN_BOLD + statusMessage + RESET);

            // Alerta só é mostrado uma vez
            statusMessage = "n/a";
        }
        if (!errorMessage.equals("n/a")) {
            System.out.println(RED_BOLD + errorMessage + RESET);

            // Alerta só é mostrado uma vez
            errorMessage = "n/a";
        }
        System.out.print("Insira a sua opcao: ");
    }

    private void buildHeader() {
        int max = biggestString();
        int lenTxt = stripColors(titulo).length();
        System.out.println("+" + repeatStringN("=", max+2) + "+");
        System.out.println("|" + repeatStringN(" ", (max-lenTxt+2)/2) + titulo +
                           repeatStringN(" ", (max+2-((max-lenTxt+2)/2)-lenTxt)) + "|");
        if (desc.size() > 0) {
            System.out.println("+" + repeatStringN("-", max+2) + "+");
            for (String d : desc) {
                System.out.println("| " + d + repeatStringN(" ", max-stripColors(d).length()+1) + "|");
            }
        }
        System.out.println("+" + repeatStringN("=", max+2) + "+");
    }

    public void addDescToTitle(List<String> l) {
        this.desc.clear();
        for (String str : l)
            this.desc.add(str);
    }

    private int biggestString() {
        int max = 0;
        int lenTxt = 0;
        int lenTag = 0;
        lenTxt = stripColors(titulo).length();
        if (lenTxt > max) max = lenTxt;
        for (String str : desc) {
            lenTxt = stripColors(str).length();
            if (lenTxt > max) max = lenTxt;
        }
        for (Opcao op : linhas) {
            lenTxt = stripColors(op.getTexto()).length();
            lenTag = stripColors(op.getTag()).length();
            if (lenTxt + lenTag + 3 > max)
                max = lenTag + lenTxt + 5;
        }
        return max;
    }

    private String stripColors(String str) {
        String withoutColors;
        String pattern = ("\033\\[0m|\033\\[0;30m|\033\\[0;31m|\033\\[0;32m|\033\\[0;33m|\033\\[0;34m|\033\\[0;35m|\033\\[0;36m|\033\\[0;37m|\033\\[1;30m|\033\\[1;31m|\033\\[1;32m|\033\\[1;33m|\033\\[1;34m|\033\\[1;35m|\033\\[1;36m|\033\\[1;37m|\033\\[4;30m|\033\\[4;31m|\033\\[4;32m|\033\\[4;33m|\033\\[4;34m|\033\\[4;35m|\033\\[4;36m|\033\\[4;37m|\033\\[40m|\033\\[41m|\033\\[42m|\033\\[43m|\033\\[44m|\033\\[45m|\033\\[46m|\033\\[47m|\033\\[0;90m|\033\\[0;91m|\033\\[0;92m|\033\\[0;93m|\033\\[0;94m|\033\\[0;95m|\033\\[0;96m|\033\\[0;97m|\033\\[1;90m|\033\\[1;91m|\033\\[1;92m|\033\\[1;93m|\033\\[1;94m|\033\\[1;95m|\033\\[1;96m|\033\\[1;97m|\033\\[0;100m|\033\\[0;101m|\033\\[0;102m|\033\\[0;103m|\033\\[0;104m|\033\\[0;105m|\033\\[0;106m|\033\\[0;107m");
        withoutColors = str.replaceAll(pattern, "");
        return withoutColors;
    }

    public void addStatusMessage(String statusMessage) {
       this.statusMessage = statusMessage;
    }

    public void addErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
