FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir adamic_adar
COPY . /adamic_adar
WORKDIR /adamic_adar
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run adamic_adar_service
