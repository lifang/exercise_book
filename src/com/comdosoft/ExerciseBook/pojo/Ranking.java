package com.comdosoft.ExerciseBook.pojo;

public class Ranking 
{
	private String avatar_url;
	private String name;
	private String point;
	
	
	
	public Ranking() {
		super();
	}
	public Ranking(String avatar_url, String name, String point) {
		super();
		this.avatar_url = avatar_url;
		this.name = name;
		this.point = point;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	
	
	
}
