package com.comdosoft.ExerciseBook.pojo;

import android.widget.TextView;

public class AnswerWirePojo {
	private TextView tv;
	private int status;
	
	public AnswerWirePojo(){}
	
	public AnswerWirePojo(TextView tv, int status) {
		super();
		this.tv = tv;
		this.status = status;
	}
	
	public TextView getTv() {
		return tv;
	}
	public void setTv(TextView tv) {
		this.tv = tv;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}