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
public class User_Data_Servlet extends HttpServlet implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());

	private Fact mainFact = null;
	private Processor message = null;


	private Thread thread;
	private String threadName;
	User_Data_Servlet(String name) {
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

		this.message = new Processor();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");

		if(mainFact.getAlgorithm().equals("all")) {
			User_Data_Servlet thread1 = new User_Data_Servlet("kstream");
			thread1.start();
			User_Data_Servlet thread2 = new User_Data_Servlet("relklinker");
			thread2.start();
			User_Data_Servlet thread3 = new User_Data_Servlet("klinker");
			thread3.start();
		}
		else {
			String algorithm = mainFact.getAlgorithm();
			Fact subFact2 = new Fact(mainFact);
			subFact2.setAlgorithm(algorithm);

			try {
				message.checkFact(subFact2);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Double truthScore = Double.valueOf(subFact2.getTruthValue());

			LOGGER.info("Extracted truth score " + truthScore + " from the result");

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
}
