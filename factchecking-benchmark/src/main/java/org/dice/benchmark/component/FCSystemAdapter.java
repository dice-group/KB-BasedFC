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
	private String factcheckContainer;
	private String factcheckContainerUrl;

	@Override
	public void init() throws Exception {

		super.init();
		LOGGER.debug("Init()");

		//Create factchecking-service container
		factcheckContainer = createContainer(BenchmarkConstants.FACTCHECK_SERVICE_IMAGE_NAME, Constants.CONTAINER_TYPE_SYSTEM,
				new String[]{});

		if (factcheckContainer.isEmpty()) {
			LOGGER.debug("Error while creating factcheck-service container {}", factcheckContainer);
			throw new Exception("Service container not created");
		} else {
			LOGGER.debug("factcheck-service container created {}", factcheckContainer);
			factcheckContainerUrl = "http://" + factcheckContainer + ":8080/api/hobbitTask/";

			LOGGER.debug("factcheck-service container accessible from {}", factcheckContainerUrl);
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

		Client client = new Client(map, MediaType.MULTIPART_FORM_DATA, factcheckContainerUrl);
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

        if (!factcheckContainer.isEmpty())
            stopContainer(factcheckContainer);

		super.close();
	}

}
