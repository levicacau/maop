#from the MOEADD directory:
export CLASSPATH=$CLASSPATH:src
#javac src/jmetal/problems/*.java
#javac src/jmetal/problems/ZDT/*.java
#javac src/jmetal/problems/DTLZ/*.java
#javac src/jmetal/problems/WFG/*.java
#javac src/jmetal/metaheuristics/fdea/FDEA_main.java


#problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
# problems="dtlz2"
problems="dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7"
objectives="2 3 5 8 10"
#objectives="2"
#variantes="ORIGINAL TSL-OneRun TSL2-OneRun TSL-After TSL2-After"
#variantes="ORIGINAL TSL-OneRun TSL-After"
# variantes="10 20 30 40 50"
variantes="FDEA FDEATESTE FastTea"

# problems="uf1 uf2 uf3 uf4 uf5 uf6 uf7 uf8 uf9 uf10"
#objectives="3"

for problem in $problems; do
	for objective in $objectives; do
        for variante in $variantes; do
	#for problem in $problems; do
		    for ((run=1;run<=30;run++)); do
			    echo $problem" "$objective" "$variante" "$run
			    mv "moeadd-$problem-$objective-$variante""_solutions.$run.txt" "$variante/$problem-$objective-$variante""_solutions.$run.txt"
		    done
        done
	done
done
echo "PRONTO!"