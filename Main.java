import java.util.*;

/**
 * Representa um estado da Maquina de Turing.
 * Cada estado sabe processar seu proprio simbolo lido e decidir a transicao (padrao State).
 */
interface EstadoMaquina {
	ResultadoTransicao processar(char simboloLido);
	boolean isFinal();
	String getNome();
}

/** Encapsula o resultado de uma transicao: o que escrever, para onde ir e a direcao do movimento. */
class ResultadoTransicao {
	char simboloParaEscrever;
	EstadoMaquina proximoEstado;
	char direcao; // 'D' = direita, 'E' = esquerda, 'P' = parar

	public ResultadoTransicao(char simboloParaEscrever, EstadoMaquina proximoEstado, char direcao) {
		this.simboloParaEscrever = simboloParaEscrever;
		this.proximoEstado = proximoEstado;
		this.direcao = direcao;
	}
}

/** Estado inicial: percorre a fita para a direita ate encontrar o fim do numero binario. */
class EstadoQ0 implements EstadoMaquina {
	private EstadoMaquina estadoQ1;

	public EstadoQ0(EstadoMaquina estadoQ1) {
		this.estadoQ1 = estadoQ1;
	}

	public ResultadoTransicao processar(char simboloLido) {
		if (simboloLido == '0') return new ResultadoTransicao('0', this, 'D');
		if (simboloLido == '1') return new ResultadoTransicao('1', this, 'D');
		if (simboloLido == '_') return new ResultadoTransicao('_', estadoQ1, 'E');
		return null;
	}

	public boolean isFinal() { return false; }
	public String getNome() { return "q0"; }
}

/** Estado de incremento: percorre a fita para a esquerda somando 1 em binario, com vai-um. */
class EstadoQ1 implements EstadoMaquina {
	private EstadoMaquina estadoFinal;

	public EstadoQ1(EstadoMaquina estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public ResultadoTransicao processar(char simboloLido) {
		if (simboloLido == '1') return new ResultadoTransicao('0', this, 'E');
		if (simboloLido == '0') return new ResultadoTransicao('1', estadoFinal, 'P');
		if (simboloLido == '_') return new ResultadoTransicao('1', estadoFinal, 'P');
		return null;
	}

	public boolean isFinal() { return false; }
	public String getNome() { return "q1"; }
}

/** Estado final de aceitacao: a maquina para ao chegar aqui. */
class EstadoFinal implements EstadoMaquina {
	public ResultadoTransicao processar(char simboloLido) {
		return null; // nunca deveria ser chamado: a maquina para (direcao 'P') antes de chegar aqui
	}

	public boolean isFinal() { return true; }
	public String getNome() { return "qf"; }
}

public class Main {

	static char[] fita;
	static int posicao;
	static EstadoMaquina estadoAtual;
	static int contadorPassos = 0;

	public static void main(String[] args) {

		estadoAtual = construirEstados();

		String entrada = lerEntrada(args);
		inicializarFita(entrada);

		System.out.println("Fita inicial:");
		imprimeFita();

		boolean rodando = true;
		while (rodando) {
			contadorPassos++;
			if (contadorPassos > 1000) {
				System.out.println("deu ruim, muitas iteracoes");
				break;
			}

			char simboloLido = fita[posicao];
			ResultadoTransicao resultado = estadoAtual.processar(simboloLido);

			if (resultado == null) {
				System.out.println("nao ha transicao para estado " + estadoAtual.getNome() + " e simbolo " + simboloLido);
				rodando = false;
			} else {
				fita[posicao] = resultado.simboloParaEscrever;
				estadoAtual = resultado.proximoEstado;

				if (resultado.direcao == 'D') posicao++;
				if (resultado.direcao == 'E') posicao--;
				if (resultado.direcao == 'P') rodando = false;

				System.out.println("passo " + contadorPassos + " -> estado=" + estadoAtual.getNome() + " pos=" + posicao);
				imprimeFita();
			}
		}

		verificarAceitacao();
	}

	/** Monta o grafo de estados da maquina (substitui a antiga lista de transicoes em String[]). */
	public static EstadoMaquina construirEstados() {
		EstadoMaquina qf = new EstadoFinal();
		EstadoMaquina q1 = new EstadoQ1(qf);
		EstadoMaquina q0 = new EstadoQ0(q1);
		return q0;
	}

	public static String lerEntrada(String[] args) {
		String entrada = "1011";
		if (args.length > 0) {
			entrada = args[0];
		}
		return entrada;
	}

	public static void inicializarFita(String entrada) {
		fita = new char[100];
		for (int i = 0; i < fita.length; i++) {
			fita[i] = '_';
		}
		for (int i = 0; i < entrada.length(); i++) {
			fita[i + 40] = entrada.charAt(i);
		}
		posicao = 40;
	}

	public static void verificarAceitacao() {
		if (estadoAtual.isFinal()) {
			System.out.println("ACEITA");
		} else {
			System.out.println("REJEITA (ou parou sem chegar em estado final)");
		}

		System.out.println("resultado final:");
		String resultado = "";
		for (int i = 0; i < fita.length; i++) {
			if (fita[i] != '_') resultado = resultado + fita[i];
		}
		System.out.println(resultado);
	}

	public static void imprimeFita() {
		String trechoFita = "";
		String marcador = "";
		for (int i = 30; i < 55; i++) {
			trechoFita += fita[i];
			marcador += (i == posicao) ? "^" : " ";
		}
		System.out.println(trechoFita);
		System.out.println(marcador);
	}
}