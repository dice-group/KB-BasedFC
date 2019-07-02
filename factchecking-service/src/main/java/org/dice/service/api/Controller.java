package org.dice.service.api;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dice.service.PreProcessor;
import org.dice.service.RPCClient;
import org.json.JSONObject;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Path("/api")
public class Controller {

	private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

	public Controller() {
		super();
		LOGGER.info("Logger Name: " + LOGGER.getName());
	}

	@GET
	@Path("/default")
	public String defaultpage() {
		return "Welcome to KB-Based-Factchecking";
	}

	@POST
	@Path("/execTask/")
	public Response execT(String request) throws JsonParseException, JsonMappingException, IOException {

		System.out.println(request);
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jObject = new JSONObject(request);

		LOGGER.info("Received http request " + jObject.toString() + " from front-end");

		Fact mainFact = mapper.readValue(jObject.toString(), Fact.class);

		LOGGER.info("Extracted values " + mainFact.getTaskId() + "," + mainFact.getSubject() + "," + mainFact.getPredicate() + "," + mainFact.getObject() + "," + mainFact.getAlgorithm());

		PreProcessor message = new PreProcessor();

		if(mainFact.getAlgorithm().equals("all")) {
			String[] algorithms = new String[] {"kstream", "relklinker", "klinker"};
			for (String algorithm : algorithms) {
				Fact subFact1 = new Fact(mainFact);
				subFact1.setAlgorithm(algorithm);
				message.checkFact(subFact1);
				Double truthScore = Double.valueOf(subFact1.getTruthValue());

				LOGGER.info("Extracted truth score " + truthScore + " from the result");

				mainFact.addResults(algorithm, truthScore);
			}
		}
		else {
			String algorithm = mainFact.getAlgorithm();
			Fact subFact2 = new Fact(mainFact);
			subFact2.setAlgorithm(algorithm);
			message.checkFact(subFact2);
			Double truthScore = Double.valueOf(subFact2.getTruthValue());

			LOGGER.info("Extracted truth score " + truthScore + " from the result");

			mainFact.setTaskId(subFact2.getTaskId());
			mainFact.addResults(algorithm, truthScore);
		}
		return Response
				.status(Response.Status.OK)
				.entity(mapper.writeValueAsString(mainFact))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	@POST
	@Path("/hobbitTask/")
	public FactCheckingHobbitResponse execT(@PathParam(value = "taskId") String taskId,
			@PathParam(value = "dataISWC") String dataISWC) {

		LOGGER.info("Received HOBBIT Task : "+ taskId);

		LOGGER.info("Sending " + dataISWC + " to " + "XXX" + " microservice");

		String result = "";
		try {
			RPCClient client = new RPCClient();
			result = client.call("{\"args\": [\"" + dataISWC + "\"], \"kwargs\": {}}");
		} catch (IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
		}

		LOGGER.info("Result " + result + " received from microservice");

		double truthValue = extractTruthValue(result);

		LOGGER.info("Truth score " + truthValue + " returned for task " + taskId);

		return new FactCheckingHobbitResponse(taskId, truthValue);
	}

	private double extractTruthValue(String result) {
		
		String[] valuesInQuotes = StringUtils.substringsBetween(result , "\\\"", "\\\"");
		return Double.parseDouble(valuesInQuotes[0]);
	}
}
