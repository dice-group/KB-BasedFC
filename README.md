# KB-BasedFC
*Wrapper for KB-based Fact Checking systems*<br>

# Data
Download data from the following [URL](https://mega.nz/#!qJU12aZa!RdUs7Uhnd5g4B4ugMgT8m_I_p_OrxDn6QnsBWGylSfk) and decompress it inside each listed folder in the `services` subdirectory.

Download database used by the microservices from the following [URL](https://mega.nz/#!GAFjXIob!OsB-VtK0wtsEBHVwbghJhslgllQUp1sYLsp34bVmKu4) and decompress it inside each listed folder in the `services` subdirectory.

Download database used by the microservices from the following [URL](https://mega.nz/#!GAFjXIob!OsB-VtK0wtsEBHVwbghJhslgllQUp1sYLsp34bVmKu4) and decompress it inside `KB-BasedFC` directory

# System requirements

* **OS:** Linux Ubuntu / Mac OSX 10.12 (Sierra)
* **Python:** Python 2.7 (we developed and tested using the Anaconda distribution)
* **Memory requirements:** >4 GB

# Runing the services

To run a specific service, go to the `services` subdirectory and inside one of the services listes in there:

* For creating the Docker image of the service from the Dockerfile <br>
```docker build -t [NAME_OF_IMAGE] .```

* For creating and running the Docker container from the created service image <br>
```docker container run -it --network="host" --name [<NAME_OF_CONTAINER] [NAME_OF_IMAGE]```

To build the Docker images and run the Docker containers altogether:<br>
* Go to the `services` subdirectory and run the following command <br>
```docker-compose up```

# Training the model for Predpath and PRA services

 The microservice for PredPath and PRA algorithms requires a training model that has been trained and placed in `output` folder under the file `trained_predpath_model.pkl` for *predpath* and `trained_pra_model.pkl` for *PRA* in their respective folders.
 
 If you wish to train the model with different set of records. You will have to run the following code. <br>
```python ./predpath_service_training.py``` or ```python ./pra_service_training.py``` accordingly.

**Note:** Change the path under *stream* function to that particular file location <br>
`datafile = abspath(expanduser('./datasets/sample_data_predpath.csv'))` and make sure you are in their respective folders.

# Example Triple

**Subject URI :** `http://dbpedia.org/resource/Kobe_Bryant`<br>
**Predicate URI :** `http://dbpedia.org/ontology/team`<br>
**Object URI :** `http://dbpedia.org/resource/Los_Angeles_Lakers`
