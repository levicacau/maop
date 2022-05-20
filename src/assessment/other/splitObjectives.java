import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter; 

/*
Split the input file in several files when find blank lines
Created to split files containing indicator results for several numbers of objectives in individual files with one number of objectives each
*/

public class splitObjectives {
	public static void main(String[] args) throws IOException {
		if(args.length != 1){
			System.out.println("uso: splitObjectives <file to split> ");
			System.exit(1);
		}
		int[] objectives={3, 5, 8, 10, 15, 20};

		try{
			FileReader fr = new FileReader(args[0]);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<double[]> dados = new ArrayList<double[]>();
			int numFronts=0;

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
						writeDados(dados, args[0]+objectives[numFronts]);
						numFronts++;
						
						//matrizMedias.add(calcularMedias(dados));
					}
					dados.clear();
				}
				linha = br.readLine();
			}
			if(dados.size() > 0){
				writeDados(dados, args[0]+objectives[numFronts]);
				numFronts++;
			}
			
		}catch(Exception e){System.out.println("Error: file not found ("+args[0]+")!");}
	}
	static void writeDados(ArrayList<double[]> dados, String filename) throws IOException{
		try (FileWriter entrada = new FileWriter(filename) ) {
			for(int i=0;i<dados.size();i++){
				for(int j=0;j<dados.get(i).length;j++){
					entrada.write(dados.get(i)[j]+" ");
				}
				entrada.write("\n");
			}
		}
		System.out.println(filename+" created");
	}
}
