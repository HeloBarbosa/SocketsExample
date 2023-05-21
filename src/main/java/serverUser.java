package trabalho3;

import java.util.*;

public class serverUser
{
	private String nome;
	private String password;

	private Map<Integer, List<Double>> dictEstatisticas = new HashMap<Integer, List<Double>>();

	public serverUser(String nome, String password)
	{
		this.nome = nome;
		this.password = password;
	
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Map<Integer, List<Double>> getDictEstatisticas()
	{
		return dictEstatisticas;
	}

	public void setDictEstatisticas(Map<Integer, List<Double>> dictEstatisticas)
	{
		this.dictEstatisticas = dictEstatisticas;
	}

	public void adicEstatistica(int numeroDiscos, int quantMovimentosJogo)
	{
		double quantMovimentos = (double) quantMovimentosJogo;

		ArrayList<Double> listMovimentos = new ArrayList<Double>();

		if (this.dictEstatisticas.containsKey(numeroDiscos))
		{
			for (int key : this.dictEstatisticas.keySet())
			{
				if (numeroDiscos == key)
				{
					double movimentosAntigo = this.dictEstatisticas.get(key).get(0);
					double numeroJogadasAntigo = this.dictEstatisticas.get(key).get(1);
					this.dictEstatisticas.get(key).set(1, numeroJogadasAntigo + 1);
					double media = ((double) movimentosAntigo * numeroJogadasAntigo + (double) quantMovimentos)
							/ ((double) numeroJogadasAntigo + 1);
					this.dictEstatisticas.get(key).set(0, media);
				}
			}
		}
		else
		{
			listMovimentos.add(quantMovimentos);
			listMovimentos.add(1.0);
			this.dictEstatisticas.put(numeroDiscos, listMovimentos);
		}
	}

	public String printEstatisticas()
	{
		String estatisticas = "";
		if (this.dictEstatisticas.size() == 0)
		{
			estatisticas += "null:0";
		}
		else
		{
			for (int key : this.dictEstatisticas.keySet())
			{
				estatisticas += "est"+":"+ key+":"+this.dictEstatisticas.get(key).get(0)+":"
				+ this.dictEstatisticas.get(key).get(1);
			}
		}
		return estatisticas;
	}

}