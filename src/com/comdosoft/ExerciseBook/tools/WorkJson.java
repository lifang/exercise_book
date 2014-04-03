package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.comdosoft.ExerciseBook.pojo.WorkPoJo;

public class WorkJson {

	public static List<WorkPoJo> json(String json) throws Exception {
		List<WorkPoJo> work_list = new ArrayList<WorkPoJo>();
		JSONObject obj = new JSONObject(json);
		JSONArray arr = obj.getJSONArray("tasks");
		if (arr.length() != 0) {
			for (int i = 0; i < arr.length(); i++) {
				List<Integer> questiontype_list = new ArrayList<Integer>();
				List<Integer> finish_list = new ArrayList<Integer>();
				JSONObject item = arr.getJSONObject(i);
				JSONArray question_arr = item.getJSONArray("question_types");
				if (question_arr.length() != 0) {
					for (int j = 0; j < question_arr.length(); j++) {
						questiontype_list.add(question_arr.getInt(j));
					}
				}
				JSONArray finish_arr = item.getJSONArray("finish_types");
				if (finish_arr.length() != 0) {
					for (int j = 0; j < finish_arr.length(); j++) {
						finish_list.add(finish_arr.getInt(j));
					}
				}
				String startstr = item.getString("start_time");
				// startstr = ExerciseBookTool.divisionTime(startstr);
				String endstr = item.getString("end_time");
				// endstr = ExerciseBookTool.divisionTime(endstr);
				int number;
				if (obj.get("knowledges_cards_count").equals(JSONObject.NULL)) {
					number = 0;
				} else {
					number = obj.getInt("knowledges_cards_count");
				}
				Log.i("suanfa", 1 + "");
				String updated_at;
				if (item.get("updated_at").equals(JSONObject.NULL)) {
					updated_at = "null";
				} else {
					updated_at = item.getString("updated_at");
				}
				Log.i("suanfa", 2 + "");
				String answer_url;
				if (item.get("answer_url").equals(JSONObject.NULL)) {
					Log.i("suanfa", 2 + "-1");
					answer_url = "null";
				} else {
					Log.i("suanfa", 2 + "-2");
					answer_url = item.getString("answer_url");
				}
				Log.i("suanfa", 3 + "");
				WorkPoJo work = new WorkPoJo(item.getInt("id"),
						item.getString("name"), startstr, endstr,
						item.getString("question_packages_url"), updated_at,
						answer_url, questiontype_list, finish_list, number);
				work_list.add(work);
				Log.i("linshi", questiontype_list.size() + "<-");
			}
		}
		return work_list;
	}

	public static Map<Integer, Integer> getProp(String json) {
		Map<Integer, Integer> number = new HashMap<Integer, Integer>();
		number.put(0, 0);
		number.put(1, 0);
		JSONObject obj;
		try {
			obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("props");
			if (arr.length() != 0) {
				for (int j = 0; j < arr.length(); j++) {
					JSONObject item = arr.getJSONObject(j);
					number.put(item.getInt("types"), item.getInt("number"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return number;
	}
}