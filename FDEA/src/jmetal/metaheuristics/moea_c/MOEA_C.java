//  MOEA/C.java
//
//  Author:

//
//  Copyright 
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.moea_c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jmetal.core.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.ranking.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.SumValueComparator;
import jmetal.util.ranking.NondominatedRanking;

/**
 * 
 */

public class MOEA_C extends Algorithm {
	private SolutionSet population_;
	private SolutionSet offspringPopulation_;
	private SolutionSet union_;
	
	private int populationSize_;
	int generations_;
	int maxGenerations_;
	
	Operator selection_;
	Operator crossover_;
	Operator mutation_;
	
	private double[] zideal_; //ideal point
	private double[] znadir_;//Nadir point
	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public MOEA_C(Problem problem) {
		super(problem);
		zideal_ = new double[problem.getNumberOfObjectives()];
		znadir_ = new double[problem.getNumberOfObjectives()];
	}

	/**
	 * Runs the MOEA/C algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws JMException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		/*
		 * step1: Basic Setting of this Algorithm
		 */
		baiscSetting();
		/*
		 * step2: Initialize the Population
		 */
		initPopulation();

	

		/*
		 * Enter the main loop??into the process of evolution
		 */
		while (generations_ < maxGenerations_) {
			/*
			 * step3 and step4:Mating Selection and Recombination to generate Offspring Populations
			 */
			generateOffspringPopulation();
			/*
			 * step5:Environmental Selection
			 */
		    environmentalSelection();
		    
		    generations_++;
		}
		
		Ranking ranking = new NondominatedRanking(population_);
		return ranking.getSubfront(0);
	} // execute
	
	/*
	 * step1: Basic Setting of this Algorithm
	 */
	public void baiscSetting(){
		generations_ = 0;
		maxGenerations_ = ((Integer) this.getInputParameter("maxGenerations")).intValue();
		populationSize_ = ((Integer) this.getInputParameter("populationSize")).intValue();
		mutation_  = operators_.get("mutation");
		crossover_ = operators_.get("crossover");
		selection_ = operators_.get("selection");
	}
	
	/*
	 * step2: Initialize the Population
	 */
	public void initPopulation() throws JMException, ClassNotFoundException {

		population_ = new SolutionSet(populationSize_);

		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			population_.add(newSolution);
			//generations_++;
		} // for
		estimateIdealPoint(population_);
		estimateNadirPoint(population_);
		normalizationObjective(population_);
		computeDistanceToIdealPoint(population_);
	} // initPopulation
	
	/*
	 * step3 and step4:Mating Selection and Recombination to generate Offspring Populations
	 */
	public void generateOffspringPopulation() throws JMException{
		offspringPopulation_ = new SolutionSet(populationSize_);
		Solution[] parents = new Solution[2];
		for (int i = 0; i < (populationSize_); i++) {
			double delta = Math.pow(1.0-(double)generations_/maxGenerations_, 0.7);
			// obtain parents
			/*parents = (Solution[]) selection_
					.execute(population_);*/
			parents[0] = (Solution) selection_.execute(population_);
			parents[1] = (Solution) selection_.execute(population_);
			Solution offSpring = (Solution) crossover_
					.execute(parents,delta);
			mutation_.execute(offSpring,delta);
			problem_.evaluate(offSpring);
			problem_.evaluateConstraints(offSpring);
			offspringPopulation_.add(offSpring);
			//generations_++;
		} // for
	}
	
	/*
	 * step5:Environmental Selection
	 */
	public void environmentalSelection(){
		/*
		 * step5.1:Combine the Population and the Offspring Population
		 */
		union_ = ((SolutionSet) population_).union(offspringPopulation_);
		/*
		 * step5.2:Normalization the Combined Population
		 */
		//Ranking nodominatedRanking = new NondominatedRanking(union_);
		SolutionSet[] st = getStSolutionSet(union_,populationSize_);
		estimateIdealPoint(st[1]);
		estimateNadirPoint(st[1]);
		//estimateIdealPoint(union_);
		//estimateNadirPoint(union_);
		normalizationObjective(union_);
		
		
		/*
		 * step5.3:Compute the Convergence Distance of each Solution
		 */
		computeDistanceToIdealPoint(st[1]);
		distrectionMaping(st[1]);
		
		population_.clear();
        List<SolutionSet> list = new <SolutionSet>ArrayList();
        for(int i=0;i<st[1].size();i++){
			SolutionSet sols = new SolutionSet();
			sols.add(st[1].get(i));
			list.add(sols);
		}	
    	/*
		 * step5.5:Agglomerative Hierarchical Clustering Based Average-Link Method 
		 * and K-Cluster Stopping Condition
		 */
        list = new HierarchicalClusteringInUnitHP(list).clusteringAnalysis(populationSize_);	
        /*
		 * Step5.6:Choose the Best Solution in each Cluster and Stored it into the next Generation
		 */
		bestSolutionSelection(list);	
			
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
				}
			}
			
		}
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
				}
			}
			
		}
	}
	
    /*
     * Normalization
     */
	public void normalizationObjective(SolutionSet solutionSet){
		for(int i=0; i<solutionSet.size(); i++){
			Solution sol = solutionSet.get(i);
			
			for(int j=0; j<problem_.getNumberOfObjectives(); j++){
				double val = 0.0;
				//val = (sol.getObjective(j) - zideal_[j])/(znadir_[j]-zideal_[j]);
				val = (sol.getObjective(j) - zideal_[j]);
				//sol.setNormalizedObjective(j, val);
				sol.setNormalizedObjective(j, sol.getObjective(j));
			}
		}
	}
	
	public void distrectionMaping(SolutionSet solutionSet){
		for(int i=0; i<solutionSet.size(); i++){
			Solution sol = solutionSet.get(i);
			for(int j=0; j<problem_.getNumberOfObjectives(); j++){
				double val = 0.0;
				val = sol.getNormalizedObjective(j)/sol.getSumValue();
				sol.setUnitHyperplaneObjective(j, val);		
			}
		}
	}
	
	 /*
     * Compute the Convergence Distance of each Solutions Which use the distance of 
     * each solution to the Ideal Point
     */
    public void computeDistanceToIdealPoint(SolutionSet solutionSet){
    	for(int i=0; i<solutionSet.size(); i++){
    		Solution sol = solutionSet.get(i);
    		double normDistance = 0.0;
    		double sumValue = 0.0;
    		for(int j=0; j<problem_.getNumberOfObjectives(); j++){
    			normDistance += sol.getNormalizedObjective(j) * sol.getNormalizedObjective(j);
    			sumValue +=  sol.getNormalizedObjective(j);
    		}
    		normDistance = Math.sqrt(normDistance);
    		
    		sol.setDistanceToIdealPoint(normDistance);
    		sol.setSumValue(sumValue);
    	}
    }
    
	public double weightSumValue(Solution solution){
		double value = 0.0;
		for(int i=0; i<problem_.getNumberOfObjectives();i++){
			value += solution.getNormalizedObjective(i);
		}
		return value;
	}
	public double weightSumValue(Solution solution1,Solution solution2){
		double value = 0.0;
		double[] lamda = new double[problem_.getNumberOfObjectives()];
		double sum = weightSumValue(solution1);
		for(int i=0; i<problem_.getNumberOfObjectives();i++){
			lamda[i] = solution1.getNormalizedObjective(i)/sum;
			value += lamda[i]*solution2.getNormalizedObjective(i);
		}
		return value;
	}
	
	public double computeChebyshev(Solution individua2, Solution individua1){
		double fitness;
		fitness = 0.0;

		double maxFun = -1.0e+30;

		for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
			double diff = Math.abs(individua1.getNormalizedObjective(n));

			double feval;
			if (individua2.getNormalizedObjective(n) == 0) {
				feval = diff / 0.000001;
			} else {
				feval = diff / individua2.getNormalizedObjective(n);
			}
			if (feval > maxFun) {
				maxFun = feval;
			}
		} // for

		fitness = maxFun;
		return fitness;
	}
	
	public double computeASFFitness(Solution s1,Solution s2){
		double fitness = -1.0e+30;
		int objectiveSize = s1.getNumberOfObjectives();
		double[] lambda = new double[objectiveSize];
		double sumValue = 0.0;
		for(int i=0; i<objectiveSize; i++){
			sumValue += s1.getNormalizedObjective(i);
		}
		for(int j=0; j<objectiveSize; j++){
			lambda[j] = s1.getNormalizedObjective(j)/sumValue;
			if(lambda[j] == 0){
				lambda[j] = 0.000001;
			}
		}
		for(int k=0; k<objectiveSize; k++){
			double sb = s2.getNormalizedObjective(k)/lambda[k];
			if(fitness < sb){
				fitness = sb;
			}
		}
		return fitness;
	}
	
	 /*
     * Compute the angle value between Solution1 and Solution2
     */
	public double computeAngle(Solution s1, Solution s2){
		double angle = 0.0;
		double distanceToidealPoint1 = s1.getDistanceToIdealPoint();
		double distanceToidealPoint2 = s2.getDistanceToIdealPoint();
		double innerProduc = 0.0; 
		for(int i=0; i<problem_.getNumberOfObjectives(); i++){
			innerProduc += s1.getNormalizedObjective(i) * s2.getNormalizedObjective(i);
		}
		double value = innerProduc/(distanceToidealPoint1*distanceToidealPoint2);
		if(value > 1.0){
			value = 1.0;
		}
		angle = Math.acos(Math.abs(value));
		//System.out.println(Math.abs(innerProduc/(distanceToidealPoint1*distanceToidealPoint2)));
		return angle;
	}//computeAngle
    
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
    
    
    public void bestSolutionSelection(List<SolutionSet> list){
    	for(int k=0; k<problem_.getNumberOfObjectives();k++){
    		double minClustering2Axis = 1.0e+30;
      		int minClustering2AxisID = -1;
      		for(int i=0;i<list.size();i++){
      			SolutionSet sols = list.get(i);
      			if(sols.size() == 0){
      				System.out.println("SolsSize1 = "+sols.size());
      				System.exit(0);
      			}
      			
      			double angle1 = Math.acos(Math.abs(sols.getCentroid().getNormalizedObjective(k)/sols.getCentroid().getDistanceToIdealPoint()));
      			//System.out.println(angle1);
      			if(angle1 < minClustering2Axis){
      				minClustering2Axis = angle1;
      				minClustering2AxisID = i;
      			}//if
      		}//for
      		double minSolution2Axis = 1.0e+30;
      		int minSolution2AxisID = -1;
      		for(int j=0;j<list.get(minClustering2AxisID).size();j++){
      			Solution sol = list.get(minClustering2AxisID).get(j);
      			double ang = Math.acos(list.get(minClustering2AxisID).get(j).getNormalizedObjective(k)/list.get(minClustering2AxisID).get(j).getDistanceToIdealPoint());
      			if(ang < minSolution2Axis){
      				minSolution2Axis = ang;
      				minSolution2AxisID = j;
      			}
      		}//for
      		population_.add(list.get(minClustering2AxisID).get(minSolution2AxisID));
      		list.remove(minClustering2AxisID);
    	}
  		double delta_ = 0.5;
  		Iterator<SolutionSet> it = list.iterator();
  		while(it.hasNext()){
  			int type = -1;
  			double rnd = PseudoRandom.randDouble();
  			if (rnd < delta_) 
  			{
  				type = 1; 
  			} else {
  				type = 2;
  			}
  			SolutionSet sols = it.next();
  			double minFitness = 1.0e+30;
  			int minFitnessID = -1;
  			Solution sol1 = sols.getCentroidVector();
  			for(int j=0;j<sols.size();j++){
  				Solution sol2 = sols.get(j);
  				if(type == 1){
  					//double fitness = sol2.getDistanceToIdealPoint();
  					double fitness = sol2.getSumValue();
  					//System.out.println("fitness = " + fitness);
  					//double fitness = computeDistance(sol2);
  					//double fitness = computeASFFitness(sol1,sol2);
  					//double fitness = weightSumValue(sol1,sol2);
  					//double fitness = computeChebyshev(sol1,sol2);
  					//double fitness = computePBIFitness(sol1,sol2);
  					if(minFitness > fitness){
  						minFitness = fitness;
  						minFitnessID = j;
  					}
  				}else{
  					//double fitness = weightSumValue(sol1,sol2);
  					//System.out.println("fitness = " + fitness);
  					//double fitness = computePBIFitness(sol1,sol2);
  					//double fitness = computeAngle(sol1,sol2);
  					double fitness = computeChebyshev(sol1,sol2);
  					//double fitness = computeASFFitness(sol1,sol2);
  					//System.out.println(fitness);
  					if(minFitness > fitness){
  						minFitness = fitness;
  						minFitnessID = j;
  					}
  				}
  			}//for
  			//System.out.println(sols.size());
  			//System.out.println(minFitness);
  			population_.add(sols.get(minFitnessID));
  			it.remove();
  		}//while
  		if(list.size() != 0){
  			System.out.println("ListSize2 = "+list.size());
  			System.exit(0);
  		}
     }
    
} // NSGA-II
