package ClienteServidor;

public class ComunicadoDeErroDeLetra extends Comunicado{

    ComunicadoSalaCompleta dadosDaForca;

    public ComunicadoDeErroDeLetra(ComunicadoSalaCompleta dados)
    {
        this.dadosDaForca = dados;
    }

    public ComunicadoSalaCompleta getDadosDaForca()
    {
        return this.dadosDaForca;
    }

    public void setDadosDaForca(ComunicadoSalaCompleta dados)
    {
        this.dadosDaForca = dados;
    }

}
