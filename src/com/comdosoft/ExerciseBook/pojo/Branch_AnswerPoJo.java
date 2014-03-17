package com.comdosoft.ExerciseBook.pojo;

public class Branch_AnswerPoJo {
	private String id;
	private String answer;
	private String ratio;

	public Branch_AnswerPoJo(String id, String answer, String ratio) {
		super();
		this.id = id;
		this.answer = answer;
		this.ratio = ratio;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

}
