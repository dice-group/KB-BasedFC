FROM ubuntu
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir kstream
COPY . /kstream
WORKDIR /kstream
RUN apt-get -y update && apt-get -y install g++
RUN apt-get -y update && apt-get -y upgrade && apt-get -y install python2.7 python-pip
RUN pip install -r requirements.txt
CMD nameko run kstream_service
