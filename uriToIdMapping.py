import sqlite3

conn = sqlite3.connect('database/knowledgegraph.db')
cursor = conn.cursor()
print("Opened database successfully")

cursor.execute("select nodeId from nodes where nodeValue = 'dbr:Al_Attles';")
results = cursor.fetchone()

cursor.execute("select relationId from relations where relationValue = 'dbo:team';")
results1 = cursor.fetchone()

cursor.execute("select nodeId from nodes where nodeValue = 'dbr:Golden_State_Warriors';")
results2 = cursor.fetchone()

print(results[0])
print(results1[0])
print(results2[0])
conn.close()