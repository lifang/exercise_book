package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class PropNumberPojo {

	private int type;
	private int branch_id;

	public PropNumberPojo() {
	}

	public PropNumberPojo(int type, int branch_id) {
		super();
		this.type = type;
		this.branch_id = branch_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}

}
