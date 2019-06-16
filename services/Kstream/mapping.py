import sqlite3

def convert(suri, puri, ouri):
	conn = sqlite3.connect('database/knowledgegraph.db')
	cursor = conn.cursor()
	print('Opened database successfully!')

	if 'dbpedia.org/resource' in suri:
		suriArr = suri.split('dbpedia.org/resource/')
		suri = 'dbr:'+suriArr[1]
	elif 'dbpedia.org/ontology' in suri:
		suriArr = suri.split('dbpedia.org/ontology/')
		suri = 'dbo:'+suriArr[1]
	elif 'www.crunchbase.com/person' in suri:
		suriArr = suri.split('www.crunchbase.com/person/')
		suri = 'cb:person/'+suriArr[1]


	if 'dbpedia.org/ontology' in puri:
		puriArr = puri.split('dbpedia.org/ontology/')
		puri = 'dbo:'+puriArr[1]

	if 'dbpedia.org/resource' in ouri:
		ouriArr = ouri.split('dbpedia.org/resource/')
		ouri = 'dbr:'+ouriArr[1]
	elif 'dbpedia.org/ontology' in ouri:
		ouriArr = ouri.split('dbpedia.org/ontology/')
		ouri = 'dbo:'+ouriArr[1]
	elif 'www.crunchbase.com/person' in ouri:
		ouriArr = ouri.split('www.crunchbase.com/person/')
		ouri = 'cb:person/'+ouriArr[1]


	cursor.execute("select nodeId from nodes where nodeValue = ?;", [suri])
	sidResult = cursor.fetchone()
	sid = sidResult[0]

	cursor.execute("select relationId from relations where relationValue = ?;", [puri])
	pidResult = cursor.fetchone()
	pid = pidResult[0]

	cursor.execute("select nodeId from nodes where nodeValue = ?;", [ouri])
	oidResult = cursor.fetchone()
	oid = oidResult[0]

	return sid, pid, oid