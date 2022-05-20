import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class tabela {
	public static void main(String[] args) throws IOException{

		if(args.length < 1){
			System.out.println("uso: medias <resultados>");
			System.exit(1);
		}
		String saida="\\begin{table*}[t]\n \\center \n \\tiny \n \\caption{results} \n \\begin{tabular}{";
		
		for(int a=0;a<args.length;a++){
		
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
			int colunas=-1;
			
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
					colunas=objs.length;
					for (int i = 0; i < objs.length; i++) {
						d[i] = Double.parseDouble(objs[i].replace(",","."));
					}
					dados.add(d);
				} else {
					if(dados.size() > 0)
						tabela+=" & "+objetivos[objIdx++]+" & "+gerarTabela(dados);
						//matrizMedias.add(calcularMedias(dados));
					dados.clear();
				}
				linha = br.readLine();
			}
			if(dados.size() > 0)
				tabela+=" & "+objetivos[objIdx++]+" & "+gerarTabela(dados);
				//matrizMedias.add(calcularMedias(dados));
				
			if(a==0){
				for(int i=0;i<colunas+2;i++){//gerar o tabular da tabela
					saida+="c|";
				}
				saida = saida.substring(0, saida.length()-1);
				saida+="}\n\\hline \n";
				BufferedReader tt = new BufferedReader(new FileReader("../../results/titles.txt"));
				linha=tt.readLine();
				String[] titles = linha.split("\\s");
				saida+="Prob. & Obj. & ";
				for(int i=0;i<titles.length;i++)
					saida+=titles[i]+" & ";
					
				saida = saida.substring(0, saida.length()-2);
				saida+="\\\\ \\hline \n";
			}
			
			saida+=tabela;
			
			saida+="\\hline \n";	
			
		}
		saida+="\\end{tabular}\n\\label{results}\n\\end{table*}";
		System.out.println(saida);
		
 	}
	static String gerarTabela(ArrayList<double[]> dados){
		String saida="";
		
		boolean[] marcar = execute(dados);
		double[] medias = new double[dados.get(0).length];
		Arrays.fill(medias,0);
		for(int i=0;i<dados.get(0).length;i++){
			for(int j=0;j<dados.size();j++){
				medias[i]+=dados.get(j)[i];
			}
			medias[i]/=dados.size();
		}
		double menorValor=Double.MAX_VALUE;
		int indiceMenor=-1;
		for(int i=0;i<medias.length;i++){//calcular o indice do menor
			if(medias[i] < menorValor){
				menorValor=medias[i];
				indiceMenor=i;
			}
		}
				
		for(int i=0;i<medias.length;i++){
			if(i != indiceMenor && !(marcar[i]) )
				saida+=format(medias[i])+" & ";
			if(i == indiceMenor && !(marcar[i]) )
				saida+="\\textbf{"+format(medias[i])+"} & ";
			if(i != indiceMenor && marcar[i])
				saida+="\\cellcolor{gray!30}{"+format(medias[i])+"} & ";
			if(i == indiceMenor && marcar[i])
				saida+="\\cellcolor{gray!30}{\\textbf{"+format(medias[i])+"}} & ";
 			menorValor=Double.MAX_VALUE;
 		}
 		saida = saida.substring(0, saida.length()-2);
 		saida+="\\\\\n";
 		return saida;
 		//System.out.print("\\\\ \n");
 		//System.out.println();
		
	}
	public static String format(double valor){
		return String.format("%.2e", valor).replace(",",".").replace("e","E");
	}
	public static boolean[] execute(ArrayList<double[]> dados){
		String entrada="";
		for(int i=0;i<dados.size();i++){
			for(int j=0;j<dados.get(i).length;j++){
				entrada+=dados.get(i)[j]+",";
			}
			entrada+="|";
		}
		boolean[] marcar = new boolean[dados.get(0).length];
		
		Process p;
		try {
			p = Runtime.getRuntime().exec("./tabelaAux.sh "+entrada);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = reader.readLine();
			while (line != null) {
				if(line.split(" ")[0].split("-").length == 2){
					marcar[Integer.parseInt(line.split(" ")[0].split("-")[0])-1]=true;
					marcar[Integer.parseInt(line.split(" ")[0].split("-")[1])-1]=true;
					//System.out.println(line.split(" ")[0]);
				}
				line = reader.readLine();
			}
			
		}catch (Exception e){	e.printStackTrace();}
		
		marcar[0]=false; //nunca marcar o primeiro porque e com ele que todo mundo ta sendo comparado
		return marcar;
	}
}
