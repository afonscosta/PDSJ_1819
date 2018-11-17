package Utilities;

import java.util.ArrayList;
import java.util.List;

import static Utilities.BusinessUtils.repeatStringN;

public class Menu {

    List<Opcao> linhas;
    String titulo;
    List<String> desc;

    public Menu(List<Opcao> linhas, String titulo) {
        this.linhas = linhas;
        this.titulo = titulo;
        this.desc = new ArrayList<>();
    }

    public Menu() {

    }

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
            tag = o.getTag();
            txt = o.getTexto();
            System.out.println("| " + txt + " " +
                               repeatStringN(".", max-2-txt.length()-tag.length()) +
                               " " + tag + " |");
        }
        System.out.println("+" + repeatStringN("=", max+2) + "+");
        System.out.print("Insira a sua opçao: ");
    }

    private void buildHeader() {
        int max = biggestString();
        System.out.println("+" + repeatStringN("=", max+2) + "+");
        System.out.println("|" + repeatStringN(" ", (max-titulo.length()+2)/2) + titulo +
                           repeatStringN(" ", (max+2-((max-titulo.length()+2)/2)-titulo.length())) + "|");
        if (desc.size() > 0) {
            System.out.println("+" + repeatStringN("-", max+2) + "+");
            for (String d : desc) {
                System.out.println("| " + d + repeatStringN(" ", max-d.length()+1) + "|");
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
        if (titulo.length() > max) max = titulo.length();
        for (String str : desc)
            if (str.length() > max) max = str.length();
        for (Opcao op : linhas) {
            lenTxt = op.getTexto().length();
            lenTag = op.getTag().length();
            if (lenTxt + lenTag + 3 > max)
                max = lenTag + lenTxt + 3 + 6;
        }
        return max;
    }

    public void addDateTimeToTitle(String dt) {
        this.titulo = this.titulo.substring(0, 9) + dt;
    }
}
