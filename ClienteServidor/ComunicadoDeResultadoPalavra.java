package ClienteServidor;

public class ComunicadoDeResultadoPalavra extends Comunicado{

    private boolean ok;

    public ComunicadoDeResultadoPalavra(boolean result) {
        this.ok = result;
    }

    public boolean getResult() {
        return ok;
    }

    public void setResult(boolean okNovo) {
        this.ok = okNovo;
    }
}
