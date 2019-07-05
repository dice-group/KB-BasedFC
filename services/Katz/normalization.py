import pandas as pd
import numpy as np

def score(value):
	df = pd.read_csv('katz.csv', sep=',')

	ones = df[df['class'] == 1]['score']
	zeros = df[df['class'] == 0]['score']

	dataFrameOnes = pd.DataFrame(data=ones, dtype=np.float64)
	dataFrameZeros = pd.DataFrame(data=zeros, dtype=np.float64)

	meanOnes = dataFrameOnes['score'].mean()
	meanZeros = dataFrameZeros['score'].mean()

	a = 0
	b = 1

	normValue = (b - a) * (value - meanZeros) / (meanOnes - meanZeros) + a

	if normValue >= 1.0:
		finalValue = 1.0
	elif normValue <= 0.0:
		finalValue = 0.0
	else:
		finalValue = normValue

	return finalValue