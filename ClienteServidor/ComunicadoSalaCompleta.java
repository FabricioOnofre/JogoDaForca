package ClienteServidor;

import Forca.*;

public class ComunicadoSalaCompleta extends Comunicado{

    private int jogadorUm;
    private int jogadorDois;
    private int jogadorTres;

    private int qualGrupo;
    private Palavra palavra;
    private Tracinhos tracinhos;
    private ControladoDeLetrasJaDigitadas controladorDeLetrasJaDigitadas;
    private ControladorDeErros controladorDeErros;
    private ControladorDePalavrasJaDigitadas controladorDePalavrasJaDigitadas;

    public ComunicadoSalaCompleta(int qtdUsuarios) {
        try
        {

            // São guardadas as posições dos três jogadores desse grupo
            this.jogadorUm      = qtdUsuarios - 3;
            this.jogadorDois    = qtdUsuarios - 2;
            this.jogadorTres    = qtdUsuarios - 1;

            // Indentifica qual é o novo grupo
            this.qualGrupo = qtdUsuarios / 3;

            // Inicia os dados do Jogo da forca
            this.palavra = BancoDePalavras.getPalavraSorteada();
            this.tracinhos = new Tracinhos (palavra.getTamanho());
            this.controladorDeLetrasJaDigitadas = new ControladoDeLetrasJaDigitadas();
            this.controladorDePalavrasJaDigitadas = new ControladorDePalavrasJaDigitadas();
            this.controladorDeErros = new ControladorDeErros ((int)(palavra.getTamanho()*0.6));

        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
    }

    public ControladorDePalavrasJaDigitadas getControladorDePalavrasJaDigitadas() {
        return controladorDePalavrasJaDigitadas;
    }

    public void setControladorDePalavrasJaDigitadas(ControladorDePalavrasJaDigitadas controladorDePalavrasJaDigitadas) {
        this.controladorDePalavrasJaDigitadas = controladorDePalavrasJaDigitadas;
    }

    public int getJogadorUm() {
        return jogadorUm;
    }

    public void setJogadorUm(int jogadorUm) {
        this.jogadorUm = jogadorUm;
    }

    public int getJogadorDois() {
        return jogadorDois;
    }

    public void setJogadorDois(int jogadorDois) {
        this.jogadorDois = jogadorDois;
    }

    public int getJogadorTres() {
        return jogadorTres;
    }

    public void setJogadorTres(int jogadorTres) {
        this.jogadorTres = jogadorTres;
    }

    public int getQualGrupo() {
        return qualGrupo;
    }

    public void setQualGrupo(int qualGrupo) {
        this.qualGrupo = qualGrupo;
    }


    public Palavra getPalavra() {
        return palavra;
    }

    public void setPalavra(Palavra palavra) {
        this.palavra = palavra;
    }


    public Tracinhos getTracinhos() {
        return tracinhos;
    }

    public void setTracinhos(Tracinhos tracinhos) {
        this.tracinhos = tracinhos;
    }

    public ControladoDeLetrasJaDigitadas getControladorDeLetrasJaDigitadas() {
        return controladorDeLetrasJaDigitadas;
    }

    public void setControladorDeLetrasJaDigitadas(ControladoDeLetrasJaDigitadas controladorDeLetrasJaDigitadas) {
        this.controladorDeLetrasJaDigitadas = controladorDeLetrasJaDigitadas;
    }

    public ControladorDeErros getControladorDeErros() {
        return controladorDeErros;
    }

    public void setControladorDeErros(ControladorDeErros controladorDeErros) {
        this.controladorDeErros = controladorDeErros;
    }

}
