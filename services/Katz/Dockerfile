FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir katz
COPY . /katz
WORKDIR /katz
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run katz_service
