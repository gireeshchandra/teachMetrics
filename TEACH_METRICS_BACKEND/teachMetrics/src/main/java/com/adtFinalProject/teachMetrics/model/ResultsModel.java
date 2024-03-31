package com.adtFinalProject.teachMetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "c_results")
public class ResultsModel {
	/*@Id
	private int _id;*/
	private int accId;
	private int score;
	private String percentage;
	
	/*public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}*/
	public int getAccId() {
		return accId;
	}
	public void setAccId(int accId) {
		this.accId = accId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
}
