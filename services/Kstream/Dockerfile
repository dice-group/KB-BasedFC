FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir kstream
COPY . /kstream
WORKDIR /kstream
RUN pip install -r requirements.txt
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run kstream_service
