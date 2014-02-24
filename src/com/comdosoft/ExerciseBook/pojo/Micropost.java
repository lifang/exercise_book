package com.comdosoft.ExerciseBook.pojo;




public class Micropost
{

	private String id;  //  信息  id
	private String  user_id;//创建者id
	private String user_types;//创建者类型
	private String  name;//主消息的创建者名字，
	private String  content;  //  消息内容
	private String avatar_url; //创建者头像
	private String  created_at;//创建时间
	private String reply_microposts_count;

	public Micropost(){}

	public Micropost(String id, String user_id, String user_types, String name,
			 String content, String avatar_url, String created_at,String reply_microposts_count) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.user_types = user_types;
		this.name = name;
		
		this.content = content;
		this.avatar_url = avatar_url;
		this.created_at = created_at;
		this.reply_microposts_count= reply_microposts_count;
	}

	
	public String getReply_microposts_count() {
		return reply_microposts_count;
	}

	public void setReply_microposts_count(String reply_microposts_count) {
		this.reply_microposts_count = reply_microposts_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_types() {
		return user_types;
	}

	public void setUser_types(String user_types) {
		this.user_types = user_types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "Micropost [id=" + id + ", user_id=" + user_id + ", user_types="
				+ user_types + ", name=" + name + ", content=" + content
				+ ", avatar_url=" + avatar_url + ", created_at=" + created_at
				+ ", reply_microposts_count=" + reply_microposts_count + "]";
	}
}
