import java.util.*;

public class Main {

	static char[] f; // fita
	static int p; // posicao
	static String est; //estado atual
	static List<String[]> t = new ArrayList<String[]>();
	static Set<String> finais = new HashSet<String>();
	static int cont = 0;

    public static void main(String[] args) {

		// maquina que incrementa um numero binario em 1
		t.add(new String[]{"q0","0","q0","0","D"});
		t.add(new String[]{"q0","1","q0","1","D"});
		t.add(new String[]{"q0","_","q1","_","E"});
		t.add(new String[]{"q1","1","q1","0","E"});
		t.add(new String[]{"q1","0","qf","1","P"});
		t.add(new String[]{"q1","_","qf","1","P"});

		finais.add("qf");

		String entrada = "1011";
		if(args.length>0){
			entrada=args[0];
		}

		f = new char[100];
		for(int i=0;i<f.length;i++){
			f[i]='_';
		}
		for(int i=0;i<entrada.length();i++){
			f[i+40]=entrada.charAt(i);
		}

		p=40;
		est="q0";

		System.out.println("Fita inicial:");
		imprimeFita();

		boolean rodando=true;
		while(rodando){
			cont++;
			if(cont>1000){
				System.out.println("deu ruim, muitas iteracoes");
				break;
			}

			char simb = f[p];
			String[] trans = null;
			for(int i=0;i<t.size();i++){
				String[] linha = t.get(i);
				if(linha[0].equals(est) && linha[1].charAt(0)==simb){
					trans=linha;
					break;
				}
			}

			if(trans==null){
				System.out.println("nao ha transicao para estado "+est+" e simbolo "+simb);
				rodando=false;
			}else{

				f[p]=trans[3].charAt(0);
				est=trans[2];

				if(trans[4].equals("D")){
					p=p+1;
				}
				if(trans[4].equals("E")){
					p=p-1;
				}
				if(trans[4].equals("P")){
					rodando=false;
				}

				System.out.println("passo "+cont+" -> estado="+est+" pos="+p);
				imprimeFita();
			}
		}

		if(finais.contains(est)){
			System.out.println("ACEITA");
		}else{
			System.out.println("REJEITA (ou parou sem chegar em estado final)");
		}

		System.out.println("resultado final:");
		String res="";
		for(int i=0;i<f.length;i++){
			if(f[i]!='_')res=res+f[i];
		}
		System.out.println(res);
    }

	public static void imprimeFita(){
		String s = "";
		for (int i = 30; i < 55; i++) {
			s += f[i];
		}
		System.out.println(s);
		String marcador="";
		for(int i=30;i<55;i++){
			if(i==p) marcador+="^"; else marcador+=" ";
		}
		System.out.println(marcador);
	}

	// funcao que verifica se string so tem 0 e 1, feita rapido e nunca usada direito
	public static boolean verificaBinario(String x){
		for(int i=0;i<x.length();i++){
			char c = x.charAt(i);
			if(c!='0' && c!='1'){
				return false;
			}
		}
		return true;
	}
}