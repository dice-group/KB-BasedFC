import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@WebServlet("/s_p_o")
public class ApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ApiController.class.getName());

	public ApiController() {
		super();
		LOGGER.info("Logger Name: " + LOGGER.getName());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader reader = request.getReader();
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jObject = new JSONObject(reader.readLine());
		reader.close();

		LOGGER.info("Received http request " + jObject.toString() + " from front-end");

		Fact mainFact = mapper.readValue(jObject.toString(), Fact.class);

		LOGGER.info("Extracted values " + mainFact.getTaskId() + "," + mainFact.getSubject() + "," + mainFact.getPredicate() + "," + mainFact.getObject() + "," + mainFact.getAlgorithm());

		Processor message = new Processor();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");

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

			mainFact.addResults(algorithm, truthScore);
		}
		out.print(mapper.writeValueAsString(mainFact));
		out.close();
	}

//	public FactCheckingHobbitResponse execT(@RequestParam(value = "taskId") String taskId,
//			@RequestParam(value = "dataISWC", required = true) String dataISWC) {
//
//		LOGGER.info("Received HOBBIT Task : "+ taskId);
//
//		Fact fact = extractFactFromISWC(dataISWC, taskId);
//		Processor message = new Processor();
//		message.checkFact(fact);
//
//		LOGGER.info("Truth score " + fact.getTruthValue() + " returned for task " + taskId);
//
//		return new FactCheckingHobbitResponse(taskId, fact.getTruthValue());
//	}
//
//	private Fact extractFactFromISWC(String dataISWC, String taskId) {
//		// TODO compute fact from the ISWC string
//		return null;
//	}
}
