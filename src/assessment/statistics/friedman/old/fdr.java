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



public class fdr {
	public static void main(String[] args) throws IOException{

		if(args.length != 1){
			System.out.println("uso: friedman resultados");
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
	
	public static void operacoes(ArrayList<double[]> dados) {
		String saida="";
		String sets="";
		int melhorIndice=-1;
		double melhorValor = Double.MAX_VALUE;
		double[] medias=calcularMedias(dados);
		int combinacoes=0;
		
		for(int a=0;a<dados.get(0).length;a++){ //percorre as colunas (resultados dos algoritmos a comparar)
			combinacoes+=a;

			saida+="conj"+a+"<-c(";
			sets+="conj"+a+",";
			for(int j=0;j<dados.size();j++){ //percorre linha a linha
				saida+=dados.get(j)[a]+",";
			}
			saida=(saida.substring(0,saida.length()-1));
			saida+=");";
			
			if(melhorValor > medias[a]){
				melhorIndice=a;
				melhorValor=medias[a];
			}
		}
		saida+="require(pgirmess);";
		sets=(sets.substring(0,sets.length()-1));
		saida+="AR1<-cbind("+sets+");";
		saida+="result<-friedman.test(AR1);";
		saida+="m<-data.frame(result\\$statistic,result\\$p.value);";
		saida+="pos_teste<-friedmanmc(AR1);";
		saida+="print(pos_teste);";
		//System.out.println(saida);
		execute(saida, combinacoes+2, melhorIndice+1);
		System.out.println();
		
		melhorValor = Double.MAX_VALUE;
		melhorIndice=-1;
		combinacoes=0;
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
	public static void execute(String saida, int combinacoes, int melhor){
		try {
			String comando="echo \""+saida+"\" | R --no-save | tail -"+combinacoes+" | grep -E '1-|\\-1 ' | grep TRUE";//teste mostrando qual TEM diferenca em relacao ao primeiro
			//#echo $1 | R --no-save | tail -$2 | grep -E "$3-|\-$3" | grep FALSE #teste mostrando qual NAO tem diferenca em relacao ao melhor
 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
 			p.waitFor();
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			while ((line=br.readLine()) != null) {
				System.out.println(line);
			}
		}catch (Exception e){	e.printStackTrace();}
		//System.out.println();
	}
}
