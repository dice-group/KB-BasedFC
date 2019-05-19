
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Servlet implementation class User_Data_Servlet
 */
@WebServlet("/s_p_o")
public class User_Data_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String true_false_value = "";
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public User_Data_Servlet() {
		super();
		// TODO Auto-generated constructor stub
		LOGGER.info("Logger Name: "+ LOGGER.getName());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		BufferedReader reader = request.getReader();
		
		JSONObject jObject = new JSONObject(reader.readLine());
		
		reader.close();
		
		LOGGER.info("Received http request " + jObject.toString() + " from front-end");
		
		int taskId = jObject.getInt("taskid");
		String subject = jObject.getString("subject");
		String predicate = jObject.getString("predicate");
		String object = jObject.getString("object");
		String algorithm = jObject.getString("algorithm");
		
		LOGGER.info("Extracted values " + taskId + "," + subject + "," + predicate + "," + object + "," + algorithm);
		
		MessageForm message = new MessageForm();
		
		LOGGER.info("Sub, Pred, Obj sent to API component");
		
		MessageForm.sendData(subject, predicate, object);
		
		true_false_value = message.getResponse();
		
		LOGGER.info("Value " + true_false_value + " returned from microservice");
		
		PrintWriter pw = response.getWriter();
	}
}
