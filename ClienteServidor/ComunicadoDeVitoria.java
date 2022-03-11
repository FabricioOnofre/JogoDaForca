package ClienteServidor;

public class ComunicadoDeVitoria extends Comunicado{

    private boolean unicoJogador = false;

    public ComunicadoDeVitoria(boolean vitoria) {
        this.unicoJogador = vitoria;
    }

    public ComunicadoDeVitoria() {
    }

    public String getUnicoJogador()
    {
        if (unicoJogador)
            return "Os outros jogadores falharam, então você venceu!";
        else
            return "Você descobriu a palavra, parabéns!";
    }

}
