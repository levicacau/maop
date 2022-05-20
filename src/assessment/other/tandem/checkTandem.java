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
Check all the optimization runs of tandem problems to see if we broke a record.
This record can be either on constrained or unconstrained problems
The second objective for constrained problems should be smaller than 3652.5 (3652.5 days == 10 years)
*/

public class checkTandem {
	public static void main(String[] args) throws IOException {
		if(args.length != 1){
			System.out.println("uso: checkTandem <tandem front file> ");
			System.exit(1);
		}
		int[] objectives={2};
		
		double[] tandemUncRecords={1233.49,1412.28,772.53,976.18,1142.6,1673.88,1163.6,1603.4,657.84,1104.51,471.99,603.76,764.74,945.4,726.81,836.46,896.96,1242.61,1192.62,1351.53,812.216,1265.44,1077.95,1209.26};
		double[] tandemConstRecords={1087.66,1338.72,664.76,651.55,938.06,1500.46,814.51,740.6,589.03,859.2,336.62,272.13,601.12,806.48,460,428.06,558.51,969.96,506.27,700,502.03,928.57,459.33,281.36};
		
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

		try{
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
					if(Math.abs(d[0]-2000) > tandemUncRecords[problem-1]){
						System.out.println("Broke unconstrained record on Tandem"+problem+": "+d[0]+" = "+Math.abs(d[0]-2000)+" is smaller than "+tandemUncRecords[problem-1]+" check file: "+args[0]);
					}
					if(Math.abs(d[0]-2000) > tandemConstRecords[problem-1] && d[1] <= 3652.5){
						System.out.println("Broke Constrained record on Tandem"+problem+": "+d[0]+" = "+Math.abs(d[0]-2000)+" is smaller than "+tandemConstRecords[problem-1]+" check file: "+args[0]);
					}
					

					//dados.add(d);
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
			
		}catch(Exception e){System.out.println("Error: file not found ("+args[0]+")!");}
	}
}
