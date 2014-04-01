package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.List;

public class AnswerTools {
	// 获取题目类型
	public static int getSelectType(String s) {
		int index = s.indexOf("</file>");
		if (index != -1) {
			String resource = s.substring(index - 3, index);
			if (resource.equals("jpg") || resource.equals("png")) {
				return 2;
			} else if (resource.equals("wav") || resource.equals("mp3")
					|| resource.equals("amr")) {
				return 0;
			}
		} else {
			return 1;
		}
		return 1;
	}

	// 获取题目内容
	public static String getSelectContent(String s) {
		int index = s.indexOf("</file>");
		if (index != -1) {
			return s.subSequence(index + 7, s.length()).toString();
		} else {
			return s;
		}
	}

	// 获取题目资源路径
	public static String getSelectPath(String s) {
		int index = s.indexOf("</file>");
		if (index != -1) {
			int startIndex = s.indexOf("<file>");
			return s.subSequence(startIndex + 6, index).toString();
		}
		return null;
	}

	// 获取文件名称
	public static String getSelectFileName(String s) {
		if (s != null && !s.equals("")) {
			String[] arr = s.split("/");
			return arr[arr.length - 1];
		}
		return null;
	}

	// 获取题目选项
	public static List<String> getSelectOption(String s) {
		List<String> arr = new ArrayList<String>();
		String[] sArr = s.split(";\\|\\|;");
		for (int i = 0; i < sArr.length; i++) {
			arr.add(sArr[i]);
		}
		return arr;
	}

	// 获取答案
	public static List<String> getSelectAnswer(String s) {
		List<String> arr = new ArrayList<String>();
		String[] sArr = s.split(";\\|\\|;");
		for (int i = 0; i < sArr.length; i++) {
			arr.add(sArr[i]);
		}
		return arr;
	}
}
