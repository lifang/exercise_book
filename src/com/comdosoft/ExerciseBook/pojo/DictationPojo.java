package com.comdosoft.ExerciseBook.pojo;

public class DictationPojo {
	private String value;
	private int flag;


	public DictationPojo() {
		super();
	}

	public DictationPojo(String value, int flag) {
		super();
		this.value = value;
		this.flag = flag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
