package com.comdosoft.ExerciseBook.pojo;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class knowledges_card 
{
	private String answer;
	private String branch_question_id;
	private String card_bag_id;
	private String content;
	private String created_at;
	private String full_text;
	private String id;
	private String mistake_types;
	private String options;
	private String question_id;
	private String resource_url;
	private String type;
	private String updated_at;
	private String your_answer;
	private List<Integer> tagsarr;
	public knowledges_card() {
	}
	public knowledges_card(String answer, String branch_question_id,
			String card_bag_id,  String content,
			String created_at, String id, String mistake_types, String options,
			String question_id, String resource_url, String type,
			String updated_at, String your_answer) {
		this.answer = answer;
		this.branch_question_id = branch_question_id;
		this.card_bag_id = card_bag_id;
		this.content = content;
		this.created_at = created_at;
		this.id = id;
		this.mistake_types = mistake_types;
		this.options = options;
		this.question_id = question_id;
		this.resource_url = resource_url;
		this.type = type;
		this.updated_at = updated_at;
		this.your_answer = your_answer;
	}
	public knowledges_card(String answer, String branch_question_id,
			String card_bag_id, String content, String created_at,
			String full_text, String id, String mistake_types, String options,
			String question_id, String resource_url, String type,
			String updated_at, String your_answer) {
		this.answer = answer;
		this.branch_question_id = branch_question_id;
		this.card_bag_id = card_bag_id;
		this.content = content;
		this.created_at = created_at;
		this.full_text = full_text;
		this.id = id;
		this.mistake_types = mistake_types;
		this.options = options;
		this.question_id = question_id;
		this.resource_url = resource_url;
		this.type = type;
		this.updated_at = updated_at;
		this.your_answer = your_answer;
	}
	
	
	public String getFull_text() {
		return full_text;
	}
	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}
	public knowledges_card(String answer, String branch_question_id,
			String card_bag_id, String content, String created_at,
			String full_text, String id, String mistake_types, String options,
			String question_id, String resource_url, String type,
			String updated_at, String your_answer,  List<Integer> tags_list) {
		this.answer = answer;
		this.branch_question_id = branch_question_id;
		this.card_bag_id = card_bag_id;
		this.content = content;
		this.created_at = created_at;
		this.full_text = full_text;
		this.id = id;
		this.mistake_types = mistake_types;
		this.options = options;
		this.question_id = question_id;
		this.resource_url = resource_url;
		this.type = type;
		this.updated_at = updated_at;
		this.your_answer = your_answer;
		this.tagsarr = tags_list;
	}
	
	public List<Integer> getTagsarr() {
		return tagsarr;
	}
	public void setTagsarr(List<Integer> tagsarr) {
		this.tagsarr = tagsarr;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getBranch_question_id() {
		return branch_question_id;
	}
	public void setBranch_question_id(String branch_question_id) {
		this.branch_question_id = branch_question_id;
	}
	public String getCard_bag_id() {
		return card_bag_id;
	}
	public void setCard_bag_id(String card_bag_id) {
		this.card_bag_id = card_bag_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMistake_types() {
		return mistake_types;
	}
	public void setMistake_types(String mistake_types) {
		this.mistake_types = mistake_types;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getYour_answer() {
		return your_answer;
	}
	public void setYour_answer(String your_answer) {
		this.your_answer = your_answer;
	}
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(answer);  
//		dest.writeString(branch_question_id);
//		dest.writeString(card_bag_id);  
//		dest.writeString(content);
//		dest.writeString(created_at);
//		dest.writeString(full_text);
//		dest.writeString(id);
//		dest.writeString(mistake_types);
//		dest.writeString(options);
//		dest.writeString(question_id);
//		dest.writeString(resource_url);
//		dest.writeString(type);
//		dest.writeString(updated_at);
//		dest.writeString(your_answer);
//		dest.writeList(tagsarr);
//	}
//	public static final Parcelable.Creator<knowledges_card> CREATOR  = new Creator<knowledges_card>(){  
//		public knowledges_card createFromParcel(Parcel source) {  
//			knowledges_card app=  new knowledges_card();  
//			app.answer = source.readString();  
//			app.branch_question_id = source.readString();  
//			app.card_bag_id = source.readString();  
//			app.content = source.readString();  
//			app.created_at = source.readString();  
//			app.full_text = source.readString();  
//			app.id = source.readString();  
//			app.mistake_types = source.readString();  
//			app.options = source.readString();  
//			app.question_id = source.readString();  
//			app.resource_url = source.readString();  
//			app.type = source.readString();  
//			app.updated_at = source.readString();
//			app.your_answer = source.readString();  
//			return app;  
//		}  
//		public knowledges_card[] newArray(int size) {  
//			return new knowledges_card[size];  
//		}  
//
//	};  
	
}
