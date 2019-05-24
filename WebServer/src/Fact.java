import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class Fact implements Serializable {

	@JsonProperty("taskId")
	private int taskId;
	
	@JsonProperty("subject")
	private String subject;
	
	@JsonProperty("predicate")
	private String predicate;
	
	@JsonProperty("object")
	private String object;
	
	@JsonProperty("algorithm")
	private String algorithm;
	
	@JsonProperty("truthValue")
	private double truthValue;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public double getTruthValue() {
		return truthValue;
	}
	public void setTruthValue(double truthValue) {
		this.truthValue = truthValue;
	}
}
