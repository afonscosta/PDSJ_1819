package Utilities;

public class Opcao {
    String texto;
    String tag;

    public Opcao (String texto, String tag) {
        this.texto = texto;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getTexto() {
        return texto;
    }

    public String toString () {
        return texto + tag;
    }
}
