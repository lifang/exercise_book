package com.comdosoft.ExerciseBook.pojo;

public class AnswerJson {

	private String pub_id;
	private String status;
	private String[] props;
	private String reading;
	private String listening;
	private String time_limit;
	private String selecting;
	private String lining;
	private String cloze;
	private String sort;

	public AnswerJson(String pub_id, String status, String[] props,
			String reading, String listening, String time_limit,
			String selecting, String lining, String cloze, String sort) {
		super();
		this.pub_id = pub_id;
		this.status = status;
		this.props = props;
		this.reading = reading;
		this.listening = listening;
		this.time_limit = time_limit;
		this.selecting = selecting;
		this.lining = lining;
		this.cloze = cloze;
		this.sort = sort;
	}

	public String getPub_id() {
		return pub_id;
	}

	public void setPub_id(String pub_id) {
		this.pub_id = pub_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getProps() {
		return props;
	}

	public void setProps(String[] props) {
		this.props = props;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getListening() {
		return listening;
	}

	public void setListening(String listening) {
		this.listening = listening;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getSelecting() {
		return selecting;
	}

	public void setSelecting(String selecting) {
		this.selecting = selecting;
	}

	public String getLining() {
		return lining;
	}

	public void setLining(String lining) {
		this.lining = lining;
	}

	public String getCloze() {
		return cloze;
	}

	public void setCloze(String cloze) {
		this.cloze = cloze;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
