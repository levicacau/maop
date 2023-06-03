package assessment.tables.old;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class tabArtigoLevi {
public static void println(String x){System.out.println(x);}
public static void println(){System.out.println();}
//static String caminho="C:\\Users\\Levi Cacau\\Downloads\\results-novoproc\\";
static String caminho="results/";
static String table[][];
static double medias[][];
static double standardsDeviations [][];
static boolean iguais[][];
static double melhores[];
	public static void main(String[] args) throws IOException{
		System.out.println("Iniciando..");
		//String objectives[]={"2","3","5","10","15","20"};
//		String objectives[]={"2","3","5"};
		String objectives[]={"2","3","5","8","10","15"};
		//String metrics[] = {"$GD_p$", "$IGD_p$", "$R_2$","Hypervolume"};
		//String metrics[] = {"$GD_p$", "$IGD_p$"};
// 		String metrics[]={"$IGD_p$"};
		String metrics[]={"Hypervolume"};

//		String[] problems={"dtlz1", "dtlz2", "dtlz3", "dtlz4", };
//		String[] problems={"dtlz5", "dtlz6", "dtlz7", "wfg1", };
//		String[] problems={"wfg2", "wfg3", "wfg4", "wfg5", };
//		String[] problems={"wfg6", "wfg7", "wfg8", "wfg9"};
		String[][] arr_problems = new String[][]{
				{"dtlz1", "dtlz2", "dtlz3", "dtlz4", "dtlz5", "dtlz6", "dtlz7", "wfg1","wfg2", "wfg3", "wfg4", "wfg5","wfg6", "wfg7", "wfg8", "wfg9"},
//				{"dtlz1", "dtlz2", "dtlz3", "dtlz4", },
//				{"dtlz5", "dtlz6", "dtlz7", "wfg1", },
//				{"wfg2", "wfg3", "wfg4", "wfg5", },
//				{"wfg6", "wfg7", "wfg8", "wfg9"}
		};
		int DTLZ=7;
		String[] titles=readTitles();

		for(String[] problems : arr_problems) {

			table = new String[2 + (titles.length * problems.length)][2 + objectives.length];
//			String [][] tableStdv = new String[2 + (titles.length * problems.length)][2 + objectives.length];
			for (int i = 0; i < table.length; i++)
				for (int j = 0; j < table[i].length; j++)
					table[i][j] = "";

			table[0][0] = "\\diagbox{Prob}{Obj}";
			table[0][1] = "";
			for (int i = 2; i < table.length; i++) {
				if ((i - 2) % titles.length == 0)
					table[i][0] = "\\multirow{2}{*}{"+problems[(i - 2) / titles.length].toUpperCase()+"}";
				table[i][1] = titles[(i - 2) % titles.length];
			}
			for (int j = 2; j < table[0].length; j++) {
				table[0][j] = objectives[(j - 2)];
				//			if((j-2)%metrics.length==0)
				//				if((((j-2)/metrics.length)+1) != 7)
				//					table[0][j]="\\multicolumn{"+metrics.length+"}{c|}{DTLZ"+(((j-2)/metrics.length)+1)+"}";
				//				else
				//					table[0][j]="\\multicolumn{"+metrics.length+"}{c}{DTLZ"+(((j-2)/metrics.length)+1)+"}";
				//			table[1][j]=metrics[(j-2)%metrics.length];
			}
			String saida = "\\begin{table*}[t]\n \\center \n \\tiny \n \\caption{results} \n \\begin{tabular}{";
			for (int i = 0; i < table[0].length; i++) {//gerar o tabular da tabela
				saida += "c|";
			}
			saida = saida.substring(0, saida.length() - 1);
			saida += "}\n\\hline\n";
//			for (int i = 0; i < table[0].length -1; i++)
//				saida += "&";
//			saida += "\\\\";
			for (int i = 2; i < table.length; i++) {
//				readData(titles.length, objectives.length, (((j - 2) / metrics.length) + 1), metrics[(j - 2) % metrics.length]);
				int p = (i-2)/2;
				readData(titles.length, objectives.length, problems[p], metrics[(i - 2) % metrics.length]);
				for (int j = 2; j < table[0].length; j++) {

					int linha = j - 2; // titles.length;
					int coluna = (i - 2) % titles.length;
					if ((!format(medias[linha][coluna]).equals(format(melhores[linha]))) && !(iguais[linha][coluna]))
						table[i][j] = format(medias[linha][coluna])+"("+ formatStdDev(standardsDeviations[linha][coluna]) +")";
					if ((format(medias[linha][coluna]).equals(format(melhores[linha]))) && !(iguais[linha][coluna]))
						table[i][j] = "\\cellcolor{gray!30}\\textbf{" + format(medias[linha][coluna]) + "("+ formatStdDev(standardsDeviations[linha][coluna]) +")"+"}";
					if ((!format(medias[linha][coluna]).equals(format(melhores[linha]))) && iguais[linha][coluna])
						table[i][j] = "\\cellcolor{gray!30}{" + format(medias[linha][coluna]) + "("+ formatStdDev(standardsDeviations[linha][coluna]) +")"+"}";
					if ((format(medias[linha][coluna]).equals(format(melhores[linha]))) && iguais[linha][coluna])
						table[i][j] = "\\cellcolor{gray!30}{\\textbf{" + format(medias[linha][coluna]) + "("+ formatStdDev(standardsDeviations[linha][coluna]) +")"+"}}";
				}
			}

//			for (int i = 2; i < table.length; i++) {
////				readData(titles.length, objectives.length, (((j - 2) / metrics.length) + 1), metrics[(j - 2) % metrics.length]);
//				int p = (i-2)/2;
//				readData(titles.length, objectives.length, problems[p], metrics[(i-2) % metrics.length]);
//				for (int j = 2; j < table[0].length; j++) {
//					int linha = j - 2; // titles.length;
//					int coluna = (i - 2) % titles.length;
//					tableStdv[i][j] = "("+ format(standardsDeviations[linha][coluna]) +")";
//				}
//			}

			println("\n" + saida);
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					if (i == 0 && j > 1) {
						if ((j - 2) % metrics.length == 0)
							System.out.print("&" + table[i][j]);
						else
							System.out.print(table[i][j]);
					} else {
						if (j != 0)
							System.out.print("&" + table[i][j]);
						else
							System.out.print(table[i][j]);
					}
				}
				if ((i - 1) % titles.length == 0)
					println("\\\\ \\hline");
				else
					println("\\\\");
			}

			println("\\end{tabular}\n\\label{results}\n\\end{table*}");
		}
	}
	public static String[] readTitles()throws IOException{
		Scanner scanner = new Scanner( new FileInputStream( caminho+"titles.txt" ) );
		String value = scanner.nextLine().trim();
		String titles[]=null;
		if (value.isEmpty()) {
			println("Titles file not set");
			System.exit(1);
		}else{
			titles=value.split(" ");
		}
		return titles;
	}
	public static double[] calcularMedias(ArrayList<double[]> dados){
		double[] saida = new double[dados.get(0).length];
		Arrays.fill(saida, 0);
		for(int i=0;i<dados.size();i++){
			for(int j=0;j<dados.get(i).length;j++){
				saida[j]+=dados.get(i)[j];
			}
		}
		for(int j=0;j<dados.get(0).length;j++){
			saida[j]/=dados.size();
		}
		return saida;
	}
	public static double[] calculateStandardDeviation(double[] medias, ArrayList<double[]> dados) {
		double[] saida = new double[dados.get(0).length];
		double[] sum = new double[dados.get(0).length];
		for(int i=0;i<dados.size();i++){
			for(int j=0;j<dados.get(i).length;j++){
				double mean = medias[j];
				double value = dados.get(i)[j];
				sum[j] += Math.pow(value - mean, 2);
			}

		}
		for(int j=0;j<dados.get(0).length;j++){
			double variance = sum[j] / (dados.get(j).length - 1);
			saida[j] = Math.sqrt(variance);
		}
		return saida;
	}
	public static void readData(int algorithms, int objectiveNumber, String problem, String metric)throws IOException{ //dtlz, algoritmo, linha
		ArrayList<double[]> dados = new ArrayList<double[]>();
		medias = new double[objectiveNumber][algorithms];
		standardsDeviations = new double[objectiveNumber][algorithms];
		iguais = new boolean[objectiveNumber][algorithms];
		melhores = new double[objectiveNumber];
		int j=0;
		Scanner scanner=null;
		boolean min=true; //flag indicando se é maximizacao ou minimizacao
		if(metric.toUpperCase().equals("$GD_P$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-gd.txt" ) );
		if(metric.toUpperCase().equals("$IGD_P$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-igd.txt" ) );
		if(metric.toUpperCase().equals("$R_2$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-r2.txt" ) );
		if(metric.toUpperCase().equals("HYPERVOLUME")){
			scanner = new Scanner( new FileInputStream( caminho+"/all-"+problem+"-hv.txt" ) );
			min=false;
		}
		while(scanner.hasNextLine()){
			String value=scanner.nextLine().trim();
			if(!value.isEmpty())
				dados.add(lineToDoubleVector(value));
			else{
				if(dados.size() > 0 && j < objectiveNumber){
					medias[j]=calcularMedias(dados);
					standardsDeviations[j]=calculateStandardDeviation(medias[j], dados);
					iguais[j]=execute(dados);
					j++;
					dados.clear();
				}
			}
		}

		
		Arrays.fill(melhores, -Double.MAX_VALUE);
		for(int i=0;i<medias.length;i++){//calcular o indice do menor
			double menorValor=0;
			if(min)
				menorValor=Double.MAX_VALUE;
			else
				menorValor=Double.MAX_VALUE*-1;
			for(j=0;j<medias[i].length;j++){
				if(min){
					if(medias[i][j] < menorValor){
						menorValor=medias[i][j];
						melhores[i]=menorValor;
					}
				}else{
					if(medias[i][j] > menorValor){
						menorValor=medias[i][j];
						melhores[i]=menorValor;
					}
				}
			}
		}
		System.out.printf(" ");
	}
	public static boolean[] execute(ArrayList<double[]> dados) throws IOException{
		try (FileWriter entrada = new FileWriter("temp.txt") ) {
			for(int i=0;i<dados.size();i++){
				for(int j=0;j<dados.get(i).length;j++){
					entrada.write(dados.get(i)[j]+" ");
				}
				entrada.write("\n");
			}
		}
		boolean[] marcar = new boolean[dados.get(0).length];
		try {
			//String comando="java -cp ../statistics/friedman fdr_full temp.txt";
			String comando="java -cp src/assessment/statistics/kruskal ksk_full temp.txt";
// 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
			Process p = Runtime.getRuntime().exec("java -cp src/assessment/statistics/kruskal ksk_full temp.txt");
			p.waitFor();
 			System.out.print(".");
 			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ( (line=br.readLine()) != null) {
				if(line.split(" ")[0].split("-").length == 2 && line.split("-").length == 2 ){
					String[] ln=line.split(" ");
					int l=Integer.parseInt(line.split(" ")[0].split("-")[0])-1;
					int c=Integer.parseInt(line.split(" ")[0].split("-")[1])-1;
					if(l==0){
						if(Boolean.parseBoolean(ln[ln.length-1])){
							marcar[c]=false;
							//marcar[l]=false;
						}else{
							marcar[c]=true;
							//marcar[l]=true;
						}
					}
				}
			}
			
			File file = new File("temp.txt");
			if(!file.delete())
				System.out.println("Temp file delete operation failed.");
		}catch (Exception e){	e.printStackTrace();}
		marcar[0]=false; //nunca marcar o primeiro porque e com ele que todo mundo ta sendo comparado
		return marcar;
	}
	public static double[] lineToDoubleVector(String line)throws IOException{
		String campos[]=line.split(" ");
		double[] saida = new double[campos.length];
		for(int j=0;j<campos.length;j++)
			saida[j]=Double.parseDouble(campos[j]);
		return saida;
	}
	public static String format(double valor){
		return String.format("%.5e", valor).replace(",",".").replace("e","E");
	}
	public static String formatStdDev(double valor){
		return String.format("%.2e", valor).replace(",",".").replace("e","E");
	}
}
