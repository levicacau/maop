# #entradas="novo/novo"\("0,0"\)" smpso/smpso"\("2,2"\)" smpso/smpso"\("2,3"\)" smpso/smpso"\("2,4"\)" smpso/smpso"\("3,2"\)" smpso/smpso"\("3,3"\)" smpso/smpso"\("3,4"\)" smpso/smpso"\("4,2"\)" smpso/smpso"\("4,3"\)" smpso/smpso"\("4,4"\)""
# problems="tandem1 tandem2 tandem3 tandem4 tandem5 tandem6 tandem7 tandem8 tandem9 tandem10 tandem11 tandem12 tandem13 tandem14 tandem15 tandem16 tandem17 tandem18 tandem19 tandem20 tandem21 tandem22 tandem23 tandem24"
# 
# 
# entradas=$(cat results/titles.txt | tail -1)
# 
# comando=""
# EFS="
# "
# for nomeSaida in $entradas; do
# 	echo "-------------$nomeSaida-------------"
# 	for problem in $problems; do
# 		for objectives in 2; do
# 			for ((run=0;run<30;run++)); do
# 				comando=$comando"results/$nomeSaida""$problem-$objectives".$run_"fronts.txt "
# 			
# 				java -Xmx1G -cp assessment/other/ checkTandem $comando
# 				
# 				unset comando;
# 			done
# 		done
# 	done
# done

problems="tandem1 tandem2 tandem3 tandem4 tandem5 tandem6 tandem7 tandem8 tandem9 tandem10 tandem11 tandem12 tandem13 tandem14 tandem15 tandem16 tandem17 tandem18 tandem19 tandem20 tandem21 tandem22 tandem23 tandem24"
objectives="2"

entradas=$(cat results/titles.txt | tail -1)

for problem in $problems; do #first step, verify if all the files exist
echo checking... $problem
	for objective in $objectives; do 
		for nomeSaida in $entradas; do
			for ((run=0;run<30;run++)); do
		
				alg=`echo $nomeSaida | cut -d '/' -f 1`
				nameBase=`echo $nomeSaida | cut -d '/' -f 2`
				file=results/$alg/split/$nameBase$problem-$objective.$run
				
				java -Xmx1G -cp assessment/other/ checkTandem $file"_fronts.txt"
				
			done
		done
	done
done