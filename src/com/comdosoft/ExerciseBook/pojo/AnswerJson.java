package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class AnswerJson {

	public String pub_id;
	public String status;
	public List<PropPojo> props;
	public AnswerPojo reading;
	public AnswerPojo listening;
	public AnswerPojo time_limit;
	public AnswerPojo selecting;
	public AnswerPojo lining;
	public AnswerPojo cloze;
	public AnswerPojo sort;

	public AnswerJson(String pub_id, String status, List<PropPojo> props,
			AnswerPojo reading, AnswerPojo listening, AnswerPojo time_limit,
			AnswerPojo selecting, AnswerPojo lining, AnswerPojo cloze,
			AnswerPojo sort) {
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

}
