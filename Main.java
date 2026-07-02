import java.util.*;

public class Main {

	static char[] fita;
	static int posicao;
	static String estadoAtual;
	static List<String[]> transicoes = new ArrayList<String[]>();
	static Set<String> finais = new HashSet<String>();
	static int contadorPassos = 0;

    public static void main(String[] args) {

		inicializarTransicoes();
		finais.add("qf");

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
			String[] transicaoEncontrada = buscarTransicao(estadoAtual, simboloLido);

			if (transicaoEncontrada == null) {
				System.out.println("nao ha transicao para estado " + estadoAtual + " e simbolo " + simboloLido);
				rodando = false;
			} else {
				rodando = executarPasso(transicaoEncontrada);
				System.out.println("passo " + contadorPassos + " -> estado=" + estadoAtual + " pos=" + posicao);
				imprimeFita();
			}
		}

		verificarAceitacao();
    }

	public static void inicializarTransicoes() {
		transicoes.add(new String[]{"q0","0","q0","0","D"});
		transicoes.add(new String[]{"q0","1","q0","1","D"});
		transicoes.add(new String[]{"q0","_","q1","_","E"});
		transicoes.add(new String[]{"q1","1","q1","0","E"});
		transicoes.add(new String[]{"q1","0","qf","1","P"});
		transicoes.add(new String[]{"q1","_","qf","1","P"});
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
		estadoAtual = "q0";
	}

	public static String[] buscarTransicao(String estado, char simbolo) {
		for (int i = 0; i < transicoes.size(); i++) {
			String[] regra = transicoes.get(i);
			if (regra[0].equals(estado) && regra[1].charAt(0) == simbolo) {
				return regra;
			}
		}
		return null;
	}

	public static boolean executarPasso(String[] regra) {
		fita[posicao] = regra[3].charAt(0);
		estadoAtual = regra[2];

		if (regra[4].equals("D")) {
			posicao = posicao + 1;
		}
		if (regra[4].equals("E")) {
			posicao = posicao - 1;
		}
		if (regra[4].equals("P")) {
			return false;
		}
		return true;
	}

	public static void verificarAceitacao() {
		if (finais.contains(estadoAtual)) {
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

	public static void imprimeFita(){
		String s = "";
		for (int i = 30; i < 55; i++) {
			s += fita[i];
		}
		System.out.println(s);
		String marcador="";
		for(int i=30;i<55;i++){
			if(i==posicao) marcador+="^"; else marcador+=" ";
		}
		System.out.println(marcador);
	}
}