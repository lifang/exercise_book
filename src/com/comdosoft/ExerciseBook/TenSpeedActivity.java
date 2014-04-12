package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.StaticLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.pojo.Time_LimitPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

public class TenSpeedActivity extends AnswerBaseActivity implements
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
	private ExerciseBook eb;
	public String path = "";
	private int questions_id;
	private String user_select = "";
	private Gson gson;
	private int branch_item;
	private int status;
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
				Log.i("aaa", "---->" + branch_questions.size() + "==" + index);
				question.setText(branch_questions.get(index).getContent());
				one_btn.setText(branch_questions.get(index).getOpption()[0]);
				two_btn.setText(branch_questions.get(index).getOpption()[1]);
				two_btn.setBackgroundResource(R.drawable.loginbtn_hui);
				one_btn.setBackgroundResource(R.drawable.loginbtn_hui);
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
		findViewById(R.id.base_propTrue).setOnClickListener(this);
		// 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
		setTimePropEnd();// 禁用道具
		setTruePropEnd();// 禁用道具
		setCheckText("下一题");
		this.mQuestionType = 2;
		yinCangCheck();
		eb = (ExerciseBook) getApplication();
		initialize();
		branch_questions = new ArrayList<Time_LimitPojo>();
		gson = new Gson();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		json = intent.getStringExtra("json");
		status = intent.getIntExtra("status", 1);
		Log.i("aaa", eb.getWork_id() + ":");
		SetJson(json);
		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			SetAnswer_Json(json2);
		}
		if (status == 0) {
			img_index = branch_questions.size() - (branch_item + 1);
			index = branch_item + 1;
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
				specified_time = time_limit.getInt("specified_time");
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					JSONObject jo = questions.getJSONObject(0);
					questions_id = jo.getInt("id");
					JSONArray jsonarr = jo.getJSONArray("branch_questions");
					img_index = jsonarr.length();
					for (int i = 0; i < jsonarr.length(); i++) {
						JSONObject item = jsonarr.getJSONObject(i);
						String[] opption = item.getString("options").split(
								";\\|\\|;");
						Time_LimitPojo tl = new Time_LimitPojo(
								item.getInt("id"), item.getString("content"),
								opption, item.getString("answer"));
						branch_questions.add(tl);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(TenSpeedActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 解析json
	private void SetAnswer_Json(String json) {
		if (json != "") {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject time_limit = obj.getJSONObject("time_limit");
				branch_item = time_limit.getInt("branch_item");
				status = time_limit.getInt("status");
				int use_time = time_limit.getInt("use_time");
				setUseTime(use_time);
				setStart();
				Log.i("aaa", branch_item + "/" + branch_questions.size());
			} catch (JSONException e) {
				Toast.makeText(TenSpeedActivity.this, "解析anwserjson发生错误",
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
			if (user_select.equals(branch_questions.get(index).getAnwser())) {
				user_boolean = 100;
				MyPlayer(true);
			} else {
				user_boolean = 0;
				MyPlayer(false);
			}
			img_index -= 1;
			String answer_history = ExerciseBookTool
					.getAnswer_Json_history(path);
			int type = 0;
			try {
				if (status == 0) {
					type = setAnswerJson(answer_history, user_select,
							user_boolean, branch_questions.get(index).getId());
				} else {
					type = Again();
				}
			} catch (Exception e) {
				Toast.makeText(TenSpeedActivity.this, "json写入发生错误",
						Toast.LENGTH_SHORT).show();
			}
			calculateRatio(user_boolean);
			index += 1;
			switch (type) {
			case 0:
				handler.sendEmptyMessage(1);
				break;
			case 1:
				roundOver();
				break;
			}
			user_select = "";

		}
	}

	private int Again() {
		int type = 0;
		if (index + 1 == branch_questions.size()) {// 结束
			type = 1;
		}
		return type;
	}

	public int setAnswerJson(String answer_history, String answer, int ratio,
			int id) {
		int type = 0;
		AnswerJson answerJson = gson.fromJson(answer_history, AnswerJson.class);
		answerJson.time_limit.setUpdate_time(ExerciseBookTool.getTimeIng());
		answerJson.update = ExerciseBookTool.getTimeIng();
		int q_item = Integer.valueOf(answerJson.time_limit.getQuestions_item());
		int b_item = Integer.valueOf(answerJson.time_limit.getBranch_item());

		b_item += 1;
		answerJson.time_limit.setBranch_item(b_item + "");
		answerJson.time_limit.setUse_time(getUseTime() + "");
		if (answerJson.time_limit.getQuestions().size() == 0) {
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(questions_id
					+ "", new ArrayList<Branch_AnswerPoJo>());
			answerJson.time_limit.getQuestions().add(aq);
			q_item += 1;
			answerJson.time_limit.setQuestions_item(q_item + "");
		}

		answerJson.time_limit.getQuestions().get(q_item).getBranch_questions()
				.add(new Branch_AnswerPoJo(id + "", answer, ratio + ""));

		if (b_item + 1 == branch_questions.size()) {
			type = 1;
			answerJson.time_limit.setStatus("1");// 0表示未完成 1表示完成
			String str = gson.toJson(answerJson);
			ExerciseBookTool.writeFile(path, str);
		}

		String str = gson.toJson(answerJson);
		ExerciseBookTool.writeFile(path, str);
		Log.i("linshi", str);
		return type;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			Log.i("aaa", "resultCode:" + resultCode);
			Intent intent = new Intent();
			switch (resultCode) {
			case 0:
				TenSpeedActivity.this.finish();
				intent.setClass(TenSpeedActivity.this,
						HomeWorkIngActivity.class);
				startActivity(intent);
				break;
			case 1:
				index = 0;// 题目索引
				img_index = 10;// 图片索引
				user_boolean = 0;
				status = 1;
				Log.i("suanfa", "--");
				handler.sendEmptyMessage(1);
				break;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}