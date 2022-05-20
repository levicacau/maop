import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter; 
import java.io.File;

/*
Check all the optimization runs of tandem problems to see if we broke a record.
This record can be either on constrained or unconstrained problems
The second objective for constrained problems should be smaller than 3652.5 (3652.5 days == 10 years)
*/

public class tandemTables {
// 	static String caminho="../../../results/up_to_20/";
	static String caminho="";
	
	public static void main(String[] args) throws IOException {
		caminho=args[0];
	
		String[] titles=readTitles();
		int[] objectives={2};
		
		String[] problems={"tandem1","tandem2","tandem3","tandem4","tandem5","tandem6","tandem7","tandem8","tandem9","tandem10","tandem11","tandem12","tandem13","tandem14","tandem15","tandem16","tandem17","tandem18","tandem19","tandem20","tandem21","tandem22","tandem23","tandem24"};
		double[] tandemUncRecords={1233.49,1412.28,772.53,976.18,1142.6,1673.88,1163.6,1603.4,657.84,1104.51,471.99,603.76,764.74,945.4,726.81,836.46,896.96,1242.61,1192.62,1351.53,812.216,1265.44,1077.95,1209.26};
		double[] tandemConstRecords={1087.66,1338.72,664.76,651.55,938.06,1500.46,814.51,740.6,589.03,859.2,336.62,272.13,601.12,806.48,460,428.06,558.51,969.96,506.27,700,502.03,928.57,459.33,281.36};
		
		ArrayList<double[]> allData = new ArrayList<double[]>();

		for(int prb=0;prb<problems.length;prb++){
			for(int obj=0;obj<objectives.length;obj++){
				for(int tt=0;tt<titles.length;tt++){
						String alg=titles[tt].split("/")[0];
						String nameBase=titles[tt].split("/")[1];
					double avgUnc=0;
					double avgCons=0;
					for(int run=0;run<30;run++){
						String file="../../results/"+alg+"/split/"+nameBase+problems[prb]+"-"+objectives[obj]+"."+run+"_fronts.txt";
						
// 						System.out.println(readData(file).size());
						ArrayList<double[]> tmp=readData(file);
						
						avgUnc+=bestUnconstrained(tmp);
						avgCons+=bestConstrained(tmp);
						
						for(int i=0;i<tmp.size();i++)
							allData.add(tmp.get(i));
					}
					//after the 30 runs
					System.out.print("\n"+prb+"_");
// 					System.out.print((nameBase+"\t");//Algorithm
					String algorithmCode=nameBase; // a_b_c_d -- a=algorithm; b=swarmNumber; c=number of global iterations; d=number of local iterations
					algorithmCode=algorithmCode.replace("imulti", "0_");
					algorithmCode=algorithmCode.replace("cmulti", "1_");
					algorithmCode=algorithmCode.replace("cmaes-mopso", "2_");
					algorithmCode=algorithmCode.replace("(1)", "1_");
					algorithmCode=algorithmCode.replace("(50)", "50_");
					algorithmCode+="100_1000";
					
					
					System.out.print(algorithmCode+"\t");//Algorithm code

					System.out.print(bestUnconstrained(allData)+"\t");//best unconstrained solution
					System.out.print((avgUnc/30.0)+"\t");//average best unconstrained solution
					System.out.print(bestConstrained(allData)+"\t");//best constrained solution
					System.out.print((avgCons/30.0)+"\t");//average best constrained solution
					System.out.print((allData.size()/30.0)+"\t");//average Pareto set size
					
					//best unc; avg. best unc; best cons; avg. best cons; avg Pareto size
					
					
					
// 					System.out.println(allData.size());
					allData.clear();
				}
			}
		}
		
		System.out.println();
		
		System.exit(1);
		int problem=0;
		if(args[0].toLowerCase().contains("tandem1")) problem=1;
		if(args[0].toLowerCase().contains("tandem2")) problem=2;
		if(args[0].toLowerCase().contains("tandem3")) problem=3;
		if(args[0].toLowerCase().contains("tandem4")) problem=4;
		if(args[0].toLowerCase().contains("tandem5")) problem=5;
		if(args[0].toLowerCase().contains("tandem6")) problem=6;
		if(args[0].toLowerCase().contains("tandem7")) problem=7;
		if(args[0].toLowerCase().contains("tandem8")) problem=8;
		if(args[0].toLowerCase().contains("tandem9")) problem=9;
		if(args[0].toLowerCase().contains("tandem10")) problem=10;
		if(args[0].toLowerCase().contains("tandem11")) problem=11;
		if(args[0].toLowerCase().contains("tandem12")) problem=12;
		if(args[0].toLowerCase().contains("tandem13")) problem=13;
		if(args[0].toLowerCase().contains("tandem14")) problem=14;
		if(args[0].toLowerCase().contains("tandem15")) problem=15;
		if(args[0].toLowerCase().contains("tandem16")) problem=16;
		if(args[0].toLowerCase().contains("tandem17")) problem=17;
		if(args[0].toLowerCase().contains("tandem18")) problem=18;
		if(args[0].toLowerCase().contains("tandem19")) problem=19;
		if(args[0].toLowerCase().contains("tandem20")) problem=20;
		if(args[0].toLowerCase().contains("tandem21")) problem=21;
		if(args[0].toLowerCase().contains("tandem22")) problem=22;
		if(args[0].toLowerCase().contains("tandem23")) problem=23;
		if(args[0].toLowerCase().contains("tandem24")) problem=24;
		if(problem==0){
			System.out.println("Unable to determine tandem instance");
			System.exit(1);
		}
	}
	public static String[] readTitles()throws IOException{
// 		Scanner scanner = new Scanner( new FileInputStream( caminho+"titles.txt" ) );
		Scanner scanner = new Scanner( new FileInputStream( caminho ) );

		String value = scanner.nextLine().trim();
		value = scanner.nextLine().trim(); //overwrite first line with second line
		String titles[]=null;
		if (value.isEmpty()) {
			System.out.println("Titles file not set");
			System.exit(1);
		}else{
			titles=value.split(" ");
		}
		return titles;
	}
	public static ArrayList<double[]> readData(String fileName){
		ArrayList<double[]> dados = new ArrayList<double[]>();
		try{
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

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
				} /*else {
					if(dados.size() > 0){
						writeDados(dados, args[0]+objectives[numFronts]);
						numFronts++;
						
						//matrizMedias.add(calcularMedias(dados));
					}
					dados.clear();
				}*/
				linha = br.readLine();
			}
// 			if(dados.size() > 0){
// 				writeDados(dados, args[0]+objectives[numFronts]);
// 				numFronts++;
// 			}
		}catch(Exception e){System.out.println("Error: file not found ("+fileName+")!");}
		return dados;
	}
	
	public static double bestUnconstrained(ArrayList<double[]> data){
		double best=Double.MAX_VALUE;
		
		for(int i=0;i<data.size();i++)
			best=Math.min(best,data.get(i)[0]);
		
		return best;
	}
	
	public static double bestConstrained(ArrayList<double[]> data){
		double best=Double.MAX_VALUE;
		
		for(int i=0;i<data.size();i++)
			if(data.get(i)[1] <= 3652.5)
				best=Math.min(best,data.get(i)[0]);
		
		return best;
	}
}
