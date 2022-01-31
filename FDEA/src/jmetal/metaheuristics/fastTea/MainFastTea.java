package jmetal.metaheuristics.fastTea;

import jmetal.core.*;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ProblemFactory;
import jmetal.problems.WFG.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class MainFastTea {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	public static final String pathProblems = "C:\\projetos\\mestrado\\Fuzzy-Decomposition-based-Evolutionary-Algorithm-main\\FDEA\\src\\resources\\referenceFronts\\";


	public static void printGD(String path,double[] GD){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path)     ;//java文件输出流，创建文件流
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;//OutputStreamWriter是字符流通向字节流的桥梁 
	      BufferedWriter bw      = new BufferedWriter(osw)        ;//缓冲区               
	      for (int i = 0; i < GD.length; i++) {  
	        bw.write(GD[i]+" ");//写到缓冲区
	        bw.newLine(); //换行       
	      }
	      
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      Configuration.logger_.severe("Error acceding to the file");
	      e.printStackTrace();
	    }       
	  } // printGD

	/**
	 * @param args
	 *            Command line arguments.
	 * @throws JMException
	 * @throws IOException
	 * @throws SecurityException
	 *             Usage: three options -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName -
	 *             jmetal.metaheuristics.nsgaII.NSGAII_main problemName
	 *             paretoFrontFile
	 */
	public static void main(String args[]) throws JMException, ClassNotFoundException, SecurityException, IOException{
		// Logger object and file to store log messages

		// 3 variantes
		//
		//30 execu珲es para cada variante, DTLZ 1 ao 7, objetivos 2, 3, 5, 8, 10, 15
		// sudo aot-get r-base

		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("FDEA_main.log");
		logger_.addHandler(fileHandler_);

		String problema = "dtlz3";

		int obj = 3;
		int p = Integer.parseInt(problema.substring(4,5));
		int var = p != 7 ? 9 + obj : 19 + obj;

		int [] popiter = getPopIterSize(obj);

		int runtimes=1;
		double[] IGDarray=new double[runtimes];
		double[] HVarray = new double[runtimes];
		long Execution_time=0;

		Problem problem = null; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator\

		Operator mutation; // Mutation operator
		Operator selection; //Selection operator

		HashMap parameters; // Operator parameters

		Solution referencePoint;

		QualityIndicator indicators;// Object to get quality indicators
		indicators = null;

		if(problema.equals("dtlz2")){
			problem = new DTLZ2("Real",var,obj);
		}

		if(problema.equals("dtlz3")){
			problem = new DTLZ3("Real",var,obj);
			popiter[1] = popiter[1]*3;
		}

		if(problema.equals("dtlz4")){
			problem = new DTLZ4("Real",var,obj);
			popiter[1] = popiter[1]*3;
		}

		if(problema.equals("dtlz5")){
			problem = new DTLZ5("Real",var,obj);
			popiter[1] = popiter[1]*3;
		}

		if(problema.equals("dtlz6")){
			problem = new DTLZ6("Real",var,obj);
			popiter[1] = popiter[1]*3;
		}

		if(problema.equals("dtlz7")){
			problem = new DTLZ7("Real",var,obj);
			popiter[1] = popiter[1]*3;
		}

		algorithm = new FastTea(problem);

//			algorithm.setInputParameter("maxGenerations", popiter[0]);
//			algorithm.setInputParameter("maxEvaluations", 190*100);
//			algorithm.setInputParameter("populationSize", 190);
		algorithm.setInputParameter("maxEvaluations", popiter[1]);
		algorithm.setInputParameter("populationSize", popiter[0]);
//			algorithm.setInputParameter("populationSize", popiter[1]);
		algorithm.setInputParameter("T", 10);
		algorithm.setInputParameter("div1", 14);
		algorithm.setInputParameter("div2", 0);

		// Mutation and Crossover for Real codification
		parameters = new HashMap();
		parameters.put("probability", 1.0);
		parameters.put("distributionIndex", 30.0);
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
				parameters);

		parameters = new HashMap();
		parameters.put("probability", 1.0 / problem.getNumberOfVariables());
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator("PolynomialMutation",
				parameters);

		parameters = null;
		selection = SelectionFactory.getSelectionOperator("RandomSelection",
				parameters);
		/*selection = SelectionFactory.getSelectionOperator("BinaryTournament",
				parameters);*/

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
		for(int i=0;i<runtimes;i++){
			long initTime = System.currentTimeMillis();
			SolutionSet population = algorithm.execute();
			Execution_time+=(System.currentTimeMillis() - initTime);

			String fileName = "FastTea_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_run"+ (i+1)  + ".txt";
			population.printObjectivesToFile(fileName);
			System.out.println(fileName);

		/*if(fun>=29 && fun<=30){
			population.printVariablesToFile("NHaEA_Min_Variables_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"run_"+ (i+1)  + ".txt");
		}*/
		//IGDarray[i]=indicators.getIGD1(population);
		/*wfghvCalculator1 wfg = new wfghvCalculator1(population,fun);
		HVarray[i] = wfg.calculatewfghv();*/
		}
		//printGD("IGD"+ "-NSGAIII-"+problem.getName(),IGDarray);
		//printGD("NHaEA_Min_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_HV.txt",HVarray);

		double sumIGD=0;
		//double sumHV=0.0;
		for(int i=0;i<runtimes;i++){
			  sumIGD+=IGDarray[i];
			  //sumHV+=HVarray[i];
			}
		logger_.info("Total execution time: " + Execution_time + "ms");
		//System.out.println("avrHV-fun"+fun+"= "+sumHV/runtimes);
  	}//main

	public static int[] getPopIterSize(int objectiveNumber){
		int popsize=0;
		int iterationNumber=0;
		if(objectiveNumber==2){
			popsize=100;
			iterationNumber=500;
		}
		if(objectiveNumber==3){
			popsize=190;
			iterationNumber=4750;
		}
		if(objectiveNumber==5){
			popsize=714; //*25
			iterationNumber=9996;
		}
		if(objectiveNumber==8){
			popsize=912; //*20
			iterationNumber=18240;
		}
		if(objectiveNumber==10){
			popsize=934; //*30
			iterationNumber=28020;
		}
		if(objectiveNumber==15){
			popsize=140;
			iterationNumber=3000;
		}
		return new int[]{popsize, iterationNumber};
	}
}
