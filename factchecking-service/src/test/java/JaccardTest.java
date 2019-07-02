import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class JaccardTest {

	private Fact fact = new Fact();
	private Processor form = new Processor();

	@Before
	public void init() {
		fact.setAlgorithm("jaccard");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}

	@Test
	public void testSingle() {
		form.checkFact(fact);
		assertEquals((double)0.0016583747927, fact.getTruthValue(), 0.00001);
	}
}
