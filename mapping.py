import sqlite3

def convert(suri, puri, ouri):
	conn = sqlite3.connect('database/knowledgegraph.db')
	cursor = conn.cursor()
	print('Opened database successfully!')

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