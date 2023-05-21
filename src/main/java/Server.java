package trabalho3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server
{
	private ServerSocket server;
	private Socket clientServer;
	private DataOutputStream dataOut;
	private DataInputStream dataIn;

	private Map<String, String> usuariosCredenciais = new HashMap<String, String>();

	public Server(int port) throws IOException
	{
		server = new ServerSocket(port);

	}

	public DataOutputStream getDataOut()
	{
		return dataOut;
	}

	public DataInputStream getDataIn()
	{
		return dataIn;
	}

	public void getClient() throws IOException
	{
		clientServer = server.accept();

		OutputStream out = clientServer.getOutputStream();
		dataOut = new DataOutputStream(out);// envia informação para o cliente

		InputStream in = clientServer.getInputStream();
		dataIn = new DataInputStream(in);// Recebe um input do cliente
	}

	public void closeCLienteAtual() throws IOException
	{
		dataOut.close();
		dataIn.close();
		clientServer.close();
	}

	public void iniciarListaUser()
	{
		usuariosCredenciais.put("cteixei", "1234");
		usuariosCredenciais.put("Helo", "2101");
		usuariosCredenciais.put("Dessinha", "2604");
		usuariosCredenciais.put("guest1", "2020");
		usuariosCredenciais.put("a", "1");

	}

	public boolean verificarCredenciais(String login, String senha)
	{
		for (String key : usuariosCredenciais.keySet())
		{

			if (login.equals(key) && senha.equals(usuariosCredenciais.get(key)))
			{
				return true;
			}
		}
		return false;
	}

	static boolean verificarInteiro(String input)
	{
		for (int i = 0; i < input.length(); i++)
			if (Character.isDigit(input.charAt(i)) == false)
				return false;
		return true;
	}

	static boolean verificarVazio (String input) {
		if("".equals(input)) {
			return false;
		}
		return true;
	}
	
	public static int[][] criarMatrix(int numeroDiscos, int pinoInicial) // Método cria uma matriz usada para
																			// construir o print do
	// desenho
	{
		int[][] Matrix = new int[numeroDiscos][3];
		for (int linha = 0; linha < Matrix.length; linha++)
		{
			Matrix[linha][pinoInicial] = 1 + linha; // O +1 é para que o primeiro elemento da primeira coluna seja
													// diferente de
			// zero
		} // Numeros diferentes de zero representam os discos.
		return Matrix;
	}

	public static int[][] movement(int[][] Matrix, int posicaoInicial, int posicaoFinal) // Método para os movimentos
	{
		// movementValue: 1(A->B) 2(A->C) 3(B->A) 4(B->C) 5(C->A) 6(C->B)
		int value = 0;
		int linha = 0;
		boolean verificar = true;
		while (verificar == true && linha < Matrix.length)
		{
			if (Matrix[linha][posicaoInicial] != 0)
			{
				value = Matrix[linha][posicaoInicial];
				Matrix[linha][posicaoInicial] = 0;
				verificar = false;
			}
			linha += 1;
		}

		if (Matrix[Matrix.length - 1][posicaoFinal] == 0)
		{
			Matrix[Matrix.length - 1][posicaoFinal] = value;
		}
		else
		{
			linha = 0;
			verificar = true;
			while (verificar == true && linha < Matrix.length)
			{
				if (Matrix[linha][posicaoFinal] != 0)
				{
					Matrix[linha - 1][posicaoFinal] = value;
					verificar = false;
				}
				linha += 1;
			}

		}
		return Matrix;
	}

	public static boolean verificarMovimento(int[][] Matrix, int posicaoInicial, int posicaoFinal)
	{
		// movementValue: 1(A->B) 2(A->C) 3(B->A) 4(B->C) 5(C->A) 6(C->B)
		// A=0 ,B=1 e C=2 => Ex Movimento 1 (1,2)= (A->B)
		int value = 0;
		int linha = 0;
		boolean verificar = true;
		while (verificar == true && linha < Matrix.length)
		{
			if (Matrix[linha][posicaoInicial] != 0) // Pego o primeiro elemento diferente de zero da posicao inicial
			{
				value = Matrix[linha][posicaoInicial];
				verificar = false; // Só quero o primeiro elemento
			}
			linha += 1;
		}

		if (Matrix[Matrix.length - 1][posicaoFinal] == 0)
		{
			return true; // Ver se a posição final estiver vazia, se estiver vazia o movimento é possível
		}
		else
		{
			for (int linha1 = 0; linha1 < Matrix.length; linha1++)
			{
				if (Matrix[linha1][posicaoFinal] != 0)
				{
					if (Matrix[linha1][posicaoFinal] < value) // Compara se as peças são maiores ou menores
					{
						return false;
					}
					else
					{
						return true;
					}
				}
			}
		}
		return true;
	}

	public static boolean verificarPeca(int[][] Matrix, int posicaoInicial)
	{
		for (int linha = 0; linha < Matrix.length; linha++)
		{
			if (Matrix[linha][posicaoInicial] != 0) // Não é possível remover uma peça de um pino vazio
			{
				return true;
			}
		}
		return false;
	}

	public static boolean verificarVitoria(int[][] Matrix, int pinoFinal)
	{
		for (int linha = 0; linha < Matrix.length; linha++)
		{
			if (Matrix[linha][pinoFinal] == 0)
			{
				return false;
			}
		}
		return true;
	}


	public static String printMatrix(int[][] Matrix)
	{
		String matrixString= "";
		
		for (int linha = 0; linha < Matrix.length; linha++)
		{
			for (int coluna = 0; coluna < Matrix[linha].length; coluna++)
			{
				String matSt = Integer.toString(Matrix[linha][coluna]);
				
				matrixString += matSt+",";
			}
			matrixString= matrixString.substring(0, matrixString.length()-1);
			matrixString += " ";
		}
		matrixString= matrixString.substring(0, matrixString.length()-1);
		return matrixString;
	}

	public void jogo(serverUser usuario) throws IOException
	{

		int numeroDiscos;
		boolean gameMenu = true;
		boolean gameOn = true;

		while (gameMenu == true)
		{
			System.out.println("Esperando a resposta do cliente para o numero de discos.");
			String reClienteNumeroDiscos = dataIn.readUTF();

			
			if (verificarInteiro(reClienteNumeroDiscos) == false && reClienteNumeroDiscos.equalsIgnoreCase("Y"))
			{
				gameMenu = false;
				gameOn = false;
				System.out.println("O cliente desejou deixar o jogo.");
				dataOut.writeUTF("false:false:y");
				dataOut.flush();
			}
			else if (verificarVazio(reClienteNumeroDiscos)==false || verificarInteiro(reClienteNumeroDiscos) == false )
			{
				gameMenu = true;
				gameOn = false;
				System.out.println("O cliente inseriu um input incorreto.");
				dataOut.writeUTF("true:false:opInvalida");
				dataOut.flush();
			}
			else if (verificarInteiro(reClienteNumeroDiscos) == true && Integer.parseInt(reClienteNumeroDiscos) < 3)
			{
				gameMenu = true;
				gameOn = false;
				System.out.println("O cliente escolheu incorretamente o numero de discos.");

				dataOut.writeUTF("true:false:min3");
				dataOut.flush();
			}
			else if (verificarInteiro(reClienteNumeroDiscos) == true && Integer.parseInt(reClienteNumeroDiscos) > 10)
			{
				gameMenu = true;
				gameOn = false;
				System.out.println("O cliente escolheu incorretamente o numero de discos.");

				dataOut.writeUTF("true:false:max10");
				dataOut.flush();
			}			
			else if (verificarInteiro(reClienteNumeroDiscos) == true && Integer.parseInt(reClienteNumeroDiscos) >= 3
					&& Integer.parseInt(reClienteNumeroDiscos) <= 10)
			{

				gameOn = true;

				numeroDiscos = Integer.parseInt(reClienteNumeroDiscos);
				int numeroOtimo = (int) Math.pow(2, numeroDiscos) - 1;

				System.out.println("O cliente quer jogar com " + numeroDiscos + " discos");

				int seuMovimento = 0;

				String nOtimo = Integer.toString(numeroOtimo);

				dataOut.writeUTF("true:true:" + nOtimo);
				dataOut.flush();

				String reClienteEscolhaPinoInicial = "";
				String reClienteEscolhaPinoFinal = "";

				boolean pinoMenu = true;

				while (pinoMenu == true)
				{
					System.out.println("Esperando a resposta do cliente para o pino incial.");
					reClienteEscolhaPinoInicial = dataIn.readUTF();

					System.out.println("Esperando a resposta do cliente para o pino final.");
					reClienteEscolhaPinoFinal = dataIn.readUTF();

					ArrayList<String> listaPinos = new ArrayList<>(Arrays.asList("A", "B", "C", "a", "b", "c"));

					if (reClienteEscolhaPinoInicial.equalsIgnoreCase(reClienteEscolhaPinoFinal)
							&& listaPinos.contains(reClienteEscolhaPinoInicial))
					{
						pinoMenu = true;
						dataOut.writeUTF("true:pinosIguais");
						dataOut.flush();
						System.out.println("O pino inicial e final não podem ser o mesmo.");
					}

					else if (listaPinos.contains(reClienteEscolhaPinoInicial)
							&& listaPinos.contains(reClienteEscolhaPinoFinal))
					{
						pinoMenu = false;
						dataOut.writeUTF("false:pinosCorretos");
						dataOut.flush();
						System.out.println(
								"O cliente escolheu como pino inicial: " + reClienteEscolhaPinoInicial.toUpperCase()
										+ " e como pino final " + reClienteEscolhaPinoFinal.toUpperCase() + "\n");

					}
					else
					{
						pinoMenu = true;
						dataOut.writeUTF("true:pinosIncorretos");
						dataOut.flush();
						System.out.println("O pino inicial e/ou final foram inseridos incorretamente!");
					}
				}

				int pinoInicial = 0;
				int pinoFinal = 2;

				if (reClienteEscolhaPinoInicial.equalsIgnoreCase("a"))
				{
					pinoInicial = 0;
				}
				else if (reClienteEscolhaPinoInicial.equalsIgnoreCase("b"))
				{
					pinoInicial = 1;
				}
				else if (reClienteEscolhaPinoInicial.equalsIgnoreCase("c"))
				{
					pinoInicial = 2;
				}
				if (reClienteEscolhaPinoFinal.equalsIgnoreCase("a"))
				{
					pinoFinal = 0;
				}
				else if (reClienteEscolhaPinoFinal.equalsIgnoreCase("b"))
				{
					pinoFinal = 1;
				}
				else if (reClienteEscolhaPinoFinal.equalsIgnoreCase("c"))
				{
					pinoFinal = 2;
				}

				int[][] Matrix = criarMatrix(numeroDiscos, pinoInicial);

				System.out.println("********** O jogo começou ********** ");
				

				while (gameOn == true)
				{
					String tabuleiroString = printMatrix(Matrix); //mandar pro cliente a matriz a ser desenhada e os movimentos
					dataOut.writeUTF(seuMovimento+":"+tabuleiroString);
					dataOut.flush();

					int movimentoEscolhido;
					System.out.println("Esperando a jogada do cliente.");
					String movCliente = dataIn.readUTF(); // recebendo o movimento do cliente

					if (verificarInteiro(movCliente) == true && verificarVazio(movCliente)==true)
					{
						movimentoEscolhido = Integer.parseInt(movCliente);

						seuMovimento = seuMovimento + 1;

						if (movimentoEscolhido == 1 && verificarMovimento(Matrix, 0, 1) == true)
						{
							// A->B
							if (verificarPeca(Matrix, 0) == false)
							{
								System.out.println("O cliente inseriu o movimento A->B que é incorreto.");
								// o primeiro é pra verificar se há erro, e o segundo se há vitória
								seuMovimento = seuMovimento - 1;
								dataOut.writeUTF(
										"true:true:invalido");
								dataOut.flush();
							}
							else
							{
								System.out.println("O cliente inseriu o movimento A->B corretamente.");
								dataOut.writeUTF("true:true:valido");
								dataOut.flush();
								Matrix = movement(Matrix, 0, 1);
							}
						}
						else if (movimentoEscolhido == 2 && verificarMovimento(Matrix, 0, 2) == true)
						{
							// A->C
							if (verificarPeca(Matrix, 0) == false)
							{
								System.out.println("O cliente inseriu um movimento A->C que é incorreto.");

								seuMovimento = seuMovimento - 1;
								dataOut.writeUTF(
										"true:true:invalido");
								dataOut.flush();
							}
							else
							{
								System.out.println("O cliente inseriu o movimento A->C corretamente.");
								dataOut.writeUTF("true:true:valido");
								dataOut.flush();
								Matrix = movement(Matrix, 0, 2);
							}
						}
						else if (movimentoEscolhido == 3 && verificarMovimento(Matrix, 1, 0) == true)
						{
							// B->A
							if (verificarPeca(Matrix, 1) == false)
							{
								System.out.println("O cliente inseriu um movimento B->A que é incorreto.");

								seuMovimento = seuMovimento - 1;
								dataOut.writeUTF(
										"true:true:invalido");
								dataOut.flush();
							}
							else
							{
								System.out.println("O cliente inseriu o movimento B->A corretamente.");
								dataOut.writeUTF("true:true:valido");
								dataOut.flush();
								Matrix = movement(Matrix, 1, 0);
							}
						}
						else if (movimentoEscolhido == 4 && verificarMovimento(Matrix, 1, 2) == true)
						{
							// B->C
							if (verificarPeca(Matrix, 1) == false)
							{
								System.out.println("O cliente inseriu um movimento B->C que é incorreto.");

								seuMovimento = seuMovimento - 1;
								dataOut.writeUTF(
										"true:true:invalido");
								dataOut.flush();
							}
							else
							{
								System.out.println("O cliente inseriu o movimento B->C corretamente.");
								dataOut.writeUTF("true:true:valido");
								dataOut.flush();
								Matrix = movement(Matrix, 1, 2);
							}
						}
						else if (movimentoEscolhido == 5 && verificarMovimento(Matrix, 2, 0) == true)
						{
							// C->A
							if (verificarPeca(Matrix, 2) == false)
							{
								System.out.println("O cliente inseriu um movimento C->A que é incorreto.");
								seuMovimento = seuMovimento - 1;
								dataOut.writeUTF(
										"true:true:invalido");
								dataOut.flush();
							}
							else
							{
								System.out.println("O cliente inseriu o movimento C->A corretamente.");
								dataOut.writeUTF("true:true:valido");
								dataOut.flush();
								Matrix = movement(Matrix, 2, 0);
							}
						}
						else if (movimentoEscolhido == 6 && verificarMovimento(Matrix, 2, 1) == true)
						{
							// C->B
							if (verificarPeca(Matrix, 2) == false)
							{
								System.out.println("O cliente inseriu um movimento C->B que é incorreto.");

								seuMovimento = seuMovimento - 1;
								dataOut.writeUTF(
										"true:true:invalido");
								dataOut.flush();
							}
							else
							{
								System.out.println("O cliente inseriu o movimento C->B corretamente.");
								dataOut.writeUTF("true:true:valido");
								dataOut.flush();
								Matrix = movement(Matrix, 2, 1);
							}
						}
						else if (movimentoEscolhido > 6 || movimentoEscolhido < 1)
						{
							System.out.println("O cliente inseriu um movimento incorreto.");

							dataOut.writeUTF("true:true:opcoes");
							dataOut.flush();
							seuMovimento = seuMovimento - 1;
						}
						else
						{
							System.out.println("O cliente inseriu um movimento incorreto.");

							dataOut.writeUTF(
									"true:true:movIncorreto");
							dataOut.flush();
							seuMovimento = seuMovimento - 1;
						}

						String veriVitoriaMat = Boolean.toString(verificarVitoria(Matrix, pinoFinal));
						dataOut.writeUTF(veriVitoriaMat);
						dataOut.flush();

						if (verificarVitoria(Matrix, pinoFinal) == true)
						{
							tabuleiroString = printMatrix(Matrix);
							usuario.adicEstatistica(numeroDiscos, seuMovimento);
							gameMenu = false;
							gameOn = false;
							if (numeroOtimo == seuMovimento)
							{

								dataOut.writeUTF("false:false:comNO:"+seuMovimento+":"+tabuleiroString);
								dataOut.flush();
								System.out.println("O cliente ganhou com o número mínimo de jogadas!");
							}
							else
							{
								dataOut.writeUTF("false:false:semNO:" +seuMovimento+":"+tabuleiroString);		
								dataOut.flush();
								System.out.println("O cliente ganhou mas não com o número mínimo de jogadas!");
							}

						}

					}
					else if (verificarInteiro(movCliente) == false && movCliente.equalsIgnoreCase("Y"))
					{
						gameMenu = false;
						gameOn = false;
						dataOut.writeUTF("false:false:leftGame");
						dataOut.flush();
						// Pedido da vitória
						dataOut.writeUTF("false"); // seGanhou = false (está na parte do cliente essa variável)
						dataOut.flush();
						System.out.println("O cliente saiu do jogo");
					}
					else
					{
						gameOn = true;
						dataOut.writeUTF("true:true:tentNov");
						dataOut.flush();
						// Pedido da vitória
						dataOut.writeUTF("false");
						dataOut.flush();
						System.out.println("O cliente inseriu uma opção errada, esperando sua nova tentativa.");
					}
				}
			}
		}
	}

	public void menuFinal(serverUser usuario) throws IOException
	{
		boolean menuFinal = true;
		while (menuFinal == true)
		{
			System.out.println("====== O cliente abriu o menu. ======");

			String resposta = dataIn.readUTF();
			System.out.println("A resposta do cliente no menu é:" + resposta);
			
			if (resposta.equals("3"))
			{
				System.out.println("Seção encerrada");
				dataOut.writeUTF("false:SeçãoEencerrada");
				dataOut.flush();
				menuFinal = false;
				closeCLienteAtual();

			}
			else if (resposta.equals("2"))
			{
				System.out.println("Ver estatísticas");
				dataOut.writeUTF("true:VerEstatísticas");
				dataOut.flush();
				String estatisticas = usuario.printEstatisticas();
				dataOut.writeUTF(estatisticas);
				dataOut.flush();

			}
			else if (resposta.equals("1"))
			{
				System.out.println("Jogar Novamente");
				dataOut.writeUTF("true:JogarNovamente");
				dataOut.flush();
				jogo(usuario);

			}
			else
			{
				System.out.println("Input inválida");
				dataOut.writeUTF("true:InputInválida");
				dataOut.flush();

			}
		}
	}

	public static void main(String[] args) throws IOException
	{
		Server servidor = new Server(7000);

		while (true)
		{
			System.out.println("I am alone!");
			servidor.getClient(); // Verifica se há cliente
			System.out.println("Tenho um cliente!");
			servidor.iniciarListaUser();

			boolean loginMenu = true;

			while (loginMenu == true)
			{
				String login = servidor.getDataIn().readUTF();
				String password = servidor.getDataIn().readUTF();

				if (servidor.verificarCredenciais(login, password) == true)
				{
					loginMenu = false;
					System.out.println("O cliente inseriu as credenciais corretamente!");
					servidor.getDataOut().writeUTF("false:false");
					servidor.getDataOut().flush();

					// Cria um novo usuário
					serverUser usuario = new serverUser(login, password);

					servidor.jogo(usuario);
					servidor.menuFinal(usuario);
				}
				else
				{
					loginMenu = true;
					boolean tentNovamente = true;
					System.out.println("O cliente não inseriu as credenciais corretamente, aguardando!");

					while (tentNovamente == true)
					{
						servidor.getDataOut().writeUTF("true:true");
						servidor.getDataOut().flush();
						String tentarNovo = servidor.getDataIn().readUTF();

						if (tentarNovo.equalsIgnoreCase("y"))
						{
							servidor.getDataOut().writeUTF("true:false");
							servidor.getDataOut().flush();
							loginMenu = true;
							tentNovamente = false;
							System.out.println("O cliente deseja tentar novamente.");
						}
						else if (tentarNovo.equalsIgnoreCase("n"))
						{
							servidor.getDataOut().writeUTF("false:false");
							servidor.getDataOut().flush();
							loginMenu = false;
							tentNovamente = false;
							System.out.println("O cliente não deseja tentar novamente.");
						}
						else
						{
							System.out.println("O cliente inseriu um input incorretamente, aguardando nova tentativa.");
							loginMenu = true;
							tentNovamente = true;

						}
					}
				}
			}
		}
	}
}
