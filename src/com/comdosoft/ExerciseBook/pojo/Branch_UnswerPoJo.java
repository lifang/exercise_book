package com.comdosoft.ExerciseBook.pojo;

public class Branch_UnswerPoJo {
	private int id;
	private String answer;
	private int ratio;

	public Branch_UnswerPoJo(int id, String answer, int ratio) {
		super();
		this.id = id;
		this.answer = answer;
		this.ratio = ratio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

}
