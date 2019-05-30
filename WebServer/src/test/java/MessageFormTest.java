import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class MessageFormTest {

	private Fact fact = new Fact();
	private MessageForm form = new MessageForm();
	
	@Before
	public void init() {
		fact.setAlgorithm("klinker");
		fact.setSubject("http://dbpedia.org/resource/Kobe_Bryant");
		fact.setPredicate("http://dbpedia.org/ontology/team");
		fact.setObject("http://dbpedia.org/resource/Los_Angeles_Lakers");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testSingle() {
		try {
			form.sendData(fact);
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
		assertEquals(0.08863, fact.getTruthValue());
	}
}
