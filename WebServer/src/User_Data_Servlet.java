
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class User_Data_Servlet
 */
@WebServlet("/s_p_o")
public class User_Data_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String true_false_value = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public User_Data_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//PrintWriter out = response.getWriter();
		//response.setContentType("application/json");
        //response.setCharacterEncoding("UTF-8");
       // out.print(true_false_value);
       // out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub			 
				
				//System.out.println("reached the user_data_servlet");
				//Below we get the subject, predicate and object from the Http Request
				//and store it to different variables.
				String subject = request.getParameter("subject");
				String predicate = request.getParameter("predicate");
				String object = request.getParameter("object");
				//System.out.println("data has been stored in their respective strings");
				System.out.println("subject is:" + subject);
				System.out.println("predicate is:" + predicate);
				System.out.println("object is:" + object);
				//calls the method "sendData" from the class "MessageForm"
				//and passes the subject, predicate and object as parameters.
				MessageForm message = new MessageForm();
				MessageForm.sendData(subject, predicate, object);
			
				//pw.print(" subject is: " + subject);
				//pw.print(" Predicate is: " + predicate);
//pw.print(" object is: " + object);
				
//Once we get the response (the truth or false value) from the microservice
//then we retrieve that value in this class and pass it as response.
true_false_value = message.getResponse();

/*try {
JSONObject jObj = new JSONObject(true_false_value);
response.setContentType("application/json");
response.getWriter().write("The Microservice response is : " + jObj);
} catch (JSONException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}*/

response.setContentType("application/json");
PrintWriter out = response.getWriter();

JSONObject jsonObject = new JSONObject();
jsonObject.put("name", true_false_value);
System.out.println("hamza here"+jsonObject.toString());
out.print(jsonObject);

/*response.setContentType("application/json");// set content to json
PrintWriter out = response.getWriter();
out.print(true_false_value);
out.flush();*/

//Prints the response on the fronted. 
//out.println("Response from the microservice is: " + true_false_value); 		//doGet(request, response);
	}
	}
