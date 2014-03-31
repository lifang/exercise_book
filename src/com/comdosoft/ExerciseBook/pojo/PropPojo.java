package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class PropPojo {

	private String type;
	private List<Integer> branch_id;

	public PropPojo() {
	}

	public PropPojo(String type, List<Integer> branch_id) {
		super();
		this.type = type;
		this.branch_id = branch_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Integer> getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(List<Integer> branch_id) {
		this.branch_id = branch_id;
	}

}
