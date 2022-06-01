package assessment.tables.old;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class tabArtigo2 {
public static void println(String x){System.out.println(x);}
public static void println(){System.out.println();}
static String caminho="results/";
static String table[][];
static double medias[][];
static boolean iguais[][];
static double melhores[];
	public static void main(String[] args) throws IOException{
		System.out.println("Iniciando..");
		//String objectives[]={"2","3","5","10","15","20"};
		String objectives[]={"2","3","5","8","10", "15"};
		//String metrics[] = {"$GD_p$", "$IGD_p$", "$R_2$","Hypervolume"};
		//String metrics[] = {"$GD_p$", "$IGD_p$"};
// 		String metrics[]={"$IGD_p$"};
		String metrics[]={"Hypervolume"};
//		String[] problems={"dtlz1", "dtlz2", "dtlz3", "dtlz4", };
//		String[] problems={"dtlz5", "dtlz6", "dtlz7", "wfg1", };
//		String[] problems={"wfg2", "wfg3", "wfg4", "wfg5", };
		String[] problems={"wfg6", "wfg7", "wfg8", "wfg9"};

		int DTLZ=7;
		String[] titles=readTitles();
		table = new String[2+(titles.length*objectives.length)][2+(problems.length)];
		for(int i=0;i<table.length;i++)
			for(int j=0;j<table[i].length;j++)
				table[i][j]="";
		
		table[0][0]="Obj.";
		table[0][1]="Algorithms";
		for(int i=2;i<table.length;i++){
			if((i-2)%titles.length==0)
				table[i][0]=objectives[(i-2)/titles.length];
			table[i][1]=titles[(i-2)%titles.length];
		}
		for(int j=2;j<table[0].length;j++){
			table[0][j]=problems[(j-2)].toUpperCase();
//			if((j-2)%metrics.length==0)
//				if((((j-2)/metrics.length)+1) != 7)
//					table[0][j]="\\multicolumn{"+metrics.length+"}{c|}{DTLZ"+(((j-2)/metrics.length)+1)+"}";
//				else
//					table[0][j]="\\multicolumn{"+metrics.length+"}{c}{DTLZ"+(((j-2)/metrics.length)+1)+"}";
//			table[1][j]=metrics[(j-2)%metrics.length];
		}
		String saida="\\begin{table*}[t]\n \\center \n \\tiny \n \\caption{results} \n \\begin{tabular}{";
		for(int i=0;i<table[0].length;i++){//gerar o tabular da tabela
			saida+="c|";
		}
		saida = saida.substring(0, saida.length()-1);
		saida+="}\n\\hline";
		
		for(int j=2;j<table[0].length;j++){
			readData(titles.length, objectives.length, (((j-2)/metrics.length)+1), metrics[(j-2)%metrics.length]);
			for(int i=2;i<table.length;i++){
				
				int linha=(i-2)/titles.length;
				int coluna=(i-2)%titles.length;
				if( (!format(medias[linha][coluna]).equals(format(melhores[linha]))) && !(iguais[linha][coluna]) )
					table[i][j]=format(medias[linha][coluna]);
				if( (format(medias[linha][coluna]).equals(format(melhores[linha]))) && !(iguais[linha][coluna]) )
					table[i][j]="\\cellcolor{gray!30}\\textbf{"+format(medias[linha][coluna])+"}";
				if( (!format(medias[linha][coluna]).equals(format(melhores[linha]))) && iguais[linha][coluna])
					table[i][j]="\\cellcolor{gray!30}{"+format(medias[linha][coluna])+"}";
				if( (format(medias[linha][coluna]).equals(format(melhores[linha]))) && iguais[linha][coluna])
					table[i][j]="\\cellcolor{gray!30}{\\textbf{"+format(medias[linha][coluna])+"}}";
			}
		}
				
		println("\n"+saida);
		for(int i=0;i<table.length;i++){
			for(int j=0;j<table[i].length;j++){
				if(i == 0 && j> 1){
					if((j-2)%metrics.length==0)
						System.out.print("&"+table[i][j]);
					else
						System.out.print(table[i][j]);
				}else{
					if(j!=0)
						System.out.print("&"+table[i][j]);
					else
						System.out.print(table[i][j]);
				}
			}
			if((i-1)%titles.length==0)
				println("\\\\ \\hline");
			else
			println("\\\\");
		}

		println("\\end{tabular}\n\\label{results}\n\\end{table*}");

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
	public static void readData(int algorithms, int objectiveNumber, int DTLZ, String metric)throws IOException{ //dtlz, algoritmo, linha
		ArrayList<double[]> dados = new ArrayList<double[]>();
		medias = new double[objectiveNumber][algorithms];
		iguais = new boolean[objectiveNumber][algorithms];
		melhores = new double[objectiveNumber];
		int j=0;
		Scanner scanner=null;
		boolean min=true; //flag indicando se é maximizacao ou minimizacao
		if(metric.toUpperCase().equals("$GD_P$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-dtlz"+DTLZ+"-gd.txt" ) );
		if(metric.toUpperCase().equals("$IGD_P$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-dtlz"+DTLZ+"-igd.txt" ) );
		if(metric.toUpperCase().equals("$R_2$"))
			scanner = new Scanner( new FileInputStream( caminho+"/all-dtlz"+DTLZ+"-r2.txt" ) );
		if(metric.toUpperCase().equals("HYPERVOLUME")){
			scanner = new Scanner( new FileInputStream( caminho+"/all-dtlz"+DTLZ+"-hv.txt" ) );
			min=false;
		}
		while(scanner.hasNextLine()){
			String value=scanner.nextLine().trim();
			if(!value.isEmpty())
				dados.add(lineToDoubleVector(value));
			else{
				if(dados.size() > 0){
					medias[j]=calcularMedias(dados);
					iguais[j]=execute(dados);
					j++;
					dados.clear();
				}
			}
		}
		if(dados.size() > 0){
			medias[j]=calcularMedias(dados);
			iguais[j]=execute(dados);
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
			String comando="java -cp ../statistics/kruskal ksk_full temp.txt";
// 			Process p =  Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", comando});
			Process p = Runtime.getRuntime().exec("java -cp ../statistics/kruskal ksk_full temp.txt");
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
}
