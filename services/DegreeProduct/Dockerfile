FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir degree_product
COPY . /degree_product
WORKDIR /degree_product
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run degree_product_service
