package View;

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

    public void show() {
        System.out.println("====" + titulo);

        for (Opcao o : linhas) {
            System.out.println(o.toString());
        }

        System.out.print("Insira a sua opcao: ");
    }
}
