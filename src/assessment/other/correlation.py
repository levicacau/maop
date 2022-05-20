"""
Calculate the average correlation between the data on the first collumn with the other collumns
"""
from scipy.stats.stats import pearsonr   
import sys

if len(sys.argv) == 1:
	print 'usage: plots.py <results>'
	quit()
	
filename=sys.argv[1]

with open(filename) as f: #open the data file and put in the data variable
	data=f.read()

inData=[]
for l in data.split('\n'): #lines
	if len(l) > 0: #if the line is not empty
		line=[]
		for c in l.split(' '): #collumns
			if len(c) > 0: #if the collumn is not empty, i.e. if is not the last
				line.append(float(c))
		inData.append(line)

collumns=[0]*(len(inData[0]))
firstLinesToIgnore=100 #ignore first lines because there was no clustering on them

for i in range(0, len(inData[0])): # invert the lines and collumns of the matrix (transpose)
	collumns[i]=[0]*(len(inData)-firstLinesToIgnore)
	for j in range (firstLinesToIgnore, len(inData)):
		collumns[i][j-firstLinesToIgnore]=inData[j][i]
		
		
for i in range(1, len(collumns)):
	print pearsonr(collumns[0],collumns[i])[0], 	#pearsonr(x, y) Calculates a Pearson correlation coefficient and the p-value for testing non-correlation.
										#Parameters: x : 1D array ; y : 1D array the same length as x
										#Returns: (Pearson's correlation coefficient,  2-tailed p-value)

#print collumns[1]