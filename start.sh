cd services
cd Kstream
nameko run kstream_service &
cd ../Klinker
nameko run klinker_service &
cd ../Relklinker
nameko run relklinker_service &
cd ../AdamicAdar
nameko run adamic_adar_service &	
cd ../DegreeProduct
nameko run degree_product_service &
cd ../Jaccard
nameko run jaccard_service &
cd ../Katz
nameko run katz_service &
cd ../Pathent
nameko run pathent_service &
cd ../Pra
nameko run pra_service &
cd ../Predpath
nameko run predpath_service &
cd ../Simrank
nameko run simrank_service &
