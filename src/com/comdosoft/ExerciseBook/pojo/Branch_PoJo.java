package com.comdosoft.ExerciseBook.pojo;

public class Branch_PoJo {
	private int id;
	private String opption;
	private String answer;

	public Branch_PoJo(int id, String opption, String answer) {
		super();
		this.id = id;
		this.opption = opption;
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOpption() {
		return opption;
	}

	public void setOpption(String opption) {
		this.opption = opption;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
