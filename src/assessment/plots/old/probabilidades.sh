EFS="
"
data=$(java probabilidades_medias $1)

mkdir ../../results/prob_img

algoritmos="CD-Ideal NWSum-Ideal Sigma-Ideal CD-MGA NWSum-MGA Sigma-MGA CD-CD NWSum-CD Sigma-CD"
#algoritmos=$(cat ../../results/titles.txt | head -1)

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
    * ) echo "Error, undetermined problem!"; exit ;;
esac
case "$(echo $1 | tr 'a-z' 'A-Z')" in #determinando o numero de objetivos a partir do nome do arquivo de entrada
	*-2_* ) objNome="2";;
	*-3_* ) objNome="3";;
	*-5_* ) objNome="5";;
	*-10_* ) objNome="10";;
	*-15_* ) objNome="15";;
	*-20_* ) objNome="20";;
	*-8_* ) objNome="8";;
	*-9_* ) objNome="9";;
	* ) echo "Error, undetermined objective number!"; exit ;;
esac
# case "$(echo $1 | tr 'a-z' 'A-Z')" in #determinando a metrica a partir do nome do arquivo de entrada
#     *IGD* ) metric="IGD_p";;
#     *GD* ) metric="GD_p";;
#     *R2* ) metric="R_2";;
#     *HV* ) metric="Hypervolume";;
#     * ) echo "Error, undetermined metric!"; exit ;;
# esac
metric="Probability"

#set xtics ('2' 0, '3' 1, '5' 2, '10' 3, '15' 4, '20' 5);\
#set xtics ('2' 0, '3' 1, '5' 2, '8' 3, '9' 4);\

echo "$data" > temp
plot="set terminal eps size 7,5 enhanced color;\
set title '$problem$EFS{/*0.8 $objNome Objectives}';\
set xlabel 'Iteration'; \
set ylabel '$metric' ;\
set output '../../results/prob_img/plot-$problem-$objNome-$metric.eps';\
titles= '$algoritmos aa' ;\
set key below ;\
plot for [idx=1:$colunas] 'temp' index idx-1 title word(titles, idx) with linespoints lw 3"

gnuplot -e "$plot"

rm temp
