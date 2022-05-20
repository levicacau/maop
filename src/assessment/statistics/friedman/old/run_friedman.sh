baseFolder="/home/olacir/Dropbox/UFPR/Doutorado/cuda-pso/actual/results"
metrica="gd"

#for swarms in NewIMulti5 NewIMulti10 NewIMulti15 NewIMulti20 NewIMulti25 NewIMulti IMulti;  do
for problem in 1 2 3 4 5 6 7; do
    for objectives in 2 3 5 10 15 20; do     
	echo "**************DTLZ$problem $objectives  ***********"
	java friedman "$baseFolder/smpso/smpso-2,2-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-2,3-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-2,4-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-3,2-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-3,3-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-3,4-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-4,2-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-4,3-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/smpso/smpso-4,4-$objectives-DTLZ$problem"_$metrica.txt"" "$baseFolder/novo/novo-0,0-$objectives-DTLZ$problem"_$metrica.txt"" 
	echo "***********************************************"
    done
done
