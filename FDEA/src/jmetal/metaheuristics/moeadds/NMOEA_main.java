package jmetal.metaheuristics.moeadds;

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
import jmetal.problems.DTLZ.ConvexDTLZ2;
import jmetal.problems.DTLZ.ConvexDTLZ3;
import jmetal.problems.DTLZ.ConvexDTLZ4;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.DTLZ.DTLZ2;
import jmetal.problems.DTLZ.DTLZ3;
import jmetal.problems.DTLZ.DTLZ4;
import jmetal.problems.DTLZ.DTLZ5;
import jmetal.problems.DTLZ.DTLZ6;
import jmetal.problems.DTLZ.DTLZ7;
import jmetal.problems.M2M.MOP1;
import jmetal.problems.M2M.MOP2;
import jmetal.problems.M2M.MOP3;
import jmetal.problems.M2M.MOP4;
import jmetal.problems.M2M.MOP5;
import jmetal.problems.M2M.MOP6;
import jmetal.problems.M2M.MOP7;
import jmetal.problems.MaF.MaF1;
import jmetal.problems.MaF.MaF2;
import jmetal.problems.MaF.MaF3;
import jmetal.problems.MaF.MaF4;
import jmetal.problems.MaF.MaF5_Concave;
import jmetal.problems.MaF.MaF6;
import jmetal.problems.MaF.MaF7;
import jmetal.problems.WFG.WFG1;
import jmetal.problems.WFG.WFG2;
import jmetal.problems.WFG.WFG3;
import jmetal.problems.WFG.WFG4;
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
import jmetal.problems.cec2009Competition.UF10;
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
import jmetal.qualityIndicator.fastHypervolume.wfg.wfghvCalculator2;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class NMOEA_main {
	public static Logger logger_; // Logger object
	public static FileHandler fileHandler_; // FileHandler object

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
	public static void main(String args[]) throws JMException, ClassNotFoundException, SecurityException, IOException{
		// Logger object and file to store log messages
		logger_ = Configuration.logger_;
		fileHandler_ = new FileHandler("NSGAIII_main.log");
		logger_.addHandler(fileHandler_);
		
		for(int fun=29;fun<=45;fun++){
		int runtimes=1;
		double[] IGDarray=new double[runtimes];	
		double[] HVarray = new double[runtimes];
		long Execution_time=0;
		
		Problem problem = null; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		Operator crossover; // Crossover operator
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
			if(fun==1){
		  	      problem = new ZDT1("Real");
		  	      
		  	      indicators = new QualityIndicator(problem,"H:\\truePF\\ZDT1.pf" ) ;
		  	    	}//problem = new WFG1("Real");
		  	if(fun==2){
		  	      problem = new ZDT2("Real");
		  	      
		  	      indicators = new QualityIndicator(problem,"H:\\truePF\\ZDT2.pf" ) ;
		  	    	}//problem = new WFG1("Real");
		  	if(fun==3){
		  	      problem = new ZDT3("Real");
		  	      
		  	      indicators = new QualityIndicator(problem,"H:\\truePF\\ZDT3.pf" ) ;
		  	    	}
		  	if(fun==4){
			      problem = new ZDT4("Real",10);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\ZDT4.pf" ) ;
			    	}//problem = new WFG1("Real");
			if(fun==5){
			      problem = new ZDT6("Real",10);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\ZDT6.pf" ) ;
			    	}//problem = new WFG1("Real");
			if(fun==6){
				//problem = new DTLZ1("Real",10,2);
			      problem = new DTLZ1("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ1.pf") ;
			    	}
		  	if(fun==7){
			      //problem = new DTLZ2("Real",10,2);
		  		problem = new DTLZ2("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ2.pf" ) ;
			    	}//problem = new WFG1("Real");
			if(fun==8){
			      problem = new DTLZ3("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ3.pf" ) ;
			    	}//problem = new WFG1("Real");
			if(fun==9){
			      problem = new DTLZ4("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ4.pf" ) ;
			    	}
		  	if(fun==10){
			      problem = new DTLZ5("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ5.pf" ) ;
			    	}//problem = new WFG1("Real");
			if(fun==11){
			      problem = new DTLZ6("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ6.pf") ;
			    	}//problem = new WFG1("Real");
			if(fun==12){
			      problem = new DTLZ7("Real",10,3);
			      
			      indicators = new QualityIndicator(problem,"H:\\truePF\\DTLZ7.pf" ) ;
			    	}
			if(fun==13){
		  	      problem = new WFG1("Real",9,10,10);
		  	      
		  	      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG1.3D.pf" ) ;
		  	      indicators = new QualityIndicator(problem);
		  	    	}//problem = new WFG1("Real");
		  	if(fun==14){
		  	      problem = new WFG2("Real",9,10,10);
		  	      
		  	      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG2.3D.pf" ) ;
		  	      indicators = new QualityIndicator(problem);
		  	    	}//problem = new WFG1("Real");
		  	if(fun==15){
		  	      problem = new WFG3("Real",9,10,10);
		  	      
		  	      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG3.3D.pf" ) ;
		  	      indicators = new QualityIndicator(problem);
		  	    	}
			if(fun==16){
			      problem = new WFG4("Real",9,10,10);
			      
			      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG4.3D.pf" ) ;
			      indicators = new QualityIndicator(problem);
			    	}//problem = new WFG1("Real");
			if(fun==17){
			      problem = new WFG5("Real",9,10,10);
			      
			      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG5.3D.pf" ) ;
			      indicators = new QualityIndicator(problem);
			    	}
		  	if(fun==18){
		  	      problem = new WFG6("Real",9,10,10);
		  	      
		  	      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG6.3D.pf" ) ;
		  	      indicators = new QualityIndicator(problem);
		  	    	}//problem = new WFG1("Real");
		  	if(fun==19){
			      problem = new WFG7("Real",9,10,10);
			      
			      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG7.3D.pf" ) ;
			      indicators = new QualityIndicator(problem);
			    	}//problem = new WFG1("Real");
			if(fun==20){
			      problem = new WFG8("Real",9,10,10);
			      
			      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG8.3D.pf" );
			      indicators = new QualityIndicator(problem);
			    	}
			if(fun==21){
			      problem = new WFG9("Real",9,10,10);
			      
			      //indicators = new QualityIndicator(problem,"H:\\truePF\\WFG9.3D.pf" ) ;
			      indicators = new QualityIndicator(problem);
			    	}//problem = new WFG1("Real");
			if(fun==22){
			      problem = new MaF1("Real",19,10);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}//problem = new WFG1("Real");
			if(fun==23){
			      problem = new MaF2("Real",19,10);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}
			if(fun==24){
			      problem = new MaF3("Real",19,10);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}//problem = new WFG1("Real");
			if(fun==25){
			      problem = new MaF4("Real",19,10);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}
			if(fun==26){
			      //problem = new MaF5_Convex("Real",14,5);
			      problem = new MaF5_Concave("Real",14,5);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}
			if(fun==27){
			      problem = new MaF6("Real",14,5);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}
			if(fun==28){
			      problem = new MaF7("Real",29,10);
			      
			      indicators = new QualityIndicator(problem) ;
			    	}
			if(fun==29){
			      problem = new UF1("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF1.dat" ) ;
			    	}
			if(fun==30){
			      problem = new UF2("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF2.dat" ) ;
			    	}
			if(fun==31){
			      problem = new UF3("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF3.dat" ) ;
			    	}
			if(fun==32){
			      problem = new UF4("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF4.dat" ) ;
			    	}
			if(fun==33){
			      problem = new UF5("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF5.dat" ) ;
			    	}
			if(fun==34){
			      problem = new UF6("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF6.dat" ) ;
			    	}
			if(fun==35){
			      problem = new UF7("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF7.dat" ) ;
			    	}
			if(fun==36){
			      problem = new UF8("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF8.dat" ) ;
			    	}
			if(fun==37){
			      problem = new UF9("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF9.dat" ) ;
			    	}
			if(fun==38){
			      problem = new UF10("Real");
			      //indicators = new QualityIndicator(problem);
			      indicators = new QualityIndicator(problem,"H:\\truePF\\UF10.dat" ) ;
			    }
			if(fun==39){
				problem = new MOP1("Real",10);
			      
			   // indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP1.txt" ) ;
			}
            if(fun==40){
            	problem = new MOP2("Real",10);
			      
			   // indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP2.txt" ) ;
			}
            if(fun==41){
            	problem = new MOP3("Real",10);
			      
			    //indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP3.txt" ) ;
			}
            if(fun==42){
            	problem = new MOP4("Real",10);
			      
			    //indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP4.txt" ) ;
			}
            if(fun==43){
            	problem = new MOP5("Real",10);
			      
			    //indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP5.txt" ) ;
			}
            if(fun==44){
            	problem = new MOP6("Real",10,3);
			      
			   // indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP6.txt" ) ;
			}
            if(fun==45){
            	problem = new MOP7("Real",10,3);
			    
			    //indicators = new QualityIndicator(problem) ;
			    indicators = new QualityIndicator(problem,"H:\\truePF\\MOP7.txt" ) ;
			}
            
            if(fun==35){
			    problem = new mDTLZ1("Real",19,10);
			      
			    indicators = new QualityIndicator(problem) ;
			}
	        if(fun==36){
			    problem = new mDTLZ2("Real",19,10);
			      
			    indicators = new QualityIndicator(problem) ;
			}
	       if(fun==37){
			   problem = new mDTLZ3("Real",19,10);
			      
			   indicators = new QualityIndicator(problem) ;
		   }
	       if(fun==38){
			   problem = new mDTLZ4("Real",19,10);
			      
			   indicators = new QualityIndicator(problem) ;
		   }
		} // else

		algorithm = new NMOEA_3(problem);
		//algorithm.setInputParameter("div1", 599);
		//algorithm.setInputParameter("div2", 0);
		//algorithm.setInputParameter("div3", 24);
		/*algorithm.setInputParameter("T", 15);
		algorithm.setInputParameter("delta",0.9);
		algorithm.setInputParameter("dataDirectory", "H:\\weight\\");*/
		
		//algorithm.setInputParameter("populationSize", 100);
		//algorithm.setInputParameter("maxGenerations",3000);
		
		if(fun <= 5){//ZDT1-6problems
			algorithm.setInputParameter("T", 15);
			algorithm.setInputParameter("div1", 99);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxGenerations",250);
		}else if(fun > 5 && fun <= 12){//DTLZ1-7
			algorithm.setInputParameter("T", 20);
			algorithm.setInputParameter("div1", 23);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 300);
			algorithm.setInputParameter("maxGenerations",400);
		}else if(fun >12 && fun <=21){//WFG1-9
			algorithm.setInputParameter("T", 20);
			algorithm.setInputParameter("div1", 23);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 275);
			algorithm.setInputParameter("maxGenerations",800);
		}else if(fun >= 22 && fun <= 28){
			algorithm.setInputParameter("T", 20);
			algorithm.setInputParameter("div1", 23);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 275);
			algorithm.setInputParameter("maxGenerations",800);
		}else if(fun >= 29 && fun <= 35){//UF1-7
			algorithm.setInputParameter("T", 15);
			algorithm.setInputParameter("div1", 599);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 600);
			algorithm.setInputParameter("maxGenerations",500);
		}else if(fun >= 36 && fun <= 38){//UF8-10
			algorithm.setInputParameter("T", 20);
			algorithm.setInputParameter("div1", 43);
			algorithm.setInputParameter("div2", 3);
			algorithm.setInputParameter("populationSize", 1000);
			algorithm.setInputParameter("maxGenerations",300);
		}else if(fun >= 39 && fun <= 43){//MOP1-5
			algorithm.setInputParameter("T", 15);
			algorithm.setInputParameter("div1", 23);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 100);
			algorithm.setInputParameter("maxGenerations",3000);
		}else if(fun >= 44 && fun <= 45){//MOP6-7
			algorithm.setInputParameter("T", 20);
			algorithm.setInputParameter("div1", 23);
			algorithm.setInputParameter("div2", 0);
			algorithm.setInputParameter("populationSize", 300);
			algorithm.setInputParameter("maxGenerations",1000);
		}

		
		// Crossover operator
		parameters = new HashMap();
		//parameters.put("CR", 1.0);
		/*parameters.put("CR", 0.5);
		parameters.put("F", 0.5);
		crossover = CrossoverFactory.getCrossoverOperator(
				"DifferentialEvolutionCrossover", parameters);*/
		
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
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",parameters);
		
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
		
		
		for(int i=0;i<runtimes;i++){
		long initTime = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		Execution_time+=(System.currentTimeMillis() - initTime);
		population.printObjectivesToFile("MOEAFD_HD_" + problem.getNumberOfObjectives() + "Obj_"+ problem.getName() + "_run" + (i+1) + ".txt");
		//logger_.info("Total execution time: " + Execution_time + "ms");
		//IGDarray[i]=indicators.getIGD1(population);
		wfghvCalculator1 wfg = new wfghvCalculator1(population,fun);
		HVarray[i] = wfg.calculatewfghv();
		}
		//printGD("MOEAFD_HD_" + problem.getNumberOfObjectives() + "Obj_"+ problem.getName() + "_IGD.txt",IGDarray);
	    printGD("MOEAFD__" + problem.getNumberOfObjectives() + "Obj_"+ problem.getName() + "_HV.txt",HVarray);
		//double sumIGD=0;
		double sumHV=0.0;
		for(int i=0;i<runtimes;i++){
			  //sumIGD+=IGDarray[i];
			  sumHV+=HVarray[i];
			}
		//logger_.info("Total execution time: " + Execution_time + "ms");
	    //System.out.println("avrIGD-fun"+fun+"= "+sumIGD/runtimes);
		System.out.println("avrHV-fun"+fun+"= "+sumHV/runtimes);
		logger_.info("Total execution time: " + Execution_time + "ms");
	 }//for-fun
  }//main
}
