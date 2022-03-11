package ClienteServidor;

public class ComunicadoDeAcertoDeLetra extends Comunicado{

    ComunicadoSalaCompleta dadosDaForca;

    public ComunicadoDeAcertoDeLetra(ComunicadoSalaCompleta dados)
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
