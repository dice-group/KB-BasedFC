import sys
import numpy as np
import warnings
import logging as log
import mapping
import extract

from nameko.rpc import rpc
from datastructures.rgraph import Graph
from time import time
from datetime import date

import os
from os.path import join, exists, abspath, expanduser

# OUR METHODS
from algorithms.linkpred.katz import katz

class Katz(object):

	name = 'katz'

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

	# ================= LINK PREDICTION ALGORITHMS ============

	def compute_katz(self, G, subs, preds, objs):
		"""
		Performs link prediction using a specified measure, such as Katz or SimRank.

		Parameters:
		-----------
		G: rgraph
			See `datastructures`.
		subs, preds, objs: sequence
			Sequences representing the subject, predicate and object of
			input triples.

		Returns:
		--------
		scores, times: sequence
			One sequence each for the proximity scores and times taken.
		"""
		measure_map = {
			'katz': {
				'measure': katz,
				'tag': 'KZ'
			}
		}

		selected_measure = 'katz'

		# back up
		data = G.csr.data.copy()
		indices = G.csr.indices.copy()
		indptr = G.csr.indptr.copy()

		# compute closure
		measure_name = measure_map[selected_measure]['tag']
		measure = measure_map[selected_measure]['measure']
		log.info('Computing {} for {} triples..'.format(measure_name, len(subs)))
		t1 = time()
		scores, times = [], []
		for idx, (s, p, o) in enumerate(zip(subs, preds, objs)):
			print '{}. Working on {}..'.format(idx + 1, (s, p, o)),
			sys.stdout.flush()
			ts = time()
			score = measure(G, s, p, o, linkpred=True)
			tend = time()
			print 'score: {:.5f}, time: {:.2f}s'.format(score, tend - ts)
			times.append(tend - ts)
			scores.append(score)

			# reset graph
			G.csr.data = data.copy()
			G.csr.indices = indices.copy()
			G.csr.indptr = indptr.copy()
			sys.stdout.flush()
		print ''
		return scores, times

	@rpc	# Methods are exposed to the outside world with entrypoint decorators (RPC in our case)
	def stream(self, data):

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

		# required for passing it to compute_katz
		sid, pid, oid = np.array([sid]), np.array([pid]), np.array([oid])

		t1 = time()

		print('\nTheir corresponding IDs are:')
		print(sid)
		print(pid)
		print(oid)
		print('\n')

		log.info('Computing KZ for triple')
		with warnings.catch_warnings():
			try:
				warnings.simplefilter("ignore")
				# compute katz
				scores, times = self.compute_katz(self.G, sid, pid, oid)

				log.info('Katz computation complete. Time taken: {:.2f} secs.\n'.format(time() - t1))
				result = '<http://swc2017.aksw.org/task2/dataset/s-' + str(
					identification) + '> <http://swc2017.aksw.org/hasTruthValue>\"' + str(
					scores[0]) + '\"<http://www.w3.org/2001/XMLSchema#double> .'
				print('The result in RDF format is:')
				print(result)

			except MemoryError:
				print('\nA MemoryError is successfully caught.')
				result = 'MemoryError'

		return result
