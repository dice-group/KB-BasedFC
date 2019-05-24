import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainClass {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		try {
			RPCClient client = new RPCClient();
			String result = client.call("{\"args\": [\"dbr:Al_Attles\", \"dbo:team\", \"dbr:Golden_State_Warriors\"], \"kwargs\": {}}");
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
