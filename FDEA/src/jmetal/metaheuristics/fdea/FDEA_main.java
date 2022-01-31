package jmetal.metaheuristics.fdea;

import jmetal.core.*;
import jmetal.metaheuristics.fastTea.FastTea;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.DTLZ.*;
import jmetal.problems.ProblemFactory;
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

public class FDEA_main {
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
	public static void main(String args[]) throws JMException, ClassNotFoundException, SecurityException, IOException{
		// Logger object and file to store log messages
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("FDEA_main.log");
		logger_.addHandler(fileHandler_);
		String variante = "FDEATESTE";
		String problema = "dtlz2";

		int obj = 2;
		int var = 10;

		if (args.length == 3) {
			problema = args[0];
			obj = Integer.parseInt(args[1]);
			variante = args[2];
		}

			long Execution_time=0;

			Problem problem = null; // The problem to solve
			Algorithm algorithm = null; // The algorithm to use
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

			algorithm.setInputParameter("maxGenerations", 0);
			algorithm.setInputParameter("maxEvaluations", 1000*100);
			algorithm.setInputParameter("populationSize", 100);
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

			long initTime = System.currentTimeMillis();
			SolutionSet population = algorithm.execute();
			Execution_time+=(System.currentTimeMillis() - initTime);

//			logger_.info("Problem " + problema + " obj "+ obj + " variante "+variante);
//			String fileName = variante+"_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+".txt";
//			population.printObjectivesToFile(fileName);
//			System.out.println(fileName);

			population.printVariablesToFile("VAR");
			population.printObjectivesToFile("FUN");


			//printGD("IGD"+ "-NSGAIII-"+problem.getName(),IGDarray);
			//printGD("NHaEA_Min_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_HV.txt",HVarray);

			//System.out.println("avrHV-fun"+fun+"= "+sumHV/runtimes);
  }//main
}
