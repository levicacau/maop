problems="tandem1 tandem2 tandem3 tandem4 tandem5 tandem6 tandem7 tandem8 tandem9 tandem10 tandem11 tandem12 tandem13 tandem14 tandem15 tandem16 tandem17 tandem18 tandem19 tandem20 tandem21 tandem22 tandem23 tandem24"
#problems="wfg6 wfg7 wfg8 wfg9"
objectives="2"


entradas=$(cat results/titles.txt | tail -1)

for problem in $problems; do #first step, verify if all the files exist
	for objective in $objectives; do 
		for nomeSaida in $entradas; do
			for ((run=0;run<30;run++)); do
		
				alg=`echo $nomeSaida | cut -d '/' -f 1`
				nameBase=`echo $nomeSaida | cut -d '/' -f 2`
				file=results/$alg/split/$nameBase$problem-$objective.$run
				
				if [ ! -f "$file""_fronts.txt" ]; then
					echo "file not found --> $file""_fronts.txt"
					#exit
					echo "running..."
					#echo "parameters/$nameBase$problem-$objective.$run.txt"
					./mopso.out "parameters/$nameBase$problem-$objective.$run.txt"
					echo "done."
				fi
				
				if [ $(cat "$file"_"fronts.txt" | wc -l) = 0 ]; then
					echo "file empty --> $file""_fronts.txt"
					#exit
					echo "running..."
					#echo "parameters/$nameBase$problem-$objective.$run.txt"
					./mopso.out "parameters/$nameBase$problem-$objective.$run.txt"
					echo "done."
				fi
				
				#if [ ! -f "$file""_solutions.txt" ]; then
				#	echo "file not found --> $file""_solutions.txt"
				#	#exit
				#	echo "running..."
				#	#echo "parameters/$nameBase$problem-$objective.$run.txt"
				#	./mopso.out "parameters/$nameBase$problem-$objective.$run.txt"
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

exit

for problem in $problems; do #if all is ok, concatenate the files
	for objective in $objectives; do 
		for nomeSaida in $entradas; do

			alg=`echo $nomeSaida | cut -d '/' -f 1`
			nameBase=`echo $nomeSaida | cut -d '/' -f 2`
			rm -f results/$alg/$nameBase$problem-$objective""_fronts.txt
			rm -f results/$alg/$nameBase$problem-$objective""_solutions.txt
		
			for ((run=0;run<30;run++)); do
		
				file=results/$alg/split/$nameBase$problem-$objective.$run
				cat $file""_fronts.txt >> results/$alg/$nameBase$problem-$objective""_fronts.txt
				cat $file""_solutions.txt >> results/$alg/$nameBase$problem-$objective""_solutions.txt
			done
			echo results/$alg/$nameBase$problem-$objective
		done
	done
done
