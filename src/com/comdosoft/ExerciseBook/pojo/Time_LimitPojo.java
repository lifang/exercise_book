package com.comdosoft.ExerciseBook.pojo;

import java.util.Arrays;

public class Time_LimitPojo {
	private int id;
	private String content;
	private String[] opption;
	private String anwser;

	public Time_LimitPojo(int id, String content, String[] opption,
			String anwser) {
		super();
		this.id = id;
		this.content = content;
		this.opption = opption;
		this.anwser = anwser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getOpption() {
		return opption;
	}

	public void setOpption(String[] opption) {
		this.opption = opption;
	}

	public String getAnwser() {
		return anwser;
	}

	public void setAnwser(String anwser) {
		this.anwser = anwser;
	}

	@Override
	public String toString() {
		return "Time_LimitPojo [id=" + id + ", content=" + content
				+ ", opption=" + Arrays.toString(opption) + ", anwser="
				+ anwser + "]";
	}
	
	
}
