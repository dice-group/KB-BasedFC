import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainClass {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		String simplifiedData = String.format(
				"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> <dbr:Al_Attles> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <dbo:team> .\n" +
				"<http://swc2017.aksw.org/task2/dataset/s-789147186> <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <dbr:Golden_State_Warriors> .\n"
				);
		
		try {
			RPCClient client = new RPCClient();
//			String result = client.call("{\"args\": [\"dbr:Al_Attles\", \"dbo:team\", \"dbr:Golden_State_Warriors\"], \"kwargs\": {}}");
			String result = client.call(simplifiedData);
//			String result = client.call("{\"args\": [392035, 599, 2115741], \"kwargs\": {}}");
			System.out.println(result);
			client.close();
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
	}

}
