package trabalho3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;


public class Client
{
	private Socket client;
	private DataOutputStream dataOut;
	private DataInputStream dataIn;
	private static Scanner scan = new Scanner(System.in);

	public Client(String host, int port) throws IOException
	{
		client = new Socket(host, port);

		OutputStream out = client.getOutputStream();
		dataOut = new DataOutputStream(out); // envia informação para o cliente

		InputStream in = client.getInputStream();
		dataIn = new DataInputStream(in); // Recebe um input do cliente
	}

	public DataOutputStream getDataOut()
	{
		return dataOut;
	}

	public DataInputStream getDataIn()
	{
		return dataIn;
	}

	public Scanner getScan()
	{
		return scan;
	}


	public static String printTabuleiro(String matrixString, String numeroDiscos)
	{
		String [] matrixDiv = matrixString.split(" ");
		int escolhaNumeroDiscos = Integer.parseInt(numeroDiscos);
		int[][] Matrix = new int[escolhaNumeroDiscos][3];
		for (int linha = 0; linha < Matrix.length; linha++)
		{
			String [] newDivCaracter = matrixDiv[linha].split(",");
			Matrix[linha][0] =   Integer.parseInt(newDivCaracter[0]);
			Matrix[linha][1] =   Integer.parseInt(newDivCaracter[1]);
			Matrix[linha][2] =   Integer.parseInt(newDivCaracter[2]);
		}
		
		int numeroColunas = (6 * (escolhaNumeroDiscos - 1) + 13); // Tamanho do maior disco é dado por (3+2(n-1))
		// então,
		// quero 3 pinos
		// na posicao do meio do disco considerando um espaçamento de 1 entre cada pino e de 2 correspondentes a
		// borda, logo : 3*(3+2(n-1))+4
		int numeroLinhas = (escolhaNumeroDiscos + 3);

		String[][] gameBoard = new String[numeroLinhas][numeroColunas];
		
		int pino1Posicao = 1 + (((3 + 2 * (escolhaNumeroDiscos - 1)) - 1)) / 2;
		int pino2Posicao = 3 * pino1Posicao;
		int pino3Posicao = 5 * pino1Posicao;
		int tamanhoPeca = 0;
		int tamanhoPecaParaCadaLado = 0;

		for (int linha = 0; linha < gameBoard.length; linha++) // Criar o tabuleiro vazio
		{
			for (int coluna = 0; coluna < gameBoard[linha].length; coluna++)
			{
				if (linha == numeroLinhas - 2) // Desenhar mesa
					gameBoard[linha][coluna] = "=";

				else if (linha != numeroLinhas - 1 && coluna == pino1Posicao
						|| linha != numeroLinhas - 1 && coluna == pino2Posicao
						|| linha != numeroLinhas - 1 && coluna == pino3Posicao)
					gameBoard[linha][coluna] = "|";
				else if (linha == numeroLinhas - 1 && coluna == pino1Posicao)
					gameBoard[linha][coluna] = "A";
				else if (linha == numeroLinhas - 1 && coluna == pino2Posicao)
					gameBoard[linha][coluna] = "B";
				else if (linha == numeroLinhas - 1 && coluna == pino3Posicao)
					gameBoard[linha][coluna] = "C";
				else
					gameBoard[linha][coluna] = " ";
			}
		}

		for (int linha = 0; linha < Matrix.length; linha++) // Criar as pecas com base na matriz (Matrix) do tabuleiro
		{
			if (Matrix[linha][0] != 0)
			{
				tamanhoPeca = 3 + 2 * (Matrix[linha][0] - 1); // Obtendo o numero de * para cada disco
				tamanhoPecaParaCadaLado = (tamanhoPeca + 1) / 2 - 1; // Quantidade de * de cada lado desconsiderando o
																		// meio
				for (int tamanho = -tamanhoPecaParaCadaLado; tamanho < tamanhoPecaParaCadaLado + 1; tamanho++)
				{
					gameBoard[linha + 1][pino1Posicao + tamanho] = "*";
				}
			}
			if (Matrix[linha][1] != 0)
			{
				tamanhoPeca = 3 + 2 * (Matrix[linha][1] - 1);
				tamanhoPecaParaCadaLado = (tamanhoPeca + 1) / 2 - 1;
				for (int tamanho = -tamanhoPecaParaCadaLado; tamanho < tamanhoPecaParaCadaLado + 1; tamanho++)
				{
					gameBoard[linha + 1][pino2Posicao + tamanho] = "*";
				}
			}
			if (Matrix[linha][2] != 0)
			{
				tamanhoPeca = 3 + 2 * (Matrix[linha][2] - 1);
				tamanhoPecaParaCadaLado = (tamanhoPeca + 1) / 2 - 1;
				for (int tamanho = -tamanhoPecaParaCadaLado; tamanho < tamanhoPecaParaCadaLado + 1; tamanho++)
				{
					gameBoard[linha + 1][pino3Posicao + tamanho] = "*";
				}
			}
		}
		
		String tabuleiroString = "";
		for (int linha = 0; linha < gameBoard.length; linha++)
		{
			for (int coluna = 0; coluna < gameBoard[linha].length; coluna++)
			{
				tabuleiroString += gameBoard[linha][coluna];
			}
			tabuleiroString += "\n";
		}
		return tabuleiroString;
	}

	public void close() throws IOException
	{
		dataOut.close();
		dataIn.close();
		client.close();
	}

	public void jogo() throws IOException
	{
		boolean gameMenu = true;
		boolean gameOn = true;

		System.out.println("// ------------------------ Torre de Hanoi ------------------------ \\\\" + "\n");

		while (gameMenu == true)
		{
			System.out.println("Por favor, insira o número de disos:               Y/y: Sair do jogo ");
			String escolhaNumeroDiscos = scan.nextLine();
			dataOut.writeUTF(escolhaNumeroDiscos);
			dataOut.flush();

			String[] numeroOtimo = dataIn.readUTF().split(":");

			boolean status1NumeroDiscos = Boolean.parseBoolean(numeroOtimo[0]);
			boolean status2NumeroDiscos = Boolean.parseBoolean(numeroOtimo[1]);

			if (status1NumeroDiscos == true && status2NumeroDiscos == true)
			{
				System.out.println("O número ótimo de jogadas para " + escolhaNumeroDiscos + " discos, é igual a "
						+ numeroOtimo[2] + "\n");
			}

			else if (status1NumeroDiscos == true && status2NumeroDiscos == false && numeroOtimo[2].equals("min3"))
			{
				System.out.println("Deve haver no mínimo 3 discos.");
			}
			else if (status1NumeroDiscos == true && status2NumeroDiscos == false && numeroOtimo[2].equals("max10"))
			{
				System.out.println("Só pode conter no máximo 10 discos.");
			}
			else if (status1NumeroDiscos == false && status2NumeroDiscos == false)
			{
				System.out.println("// ================== Você saiu do jogo! ================== //");
			}
			else
			{
				System.out.println("Opção inválida! Tente novamente!");
				
			}

			gameMenu = status1NumeroDiscos;
			gameOn = status2NumeroDiscos;

			boolean pinoMenu = gameOn;

			while (pinoMenu == true)
			{
				System.out.println("Por favor, insira o pino inicial:  [A,B,C]");
				String escolhaPinoInicial = scan.nextLine();
				dataOut.writeUTF(escolhaPinoInicial);
				dataOut.flush();

				System.out.println("Por favor, insira o pino final:  [A,B,C]");
				String escolhaPinoFinal = scan.nextLine();
				dataOut.writeUTF(escolhaPinoFinal);
				dataOut.flush();

				String[] Pinos = dataIn.readUTF().split(":");
				boolean veriPinos = Boolean.parseBoolean(Pinos[0]);

				if (veriPinos == true && Pinos[1].equals("pinosIguais"))
				{
					System.out.println("O pino inicial e final não podem ser o mesmo!");
				}
				else if (veriPinos == false && Pinos[1].equals("pinosCorretos"))
				{
					System.out.println(
							"O pino inicial é " + escolhaPinoInicial.toUpperCase() + " e o pino final é " + escolhaPinoFinal.toUpperCase() + "\n");
				}

				else
				{
					System.out.println("O pino inicial e/ou final foram inseridos incorretamente!");
				}
				pinoMenu = veriPinos;
			}

			while (gameOn == true)
			{
				String [] tab = dataIn.readUTF().split(":"); //imprime o tabuleiro e o numero de movimentos.
				System.out.println("\n" + "Seus movimentos: " + tab[0] + "\n\n" + printTabuleiro(tab[1],escolhaNumeroDiscos));
				
				// CLiente escolhe o movimento
				System.out.println("Por favor, escolha o seu movimento: " + "\n" + "\n"
						+ "1: A->B   2: A->C   3: B->A   4: B->C   5: C->A   6: C->B	 Y/y: Sair do jogo");
				String escolhaMov = scan.nextLine();
				dataOut.writeUTF(escolhaMov); // enviando ao servidor o movimento
				dataOut.flush();

				String[] respServidor3 = dataIn.readUTF().split(":");
				
				boolean veriMovimento1 = Boolean.parseBoolean(respServidor3[0]);
				boolean veriMovimento2= Boolean.parseBoolean(respServidor3[1]);
				
				if(veriMovimento1==true && veriMovimento2==true && respServidor3[2].equals("invalido"))
				{
					System.out.println("O movimento só é possível quando há peça para ser retirada.");
				}
				else if (veriMovimento1==true && veriMovimento2==true && respServidor3[2].equals("valido"))
				{
					System.out.println("O movimento é válido.");
				}
				else if (veriMovimento1==true && veriMovimento2==true && respServidor3[2].equals("opcoes")) {
					System.out.println("Sua escolha deve estar nas opções apresentadas!");
				}
				else if (veriMovimento1==true && veriMovimento2==true && respServidor3[2].equals("movIncorreto")) {
					System.out.println("Não é possível colocar uma peça de tamanho maior em cima de uma peça de tamanho menor!");
				}
				else if (veriMovimento1==false && veriMovimento2==false && respServidor3[2].equals("leftGame")){
					System.out.println("// ================== Você saiu do jogo! ================== //");
				}
				else {
					System.out.println("Opção inválida! Tente novamente!");
				}
				gameMenu = Boolean.parseBoolean(respServidor3[0]);
				gameOn = Boolean.parseBoolean(respServidor3[1]);

				boolean seGanhou = Boolean.parseBoolean(dataIn.readUTF());

				if (seGanhou == true)
				{
					String[] respServidor4 = dataIn.readUTF().split(":");
					boolean veriVit1 = Boolean.parseBoolean(respServidor4[0]);
					boolean veriVit2= Boolean.parseBoolean(respServidor4[1]);
					if(veriVit1 == false && veriVit2 == false && respServidor4[2].equals("comNO")) {
						System.out.println(printTabuleiro(respServidor4[4],escolhaNumeroDiscos)+"\n*********************************************************\n"
										+ "Parabéns! Você ganhou com " + respServidor4[3] + " jogadas!!! " + "\n"
										+ "Você ganhou realizando o número mínimo de jogadas, que é " + numeroOtimo[2]
										+ "\n" + "*********************************************************");
					}
					if(veriVit1 == false && veriVit2 == false && respServidor4[2].equals("semNO")) {
						
						System.out.println(printTabuleiro(respServidor4[4],escolhaNumeroDiscos)+"\n*********************************************************\n"
										+ "Parabéns! Você ganhou com " + respServidor4[3] + " jogadas!!! " + "\n"
										+ "No entanto você poderia ter ganho com o número mínimo de jogadas, que é " + numeroOtimo[2]
										+ "\n" + "*********************************************************");
					}
					
					gameMenu = veriVit1;
					gameOn =  veriVit2;

				}

			}
		}
	}

	public void menuFinalCliente() throws IOException
	{
		boolean menuFinal = true;
		while (menuFinal == true)
		{
			System.out.println("==========Selecione uma opção========== \n" + "1-> Jogar novamente \n"
					+ "2-> Ver estatísticas \n" + "3-> Sair");

			String opcao = scan.nextLine();
			dataOut.writeUTF(opcao);
			dataOut.flush();
			
			
			String[] respostaServidor = dataIn.readUTF().split(":");

			menuFinal = Boolean.parseBoolean(respostaServidor[0]);

			if (respostaServidor[1].equals("JogarNovamente"))
			{
				System.out.println("Iniciando o jogo!");
				jogo();
			}

			else if (respostaServidor[1].equals("VerEstatísticas"))
			{
				String [] verEstat = dataIn.readUTF().split(":");
				if(verEstat[0].equals("null"))
				{
					System.out.println("Ainda não há estatísticas feitas");
				}
				else {
					System.out.println("Para " + verEstat[1]+ " discos, você realizou uma média de " + verEstat[2]+" movimentos e jogou "+verEstat[3]+" vezes \n");
					
				}
			}
			else if (respostaServidor[1].equals("SeçãoEencerrada")) {
				
				System.out.println("Você decidiu encerrar a seção ");
				close();
			}
			else {
				System.out.println("Input inválida!");
			}

		}
	}

	public static void main(String[] args) throws IOException
	{
		boolean conexao = true;

		while (conexao == true)
		{
			System.out.println("Deseja conectar-se ao servidor do jogo e inicar uma sessão? [Y/N]");
			String resposta = scan.nextLine();

			if (resposta.equalsIgnoreCase("y"))
			{
				Client cliente = new Client("localhost", 7000);

				boolean loginMenu = true;

				while (loginMenu == true)
				{
					// Pede o login ao utilizador
					System.out.println("Por favor, insira o login: ");
					String login = scan.nextLine();
					cliente.getDataOut().writeUTF(login);
					cliente.getDataOut().flush();

					// Pede a senha

					System.out.println("Insira a sua password: ");
					String password = scan.nextLine();
					cliente.getDataOut().writeUTF(password);
					cliente.getDataOut().flush();

					String[] serverResponse = cliente.getDataIn().readUTF().split(":");

					loginMenu = Boolean.parseBoolean(serverResponse[0]);
					boolean tentNovamente = Boolean.parseBoolean(serverResponse[1]);

					if (tentNovamente == false)
					{
						System.out.println("========== Bem Vindo " + login + " ==========" + "\n");

						cliente.jogo();
						cliente.menuFinalCliente();
					}

					while (tentNovamente == true)
					{
						System.out.println("Inputs incorretos, deseja tentar novamente? [Y/N]");
						// Se a pessoa deseja tentar novamente inserir os inputs
						String tentarNovo = scan.nextLine();

						cliente.getDataOut().writeUTF(tentarNovo);
						cliente.getDataOut().flush();

						String[] serverResponse2 = cliente.getDataIn().readUTF().split(":");

						boolean status1 = Boolean.parseBoolean(serverResponse2[0]);
						boolean status2 = Boolean.parseBoolean(serverResponse2[1]);

						if (status1 == true && status2 == false)
						{
							System.out.println("Você ecolheu tentar novamente.");
						}
						else if (status1 == false && status2 == false)
						{
							System.out.println("Você ecolheu não tentar novamente.");
						}
						loginMenu = status1;
						tentNovamente = status2;

					}
				}
				cliente.close();
				conexao = false;
			}
			else if (resposta.equalsIgnoreCase("n"))
			{
				System.out.println("Que pena! Até mais :) ");
				conexao = false;
			}
			else
			{
				System.out.println("Você inseriu um input incorreto, por favor tente novamente!");
			}
		}

	}

}
