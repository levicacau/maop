#entradas="novo/novo"\("0,0"\)" smpso/smpso"\("2,2"\)" smpso/smpso"\("2,3"\)" smpso/smpso"\("2,4"\)" smpso/smpso"\("3,2"\)" smpso/smpso"\("3,3"\)" smpso/smpso"\("3,4"\)" smpso/smpso"\("4,2"\)" smpso/smpso"\("4,3"\)" smpso/smpso"\("4,4"\)""

basePath=results
pathHv=src/assessment/metrics/

entradas=$(cat $basePath/titles.txt | tail -1)

comando=""
EFSddd="
"
#for problem in dtlz1 dtlz4 dtlz7 wfg1 wfg4 wfg6; do
for problem in $1; do
#problem=$1
	rm -f "$basePath/all-$problem-hv.txt"
	for objectives in 2 3 5 8 10 15; do
# 	for objectives in 15; do
	#for objectives in 2 3 5 8 9; do
		for nomeSaida in $entradas; do
			comando=$comando"$basePath/$nomeSaida""$problem-$objectives"_"fronts.txt "
		done
		java -Xmx1G -cp $pathHv hv $comando >> "results/all-$problem-hv.txt"
#		java -Xmx1G hv $comando >> "$basePath/all-$problem-hv.txt"

		unset comando;
		echo $EFS >> "$basePath/all-$problem-hv.txt"
		echo $EFS >> "$basePath/all-$problem-hv.txt"
		echo $EFS >> "$basePath/all-$problem-hv.txt"
		echo $EFS >> "$basePath/all-$problem-hv.txt"
		echo $EFS >> "$basePath/all-$problem-hv.txt"
		echo $EFS >> "$basePath/all-$problem-hv.txt"
		echo $EFS >> "$basePath/all-$problem-hv.txt"

		echo "$problem-$objectives - HV"
	done
done
