FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir pathent
COPY . /pathent
WORKDIR /pathent
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run pathent_service
