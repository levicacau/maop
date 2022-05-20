EFS="
"
mkdir -p ../../results/img

#algoritmos="H-MOPSO SMPSO CD-Ideal CD-MGA NWSum-CD NWSum-Ideal NWSum-MGA Sigma-CD Sigma-Ideal Sigma-MGA"
algoritmos=$(cat ../../results/titles.txt | head -1)

xtic=""
colunas=0
for c in $algoritmos; do #contando as colunas do arquivo a partir da entrada e monta o xtic
	let colunas=colunas+1
	xtic=$xtic"'$c' $colunas,"
done
xtic=$(echo -n "$xtic" | head -c -1) #remove a ultima virgula

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

#	set title '$problem$EFS{/*0.8 $objNome Objectives}';\


for obj in 0 1 2 3 4 5; do
#for obj in 0 1 2 3 4; do
	case "$obj" in #numero de objetivos
	    *0* ) objNome="3";;
	    *1* ) objNome="5";;
	    *2* ) objNome="8";;
 	    *3* ) objNome="10";;
 	    *4* ) objNome="15";;
 	    *5* ) objNome="20";;
# 	    *3* ) objNome="8";;
# 	    *4* ) objNome="9";;
	    * ) echo "Error, undetermined objective number!"; exit ;;
	esac
	plot="set terminal eps size 4,2.5 enhanced color;\
	set xtics ($xtic);\
	set style data boxplot;\
	set ylabel '$metric' ;\
	set output '../../results/img/boxplot-$problem-$objNome-$metric.eps';\
	styles= '1 2 3 4 7 8 9 10 11 12 13 16 17 18 19 20 21 22 25 26 27 28 29 30 31 34 35 36 37 38 39 40';\
	titles= '$algoritmos' ;\
	set key below ;\
	set format y '%.2E' ;\
	plot for [i=1:$colunas] '$1' index $obj using (i):i notitle lt word(styles, i)" #lt 7 black

	gnuplot -e "$plot"
done

