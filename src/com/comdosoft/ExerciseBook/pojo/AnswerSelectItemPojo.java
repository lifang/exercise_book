package com.comdosoft.ExerciseBook.pojo;

import android.widget.LinearLayout;

public class AnswerSelectItemPojo {
	private LinearLayout linearLayout;
	private int status;
	
	public AnswerSelectItemPojo(){}

	public AnswerSelectItemPojo(LinearLayout linearLayout, int status) {
		super();
		this.linearLayout = linearLayout;
		this.status = status;
	}

	public LinearLayout getLinearLayout() {
		return linearLayout;
	}

	public void setLinearLayout(LinearLayout linearLayout) {
		this.linearLayout = linearLayout;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
