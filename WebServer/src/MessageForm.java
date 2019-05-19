import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class MessageForm {
	private static String simplifiedData;
	private static String response = "";
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());

	public static void sendData(String subjectUri, String predicateUri, String objectUri) {

		int x =  generateRandomNumber();
		int num = Math.abs(x);
		String date = getDate();
		simplifiedData = String.format("<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#type> <http://www.w3.org/%s-rdf-syntax-ns#Statement> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#subject> <%s> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#predicate> <%s> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#object> <%s> .\n"
				, num
				, date
				, date
				, num
				, date
				, subjectUri
				, num
				, date
				, predicateUri
				, num
				, date
				, objectUri);

		try {
			RPCClient client = new RPCClient();
			LOGGER.info("Sending " + simplifiedData + " to microservice");
			response = client.call(simplifiedData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int generateRandomNumber()
	{
		Random randomno = new Random();
		int value = randomno.nextInt();
		return value;
	}

	public static String getDate()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		return date;
	}

	public String getResponse()
	{ 
		return response;
	}

}
