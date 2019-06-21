FROM ubuntu
MAINTAINER Rakshit Krishnappa Ravi "rakshit@mail.uni-paderborn.de""
RUN mkdir adamic_adar
COPY . /adamic_adar
WORKDIR /adamic_adar
RUN apt-get -y update && apt-get -y install g++
RUN apt-get -y update && apt-get -y upgrade && apt-get -y install python2.7 python-pip
RUN pip install -r requirements.txt
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run adamic_adar_service
