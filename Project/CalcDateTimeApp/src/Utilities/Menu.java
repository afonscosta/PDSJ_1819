package Utilities;

import java.util.List;

public class Menu {

    List<Opcao> linhas;
    String titulo;

    public Menu(List<Opcao> linhas, String titulo) {
        this.linhas = linhas;
        this.titulo = titulo;
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
        System.out.println("====" + titulo);

        for (Opcao o : linhas) {
            System.out.println(o.toString());
        }

        System.out.print("Insira a sua opçao: ");
    }

    public void addDateTimeToTitle(String dt) {
        this.titulo = this.titulo.substring(0, 9) + dt;
    }
}
