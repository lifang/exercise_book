package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class Answer_QuestionsPojo {

	private String id;
	private List<Branch_AnswerPoJo> branch_questions;

	public Answer_QuestionsPojo(String id, List<Branch_AnswerPoJo> branch_questions) {
		super();
		this.id = id;
		this.branch_questions = branch_questions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Branch_AnswerPoJo> getBranch_questions() {
		return branch_questions;
	}

	public void setBranch_questions(List<Branch_AnswerPoJo> branch_questions) {
		this.branch_questions = branch_questions;
	}

}
