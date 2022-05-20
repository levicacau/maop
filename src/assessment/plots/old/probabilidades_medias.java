import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class probabilidades_medias {
	static double[][] matrizMedias;
	static int[] inicio;
	static int[] meio;
	static int[] fim;
	static int menorFront;
	
	
	public static void main(String[] args) throws IOException{
		inicio = new int[9];
		meio = new int[9];
		fim = new int[9];
		//int tam=0;
		menorFront=Integer.MAX_VALUE;

		if(args.length != 1){
			System.out.println("uso: probabilidades_medias <resultado>");
			System.exit(1);
		}
	
		FileReader fr = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<double[]> dados = new ArrayList<double[]>();

		String linha = br.readLine();
		int contExec=0;
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
					if(dados.size() < menorFront)
						menorFront=dados.size();
					calcularMedias(dados);
					contExec++;
				}
				//tam=dados.size();
				dados.clear();
			}
			linha = br.readLine();
		}
		if(dados.size() > 0){
			if(dados.size() < menorFront)
				menorFront=dados.size();
			calcularMedias(dados);
			contExec++;
		}
			
// // // 		for(int i=0;i<matrizMedias.length;i++){
// // // 			for(int j=0;j<matrizMedias[i].length;j++){
// // // 				System.out.print((matrizMedias[i][j]/contExec)+" ");
// // // 			}
// // // 			System.out.println();
// // // 		}



// 		for(int j=0;j<matrizMedias[0].length;j++){
// 			for(int i=0;i<matrizMedias.length;i++){
// 				System.out.println(i+" "+(matrizMedias[i][j]/contExec));
// 			}
// 			System.out.println("\n");
// 		}
		
		


		System.out.println("\t\tCD-Ideal\tNWSum-Ideal\tSigma-Ideal\tCD-MGA\t\tNWSum-MGA\tSigma-MGA\tCD-CD\t\tNWSum-CD\tSigma-CD");
		System.out.print("It: ("+(menorFront/3)+")\t");
		for(int i=0;i<inicio.length;i++)
			System.out.print(inicio[i]+" ("+format((inicio[i]/30.0)*100)+"%)\t");
		System.out.print("\nIt: ("+(2*menorFront/3)+")\t");
		for(int i=0;i<meio.length;i++)
			System.out.print(meio[i]+" ("+format((meio[i]/30.0)*100)+"%)\t");
		System.out.print("\nIt: ("+menorFront+")\t");
		for(int i=0;i<fim.length;i++)
			System.out.print(fim[i]+" ("+format((fim[i]/30.0)*100)+"%)\t");
		System.out.println();

	}
	static void calcularMedias(ArrayList<double[]> dados){
// 		System.out.print("it: "+(menorFront/3)+"="+dados.get((menorFront/3)-1)[9]);
// 		System.out.print("\tit: "+((2*menorFront)/3)+"="+dados.get(((2*menorFront)/3)-1)[9]);
// 		System.out.println("\tit: "+(menorFront)+"="+dados.get(menorFront-1)[9]);
		inicio[(int)dados.get((menorFront/3)-1)[9]]++;
		meio[(int)dados.get(((2*menorFront)/3)-1)[9]]++;
		fim[(int)dados.get(menorFront-1)[9]]++;

	
		if(matrizMedias == null){
			matrizMedias = new double[menorFront][dados.get(0).length];
			for(int i=0;i<matrizMedias.length;i++)
				for(int j=0;j<matrizMedias[i].length;j++)
					matrizMedias[i][j]=0;
		}
		for(int i=0;i<menorFront;i++){
			for(int j=0;j<dados.get(i).length;j++){
				matrizMedias[i][j]+=dados.get(i)[j];
			}
		}
	}
	public static String format(double valor){
		return String.format("%.2f", valor).replace(",",".").replace("e","E");
	}
}
