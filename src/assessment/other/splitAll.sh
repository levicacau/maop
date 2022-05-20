problems="wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
objectives="3 5 8 10 15 20"
metrics="gdp igdp hv r2"
# metrics="gdp igdp r2"

colunas="true false true false false false true"

EFS="
"

for problem in $problems; do
	for metric in $metrics; do
			
			file="results/all-$problem-$metric.txt"
			java -cp assessment/other splitAlgorithms $file "$colunas" > results/all-"sm-"$problem-$metric.txt
	done
done