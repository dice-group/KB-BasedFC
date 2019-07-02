import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

public class Processor {
	private static final Logger LOGGER = Logger.getLogger(ApiController.class.getName());
	
	public void checkFact(Fact fact) {

		result = client.call("{\"args\": [\"" + statement + "\"], \"kwargs\": {}}");

			if(fact.getAlgorithm().equals("kstream"))
				client.setRoutingKey("kstream.stream");
			else if(fact.getAlgorithm().equals("relklinker"))
				client.setRoutingKey("relklinker.stream");
			else if(fact.getAlgorithm().equals("klinker"))
				client.setRoutingKey("klinker.stream");
			else if(fact.getAlgorithm().equals("predpath"))
				client.setRoutingKey("predpath.stream");
			else if(fact.getAlgorithm().equals("pra"))
				client.setRoutingKey("pra.stream");
			else if(fact.getAlgorithm().equals("katz"))
				client.setRoutingKey("katz.stream");
			else if(fact.getAlgorithm().equals("pathent"))
				client.setRoutingKey("pathent.stream");
			else if(fact.getAlgorithm().equals("simrank"))
				client.setRoutingKey("simrank.stream");
			else if(fact.getAlgorithm().equals("adamic_adar"))
				client.setRoutingKey("adamic_adar.stream");
			else if(fact.getAlgorithm().equals("jaccard"))
				client.setRoutingKey("jaccard.stream");
			else if(fact.getAlgorithm().equals("degree_product"))
				client.setRoutingKey("degree_product.stream");

		LOGGER.info("Result " + result + " received from microservice");

		fact.setTruthValue(extractTruthValue(result));
	}

	public String generateRDFStatement(Fact fact) {
		String date = getDate();
		long id = fact.getTaskId();
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
		String[] valuesInQuotes = StringUtils.substringsBetween(result , "\\\"", "\\\"");
		return Double.parseDouble(valuesInQuotes[0]);
	}

}
