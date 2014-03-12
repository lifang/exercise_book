package com.comdosoft.ExerciseBook.pojo;

public class Time_Limit_UnswerPojo {
	private int status;
	private String update_time;
	private int questions_item;
	private int branch_item;
	private int use_time;
	private String[] questions;

	public Time_Limit_UnswerPojo(int status, String update_time,
			int questions_item, int branch_item, int use_time,
			String[] questions) {
		super();
		this.status = status;
		this.update_time = update_time;
		this.questions_item = questions_item;
		this.branch_item = branch_item;
		this.use_time = use_time;
		this.questions = questions;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public int getQuestions_item() {
		return questions_item;
	}

	public void setQuestions_item(int questions_item) {
		this.questions_item = questions_item;
	}

	public int getBranch_item() {
		return branch_item;
	}

	public void setBranch_item(int branch_item) {
		this.branch_item = branch_item;
	}

	public int getUse_time() {
		return use_time;
	}

	public void setUse_time(int use_time) {
		this.use_time = use_time;
	}

	public String[] getQuestions() {
		return questions;
	}

	public void setQuestions(String[] questions) {
		this.questions = questions;
	}

}