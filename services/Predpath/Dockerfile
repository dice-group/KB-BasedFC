FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir predpath
COPY . /predpath
WORKDIR /predpath
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run predpath_service
