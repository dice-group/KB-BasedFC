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

/**
 * Servlet implementation class User_Data_Servlet
 */
@WebServlet("/s_p_o")
public class User_Data_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public User_Data_Servlet() {
		super();
		// TODO Auto-generated constructor stub
		LOGGER.info("Logger Name: " + LOGGER.getName());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO Auto-generated method stub

		BufferedReader reader = request.getReader();
		ObjectMapper mapper = new ObjectMapper();

		JSONObject jObject = new JSONObject(reader.readLine());

		reader.close();

		LOGGER.info("Received http request " + jObject.toString() + " from front-end");

		Fact mainFact = mapper.readValue(jObject.toString(), Fact.class);

		LOGGER.info("Extracted values " + mainFact.getTaskId() + "," + mainFact.getSubject() + "," + mainFact.getPredicate() + "," + mainFact.getObject() + "," + mainFact.getAlgorithm());

		MessageForm message = new MessageForm();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");

		if(mainFact.getAlgorithm().equals("all")) {
			String[] algorithms = new String[] {"kstream", "relklinker", "klinker"};
			for (String algorithm : algorithms) {
				Fact subFact1 = new Fact(mainFact);
				subFact1.setAlgorithm(algorithm);
				try {
					message.sendData(subFact1);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Double truthScore = Double.valueOf(subFact1.getTruthValue());

				LOGGER.info("Extracted truth score " + truthScore + " from the result");

				mainFact.addResults(algorithm, truthScore);
			}
		}
		else {
			String algorithm = mainFact.getAlgorithm();
			Fact subFact2 = new Fact(mainFact);
			subFact2.setAlgorithm(algorithm);
			
			try {
				message.sendData(subFact2);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Double truthScore = Double.valueOf(subFact2.getTruthValue());

			LOGGER.info("Extracted truth score " + truthScore + " from the result");

			mainFact.addResults(algorithm, truthScore);
		}
		out.print(mapper.writeValueAsString(mainFact));
		out.close();
	}
}
