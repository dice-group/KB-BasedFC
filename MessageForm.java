import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;

public class MessageForm {
	private static String simplifiedData;

	public MessageForm()
	{
		//User_Data_Servlet User_Data_Servlet = new User_Data_Servlet();
		//User_Data_Servlet.doPost(request, response);
	}
	
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
		 System.out.println(simplifiedData);
	}
	
	//function to generate random number
	public static int gtn()
	{
		Random randomno = new Random();
	    int value = randomno.nextInt();
		return value;
	}
	
	//function to get current date
	public static String getDate()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		String dates = dtf.format(now);
		System.out.println(); 
		return dates;
	}

}
