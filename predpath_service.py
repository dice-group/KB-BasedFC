import sys
import os
import pandas as pd
import numpy as np
import ujson as json
import logging as log
import warnings

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
	def stream(self, sid, pid, oid):
		#int_sid = int(sid)
		#int_pid = int(pid)
		#int_oid = int(oid)

		df = [sid, pid, oid] # required for passing it to predpath_train_model

		print("Triples are %s" % (df))
		t1 = time()
		vec, model = predpath_train_model(self.G, sid, pid, oid) # train
		print 'Time taken: {:.2f}s\n'.format(time() - t1)
		return json.dumps({'FC value': vec})
