#sudo apt-get install python-matplotlib
import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter
import numpy as np
import sys


if len(sys.argv) == 1:
	print ('usage: plots.py <results>')
	quit()

objNumbers=[2,3,5,8,10,15] #numbers of objectives in the input file
#filename='/media/temp/injecting CMAES/results/up_to_20/all-dtlz1-igdp.txt'
filename=sys.argv[1]

markers=[".","v","^","<",">","s","p","*","h","x","D","d","+","|","_",",","o","1","2","3","4","H"] #enumerate the markers, so we can have one per line
with open(filename.split('all-')[0]+"titles.txt") as f: #open the titles file and put in the titles variable
	titles=f.read().split('\n')[0].split(' ')
	
with open(filename) as f: #open the data file and put in the data variable
	data=f.read()

sz=len(data.split('\n')[0].split(' '))-1 #number of collumns
avgs=[0]*sz #store the average of the executions
mat=[] #store the average of the executions for all the objective numbers
objNumber=[] #used for make the labels of the xticks
numbers=[] #used to make the xticks equally spaced
numLines=0 #counter of line numbers per objective number (independent runs)


for l in data.split('\n'): #lines
	if len(l) > 0: #if the line is not empty
		line=[]
		for c in l.split(' '): #collumns
			if len(c) > 0: #if the collumn is not empty, i.e. if is not the last
				line.append(float(c))
		for i in range(0, sz):#sum of the values of the lines to make the average
			avgs[i]+=line[i]
		numLines+=1 #line counter
	else: #if the line is empty, it is a separation, so draw the plot
		if numLines > 0:
			for i in range(0, sz):#compute the average of the executions
				avgs[i]/=numLines
			objNumber.append(objNumbers[len(mat)])# append for the xticks
			numbers.append(len(mat))#used to make the xticks equally spaced
			mat.append(avgs) #put the averages in the mat
			numLines=0 #reset the line counter
			avgs=[0]*sz #reset the averages

plt.figure().subplots_adjust(bottom=0.2, left=0.14) #make room for the bottom legend

for i in range(0, sz): #for each algorithm ()
	tmp=[0]*len(mat)
	for j in range(0, len(mat)): #for each objective number
		tmp[j]=mat[j][i] #invert the mat matrix to plot
	plt.plot(numbers, tmp, label=titles[i], marker=markers[i])

#define the metric and problem
if filename.find('gd') != -1:
	metric="GD"
if filename.find('igd') != -1:
	metric="IGD_p"
if filename.find('hv') != -1:
	metric="Hypervolume"
if filename.find('r2') != -1:
	metric="R2"
	
if filename.find('dtlz1') != -1:
	problem="DTLZ1"
if filename.find('dtlz2') != -1:
	problem="DTLZ2"
if filename.find('dtlz3') != -1:
	problem="DTLZ3"
if filename.find('dtlz4') != -1:
	problem="DTLZ4"
if filename.find('dtlz5') != -1:
	problem="DTLZ5"
if filename.find('dtlz6') != -1:
	problem="DTLZ6"
if filename.find('dtlz7') != -1:
	problem="DTLZ7"
if filename.find('wfg1') != -1:
	problem="WFG1"
if filename.find('wfg2') != -1:
	problem="WFG2"
if filename.find('wfg3') != -1:
	problem="WFG3"
if filename.find('wfg4') != -1:
	problem="WFG4"
if filename.find('wfg5') != -1:
	problem="WFG5"
if filename.find('wfg6') != -1:
	problem="WFG6"
if filename.find('wfg7') != -1:
	problem="WFG7"
if filename.find('wfg8') != -1:
	problem="WFG8"
if filename.find('wfg9') != -1:
	problem="WFG9"

majorFormatter = FormatStrFormatter('%.1e')
plt.gca().yaxis.set_major_formatter(majorFormatter)

plt.xticks(numbers, objNumber) #set the xticks
plt.legend(loc='upper center', bbox_to_anchor=(0.5, -0.13), fancybox=True, shadow=True, ncol=5, fontsize=10) #set the legend
plt.title(problem)
plt.xlabel('Objectives')
plt.ylabel('$'+metric+'$')
#plt.ticklabel_format(style='plain', axis='y', scilimits=(0,0))



#plt.show() #show the plot
plt.savefig('../../../results/img/plot-'+problem+'-'+metric+'.eps') #save the plot as file
