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

#include "../../variables.cpp"
#include "../../problem/Problem.cpp"
#include "../../Solution.cpp"
#include "../../Particle.cpp"
#include "../../Repository.cpp"
#include "../../Swarm.cpp"
#include "../../util.cpp"

bool isZero(double* array, int arraySize);
int main(int argc, char* argv[]){
	int maxSize=20000;
	objectiveNumber=2;
	decisionNumber=18;
	
	double dados_fronts[maxSize][maxDecisionNumber];
	double dados_sols[maxSize][maxDecisionNumber];
	Repository rep;
	sprintf(problem, "tandem1");//any problem, just to avoid errors
	rep.initialize("cd", 20000);
	Solution sol;
	printf("Don't mind this error:");
	sol.initialize();
	int swarmNumbers[]={1,50};
	
	for(int p=0;p<24;p++){ //24 tandem problems
		printf("\nprocessing... tandem%d\n", p+1);
		for(int run=0;run<30;run++){//30 runs
			for(int sw=0;sw<2;sw++){//2 swarm numbers
				
				char filename[1000]="";
				
				//"../../results/cmulti/split/cmulti(1)tandem1-2.0_fronts.txt"
				sprintf(filename, "../../results/cmulti/split/cmulti(%d)tandem%d-2.%d_fronts.txt", swarmNumbers[sw], p+1, run);
// 				printf("%s\n", filename);
				
	
		// 	for(int f=1;f<argc;f++){
// 				int size=lerArquivos(dados_fronts, argv[f]);
				int size=lerArquivos(dados_fronts, filename);
				
				char filenameSols[1000]="";
// 				strncpy(filenameSols, argv[f], strlen(argv[f])-10);
				strncpy(filenameSols, filename, strlen(filename)-10);
				sprintf(filenameSols, "%ssolutions.txt" ,filenameSols);
				lerArquivos(dados_sols, filenameSols);
						
				for(int s=0;s<size;s++){
					if(!isZero(dados_fronts[s], objectiveNumber)){
						
						if(dados_fronts[s][0] == 0){
							printf("\n %s (%d)\n", filename, s);
							printVector(dados_fronts[s], objectiveNumber);
							printf("\n\n");
							exit(1);
						}

						for(int o=0;o<objectiveNumber;o++){
							sol.objectiveVector[o]=dados_fronts[s][o];
						}
						for(int d=0;d<decisionNumber;d++){
							sol.decisionVector[d]=dados_sols[s][d];
						}
						rep.forceAdd(sol);
					}
				}
			}
		}
		char saida[1000]="";
		sprintf(saida, "tandem%d", p+1);
// 		fprintf(stderr, "tandem%d -- %d", p+1, rep.getActualSize());

		rep.organize();
		std::sort(rep.getSolutions(), rep.getSolutions()+rep.getActualSize(), compareSolutions(0)); //sort the solutions according to the first objectives, just a cosmetic step
		
		//printing the solutions found
		double vector[objectiveNumber+decisionNumber];
		for(int i=0;i<rep.getActualSize();i++){
			for(int o=0;o<objectiveNumber;o++){
				//printf("%5.12f ", rep.getSolution(i).objectiveVector[o]);
				vector[o]=rep.getSolution(i).objectiveVector[o];
			}
			for(int d=0;d<decisionNumber;d++){
				//printf("%5.12f ", rep.getSolution(i).decisionVector[d]);
				vector[d+objectiveNumber]=rep.getSolution(i).decisionVector[d];
			}
			printVectorToFile(vector, objectiveNumber+decisionNumber, saida);
		}
		
		printf("\nevaluating %d solutions...\n",rep.getActualSize());
		
		char saidaAll[1000]="";
		sprintf(saidaAll, "tandem%d_all", p+1);
		//printing all the solutions evaluated with all the problems
		double vectorAll[24+18];
		for(int i=0;i<rep.getActualSize();i++){
			for(int p=1;p<24;p++){
// 				sprintf(problem, "tandem%d", p);//setting the problem
				switch(p){
					case 1: {int ar[]={3,2,2,2,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVVVS
					case 2: {int ar[]={3,2,2,3,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVVES
					case 3: {int ar[]={3,2,2,4,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVVMS
					case 4: {int ar[]={3,2,2,5,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVVJS
					case 5: {int ar[]={3,2,3,2,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVEVS
					case 6: {int ar[]={3,2,3,3,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVEES
					case 7: {int ar[]={3,2,3,4,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVEMS
					case 8: {int ar[]={3,2,3,5,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVEJS
					case 9: {int ar[]={3,2,4,2,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVMVS
					case 10: {int ar[]={3,2,4,3,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVMES
					case 11: {int ar[]={3,2,4,4,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVMMS
					case 12: {int ar[]={3,2,4,5,6};memcpy(seq,ar,sizeof(int)*5);}break;//EVMJS
					case 13: {int ar[]={3,3,2,2,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEVVS
					case 14: {int ar[]={3,3,2,3,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEVES
					case 15: {int ar[]={3,3,2,4,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEVMS
					case 16: {int ar[]={3,3,2,5,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEVJS
					case 17: {int ar[]={3,3,3,2,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEEVS
					case 18: {int ar[]={3,3,3,3,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEEES
					case 19: {int ar[]={3,3,3,4,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEEMS
					case 20: {int ar[]={3,3,3,5,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEEJS
					case 21: {int ar[]={3,3,4,2,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEMVS
					case 22: {int ar[]={3,3,4,3,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEMES
					case 23: {int ar[]={3,3,4,4,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEMMS
					case 24: {int ar[]={3,3,4,5,6};memcpy(seq,ar,sizeof(int)*5);}break;//EEMJS
					default:
						printf("\nERROR! on problem ID of tandem (%s) (%d)\n", problem, p);
						exit(1);
				}
				
				rep.getSolution(i).evaluate();
				vectorAll[p-1]=rep.getSolution(i).objectiveVector[0];
			}
// 			printf("\nevaluated with 24 problems:");
// 			printVector(vectorAll, 24);
// 			printf("\n");
			for(int d=0;d<decisionNumber;d++)
				vectorAll[d+24]=rep.getSolution(i).decisionVector[d];
			printVectorToFile(vectorAll, objectiveNumber+decisionNumber, saidaAll);
		}
		
		rep.clear();
	}
	
	return 0;
}
bool isZero(double* array, int arraySize){
	for(int i=0;i<arraySize;i++)
		if(array[i] != 0)
			return false;
	
	return true;
}