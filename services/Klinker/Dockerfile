FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir klinker
COPY . /klinker
WORKDIR /klinker
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run klinker_service
