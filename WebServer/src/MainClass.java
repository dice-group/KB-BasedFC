import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainClass {
	private String value1 = "";
    
	
	public void mainmethod(String data){
		// TODO Auto-generated method stub
		
		try {
			//System.out.println("In Main3:  " + data);
			RPCClient client = new RPCClient();
			//System.out.println("In Main2");
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
	
	public String getResult()
	{ 
		return value1;
	}


}
