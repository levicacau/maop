# metrics="gd igd r2 hv"
metrics="hv"
# problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
#problems="dtlz1 dtlz2 dtlz3 dtlz4 wfg1 wfg2 wfg3 wfg4 wfg5 wfg6 wfg7 wfg8 wfg9"
problems="dtlz1 dtlz2 dtlz3 dtlz4 dtlz5 dtlz6 dtlz7"

mkdir -p ../../../results/img

for problem in problems.split(' '):
	for metric in $metrics; do
		#./boxplot.sh "../../results/up_to_20/all-$problem-$metric.txt"
		#./linhas.sh "../../results/up_to_20/all-$problem-$metric.txt"
		python3 boxplot.py "../../results/all-$problem-$metric.txt"
		python3 linhas.py "../../results/all-$problem-$metric.txt"
	done
	#./gdXigd.sh "../../results/all-$problem-gd.txt" "../../results/all-$problem-igd.txt"
done
