import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class User_Data_Servlet
 */
@WebServlet({"/User_Data_Servlet", "/s_p_o"})
public class User_Data_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String true_false_value = "";
    /**
     * Default constructor. 
     */
    public User_Data_Servlet() {
    	super();
        // TODO Auto-generated constructor stub
    }
	 
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}*/

	 //doPost method used to perform the post request from the frontend
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	
		//pw is an object used to print on frontend
		PrintWriter pw=response.getWriter();
				
		//Below we get the subject, predicate and object from the Http Request
		//and store it to different variables.
		String subject = request.getParameter("subject");
		String predicate = request.getParameter("predicate");
		String object = request.getParameter("object");
		
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
		
		//Prints the response on the frontend. 
		pw.println("Response from the microservice is: " + true_false_value); 
		
		//doGet(request, response);
	}

}
