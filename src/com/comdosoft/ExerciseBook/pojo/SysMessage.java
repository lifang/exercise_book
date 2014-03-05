package com.comdosoft.ExerciseBook.pojo;

public class SysMessage 
{
	private String content;
	private String created_at;
	
	private String id;
	private String class_id;
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public SysMessage() {
		super();
	}
	public String getContent() {
		return content;
	}
	public SysMessage(String content, String created_at, String id,
			String class_id) {
		super();
		this.content = content;
		this.created_at = created_at;
		this.id = id;
		this.class_id = class_id;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreated_at() {
		return created_at;
	}
	public SysMessage(String content, String created_at, String id) {
		super();
		this.content = content;
		this.created_at = created_at;
		this.id = id;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
