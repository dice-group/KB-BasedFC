import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class KatzTest {

	private Fact fact = new Fact();
	private Processor process = new Processor();

	@Before
	public void init() {
		fact.setAlgorithm("katz");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}

	@Test
	public void testSingle() {
		process.checkFact(fact);
		assertEquals((double)0.138625, fact.getTruthValue(), 0.00001);
	}
}
