package ClienteServidor;

public class PedidoDeVezPalavra extends Comunicado{

    private String palavra;


    public PedidoDeVezPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }
}
