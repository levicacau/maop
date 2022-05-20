#sudo apt-get install python-matplotlib
import sys

if len(sys.argv) == 1:
	print 'use: matplot.py <file>'
	quit()
else:
	print len(sys.argv)
	print sys.argv[1]

import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt

#with open("/media/temp/injecting CMAES/assessment/metrics/pareto/DTLZ2_3") as f:
with open(sys.argv[1]) as f:
    data = f.read()

data = data.split('\n')
x=[]
y=[]
z=[]
for row in data: 
	if len(row.split('\t')) > 2: 
		x.append(float(row.split('\t')[0]))
		y.append(float(row.split('\t')[1]))
		z.append(float(row.split('\t')[2]))


fig = plt.figure()
ax = Axes3D(fig)
ax.scatter(x,y,z,c='black',marker='^')
fig.show()
raw_input("Press Enter to continue...")
#fig.savefig('scatter3d.eps')