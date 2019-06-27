FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir relklinker
COPY . /relklinker
WORKDIR /relklinker
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run relklinker_service
