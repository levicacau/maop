#entradas="novo/novo"\("0,0"\)" smpso/smpso"\("2,2"\)" smpso/smpso"\("2,3"\)" smpso/smpso"\("2,4"\)" smpso/smpso"\("3,2"\)" smpso/smpso"\("3,3"\)" smpso/smpso"\("3,4"\)" smpso/smpso"\("4,2"\)" smpso/smpso"\("4,3"\)" smpso/smpso"\("4,4"\)""

entradas=$(cat results/titles.txt | tail -1)

comando=""
EFS="
"
for problem in dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7; do
# problem=$1
	rm -f "results/all-$problem-gd.txt"	
	for objectives in 2 3 5 8 10 15; do
	#for objectives in 2 3 5 8 9; do
		for nomeSaida in $entradas; do
			comando=$comando"results/$nomeSaida""$problem-$objectives"_"fronts.txt "
		done
		java -Xmx4G -cp assessment/metrics/ gdp $comando >> "results/all-$problem-gd.txt"

		unset comando;
		echo $EFS >> "results/all-$problem-gd.txt"
		echo $EFS >> "results/all-$problem-gd.txt"
		echo $EFS >> "results/all-$problem-gd.txt"
		echo $EFS >> "results/all-$problem-gd.txt"
		echo $EFS >> "results/all-$problem-gd.txt"
		echo $EFS >> "results/all-$problem-gd.txt"
		echo $EFS >> "results/all-$problem-gd.txt"

		echo "$problem-$objectives - GD"
	done
done
