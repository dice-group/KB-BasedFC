import static org.junit.Assert.assertEquals;

import org.dice.service.PreProcessor;
import org.dice.service.api.Fact;
import org.junit.Before;
import org.junit.Test;

public class AdamicAdarTest {

	private Fact fact = new Fact();
	private PreProcessor process = new PreProcessor();

	@Before
	public void init() {
		fact.setAlgorithm("adamic_adar");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}

	@Test
	public void testSingle() {
		process.checkFact(fact);
		assertEquals((double)1.0, fact.getTruthValue(), 0.00001);
	}
}
