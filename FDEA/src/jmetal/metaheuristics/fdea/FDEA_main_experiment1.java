package jmetal.metaheuristics.fdea;

import jmetal.core.*;
import jmetal.metaheuristics.fastTea.FastTea;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.DTLZ.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class FDEA_main_experiment1 {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	public static final String pathProblems = "C:\\projetos\\mestrado\\Fuzzy-Decomposition-based-Evolutionary-Algorithm-main\\FDEA\\src\\resources\\referenceFronts\\";


	public static void printGD(String path,double[] GD){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path)     ;//java
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;//OutputStreamWriter
	      BufferedWriter bw      = new BufferedWriter(osw)        ;//
	      for (int i = 0; i < GD.length; i++) {  
	        bw.write(GD[i]+" ");//
	        bw.newLine(); //
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
	public static void main(String args[]) throws JMException, ClassNotFoundException, SecurityException, IOException, InterruptedException {
		// Logger object and file to store log messages
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("FDEA_main.log");
		logger_.addHandler(fileHandler_);

//		String[] problems = "dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7".split(" ");
//		String[] objectives = "2 3 5 8 10 15".split(" ");
//		String[] variantes = "FDEATESTE FDEA".split(" ");

		String[] problems = "dtlz2 dtlz3 dtlz4".split(" ");
		String[] objectives = "2 3".split(" ");
		String[] variantes = "FDEATESTE".split(" ");


		int obj = 2;
		int variaveis = 24;
		int threadPoolSize = 10;

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);

		for (String problema : problems) {
			for (String objective : objectives) {
				obj = Integer.parseInt(objective);
				for (String variante : variantes) {
					for (int run = 1; run <= 30; run++) {
						executor.execute(new RunExperiment(problema, obj, variante, getVar(problema, obj), run));
					}
				}
			}
		}
		executor.shutdown();
//		try {
			if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
//				executor.shutdownNow();
				System.out.println("shutdownNow!!!");
			}else{
				System.out.println("N�o");
			}
//		} catch (InterruptedException ex) {
//			executor.shutdownNow();
//			Thread.currentThread().interrupt();
//			System.out.println("shutdownNow!!!interrupt");
//		}
		System.out.println("Ok!!!");

  }//main

	private static int getVar(String problem, int obj) {
		int p = Integer.parseInt(problem.substring(4,5));
		return p != 7 ? 9 + obj : 19 + obj;
	}

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
		return new int[]{popsize, iterationNumber*3};
	}

	public static class RunExperiment extends Thread {
		String problema, variante;
		int obj, var, run;
		public RunExperiment(String problema, int obj, String variante, int var, int run) {
			this.problema = problema;
			this.variante = variante;
			this.obj = obj;
			this.var = var;
			this.run = run;
		}

		@Override
		public void run() {

			logger_.info("Problem " + problema + " obj " + obj + " variante " + variante + " run " + run);

			long Execution_time=0;

			Problem problem = null; // The problem to solve
			Algorithm algorithm = null; // The algorithm to use
			Operator crossover = null; // Crossover operator\

			Operator mutation = null; // Mutation operator
			Operator selection = null; //Selection operator

			HashMap parameters; // Operator parameters

			Solution referencePoint;

			QualityIndicator indicators;// Object to get quality indicators
			indicators = null;
			int [] popiter = getPopIterSize(obj);

			if(problema.equals("dtlz2")){
				problem = new DTLZ2("Real",var,obj);
				popiter[1] = popiter[1]/3;
			}

			if(problema.equals("dtlz3")){
				problem = new DTLZ3("Real",var,obj);
			}

			if(problema.equals("dtlz4")){
				problem = new DTLZ4("Real",var,obj);
			}

			if(problema.equals("dtlz5")){
				problem = new DTLZ5("Real",var,obj);
			}

			if(problema.equals("dtlz6")){
				problem = new DTLZ6("Real",var,obj);
			}

			if(problema.equals("dtlz7")){
				problem = new DTLZ7("Real",var,obj);
			}

			if(variante.equals("FDEATESTE")){
				algorithm = new FDEATESTE(problem);
			}
			if(variante.equals("FDEA")){
				algorithm = new FDEA(problem);
			}
			if(variante.equals("FastTea")){
				algorithm = new FastTea(problem);
			}

//			else{
//				algorithm = new FDEA(problem);
//			}

//			algorithm.setInputParameter("maxGenerations", 100);
			algorithm.setInputParameter("maxEvaluations", popiter[1]);
			algorithm.setInputParameter("populationSize", popiter[0]);
			algorithm.setInputParameter("T", 10);
			algorithm.setInputParameter("div1", 14);
			algorithm.setInputParameter("div2", 0);

			// Mutation and Crossover for Real codification
			parameters = new HashMap();
			parameters.put("probability", 1.0);
			parameters.put("distributionIndex", 30.0);
			try {
				crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
						parameters);
			} catch (JMException e) {
				e.printStackTrace();
			}

			parameters = new HashMap();
			parameters.put("probability", 1.0 / problem.getNumberOfVariables());
			parameters.put("distributionIndex", 20.0);
			try {
				mutation = MutationFactory.getMutationOperator("PolynomialMutation",
						parameters);
			} catch (JMException e) {
				e.printStackTrace();
			}

			parameters = null;
			try {
				selection = SelectionFactory.getSelectionOperator("RandomSelection",
						parameters);
			} catch (JMException e) {
				e.printStackTrace();
			}
			/*selection = SelectionFactory.getSelectionOperator("BinaryTournament",
					parameters);*/

			// Add the operators to the algorithm
			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("mutation", mutation);
			algorithm.addOperator("selection", selection);

			long initTime = System.currentTimeMillis();
			SolutionSet population = null;

			try {
				population = algorithm.execute();
			} catch (JMException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			Execution_time+=(System.currentTimeMillis() - initTime);

			logger_.info("Total execution time: "+Execution_time);

			population.printVariablesToFile("results/moeadd-" + problema + "-" + obj + "-" + variante + "_solutions." + run + ".txt");
			population.printObjectivesToFile("results/moeadd-" + problema + "-" + obj + "-" + variante + "_fronts." + run + ".txt");

		}
	}
}
