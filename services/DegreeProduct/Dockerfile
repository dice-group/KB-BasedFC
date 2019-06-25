FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Rakshit Krishnappa Ravi "rakshit@mail.uni-paderborn.de""
RUN mkdir degree_product
COPY . /degree_product
WORKDIR /degree_product
RUN pip install -r requirements.txt
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run degree_product_service
