EFS="
"

for filename in $@; do
	qdel $(echo $filename | cut -d "." -f 3 | sed -e 's/o/''/g')
done