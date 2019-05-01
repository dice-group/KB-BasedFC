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

    /**
     * Default constructor. 
     */
    public User_Data_Servlet() {
    	super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String subject = request.getParameter("subject");
		String predicate = request.getParameter("predicate");
		String object = request.getParameter("object");
		//prints the subject, predicate and object on the webpage.
		PrintWriter pw=response.getWriter();
		
		//calls the class MessageForm and passes the subject, predicate and object as parameters.
		MessageForm message = new MessageForm();
		MessageForm.sendData(subject, predicate, object);
		
		
		pw.print(" subject is: " + subject);
		pw.print(" Predicate is: " + predicate);
		pw.print(" object is: " + object);
		
		
		//doGet(request, response);
	}

}
