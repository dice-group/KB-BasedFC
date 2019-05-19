import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

public class MessageForm {
	private static String simplifiedData;
	private static String response = "";
	
	//Constructor
	public MessageForm()
	{}
	
	public static void sendData(String subjectUri, String predicateUri, String objectUri) {
		
		  int x =  gtn();
		  int num = Math.abs(x);
		  String dates = getDate();
		  simplifiedData = String.format("<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#type> <http://www.w3.org/%s-rdf-syntax-ns#Statement> .\n" +
	                "<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#subject> <%s> .\n" +
	                "<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#predicate> <%s> .\n" +
	                "<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#object> <%s> .\n"
	                , num
	                , dates
	                , dates
	                , num
	                , dates
	                , subjectUri
	                , num
	                , dates
	                , predicateUri
	                , num
	                , dates
	                , objectUri);
		 //System.out.println("this is the data in message form: " + simplifiedData);
		 
		 /*Calling the CallToMicroservice and passing "simplifiedData" which
		 is in ISWC format to the micro-service. */
		 MainClass obj = new MainClass();
		 obj.mainmethod(simplifiedData);
		 
		 /*Getting the response form the MainClass 
		 in order to pass it to User_Data_Servlet.java class */
		 response = obj.getResult();
		 //System.out.println("Response is: " + response);
		
	}
	
	//Function to generate random number
	public static int gtn()
	{
		Random randomno = new Random();
	    int value = randomno.nextInt();
		return value;
	}
	
	//Function to get current date
	public static String getDate()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		String dates = dtf.format(now);
		return dates;
	}
	
	//Get Function to get the (truth or false) value
	public String getResponse()
	{ 
		return response;
	}

}