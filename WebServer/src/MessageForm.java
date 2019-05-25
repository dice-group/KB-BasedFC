import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public class MessageForm {
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());

	public void sendData(Fact fact) {

		String result = "";
		int x =  generateRandomNumber();
		int num = Math.abs(x);
		String date = getDate();
		String simplifiedData = String.format("<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#type> <http://www.w3.org/%s-rdf-syntax-ns#Statement> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#subject> <%s> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#predicate> <%s> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#object> <%s> .\n"
				, num
				, date
				, date
				, num
				, date
				, fact.getSubject()
				, num
				, date
				, fact.getPredicate()
				, num
				, date
				, fact.getObject());

		try {
			RPCClient client = new RPCClient();
			LOGGER.info("Sending " + simplifiedData + " to microservice");
//			result = client.call(simplifiedData);
			result = client.call("{\"args\": [392035, 599, 2115741], \"kwargs\": {}}");
			LOGGER.info("Result " + result + " received from microservice");
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
//		fact.setTruthValue(extractTruthValue(result));
		fact.setTruthValue(extractTruthValue(String.format("<http://swc2017.aksw.org/task2/dataset/s-%s> <http://swc2017.aksw.org/hasTruthValue> \"0.0\"^^<http://www.w3.org/2001/XMLSchema#double> .", num)));
	}

	private int generateRandomNumber()
	{
		Random randomno = new Random();
		int value = randomno.nextInt();
		return value;
	}

	private String getDate()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		return date;
	}
	
	private double extractTruthValue(String result) {
		String[] valuesInQuotes = StringUtils.substringsBetween(result , "\"", "\"");
		return Double.parseDouble(valuesInQuotes[0]);
	}

}
