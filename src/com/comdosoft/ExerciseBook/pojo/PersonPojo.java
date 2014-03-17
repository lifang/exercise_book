package com.comdosoft.ExerciseBook.pojo;

public class PersonPojo {

	private String name;
	private int age;
	private int sex;
	private int type;

	public PersonPojo() {
	};

	public PersonPojo(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public PersonPojo(String name, int age, int type) {
		super();
		this.name = name;
		this.age = age;
		this.type = type;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		switch (type) {
		case 0:
			return "PersonPojo [name=" + name + ", age=" + age + ", sex=" + sex
					+ "]";
		case 1:
			return "PersonPojo [name=" + name + ", age=" + age + "]";
		}
		return null;
	}
}
