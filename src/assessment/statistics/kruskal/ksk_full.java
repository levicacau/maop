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



public class ksk_full {
	public static void main(String[] args) throws IOException{

		if(args.length != 1){
			System.out.println("uso: ksk_full resultados");
			System.exit(1);
		}
	
		FileReader fr = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<double[]> dados = new ArrayList<double[]>();

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
					operacoes(dados);
				dados.clear();
			}
			linha = br.readLine();
		}
		if(dados.size() > 0)
			operacoes(dados);	
	}
	
// 	public static void operacoes(ArrayList<double[]> dados) {//original using package pgirmess and function kruskalmc
// 		String saida="";
// 		String sets="";
// 		String categs="";
// 		int melhorIndice=-1;
// 		double melhorValor = Double.MAX_VALUE;
// 		double[] medias=calcularMedias(dados);
// 		int combinacoes=0;
// 		
// 		for(int a=0;a<dados.get(0).length;a++){ //percorre as colunas (resultados dos algoritmos a comparar)
// 			combinacoes+=a;
// 			
// 			categs+="'"+(a+1)+"',";
// 			saida+="conj"+a+"<-c(";
// 			sets+="conj"+a+",";
// 			for(int j=0;j<dados.size();j++){ //percorre linha a linha
// 				saida+=dados.get(j)[a]+",";
// 			}
// 			saida=(saida.substring(0,saida.length()-1));
// 			saida+=");";
// 			
// 			if(melhorValor > medias[a]){
// 				melhorIndice=a;
// 				melhorValor=medias[a];
// 			}
// 		}
// 		saida+="require(pgirmess);";
// 		sets=(sets.substring(0,sets.length()-1));
// 		categs=(categs.substring(0,categs.length()-1));
// 		//saida+="AR1<-cbind("+sets+");";
// 		saida+="AR1<-c("+sets+");";
// 		saida+="categ<-as.factor(rep(c("+categs+"),each="+dados.size()+"));";
// 		//saida+="result<-friedman.test(AR1);";
// 		saida+="result<-kruskal.test(AR1,categ);";		
// 		saida+="m<-data.frame(result\\$statistic,result\\$p.value);";
// 		saida+="print(m);";
// 		//saida+="pos_teste<-friedmanmc(AR1);";
// 		saida+="pos_teste<-kruskalmc(AR1,categ);";
// 		
// 		saida+="print(pos_teste);";
// 		//System.out.println(saida);
// 		execute(saida, combinacoes+2, melhorIndice+1);
// 		System.out.println();
// 		
// 		melhorValor = Double.MAX_VALUE;
// 		melhorIndice=-1;
// 		combinacoes=0;
// 		saida=sets="";
// 	}
	public static void operacoes(ArrayList<double[]> dados) {
		String saida="";
		String sets="";
		String categs="";
		int melhorIndice=-1;
		double melhorValor = Double.MAX_VALUE;
		double[] medias=calcularMedias(dados);
		//int combinacoes=0;
		
		for(int a=0;a<dados.get(0).length;a++){ //percorre as colunas (resultados dos algoritmos a comparar)
			//combinacoes+=a;
			
			categs+="'"+(a+1)+"',";
			saida+="conj"+a+"<-c(";
			sets+="conj"+a+",";
			for(int j=0;j<dados.size();j++){ //percorre linha a linha
				saida+=dados.get(j)[a]+",";
			}
			saida=(saida.substring(0,saida.length()-1));
			saida+=");\n";
			
			if(melhorValor > medias[a]){
				melhorIndice=a;
				melhorValor=medias[a];
			}
		}
// 		saida+="require(pgirmess);";
		saida+="require(PMCMRplus);";
		saida+="options(width=1000);";//set the line width, so there is no line break on the output, this was causing issues with more than 10 algorithms
		sets=(sets.substring(0,sets.length()-1));
		categs=(categs.substring(0,categs.length()-1));
		//saida+="AR1<-cbind("+sets+");";
		saida+="AR1<-c("+sets+");";
		saida+="categ<-as.factor(rep(c("+categs+"),each="+dados.size()+"));";
		//saida+="result<-friedman.test(AR1);";
		saida+="result<-kruskal.test(AR1,categ);";
		//saida+="m<-data.frame(result\\$statistic,result\\$p.value);";
		saida+="print(result);";
		//saida+="pos_teste<-friedmanmc(AR1);";
// 		saida+="pos_teste<-kruskalmc(AR1,categ);";
		saida+="pos_teste<-kwAllPairsNemenyiTest(AR1, categ, method='Tukey');";
		
		saida+="print(pos_teste);";
		//System.out.println(saida);
		execute(saida, dados.get(0).length, melhorIndice+1);
		System.out.println();
		
		melhorValor = Double.MAX_VALUE;
		melhorIndice=-1;
		//combinacoes=0;
		saida=sets="";
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
// 	public static void execute(String saida, int combinacoes, int melhor){//original using package pgirmess
// 		try {
// 			String comando="echo \""+saida+"\" | R --no-save | tail -"+(combinacoes+5);//teste mostrando todas as diferencas e o p-value
// 			System.out.println(comando);
// 			//String comando="echo \""+saida+"\" | R --no-save | tail -"+combinacoes;//teste mostrando todas as diferencas
// 			//System.out.println(saida);
// 			//System.exit(1);
//  			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
//  			p.waitFor();
//  			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
// 
// 			String line;
// 			while ((line=br.readLine()) != null) {
// 				System.out.println(line);
// 			}
// 		}catch (Exception e){	e.printStackTrace();}
// 		//System.out.println();
// 	}
	public static void execute(String saida, int combinacoes, int melhor){// to be used with package PMCMR and keep compatibility
		try {
// 			System.out.println(saida);
			try( FileWriter entrada = new FileWriter("rScript.r") ){
				entrada.write(saida);
			}
		
// 			String comando="echo \""+saida+"\" | R --no-save | tail -"+(combinacoes+11)+" | head -"+(combinacoes+8);//teste mostrando todas as diferencas e o p-value
// 			String comando="R --vanilla -e \""+saida+"\" | tail -"+(combinacoes+11)+" | head -"+(combinacoes+8);//teste mostrando todas as diferencas e o p-value
// 			String comando="Rscript rScript.r | tail -"+(combinacoes+11)+" | head -"+(combinacoes+8);//teste mostrando todas as diferencas e o p-value
			String comando="Rscript rScript.r";//teste mostrando todas as diferencas e o p-value

			
// 			System.out.println("#### Log:"+comando);
			//String comando="echo \""+saida+"\" | R --no-save | tail -"+combinacoes;//teste mostrando todas as diferencas
			//System.out.println(saida);
			//System.exit(1);
 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
 			p.waitFor();
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
 			
 			File file = new File("rScript.r");
//			if(!file.delete())
//				System.out.println("script file delete operation failed.");

			String line;
			ArrayList<String> lines = new ArrayList<String>();
			System.out.println("#### Lines");
			while ((line=br.readLine()) != null) {
 				System.out.println(line);
				lines.add(line);
			}
			System.out.println("#### Lines");
//			System.out.println(lines.get(0));
//			System.out.println(lines.get(1));
//			System.out.println(lines.get(3));
//			System.out.println(lines.get(4));
//
//// 			System.out.println();
//			System.out.println("Comparisons");
//			System.out.println("    p-value limit p-value difference");
			//ArrayList<double[]> matrix = new ArrayList<double[]>();
			double[][] matrix = new double[combinacoes][combinacoes];
			for(int i=lines.size()-combinacoes+1;i<lines.size()-2;i++){
				double[] splittedValue = new double[combinacoes];
// 				System.out.println(lines.get(i)+"\t\t("+i+")");

				for(int j=0;j<combinacoes-1;j++){
//					System.out.println("#### "+lines.get(i));
					String part=lines.get(i).replace("<","").split("\\s+")[j+1];
 					System.out.println(part);
					
					if(part.compareTo("-") !=0 ){
// 						System.out.println(lines.get(i));
					
// 						System.out.println("l: "+lines.get(i).split("\\s+")[0]);
// 						System.out.println("c: "+lines.get(lines.size()-combinacoes-2).split("\\s+")[j+1]);
// 						System.exit(1);
					
						int l=Integer.parseInt(lines.get(i).split("\\s+")[0])-1;
						int c=Integer.parseInt(lines.get(lines.size()-combinacoes-2).split("\\s+")[j+1])-1;
						//System.out.println(l+" x "+c);
					
						matrix[l][c]=Double.parseDouble(part);
						matrix[c][l]=Double.parseDouble(part);
// 						System.out.println("\t\t"+Double.parseDouble(part));
// 						matrix[indices.get(j)-1][indices.get(counter)-1]=Double.parseDouble(part);
						//splittedValue[indices.get(j)-1]=Double.parseDouble(part);
					//System.out.print(splittedValue[j]+" ");
					}
				}
				//matrix[indices.get(counter)-1]=splittedValue;
			}
			
// 			for(int i=0;i<indices.size();i++){
// // 				indices[j] = Integer.parseInt(lines.get(lines.size()-combinacoes).split("\\s+")[j+1]);
// 				System.out.println(indices.get(i));
// 			}
			
			
			for(int i=0;i<matrix.length;i++){
				for(int j=0;j<i;j++){
// 				for(int j=0;j<matrix[i].length;j++){
					System.out.print((i+1)+"-"+(j+1)+"\t"+matrix[i][j]+"\t 0.05\t");
					if(matrix[i][j] < 0.05)
						System.out.println("TRUE");
					else
						System.out.println("FALSE");
						
// 					System.out.printf(matrix[i][j]+"\t");
// 					System.out.println();
				}
// 				System.out.println();
			}
// 			System.out.println();
// 			for(int i=0;i<matrix.length;i++){
// 				for(int j=0;j<matrix[i].length;j++){
// 					System.out.printf(matrix[i][j]+"\t");
// 				}
// 				System.out.println();
// 			}
			
			System.out.println(">");
				
			
		}catch (Exception e){	e.printStackTrace();}
		//System.out.println();
	}
}
