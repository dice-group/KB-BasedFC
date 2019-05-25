
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
	private static String result = "";
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
		PrintWriter out = response.getWriter();

		JSONObject jObject = new JSONObject(reader.readLine());

		reader.close();

		LOGGER.info("Received http request " + jObject.toString() + " from front-end");

		Fact fact = mapper.readValue(jObject.toString(), Fact.class);

		LOGGER.info("Extracted values " + fact.getTaskId() + "," + fact.getSubject() + "," + fact.getPredicate() + "," + fact.getObject() + "," + fact.getAlgorithm());

		MessageForm message = new MessageForm();

		LOGGER.info("Sub, Pred, Obj sent to API component");

		try {
			message.sendData(fact);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.info("Extracted truth score " + fact.getTruthValue() + " from the result");

		response.setContentType("application/json");
		out.print(mapper.writeValueAsString(fact));
		out.close();
	}
}
