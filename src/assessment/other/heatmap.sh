mkdir -p img
# for ((gen=0;gen<100;gen++)); do
# 	plot="set terminal eps size 17,5 enhanced color;\
# 	set output 'img/htm-$gen.eps';\
# 	set multiplot layout 1, 2 title 'Gen. $gen' ;\
# 	set palette gray;\
# 	set xrange [-0.5:200.5];\
# 	set yrange [-0.5:200.5];\
# 	set title 'Before';\
# 	plot '../../probSolsB-$gen.txt' matrix with image;\
# 	set title 'After';\
# 	plot '../../probSolsA-$gen.txt' matrix with image;\
# 	unset multiplot;"
# 	
# 	gnuplot -e "$plot"
# done

for ((gen=0;gen<100;gen++)); do
	plot="set terminal eps enhanced color;\
	set output 'img/htm-$gen.eps';\
	set palette gray;\
	set xrange [-0.5:200.5];\
	set yrange [-0.5:200.5];\
	set title '';\
	plot '../../probSolsA-$gen.txt' matrix with image;"
	
	gnuplot -e "$plot"
done

plot="set terminal eps enhanced color;\
set key below ;\
set palette gray;\
set output 'img/likelihood.eps';\
plot '../../saida.txt' using 1 with points title 'Average log likelihood';\
set output 'img/quality.eps';\
plot '../../saida.txt' using 2 with points title 'Average PBI from the solutions generated', '../../saida.txt' using 3 title 'Average PBI from the best solutions so far'"
gnuplot -e "$plot"







# #
# # Gnuplot version 5.0 demo of multiplot auto-layout capability
# #
# #
# set multiplot layout 3, 1 title "Multiplot layout 3, 1" font ",14"
# set tmargin 2
# set title "Plot 1"
# unset key
# plot sin(x)/x
# #
# set title "Plot 2"
# unset key
# plot 'silver.dat' using 1:2 ti 'silver.dat'
# #
# set style histogram columns
# set style fill solid
# set key autotitle column
# set boxwidth 0.8
# set format y "    "
# set tics scale 0
# set title "Plot 3"
# plot 'immigration.dat' using 2 with histograms, \
#      '' using 7  with histograms , \
#      '' using 8  with histograms , \
#      '' using 11 with histograms 
# #
# unset multiplot
# #
# #
# #