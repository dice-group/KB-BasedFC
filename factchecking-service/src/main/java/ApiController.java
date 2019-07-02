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

import java.util.logging.Logger;

@WebServlet("/api")
public class ApiController extends HttpServlet implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());

	private Fact mainFact = null;
	private Processor message = null;
	private Thread thread;
	private String threadName;

	ApiController(String name) {
		threadName = name;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

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

		this.mainFact = mapper.readValue(jObject.toString(), Fact.class);

		LOGGER.info("Extracted values " + mainFact.getTaskId() + "," + mainFact.getSubject() + "," + mainFact.getPredicate() + "," + mainFact.getObject() + "," + mainFact.getAlgorithm());

		this.message = new PreProcessor();

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");

		if(mainFact.getAlgorithm().equals("all")) {
			ApiController thread1 = new ApiController("kstream");
			thread1.start();
			ApiController thread2 = new ApiController("relklinker");
			thread2.start();
			ApiController thread3 = new ApiController("klinker");
			thread3.start();
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
		out.print(mapper.writeValueAsString(mainFact));
		out.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Fact subFact1 = new Fact(mainFact);
		subFact1.setAlgorithm(Thread.currentThread().getName());
		try {
			message.sendData(subFact1);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Double truthScore = Double.valueOf(subFact1.getTruthValue());

		LOGGER.info("Extracted truth score " + truthScore + " from the result");

		mainFact.addResults(Thread.currentThread().getName(), truthScore);
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
