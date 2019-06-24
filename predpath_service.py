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
from os.path import expanduser, abspath, exists, join, basename, splitext, dirname, \
	isdir, splitext
from datetime import date
from datastructures.rgraph import Graph, weighted_degree

# OUR METHODS
from algorithms.predpath.predpath_mining import train_model as predpath_train_model

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
	def stream(self, sid, pid, oid, args=None):
		int_sid = int(sid)
		int_pid = int(pid)
		int_oid = int(oid)

		print(int_sid)
		print(int_pid)
		print(int_oid)

		data = {'sid': [int_sid], 
			'pid': [int_pid], 
			'oid': [int_oid],
			'class': [1]}

		# ensure input file and output directory is valid.
		outdir = abspath(expanduser('./output'))
		assert exists(outdir)
		datafile = abspath(expanduser('./datasets/Player_vs_Team_NBA.csv'))
		assert exists(datafile)
		#args.dataset = datafile
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

		#data = {'triple': [[int_sid,int_pid,int_oid], 
					#[int_sid,int_pid,int_oid], 
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid],
					#[int_sid,int_pid,int_oid]],
					#'class': [1,1,1,1,1,0,0,0,0,0]}
		#dfObj = pd.DataFrame(data)
		#lst = [[int_sid,1], [int_pid,1], [int_oid,1]] # required for passing it to predpath_train_model
		#dfObj = pd.DataFrame(lst, columns = ['ids','class']) 
		#print("<<<<<<<<< dfObj is %s" % (dfObj))

		#df = pd.read_table(dfObj, sep=',', header=0)
		#log.info('Read data: {} {}'.format(df.shape, basename(dfObj)))
		#spo_df = dfObj.dropna(axis=0, subset=['sid','pid','oid','class'])
		print("<<<<<<<<<dropna is %s" % (spo_df))
		t1 = time()
		log.info('Computing predpath for {} triples..'.format(spo_df.shape[0]))
		vec, model = predpath_train_model(self.G, spo_df) # train
		print 'Time taken: {:.2f}s\n'.format(time() - t1)
		# save model
		predictor = { 'dictvectorizer': vec, 'model': model }
		print("<<<<<<<<<got the results")
		try:
			outpkl = join(outdir, 'out_pra_{}_{}.pkl'.format(base, DATE))
			with open(outpkl, 'wb') as g:
				s = pkl.dump(predictor, g, protocol=pkl.HIGHEST_PROTOCOL)
			print 'Saved: {}'.format(outpkl)
			print("<<<<<<<<<file has been saved>>>>>>>>>")
			print("<<<<<<<<<predicting the test case>>>>>>>>>")
			X = vec.transform([["392035", "599", "2115741"]])
			pred = model['clf'].predict(X) # array
			print("The result is %s" % (pred))
			#pickle_in = open("./output/out_pra_Player_vs_Team_NBA_2019-06-20.pkl","rb")
			#example = pkl.load(pickle_in)
			#result = example.predict(["392035", "599", "2115741"])
			#print("The result is %s" % (results))
		except IOError, e:
			raise e
		result = '<http://swc2017.aksw.org/task2/dataset/s'
		return result

