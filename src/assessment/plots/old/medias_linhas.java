import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class medias_linhas {
	public static void main(String[] args) throws IOException{	

		if(args.length != 1){
			System.out.println("uso: medias <resultados>");
			System.exit(1);
		}
	
		FileReader fr = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<double[]> dados = new ArrayList<double[]>();
		ArrayList<double[]> matrizMedias = new ArrayList<double[]>();

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
				if(dados.size() > 0)
					matrizMedias.add(calcularMedias(dados));
				dados.clear();
			}
			linha = br.readLine();
		}
		if(dados.size() > 0)
			matrizMedias.add(calcularMedias(dados));
			
	for(int i=0;i<matrizMedias.get(0).length;i++){
		for(int o=0;o<matrizMedias.size();o++)
				System.out.print(o+" "+matrizMedias.get(o)[i]+"\n");
			System.out.print("\n\n");
		}
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
}
