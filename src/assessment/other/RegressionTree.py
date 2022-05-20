#!/usr/bin/env python3

import argparse
import numpy as np
from sklearn.externals.six import StringIO  
import pydotplus
from sklearn import tree


if __name__ == '__main__':
	parser = argparse.ArgumentParser()
parser.add_argument(
	'integers', metavar='int', type=int, choices=range(1000),
	nargs='+', help='an integer in the range 0..25')
parser.add_argument(
	'--sum', dest='accumulate', action='store_const', const=sum,
	default=max, help='sum the integers (default: find the max)')

args = parser.parse_args()
metric=args.integers[0] # Type of features

if metric==1:
	data = np.loadtxt('Silhouette_data.txt',delimiter=',',unpack=True).transpose() 
elif  metric==2:
	data = np.loadtxt('DunnIndex_data.txt',delimiter=',',unpack=True).transpose() 
elif  metric==3:
	data = np.loadtxt('DaviesBouldingIndex_data_precision.txt',delimiter=',',unpack=True).transpose()
tree_file = 'Tree_%i.pdf' %(metric)
# We print the size of the data
print(data.shape)

# The regressor is defined
#regressor = tree.DecisionTreeRegressor(max_depth=4)
regressor = tree.DecisionTreeRegressor(min_samples_split=10)


# The regressor is learned 
regressor.fit(data[:,:3], data[:,3])

# We create a pdf file with the tree
dot_data = StringIO() 
featureNames=["Clustering space", "Obj. Number","Problem"]

#sklearn > 0.16
tree.export_graphviz(regressor, out_file=dot_data, feature_names=featureNames, special_characters=True, rounded=True, filled=True, impurity=False, label="root") 
graph = pydotplus.graph_from_dot_data(dot_data.getvalue()) 
graph.write_pdf(tree_file) 

# We print the prediction
prediction = regressor.predict(data[:,:3])
print(prediction)

#   ./RegressionTree.py 3  # Example of calling the program
