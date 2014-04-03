package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class WorkPoJo {

	private int id;
	private String name;
	private String start_time;
	private String end_time;
	private String question_packages_url;
	private String updated_at;
	private String answer_url;
	private List<Integer> question_types;
	private List<Integer> finish_types;
	private int number;

	public WorkPoJo() {
	}

	public WorkPoJo(int id, String name, String start_time, String end_time,
			String question_packages_url, String updated_at, String answer_url,
			List<Integer> question_types, List<Integer> finish_types, int number) {
		super();
		this.id = id;
		this.name = name;
		this.start_time = start_time;
		this.end_time = end_time;
		this.question_packages_url = question_packages_url;
		this.updated_at = updated_at;
		this.answer_url = answer_url;
		this.question_types = question_types;
		this.finish_types = finish_types;
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getQuestion_packages_url() {
		return question_packages_url;
	}

	public void setQuestion_packages_url(String question_packages_url) {
		this.question_packages_url = question_packages_url;
	}

	public List<Integer> getQuestion_types() {
		return question_types;
	}

	public void setQuestion_types(List<Integer> question_types) {
		this.question_types = question_types;
	}

	public List<Integer> getFinish_types() {
		return finish_types;
	}

	public void setFinish_types(List<Integer> finish_types) {
		this.finish_types = finish_types;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getAnswer_url() {
		return answer_url;
	}

	public void setAnswer_url(String answer_url) {
		this.answer_url = answer_url;
	}
}
