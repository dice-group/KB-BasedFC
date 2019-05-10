#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import os.path

data_folder = os.path.join("data", "kg")
file_to_open = os.path.join(data_folder, "relations.txt")

# f = open(file_to_open, "r")
# print(f.read())

data = np.genfromtxt(file_to_open, dtype=None, delimiter=" ")
