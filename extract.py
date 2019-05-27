data = "<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement> .\n"+\
		"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> <dbr:Al_Attles> .\n"+\
		"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <dbo:team> .\n"+\
		"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <dbr:Golden_State_Warriors> .\n"

print(data)

dataArr = data.split(' .')

# Extract ID and date
headISWC = dataArr[0]
headISWCArr = headISWC.split(' ')

idISWC = headISWCArr[0]
idISWC = idISWC[1:-1].split("> <")
idISWCArr = idISWC[0].split('-')
identification = idISWCArr[1]
print(identification)

dateISWC = headISWCArr[1]
dateISWC = dateISWC[1:-1].split("> <")
dateISWC = dateISWC[0]
dateISWCArr = dateISWC.split('w3.org/')
dateISWC = dateISWCArr[1]
dateISWCArr = dateISWC.split('-')
date = dateISWCArr[0]
print(date)


# Extract subject, predicate and object in their ISWC format
subISWC = dataArr[1]
predISWC = dataArr[2]
objISWC = dataArr[3]


# Extract subject
subArray = subISWC.split(' ')

sub = subArray[2][1:-1].split("> <")
sub = sub[0]
print(sub)


# Extract predicate
predArray = predISWC.split(' ')

pred = predArray[2][1:-1].split("> <")
pred = pred[0]
print(pred)


# Extract object
objArray = objISWC.split(' ')

obj = objArray[2][1:-1].split("> <")
obj = obj[0]
print(obj)

