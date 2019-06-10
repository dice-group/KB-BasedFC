import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.hobbit.core.components.AbstractSystemAdapter;

public class HobbitSystemAdapter extends AbstractSystemAdapter {
	
	private static final Logger LOGGER = Logger.getLogger(HobbitSystemAdapter.class.getName());

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
	}
	
	@Override
	public void receiveGeneratedData(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveGeneratedTask(String taskId, byte[] data) {
		
		LOGGER.info("Received HOBBIT Task : " + LOGGER.getName());
		
		byte[] result = null;
		try {
			RPCClient client = new RPCClient();
			result = client.call("{\"args\": [\"" + data + "\"], \"kwargs\": {}}").getBytes();
			sendResultToEvalStorage(taskId, result);
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
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		super.close();
	}
	
}
