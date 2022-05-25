"""
Thanks Josh Hemann for the example
"""
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.patches import Polygon
from matplotlib.ticker import FormatStrFormatter
import sys
import os

if len(sys.argv) == 1:
	print ('usage: plots.py <results>')
	quit()

objNumbers=[2,3,5,8,10,15] #numbers of objectives in the input file
#objNumbers=[0] #numbers of objectives in the input file

#filename='/media/temp/injecting CMAES/results/up_to_20/all-dtlz1-igdp.txt'
filename=sys.argv[1]

with open(filename.split('all-')[0]+"titles.txt") as f: #open the titles file and put in the titles variable
	titles=f.read().split('\n')[0].split(' ')
with open(filename) as f: #open the data file and put in the data variable
	data=f.read()

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
if filename.find('uf') != -1:
	problem="UF"
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
if filename.find('uf1') != -1:
	problem="UF1"
if filename.find('uf2') != -1:
	problem="UF2"
if filename.find('uf3') != -1:
	problem="UF3"
if filename.find('uf4') != -1:
	problem="UF4"
if filename.find('uf5') != -1:
	problem="UF5"
if filename.find('uf6') != -1:
	problem="UF6"
if filename.find('uf7') != -1:
	problem="UF7"
if filename.find('uf8') != -1:
	problem="UF8"
if filename.find('uf9') != -1:
	problem="UF9"
if filename.find('uf10') != -1:
	problem="UF10"


mat=[]
mats=[]
numLines=0
for l in data.split('\n'): #lines
	if len(l) > 0: #if the line is not empty
		line=[]
		for c in l.split(' '): #collumns
			if len(c) > 0: #if the collumn is not empty, i.e. if is not the last
				line.append(float(c))
		mat.append(line)
		#numLines+=1 #line counter
	else: #if the line is empty, it is a separation, so draw the plot
		if len(mat) > 0:
			mats.append(mat)
			mat=[]


for m in range(0, len(mats)):
	mat=mats[m]
	mt=[0]*(len(mat[0]))
	#invert the lines and collumns of the matrix
	for i in range(0, len(mat[0])): #for each algorithm ()
		mt[i]=[0]*len(mat)
		for j in range(0, len(mat)): #for each objective number
			mt[i][j]=mat[j][i] #invert the mat matrix to plot 

	
	fig, ax1 = plt.subplots()
	fig.subplots_adjust(left=0.14, bottom=0.2) #make room for the left axis title


	majorFormatter = FormatStrFormatter('%.1e')
	plt.gca().yaxis.set_major_formatter(majorFormatter)
	
	#bp = plt.boxplot(mt,0,'rs',0)
	bp = plt.boxplot(mt,0)

	plt.title(problem+'\n'+str(objNumbers[m])+' obj.') #set the title
	plt.setp(bp['boxes'], color='black') #color of boxplot
	plt.setp(bp['whiskers'], color='black') #color or lines (first and last quartile)
	plt.setp(bp['fliers'], color='red', marker='+') # outlier markers

	# Add a horizontal grid to the plot, but make it very light in color so we can use it for reading data values but not be distracting
	ax1.yaxis.grid(True, linestyle='-', which='major', color='lightgrey', alpha=0.5)

	# Hide these grid behind plot objects
	ax1.set_axisbelow(True)
	##ax1.set_ylabel('PBIL variants', fontsize=30, rotation=-90) #y label
	##ax1.set_xlabel('normalized HV', fontsize=30) #x label
	#ax1.set_xlabel('metric') #x label
	ax1.set_ylabel('$'+metric+'$') #y label


	# Now fill the boxes with desired colors
	boxColors = ['darkkhaki','royalblue', 'm', 'green']
	numBoxes = len(mats[0][0])
	medians = list(range(numBoxes))
	for i in range(numBoxes):
		box = bp['boxes'][i]
		boxX = []
		boxY = []
		for j in range(5):
			boxX.append(box.get_xdata()[j])
			boxY.append(box.get_ydata()[j])
		boxCoords = list(zip(boxX,boxY))
		# Alternate between Dark Khaki and Royal Blue
		k = i % 4
		
		print(boxX)
		print(boxY)
		
		boxPolygon = Polygon(boxCoords, facecolor=boxColors[k])
		ax1.add_patch(boxPolygon)
		# Now draw the median lines back over what we just filled in
		med = bp['medians'][i]
		medianX = []
		medianY = []
		for j in range(2):
			medianX.append(med.get_xdata()[j])
			medianY.append(med.get_ydata()[j])
			plt.plot(medianX, medianY, 'k')
			medians[i] = medianY[0]
	# Finally, overplot the sample averages, with horizontal alignment in the center of each box

	# Set the axes ranges and axes labels

	#top = len(mats[0][0])
	#bottom = 0
	#ax1.set_ylim(bottom, top+10)
	#ytickNames = plt.setp(ax1, yticklabels=np.repeat(titles, 1)) #names for the y axis
	xtickNames = plt.setp(ax1, xticklabels=np.repeat(titles, 1)) #names for the x axis
	##plt.setp(ytickNames, fontsize=32)
	##plt.xticks(fontsize=42, rotation=-90)
	plt.xticks(rotation=-45)
	
	directory='../../../results/img'

	if not os.path.exists(directory):
		os.makedirs(directory)

	#plt.show() #show the plot
	plt.savefig(directory+'/boxplot-'+problem+'-'+str(objNumbers[m])+'-'+metric+'.eps') #save the plot as file
