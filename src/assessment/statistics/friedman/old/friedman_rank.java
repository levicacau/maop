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

public class friedman_rank {
	static double[] resultadosFinais;
	public static void main(String[] args) throws IOException{
		int colunas=-1;
		if(args.length < 1){
			System.out.println("uso: friedman resultados");
			System.exit(1);
		}
		String saida="\n\\begin{table*}[t]\n \\center \n \\tiny \n \\caption{results} \n \\begin{tabular}{";		
		for(int a=0;a<args.length;a++){
			if(a==0){
				//BufferedReader tt = new BufferedReader(new FileReader("../../../results/titles.txt"));
				BufferedReader tt = new BufferedReader(new FileReader("../../../results_primeira-parte/titles.txt"));
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
			
			FileReader fr = new FileReader(args[a]);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<double[]> dados = new ArrayList<double[]>();
			resultadosFinais = new double[colunas];
			
			String tabela=problem;
			int[] objetivos={2,3,5,10,15,20};
			int objIdx=0;
	

			String linha = br.readLine();
			while (linha != null) {
				String[] objs = linha.split("\\s");
				if(objs.length==0)
					objs = linha.split("\t");
				if ( objs.length>0 && (objs.length != 1 || !(objs[0].equals(""))) && !objs[0].equals("#") ) {
					double[] d = new double[objs.length];
					for (int i = 0; i < objs.length; i++) {
						d[i] = Double.parseDouble(objs[i].replace(",","."));
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
			
			
			double menorValor=Double.MAX_VALUE;
			for(int j=0;j<colunas;j++)
				if(resultadosFinais[j] < menorValor)
					menorValor=resultadosFinais[j];
			saida+=" & Sum & ";
			for(int j=0;j<colunas;j++)
				if(resultadosFinais[j] == menorValor)
					saida+="\\textbf{"+format(resultadosFinais[j])+"} & ";
				else
					saida+=format(resultadosFinais[j])+" & ";
			saida = saida.substring(0, saida.length()-2);
			saida+="\\\\\n";
			saida+="\\hline \n";
			
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
		
		//ranqueia considerando somente as linhas
		for(int i=0;i<dados.size();i++){
			indMenor=-1;
			menorValor=Double.MAX_VALUE;
			while(true){
				for(int j=0;j<colunas;j++){
					double valor=dados.get(i)[j];
					if(valor < menorValor && !(listado(lista,valor)) ){
						menorValor=valor;
						indMenor=j;
					}
				}
				if(indMenor==-1)
					break;
				lista.add(menorValor);
				indMenor=-1;
				menorValor=Double.MAX_VALUE;
			}
			int contRank=1;
			for(int l=0;l<lista.size();l++){
				double rankAtual=0;
				ArrayList<Integer> valores=encontraValores(dados, lista.get(l), i);
				for(int v=0;v<valores.size();v++)
					rankAtual+=contRank++;
				rankAtual/=(double)valores.size();
				for(int v=0;v<valores.size();v++){
					ranks[i][valores.get(v)]=rankAtual;
				}
			}
			lista.clear();
		}
		
// 		//ranqueia considerando todas as 30 instancias (N=300)
// 		while(true){
// 			for(int i=0;i<tamTotal;i++){
// 				double valor=dados.get(i/colunas)[i%colunas];
// 				if(valor < menorValor && !(listado(lista,valor)) ){
// 					menorValor=valor;
// 					indMenor=i;
// 				}
// 			}
// 			if(indMenor==-1)
// 				break;
// 			lista.add(menorValor);
// 			indMenor=-1;
// 			menorValor=Double.MAX_VALUE;
// 		}
// 		int contRank=1;
// 		for(int i=0;i<lista.size();i++){
// 			double rankAtual=0;
// 			ArrayList<Integer> valores=encontraValores(dados, lista.get(i));
// 			for(int j=0;j<valores.size();j++)
// 				rankAtual+=contRank++;
// 			rankAtual/=(double)valores.size();
// 			for(int j=0;j<valores.size();j++){
// 				ranks[valores.get(j)/colunas][valores.get(j)%colunas]=rankAtual;
// 			}
// 			//for(int j=0;j<contaRepetido(dados, lista.get(i));j++)
// 				
// 		}
		matriz=execute(dados);
// 		for(int i=0;i<colunas;i++){
// 			for(int j=0;j<colunas;j++){
// 				System.out.print(matriz[i][j]+"\t");
// 			}
// 			System.out.println();
// 		}
// 		System.out.println();
		for(int i=0;i<ranks.length;i++){
			for(int j=0;j<ranks[0].length;j++){
				avgRanks[j]+=ranks[i][j];
// 				System.out.print(ranks[i][j]+"\t");
			}
//  			System.out.println();
 		}
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
		int contRank=1;
		double[] ranksFinais = new double[colunas];
		for(int l=0;l<listaInt.size();l++){
			double rankAtual=0;
			ArrayList<Integer> valores=encontraValores(listaInt.get(l), matriz);
			//System.out.print("igual a: ("+(l+1)+") "+listaInt.get(l)+" -> ");
			for(int v=0;v<valores.size();v++){
			//	System.out.print(valores.get(v)+"["+rankDoAlg(listaInt,valores.get(v))+"] ");
				rankAtual+=rankDoAlg(listaInt,valores.get(v));
			}
			rankAtual/=(double)valores.size();
// 			System.out.print("el: "+listaInt.get(l)+" -> ");
// 			for(int i=0;i<valores.size();i++)
// 				System.out.print(valores.get(i)+" ");
// 			System.out.print(" ranks: ");
// 			for(int i=0;i<valores.size();i++)
// 				System.out.print(rankDoAlg(listaInt,valores.get(i))+" ");
// 				
// 			System.out.print(" ranksAtual: "+rankAtual);
// 			System.out.println();
			ranksFinais[listaInt.get(l)]=rankAtual;
		}
		listaInt.clear();
		
		
		menorValor=Double.MAX_VALUE;
		for(int j=0;j<colunas;j++)
			if(ranksFinais[j] < menorValor)
				menorValor=ranksFinais[j];
 		
		for(int j=0;j<colunas;j++){
			//avgRanks[j]/=dados.size();
			//System.out.print(avgRanks[j]+"("+ranksFinais[j]+") ");
			//saida+=avgRanks[j]+" ("+ranksFinais[j]+")"+" & ";
			if(ranksFinais[j] == menorValor)
				saida+="\\textbf{"+avgRanks[j]+" ("+ranksFinais[j]+")"+"} & ";
			else
				saida+=avgRanks[j]+" ("+ranksFinais[j]+")"+" & ";
			
			
			resultadosFinais[j]+=ranksFinais[j];
		}
		//System.out.println("");
		
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
			String comando="java fdr_full temp.txt";
 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
 			p.waitFor();
 			System.out.print(".");
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ( (line=br.readLine()) != null) {
				if(line.split(" ")[0].split("-").length == 2){
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
}
