import sys
import os
import pandas as pd
import numpy as np
import ujson as json
import logging as log
import warnings

from nameko.rpc import rpc
from time import time
from os.path import expanduser, abspath, exists, join, basename, splitext
from datetime import date
from datastructures.rgraph import Graph, weighted_degree

class KnowledgeLinker(object):

	name = 'klinker'

	HOME = abspath(expanduser('~/git/FC/KB-BasedFC/data/'))

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



    # ================= KNOWLEDGE LINKER ALGORITHM ============

	def compute_klinker(G, subs, preds, objs):
		"""
		Parameters:
		-----------
		G: rgraph
			See `datastructures`.
		subs, preds, objs: sequence
			Sequences representing the subject, predicate and object of
			input triples.

		Returns:
		--------
		scores, paths, rpaths, times: sequence
			One sequence each for the proximity scores, shortest path in terms of
			nodes, shortest path in terms of relation sequence, and times taken.
		"""
		# set weights
		indegsim = weighted_degree(G.indeg_vec, weight=WTFN).reshape((1, G.N))
		indegsim = indegsim.ravel()
		targets = G.csr.indices % G.N
		specificity_wt = indegsim[targets] # specificity
		G.csr.data = specificity_wt.copy()

		# back up
		data = G.csr.data.copy()
		indices = G.csr.indices.copy()
		indptr = G.csr.indptr.copy()

		# compute closure
		scores, paths, rpaths, times = [], [], [], []
		print '{}. Working on {}..'.format(idx+1, (s, p, o)),
		ts = time()
		rp = closure(G, s, p, o, kind='metric', linkpred=True)
		tend = time()
		print 'time: {:.2f}s'.format(tend - ts)
		times.append(tend - ts)
		scores.append(rp.score)
		paths.append(rp.path)
		rpaths.append(rp.relational_path)

		# reset graph
		G.csr.data = data.copy()
		G.csr.indices = indices.copy()
		G.csr.indptr = indptr.copy()
		sys.stdout.flush()
		log.info('')
		return scores, paths, rpaths, times

	def normalize(df):
		softmax = lambda x: np.exp(x) / float(np.exp(x).sum())
		df['softmaxscore'] = df[['sid','score']].groupby(by=['sid'], as_index=False).transform(softmax)
		return df



    @rpc	# Methods are exposed to the outside world with entrypoint decorators (RPC in our case)
	def stream(self, sid, pid, oid):

		sid, pid, oid = np.array([sid]), np.array([pid]), np.array([oid])	# required for passing it to compute_klinker

		t1 = time()

		log.info('Computing KL for triple')
		with warnings.catch_warnings():
			warnings.simplefilter("ignore")
			# TODO: compute klinker
			scores, paths, rpaths, times = self.compute_klinker(self.G, sid, pid, oid)

			log.info('KLinker computation complete. Time taken: {:.2f} secs.\n'.format(time() - t1))
		return json.dumps({'FC value': scores})
