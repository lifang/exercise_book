package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class ListHistoryPojo {
	private int id;
	private List<HistoryPojo> historyList;

	public ListHistoryPojo() {
		super();
	}

	
	public ListHistoryPojo(int id, List<HistoryPojo> historyList) {
		super();
		this.id = id;
		this.historyList = historyList;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<HistoryPojo> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<HistoryPojo> historyList) {
		this.historyList = historyList;
	}

}
