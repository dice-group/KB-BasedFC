def getValues(data):

	dataArr = data.split(' .')

	# Extract ID and date
	headISWC = dataArr[0]
	headISWCArr = headISWC.split(' ')

	idISWC = headISWCArr[0]
	idISWC = idISWC[1:-1].split("> <")
	idISWCArr = idISWC[0].split('-')
	identification = idISWCArr[1]

	dateISWC = headISWCArr[1]
	dateISWC = dateISWC[1:-1].split("> <")
	dateISWC = dateISWC[0]
	dateISWCArr = dateISWC.split('w3.org/')
	dateISWC = dateISWCArr[1]
	dateISWCArr = dateISWC.split('-')
	date = dateISWCArr[0]

	# Extract subject, predicate and object in their ISWC format
	subISWC = dataArr[1]
	predISWC = dataArr[2]
	objISWC = dataArr[3]

	# Extract subject
	subArray = subISWC.split(' ')

	sub = subArray[2][1:-1].split("> <")
	sub = sub[0]

	# Extract predicate
	predArray = predISWC.split(' ')

	pred = predArray[2][1:-1].split("> <")
	pred = pred[0]

	# Extract object
	objArray = objISWC.split(' ')

	obj = objArray[2][1:-1].split("> <")
	obj = obj[0]

	return identification, date, sub, pred, obj