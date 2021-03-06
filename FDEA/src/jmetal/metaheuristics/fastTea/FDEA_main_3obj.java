package jmetal.metaheuristics.fdea;

import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.*;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.thetadea.ThetaDEA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.Fonseca;
import jmetal.problems.Kursawe;
import jmetal.problems.ProblemFactory;
import jmetal.problems.Schaffer;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.DTLZ.DTLZ2;
import jmetal.problems.DTLZ.DTLZ3;
import jmetal.problems.DTLZ.DTLZ31;
import jmetal.problems.DTLZ.DTLZ32;
import jmetal.problems.DTLZ.DTLZ33;
import jmetal.problems.DTLZ.DTLZ34;
import jmetal.problems.DTLZ.DTLZ35;
import jmetal.problems.DTLZ.DTLZ36;
import jmetal.problems.DTLZ.DTLZ37;
import jmetal.problems.DTLZ.DTLZ38;
import jmetal.problems.DTLZ.DTLZ4;
import jmetal.problems.DTLZ.DTLZ5;
import jmetal.problems.DTLZ.DTLZ6;
import jmetal.problems.DTLZ.DTLZ7;
import jmetal.problems.MaF.MaF1;
import jmetal.problems.MaF.MaF10;
import jmetal.problems.MaF.MaF11;
import jmetal.problems.MaF.MaF12;
import jmetal.problems.MaF.MaF13;
import jmetal.problems.MaF.MaF2;
import jmetal.problems.MaF.MaF3;
import jmetal.problems.MaF.MaF4;
import jmetal.problems.MaF.MaF5_Concave;
import jmetal.problems.MaF.MaF6;
import jmetal.problems.MaF.MaF7;
import jmetal.problems.MaF.MaF8;
import jmetal.problems.MaF.MaF9;
import jmetal.problems.WFG.WFG1;
import jmetal.problems.WFG.WFG2;
import jmetal.problems.WFG.WFG3;
import jmetal.problems.WFG.WFG4;
import jmetal.problems.WFG.WFG41;
import jmetal.problems.WFG.WFG42;
import jmetal.problems.WFG.WFG43;
import jmetal.problems.WFG.WFG44;
import jmetal.problems.WFG.WFG45;
import jmetal.problems.WFG.WFG46;
import jmetal.problems.WFG.WFG47;
import jmetal.problems.WFG.WFG48;
import jmetal.problems.WFG.WFG5;
import jmetal.problems.WFG.WFG6;
import jmetal.problems.WFG.WFG7;
import jmetal.problems.WFG.WFG8;
import jmetal.problems.WFG.WFG9;
import jmetal.problems.ZDT.ZDT1;
import jmetal.problems.ZDT.ZDT2;
import jmetal.problems.ZDT.ZDT3;
import jmetal.problems.ZDT.ZDT4;
import jmetal.problems.ZDT.ZDT6;
import jmetal.problems.cec2009Competition.UF1;
import jmetal.problems.cec2009Competition.UF2;
import jmetal.problems.cec2009Competition.UF3;
import jmetal.problems.cec2009Competition.UF4;
import jmetal.problems.cec2009Competition.UF5;
import jmetal.problems.cec2009Competition.UF6;
import jmetal.problems.cec2009Competition.UF7;
import jmetal.problems.cec2009Competition.UF8;
import jmetal.problems.cec2009Competition.UF9;
import jmetal.problems.mDTLZ.mDTLZ1;
import jmetal.problems.mDTLZ.mDTLZ2;
import jmetal.problems.mDTLZ.mDTLZ3;
import jmetal.problems.mDTLZ.mDTLZ4;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.qualityIndicator.fastHypervolume.wfg.wfghvCalculator1;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class FDEA_main_3obj{
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object
	public static final String pathProblems = "C:\\projetos\\mestrado\\Fuzzy-Decomposition-based-Evolutionary-Algorithm-main\\FDEA\\src\\resources\\referenceFronts\\";


	public static void printGD(String path,double[] GD){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path)     ;//java??????????????????????
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;//OutputStreamWriter???????????????????????? 
	      BufferedWriter bw      = new BufferedWriter(osw)        ;//??????               
	      for (int i = 0; i < GD.length; i++) {  
	        bw.write(GD[i]+" ");//??????????
	        bw.newLine(); //????       
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
		fileHandler_ = new FileHandler("NSGAIII_main.log");
		logger_.addHandler(fileHandler_);
		
		for(int fun=1;fun<=2;fun++){
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
				problem = new DTLZ2("Real",10,3);

				indicators = new QualityIndicator(problem, pathProblems+"DTLZ2.3D.pf");
			}
			if(fun==3){
				problem = new DTLZ3("Real",10,3);
				indicators = new QualityIndicator(problem, pathProblems+"DTLZ3.3D.pf" );
			}
			if(fun==6){
				//problem = new DTLZ1("Real",10,2);
			      problem = new DTLZ1("Real",7,3);
			      
			      indicators = new QualityIndicator(problem,"E:\\TruePF\\DTLZ\\DTLZ1_3D.txt" );
			    	}
		  	if(fun==7){
			      //problem = new DTLZ2("Real",10,2);
		  		problem = new DTLZ2("Real",12,3);
			      
			      indicators = new QualityIndicator(problem,"E:\\TruePF\\DTLZ\\DTLZ2_3D.txt" );
			    	}//problem = new WFG1("Real");
			if(fun==8){
			      problem = new DTLZ3("Real",12,3);
			      
			      indicators = new QualityIndicator(problem,"E:\\TruePF\\DTLZ\\DTLZ2_3D.txt" );
			    	}//problem = new WFG1("Real");
			if(fun==9){
			      problem = new DTLZ4("Real",12,3);
			      
			      indicators = new QualityIndicator(problem,"E:\\TruePF\\DTLZ\\DTLZ2_3D.txt" );
			    	}
		  	if(fun==10){
			      problem = new DTLZ5("Real",12,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\DTLZ\\DTLZ5_3D.txt" );
			    	}//problem = new WFG1("Real");
			if(fun==11){
			      problem = new DTLZ6("Real",12,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\DTLZ\\DTLZ5_3D.txt" );
			    	}//problem = new WFG1("Real");
			if(fun==12){
			      problem = new DTLZ7("Real",22,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\DTLZ\\DTLZ7_3D.txt" );
			    	}
			if(fun==13){
		  	      problem = new WFG1("Real",4,20,3);
		  	      
		  	      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG\\WFG1_3D_10000.txt");
		  	    	}//problem = new WFG1("Real");
		  	if(fun==14){
		  	      problem = new WFG2("Real",4,20,3);
		  	      
		  	      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG\\WFG2_3D_10000.txt");
		  	    	}//problem = new WFG1("Real");
		  	if(fun==15){
		  	      problem = new WFG3("Real",4,20,3);
		  	      
		  	      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG\\WFG3_3D_10000.txt");
		  	    	}
			if(fun==16){
			      problem = new WFG4("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG\\WFG9_3D_10000.txt");
			    	}//problem = new WFG1("Real");
			if(fun==17){
			      problem = new WFG5("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\WFG\\WFG4_3D.txt" );
			    	}
		  	if(fun==18){
		  	      problem = new WFG6("Real",4,20,3);
		  	      
		  	      indicators = new QualityIndicator(problem,"D:\\TruePF\\WFG\\WFG4_3D.txt" );
		  	    	}//problem = new WFG1("Real");
		  	if(fun==19){
			      problem = new WFG7("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\WFG\\WFG4_3D.txt" );
			    	}//problem = new WFG1("Real");
			if(fun==20){
			      problem = new WFG8("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\WFG\\WFG4_3D.txt" );
			    	}
			if(fun==21){
			      problem = new WFG9("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF\\WFG\\WFG4_3D.txt" );
			    	}//problem = new WFG1("Real");
			if(fun==22){
			      problem = new MaF1("Real",12,3);
			      
			      //indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF1_3D_10000.txt");
			    	}//problem = new WFG1("Real");
			if(fun==23){
			      problem = new MaF2("Real",12,3);
			      
			     // indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF2_3D_10000.txt");
			    	}
			if(fun==24){
			      problem = new MaF3("Real",12,3);
			      
			      //indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF3_3D_10000.txt");
			    	}//problem = new WFG1("Real");
			if(fun==25){
			      problem = new MaF4("Real",12,3);
			      
			      //indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF4_3D_10000.txt" );
			    	}
			if(fun==26){
			      //problem = new MaF5_Convex("Real",14,5);
			      problem = new MaF5_Concave("Real",12,3);
			      
			      //indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF5_3D_10000.txt" );
			    	}
			if(fun==27){
			      problem = new MaF6("Real",12,3);
			      
			      //indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF6_3D_10000.txt" );
			    	}
			if(fun==28){
			      problem = new MaF7("Real",22,3);
			      
			      //indicators = new QualityIndicator(problem) ;
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF7_3D_10000.txt" );
			    	}
			 if(fun==29){
		  	      problem = new WFG41("Real",4,20,3);
		  	      
		  	    indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG41_3D_10000.txt");
		  	    	}//problem = new WFG1("Real");
		  	if(fun==30){
		  	      problem = new WFG42("Real",4,20,3);
		  	      
		  	    indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG42_3D_10000.txt");
		  	    	}//problem = new WFG1("Real");
		  	if(fun==31){
		  	      problem = new WFG43("Real",4,20,3);
		  	      
		  	    indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG43_3D_10000.txt");
		  	    	}
			if(fun==32){
			      problem = new WFG44("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG44_3D_10000.txt");
			    	}//problem = new WFG1("Real");
			if(fun==33){
			      problem = new WFG45("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG45_3D_10000.txt");
			    	}
		  	if(fun==34){
		  	      problem = new WFG46("Real",4,20,3);
		  	      
		  	    indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG46_3D_10000.txt");
		  	    	}//problem = new WFG1("Real");
		  	if(fun==35){
			      problem = new WFG47("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG47_3D_10000.txt");
			    	}//problem = new WFG1("Real");
			if(fun==36){
			      problem = new WFG48("Real",4,20,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\WFG4X\\WFG48_3D_10000.txt");
			}
			if(fun==37){
			      problem = new MaF8("Real",2,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF8_3D_10000.txt") ;
			    	}
			if(fun==38){
			      problem = new MaF9("Real",2,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF9_3D_10000.txt") ;
			    	}
			if(fun==39){
			      problem = new MaF13("Real",5,3);
			      
			      indicators = new QualityIndicator(problem,"D:\\TruePF_Sampling\\PF\\MaF\\MaF13_3D_10000.txt") ;
			    	}
		} // else

		algorithm = new FDEATESTE(problem);
		
		//algorithm = new NHaEA_Max(problem);
		
		algorithm.setInputParameter("maxGenerations", 500);
		algorithm.setInputParameter("populationSize", 120);
		algorithm.setInputParameter("T", 12);
		algorithm.setInputParameter("div1", 14);
		algorithm.setInputParameter("div2", 0);
		
		// Mutation and Crossover for Real codification
		parameters = new HashMap();
		parameters.put("probability", 1.0);
		parameters.put("distributionIndex", 20.0);
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
//		population.printObjectivesToFile("FDEA_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_run"+ (i+1)  + ".txt");

		String fileName = "FDEA_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_run"+ (i+1)  + ".txt";
		population.printObjectivesToFile(fileName);
		System.out.println(fileName);

		/*if(fun>=29 && fun<=30){
			population.printVariablesToFile("NHaEA_Min_Variables_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"run_"+ (i+1)  + ".txt");
		}*/
		IGDarray[i]=indicators.getIGD1(population);
		wfghvCalculator1 wfg = new wfghvCalculator1(population,fun);
		HVarray[i] = wfg.calculatewfghv();
		}
		//printGD("FDEA_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_IGD.txt",IGDarray);
		//printGD("FDEA_"+problem.getNumberOfObjectives()+"Obj_"+problem.getName()+"_HV.txt",HVarray);
		double sumIGD=0;
		double sumHV=0.0;
		for(int i=0;i<runtimes;i++){
			  sumIGD+=IGDarray[i];
			  sumHV+=HVarray[i];
			}
		logger_.info("Total execution time: " + Execution_time + "ms");
	    System.out.println("avrIGD-fun"+fun+"= "+sumIGD/runtimes);
		System.out.println("avrHV-fun"+fun+"= "+sumHV/runtimes);
	 }//for-fun
  }//main
}
