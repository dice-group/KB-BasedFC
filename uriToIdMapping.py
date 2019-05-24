import sqlite3

# conn = sqlite3.connect('database/knowledgegraph.db')
# cursor = conn.cursor()
# print("Opened database successfully")
#
# subjectURI = 'dbr:Al_Attles'
# predicateURI = 'dbo:team'
# objectURI = 'dbr:Golden_State_Warriors'
#
# cursor.execute("select nodeId from nodes where nodeValue = ?;", [subjectURI])
# results = cursor.fetchone()
#
# cursor.execute("select relationId from relations where relationValue = ?;", [predicateURI])
# results1 = cursor.fetchone()
#
# cursor.execute("select nodeId from nodes where nodeValue = ?;", [objectURI])
# results2 = cursor.fetchone()
#
# print(results[0])
# print(results1[0])
# print(results2[0])
# conn.close()

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