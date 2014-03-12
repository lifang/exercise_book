package com.comdosoft.ExerciseBook.pojo;

public class AnswerOrderPojo {

	private int question_id;
	private int branch_questionid;
	private String answer;

	public AnswerOrderPojo() {
	}

	public AnswerOrderPojo(int question_id, int branch_questionid, String answer) {
		super();
		this.question_id = question_id;
		this.branch_questionid = branch_questionid;
		this.answer = answer;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public int getBranch_questionid() {
		return branch_questionid;
	}

	public void setBranch_questionid(int branch_questionid) {
		this.branch_questionid = branch_questionid;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
