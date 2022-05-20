echo "------------------------melhor: $3------------------------"
#echo $1 | R --no-save | tail -$(echo "$2+1" | bc) | head -$(echo "$2" | bc) | grep -E "$3-|\-$3" | grep FALSE

#echo $1 | R --no-save | tail -$2 | grep -E "$3-|\-$3" | grep FALSE #teste mostrando qual NAO tem diferenca em relacao ao melhor
#echo $1 | R --no-save | tail -$2 | grep -E "1-|\-1 " | grep TRUE #teste mostrando qual TEM diferenca em relacao ao primeiro

echo $1 | R --no-save

echo "----------------------------------------------------------"
