package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class ClozePojo {
	private int id;
	private String content;
	private List<Branch_PoJo> list;

	public ClozePojo(int id, String content, List<Branch_PoJo> list) {
		super();
		this.id = id;
		this.content = content;
		this.list = list;
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

	public List<Branch_PoJo> getList() {
		return list;
	}

	public void setList(List<Branch_PoJo> list) {
		this.list = list;
	}

}