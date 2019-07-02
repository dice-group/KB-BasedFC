package org.dice.service.api;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jackson.annotate.JsonProperty;

public class Fact implements Serializable {

	@JsonProperty("taskId")
	private long taskId;
	
	@JsonProperty("subject")
	private String subject;
	
	@JsonProperty("predicate")
	private String predicate;
	
	@JsonProperty("object")
	private String object;
	
	@JsonProperty("algorithm")
	private String algorithm;
	
	@JsonProperty("averageTruthValue")
	private double truthValue;
	
	@JsonProperty("results")
	private HashMap<String, Double> results = new HashMap<String, Double>();
	
	public Fact() {
		
	}
	
	public Fact(Fact fact) {
		this.taskId = fact.taskId;
		this.subject = fact.subject;
		this.predicate = fact.predicate;
		this.object = fact.object;
	}
	
	public long getTaskId() {
		return taskId;
	}
	
	public void setTaskId(long id) {
		this.taskId = id;
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
	
	public void addResults(String algorithm, Double truthValue) {
		this.results.put(algorithm, truthValue);
		estimateAverageTruthValue();
	}
	
	private void estimateAverageTruthValue() {
		Iterator<Double> it = this.results.values().iterator();
		double result = 0;
		int count = 0;
	    while (it.hasNext()) {
	      Double x = it.next();
	      result += x;
	      count++;
	    }
	    this.truthValue = result / count;
	}
}
