import sys
import os
import pandas as pd
import numpy as np
import ujson as json
import logging as log
import warnings
import argparse
import cPickle as pkl
import mapping
import extract

from nameko.rpc import rpc
from time import time
from pandas import DataFrame
from os.path import expanduser, abspath, exists, join, basename, splitext, dirname, \
	isdir, splitext
from datetime import date
from datastructures.rgraph import Graph, weighted_degree

# OUR METHODS
from algorithms.predpath.predpath_mining import train_model as predpath_train_model
from algorithms.predpath.predpath_mining import predict as predpath_predict

class Predpath(object):

	name = 'predpath'

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

	# ================= PREDPATH ALGORITHM IMPLEMENTATION ============

	
	@rpc	# Methods are exposed to the outside world with entrypoint decorators (RPC in our case)
	def stream(self, data, args=None):

		print('\nThe following request in RDF format was passed:')
		print(data)

		identification, theDate, suri, puri, ouri = extract.getValues(data)

		print('\nSURI, PURI and OURI are:')
		print(suri)
		print(puri)
		print(ouri)
		print('\n')

		# sid, pid, oid = self.uriToId(suri, puri, ouri)
		sid, pid, oid = mapping.convert(suri, puri, ouri)

		# required for passing it to compute_mincostflow
		sid, pid, oid = np.array([sid]), np.array([pid]), np.array([oid])

		t1 = time()

		print('\nTheir corresponding IDs are:')
		print(sid)
		print(pid)
		print(oid)
		print('\n')

		log.info('Computing Predpath for triple')

		int_sid = int(sid)
		int_pid = int(pid)
		int_oid = int(oid)

		print("The subject id is: %s " % int_sid)
		print("The predicate id is: %s" % int_pid)
		print("The object id is: %s" % int_oid)

		# Creating a dataframe 
		data = {'sid': [int_sid], 
			'pid': [int_pid], 
			'oid': [int_oid],
			'class': [0]}

		#__________________test________________________
		dfObj = pd.DataFrame(data)
		test_spo_df = dfObj.dropna(axis=0, subset=['sid','pid','oid','class'])

		test_model_pkl = open("./output/trained_model.pkl","rb")
		test_model = pkl.load(test_model_pkl)

		test_vec_pkl = open("./output/vector_file.pkl","rb")
		test_vec = pkl.load(test_vec_pkl)
                with warnings.catch_warnings():
			try:
				warnings.simplefilter("ignore")
				# predicate_predpath() function is used to predict the triple's veracity
				array_value = predpath_predict(self.G, test_spo_df,test_vec, test_model) # test
				val = str(array_value)[1:-1]
				log.info('Predpath computation complete. Time taken: {:.2f} secs.\n'.format(time() - t1))
				result = '<http://swc2017.aksw.org/task2/dataset/s-' + str(
					identification) + '> <http://swc2017.aksw.org/hasTruthValue>\"' + str(
					val) + '\"<http://www.w3.org/2001/XMLSchema#double> .'
				print('The result in RDF format is:')
				print(result)

			except MemoryError:
				print('\nA MemoryError is successfully caught.')
				result = 'MemoryError'
		
		return result
