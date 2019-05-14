import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainClass {
	private String value1 = "";
    
	
	public void CallToMicroservice(String data){
		// TODO Auto-generated method stub
		try {
			//System.out.println("In Main:  " + data);
			RPCClient client = new RPCClient();
			String result = client.call("{\"args\": [392035, 599, 2115741], \"kwargs\": {}}");
			System.out.println(result);
			value1 = result;
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
	
	public String getResults()
	{ 
		return value1;
	}

	


}
