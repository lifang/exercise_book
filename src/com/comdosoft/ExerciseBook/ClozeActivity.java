package com.comdosoft.ExerciseBook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.Branch_PoJo;
import com.comdosoft.ExerciseBook.pojo.ClozePojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.PredicateLayout;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class ClozeActivity extends Activity implements Urlinterface {

	private ExerciseBook eb;
	public String path = "";
	public String sdpath = Environment.getExternalStorageDirectory() + "/";
	private Timer timer;
	private String content = "";
	private String[] DA = new String[] { "apple", "an", "a" };
	private PredicateLayout myLayout;
	private List<PopupWindow> popup_list;
	private String[] str;
	private int specified_time;
	private List<Branch_PoJo> Branchlist;
	private List<ClozePojo> list;
	private int index = 0;
	private int questions_item;
	private int branch_item;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				content = list.get(questions_item).getContent();
				setTextView();
				break;
			case 1:
				break;
			case 2:
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloze);
		eb = (ExerciseBook) getApplication();
		initialize();

		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		String json = intent.getStringExtra("json");
		SetJson(json);

		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			SetAnswer_Json(json2);
			if (questions_item == -1) {// -1表示已经做完所有的题目
				Log.i("suanfa", "全部题目已完成");

			} else {
				Log.i("suanfa", "继续做题");
				handler.sendEmptyMessage(0);
			}
		} else {
			questions_item = 0;
			branch_item = 0;
			handler.sendEmptyMessage(0);
		}

	}

	// 初始化
	public void initialize() {
		myLayout = (PredicateLayout) findViewById(R.id.myLayout);

		timer = new Timer(true);
		timer.schedule(task, 1000, 1000);
	}

	private void setTextView() {
		str = content.split("\\[\\[tag\\]\\]");
		Log.i("aaa", str.length + "=");
		popup_list = new ArrayList<PopupWindow>();
		for (int i = 0; i < str.length; i++) {
			View view1 = View.inflate(this, R.layout.cloze_view, null);
			TextView text = (TextView) view1.findViewById(R.id.tv);
			text.setText(str[i].toString());
			Spinner spinner = (Spinner) view1.findViewById(R.id.spinner);
			final List<String> str_list = getlist(DA);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.spinnerview, str_list) {
				public View getDropDownView(int position, View convertView,
						ViewGroup parent) {
					View view = getLayoutInflater().inflate(
							R.layout.my_spinner, parent, false);
					TextView label = (TextView) view.findViewById(R.id.label);
					label.setText(str_list.get(position));
					// ImageView iv = (ImageView) view.findViewById(R.id.iv);
					if (position == 0) {
						label.setVisibility(View.GONE);
					} else {
						label.setBackgroundColor(getResources().getColor(
								R.color.spinner_color));
						// iv.setVisibility(View.GONE);
					}
					// 这里ViewGroup parent就是设置整个下拉列表框的
					parent.setFadingEdgeLength(0); // 设置模糊边界
					// parent.setOverScrollMode(View.OVER_SCROLL_NEVER);// 滚动的
					// parent.setBackgroundResource(R.drawable.sprin);// 背景
					return view;
				}
			};
			// adapter.setDropDownViewResource(R.layout.my_spinner);
			spinner.setAdapter(adapter);
			myLayout.addView(view1);
		}
	}

	// 获取历史索引
	private void SetAnswer_Json(String json) {
		if (json != "") {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject time_limit = obj.getJSONObject("reading");
				questions_item = time_limit.getInt("questions_item");
				branch_item = time_limit.getInt("branch_item");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void SetJson(String json) {
		list = new ArrayList<ClozePojo>();
		if (json != "") {
			try {
				JSONObject time_limit = new JSONObject(json);
				specified_time = time_limit.getInt("specified_time");
				Log.i("aaa", specified_time + "--");
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					for (int i = 0; i < questions.length(); i++) {
						JSONObject jo = questions.getJSONObject(i);
						JSONArray jsonarr = jo.getJSONArray("branch_questions");
						Branchlist = new ArrayList<Branch_PoJo>();
						for (int j = 0; j < jsonarr.length(); j++) {
							JSONObject item = jsonarr.getJSONObject(j);
							Branch_PoJo tl = new Branch_PoJo(item.getInt("id"),
									item.getString("opption"),
									item.getString("answer"));
							Branchlist.add(tl);
						}

						ClozePojo lp = new ClozePojo(jo.getInt("id"),
								jo.getString("content"), Branchlist);
						Log.i("suanfa", "id--" + jo.getInt("id"));
						list.add(lp);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(ClozeActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	TimerTask task = new TimerTask() {
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};

	private List<String> getlist(String[] str) {
		List<String> strlist = new ArrayList<String>();
		strlist.add("");
		for (int i = 0; i < str.length; i++) {
			strlist.add(str[i]);
		}
		return strlist;
	}

	public void onclick(View v) {
	}
}