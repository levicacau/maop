problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
#problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7"
#problems="wfg6 wfg7 wfg8 wfg9"
objectives="2 3 5 8 10 15"

basePath=results

entradas=$(cat "$basePath/titles.txt" | tail -1)

problems=${problems^^}

for problem in $problems; do #first step, verify if all the files exist
	for objective in $objectives; do
		echo checking $problem - $objective ...
		for nomeSaida in $entradas; do
			for ((run=1;run<31;run++)); do
				alg=`echo $nomeSaida | cut -d '/' -f 1`
				nameBase=`echo $nomeSaida | cut -d '/' -f 1`
				file=$basePath/$alg/split/$problem-$objective-$nameBase
				paramFile="parameters/$problem-$objective-$nameBase.txt"

				if [ ! -f "$file""_fronts.$run.txt" ]; then
					echo "file not found --> $file""_fronts.$run.txt"
					exit
# 					echo "running..."
# 					echo "parameters/$nameBase$problem-$objective.txt"
# 					./mopso.out "parameters/$nameBase$problem-$objective.txt"
# 					qsub assessment/other/runQsub.sh "./mopso.out \"$paramFile\" $run"
# 					echo "done."

				elif [ $(cat "$file"_"fronts.$run.txt" | wc -l) -le 0 ]; then # -le less than or equal --- 3, one solution and two blank lines
					echo "file empty or with only one solution --> $file""_fronts.$run.txt"
					exit
# 					echo "running..."
# 					echo "parameters/$nameBase$problem-$objective.txt"
# 					./mopso.out "parameters/$nameBase$problem-$objective.txt"
# 					qsub assessment/other/runQsub.sh "./mopso.out \"$paramFile\" $run"
# 					echo "done."
				fi

				#if [ ! -f "$file""_solutions.txt" ]; then
				#	echo "file not found --> $file""_solutions.txt"
				#	#exit
				#	echo "running..."
				#	#echo "parameters/$nameBase$problem-$objective.txt"
				#	./mopso.out "parameters/$nameBase$problem-$objective.txt"
				#	echo "done."
				#fi

# 				if [ $(cat "$file"_"solutions.txt" | wc -l) == 0 ]; then
# 					echo "file empty --> $file""_fronts.txt"
# 					exit
# 				fi

			done
		done
	done
done

echo "All ok, merging files ..."

for problem in $problems; do #if all is ok, concatenate the files
	for objective in $objectives; do
		echo merging $problem - $objective ...
		for nomeSaida in $entradas; do

			alg=`echo $nomeSaida | cut -d '/' -f 1`
			nameBase=`echo $nomeSaida | cut -d '/' -f 1`
			rm -f $basePath/$alg/$nameBase$problem-$objective""_fronts.txt
			rm -f $basePath/$alg/$nameBase$problem-$objective""_solutions.txt

			for ((run=1;run<31;run++)); do

				file=$basePath/$alg/split/$problem-$objective-$nameBase
				cat $file""_fronts.$run.txt >> $basePath/$alg/$problem-$objective""_fronts.txt
				cat $file""_solutions.$run.txt >> $basePath/$alg/$problem-$objective""_solutions.txt

				echo "" >> $basePath/$alg/$problem-$objective""_fronts.txt
				echo "" >> $basePath/$alg/$problem-$objective""_fronts.txt
				echo "" >> $basePath/$alg/$problem-$objective""_solutions.txt
				echo "" >> $basePath/$alg/$problem-$objective""_solutions.txt
			done
# 			echo results/$alg/$nameBase$problem-$objective
		done
	done
done
