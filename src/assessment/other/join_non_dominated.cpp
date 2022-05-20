//Algorithm created to join all the non-dominated solutions from several executions
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <values.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <iostream>
#include <algorithm>
#include <unistd.h>

#include "../../additionalCode/cma-es/eda.h"
#include "../../additionalCode/cobyla/cobyla.c"
#include "../../variables.cpp"
#include "../../Problem.cpp"
#include "../../Solution.cpp"
#include "../../Particle.cpp"
#include "../../Repository.cpp"
#include "../../additionalCode/cma-es/eda.cpp"
#include "../../Swarm.cpp"
#include "../../util.cpp"
#include "../../additionalCode/rBOA/eda.cpp"

bool isZero(double* array, int arraySize);
int main(int argc, char* argv[]){
	int maxSize=3000000;
	int maxDecisionNumber=20;
	decisionNumber=maxDecisionNumber;
	objectiveNumber=countColumns(argv[1]);
	inferiorPositionLimit = new double[maxDecisionNumber];
	superiorPositionLimit = new double[maxDecisionNumber];
	
	//initialize variables to store the decision and objective vectors
	double **dados_fronts, **dados_sols;
	dados_fronts = new double*[maxSize];
	dados_sols = new double*[maxSize];
	for(int i=0; i<maxSize;i++){
		dados_fronts[i] = new double[objectiveNumber];
// 		dados_sols[i] = new double[maxDecisionNumber];
	}
		
	Repository rep;
	sprintf(problemName, "dtlz1");//any problem, just to avoid errors
	rep.initialize("unbounded", 1);//any repository, just to avoid errors
	Solution sol;
	
	for(int f=1;f<argc;f++){
		int size=readFile(dados_fronts, argv[f]);
				
// 		char filenameSols[1000]="";
// 		strncpy(filenameSols, argv[f], strlen(argv[f])-10);
// 		strncpy(filenameSols, filename, strlen(filename)-10);
// 		sprintf(filenameSols, "%ssolutions.txt" ,filenameSols);
// 		readFile(dados_sols, filenameSols);
		
		for(int s=0;s<size;s++){
			if(!isZero(dados_fronts[s], objectiveNumber)){
				
// 				if(dados_fronts[s][0] == 0){
// 					printf("\n %s (%d)\n", argv[f], s);
// 					printVector(dados_fronts[s], objectiveNumber);
// 					printf("\n\n");
// 					exit(1);
// 				}

				for(int o=0;o<objectiveNumber;o++){
					sol.objectiveVector[o]=dados_fronts[s][o];
				}
// 				sol.print();
				
// 				for(int d=0;d<decisionNumber;d++){
// 					sol.decisionVector[d]=dados_sols[s][d];
// 				}
// 				rep.forceAdd(sol);
				rep.add(sol);
			}else{
				fprintf(stderr, "WARNING!, vector with only zeros, check the fronts \"%s\"\n", argv[f]);
// 				exit(1);
			}
		}
	}
// 	printf("size: %d\n", rep.getActualSize());
	rep.organize();
	std::sort(rep.getSolutions(), rep.getSolutions()+rep.getActualSize(), compareSolutions(0)); //sort the solutions according to the first objectives, just a cosmetic step
	
	for(int i=0;i<rep.getActualSize();i++){
		for(int o=0;o<objectiveNumber;o++){
			printf("%.13f ", rep.getSolution(i).objectiveVector[o]);
		}
		printf("\n");
	}
	return 0;
}
bool isZero(double* array, int arraySize){
	for(int i=0;i<arraySize;i++)
		if(array[i] != 0)
			return false;
	
	return true;
}
void Problem::calculateCoco(double* decisionVector, double* objectiveVector){}