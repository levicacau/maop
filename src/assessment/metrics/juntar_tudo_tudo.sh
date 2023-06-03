 problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
 problems=${problems^^}
 basePath=./src/assessment/metrics
 for problem in $problems; do
#for problem in dtlz1; do
	#./assessment/metrics/juntar_tudo_gd.sh $problem &
  #./juntar_tudo_igd.sh $problem &
	#./assessment/metrics/juntar_tudo_r2.sh $problem &
	./juntar_tudo_hv.sh $problem &
#	$basePath/juntar_tudo_hv.sh $problem
done
