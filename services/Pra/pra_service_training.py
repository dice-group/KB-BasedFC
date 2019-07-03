import sys
import os
import pandas as pd
import numpy as np
import ujson as json
import logging as log
import warnings
import argparse
import cPickle as pkl

from nameko.rpc import rpc
from time import time
from pandas import DataFrame
from os.path import expanduser, abspath, exists, join, basename, splitext, dirname, \
	isdir, splitext
from datetime import date
from datastructures.rgraph import Graph, weighted_degree

# OUR METHODS
from algorithms.pra.pra_mining import train_model as pra_train_model

class Predpath(object):

	name = 'pra'

	HOME = abspath(expanduser('./data/'))
	

	if not exists(HOME):
		print 'Data directory not found: %s' % HOME
		print 'Download data per instructions on:'
		print '\thttps://github.com/shiralkarprashant/knowledgestream#data'
		print 'and enter the directory path below.'
		data_dir = raw_input('\nPlease enter data directory path: ')
		if data_dir != '':
			data_dir = abspath(expanduser(data_dir))
		if not os.path.isdir(data_dir):
			raise Exception('Entered path "%s" not a directory.' % data_dir)
		if not exists(data_dir):
			raise Exception('Directory does not exist: %s' % data_dir)
		HOME = data_dir
	# raise Exception('Please set HOME to data directory in algorithms/__main__.py')
	PATH = join(HOME, 'kg/_undir/')
	assert exists(PATH)
	SHAPE = (6060993, 6060993, 663)
	WTFN = 'logdegree'

	# relational similarity using TF-IDF representation and cosine similarity
	RELSIMPATH = join(HOME, 'relsim/coo_mat_sym_2016-10-24_log-tf_tfidf.npy')
	assert exists(RELSIMPATH)

	# Date
	DATE = '{}'.format(date.today())

	# data types for int and float
	_short = np.int16
	_int = np.int32
	_int64 = np.int64
	_float = np.float

	# load knowledge graph
	G = Graph.reconstruct(PATH, SHAPE, sym=True)  # undirected
	assert np.all(G.csr.indices >= 0)

	# relational similarity
	relsim = np.load(RELSIMPATH)

	#__________________train_______________________

	# ensure input file and output directory is valid.     
	outdir = abspath(expanduser('./output'))
	assert exists(outdir)

        #sample data file consisting of records that is used to train the model.
	datafile = abspath(expanduser('./datasets/sample_data_pra.csv'))
	assert exists(datafile)
	log.info('Dataset: {}'.format(basename(datafile)))

	# Date
	DATE = '{}'.format(date.today())

	# read data
	df = pd.read_table(datafile, sep=',', header=0)
	log.info('Read data: {} {}'.format(df.shape, basename(datafile)))
	spo_df = df.dropna(axis=0, subset=['sid', 'pid', 'oid'])
	log.info('Note: Found non-NA records: {}'.format(spo_df.shape))

	# execute
	base = splitext(basename(datafile))[0]
	t1 = time()
	log.info('Computing pra for {} triples..'.format(spo_df.shape[0]))

        #function that trains the model
	features, model = pra_train_model(G, spo_df) # train
	print 'Time taken: {:.2f}s\n'.format(time() - t1)

	# save model
	predictor = { 'dictvectorizer': features, 'model': model }
	try:
		outpkl = join(outdir, 'trained_pra_model.pkl')
		with open(outpkl, 'wb') as g:
			s = pkl.dump(model, g, protocol=pkl.HIGHEST_PROTOCOL)

		outpkl_features = join(outdir, 'pra_features_file.pkl')
		with open(outpkl_features, 'wb') as g:
			s = pkl.dump(features, g, protocol=pkl.HIGHEST_PROTOCOL)
		
	except IOError, e:
		raise e
        
	print("<<<<<<<<<<<<<<<<<<<<<<< The model has been trained and it is saved in the output folder along with the features >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
