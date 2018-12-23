import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainClass {

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		// TODO Auto-generated method stub
		RPCClient client = new RPCClient();
		System.out.println(client.call("{\"args\": [392035, 699, 2115741], \"kwargs\": {}}"));
	}

}
