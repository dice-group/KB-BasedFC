FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir pra
COPY . /pra
WORKDIR /pra
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run pra_service
