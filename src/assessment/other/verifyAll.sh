#entradas="novo/novo"\("0,0"\)" smpso/smpso"\("2,2"\)" smpso/smpso"\("2,3"\)" smpso/smpso"\("2,4"\)" smpso/smpso"\("3,2"\)" smpso/smpso"\("3,3"\)" smpso/smpso"\("3,4"\)" smpso/smpso"\("4,2"\)" smpso/smpso"\("4,3"\)" smpso/smpso"\("4,4"\)""

entradas=$(cat results/titles.txt | tail -1)

comando=""
EFS="
"
for nomeSaida in $entradas; do
	echo "-------------$nomeSaida-------------"
	for problem in dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9; do
		for objectives in 3 5 8 10 15 20; do
			comando=$comando"results/$nomeSaida""$problem-$objectives"_"fronts.txt "
		
			java -Xmx1G -cp assessment/other/ verifyFronts $comando
			
			unset comando;
		done
	done
done

