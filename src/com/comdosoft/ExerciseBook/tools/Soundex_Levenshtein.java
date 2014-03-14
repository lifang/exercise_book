package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Soundex_Levenshtein {
	private static final String SOUNDEX_DIGITS = "01230120022455012623010202";

	// 听写的算法 s:用户输入文本 tl:正确答案
	public static List<int[]> Engine(String s, List<String> tl) {
		List<String> ml = new ArrayList<String>();
		List<int[]> str = new ArrayList<int[]>();
		String[] arrT = s.split(" ");
		for (int i = 0; i < arrT.length; i++) {
			ml.add(arrT[i].toString());
		}

		for (int i = 0; i < ml.size(); i++) {
			int[] arr = new int[2];
			int k = 0;
			int temp = 0;
			for (int j = 0; j < tl.size(); j++) {
				int value = dragonEngine(ml.get(i), tl.get(j));
				if (value >= temp) {
					temp = value;
					k = j;
				}
			}
			arr[0] = k;
			arr[1] = temp;
			Log.i("suanfa", k + "==>>" + temp);
			str.add(arr);
		}

		return str;
	}

	// 朗读的算法 s:用户输入文本 tl:正确答案
	public static List<int[]> Engine2(String s, List<String> tl) {
		Log.i("suanfa", s + "----");
		List<String> ml = new ArrayList<String>();
		List<int[]> str = new ArrayList<int[]>();
		String[] arrT = s.split(" ");
		for (int i = 0; i < arrT.length; i++) {
			ml.add(arrT[i].toString());
		}
		for (int i = 0; i < ml.size(); i++) {
			int[] arr = new int[2];
			int temp = 0;
			for (int j = 0; j < tl.size(); j++) {
				if (tl.get(j).matches("[\\u4E00-\\u9FA5]+")) {//过滤中文
					temp = 0;
				}
				if (ml.get(i).matches("[0-9]+")) {//正文题目有数字直接算对
					temp = 10;
					break;
				} else {
					if (!tl.get(j).matches("[0-9]+")
							&& !tl.get(j).matches("[\\u4E00-\\u9FA5]+")) {
						int value = dragonEngine(ml.get(i), tl.get(j));
						if (value >= temp) {
							temp = value;
						}
					}
				}
			}
			arr[0] = i;
			arr[1] = temp;
			Log.i("linshi", i + "==>>" + temp);
			str.add(arr);
		}
		return str;
	}

	public static int dragonEngine(String x, String y) {
		String cX = calculateCode(x);
		String cY = calculateCode(y);
		int calu = 0;
		for (int i = 0; i < cX.length(); i++) {
			if (cX.substring(i, i + 1).equals(cY.substring(i, i + 1))) {
				calu++;
			}
		}
		int dist = distance(x, y);
		return 6 - dist + calu;
	}

	public static String calculateCode(String string) {
		String word = string.toUpperCase();
		word = word.replaceAll("[^A-Z]", "");
		if (word.length() == 0) {
			return "";
		}
		char first = word.charAt(0);
		word = first + word.substring(1).replaceAll("[HW]", "");
		StringBuffer sndx = new StringBuffer();
		for (int i = 0; i < word.length(); i++) {
			sndx.append(SOUNDEX_DIGITS.charAt((int) (word.charAt(i) - 'A')));
		}
		word = sndx.toString().replaceAll("(.)\\1+", "$1");
		word = first + word.substring(1);
		word = word.replaceAll("0", "");
		return (word + "000").substring(0, 4);
	}

	public static int distance(String x, String y) {

		int m = x.length();
		int n = y.length();

		int[][] T = new int[m + 1][n + 1];

		T[0][0] = 0;
		for (int j = 0; j < n; j++) {
			T[0][j + 1] = T[0][j] + ins(y, j);
		}
		for (int i = 0; i < m; i++) {
			T[i + 1][0] = T[i][0] + del(x, i);
			for (int j = 0; j < n; j++) {
				T[i + 1][j + 1] = min(T[i][j] + sub(x, i, y, j), T[i][j + 1]
						+ del(x, i), T[i + 1][j] + ins(y, j));
			}
		}

		return T[m][n];
	}

	private static int sub(String x, int xi, String y, int yi) {
		return x.charAt(xi) == y.charAt(yi) ? 0 : 1;
	}

	private static int ins(String x, int xi) {
		return 1;
	}

	private static int del(String x, int xi) {
		return 1;
	}

	private static int min(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
}