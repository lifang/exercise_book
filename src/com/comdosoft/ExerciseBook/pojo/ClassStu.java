package com.comdosoft.ExerciseBook.pojo;

public class ClassStu 
{
	private String id;
	private String Name;
	private String Avatar_url;
	private int success_1;
	private int success_2;
	private int success_3;
	private int success_4;
	public ClassStu(String name, String avatar_url, int success_1,
			int success_2, int success_3, int success_4) {
		Name = name;
		Avatar_url = avatar_url;
		this.success_1 = success_1;
		this.success_2 = success_2;
		this.success_3 = success_3;
		this.success_4 = success_4;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAvatar_url() {
		return Avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		Avatar_url = avatar_url;
	}
	public int getSuccess_1() {
		return success_1;
	}
	public void setSuccess_1(int success_1) {
		this.success_1 = success_1;
	}
	public int getSuccess_2() {
		return success_2;
	}
	public void setSuccess_2(int success_2) {
		this.success_2 = success_2;
	}
	
	public int getSuccess_3() {
		return success_3;
	}
	public void setSuccess_3(int success_3) {
		this.success_3 = success_3;
	}
	public ClassStu() {
	}
	public ClassStu(String id, String name, String avatar_url, int success_1,
			int success_2, int success_3, int success_4) {
		this.id = id;
		Name = name;
		Avatar_url = avatar_url;
		this.success_1 = success_1;
		this.success_2 = success_2;
		this.success_3 = success_3;
		this.success_4 = success_4;
	}
	public int getSuccess_4() {
		return success_4;
	}
	public void setSuccess_4(int success_4) {
		this.success_4 = success_4;
	}

}
