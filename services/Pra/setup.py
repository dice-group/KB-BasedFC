""" 
Script for Cython code compilation. 

ref: https://docs.python.org/3/distutils/apiref.html
"""

import os
import argparse

from os.path import dirname, abspath
from Cython.Build import cythonize
from numpy import get_include
from setuptools import setup, Extension

_wd = abspath(os.curdir)
base = abspath(dirname(__file__))
_incl = [get_include()]

# change to the project directory
os.chdir(base)

# first, create an Extension object w/ appropriate name and sources
ext = [
	# ====== DATA STRUCTURES ======
	Extension(
		name='datastructures.heap', 
		sources=['datastructures/heap.pyx'], 
		include_dirs=_incl,
		extra_compile_args=['-w'],
		extra_link_args=['-w']
	),
	# ====== PATH RANKING ALGORITHM (PRA) ======
	Extension(
		name='algorithms.pra.pra_helper', 
		sources=['algorithms/pra/pra_helper.pyx'], 
		include_dirs=_incl,
		extra_compile_args=['-w'],
		extra_link_args=['-w'],
		language='c++'
	)
]

# use cythonize on the extension object
# setup(ext_modules=cythonize(ext))

kwargs = dict(
    name="knowledgestream",
    description='Knowledge Stream Algorithm',
    version='0.1.0',
    author='Prashant Shiralkar and others (see CONTRIBUTORS.md)',
    author_email='pshiralk@indiana.edu',
    packages=[
        'datastructures', 'algorithms', 'algorithms.pra'
    ],
    include_package_data=True,
    install_requires=[
        'numpy',
        'scipy',
    ],
    entry_points={
        'console_scripts': [
            'kstream = algorithms.__main__:main'
        ]
    },
    ext_modules=cythonize(ext)
)

parser = argparse.ArgumentParser(description=__file__, add_help=False)

if __name__ == '__main__':
    args, rest = parser.parse_known_args()
    kwargs['script_args'] = rest
    setup(**kwargs)

# back to the working directory
os.chdir(_wd)
