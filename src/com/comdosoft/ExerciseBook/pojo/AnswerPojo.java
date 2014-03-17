package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class AnswerPojo {
	private String status;
	private String update_time;
	private String questions_item;
	private String branch_item;
	private String use_time;
	private List<Answer_QuestionsPojo> questions;

	
	public AnswerPojo() {
		this.setQuestions_item("-1");
		this.setBranch_item("-1");
	}

	public AnswerPojo(String status, String update_time,
			String questions_item, String branch_item, String use_time,
			List<Answer_QuestionsPojo> questions) {
		super();
		this.status = status;
		this.update_time = update_time;
		this.questions_item = questions_item;
		this.branch_item = branch_item;
		this.use_time = use_time;
		this.questions = questions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getQuestions_item() {
		return questions_item;
	}

	public void setQuestions_item(String questions_item) {
		this.questions_item = questions_item;
	}

	public String getBranch_item() {
		return branch_item;
	}

	public void setBranch_item(String branch_item) {
		this.branch_item = branch_item;
	}

	public String getUse_time() {
		return use_time;
	}

	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}

	public List<Answer_QuestionsPojo> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Answer_QuestionsPojo> questions) {
		this.questions = questions;
	}

}