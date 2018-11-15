package View;

public class Opcao {
    String texto;
    String tag;

    public Opcao (String texto, String tag) {
        this.texto = texto;
        this.tag = tag;
    }

    public String toString () {
        return texto + "\t" + tag;
    }
}
