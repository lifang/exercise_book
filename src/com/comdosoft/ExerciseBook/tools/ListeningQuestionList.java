package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.List;

import com.comdosoft.ExerciseBook.pojo.ListeningPojo;

public class ListeningQuestionList {
	public static List<ListeningPojo> listeningList = new ArrayList<ListeningPojo>();
	public static List<List<String>> answerList = new ArrayList<List<String>>();
	public static int Record_Count = 0;
	public static int Small_Index = 0;

	public static void addListeningPojo(ListeningPojo lp) {
		listeningList.add(lp);
	}

	public static ListeningPojo getListeningPojo(int index) {
		return listeningList.get(index);
	}

	public static List<ListeningPojo> getListeningPojoList() {
		return listeningList;
	}

	public static void delListeningPojoList(int index) {
		listeningList.remove(index);
	}

	public static void addAnswer(List<String> list) {
		answerList.add(list);
	}

	public static List<List<String>> getAnswerList() {
		return answerList;
	}

	public static int getRecordCount() {
		return answerList.size();
	}
}
