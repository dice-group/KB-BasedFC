
public class FactCheckingHobbitResponse {
	private static final String RESPONSE =
			"<http://swc2017.aksw.org/%s/dataset/> " +
					"<http://swc2017.aksw.org/hasTruthValue> \"%s\"" +
					"^^<http://www.w3.org/2001/XMLSchema#double> .";

	private String taskId;
	private double truthValue = 0.0;

	private String responseData;

	FactCheckingHobbitResponse(String taskId, double truthValue) {
		this.taskId = taskId;
		this.truthValue = truthValue;
		init();
	}

	private void init() {
		responseData = String.format(RESPONSE, taskId, truthValue);
	}

	public double getTruthValue() {
		return truthValue;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getResponseData() {
		return responseData;
	}
}
