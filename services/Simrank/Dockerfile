FROM enriozuni/docker_ubuntu_python27_gpp
MAINTAINER Enri Ozuni "enriozuni@hotmail.com"
RUN mkdir simrank
COPY . /simrank
WORKDIR /simrank
RUN pip install -r requirements.txt
RUN python setup.py build_ext -if
RUN python setup.py install
CMD nameko run simrank_service
