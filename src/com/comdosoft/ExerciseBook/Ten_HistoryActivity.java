package com.comdosoft.ExerciseBook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.Time_LimitPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

public class Ten_HistoryActivity extends AnswerBaseActivity implements
		Urlinterface, OnClickListener {

	private String json;
	private TextView question;
	private ImageView question_number;
	private Button one_btn;
	private Button two_btn;
	private List<Time_LimitPojo> branch_questions;
	private int index = 0;// 题目索引
	private int img_index = 1;// 图片索引
	private int user_boolean = 0;
	public String path = "";
	private String user_select = "";
	private Gson gson;
	private AnswerPojo answer;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				int imgid = 0;
				question_number.setBackgroundResource(0);
				try {// 利用java反射动态设置图片
					imgid = (Integer) R.drawable.class.getField(
							"ten_" + img_index).get(null);
				} catch (Exception e) {
					imgid = 0;
				}
				question_number.setBackgroundResource(imgid);
				question.setText(branch_questions.get(index).getContent());
				one_btn.setText(branch_questions.get(index).getOpption()[0]);
				two_btn.setText(branch_questions.get(index).getOpption()[1]);
				if (branch_questions.get(index).getOpption()[0]
						.equals(branch_questions.get(index).getAnwser())) {
					one_btn.setBackgroundResource(R.drawable.loginbtn_lv);
					two_btn.setBackgroundResource(R.drawable.loginbtn_hui);
				} else {
					one_btn.setBackgroundResource(R.drawable.loginbtn_hui);
					two_btn.setBackgroundResource(R.drawable.loginbtn_lv);
				}
				break;
			case 2:
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.ten_speed);
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		setTimePropEnd();// 禁用道具
		setTruePropEnd();// 禁用道具
		setCheckText("下一个");
		initialize();
		branch_questions = new ArrayList<Time_LimitPojo>();
		gson = new Gson();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		json = intent.getStringExtra("json");
		SetJson(json);
		SetAnswer();
	}

	private void SetAnswer() {
		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			answer = ExerciseBookTool.getAnswer(json2, "cloze");
		}
		handler.sendEmptyMessage(1);
	}

	// 初始化
	public void initialize() {
		question = (TextView) findViewById(R.id.question);// 題目
		question_number = (ImageView) findViewById(R.id.question_number);// 數量
		one_btn = (Button) findViewById(R.id.one_btn);
		two_btn = (Button) findViewById(R.id.two_btn);

	}

	private void SetJson(String json) {
		Log.i("aaa", "--" + json);
		if (json != "") {
			try {
				JSONObject time_limit = new JSONObject(json);
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					JSONObject jo = questions.getJSONObject(0);
					JSONArray jsonarr = jo.getJSONArray("branch_questions");
					img_index = jsonarr.length();
					for (int i = 0; i < jsonarr.length(); i++) {
						JSONObject item = jsonarr.getJSONObject(i);
						String[] opption = item.getString("opption").split(
								";\\|\\|;");
						Time_LimitPojo tl = new Time_LimitPojo(
								item.getInt("id"), item.getString("content"),
								opption, item.getString("anwser"));
						branch_questions.add(tl);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(Ten_HistoryActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	TimerTask task = new TimerTask() {
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};

	public void btnonclick(View v) {
		if (img_index > 0) {
			switch (v.getId()) {
			case R.id.one_btn:
				one_btn.setBackgroundResource(R.drawable.loginbtn_lv);
				two_btn.setBackgroundResource(R.drawable.loginbtn_hui);
				user_select = branch_questions.get(index).getOpption()[0];
				break;
			case R.id.two_btn:
				two_btn.setBackgroundResource(R.drawable.loginbtn_lv);
				one_btn.setBackgroundResource(R.drawable.loginbtn_hui);
				user_select = branch_questions.get(index).getOpption()[1];
				break;
			}
		}
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			if (index + 1 < branch_questions.size()) {
				index += 1;
				img_index -= 1;
				handler.sendEmptyMessage(1);
			} else {
				MyDialog("没有更多历史记录了,点击确定退出!", 1);
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Ten_HistoryActivity.this.finish();
			Intent intent = new Intent();
			intent.setClass(Ten_HistoryActivity.this, HomeWorkIngActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}