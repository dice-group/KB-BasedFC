package org.dice.benchmark.component;

import org.dice.benchmark.BenchmarkConstants;
import org.dice.benchmark.system.api.Client;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractSystemAdapter;
import org.dice.benchmark.system.api.FactCheckingHobbitResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

public class FCSystemAdapter extends AbstractSystemAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FCSystemAdapter.class);
	private String factcheckingContainer;
	private String factcheckingContainerUrl;
	private String rabbitmqContainer;
	private String kstreamContainer;
	private String klinkerContainer;
	private String relklinkerContainer;

	@Override
	public void init() throws Exception {

		super.init();
		LOGGER.debug("Init()");

		//Create rabbitmq container
		rabbitmqContainer = createContainer(BenchmarkConstants.RABBITMQ_IMAGE_NAME, Constants.CONTAINER_TYPE_BENCHMARK,
				new String[]{});

		if (rabbitmqContainer.isEmpty()) {
			LOGGER.debug("Error while creating rabbitmq container {}", rabbitmqContainer);
			throw new Exception("Rabbitmq container not created");
		} else
			LOGGER.debug("Rabbitmq container created {}", rabbitmqContainer);

		//Create kstream container
		kstreamContainer = createContainer(BenchmarkConstants.KSTREAM_MICROSERVICE_IMAGE_NAME, Constants.CONTAINER_TYPE_SYSTEM,
				new String[]{"RABBITMQ_HOSTNAME=" + rabbitmqContainer});

		if (kstreamContainer.isEmpty()) {
			LOGGER.debug("Error while creating kstream container {}", kstreamContainer);
			throw new Exception("Kstream container not created");
		} else
			LOGGER.debug("Kstream container created {}", kstreamContainer);

		//Create klinker container
		klinkerContainer = createContainer(BenchmarkConstants.KLINKER_MICROSERVICE_IMAGE_NAME, Constants.CONTAINER_TYPE_SYSTEM,
				new String[]{"RABBITMQ_HOSTNAME=" + rabbitmqContainer});

		if (klinkerContainer.isEmpty()) {
			LOGGER.debug("Error while creating klinker container {}", klinkerContainer);
			throw new Exception("Klinker container not created");
		} else
			LOGGER.debug("Klinker container created {}", klinkerContainer);

		//Create relklinker container
		relklinkerContainer = createContainer(BenchmarkConstants.RELKLINKER_MICROSERVICE_IMAGE_NAME, Constants.CONTAINER_TYPE_SYSTEM,
				new String[]{"RABBITMQ_HOSTNAME=" + rabbitmqContainer});

		if (relklinkerContainer.isEmpty()) {
			LOGGER.debug("Error while creating relklinker container {}", relklinkerContainer);
			throw new Exception("Relklinker container not created");
		} else
			LOGGER.debug("Relklinker container created {}", relklinkerContainer);

		//Create factchecking-service container
		factcheckingContainer = createContainer(BenchmarkConstants.FACTCHECKING_SERVICE_IMAGE_NAME, Constants.CONTAINER_TYPE_SYSTEM,
				new String[]{"RABBITMQ_HOSTNAME=" + rabbitmqContainer});

		if (factcheckingContainer.isEmpty()) {
			LOGGER.debug("Error while creating factchecking-service container {}", factcheckingContainer);
			throw new Exception("Service container not created");
		} else {
			LOGGER.debug("factchecking-service container created {}", factcheckingContainer);
			factcheckingContainerUrl = "http://" + factcheckingContainer + ":8080/factchecking-service/api/hobbitTask/";

			LOGGER.debug("factchecking-service container accessible from {}", factcheckingContainerUrl);
		}
	}

	@Override
	public void receiveGeneratedData(byte[] data) {
		String dataStr = new String(data);
		LOGGER.trace("receiveGeneratedData(" + new String(data) + "): " + dataStr);
	}

	@Override
	public void receiveGeneratedTask(String taskId, byte[] data) {
		LOGGER.debug("receiveGeneratedTask({})->{}", taskId, new String(data));

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("dataISWC", data);
		map.add("taskId", taskId);

		Client client = new Client(map, MediaType.MULTIPART_FORM_DATA, factcheckingContainerUrl);
		ResponseEntity<FactCheckingHobbitResponse> response = client.getResponse(HttpMethod.POST);

		if (response.getStatusCode().equals(HttpStatus.OK)) {

			FactCheckingHobbitResponse result = response.getBody();

			try {
				LOGGER.debug("sendResultToEvalStorage({})->{}", taskId, result.getTruthValue());
				sendResultToEvalStorage(taskId, String.valueOf(result.getTruthValue()).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			try {

				LOGGER.error("{} received for Task {}", response.getStatusCode(), taskId);
				sendResultToEvalStorage(taskId, String.valueOf(0.0).getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {

		LOGGER.debug("close()");

		if (!factcheckingContainer.isEmpty())
			stopContainer(factcheckingContainer);
		if (!rabbitmqContainer.isEmpty())
			stopContainer(rabbitmqContainer);
		if (!kstreamContainer.isEmpty())
			stopContainer(kstreamContainer);
		if (!klinkerContainer.isEmpty())
			stopContainer(klinkerContainer);
		if (!relklinkerContainer.isEmpty())
			stopContainer(relklinkerContainer);

		super.close();
	}

}
