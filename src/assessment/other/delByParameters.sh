EFS="
"

if [ "$1" == "" ]; then
	echo "usage: delByParameters.sh <string to filter>"
	exit
fi

allPids=`qstat | grep olacir | cut -d ' ' -f 2`

count=0
for pid in $allPids; do
	command=`qstat -j $pid | grep "job_args:" | tr -s ' ' | cut -d ' ' -f 2- | grep -i $1`
	if [ "$command" != "" ]; then
		echo -e "$command\tID: $pid"
		pids+="$pid "
		count=`echo $count+1 | bc`
	fi
done

echo ""
echo "Would you like to delete these $count jobs?"
read answer

if [ "$answer" == "y" ] || [ "$answer" == "yes" ]; then
	
	for pid in $pids; do
		qdel $pid
# 		qalter $pid -q loki.q
	done
else
	echo "Nothing done"
fi