entrada=$1

algorithms="cmaes-mopso(999-0) cmaes-mopso(999-1) cmaes-mopso(999-2) cmaes-mopso(999-3) cmaes-mopso(999-4) cmaes-mopso(999-5) cmaes-mopso(999-6) cmaes-mopso(999-7)"

for algorithm in $algorithms; do
	rm -rf $algorithm
	mkdir -p $algorithm
	for ((problem=1;problem<56;problem++)); do
		echo "$algorithm - COCO$problem"
	
		fileNames="$entrada/$algorithm"coco"$problem-2.0-"coco"$problem"
		
		infoFile=$(ls $fileNames | grep .info)
		
# 		echo info: $infoFile
		cat $fileNames/$infoFile >> $algorithm/$infoFile
		echo "" >> $algorithm/$infoFile

		for fileName in $(ls $fileNames | grep -v .info); do
# 			echo "cp -R $fileNames/$fileName $algorithm"
			cp -R $fileNames/$fileName $algorithm

		done
	done
	for fileName in $(ls $algorithm | grep .info); do
		cat $algorithm/$fileName | head -2 > tmp.txt
		cat $algorithm/$fileName | grep "function" >> tmp.txt
		mv tmp.txt $algorithm/$fileName
	done
done