package ClienteServidor;

import Forca.*;

public class ComunicadoDeTurno extends Comunicado{

    ComunicadoSalaCompleta dadosDaForca;

    public ComunicadoDeTurno(ComunicadoSalaCompleta dados)
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