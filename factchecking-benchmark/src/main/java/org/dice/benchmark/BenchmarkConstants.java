package org.dice.benchmark;

public class BenchmarkConstants {

	public static String GIT_USERNAME = "factcheckingdemo";
	public static String GIT_REPO_PATH = "git.project-hobbit.eu:4567/" + GIT_USERNAME + "/";
	public static String PROJECT_NAME = "factchecking-benchmark";
	
	public static final String FACTCHECKING_SERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/factchecking-service";
	public static final String RABBITMQ_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/rabbitmq";
	public static final String KSTREAM_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/kstream-microservice";
	public static final String KLINKER_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/klinker-microservice";
	public static final String RELKLINKER_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/relklinker-microservice";
	public static final String PREDPATH_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/predpath-microservice";
	public static final String PRA_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/pra-microservice";
	public static final String KATZ_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/katz-microservice";
	public static final String PATHENT_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/pathent-microservice";
	public static final String SIMRANK_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/simrank-microservice";
	public static final String ADAMICADAR_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/adamicadar-microservice";
	public static final String JACCARD_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/jaccard-microservice";
	public static final String DEGREEPRODUCT_MICROSERVICE_IMAGE_NAME = GIT_REPO_PATH + PROJECT_NAME + "/degreeproduct-microservice";
}
