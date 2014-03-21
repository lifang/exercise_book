package com.comdosoft.ExerciseBook.pojo;

import java.util.List;

public class AnswerBasePojo {
	private int questions_id;
	private int branch_questions_id;
	private int type;
	private String content;
	private String path;
	private List<String> option;
	private List<String> answer;

	public AnswerBasePojo() {
	}

	public AnswerBasePojo(int questions_id, int branch_questions_id,
			String content) {
		super();
		this.questions_id = questions_id;
		this.branch_questions_id = branch_questions_id;
		this.content = content;
	}
	
	public AnswerBasePojo(int questions_id, int branch_questions_id,
			String content, String path) {
		super();
		this.questions_id = questions_id;
		this.branch_questions_id = branch_questions_id;
		this.content = content;
		this.path = path;
	}

	public AnswerBasePojo(int questions_id, int branch_questions_id, int type,
			String content, List<String> option, List<String> answer) {
		super();
		this.questions_id = questions_id;
		this.branch_questions_id = branch_questions_id;
		this.type = type;
		this.content = content;
		this.option = option;
		this.answer = answer;
	}

	public AnswerBasePojo(int questions_id, int branch_questions_id, int type,
			String content, String path, List<String> option,
			List<String> answer) {
		super();
		this.questions_id = questions_id;
		this.branch_questions_id = branch_questions_id;
		this.type = type;
		this.content = content;
		this.path = path;
		this.option = option;
		this.answer = answer;
	}

	public int getQuestions_id() {
		return questions_id;
	}

	public void setQuestions_id(int questions_id) {
		this.questions_id = questions_id;
	}

	public int getBranch_questions_id() {
		return branch_questions_id;
	}

	public void setBranch_questions_id(int branch_questions_id) {
		this.branch_questions_id = branch_questions_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getOption() {
		return option;
	}

	public void setOption(List<String> option) {
		this.option = option;
	}

	public List<String> getAnswer() {
		return answer;
	}

	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}

}
