package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.List;

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
				startstr = ExerciseBookTool.divisionTime(startstr);
				String endstr = item.getString("end_time");
				endstr = ExerciseBookTool.divisionTime(endstr);
				WorkPoJo work = new WorkPoJo(item.getInt("id"),
						item.getString("name"), startstr, endstr,
						item.getString("question_packages_url"),
						questiontype_list, finish_list);
				work_list.add(work);
				Log.i("linshi", questiontype_list.size() + "<-");
			}
		}
		return work_list;
	}
}
