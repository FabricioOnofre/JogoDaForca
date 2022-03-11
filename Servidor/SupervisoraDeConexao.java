package Servidor;

import ClienteServidor.*;
import Forca.ControladoDeLetrasJaDigitadas;
import Forca.ControladorDeErros;
import Forca.ControladorDePalavrasJaDigitadas;
import Forca.Tracinhos;

import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread
{
    private Parceiro            usuario;
    private Socket              conexao;
    private ArrayList<Parceiro> usuarios;


    ComunicadoSalaCompleta dadosDaForca;
    int idJog1, idJog2, idJog3;

    public SupervisoraDeConexao
    (Socket conexao, ArrayList<Parceiro> usuarios)
    throws Exception
    {
        if (conexao==null)
            throw new Exception ("Conexao ausente");

        if (usuarios==null)
            throw new Exception ("Usuarios ausentes");

        this.conexao  = conexao;
        this.usuarios = usuarios;
    }

    public void run ()
    {

        ObjectOutputStream transmissor;
        try
        {
            transmissor =
            new ObjectOutputStream(
            this.conexao.getOutputStream());
        }
        catch (Exception erro)
        {
            return;
        }
        
        ObjectInputStream receptor=null;
        try
        {
            receptor=
            new ObjectInputStream(
            this.conexao.getInputStream());
        }
        catch (Exception err0)
        {
            try
            {
                transmissor.close();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread
            
            return;
        }

        try
        {
            this.usuario =
            new Parceiro (this.conexao,
                          receptor,
                          transmissor);
        }
        catch (Exception erro)
        {} // sei que passei os parametros corretos


        try
        {
            synchronized (this.usuarios)
            {
                // O id do Usuario corresponde a sua posição no vetor de usuarios
                this.usuario.setId(this.usuarios.size());
                this.usuarios.add (this.usuario);

                // Verifica se há três novos usuarios
                if (this.usuarios.size() % 3 == 0)
                {
                    int qtdUsuarios = this.usuarios.size();

                    // Um novo comunicado é gerado para o novo grupo
                    // Sendo guardado os jogadores e informações desse novo grupo
                    ComunicadoSalaCompleta comunicadoSalaCompleta
                            = new ComunicadoSalaCompleta(qtdUsuarios);

                    int primeiroJogador = comunicadoSalaCompleta.getJogadorUm();
                    int segundoJogador  = comunicadoSalaCompleta.getJogadorDois();
                    int terceiroJogador = comunicadoSalaCompleta.getJogadorTres();

                    // Envia um comunicado com informações do jogo pra cada jogador
                    this.usuarios.get(primeiroJogador).receba(comunicadoSalaCompleta);
                    this.usuarios.get(segundoJogador).receba(comunicadoSalaCompleta);
                    this.usuarios.get(terceiroJogador).receba(comunicadoSalaCompleta);

                    // Envia um comunicado para o 1° Jogador do grupo iniciar a partida
                    ComunicadoDeTurno inicioDeJogo = new ComunicadoDeTurno(comunicadoSalaCompleta);
                    this.usuarios.get(primeiroJogador).receba(inicioDeJogo);
                }
            }

            // Guarda a posição do usuário que essa supervisora cuida
            int idJogadorAtual = this.usuario.getId();

            for(;;)
            {

                // Espera usuario enviar um comunicado
                Comunicado comunicado = this.usuario.envie ();

                if (comunicado==null)
                    return;
                else if (comunicado instanceof ComunicadoSalaCompleta)
                {
                    // Atualiza as informações do jogo
                    dadosDaForca = (ComunicadoSalaCompleta)comunicado;

                    // Recebe as posição em que cada jogador desse grupo está no vetor
                    idJog1 = dadosDaForca.getJogadorUm();
                    idJog2 = dadosDaForca.getJogadorDois();
                    idJog3 = dadosDaForca.getJogadorTres();
                }
                else if(comunicado instanceof ComunicadoDeSaida){

                    // Excluo o jogador que deseja sair do jogo
                    excluiJogador();

                    // Envio um comunicado para passar a vez ao próximo jogador do grupo
                    passaVezExcluir();
                }
                else if (comunicado instanceof PedidoDeVezPalavra)
                {
                    // Recebe a palavra digitada pelo jogador
                    PedidoDeVezPalavra resultado = (PedidoDeVezPalavra)comunicado;
                    String palavra = resultado.getPalavra().toUpperCase();

                    // Verifica se a palavra já foi digitada por outros jogadores
                    if (dadosDaForca.getControladorDePalavrasJaDigitadas().isJaDigitada (palavra))
                    {

                        // Caso seja verdade, o usuario é avisado d
                        this.usuarios.get(idJogadorAtual).receba(new ComunicadoDePalavraJaDigitada());
                    }
                    else
                    {

                        // Caso seja falso, a palavra é registrada como digitada
                        dadosDaForca.getControladorDePalavrasJaDigitadas().registre(palavra);

                        // Verifica se o jogador acertou ou não a palavra na FORCA
                        if(palavra.equals(dadosDaForca.getPalavra().toString()))
                        {
                            // Ele é avisado da vitória no jogo
                            this.usuarios.get(idJogadorAtual).receba(new ComunicadoDeResultadoPalavra(true));

                            // Os outros jogadores são avisados da derrota
                            avisoDePerda(false);
                        }
                        else
                        {
                            // O jogador é excluido e avisado de sua derrota
                            excluiJogador();
                            this.usuarios.get(idJogadorAtual).receba(new ComunicadoDeResultadoPalavra(false));

                            // Passa a vez ao próximo jogador do grupo
                            passaVezExcluir();
                        }
                    }
                }
                else if (comunicado instanceof PedidoDeLetra)
                {
                    // Recebe a letra digitada pelo jogador
                    PedidoDeLetra resultado = (PedidoDeLetra)comunicado;
                    char letra = Character.toUpperCase(resultado.getLetra());

                    // Verifica se o jogo ainda não acabou
                    if (dadosDaForca.getTracinhos().isAindaComTracinhos() &&
                            !dadosDaForca.getControladorDeErros().isAtingidoMaximoDeErros())
                    {
                        try
                        {

                            // Verifica se a letra já foi digitada anteriormente
                            if (dadosDaForca.getControladorDeLetrasJaDigitadas().isJaDigitada (letra))
                            {
                                this.usuarios.get(idJogadorAtual).receba(new ComunicadoDeLetraJaDigitada());
                            }
                            else
                            {

                                // Caso seja falso, a letra é registrada como digitada
                                dadosDaForca.getControladorDeLetrasJaDigitadas().registre (letra);

                                // Verifica se a letra existe na palavra que esta jogo
                                int qtd = dadosDaForca.getPalavra().getQuantidade (letra);
                                if (qtd==0)
                                {

                                    // Caso chegue ao máximo de erros, todos jogadores perdem o jogo
                                    dadosDaForca.getControladorDeErros().registreUmErro ();
                                    if (dadosDaForca.getControladorDeErros().isAtingidoMaximoDeErros())
                                    {
                                        // Todos jogadores são avisados da derrota
                                        avisoDePerda(true);
                                    }
                                    else
                                    {
                                        // Caso contrário, o jogador é avisado sobre seu erro
                                        this.usuarios.get(idJogadorAtual).receba(new ComunicadoDeErroDeLetra(dadosDaForca));

                                        // Passa a vez ao próximo jogador do grupo
                                        liberaProxJogador();
                                    }
                                }
                                else
                                {

                                    // É revelado onde a palavra possui essa letra
                                    for (int i=0; i<qtd; i++)
                                    {
                                        int posicao = dadosDaForca.getPalavra().getPosicaoDaIezimaOcorrencia (i,letra);
                                        dadosDaForca.getTracinhos().revele (posicao, letra);
                                    }

                                    // Verifica se o jogo acabou
                                    if (!dadosDaForca.getTracinhos().isAindaComTracinhos())
                                    {
                                        // Ele é avisado da vitória no jogo
                                        this.usuarios.get(idJogadorAtual).receba(new ComunicadoDeVitoria(false));

                                        // Os outros jogadores são avisados da derrota
                                        avisoDePerda(false);
                                    }
                                    else
                                    {
                                        // Caso contrário, o jogador é avisado sobre seu acerto de letra
                                        this.usuarios.get(idJogadorAtual).receba(new ComunicadoDeAcertoDeLetra(dadosDaForca));

                                        // Passa a vez ao próximo jogador do grupo
                                        liberaProxJogador();
                                    }
                                }
                            }
                        }
                        catch (Exception erro)
                        {
                            System.err.println (erro.getMessage());
                        }
                    }
                }
                else if (comunicado instanceof PedidoParaSair)
                {
                    // Conexão com usuario é fechada
                    this.usuario.adeus();
                }
            }
        }
        catch (Exception erro)
        {
            try
            {
                transmissor.close ();
                receptor   .close ();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread

        }
    }

    private void liberaProxJogador(){
        try {

            int idJogadorAtual = this.usuario.getId();

            if (idJogadorAtual == idJog1)
            {
                if(idJog2 != -1)
                    this.usuarios.get(idJog2).receba(new ComunicadoDeTurno(dadosDaForca));
                else if(idJog3 != -1)
                    this.usuarios.get(idJog3).receba(new ComunicadoDeTurno(dadosDaForca));

            }
            else if (idJogadorAtual == idJog2)
            {
                if(idJog3 != -1)
                    this.usuarios.get(idJog3).receba(new ComunicadoDeTurno(dadosDaForca));
                else if(idJog1 != -1)
                    this.usuarios.get(idJog1).receba(new ComunicadoDeTurno(dadosDaForca));
            }
            else if (idJogadorAtual == idJog3)
            {
                if(idJog1 != -1)
                    this.usuarios.get(idJog1).receba(new ComunicadoDeTurno(dadosDaForca));
                else if(idJog2 != -1)
                    this.usuarios.get(idJog2).receba(new ComunicadoDeTurno(dadosDaForca));
            }
        }
        catch (Exception erro){
            erro.printStackTrace();
        }
    }

    private void passaVezExcluir(){
        try {

            //
            int idJogadorAtual = this.usuario.getId();

            if (idJogadorAtual == idJog1)
            {
                if(idJog2 != -1)
                {
                    if(idJog3 == -1)
                        this.usuarios.get(idJog2).receba(new ComunicadoDeVitoria(true));
                    else
                        this.usuarios.get(idJog2).receba(new ComunicadoDeTurno(dadosDaForca));
                }
                else
                    this.usuarios.get(idJog3).receba(new ComunicadoDeVitoria(true));
            }
            else if (idJogadorAtual == idJog2)
            {
                if(idJog3 != -1)
                {
                    if(idJog1 == -1)
                        this.usuarios.get(idJog3).receba(new ComunicadoDeVitoria(true));
                    else
                        this.usuarios.get(idJog3).receba(new ComunicadoDeTurno(dadosDaForca));
                }
                else
                    this.usuarios.get(idJog1).receba(new ComunicadoDeVitoria(true));
            }
            else if (idJogadorAtual == idJog3)
            {
                if(idJog1 != -1)
                {
                    if(idJog2 == -1)
                        this.usuarios.get(idJog1).receba(new ComunicadoDeVitoria(true));
                    else
                        this.usuarios.get(idJog1).receba(new ComunicadoDeTurno(dadosDaForca));
                }
                else
                    this.usuarios.get(idJog2).receba(new ComunicadoDeVitoria(true));
            }
        }
        catch (Exception erro){
            erro.printStackTrace();
        }
    }

    private void avisoDePerda(boolean todos)
    {
        try
        {
            // Posição que o jogador atual está no vetor de usuarios
            int idJogadorAtual = this.usuario.getId();

            // Todos os jogadores perderam a partida?
            if(!todos) {

                // Verifica se o usuario que ganhou não é o 1° jogador do grupo
                // Caso seja verdade, ele é avisado sobre a sua derrota se ainda estiver no jogo
                if (idJogadorAtual != idJog1 && idJog1 != -1) {

                    if (idJogadorAtual != idJog2)

                        // 1° jogador é informado da sua derrota para o 3° jogaodr
                        this.usuarios.get(idJog1).receba(new ComunicadoDePerda(3));
                    else

                        // 1° jogador é informado da sua derrota para o 2° jogaodr
                        this.usuarios.get(idJog1).receba(new ComunicadoDePerda(2));
                }


                // Verifica se o usuario que ganhou não é o 2° jogador do grupo
                // Caso seja verdade, ele é avisado sobre a sua derrota se ainda estiver no jogo
                if (idJogadorAtual != idJog2 && idJog2 != -1) {

                    if (idJogadorAtual != idJog1)

                        // 2° jogador é informado da sua derrota para o 3° jogador
                        this.usuarios.get(idJog2).receba(new ComunicadoDePerda(3));
                    else

                        // 2° jogador é informado da sua derrota para o 1° jogaodr
                        this.usuarios.get(idJog2).receba(new ComunicadoDePerda(1));
                }

                // Verifica se o usuario que ganhou não é o 3° jogador do grupo
                // Caso seja verdade, ele é avisado sobre a sua derrota se ainda estiver no jogo
                if (idJogadorAtual != idJog3 && idJog3 != -1) {

                    if (idJogadorAtual != idJog2)

                        // 3° jogador é informado da sua derrota para o 1° jogaodr
                        this.usuarios.get(idJog3).receba(new ComunicadoDePerda(1));
                    else

                        // 3° jogador é informado da sua derrota para o 2° jogaodr
                        this.usuarios.get(idJog3).receba(new ComunicadoDePerda(2));
                }
            }
            else // Nenhum jogador ganhou a partida
            {
                // Se o 1° jogador ainda estiver no jogo ele recebe é avisado da derrota
                if (idJog1 != -1)
                    this.usuarios.get(idJog1).receba(new ComunicadoDePerda(true));

                // Se o 2° jogador ainda estiver no jogo ele recebe é avisado da derrota
                if (idJog2 != -1)
                    this.usuarios.get(idJog2).receba(new ComunicadoDePerda(true));

                // Se o 3° jogador ainda estiver no jogo ele recebe é avisado da derrota
                if (idJog3 != -1)
                    this.usuarios.get(idJog3).receba(new ComunicadoDePerda(true));
            }
        }
        catch (Exception erro){
            erro.printStackTrace();
        }
    }

    private void excluiJogador()
    {
        try
        {
            // Posição que esse jogador está no vetor de usuarios
            int idJogadorAtual = this.usuario.getId();

            // Verifica qual é o jogador do grupo, com isso,
            // o jogador é excluido do vetor ao receber uma posição negativa
            if(idJogadorAtual == idJog1)
            {
                dadosDaForca.setJogadorUm(-1);
            }
            else if(idJogadorAtual == idJog2)
            {
                dadosDaForca.setJogadorDois(-1);
            }
            else if(idJogadorAtual == idJog3)
            {
                dadosDaForca.setJogadorTres(-1);
            }

        }
        catch (Exception erro){
            erro.printStackTrace();
        }
    }
}
