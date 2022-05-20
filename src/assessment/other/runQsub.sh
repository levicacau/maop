#/bin/bash
#########################################
## Parametros que le pasamos al script ##
#########################################
#$ -S /bin/bash
#######################################
# Usar el directorio de trabajo actual
#######################################
#$ -cwd
# Tiempo de trabajo
#$ -l h_rt=2400:00:00
# juntar la salida estandar y de error en un solo fichero
#$ -j y
###########################
# usar colas indicadas
###########################
#$ -q libra.q,loki.q,gemini.q,pegasus.q
##$ -t 1-30:1
##$ -o /dev/null

echo ""
echo "Host --> $HOSTNAME"

echo Init Time: `date`
init=`date +'%s'`

echo $1
eval "$1"

final=`date +'%s'`
echo End Time: `date` --- run in $(( (($final-$init)/3600) )):$(( (($final-$init)/60)%60 )):$(( (($final-$init))%60 )) ---

echo "Pronto!"

