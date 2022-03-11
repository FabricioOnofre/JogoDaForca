package Cliente;

import ClienteServidor.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente3
{
	public static final String HOST_PADRAO  = "localhost";
	public static final int    PORTA_PADRAO = 3000;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";

	private static ComunicadoSalaCompleta dadosDaForca = null;



	public static void main (String[] args) throws Exception {
		if (args.length>2)
		{
			System.err.println ("Uso esperado: java Cliente [HOST [PORTA]]\n");
			return;
		}

		Socket conexao=null;
		try
		{
			String host = Cliente3.HOST_PADRAO;
			int    porta= Cliente3.PORTA_PADRAO;

			if (args.length>0)
				host = args[0];

			if (args.length==2)
				porta = Integer.parseInt(args[1]);

			conexao = new Socket (host, porta);
		}
		catch (Exception erro)
		{
			System.err.println ("Indique o servidor e a porta corretos!\n");
			return;
		}

		ObjectOutputStream transmissor=null;
		try
		{
			transmissor =
					new ObjectOutputStream(
							conexao.getOutputStream());
		}
		catch (Exception erro)
		{
			System.err.println ("Indique o servidor e a porta corretos!\n");
			return;
		}

		ObjectInputStream receptor=null;
		try
		{
			receptor =
					new ObjectInputStream(
							conexao.getInputStream());
		}
		catch (Exception erro)
		{
			System.err.println ("Indique o servidor e a porta corretos!\n");
			return;
		}

		Parceiro servidor=null;
		try
		{
			servidor =
					new Parceiro (conexao, receptor, transmissor);
		}
		catch (Exception erro)
		{
			System.err.println ("Indique o servidor e a porta corretos!\n");
			return;
		}

		TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento = null;
		try
		{
			tratadoraDeComunicadoDeDesligamento = new TratadoraDeComunicadoDeDesligamento (servidor);
		}
		catch (Exception erro)
		{} // sei que servidor foi instanciado

		tratadoraDeComunicadoDeDesligamento.start();

		Comunicado comunicado = null;


		/* ********************************************************************************** */

		try
		{

			System.out.println( ANSI_RED + "\nAguardando outros jogadores...\n" + ANSI_RESET);

			// Enquanto o grupo não estiver completo
			// O usuario espera o grupo ficar com 3 jogadores
			do
			{
				comunicado = (Comunicado)servidor.espie ();
			}
			while (!(comunicado instanceof ComunicadoSalaCompleta));

			// O cliente recebe os dados atualizados e repassa para a sua supervisora
			dadosDaForca = (ComunicadoSalaCompleta) servidor.envie();
			servidor.receba(dadosDaForca);

			System.out.println( ANSI_BLUE + "\nGRUPO " + dadosDaForca.getQualGrupo() + " completo...\n" + ANSI_RESET);

		}
		catch (Exception erro)
		{
			erro.printStackTrace();
		}

		char opcao = ' ';
		boolean fimDeJogo = false;

		System.out.println("---------------------------------------------------------------------------------------------\n" +
				ANSI_YELLOW  +"##########			              REGRAS DO JOGO DA FORCA		                   		##########"+  " \n\n" +
				"O jogo se inicia com 3 jogadores, em seguida será sorteada uma palavra aleatória para o jogo.\n" +
				"Cada jogador terá oportunidade de dizer qual é a palavra em jogo ou de escolher uma letra.\n" +
				"O jogador que optar por dizer a palavra em jogo perderá a partida, caso erre a palavra. \n" +
				"O jogador que optar por dizer uma letra, passara a vez ao acertar ou errar uma letra da palavra.\n" +
				"☆ - Ganha o jogo aquele que acertar a palavra ou completar a última letra da palavra! - ☆\n" + ANSI_RESET +
				ANSI_RED +"OBS: Os turnos serão jogados de acordo com a chegada no jogo."+ ANSI_RESET +"\n"+
				"---------------------------------------------------------------------------------------------\n\n\n\n");

		do
		{
			try {
				System.out.println("Espere o seu turno para jogar...");

				// Jogador aguarda seu turno para jogar
				// Podendo ou não ter ganhado ou perdido o jogo
				comunicado = null;
				do {
					comunicado = (Comunicado) servidor.espie();
				}
				while (!(comunicado instanceof ComunicadoDeTurno) &&
						!(comunicado instanceof ComunicadoDePerda) &&
						!(comunicado instanceof ComunicadoDeVitoria));

				comunicado = servidor.envie();
			}
			catch (Exception erro) {
				erro.printStackTrace();
			}

			if(comunicado instanceof ComunicadoDeTurno)
			{
				System.out.println(ANSI_PURPLE +" - AGORA É SUA VEZ - " + ANSI_RESET);

				// Recebe os dados atualizados
				dadosDaForca = ((ComunicadoDeTurno) comunicado).getDadosDaForca();

				try {
					// Envia os dados atualizados para o servidor
					servidor.receba(dadosDaForca);
				}
				catch (Exception erro)
				{
					erro.printStackTrace();
				}

				// Atualiza as informações na tela para o jogador
				System.out.println("\n\nDados do JOGO DA FORCA:");
				System.out.println("Palavra............: " + dadosDaForca.getTracinhos());
				System.out.println("Letras Digitadas...: " + dadosDaForca.getControladorDeLetrasJaDigitadas());
				System.out.println("Palavras digitadas.: " + dadosDaForca.getControladorDePalavrasJaDigitadas());
				System.out.println("Erros..............: " + dadosDaForca.getControladorDeErros());
				System.out.println ("PALAVRA: " + dadosDaForca.getPalavra() + "\n");


				// Exibi o menu de opções
				// Enquanto o jogador não digitar uma opção corretamente
				for(;;) {

					System.out.print("\nMenu de opções do jogo\n");
					System.out.print("|---------------------|\n");
					System.out.print(" Opção 1 - PALAVRA	\n");
					System.out.print(" Opção 2 - LETRA    	\n");
					System.out.print(" Opção 3 - Sair    	\n");
					System.out.print("|---------------------|\n");
					System.out.print("Digite uma opção: ");

					try
					{
						opcao = Teclado.getUmChar();
					}
					catch (Exception erro)
					{ }

					if("123".indexOf(opcao) == -1)
					{
						System.out.println( ANSI_RED + "[ERROR] Opção inválida. Por favor, digite novamente." + ANSI_RESET);
					}
					else
					{
						break;
					}
				}
			}
			else if(comunicado instanceof ComunicadoDeVitoria)
			{
				System.out.println(ANSI_BLUE +" ＼(^-^)／ SUCESSO, VOCÊ GANHOU!!!"+ ANSI_RESET);
				System.out.println(ANSI_BLUE + ((ComunicadoDeVitoria) comunicado).getUnicoJogador() + ANSI_RESET);
				opcao = '0'; // Força o jogador a terminar o jogo
			}
			else if(comunicado instanceof ComunicadoDePerda)
			{
				System.out.println(ANSI_RED + " (╥_╥) QUE PENA, VOCÊ PERDEU!!!" + ANSI_RESET);
				System.out.println(ANSI_RED + ((ComunicadoDePerda)comunicado).getComunicadoDePerda() + ANSI_RESET);
				opcao = '0'; // Força o jogador a terminar o jogo
			}

			// Verifica o que o usuario deseja fazer no jogo
			switch (opcao){
				case '1':
					fimDeJogo = escolheuPalavra(servidor);
					break;

				case '2':
					fimDeJogo = escolheuLetra(servidor);
					break;

				case '3':
					try{
						servidor.receba(new ComunicadoDeSaida());
						fimDeJogo = true;
					}
					catch (Exception err)
					{
						System.err.println("Erro ao tentar sair do servidor");
					}
					break;

				default:
					fimDeJogo = true;
			}
		}
		while (!fimDeJogo);

		try
		{
			servidor.receba (new PedidoParaSair());
		}
		catch (Exception erro)
		{}

		System.out.println("Jogo acabou! A palavra era " +ANSI_GREEN + dadosDaForca.getPalavra() + ANSI_RESET +"\n");
		System.out.println ("Obrigado por usar este programa!");
		System.exit(0);
	}

	private static boolean escolheuPalavra(Parceiro servidor){

		boolean palavraJaDigitada = false;

		do {
			String palavra = "";

			// Espera o jogador digitar uma palavra
			// Enquanto o jogador não digitar corretamente
			for (;;) {
				try {
					System.out.print ("Qual é a palavra sorteada?");
					palavra = Teclado.getUmString();
					System.out.println ();
					break;
				} catch (Exception err) {
					System.err.println("[ERROR] Digitação inválida. Por favor, digite corretamente.");
				}
			}

			try {
				// Palavra é enviada ao servidor
				servidor.receba(new PedidoDeVezPalavra(palavra));
				Comunicado comunicado = null;

				// Jogador aguarda a resposta do servidor
				// Dizendo se ele ganhou, perdeu ou se a palavra já foi digitada
				do {
					comunicado = (Comunicado) servidor.espie();
				}
				while (!(comunicado instanceof ComunicadoDeResultadoPalavra) &&
						!(comunicado instanceof ComunicadoDePalavraJaDigitada));

				comunicado = servidor.envie();

				if(comunicado instanceof ComunicadoDePalavraJaDigitada)
				{
					palavraJaDigitada = true; // Jogador terá que enviar uma nova palavra
					System.out.println(ANSI_RED +" (-_-) Ops! Essa palavra já foi digitada anteriormente." + ANSI_RESET);
				}
				else
				{
					palavraJaDigitada = false;

					// Verifica se ele ganhou ou perdeu o jogo
					ComunicadoDeResultadoPalavra resultado = (ComunicadoDeResultadoPalavra) comunicado;
					if(!resultado.getResult())
					{
						System.out.println(ANSI_RED + " Palavra errada (╥_╥) QUE PENA, VOCÊ PERDEU!!!" + ANSI_RESET);
					}
					else
					{
						System.out.println(ANSI_BLUE +" Palavra certa ＼(^-^)／ SUCESSO, VOCÊ GANHOU!!!"+ ANSI_RESET);
					}
				}
			}
			catch (Exception err) {
				//err.printStackTrace();
				System.err.println("[ERROR] Falha na comunicação com o Servidor.");
				System.err.println("Por favor, tenta enviar a palavra novamente.");
			}
		}
		while (palavraJaDigitada);


		// Ganhando ou perdendo o jogo ira acabar para esse jogador
		return true;
	}


	private static boolean escolheuLetra(Parceiro servidor)
	{

		boolean letraJaDigitada = false;
		do
		{
			char letra = ' ';

			for(;;) {
				try {
					System.out.print("Digite uma letra: ");
					letra = Teclado.getUmChar();
					System.out.println ();
					break;
				} catch (Exception err) {
					System.out.println(ANSI_RED + "[ERROR] Digitação inválida. Por favor, digite corretamente." + ANSI_RESET);
				}
			}

			try {
				// Letra é enviada ao servidor
				servidor.receba(new PedidoDeLetra(letra));


				// Jogador aguarda a resposta do servidor
				// Dizendo se ele acertou, errou, perdou, ganhou ou se a letra já foi digitada
				Comunicado comunicado = null;
				do {
					comunicado = (Comunicado) servidor.espie();
				}
				while (!(comunicado instanceof ComunicadoDeLetraJaDigitada) &&
						!(comunicado instanceof ComunicadoDePerda) &&
						!(comunicado instanceof ComunicadoDeAcertoDeLetra) &&
						!(comunicado instanceof ComunicadoDeErroDeLetra) &&
						!(comunicado instanceof ComunicadoDeVitoria));

				comunicado = servidor.envie();


				if (comunicado instanceof ComunicadoDePerda)
				{
					System.out.println(ANSI_RED + " (╥_╥) QUE PENA, VOCÊ PERDEU!!!" + ANSI_RESET);
					System.out.println(ANSI_RED + ((ComunicadoDePerda)comunicado).getComunicadoDePerda() + ANSI_RESET);
					return true; // Perdeu, então o jogo ira acabar
				}
				else if (comunicado instanceof ComunicadoDeLetraJaDigitada)
				{
					System.out.println(ANSI_RED +" (-_-) Ops! Essa letra já foi digitada anteriormente." + ANSI_RESET);
					letraJaDigitada = true; // Jogador terá que enviar uma nova letra
				}
				else if (comunicado instanceof ComunicadoDeAcertoDeLetra)
				{
					System.out.println(ANSI_GREEN +"Você acertou! (^ｰ^) Essa letra existe na palavra.\n" + ANSI_RESET);
					return false; // Acertou, porém a palavra ainda não está completa, então jogo continua
				}
				else if (comunicado instanceof ComunicadoDeErroDeLetra)
				{
					System.out.println(ANSI_RED + "Você errou! (ಥ_ಥ) Essa letra não existe na palavra.\n" + ANSI_RESET);
					return false; // Errou, porém o máximo de erros ainda não chegou ao limite, então jogo continua
				}
				else if (comunicado instanceof ComunicadoDeVitoria) {
					System.out.println(ANSI_BLUE +" Palavra certa ＼(^-^)／ SUCESSO, VOCÊ GANHOU!!!"+ ANSI_RESET);
					return true; // Ganhou, então jogo ira acabar
				}
			}
			catch (Exception err)
			{
				System.err.println("[ERROR] Falha na comunicação com o Servidor.");
				System.err.println("Por favor, tenta enviar a letra novamente.");
			}
		}
		while (letraJaDigitada);

		return false;
	}
}
