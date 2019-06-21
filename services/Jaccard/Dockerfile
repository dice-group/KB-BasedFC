FROM ubuntu
MAINTAINER Rakshit Krishnappa Ravi "rakshit@mail.uni-paderborn.de"
RUN mkdir jaccard
COPY . /jaccard
WORKDIR /jaccard
RUN apt-get -y update && apt-get -y install g++
RUN apt-get -y update && apt-get -y upgrade && apt-get -y install python2.7 python-pip
RUN pip install -r requirements.txt
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run jaccard_service
