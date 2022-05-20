"""
Calculate the average correlation between the data on the first collumn with the other collumns
"""
from scipy.stats.stats import pearsonr   
import numpy as np
import sys

inData=[]
correlations=[]
statistics=[]
for i in range(0, 24):
	corr=[]
	stat=[]
	with open("tandem"+str(i+1)+"_all") as fi: #open the data file and put in the data variable
		datai=fi.read()
	for j in range(i+1, 24):
		with open("tandem"+str(j+1)+"_all") as fj: #open the data file and put in the data variable
			dataj=fj.read()
		
		for l in datai.split('\n'): #lines
			if len(l) > 0: #if the line is not empty
				line=[]
				for c in l.split(' '): #collumns
					if len(c) > 0: #if the collumn is not empty, i.e. if is not the last
						line.append(float(c))
				inData.append(line)
		
		for l in dataj.split('\n'): #lines
			if len(l) > 0: #if the line is not empty
				line=[]
				for c in l.split(' '): #collumns
					if len(c) > 0: #if the collumn is not empty, i.e. if is not the last
						line.append(float(c))
				inData.append(line)

		a = np.array(inData)
		b = a.transpose()
		
		c=pearsonr(b[i],b[j])#pearsonr(x, y) Calculates a Pearson correlation coefficient and the p-value for testing non-correlation.
							#Parameters: x : 1D array ; y : 1D array the same length as x
							#Returns: (Pearson's correlation coefficient,  2-tailed p-value)
		corr.append(c[0])
		stat.append(c[1])
		
		#print str(i)+" "+str(j)+" "+str(c)
		sys.stderr.write(str(i)+" "+str(j)+" "+str(c)+"\n")
		
		#print len(corr)
		inData=[]
	correlations.append(corr)
	statistics.append(stat)
	corr=[]
	stat=[]

print "Correlations:"
for i in range(0, len(correlations)):
	for j in range(0, len(correlations[i])):
		print str(correlations[i][j])+" ",
	print ""
	
	
print "p-values:"
for i in range(0, len(statistics)):
	for j in range(0, len(statistics[i])):
		print str(statistics[i][j])+" ",
	print ""

	
quit()

inData=[]
for l in data.split('\n'): #lines
	if len(l) > 0: #if the line is not empty
		line=[]
		for c in l.split(' '): #collumns
			if len(c) > 0: #if the collumn is not empty, i.e. if is not the last
				line.append(float(c))
		inData.append(line)

collumns=[0]*(len(inData[0]))
firstLinesToIgnore=0 #ignore first lines because there was no clustering on them

for i in range(0, len(inData[0])): # invert the lines and collumns of the matrix (transpose)
	collumns[i]=[0]*(len(inData)-firstLinesToIgnore)
	for j in range (firstLinesToIgnore, len(inData)):
		collumns[i][j-firstLinesToIgnore]=inData[j][i]
		
		
for i in range(1, len(collumns)):
	print pearsonr(collumns[0],collumns[i])[0], 	#pearsonr(x, y) Calculates a Pearson correlation coefficient and the p-value for testing non-correlation.
										#Parameters: x : 1D array ; y : 1D array the same length as x
										#Returns: (Pearson's correlation coefficient,  2-tailed p-value)

#print collumns[1]