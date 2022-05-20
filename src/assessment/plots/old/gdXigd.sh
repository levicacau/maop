gd=$(java medias_gdXigd $1)
igd=$(java medias_gdXigd $2)
EFS="
"
linha=1
mkdir ../../results/img
algoritmos=$(cat ../../results/titles.txt | head -1)
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
    * ) echo "Error, undetermined problem!"; exit ;;
esac
if [ $(echo "$gd" | wc -l) = $(echo "$igd" | wc -l) ]; then #verificando o tamanho dos arquivos pra garantir que e igual
	numLinhas=$(echo "$gd" | wc -l)
else
	echo "erro! gd e igd de tamanhos diferentes"
	exit
fi

entrada=""
while [  $linha -le $numLinhas ]; do #juntando o gd e igd
	entrada=$entrada" "$(echo "$gd" | head -$linha | tail -1)
	entrada=$entrada" "$(echo "$igd" | head -$linha | tail -1)
	entrada=$entrada" "$EFS
let linha=linha+1
done
echo "$entrada" > temp

#plot "temp" index 0 every ::0::0 title 'data', '' index 0 every ::1::1 title 'data2'

for obj in 0 1 2 3 4 5; do
#for obj in 0 1 2 3 4; do
	case "$obj" in #numero de objetivos
	    *0* ) objNome="2";;
	    *1* ) objNome="3";;
	    *2* ) objNome="5";;
 	    *3* ) objNome="10";;
 	    *4* ) objNome="15";;
 	    *5* ) objNome="20";;
# 	    *3* ) objNome="8";;
# 	    *4* ) objNome="9";;
	    * ) echo "Error, undetermined objective number!"; exit ;;
	esac
plot="set terminal eps size 7,5 enhanced color; \
titles= '$algoritmos aa' ; \
set xlabel 'GD_p'; \
set ylabel 'IGD_p'; \
set title '$problem$EFS{/*0.8 $objNome Objectives}';\
styles= '1 2 3 4 7 8 9 10 11 12 13 16 17 18 19 20 21 22 25 26 27 28 29 30 31 34 35 36 37 38 39 40';\
set key below; \
set output '../../results/img/gdXigd-$problem-$objNome.eps'; \
set format y '%.2E' ;\
plot 'temp' "
	alg=1
	while [  $alg -le $colunas ]; do
		
		if [ $alg -ne '1' ]; then
			plot=$plot"''"
		fi
		
		plot=$plot" index $obj every ::$alg-1::$alg-1 title word(titles, $alg) lt word(styles, $alg),"
		let alg=alg+1
	done

	plot=$(echo -n "$plot" | head -c -1) #remove a ultima virgula

	gnuplot -e "$plot"
		
	plot=""
done

rm temp


