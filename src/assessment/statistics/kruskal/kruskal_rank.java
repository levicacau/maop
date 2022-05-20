import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileWriter; 
import java.io.File;

public class kruskal_rank {
	static double[] resultadosFinais;
	static String metric="";
	static String caminho="../../../results/up_to_20/titles.txt";
	static int[] objetivos={3,5,8,10,15,20};
// 	static int[] objetivos={3,5,8};
	public static void main(String[] args) throws IOException{
		int colunas=-1;
		if(args.length < 1){
			System.out.println("uso: kruskal_rank resultados");
			System.exit(1);
		}
		int fator=1; //fator a ser usado no caso do hyervolume, que quanto maior, melhor
		if(args[0].toLowerCase().contains("gd"))
			metric="GD";
		if(args[0].toLowerCase().contains("igd"))
			metric="IGD";
		if(args[0].toLowerCase().contains("r2"))
			metric="R2";
		if(args[0].toLowerCase().contains("hv")){
			metric="Hypervolume";
			fator=-1;
		}
		
		String saida="\n\\begin{table*}[t]\n \\center \n \\tiny \n \\caption{"+metric+" results} \n \\begin{tabular}{";
		for(int a=0;a<args.length;a++){
			if(a==0){
				BufferedReader tt = new BufferedReader(new FileReader(caminho));
				//BufferedReader tt = new BufferedReader(new FileReader("../../../results_primeira-parte/titles.txt"));
				String linha=tt.readLine();
				String[] titles = linha.split("\\s");
				colunas=titles.length;
				for(int i=0;i<colunas+2;i++){//gerar o tabular da tabela
					saida+="c|";
				}
				saida = saida.substring(0, saida.length()-1);
				saida+="}\n\\hline \n";
				saida+="Prob. & Obj. & ";
				for(int i=0;i<titles.length;i++)
					saida+=titles[i]+" & ";
					
				saida = saida.substring(0, saida.length()-2);
				saida+="\\\\ \\hline \n";
			}
			
			String problem="";
			if(args[a].toLowerCase().contains("dtlz1") )
				problem="DTLZ1";
			if(args[a].toLowerCase().contains("dtlz2") )
				problem="DTLZ2";
			if(args[a].toLowerCase().contains("dtlz3") )
				problem="DTLZ3";
			if(args[a].toLowerCase().contains("dtlz4") )
				problem="DTLZ4";
			if(args[a].toLowerCase().contains("dtlz5") )
				problem="DTLZ5";
			if(args[a].toLowerCase().contains("dtlz6") )
				problem="DTLZ6";
			if(args[a].toLowerCase().contains("dtlz7") )
				problem="DTLZ7";
			if(args[a].toLowerCase().contains("wfg1") )
				problem="WFG1";
			if(args[a].toLowerCase().contains("wfg2") )
				problem="WFG2";
			if(args[a].toLowerCase().contains("wfg3") )
				problem="WFG3";
			if(args[a].toLowerCase().contains("wfg4") )
				problem="WFG4";
			if(args[a].toLowerCase().contains("wfg5") )
				problem="WFG5";
			if(args[a].toLowerCase().contains("wfg6") )
				problem="WFG6";
			if(args[a].toLowerCase().contains("wfg7") )
				problem="WFG7";
			if(args[a].toLowerCase().contains("wfg8") )
				problem="WFG8";
			if(args[a].toLowerCase().contains("wfg9") )
				problem="WFG9";
			
			FileReader fr = new FileReader(args[a]);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<double[]> dados = new ArrayList<double[]>();
			resultadosFinais = new double[colunas];
			
			String tabela=problem;
// 			int[] objetivos={2,3,5,8,9};
			int objIdx=0;
	

			String linha = br.readLine();
			while (linha != null) {
				String[] objs = linha.split("\\s");
				if(objs.length==0)
					objs = linha.split("\t");
				if ( objs.length>0 && (objs.length != 1 || !(objs[0].equals(""))) && !objs[0].equals("#") ) {
					double[] d = new double[objs.length];
					for (int i = 0; i < objs.length; i++) {
						d[i] = Double.parseDouble(objs[i].replace(",","."))*fator;
					}
					dados.add(d);
				} else {
					if(dados.size() > 0){
						tabela+=" & "+objetivos[objIdx++]+" & "+operacoes(dados);
					}
					dados.clear();
				}
				linha = br.readLine();
			}
			if(dados.size() > 0)
				tabela+=" & "+objetivos[objIdx++]+" & "+operacoes(dados);
				
				

			
			saida+=tabela;
			saida+="\\hline \n";
			
			
// 			double menorValor=Double.MAX_VALUE;
// 			for(int j=0;j<colunas;j++)
// 				if(resultadosFinais[j] < menorValor)
// 					menorValor=resultadosFinais[j];
// 			saida+=" & Sum & ";
// 			for(int j=0;j<colunas;j++)
// 				if(resultadosFinais[j] == menorValor)
// 					saida+="\\textbf{"+format(resultadosFinais[j])+"} & ";
// 				else
// 					saida+=format(resultadosFinais[j])+" & ";
// 			saida = saida.substring(0, saida.length()-2);
// 			saida+="\\\\\n";
// 			saida+="\\hline \n";
			
		}
		saida+="\\end{tabular}\n\\label{results}\n\\end{table*}";
		System.out.println(saida);
	}
	public static String format(double valor){
		return String.format("%.2f", valor).replace(",",".").replace("e","E");
	}
	
	public static String operacoes(ArrayList<double[]> dados) throws IOException {
		int colunas=dados.get(0).length;
		String saida="";
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

		matriz=execute(dados);

		for(int i=0;i<ranks.length;i++){
			for(int j=0;j<ranks[0].length;j++){
				avgRanks[j]+=ranks[i][j];
			}
 		}
 		for(int i=0;i<avgRanks.length;i++)
			avgRanks[i]/=ranks.length; //kruskal
 		
		//ranqueia a media dos ranks
		indMenor=-1;
		menorValor=Double.MAX_VALUE;
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
		contRank=1;
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
		
		
		menorValor=Double.MAX_VALUE;
		for(int j=0;j<colunas;j++)
			if(ranksFinais[j] < menorValor)
				menorValor=ranksFinais[j];
 		
		for(int j=0;j<colunas;j++){
			if(ranksFinais[j] == menorValor)
				saida+="\\cellcolor{gray!30}\\textbf{"+format(avgRanks[j])+" ("+format(ranksFinais[j])+")"+"} & ";
			else
				saida+=format(avgRanks[j])+" ("+format(ranksFinais[j])+")"+" & ";
			
			
			resultadosFinais[j]+=ranksFinais[j];
		}		
		saida = saida.substring(0, saida.length()-2);
 		saida+="\\\\\n";
 		return saida;
	
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
public static boolean[][] execute(ArrayList<double[]> dados) throws IOException{
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
					
					
// 					criticalDifference=Double.parseDouble(line.split("\\s+")[2]);
				}
			}
			
// 			for(int i=0;i<marcar.length;i++){
// 				for(int j=0;j<marcar.length;j++)
// 					System.out.print(marcar[i][j]+"\t");
// 				System.out.println();
// 			}
// 			System.out.println();
			
			File file = new File("temp.txt");
			if(!file.delete())
				System.out.println("Temp file delete operation failed.");
		}catch (Exception e){	e.printStackTrace();}
		return marcar;
	}
}
