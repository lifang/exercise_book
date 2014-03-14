package com.comdosoft.ExerciseBook.pojo;

public class QuestionPojo {

	private int id;
	private String content;
	private String url;

	public QuestionPojo() {
		super();
	}
	

	public QuestionPojo(int id, String content, String url) {
		super();
		this.id = id;
		this.content = content;
		this.url = url;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "QuestionPojo [id=" + id + ", content=" + content + ", url="
				+ url + "]";
	}

}
