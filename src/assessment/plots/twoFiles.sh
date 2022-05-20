case "$1" in #determinando a metrica a partir do parametro de entrada
    2 ) metric="Global repository size";;
    4 ) metric="Total solution updates";;
    6 ) metric="Average inner distance in Objective space";;
    8 ) metric="Average inner distance in Decision space";;
    * ) echo "Error, second parameter must be 2,4,6 or 8"; exit ;;
esac

# for ((idx=0;idx<10;idx++)); do

	plot="set terminal eps enhanced color;\
	set output 'comparison_$1.eps';\
	set xlabel 'Iterations'; \
	set ylabel '$metric' ;\
	set key below ;\
	plot for [idx=0:8] 'output_moead.txt' index idx using $1 with lines notitle linecolor 1, for [idx=0:8] 'output_cmaes.txt' index idx using $1 with lines notitle linecolor 2,
	'output_moead.txt' index 9 using $1 with lines title 'MOEA/D-DE' linecolor 1, 'output_cmaes.txt' index 9 using $1 with lines title 'MOEA/D-CMA-ES' linecolor 2"

	gnuplot -e "$plot"
	
# done