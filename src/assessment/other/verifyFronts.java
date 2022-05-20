import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class verifyFronts {
	public static void main(String[] args){

		if(args.length != 1){
			System.out.println("uso: verifyFronts <resultados>");
			System.exit(1);
		}
		String problem=determineProblem(args).toUpperCase();
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
						objNumber= verifyObj(dados, problem);
						numFronts++;
						avgSolutions+=dados.size();
						
						//matrizMedias.add(calcularMedias(dados));
					}
					dados.clear();
				}
				linha = br.readLine();
			}
			if(dados.size() > 0){
				objNumber= verifyObj(dados, problem);
				numFronts++;
				avgSolutions+=dados.size();
			}
			
			System.out.println("Problem: "+problem+", Obj: "+objNumber+"\tFronts: "+numFronts+", avg. size: "+avgSolutions/numFronts);
				//matrizMedias.add(calcularMedias(dados));
		}catch(Exception e){System.out.println("Error on Problem: "+problem+" File not found ("+args[0]+")!");}
	}
	static int verifyObj(ArrayList<double[]> dados, String problem){
		for(int i=1;i<dados.size();i++){
			if(dados.get(0).length != dados.get(i).length){
				System.out.println("ERROR! different size in objective vectors!");
				System.exit(1);
			}
			if(problem.equals("DTLZ1")){
				double sum=0;
				for(int j=0;j<dados.get(i).length;j++)
					sum+=dados.get(i)[j];
				if(sum < 0.5){
					System.out.println("\u001B[31m"+"ERROR ON LINE "+i+"! sum of objectives of "+problem+" is "+sum+"!"+"\u001B[0m");
					System.exit(1);
				}
			}
			if(problem.equals("DTLZ2") || problem.equals("DTLZ3") || problem.equals("DTLZ4") || problem.equals("DTLZ5") || problem.equals("DTLZ6")){
				double sum=0;
				for(int j=0;j<dados.get(i).length;j++)
					sum+=dados.get(i)[j]*dados.get(i)[j];
				if(sum < 1){
					System.out.println("\u001B[31m"+"ERROR ON LINE "+i+"! sum of objectives square of "+problem+" is "+sum+"!"+"\u001B[0m");
					System.exit(1);
				}
			}
		}
		return dados.get(0).length;
	}
	public static String determineProblem(String[] args){
		String problem="";
		String problemTest="";
		for(int i=0;i<args.length;i++){
			if(args[i].toLowerCase().contains("dtlz1"))
				problem="dtlz1";
			if(args[i].toLowerCase().contains("dtlz2"))
				problem="dtlz2";
			if(args[i].toLowerCase().contains("dtlz3"))
				problem="dtlz3";
			if(args[i].toLowerCase().contains("dtlz4"))
				problem="dtlz4";
			if(args[i].toLowerCase().contains("dtlz5"))
				problem="dtlz5";
			if(args[i].toLowerCase().contains("dtlz6"))
				problem="dtlz6";
			if(args[i].toLowerCase().contains("dtlz7"))
				problem="dtlz7";
			if(args[i].toLowerCase().contains("wfg1"))
				problem="wfg1";
			if(args[i].toLowerCase().contains("wfg2"))
				problem="wfg2";
			if(args[i].toLowerCase().contains("wfg3"))
				problem="wfg3";
			if(args[i].toLowerCase().contains("wfg4"))
				problem="wfg4";
			if(args[i].toLowerCase().contains("wfg5"))
				problem="wfg5";
			if(args[i].toLowerCase().contains("wfg6"))
				problem="wfg6";
			if(args[i].toLowerCase().contains("wfg7"))
				problem="wfg7";
			if(args[i].toLowerCase().contains("wfg8"))
				problem="wfg8";
			if(args[i].toLowerCase().contains("wfg9"))
				problem="wfg9";
			if(problemTest != "" && problemTest != problem){
				System.err.println("\nErro ao determinar o problema!!!\n");
				System.exit(1);
			}else
				problemTest=problem;
		}
		return problem;
	}
}
