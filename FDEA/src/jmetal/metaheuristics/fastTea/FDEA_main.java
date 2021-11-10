package jmetal.metaheuristics.fdea;

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

public class FDEA_main {
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
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("FDEA_main.log");
		logger_.addHandler(fileHandler_);

		int obj = 2;
		int var = 10;

		for(int fun=2;fun<=2;fun++){
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

			if (args.length == 1) {
				Object[] params = { "Real" };
				problem = (new ProblemFactory()).getProblem(args[0], params);
			} // if
			else if (args.length == 2) {
				Object[] params = { "Real" };
				problem = (new ProblemFactory()).getProblem(args[0], params);
				indicators = new QualityIndicator(problem, args[1]);
			} // if
			else { // Default problem
				if(fun==2){
					problem = new DTLZ2("Real",var,obj);

					  indicators = new QualityIndicator(problem, pathProblems+"DTLZ2."+obj+"D.pf");
						}
				if(fun==3){
					  problem = new DTLZ3("Real",var,obj);
					  indicators = new QualityIndicator(problem, pathProblems+"DTLZ3."+obj+"D.pf" );
						}

			} // else

			algorithm = new FDEATESTE(problem);

			algorithm.setInputParameter("maxGenerations", 1000);
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
			for(int i=0;i<runtimes;i++){
				long initTime = System.currentTimeMillis();
				SolutionSet population = algorithm.execute();
				Execution_time+=(System.currentTimeMillis() - initTime);

				String fileName = "FDEA_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_run"+ (i+1)  + ".txt";
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
			System.out.println("avrIGD-fun"+fun+"= "+sumIGD/runtimes);


			//System.out.println("avrHV-fun"+fun+"= "+sumHV/runtimes);
		}//for-fun
  }//main
}
