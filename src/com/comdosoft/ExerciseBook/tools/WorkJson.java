package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.comdosoft.ExerciseBook.pojo.PropNumberPojo;
import com.comdosoft.ExerciseBook.pojo.PropPojo;
import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.google.gson.JsonObject;

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
				startstr = ExerciseBookTool.divisionTime(startstr);
				String endstr = item.getString("end_time");
				endstr = ExerciseBookTool.divisionTime(endstr);
				int number;
				if (obj.get("knowledges_cards_count").equals(JSONObject.NULL)) {
					Log.i("aaa", 1 + "");
					number = 0;
				} else {
					Log.i("aaa", 2 + "");
					number = obj.getInt("knowledges_cards_count");
				}
				WorkPoJo work = new WorkPoJo(item.getInt("id"),
						item.getString("name"), startstr, endstr,
						item.getString("question_packages_url"),
						questiontype_list, finish_list, number);
				work_list.add(work);
				Log.i("linshi", questiontype_list.size() + "<-");
			}
		}

		return work_list;
	}

	public static List<PropNumberPojo> getProp(String json) {
		List<PropNumberPojo> prop_list = new ArrayList<PropNumberPojo>();
		JSONObject obj;
		try {
			obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("props");
			if (arr.length() != 0) {
				for (int j = 0; j < arr.length(); j++) {
					JSONObject item = arr.getJSONObject(j);
					PropNumberPojo prop = new PropNumberPojo(item.getInt("types"), item.getInt("number"));
					prop_list.add(prop);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return prop_list;
	}
}
