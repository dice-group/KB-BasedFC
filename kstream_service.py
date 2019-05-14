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

# OUR METHODS
from algorithms.mincostflow.ssp import succ_shortest_path

class KnowledgeStream(object):

	name = 'kstream'

	HOME = abspath(expanduser('~/Documents/project/KB-BasedFC/data/'))

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

	# ================= KNOWLEDGE STREAM ALGORITHM ============

	def compute_mincostflow(self, G, relsim, sid, pid, oid):
		"""
        Parameters:
        -----------
        G: rgraph
            See `datastructures`.
        relsim: ndarray
            A square matrix containing relational similarity scores.
        subs, preds, objs: sequence
            Sequences representing the subject, predicate and object of
            input triples.
        flowfile: str
            Absolute path of the file where flow will be stored as JSON,
            one line per triple.

        Returns:
        --------
        mincostflows: sequence
            A sequence containing total flow for each triple.
        times: sequence
            Times taken to compute stream of each triple.
        """
		# take graph backup
		G_bak = {
			'data': G.csr.data.copy(),
			'indices': G.csr.indices.copy(),
			'indptr': G.csr.indptr.copy()
		}
		cost_vec_bak = np.log(G.indeg_vec).copy()

		# some set up
		G.sources = np.repeat(np.arange(G.N), np.diff(G.csr.indptr))
		G.targets = G.csr.indices % G.N
		cost_vec = cost_vec_bak.copy()
		indegsim = weighted_degree(G.indeg_vec, weight=self.WTFN)
		specificity_wt = indegsim[G.targets]  # specificity
		relations = (G.csr.indices - G.targets) / G.N

		s, p, o = [int(x) for x in (sid, pid, oid)]
		ts = time()
		print '{}. Working on {} .. '.format(1, (s, p, o)),
		sys.stdout.flush()

		# set weights
		relsimvec = np.array(relsim[p, :])  # specific to predicate p
		relsim_wt = relsimvec[relations]
		G.csr.data = np.multiply(relsim_wt, specificity_wt)

		# compute
		mcflow = succ_shortest_path(
			G, cost_vec, s, p, o, return_flow=False, npaths=5
		)
		mincostflow = mcflow.flow
		tend = time()
		times = tend - ts
		print 'mincostflow: {:.5f}, #paths: {}, time: {:.2f}s.'.format(
			mcflow.flow, len(mcflow.stream['paths']), tend - ts
		)

		# reset state of the graph
		np.copyto(G.csr.data, G_bak['data'])
		np.copyto(G.csr.indices, G_bak['indices'])
		np.copyto(G.csr.indptr, G_bak['indptr'])
		np.copyto(cost_vec, cost_vec_bak)
		return mincostflow, times

	@rpc	# Methods are exposed to the outside world with entrypoint decorators (RPC in our case)
	def stream(self, sid, pid, oid):

		sid, pid, oid = np.array([sid]), np.array([pid]), np.array([oid])	# required for passing it to compute_mincostflow

		t1 = time()

		log.info('Computing KS for triple')
		with warnings.catch_warnings():
			warnings.simplefilter("ignore")
			# compute min. cost flow
			mincostflows, times = self.compute_mincostflow(self.G, self.relsim, sid, pid, oid)
			# spo_df = self.normalize(spo_df)

			log.info('Mincostflow computation complete. Time taken: {:.2f} secs.\n'.format(time() - t1))
		return json.dumps({'FC value': mincostflows})
