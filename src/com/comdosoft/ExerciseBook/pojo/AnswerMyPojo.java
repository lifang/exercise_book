package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class AnswerMyPojo {

	private int questions_item;
	private int branch_item;
	private int status;
	private int use_time;
	private List<String> answer;
	private List<Integer> ratio;

	public AnswerMyPojo() {
	}

	public AnswerMyPojo(int questions_item, int branch_item, int use_time,
			int status) {
		super();
		this.questions_item = questions_item;
		this.branch_item = branch_item;
		this.use_time = use_time;
		this.status = status;
	}

	public AnswerMyPojo(int status, int use_time, List<String> answer,
			List<Integer> ratio) {
		super();
		this.status = status;
		this.use_time = use_time;
		this.answer = answer;
		this.ratio = ratio;
	}

	public int getUse_time() {
		return use_time;
	}

	public void setUse_time(int use_time) {
		this.use_time = use_time;
	}

	public List<Integer> getRatio() {
		return ratio;
	}

	public void setRatio(List<Integer> ratio) {
		this.ratio = ratio;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}

}
