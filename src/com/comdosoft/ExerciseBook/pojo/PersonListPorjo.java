package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class PersonListPorjo {
	private int id;
	private List<PersonPojo> persons;

	public PersonListPorjo() {
	}

	public PersonListPorjo(int id, List<PersonPojo> persons) {
		super();
		this.id = id;
		this.persons = persons;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "PersonListPorjo [id=" + id + ", persons=" + persons + "]";
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<PersonPojo> getPersons() {
		return persons;
	}

	public void setPersons(List<PersonPojo> persons) {
		this.persons = persons;
	}

}
