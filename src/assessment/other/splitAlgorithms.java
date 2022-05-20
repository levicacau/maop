import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class splitAlgorithms {
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("uso: splitAlgorithms <resultados> <collumns to show (true false true ...)>");
			System.exit(1);
		}
		String[] cl=args[1].split("\\s+");
		boolean[] colunas=new boolean[cl.length];
		for(int i=0;i<cl.length;i++)
			colunas[i]=Boolean.parseBoolean(cl[i]);
	
		//boolean[] colunas={false,true,false,true,false,false,true};

		try{
			FileReader fr = new FileReader(args[0]);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<double[]> dados = new ArrayList<double[]>();
			int numFronts=0;
			double avgSolutions=0;
			int objNumber=0;
			//ArrayList<double[]> matrizMedias = new ArrayList<double[]>();

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
						printDados(dados, colunas);
						//numFronts++;
						//avgSolutions+=dados.size();
						
						//matrizMedias.add(calcularMedias(dados));
					}
					dados.clear();
				}
				linha = br.readLine();
			}
			if(dados.size() > 0){
				printDados(dados, colunas);
				//numFronts++;
				//avgSolutions+=dados.size();
			}
			
			//System.out.println("Problem: "+problem+", Obj: "+objNumber+"\tFronts: "+numFronts+", avg. size: "+avgSolutions/numFronts);
				//matrizMedias.add(calcularMedias(dados));
		}catch(Exception e){System.out.println("Error: file not found ("+args[0]+")!");}
	}
	static void printDados(ArrayList<double[]> dados, boolean[] colunas){
		for(int i=0;i<dados.size();i++){
			for(int j=0;j<dados.get(i).length;j++)
				if(colunas[j])
					System.out.print(dados.get(i)[j]+" ");
			System.out.println();
		}
		System.out.print("\n\n\n\n\n\n\n");
	}
}
