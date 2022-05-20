data=$(java medias_linhas $1)

mkdir -p ../../results/img

#algoritmos="H-MOPSO SMPSO CD-Ideal CD-MGA NWSum-CD NWSum-Ideal NWSum-MGA Sigma-CD Sigma-Ideal Sigma-MGA"
algoritmos=$(cat ../../results/titles.txt | head -1)

colunas=0
for c in $algoritmos; do #contando as colunas do arquivo a partir da entrada
	let colunas=colunas+1
done
case "$(echo $1 | tr 'a-z' 'A-Z')" in #determinando o problema a partir do nome do arquivo de entrada
    *DTLZ1* ) problem="DTLZ1";;
    *DTLZ2* ) problem="DTLZ2";;
    *DTLZ3* ) problem="DTLZ3";;
    *DTLZ4* ) problem="DTLZ4";;
    *DTLZ5* ) problem="DTLZ5";;
    *DTLZ6* ) problem="DTLZ6";;
    *DTLZ7* ) problem="DTLZ7";;
    *WFG1* ) problem="WFG1";;
    *WFG2* ) problem="WFG2";;
    *WFG3* ) problem="WFG3";;
    *WFG4* ) problem="WFG4";;
    *WFG5* ) problem="WFG5";;
    *WFG6* ) problem="WFG6";;
    *WFG7* ) problem="WFG7";;
    *WFG8* ) problem="WFG8";;
    *WFG9* ) problem="WFG9";;
    * ) echo "Error, undetermined problem!"; exit ;;
esac
case "$(echo $1 | tr 'a-z' 'A-Z')" in #determinando a metrica a partir do nome do arquivo de entrada
    *IGD* ) metric="IGD_p";;
    *GD* ) metric="GD_p";;
    *R2* ) metric="R_2";;
    *HV* ) metric="Hypervolume";;
    * ) echo "Error, undetermined metric!"; exit ;;
esac

#set xtics ('2' 0, '3' 1, '5' 2, '10' 3, '15' 4, '20' 5);\
#set xtics ('2' 0, '3' 1, '5' 2, '8' 3, '9' 4);\
#set xtics ('3' 0, '5' 1, '8' 2, '10' 3, '15' 4, '20' 5);\
#set yrange [0:0.6]; \

echo "$data" > temp
plot="set terminal eps size 7,5 enhanced color;\
set xtics ('3' 0, '5' 1, '8' 2, '10' 3, '15' 4, '20' 5);\
set title '$problem';\
set xlabel 'Objectives'; \
set ylabel '$metric' ;\
set output '../../results/img/plot-$problem-$metric.eps';\
titles= '$algoritmos aa' ;\
styles= '1 2 3 4 7 8 9 10 11 12 13 16 17 18 19 20 21 22 25 26 27 28 29 30 31 34 35 36 37 38 39 40';\
set key below ;\
set format y '%.2E' ;\
plot for [idx=1:$colunas] 'temp' index idx-1 title word(titles, idx) with linespoints lw 3 lt word(styles, idx)"

gnuplot -e "$plot"

rm temp
