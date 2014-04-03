package com.comdosoft.ExerciseBook.pojo;

import java.util.List;
public class knowledges_card 
{
	private String id;
	private String card_bag_id;
	private String mistake_types;
	private String branch_question_id;
	private String your_answer;		//你的回答
	private String created_at;
	private String updated_at;
	private String content;
	private String question_id;
	private String resource_url;
	private String types;
	private String answer;		    //正确答案
	private String options;			//选项/完形
	private String full_text;		//完形填空    
	private List<Integer> tagsarr;
	public knowledges_card() {
	}
	public knowledges_card(String id, String card_bag_id, String mistake_types,
			String branch_question_id, String your_answer, String created_at,
			String updated_at, String content, String question_id,
			String resource_url, String types, String answer, String options,
			String full_text, List<Integer> tagsarr) {
		this.id = id;
		this.card_bag_id = card_bag_id;
		this.mistake_types = mistake_types;
		this.branch_question_id = branch_question_id;
		this.your_answer = your_answer;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.content = content;
		this.question_id = question_id;
		this.resource_url = resource_url;
		this.types = types;
		this.answer = answer;
		this.options = options;
		this.full_text = full_text;
		this.tagsarr = tagsarr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCard_bag_id() {
		return card_bag_id;
	}
	public void setCard_bag_id(String card_bag_id) {
		this.card_bag_id = card_bag_id;
	}
	public String getMistake_types() {
		return mistake_types;
	}
	public void setMistake_types(String mistake_types) {
		this.mistake_types = mistake_types;
	}
	public String getBranch_question_id() {
		return branch_question_id;
	}
	public void setBranch_question_id(String branch_question_id) {
		this.branch_question_id = branch_question_id;
	}
	public String getYour_answer() {
		return your_answer;
	}
	public void setYour_answer(String your_answer) {
		this.your_answer = your_answer;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getResource_url() {
		return resource_url;
	}
	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getFull_text() {
		return full_text;
	}
	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}
	public List<Integer> getTagsarr() {
		return tagsarr;
	}
	public void setTagsarr(List<Integer> tagsarr) {
		this.tagsarr = tagsarr;
	}

}
