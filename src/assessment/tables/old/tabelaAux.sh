echo $1 | sed -e "s/,/ /g" | sed -e "s/|/\n/g" > temp

cd ../statistics/friedman
java fdr ../../tables/temp

#cd ../statistics/wilcoxon
#java wcx ../../tables/temp

rm ../../tables/temp
