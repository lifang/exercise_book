package com.comdosoft.ExerciseBook.pojo;

public class Child_Micropost {

//	id,sender_id,发送者id，sender_types发送者类型,
//	sender_name发送者姓名,sender_nickname发送者昵称，
//	sender_avatar_url发送者头像url,content消息内容,reciver_name接受者姓名,
//	reciver_nickname接受者昵称,sender_avatar_url接受者头像url,created_at创建时间
	

	private String id;
	private String sender_id;
	private String sender_types;
	private String sender_name;
	
	private String sender_avatar_url;//发送者头像url
	private String content;//消息内容
	private String reciver_name;

	private String created_at ;
	private String good ;
	
	public Child_Micropost(){};
	
	public Child_Micropost(String id, String sender_id, String sender_types,
			String sender_name, 
			String sender_avatar_url, String content, String reciver_name,
			String created_at,String good) {
		super();
		this.id = id;
		this.sender_id = sender_id;
		this.sender_types = sender_types;
		this.sender_name = sender_name;
		
		this.sender_avatar_url = sender_avatar_url;
		this.content = content;
		this.reciver_name = reciver_name;
		
		this.created_at = created_at;
		this.good = good;
	}

	public String getGood() {
		return good;
	}

	public void setGood(String good) {
		this.good = good;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

	public String getSender_types() {
		return sender_types;
	}

	public void setSender_types(String sender_types) {
		this.sender_types = sender_types;
	}

	public String getSender_name() {
		return sender_name;
	}

	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	

	public String getSender_avatar_url() {
		return sender_avatar_url;
	}

	public void setSender_avatar_url(String sender_avatar_url) {
		this.sender_avatar_url = sender_avatar_url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReciver_name() {
		return reciver_name;
	}

	public void setReciver_name(String reciver_name) {
		this.reciver_name = reciver_name;
	}


	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	
	
	
	
	
	
}
