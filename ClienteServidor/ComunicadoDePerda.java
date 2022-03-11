package ClienteServidor;

public class ComunicadoDePerda extends Comunicado{

    private int ganhador;
    private boolean isPerderam;

    public ComunicadoDePerda(boolean perdeu){
        this.isPerderam = perdeu;
    }

    public ComunicadoDePerda(int jogador){

        this.ganhador = jogador;
        this.isPerderam = false;
    }

    public String getComunicadoDePerda(){
        if(this.isPerderam == true){
            return "Todos os jogadores perderam, pois o máximo de erros foi atinigido. ";
        }
        else
        {
            return "Vencedor: Jogador " + ganhador + ". ";
        }
    }
}
