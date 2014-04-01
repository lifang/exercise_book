package com.comdosoft.ExerciseBook.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class tags 
{
	private String card_bag_id;
	private String created_at;
	private String id;
	private String name;
	private String upadted_at;
	public tags(String card_bag_id, String created_at, String id, String name,
			String upadted_at) {
		this.card_bag_id = card_bag_id;
		this.created_at = created_at;
		this.id = id;
		this.name = name;
		this.upadted_at = upadted_at;
	}
	public tags() {
	}
	public String getCard_bag_id() {
		return card_bag_id;
	}
	public void setCard_bag_id(String card_bag_id) {
		this.card_bag_id = card_bag_id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpadted_at() {
		return upadted_at;
	}
	public void setUpadted_at(String upadted_at) {
		this.upadted_at = upadted_at;
	}
//	public int describeContents() {
//		return 0;
//	}
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(card_bag_id);  
//		dest.writeString(created_at);
//		dest.writeString(id);  
//		dest.writeString(name);
//		dest.writeString(upadted_at);
//	}
//	public static final Parcelable.Creator<tags> CREATOR  = new Creator<tags>(){  
//		public tags createFromParcel(Parcel source) {  
//			tags app=  new tags();  
//			app.card_bag_id = source.readString();  
//			app.created_at = source.readString();  
//			app.id = source.readString();
//			app.name = source.readString();  
//			app.upadted_at = source.readString();  
//			return app;  
//		}  
//		public tags[] newArray(int size) {  
//			return new tags[size];  
//		}  
//
//	};  

}
