import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class KstreamTest {

	private Fact fact = new Fact();
	private Processor process = new Processor();

	@Before
	public void init() {
		fact.setAlgorithm("kstream");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}

	@Test
	public void testSingle() {
		process.checkFact(fact);
		assertEquals((double)0.08862923190529146, fact.getTruthValue(), 0.00001);
	}
}
