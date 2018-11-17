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
        clearConsole();
        buildHeader();

        for (Opcao o : linhas) {
            System.out.println(o.toString());
        }

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
        if (titulo.length() > max) max = titulo.length();
        for (String str : desc)
            if (str.length() > max) max = str.length();
        return max;
    }

    public void addDateTimeToTitle(String dt) {
        this.titulo = this.titulo.substring(0, 9) + dt;
    }
}
