import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;

public class kruskal_rank_full{
	public static void println(String x){System.out.println(x);}
	public static void println(){System.out.println();}
	static String caminho="../../results/";
	//static String caminho="../../../results_primeira-parte/";
	static String table[][];
	static double[][] matrizGeral;
	static double criticalDifference=0;

	public static void main(String[] args) throws IOException{
		//String objectives[]={"2","3","5","10","15","20"};
		String objectives[]={"2","3","5","8","10"};
// 		String objectives[]={"3","5","8"};
		//String metrics[] = {"$GD_p$", "$IGD_p$", "$R_2$", "Hypervolume"};
		//String metrics[] = {"$IGD_p$"};
 		String metrics[]={"$IGD_p$", "Hypervolume"};
		//String[] problems={"dtlz1", "dtlz2", "dtlz3", "dtlz4", "dtlz5", "dtlz6", "dtlz7", "wfg1", "wfg2", "wfg3", "wfg4", "wfg5", "wfg6", "wfg7", "wfg8", "wfg9"};
 		String[] problems={"dtlz1", "dtlz2", "dtlz3", "dtlz4", "dtlz5", "dtlz6", "dtlz7"};
 		//String[] problems={"wfg1", "wfg2", "wfg3", "wfg4", "wfg5", "wfg6", "wfg7", "wfg8", "wfg9"};
// 		String[] problems={"dtlz2"};

		String[] titles=readTitles();

		for(int m=0;m<metrics.length;m++){
			String metric=metrics[m];

			table = new String[1+(titles.length*objectives.length)][2+problems.length];
			matrizGeral = new double[problems.length*objectives.length][titles.length];
			for(int i=0;i<table.length;i++) //inicializa a tabela
				for(int j=0;j<table[i].length;j++)
					table[i][j]="";

			//montando as colunas laterais da tabela
			table[0][0]="Obj.";
			table[0][1]="Algorithms";
			for(int i=1;i<table.length;i++){ //percorrendo as linhas
				if((i-1)%titles.length==0)
					table[i][0]=objectives[(i-1)/titles.length];
				table[i][1]=titles[(i-1)%titles.length];
			}
			for(int j=2;j<table[0].length;j++) //montando o cabecalho da tabela
					table[0][j]=problems[(j-2)].toUpperCase();

			//montando o cabecalho da tabela
			String saida="\\begin{table*}[t]\n \\center \n %\\tiny \n \\caption{"+metric+" results} \n \\begin{tabular}{";
			for(int i=0;i<table[0].length;i++){//gerar o tabular da tabela
				saida+="c|";
			}
			saida = saida.substring(0, saida.length()-1);
			saida+="}\n\\hline";

			//obtendo os valores do centro da tabela
			for(int j=2;j<table[0].length;j++){
				String[][] matriz=readData(titles.length, objectives.length, problems[(j-2)], metric, j-2);
				for(int i=1;i<table.length;i++){
					int linha=(i-1)/titles.length;
					int coluna=(i-1)%titles.length;
					table[i][j]=matriz[linha][coluna];
				}
			}
	// 		//double[] avgRanks=new double[objectives.length*titles.length];
	// 		double[] avgRanks=new double[titles.length];
	// 		for(int i=0;i<matrizGeral.length;i++){
	// 			for(int j=0;j<matrizGeral[i].length;j++)
	// 				//avgRanks[((i%objectives.length)*titles.length)+j]+=matrizGeral[i][j];
	// 				avgRanks[j]+=matrizGeral[i][j];
	// 		}
	// 		boolean[][] fdr=execute_fdr(matrizGeral);
	// 		double[] ranksFinais=rankStatistics(avgRanks, fdr);
	// 		for(int i=0;i<ranksFinais.length;i++){
	// 				System.out.print(avgRanks[i]+"("+ranksFinais[i]+") ");
	// 		}

			System.out.println("(crit. Diff: "+criticalDifference+")");
			//montando o final da tabela
			println("\n"+saida);
			for(int i=0;i<table.length;i++){
				for(int j=0;j<table[i].length;j++){
					if(i == 0 && j> 1){
						System.out.print("&"+table[i][j]);

					}else{
						if(j!=0)
							System.out.print("&"+table[i][j]);
						else
							System.out.print(table[i][j]);
					}
				}
				if((i)%titles.length==0)
					println("\\\\ \\hline");
				else
				println("\\\\");
			}
			println("\\end{tabular}\n\\label{results}\n\\end{table*}");
		}//end metrics loop
	}

	public static String[] readTitles()throws IOException{
		Scanner scanner = new Scanner( new FileInputStream( caminho+"titles.txt" ) );
		String value = scanner.nextLine().trim();
		String titles[]=null;
		if (value.isEmpty()) {
			println("Titles file not set");
			System.exit(1);
		}else{
			titles=value.split(" ");
		}
		return titles;
	}
	public static String[][] readData(int algorithms, int objectiveNumber, String problem, String metric, int index)throws IOException{ //
		ArrayList<double[]> dados = new ArrayList<double[]>();
		String retorno[][] = new String[objectiveNumber][algorithms];

		int i=0;
		Scanner scanner=null;
		int fator=1; //fator a ser usado no caso do hyervolume, que quanto maior, melhor
		if(metric.toUpperCase().equals("$GD_P$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-gdp.txt" ) );
		if(metric.toUpperCase().equals("$IGD_P$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-igd.txt" ) );
		if(metric.toUpperCase().equals("$R_2$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-r2.txt" ) );
		if(metric.toUpperCase().equals("HYPERVOLUME")){
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-hv.txt" ) );
			fator=-1;
		}
		while(scanner.hasNextLine()){
			String value=scanner.nextLine().trim();
			if(!value.isEmpty())
				dados.add(lineToDoubleVector(value, fator));
			else{
				if(dados.size() > 0){
					retorno[i]=operacoes(dados,((index)*objectiveNumber)+i);
					//iguais[j]=execute(dados);
					i++;
					dados.clear();
				}
			}
		}
		if(dados.size() > 0){
			retorno[i]=operacoes(dados,i);
		}

		System.out.printf(" ");
		return retorno;
	}
	public static String[] operacoes(ArrayList<double[]> dados, int index) throws IOException {
		int colunas=dados.get(0).length;
		String saida[]= new String[colunas];
		boolean[][] matriz = new boolean[colunas][colunas];
		double ranks[][] = new double[dados.size()][colunas];
		double avgRanks[] = new double[colunas];
		double tamTotal=dados.size()*colunas;
		ArrayList<Double> lista = new ArrayList<Double>();
		int indMenor=-1;
		double menorValor=Double.MAX_VALUE;
		int contRank=1;
		//ranqueia considerando todas as 30 instancias (N=300)
		while(true){
			for(int i=0;i<tamTotal;i++){
				double valor=dados.get(i/colunas)[i%colunas];
				if(valor < menorValor && !(listado(lista,valor)) ){
					menorValor=valor;
					indMenor=i;
				}
			}
			if(indMenor==-1)
				break;
			lista.add(menorValor);
			indMenor=-1;
			menorValor=Double.MAX_VALUE;
		}
		contRank=1;
		for(int i=0;i<lista.size();i++){
			double rankAtual=0;
			ArrayList<Integer> valores=encontraValores(dados, lista.get(i));
			for(int j=0;j<valores.size();j++)
				rankAtual+=contRank++;
			rankAtual/=(double)valores.size();
			for(int j=0;j<valores.size();j++){
				ranks[valores.get(j)/colunas][valores.get(j)%colunas]=rankAtual;
			}
		}

 		matriz=execute_ksk(dados);

		for(int i=0;i<ranks.length;i++){
			for(int j=0;j<ranks[0].length;j++){
				avgRanks[j]+=ranks[i][j];
				//System.out.print(format(ranks[i][j])+"\t");
			}
			//System.out.println();
 		}
 		for(int i=0;i<avgRanks.length;i++){
			avgRanks[i]/=ranks.length; //kruskal
			//System.out.print(format(avgRanks[i])+"\t");
		}
		matrizGeral[index]=avgRanks;

		double[] ranksFinais=rankStatistics(avgRanks, matriz);

		menorValor=Double.MAX_VALUE;
		for(int j=0;j<colunas;j++)
			if(ranksFinais[j] < menorValor)
				menorValor=ranksFinais[j];

		for(int j=0;j<colunas;j++){
			if(ranksFinais[j] == menorValor)
				saida[j]="\\cellcolor{gray!30}\\textbf{"+format(avgRanks[j])+" ("+format(ranksFinais[j])+")"+"}";
			else
				saida[j]=format(avgRanks[j])+" ("+format(ranksFinais[j])+")";
		}
 		return saida;

	}
	static double[] rankStatistics(double[] avgRanks, boolean[][] matriz){
		//ranqueia a media dos ranks
		int colunas=matriz.length;
		int indMenor=-1;
		double menorValor=Double.MAX_VALUE;
		ArrayList<Integer> listaInt = new ArrayList<Integer>();
		while(true){
			for(int j=0;j<colunas;j++){
				double valor=avgRanks[j];
				if(valor < menorValor && !(listado(listaInt,j)) ){
					menorValor=valor;
					indMenor=j;
				}
			}
			if(indMenor==-1)
				break;
			listaInt.add(indMenor);
			indMenor=-1;
			menorValor=Double.MAX_VALUE;
		}
		int contRank=1;
		double[] ranksFinais = new double[colunas];
		for(int l=0;l<listaInt.size();l++){
			double rankAtual=0;
			ArrayList<Integer> valores=encontraValores(listaInt.get(l), matriz);
			for(int v=0;v<valores.size();v++){
				rankAtual+=rankDoAlg(listaInt,valores.get(v));
			}
			rankAtual/=(double)valores.size();
			ranksFinais[listaInt.get(l)]=rankAtual;
		}
		listaInt.clear();
		return ranksFinais;
	}
	static boolean listado(ArrayList<Double> lista, double valor){
		boolean listado=false;
		for(int i=0;i<lista.size();i++)
			if(lista.get(i) == valor)
				listado=true;
		return listado;
	}
	static boolean listado(ArrayList<Integer> lista, int valor){
		boolean listado=false;
		for(int i=0;i<lista.size();i++)
			if(lista.get(i) == valor)
				listado=true;
		return listado;
	}
	static int rankDoAlg(ArrayList<Integer> list, int alg){
		for(int i=0;i<list.size();i++)
			if(alg == list.get(i))
				return i+1;
		System.out.println("Erro!");
		System.exit(1);
		return -1;
	}
	static ArrayList<Integer> encontraValores(ArrayList<double[]> dados, double valor){
		int colunas=dados.get(0).length;
		double tamTotal=dados.size()*colunas;
		ArrayList<Integer> retorno = new ArrayList<Integer>();
		for(int i=0;i<tamTotal;i++){
			double tab=dados.get(i/colunas)[i%colunas];
			if(tab == valor)
				retorno.add(i);
		}
		return retorno;
	}
	static ArrayList<Integer> encontraValores(ArrayList<double[]> dados, double valor, int linha){
		ArrayList<Integer> retorno = new ArrayList<Integer>();
		for(int j=0;j<dados.get(linha).length;j++){
			if(dados.get(linha)[j] == valor)
				retorno.add(j);
		}
		return retorno;
	}
	static ArrayList<Integer> encontraValores(int valor, boolean[][] matriz){
		ArrayList<Integer> retorno = new ArrayList<Integer>();
		for(int j=0;j<matriz.length;j++){
			if(matriz[valor][j] == false)
				retorno.add(j);
		}
		return retorno;
	}

	static double[] calcularMedias(ArrayList<double[]> dados){
		double[] medias = new double[dados.get(0).length];
		Arrays.fill(medias,0);
		for(int i=0;i<dados.get(0).length;i++){
			for(int j=0;j<dados.size();j++){
				medias[i]+=dados.get(j)[i];
			}
			medias[i]/=dados.size();
		}
		return medias;
	}
	public static boolean[][] execute_ksk(ArrayList<double[]> dados) throws IOException{
		try (FileWriter entrada = new FileWriter("temp.txt") ) {
			for(int i=0;i<dados.size();i++){
				for(int j=0;j<dados.get(i).length;j++){
					entrada.write(dados.get(i)[j]+" ");
				}
				entrada.write("\n");
			}
		}
		boolean[][] marcar = new boolean[dados.get(0).length][dados.get(0).length];
		try {
			String comando="java ksk_full temp.txt";
// 			String comando="java -cp /home/olacir/Dropbox/UFPR/Doutorado/mopso/assessment/statistics/kruskal ksk_full temp.txt";
 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
 			p.waitFor();
 			System.out.print(".");
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ( (line=br.readLine()) != null) {
// 			System.out.println(line);
// 				if(line.split("\\s+")[0].split("-").length == 2 && line.split("-").length == 2){
				if(line.split("\\s+")[0].split("-").length == 2 && line.split("-")[0].length() < 5){
// 					System.out.println(line);
					String[] ln=line.split("\\s+");
// 					for(int i=0;i<ln.length;i++)
// 						System.out.print(ln[i]+"\t");
					int l=Integer.parseInt(line.split("\\s+")[0].split("-")[0])-1;
					int c=Integer.parseInt(line.split("\\s+")[0].split("-")[1])-1;
					marcar[c][l]=Boolean.parseBoolean(ln[ln.length-1]);
					marcar[l][c]=Boolean.parseBoolean(ln[ln.length-1]);

// 					System.out.println(ln[ln.length-1]+"--"+marcar[c][l]+"("+(l+1)+","+(c+1)+")");


					criticalDifference=Double.parseDouble(line.split("\\s+")[2]);
				}
			}

// 			for(int i=0;i<marcar.length;i++){
// 				for(int j=0;j<marcar.length;j++)
// 					System.out.print(marcar[i][j]+"\t");
// 				System.out.println();
// 			}
// 			System.out.println();

			File file = new File("temp.txt");
//			if(!file.delete())
//				System.out.println("Temp file delete operation failed.");
		}catch (Exception e){	e.printStackTrace();}
		return marcar;
	}
	public static boolean[][] execute_fdr(double[][] dados) throws IOException{
		try (FileWriter entrada = new FileWriter("temp.txt") ) {
			for(int i=0;i<dados.length;i++){
				for(int j=0;j<dados[i].length;j++){
					entrada.write(dados[i][j]+" ");
				}
				entrada.write("\n");
			}
		}
		boolean[][] marcar = new boolean[dados[0].length][dados[0].length];
		try {
			String comando="java ksk_full temp.txt";
 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
 			p.waitFor();
 			System.out.print(".");
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ( (line=br.readLine()) != null) {
				if(line.split(" ")[0].split("-").length == 2 && line.split("-").length == 2){
					String[] ln=line.split(" ");
					int l=Integer.parseInt(line.split(" ")[0].split("-")[0])-1;
					int c=Integer.parseInt(line.split(" ")[0].split("-")[1])-1;
					marcar[c][l]=Boolean.parseBoolean(ln[ln.length-1]);
					marcar[l][c]=Boolean.parseBoolean(ln[ln.length-1]);
				}
			}

			File file = new File("temp.txt");
			if(!file.delete())
				System.out.println("Temp file delete operation failed.");
		}catch (Exception e){	e.printStackTrace();}
		return marcar;
	}
	public static double[] lineToDoubleVector(String line, int fator)throws IOException{
		String campos[]=line.split(" ");
		double[] saida = new double[campos.length];
		for(int j=0;j<campos.length;j++)
			saida[j]=Double.parseDouble(campos[j])*fator;
		return saida;
	}
	public static String format(double valor){
		return String.format("%.2f", valor).replace(",",".").replace("e","E");
	}
}
