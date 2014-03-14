package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class ListeningPojo {
	private int id;
	private List<QuestionPojo> questtionList;
	
	public ListeningPojo() {
		super();
	}

	public ListeningPojo(int id, List<QuestionPojo> questtionList) {
		super();
		this.id = id;
		this.questtionList = questtionList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<QuestionPojo> getQuesttionList() {
		return questtionList;
	}

	public void setQuesttionList(List<QuestionPojo> questtionList) {
		this.questtionList = questtionList;
	}

}
