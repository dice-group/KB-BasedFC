import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public class MessageForm {
	private static final Logger LOGGER = Logger.getLogger(User_Data_Servlet.class.getName());
	private static AtomicLong idCounter = new AtomicLong();

	public void sendData(Fact fact) throws IOException, TimeoutException, InterruptedException {

		String result = "";

		if(fact.getAlgorithm().equals("all")) {
			String[] routingKeys = new String[] {"kstream.stream, relklinker.stream, klinker.stream"};
			for (String key : routingKeys) {

				RPCClient multiple = new RPCClient();
				String statement = generateRDFStatement(fact);

				LOGGER.info("Sending " + statement + " to microservice");

				multiple.setRoutingKey(key);

				LOGGER.info("Sending " + statement + " to " + key + " queue.");

				result = multiple.call("{\"args\": [\"" + statement + "\"], \"kwargs\": {}}");
				
				LOGGER.info("Result " + result + " received from microservice");
				
				fact.setTruthValue(extractTruthValue(result));
			}
		}
		else {

			RPCClient client = new RPCClient();
			String statement = generateRDFStatement(fact);

			if(fact.getAlgorithm().equals("kstream"))
				client.setRoutingKey("kstream.stream");
			else if(fact.getAlgorithm().equals("relklinker"))
				client.setRoutingKey("relklinker.stream");
			else if(fact.getAlgorithm().equals("klinker"))
				client.setRoutingKey("klinker.stream");

			LOGGER.info("Sending " + statement + " to " + fact.getAlgorithm() + " microservice");

			result = client.call("{\"args\": [\"" + statement + "\"], \"kwargs\": {}}");
			
			LOGGER.info("Result " + result + " received from microservice");
			
			fact.setTruthValue(extractTruthValue(result));
		}
	}

	public String generateRDFStatement(Fact fact) {
		String date = getDate();
		long id = idCounter.getAndIncrement();
		return String.format("<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#type> <http://www.w3.org/%s-rdf-syntax-ns#Statement> ." +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#subject> <%s> ." +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#predicate> <%s> ." +
				"<http://swc2017.aksw.org/task2/dataset/s-%s> <http://www.w3.org/%s-rdf-syntax-ns#object> <%s> ."
				, id
				, date
				, date
				, id
				, date
				, fact.getSubject()
				, id
				, date
				, fact.getPredicate()
				, id
				, date
				, fact.getObject());
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