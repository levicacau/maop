package jmetal.metaheuristics.fastTea;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import Jama.Matrix;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.moea_c.Utils;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.ranking.NondominatedRanking;
import jmetal.util.ranking.Ranking;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastTea extends Algorithm{
	private int populationSize_;
	private int populationSizeGeneral_;

	private SolutionSet population_;
	SolutionSet offspringPopulation_;
	SolutionSet union_;
	
	int generations_;
	int maxGenerations_, maxGen1;
	int interv;
	int evaluations_ = 0;
	int worst;

	Operator crossover_;
	Operator mutation_;
	Operator selection_;
	
	private double[] zideal_; //ideal point
	private double[] znadir_;//Nadir point
	double[][] extremePoints_; // extreme points
	
	int T_;
	int[][] neighborhood_;

	double[] pValue;
	double[][] w;
	int t=0;

	Map<Integer, Solution> p2 = new HashMap<Integer, Solution>();
	Map<Integer, Integer[]> crowdNeighbor2 = new HashMap<Integer, Integer[]>();
	int[][] crowdNeighbor;
	List<Integer> indexToExclude = new ArrayList<Integer>();

	
	public FastTea(Problem problem) {
		super(problem);
	} // CAEA_Min_Ideal
    
	public static void printGD(String path,double[] GD){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path)     ;//java�ļ�������������ļ���
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;//OutputStreamWriter���ַ���ͨ���ֽ��������� 
	      BufferedWriter bw      = new BufferedWriter(osw)        ;//������               
	      for (int i = 0; i < GD.length; i++) {  
	        bw.write(GD[i]+" ");//д��������
	        bw.newLine(); //����       
	      }
	      
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      Configuration.logger_.severe("Error acceding to the file");
	      e.printStackTrace();
	    }       
	  } // printGD
	
	public static void printGD(String path,double[][] GD){
	    try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream(path)     ;//java�ļ�������������ļ���
	      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;//OutputStreamWriter���ַ���ͨ���ֽ��������� 
	      BufferedWriter bw      = new BufferedWriter(osw)        ;//������               
	      for (int i = 0; i < GD.length; i++) {
	    	  for(int j=0;j<GD[i].length;j++){
	    		  bw.write(GD[i][j]+" ");//д��������
	    	  }
	          bw.newLine(); //����       
	      }
	      
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      Configuration.logger_.severe("Error acceding to the file");
	      e.printStackTrace();
	    }       
	  } // printGD
	
	
	public SolutionSet execute2() throws JMException, ClassNotFoundException {

		generations_ = 0;
		evaluations_ = 0;

		int maxEvaluations_ = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		maxGen1 = maxEvaluations_/2;

		populationSizeGeneral_ = ((Integer) this.getInputParameter("populationSize")).intValue();
		populationSize_ = populationSizeGeneral_/5;
		mutation_ = operators_.get("mutation");
		crossover_ = operators_.get("crossover");
		selection_ = operators_.get("selection");
		
		T_ = ((Integer) this.getInputParameter("T")).intValue();
		neighborhood_ = new int[populationSize_][T_];

		if(problem_.getNumberOfObjectives() == 2){
			interv = 12;
		}else if(problem_.getNumberOfObjectives() == 3){
			interv = 20;
		}else if(problem_.getNumberOfObjectives() == 5){
			interv = 24;
		}else if(problem_.getNumberOfObjectives() == 8){
			interv = 32;
		}else if(problem_.getNumberOfObjectives() == 10){
			interv = 40;
		}else if(problem_.getNumberOfObjectives() == 15){
			interv = 60;
		}else{
			interv = 30;
		}
		pValue = new double[maxEvaluations_/interv];
		w = new double[maxEvaluations_/interv][problem_.getNumberOfObjectives()];
		
		initPopulation();// initialize the population;
		
		initIdealPoint();  // initialize the ideal point
		
		initNadirPoint();    // initialize the nadir point
		
		initExtremePoints(); // initialize the extreme points
		int nn = 0;
		//while
		//printGD("FDEA_"+problem_.getNumberOfObjectives()+"Obj_"+problem_.getName()+"_Pvalue.txt",pValue);
		//printGD("FDEA_"+problem_.getNumberOfObjectives()+"Obj_"+problem_.getName()+"_Wvalue.txt",w);

		firstPhase();
//		System.out.println("tamanho da popula��o "+population_.size());
//		Generate offspring population Q with N ? N1 individuals from P
//		by using genetic operators and evaluate their objective vectors

		generateOffspring(populationSizeGeneral_ - populationSize_);
//		System.out.println("tamanho da popula��o "+population_.size());
		SolutionSet basepop = new SolutionSet();
		basepop = ((SolutionSet) basepop).union(population_);

		population_ = ((SolutionSet) population_).union(offspringPopulation_);
//		System.out.println("tamanho da popula��o "+population_.size());

		/* Second Phase */
		while(evaluations_ <= maxEvaluations_){
			generateNewPopulationI(basepop);
			generations_ ++;
//			System.out.println("Gera��es: "+generations_ + " Popula��o: "+population_.size());
//			String fileName = "FastTea_"+problem_.getNumberOfObjectives()+"Obj_"+problem_.getName()+"_run"+ 1  + ".txt";
//			population_.printObjectivesToFile(fileName);
		}

//		System.out.println("tamanho da popula��o "+population_.size());

//		Ranking nodominatedRanking = new NondominatedRanking(population_);
//		return nodominatedRanking.getSubfront(0);
		return population_;
	}//execute

	public SolutionSet execute() throws JMException, ClassNotFoundException {



		populationSizeGeneral_ = ((Integer) this.getInputParameter("populationSize")).intValue();
		populationSize_ = populationSizeGeneral_/2;
		mutation_ = operators_.get("mutation");
		crossover_ = operators_.get("crossover");
		selection_ = operators_.get("selection");

		generations_ = 0;


//		maxGenerations_ = (Integer) this.getInputParameter("maxGenerations");
		int totalEvaluations = (Integer) this.getInputParameter("maxEvaluations");
		int maxGen2 = 10;
		int evaluationsGen2 = maxGen2 * populationSizeGeneral_;
		maxGen1 = (totalEvaluations - evaluationsGen2)/populationSize_;
//		int totalEvaluations = populationSizeGeneral_ * maxGenerations_ + populationSizeGeneral_;
//		int maxGen2 = 10 * populationSizeGeneral_;
//		maxGen1 = totalEvaluations - maxGen2;

		maxGenerations_ = maxGen1+maxGen2;


		T_ = ((Integer) this.getInputParameter("T")).intValue();
		neighborhood_ = new int[populationSize_][T_];

		if(problem_.getNumberOfObjectives() == 2){
			interv = 12;
		}else if(problem_.getNumberOfObjectives() == 3){
			interv = 20;
		}else if(problem_.getNumberOfObjectives() == 5){
			interv = 24;
		}else if(problem_.getNumberOfObjectives() == 8){
			interv = 32;
		}else if(problem_.getNumberOfObjectives() == 10){
			interv = 40;
		}else if(problem_.getNumberOfObjectives() == 15){
			interv = 60;
		}else{
			interv = 30;
		}
		pValue = new double[maxGenerations_/interv];
		w = new double[maxGenerations_/interv][problem_.getNumberOfObjectives()];

		initPopulation();// initialize the population;

		initIdealPoint();  // initialize the ideal point

		initNadirPoint();    // initialize the nadir point

		initExtremePoints(); // initialize the extreme points
		int nn = 0;
		//while
		//printGD("FDEA_"+problem_.getNumberOfObjectives()+"Obj_"+problem_.getName()+"_Pvalue.txt",pValue);
		//printGD("FDEA_"+problem_.getNumberOfObjectives()+"Obj_"+problem_.getName()+"_Wvalue.txt",w);

		firstPhase();
//		System.out.println("tamanho da popula��o "+population_.size());
//		System.out.println("Evaluations "+evaluations_);
//		System.out.println("Generations "+generations_);
//		Generate offspring population Q with N ? N1 individuals from P
//		by using genetic operators and evaluate their objective vectors

		generateOffspring(populationSizeGeneral_ - populationSize_);
//		System.out.println("tamanho da popula��o "+population_.size());
		SolutionSet basepop = new SolutionSet();
		basepop = ((SolutionSet) basepop).union(population_);

		population_ = ((SolutionSet) population_).union(offspringPopulation_);
//		System.out.println("tamanho da popula��o "+population_.size());

		/* Second Phase */
		populationSize_ = populationSizeGeneral_;
		while(generations_ <= maxGenerations_){
			generateNewPopulationI(basepop);
			generations_ ++;
//			System.out.println("Gera��es: "+generations_ + " Popula��o: "+population_.size());
//			String fileName = "FastTea_"+problem_.getNumberOfObjectives()+"Obj_"+problem_.getName()+"_run"+ 1  + ".txt";
//			population_.printObjectivesToFile(fileName);
		}

//		System.out.println("tamanho da popula��o "+population_.size());
//		System.out.println("Evaluations "+evaluations_);
//		System.out.println("Generations "+generations_);

		return population_;
	}//execute
	private void generateNewPopulationI(SolutionSet basepop) throws JMException {
		/*
		Input: Population P', population size N.
		Output: New population P".
		1: Generate offspring population O with N individuals from P' by
		using genetic operators and evaluate their objective vectors.
		2: Normalize the objective vectors of all individuals in P" = P'?O.
		3: Determine the ideal point zide and the nadir point znad of P".
		4: Compute the crowdedness of all individuals in P" by (3).
		5: while |P"| > N do
			6: Find the most crowded individual (x) in P" and its closest
		individual (x1) to form set S.
		7: Calculate the distances to the ideal point for all individuals in
		S.
		8: Select the individual with maximum distance value in S and
		delete it from P".
		9: Update the crowdedness of all individuals in P".
		10: end while
		11: return P".*/

		generateOffspring(populationSizeGeneral_);
		union_ = ((SolutionSet) population_).union(offspringPopulation_);

		updateIdealPoint(union_);
		updateNadirPoint(union_);
		normalizationObjective(union_);

		double p = estimation_Curvature(population_);
		mapping(union_, p);

		/* Separa solu��es dominadas */
//		SolutionSet[] st = getStSolutionSet(union_,populationSize_);
//		double p = 1.0;
//		SolutionSet[] subPopulation = null;
//		//Autodecomposition
//		//estimateIdealPoint(st[0]);
//		updateIdealPoint(st[0]);
//		if(st[0].size() < problem_.getNumberOfObjectives()){
//			updateNadirPoint(st[1]);
//		}else{
//			updateNadirPoint(st[0]);
//		}
//
//
//		p = estimation_Curvature(basepop);
//		mapping(st[1], p);
//
//		union_ = st[1];


		p2 = new HashMap<Integer, Solution>();
		for(int i=0; i<union_.size(); i++){
			p2.put(i, union_.get(i));
		}
		crowdNeighbor2 = new HashMap<Integer, Integer[]>();
		computeCrowdednessIndividuals2(p2);
		int mostCrowded;
		int worstCrowded;
		while(p2.size() > populationSizeGeneral_) {
			mostCrowded = findMostCrowded2(p2);
			worstCrowded = getWorstCrowded2(p2, mostCrowded);
			p2.remove(worstCrowded);
			crowdNeighbor2.remove(worstCrowded);
			updateCrowdednessIndividuals2(p2, worstCrowded);
			worst = worstCrowded;
		}

//		while(union_.size() > populationSizeGeneral_) {
//			crowdNeighbor = new int[union_.size()][2];
//			computeCrowdednessIndividuals(union_);
//			int mostCrowded = findMostCrowded(union_);
//			int worstCrowded = getWorstCrowded(union_, mostCrowded);
//			union_.remove(worstCrowded);
//
//		}
//		population_ = union_;
		population_.clear();
		for(int i : p2.keySet())
			population_.add(p2.get(i));
	}

	private int getWorstCrowded2(Map<Integer, Solution> p, int x) {
		int x1, x2;
		x1 = crowdNeighbor2.get(x)[0];
		x2 = crowdNeighbor2.get(x)[1];
		p.get(x).getDistanceToIdealPoint();
		if(p.get(x1) == null){
//			int a = 0;
			return x;
		}
		if(p.get(x2) == null){
//			int b = 0;
			return x;
		}
		p.get(x1).getDistanceToIdealPoint();
		p.get(x2).getDistanceToIdealPoint();
		if(p.get(x).getDistanceToIdealPoint() > p.get(x1).getDistanceToIdealPoint() && p.get(x).getDistanceToIdealPoint() > p.get(x2).getDistanceToIdealPoint()){
			return x;
		}else{
			if(p.get(x1).getDistanceToIdealPoint() > p.get(x2).getDistanceToIdealPoint())
				return x1;
			else
				return x2;
		}
	}

	private int findMostCrowded2(Map<Integer, Solution> p) {
		int mostCrowded = -1;
		for(int i : p.keySet()){
			if(mostCrowded == -1) mostCrowded = i;
			if(p.get(mostCrowded).getCrowdingDistance() > p.get(i).getCrowdingDistance()) {
				mostCrowded = i;
			}
		}
		return mostCrowded;
	}

	private void updateCrowdednessIndividuals2(Map<Integer, Solution> p, int worst) {
		Solution xi, xj;
		for(int i : p.keySet()){
			if(crowdNeighbor2.get(i)[0] == worst || crowdNeighbor2.get(i)[1] == worst){
				xi = p.get(i);
				double x, x1 = 180, x2 = 180;
				for(int j : p.keySet()){
					if(i==j) continue;
					xj = p.get(j);
					double alfa = computeAngle(xi, xj);
					double beta = computeAngleNadir(xi, xj);
					x = (alfa + beta)/2;
					if(x <= x1){
						x2 = x1;
						x1 = x;
						crowdNeighbor2.get(i)[1] = crowdNeighbor2.get(i)[0];
						crowdNeighbor2.get(i)[0] = j;
					}
				}
				xi.setCrowdingDistance(x1+x2);
			}
		}

	}
	private void computeCrowdednessIndividuals2(Map<Integer, Solution> p) {
		Solution xi, xj;
		for(int i : p.keySet()){
			double x, x1 = 180, x2 = 180;
			crowdNeighbor2.put(i, new Integer[]{0,0});
			xi = p.get(i);
			for(int j : p.keySet()){
				if(i==j) continue;
				xj = p.get(j);
				double alfa = computeAngle(xi, xj);
				double beta = computeAngleNadir(xi, xj);
				x = (alfa + beta)/2;
				if(x <= x1){
					x2 = x1;
					x1 = x;
					crowdNeighbor2.get(i)[1] = crowdNeighbor2.get(i)[0];
					crowdNeighbor2.get(i)[0] = j;
				}
			}
			xi.setCrowdingDistance(x1+x2);
		}
	}

	private int getWorstCrowded(Map<Integer, Solution> p, int x) {
		int x1, x2;
		x1 = crowdNeighbor[x][0];
		x2 = crowdNeighbor[x][1];
		if(p.get(x).getDistanceToIdealPoint() > p.get(x1).getDistanceToIdealPoint() && p.get(x).getDistanceToIdealPoint() > p.get(x2).getDistanceToIdealPoint()){
			return x;
		}else{
			if(p.get(x1).getDistanceToIdealPoint() > p.get(x2).getDistanceToIdealPoint())
				return x1;
			else
				return x2;
		}
	}

	private int findMostCrowded(Map<Integer, Solution> p) {
		int mostCrowded = 0;
		for(int i=1; i<p.size(); i++){
			if(p.get(mostCrowded).getCrowdingDistance() > p.get(i).getCrowdingDistance()) {
				mostCrowded = i;
			}
		}
		return mostCrowded;
	}

	private void computeCrowdednessIndividuals(SolutionSet p) {
		Solution xi, xj;
		for(int i=0; i<p.size(); i++){
			double x, x1 = 180, x2 = 180;
			crowdNeighbor[i][1] = 0;
			crowdNeighbor[i][0] = 0;
			xi = p.get(i);
			for(int j=0; j<p.size(); j++){
				if(i==j) continue;
				xj = p.get(j);
				double alfa = computeAngle(xi, xj);
				double beta = computeAngleNadir(xi, xj);
				x = (alfa + beta)/2;
				if(x <= x1){
					x2 = x1;
					x1 = x;
					crowdNeighbor[i][1] = crowdNeighbor[i][0];
					crowdNeighbor[i][0] = j;
				}
			}
			xi.setCrowdingDistance(x1+x2);
		}
	}

	public void firstPhase() throws JMException{
		while (generations_ < maxGen1) {

			if(generations_ < maxGen1){
				reproduction(generations_, maxGen1);
			}else{
				reproduction_Neighbor();
			}
			union_ = ((SolutionSet) population_).union(offspringPopulation_);
			population_.clear();
			SolutionSet[] st = getStSolutionSet(union_,populationSize_);
			double p = 1.0;
			SolutionSet[] subPopulation = null;
			//Autodecomposition
			//estimateIdealPoint(st[0]);
			updateIdealPoint(st[0]);
			if(st[0].size() < problem_.getNumberOfObjectives()){
				updateNadirPoint(st[1]);
			}else{
				updateNadirPoint(st[0]);
			}

			//estimateNadirPoint(st[1]);

			normalizationObjective(st[1]);

				/*if((generations_)/100 == 0){
					p = estimation_Curvature(st[0]);
				}*/
			if(generations_ > 0.0*maxGenerations_){
				p = estimation_Curvature(st[0]);
			}


				/*if(generations_%interv == 0){
				  pValue[nn] = p;
				  System.out.println("The current curvature is p = "+p);
				  nn++;
			    }*/
			if(st[1].size() == populationSize_){
				population_ = st[1];
			}else{
				mapping(st[1],p);
				subPopulation = new MostSimilarBasedSampling(st[1], problem_.getNumberOfObjectives())
						.getIdealPointOrientedPopulation(populationSize_);
				//Elites Selection to preserve convergence
				getNextPopulation(subPopulation,generations_,maxGenerations_, interv);
			}

			generations_++;
		}
//		Ranking nodominatedRanking = new NondominatedRanking(population_);
//		population_ = nodominatedRanking.getSubfront(0);
	}

	public void initPopulation() throws JMException, ClassNotFoundException {
		evaluations_ += populationSize_;
		population_ = new SolutionSet(populationSize_);

		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);

			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);

			population_.add(newSolution);
		} // for
	} // initPopulation
	
	public void generateOffspring(int N) throws JMException{
		evaluations_ += populationSize_;
		offspringPopulation_ = new SolutionSet(N);
		Solution[] parents = new Solution[2];
		for (int i = 0; i < (N); i++) {
			// obtain parents
			parents = (Solution[]) selection_.execute(population_);

			Solution[] offSpring = (Solution[]) crossover_
					.execute(parents);
			mutation_.execute(offSpring[0]);
			problem_.evaluate(offSpring[0]);
			problem_.evaluateConstraints(offSpring[0]);
			offspringPopulation_.add(offSpring[0]);
		} // for
	}

	public void reproduction(int G, int Gmax) throws JMException{
		evaluations_ += populationSize_;
		offspringPopulation_ = new SolutionSet(populationSize_);
		Solution[] parents = new Solution[2];
		for (int i = 0; i < (populationSize_); i++) {
			if (G < Gmax) {
				// obtain parents
				parents = (Solution[]) selection_.execute(population_);

				Solution[] offSpring = (Solution[]) crossover_
						.execute(parents);
				mutation_.execute(offSpring[0]);
				problem_.evaluate(offSpring[0]);
				problem_.evaluateConstraints(offSpring[0]);
				offspringPopulation_.add(offSpring[0]);
			} // if
		} // for
	}
	
	public void reproduction_Neighbor() throws JMException{
		evaluations_ += populationSize_;
		getNeighborhood_Population();
		
		offspringPopulation_ = new SolutionSet(populationSize_);
		Solution[] parents = new Solution[2];
		for (int i = 0; i < (populationSize_); i++) {
			// obtain parents
			int r1 = PseudoRandom.randInt(0, populationSize_-1);
			parents[0] = population_.get(r1);
			double rd = PseudoRandom.randDouble();
			if(rd < 0.2){
				int r2 = PseudoRandom.randInt(0, populationSize_-1);
				while(r1 == r2){
					r2 = PseudoRandom.randInt(0, populationSize_-1);
				}
				parents[1] = population_.get(r2);
			}else{
				int r2 = PseudoRandom.randInt(0, T_-1);
				while(r1 == neighborhood_[r1][r2]){
					r2 = PseudoRandom.randInt(0, T_-1);
				}
				parents[1] = population_.get(neighborhood_[r1][r2]);
			}
			//parents = (Solution[]) selection_.execute(population_);

			Solution[] offSpring = (Solution[]) crossover_
					.execute(parents);
			mutation_.execute(offSpring[0]);
			problem_.evaluate(offSpring[0]);
			problem_.evaluateConstraints(offSpring[0]);
			offspringPopulation_.add(offSpring[0]);
		} // for
	}

	public void expand_population() throws JMException{
		evaluations_ += populationSize_;
		getNeighborhood_Population();

		offspringPopulation_ = new SolutionSet(populationSizeGeneral_);
		int target = populationSizeGeneral_ - populationSizeGeneral_/5;
		Solution[] parents = new Solution[2];
		int c =0;
		while(offspringPopulation_.size() < target) {
			c++;
			// obtain parents
			parents = (Solution[]) selection_.execute(population_);

			Solution[] offSpring = (Solution[]) crossover_
					.execute(parents);
			mutation_.execute(offSpring[0]);
			problem_.evaluate(offSpring[0]);
			problem_.evaluateConstraints(offSpring[0]);
			offspringPopulation_.add(offSpring[0]);
//			System.out.println("Offspring "+c);
		} // for
		for(int i=0;i<offspringPopulation_.size();i++){
			c--;
			population_.add(offspringPopulation_.get(i));
			System.out.println("Offspring "+offspringPopulation_.get(i));
		}
		populationSize_ = populationSizeGeneral_;
	}

	public void getNeighborhood_Population(){
		double[] x = new double[populationSize_];
		int[] idx = new int[populationSize_];
		
		for(int i=0;i<populationSize_;i++){
			for(int j=0;j<populationSize_;j++){
				x[j]=computeAngle(population_.get(i),population_.get(j));
				idx[j]=j;
			}
			Utils.minFastSort(x, idx, populationSize_, T_);
			System.arraycopy(idx,0,neighborhood_[i],0,T_);
		}
	}
	
    public SolutionSet[] getStSolutionSet(SolutionSet ss,int size) {
		SolutionSet[] sets = new SolutionSet[2];
		Ranking ranking = new NondominatedRanking(ss);

		int remain = size;
		int index = 0;
		SolutionSet front = null;
		SolutionSet mgPopulation = new SolutionSet();
		front = ranking.getSubfront(index);
		sets[0] = front;
		while ((remain > 0) && (remain >= front.size())) {

			for (int k = 0; k < front.size(); k++) {
				mgPopulation.add(front.get(k));
			} // for

			// Decrement remain
			remain = remain - front.size();

			// Obtain the next front
			index++;
			if (remain > 0) {
				front = ranking.getSubfront(index);
			} // if
		}
		if (remain > 0) { // front contains individuals to insert
			for (int k = 0; k < front.size(); k++) {
				mgPopulation.add(front.get(k));
			}
		}

		sets[1] = mgPopulation;

		return sets;
	}
    
    /*
	 * Estimate the Ideal Point 
	 */
	public void estimateIdealPoint(SolutionSet solutionSet){
		for(int i=0; i<problem_.getNumberOfObjectives();i++){
			zideal_[i] = 1.0e+30;
			for(int j=0; j<solutionSet.size();j++){
				if(solutionSet.get(j).getObjective(i) < zideal_[i]){
					zideal_[i] = solutionSet.get(j).getObjective(i);
				}//if
			}//for
		}//for
	}
	
	/*
	 * Estimate the Nadir Point 
	 */
    public void estimateNadirPoint(SolutionSet solutionSet){
    	for(int i=0; i<problem_.getNumberOfObjectives();i++){
			znadir_[i] = -1.0e+30;
			for(int j=0; j<solutionSet.size();j++){
				if(solutionSet.get(j).getObjective(i) > znadir_[i]){
					znadir_[i] = solutionSet.get(j).getObjective(i);
				}//if
			}//for
		}//for
	}
	
    void copyObjectiveValues(double[] array, Solution individual) {
		for (int i = 0; i < individual.numberOfObjectives(); i++) {
			array[i] = individual.getObjective(i);
		}
	}
	

	double asfFunction(Solution sol, int j) {
		double max = Double.MIN_VALUE;
		double epsilon = 1.0E-6;

		int obj = problem_.getNumberOfObjectives();

		for (int i = 0; i < obj; i++) {

			double val = Math.abs((sol.getObjective(i) - zideal_[i])
					/ (znadir_[i] - zideal_[i]));

			if (j != i)
				val = val / epsilon;

			if (val > max)
				max = val;
		}

		return max;
	}

	double asfFunction(double[] ref, int j) {
		double max = Double.MIN_VALUE;
		double epsilon = 1.0E-6;

		int obj = problem_.getNumberOfObjectives();

		for (int i = 0; i < obj; i++) {

			double val = Math.abs((ref[i] - zideal_[i])
					/ (znadir_[i] - zideal_[i]));
			

			if (j != i)
				val = val / epsilon;

			if (val > max)
				max = val;
		}

		return max;
	}
	
	
	
	void initIdealPoint() {
		int obj = problem_.getNumberOfObjectives();
		zideal_ = new double[obj];
		for (int j = 0; j < obj; j++) {
			zideal_[j] = Double.MAX_VALUE;

			for (int i = 0; i < population_.size(); i++) {
				if (population_.get(i).getObjective(j) < zideal_[j])
					zideal_[j] = population_.get(i).getObjective(j);
			}
		}
	}
	
	
	void updateIdealPoint(SolutionSet pop){
		for (int j = 0; j < problem_.getNumberOfObjectives(); j++) {
			for (int i = 0; i < pop.size(); i++) {
				if (pop.get(i).getObjective(j) < zideal_[j])
					zideal_[j] = pop.get(i).getObjective(j);
			}
		}
	}
	
	void initNadirPoint() {
		int obj = problem_.getNumberOfObjectives();
		znadir_ = new double[obj];
		for (int j = 0; j < obj; j++) {
			znadir_[j] = Double.MIN_VALUE;

			for (int i = 0; i < population_.size(); i++) {
				if (population_.get(i).getObjective(j) > znadir_[j])
					znadir_[j] = population_.get(i).getObjective(j);
			}
		}
	}
	
	
	public void initExtremePoints() {
		int obj = problem_.getNumberOfObjectives();
		extremePoints_ = new double[obj][obj];
		for (int i = 0; i < obj; i++){
			for (int j = 0; j < obj; j++){
				extremePoints_[i][j] = 1.0e+30;
			}
		}
		
	}

	
	void updateNadirPoint(SolutionSet pop){
		
		updateExtremePoints(pop);

		
		int obj = problem_.getNumberOfObjectives();
		double[][] temp = new double[obj][obj];

		for (int i = 0; i < obj; i++) {
			for (int j = 0; j < obj; j++) {
				double val = extremePoints_[i][j] - zideal_[j];
				temp[i][j] = val;
			}
		}

		Matrix EX = new Matrix(temp);

		boolean sucess = true;
		
		if (EX.rank() == EX.getRowDimension()) {
			double[] u = new double[obj];
			for (int j = 0; j < obj; j++)
				u[j] = 1;

			Matrix UM = new Matrix(u, obj);

			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < obj; j++) {

				double aj = 1.0 / AL.get(j, 0) + zideal_[j];
		

				if ((aj > zideal_[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					znadir_[j] = aj;
				else {
					sucess = false;
					break;
				}
			}
		} 
		else 
			sucess = false;
		
		
		if (!sucess){
			double zmax[] = computeMaxPoint(pop);
			for (int j = 0; j < obj; j++) {
				znadir_[j] = zmax[j];
			}
		}
	}
	
	
	
	
	public void updateExtremePoints(SolutionSet pop){
		for (int i = 0; i < pop.size(); i++)
			updateExtremePoints(pop.get(i));
	}
	
	
	public void updateExtremePoints(Solution individual){
		int obj = problem_.getNumberOfObjectives();
		for (int i = 0; i < obj; i++){
			double asf1 = asfFunction(individual, i);
			double asf2 = asfFunction(extremePoints_[i], i);
			
			if (asf1 < asf2){
				copyObjectiveValues(extremePoints_[i], individual);
			}
		}
	}
	
	
	double[] computeMaxPoint(SolutionSet pop){
		int obj = problem_.getNumberOfObjectives();
		double zmax[] = new double[obj];
		for (int j = 0; j < obj; j++) {
			zmax[j] = Double.MIN_VALUE;

			for (int i = 0; i < pop.size(); i++) {
				if (pop.get(i).getObjective(j) > zmax[j])
					zmax[j] = pop.get(i).getObjective(j);
			}
		}
		return zmax;
	}
	
    /*
     * Normalization
     */
	public void normalizationObjective(SolutionSet solutionSet){
		for(int i=0; i<solutionSet.size(); i++){
			Solution sol = solutionSet.get(i);
			for(int j=0; j<problem_.getNumberOfObjectives(); j++){
				double val = 0.0;
				val = (sol.getObjective(j)-zideal_[j])/(znadir_[j]-zideal_[j]);
				sol.setNormalizedObjective(j, val);
			}//for
		}//for
	}
	
    public double computeAngle(Solution so1, Solution so2){
		double angle = 0.0;
		double distanceToidealPoint1 = so1.getDistanceToIdealPoint();
		double distanceToidealPoint2 = so2.getDistanceToIdealPoint();
		double innerProduc = 0.0;
		for(int i=0; i<problem_.getNumberOfObjectives(); i++){
			innerProduc += so1.getNormalizedObjective(i)*so2.getNormalizedObjective(i);
		}
		angle = innerProduc/(distanceToidealPoint1*distanceToidealPoint2);
		double value = Math.abs(angle);
//		if(value < 0.0){
//			value = 0.0;
//		}else if(value > 1.0){
//			value = 1.0;
//		}
		angle = Math.acos(value);

//		if(angle == 0.0){
//			int a = 0;
//		}

		return angle;
	}
    
    public double computeAngleNadir(Solution so1, Solution so2){
		double angle = 0.0;
		double distanceToNadirPoint1 = so1.getDistanceToNadirPoint();
		double distanceToNadirPoint2 = so2.getDistanceToNadirPoint();
		double innerProduc = 0.0;
		for(int i=0; i<problem_.getNumberOfObjectives(); i++){
			innerProduc += so1.getNormalizedObjective(i)*so2.getNormalizedObjective(i);
		}
		angle = innerProduc/(distanceToNadirPoint1*distanceToNadirPoint2);
		double value = Math.abs(angle);
//		if(value < 0.0){
//			value = 0.0;
//		}else if(value > 1.0){
//			value = 1.0;
//		}
		angle = Math.acos(value);
		return angle;
	}

    public double computeDistance(Solution so1, Solution so2){
		double dis = 0.0;
		double innerProduc = 0.0;
		int objNumber_ = problem_.getNumberOfObjectives();
		for(int i=0; i<objNumber_; i++){
			innerProduc += Math.pow(so1.getIthTranslatedObjective(i)-so2.getIthTranslatedObjective(i), 2);
		}
		dis = Math.sqrt(innerProduc);
		return dis;
	}
    
    public void getNextPopulation(SolutionSet[] subPopulation, int gen, int maxGen, int interv){
    	int objNumber = problem_.getNumberOfObjectives();
    	SolutionSet[] ReferenceSet = new LeastSimilarBasedSampling(subPopulation[0],objNumber)
    			.getIdealPointOrientedPopulation(objNumber);
    	subPopulation[0].clear();
    	
    	SolutionSet subSets[] = new SolutionSet[populationSize_];
    	
    	for(int i=0;i<objNumber;i++){
    		subSets[i] = new SolutionSet();
    		subSets[i].add(ReferenceSet[0].get(i));
    		subPopulation[0].add(ReferenceSet[0].get(i));
    	}
    	
		for(int i=0; i<populationSize_-objNumber;i++){
			  subSets[i+objNumber] = new SolutionSet();
			  subSets[i+objNumber].add(ReferenceSet[1].get(i));
			  subPopulation[0].add(ReferenceSet[1].get(i));
		}
		
		for(int i=0; i<subPopulation[1].size();i++){
			Solution s1 = subPopulation[1].get(i);
			double minAngle = computeDistance(s1,subPopulation[0].get(0));
			int minIndex = 0;
			for(int j=1; j<subPopulation[0].size();j++){
				Solution s2 = subPopulation[0].get(j);
				double angle = computeDistance(s1,s2);
				if(angle<minAngle){
				   minAngle = angle;
				   minIndex = j;
				}
			}
			subSets[minIndex].add(s1);
		}
		
		double[][] centers = new double[populationSize_][problem_.getNumberOfObjectives()];
		for(int i=0;i<populationSize_;i++){
			for(int m=0;m<problem_.getNumberOfObjectives();m++){
				double summ = 0;
				for(int j=0;j<subSets[i].size();j++){
					summ += subSets[i].get(j).getUnitHypersphereObjective(m);
				}
				centers[i][m] = summ/subSets[i].size();
			}
		}
		
		double[] re = new double[problem_.getNumberOfObjectives()];
		for(int i=0;i<problem_.getNumberOfObjectives(); i++){
    		double sum=0;
    		for(int j=0;j<populationSize_; j++){
    			sum += centers[j][i];
    			//sum += subPopulation[0].get(j).getIthTranslatedObjective(i);
    			//sum += subPopulation[0].get(j).getNormalizedObjective(i);
    		}
    		sum = sum/(populationSize_);
    		re[i] = sum;
    		//System.out.print(re[i]+" ");
    		//re[i] = 1.0;
    	}
		/*if(gen%interv == 0){
			w[t] = re;
			t++;
		}*/
		for(int i=0; i<objNumber;i++){
			double rd = PseudoRandom.randDouble();
			if(gen > 0.0*maxGen){
				population_.add(subSets[i].get(0));
			}else{
				double minValue; 
				//minValue = subSets[i].get(0).getDistanceToIdealPoint();
				//minValue = subSets[i].get(0).getSumValue();
				minValue = computeWS(subSets[i].get(0), re);
				int minIndex = 0;
				for(int j=1; j<subSets[i].size();j++){
				   double value; 
				   //value = subSets[i].get(j).getDistanceToIdealPoint();
				   //value = subSets[i].get(j).getSumValue();
				   value = computeWS(subSets[i].get(j), re);
				   if(value < minValue){
					  minValue = value;
					  minIndex = j;
				   }
				}
				population_.add(subSets[i].get(minIndex));
			}
		}
		
		for(int i=objNumber; i<populationSize_;i++){
		    double minValue; 
			//minValue = subSets[i].get(0).getDistanceToIdealPoint();
		    //minValue = subSets[i].get(0).getSumValue();
			minValue = computeWS(subSets[i].get(0), re);
			int minIndex = 0;
			for(int j=1; j<subSets[i].size();j++){
			   double value; 
			   //value = subSets[i].get(j).getDistanceToIdealPoint();
			   //value = subSets[i].get(j).getSumValue();
			   value = computeWS(subSets[i].get(j), re);
			   if(value < minValue){
				  minValue = value;
				  minIndex = j;
			   }
			}
			population_.add(subSets[i].get(minIndex));
		}
    }
    
    public double computeWS(Solution so1, double[] re){
    	double value = 0.0;
    	double sum = 0.0;
    	for(int i=0; i<problem_.getNumberOfObjectives(); i++){
    		sum += re[i];
    	}
    	if(sum == 0){
    		sum = 0.000001;
    	}
    	for(int i=0; i<problem_.getNumberOfObjectives(); i++){
    		re[i] = re[i]/sum;
    		value += so1.getNormalizedObjective(i)*re[i];
    	}
    	return value;
    }
    
    public double computeLWS(Solution s, Solution r){
    	double value=0;
    	for(int i=0;i<problem_.getNumberOfObjectives();i++){
    		value+=s.getNormalizedObjective(i)*r.getNormalizedObjective(i);
    	}
    	return value;
    }
    
    public double estimation_Curvature(SolutionSet solutionSet){
    	SolutionSet solSet = solutionSet;
    	double c = 1.0;
    	int size = solSet.size();
    	int numb = problem_.getNumberOfObjectives();
    	
    	double sum = 0.0;
    	double mean = 0.0;
    	double var = 0.0;
    	
    	SolutionSet sSet = new SolutionSet();
    	for(int i=0;i<size;i++){
    		Solution sol = solSet.get(i);
    		sSet.add(sol);
    	}
    	
    	for(int i=0;i<sSet.size();i++){
    		Solution sol = sSet.get(i);
    		for(int j=0; j<numb; j++){
    			if(sol.getNormalizedObjective(j) > 1.0){
    				sSet.remove(i);
    				i--;
    				break;
    			}
    		}
    	}
    	solSet = sSet;
    	size = sSet.size();
    	
    	double[] dis = new double[size];
    	for(int i=0;i<size;i++){
    		Solution sol = solSet.get(i);
    		dis[i] = 0.0;
    		for(int j=0; j<numb; j++){
    			dis[i] += sol.getNormalizedObjective(j);
    		}
    		dis[i] = dis[i] - 1.0;
    		dis[i] = dis[i]/Math.sqrt(numb);
    		sum += dis[i];
    	}
    	
    	mean = sum/size;
    	
    	sum = 0.0;
    	for(int i=0;i<size;i++){
    		sum += Math.pow(dis[i]-mean, 2);
    	}
    	
    	if(size > 1){
    		var = sum/(size-1);
    	}else{
    		var = sum/size;
    	}
    	var = Math.sqrt(var);
    	double cv = var/Math.abs(mean);
  
    	
    	/*strategy*/
    	if(mean >= 0){
    		int T2 = 51;
    		double[] p1 = new double[T2];
    		for(int k=0; k<T2; k++){
        		p1[k] = 1.0 + 0.05*k;
        	}
    		double[] E1 = new double[T2];
    		for(int i=0;i<T2;i++){
    			double ss = 0.0;
    			if(size <= 3){
    				for(int n=0;n<size;n++){
        				Solution sol = solSet.get(n);
        				double sumV = 0.0;
        				for(int m=0;m<numb;m++){
        					sumV += Math.pow(sol.getNormalizedObjective(m), p1[i]);
            			}
        				//sumV = Math.pow(sumV, 1.0/p1[i]);
        				ss += sumV;
        			}
        			E1[i] = (ss)/(size);
    			}else{
    				double minSum = Double.MAX_VALUE;
    				double maxSum = Double.MIN_VALUE;
    				for(int n=0;n<size;n++){
        				Solution sol = solSet.get(n);
        				double sumV = 0.0;
        				for(int m=0;m<numb;m++){
            				sumV += Math.pow(sol.getNormalizedObjective(m), p1[i]);
            			}
        				//sumV = Math.pow(sumV, 1.0/p1[i]);
        				ss += sumV;
        				if(sumV < minSum){
        					minSum = sumV;
        				}
        				if(sumV > maxSum){
        					maxSum = sumV;
        				}
        			}
        			//E1[i] = (ss-minSum-maxSum)/(size-2);
        			E1[i] = (ss)/(size);
    			}
    		}
    		int minID = 0;
        	double min = Math.abs(1.0 - E1[0]);
        	for(int i=1;i<T2;i++){
        		double value1 = Math.abs(1.0 - E1[i]);
        		if(value1 < min){
        			min = value1;
        			minID = i;
        		}
        	}
        	c = p1[minID];
    	}else{
    		int T1 = 17;
    		double[] p2 = new double[T1];
    		for(int k=0; k<T1; k++){
        		p2[k] = 1.0 - 0.03*(k);
        	}
    		double[] E2 = new double[T1];
    		for(int i=0;i<T1;i++){
    			double ss = 0.0;
    			if(size <= 3){
    				for(int n=0;n<size;n++){
        				Solution sol = solSet.get(n);
        				double sumV = 0.0;
        				for(int m=0;m<numb;m++){
        					sumV += Math.pow(sol.getNormalizedObjective(m), p2[i]);
            			}
        				sumV = Math.pow(sumV, 1.0/p2[i]);
        				ss += sumV;
        			}
        			E2[i] = (ss)/(size);
    			}else{
    				double minSum = Double.MAX_VALUE;
    				double maxSum = Double.MIN_VALUE;
    				for(int n=0;n<size;n++){
        				Solution sol = solSet.get(n);
        				double sumV = 0.0;
        				for(int m=0;m<numb;m++){
            				sumV += Math.pow(sol.getNormalizedObjective(m), p2[i]);
            			}
        				sumV = Math.pow(sumV, 1.0/p2[i]);
        				ss += sumV;
        				if(sumV < minSum){
        					minSum = sumV;
        				}
        				if(sumV > maxSum){
        					maxSum = sumV;
        				}
        			}
        			//E2[i] = (ss-minSum-maxSum)/(size-2);
        			E2[i] = (ss)/(size);
    			}
    		}
    		int minID = 0;
        	double min = Math.abs(1.0 - E2[0]);
        	for(int i=1;i<T1;i++){
        		double value1 = Math.abs(1.0 - E2[i]);
        		if(value1 < min){
        			min = value1;
        			minID = i;
        		}
        	}
        	c = p2[minID];
        	double c1, c2;
    	}

    	//c = PseudoRandom.randDouble(p[minID[1]],p[minID[0]]);
   
    	if(c!=1.0){
        	if(cv < 0.15){
        		double rd = PseudoRandom.randDouble();
        		if(mean < 0 && rd < 0.95){
        			c = 1.0 - cv;
        		}else if(mean > 0 && rd < 0.95){
        			c = 1.0 + cv;
        		}
        	}/*else if(cv<0.2 && cv>=0.1){
        		double rd = PseudoRandom.randDouble();
        		if(mean < 0 && rd < 0.8){
        			c = 0.88;
        		}else if(mean > 0 && rd < 0.8){
        			c = 1.25;
        		}
        	}*/
    	}
    	//c=1.7;
    	return c;
    }

	public void updateNadirIdeal(SolutionSet solSet){
		int size = solSet.size();
		int numb = problem_.getNumberOfObjectives();
		for(int i=0; i<size; i++) {
			Solution sol = solSet.get(i);
			double normDistance = 0.0;
			double distance = 0.0;
			for (int j = 0; j < numb; j++) {
				normDistance += sol.getNormalizedObjective(j);
				distance +=sol.getNormalizedObjective(j);
			}

			sol.setDistanceToIdealPoint(normDistance);
			sol.setDistanceToNadirPoint(distance);
		}
	}

    public void mapping(SolutionSet solSet, double curvature){
    	double p = curvature;
    	int size = solSet.size();
    	int numb = problem_.getNumberOfObjectives();

    	for(int i=0; i<size; i++){
    		Solution sol = solSet.get(i);
    		double normDistance = 0.0;
    		double sumValue = 0.0;
    		double distance = 0.0;
    		for(int j=0; j<numb; j++){
    			normDistance += Math.pow(sol.getNormalizedObjective(j), p);
    			sumValue +=  sol.getNormalizedObjective(j);
    			distance += Math.pow(sol.getNormalizedObjective(j), 2);
    		}
    		normDistance = Math.pow(normDistance, 1/p);
    		distance = Math.sqrt(distance);
    		
    		sol.setDistanceToIdealPoint(normDistance);
    		sol.setDistanceToNadirPoint(distance);
    		sol.setSumValue(sumValue);
    		
    		if(sol.getDistanceToIdealPoint() == 0){
				//System.out.println("Error: This solution is in the origin");
				double dis = 0.0;
				/*for(int j=0; j<numb; j++){
					sol.setNormalizedObjective(j, 0.01);
					dis += Math.pow(sol.getNormalizedObjective(j), p);
				}
				dis = Math.pow(dis, 1/p);*/
				sol.setDistanceToIdealPoint(0.001);
				sol.setDistanceToNadirPoint(0.001);
				//System.exit(0);
			}
    		
    		for(int j=0; j<problem_.getNumberOfObjectives(); j++){
    			double value1 = sol.getNormalizedObjective(j)/sol.getDistanceToIdealPoint();
    			sol.setIthTranslatedObjective(j, value1);
    			double value2 = sol.getNormalizedObjective(j)/sol.getDistanceToNadirPoint();
    			sol.setUnitHypersphereObjective(j, value2);
    		}
    	}
    }
    
    public int permutation(int N, int M){
    	int result = 1;
    	int n = N;
    	int m = M;
    	
    	for(int i=m;i>0;i--){
    		result *= n;
    		n--;
    	}
    	
    	return result;
    }
    public int combination(int N, int M){
    	int result = 1;
    	int n = N;
    	int m = M;
    	
    	int half = n/2;
    	if(m > half){
    		m = n - m;
    	}
    	
    	int  numerator = permutation(n, m);
    	
    	int denominator = permutation(m, m);
    	
    	result = numerator/denominator;
    	
    	return result;
    }
    
    public void sss(){
    	int numb = 12;
    	int sumId = 0;
    	double p = 1.0;
    	double sumV = 0.0;
    	for(int m=1;m<=numb;m++){
    		int c = combination(numb,m);
    		sumId += c;
    		sumV += (((Math.pow(m,1.0-(1.0/p))-1.0)/Math.sqrt(numb))*(double)c);
    	}
    	
    	double E = sumV/sumId;
    }

}
