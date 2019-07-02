import static org.junit.Assert.assertEquals;

import org.dice.service.PreProcessor;
import org.dice.service.api.Fact;
import org.junit.Before;
import org.junit.Test;

public class PathentTest {

	private Fact fact = new Fact();
	private PreProcessor process = new PreProcessor();

	@Before
	public void init() {
		fact.setAlgorithm("pathent");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}

	@Test
	public void testSingle() {
		process.checkFact(fact);
		assertEquals((double)2878.18272387978, fact.getTruthValue(), 0.00001);
	}
}
