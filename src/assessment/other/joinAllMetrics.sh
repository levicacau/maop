problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
# problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7"
objectives="3 5 8 10 15 20"
# metrics="gdp igdp hv r2"
metrics="hv"
EFS="
"

for problem in $problems; do #first step, verify if all the files exist
	for metric in $metrics; do
		for objective in $objectives; do 
			
			file="results/all-$problem-$metric.$objective.txt"
			if [ ! -f $file ]; then
				echo "file not found --> $file"
				exit
			fi
			if [ $(cat "$file" | wc -l) = 0 ]; then
				echo "file empty --> $file"
				exit
			fi
				
		done
	done
done
echo "All ok, merging files ..."

for problem in $problems; do #first step, verify if all the files exist
	for metric in $metrics; do
		rm -f "results/all-$problem-$metric.txt"
		for objective in $objectives; do 
			
			file="results/all-$problem-$metric.$objective.txt"
			cat $file >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
			echo $EFS >> "results/all-$problem-$metric.txt"
				
		done
	done
done