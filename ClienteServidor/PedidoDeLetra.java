package ClienteServidor;

public class PedidoDeLetra extends Comunicado{

    private char letra;

    public PedidoDeLetra(char letra) {
        this.letra = letra;
    }

    public char getLetra() {
        return letra;
    }

    public void setLetra(char palavra) {
        this.letra = letra;
    }
}
