FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir jaccard
COPY . /jaccard
WORKDIR /jaccard
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run jaccard_service
