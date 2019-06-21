FROM ubuntu
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir katz
COPY . /katz
WORKDIR /katz
RUN apt-get -y update && apt-get -y install g++
RUN apt-get -y update && apt-get -y upgrade && apt-get -y install python2.7 python-pip
RUN pip install -r requirements.txt
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run katz_service
