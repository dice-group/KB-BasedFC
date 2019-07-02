import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AdamicAdarTest {

	private Fact fact = new Fact();
	private Processor process = new Processor();
	
	@Before
	public void init() {
		fact.setAlgorithm("adamic_adar");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}
	
	@Test
	public void testSingle() {
		try {
			process.checkFact(fact);
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
		assertEquals((double)0.9102392266268373, fact.getTruthValue(), 0.00001);
	}
}
