package com.comdosoft.ExerciseBook;

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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.Time_LimitPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class TenSpeedActivity extends Activity implements Urlinterface {

	private TextView question;
	private ImageView question_number;
	private Button one_btn;
	private Button two_btn;
	private List<Time_LimitPojo> branch_questions;
	private List<Boolean> user_boolean;
	private String type = "2";// 任务类型 2表示十速挑战
	private int index = 0;// 题目索引
	private int img_index = 1;// 图片索引
	private int time = 0;// 做题时间
	private String notice;
	private Timer timer;// 计时器
	private ExerciseBook eb;
	public String path = "";
	private int specified_time;
	private int questions_id;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				time += 1;
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
		setContentView(R.layout.ten_speed);
		eb = (ExerciseBook) getApplication();
		initialize();
		branch_questions = new ArrayList<Time_LimitPojo>();
		user_boolean = new ArrayList<Boolean>();

		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		String json = intent.getStringExtra("json");

		SetJson(json);
	}

	// 初始化
	public void initialize() {
		question = (TextView) findViewById(R.id.question);// 題目
		question_number = (ImageView) findViewById(R.id.question_number);// 數量
		one_btn = (Button) findViewById(R.id.one_btn);
		two_btn = (Button) findViewById(R.id.two_btn);
		handler.sendEmptyMessage(1);

		timer = new Timer(true);
		timer.schedule(task, 1000, 1000);
	}

	private void SetJson(String json) {
		Log.i("aaa", "--" + json);
		if (json != "") {
			try {
				JSONObject time_limit = new JSONObject(json);
				specified_time = time_limit.getInt("specified_time");
				Log.i("aaa", specified_time + "--");
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					JSONObject jo = questions.getJSONObject(0);
					questions_id = jo.getInt("id");
					Log.i("aaa", questions_id + "--");
					JSONArray jsonarr = jo.getJSONArray("branch_questions");
					img_index = jsonarr.length();
					for (int i = 0; i < jsonarr.length(); i++) {
						JSONObject item = jsonarr.getJSONObject(i);
						String[] opption = item.getString("opption").split(
								";\\|\\|;");
						Time_LimitPojo tl = new Time_LimitPojo(
								item.getInt("id"), item.getString("content"),
								opption, item.getString("anwser"));
						Log.i("aaa", tl.toString());
						branch_questions.add(tl);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(TenSpeedActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
			handler.sendEmptyMessage(1);
		}
	}

	TimerTask task = new TimerTask() {
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};

	public void onclick(View v) {
		if (img_index > 0) {
			String user_select = "";
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
				user_boolean.add(true);
				Log.i("aaa", "正确");
			} else {
				user_boolean.add(false);
				Log.i("aaa", "错误");
			}
			img_index -= 1;
			index += 1;
			if (img_index <= 0) {
				Log.i("aaa", time + "");
				timer.cancel();
				Intent intent = new Intent(TenSpeedActivity.this,
						WorkEndActivity.class);
				startActivity(intent);
			} else {
				handler.sendEmptyMessage(1);
			}
		} 
	}
}