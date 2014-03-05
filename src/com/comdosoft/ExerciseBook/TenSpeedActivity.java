package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class TenSpeedActivity extends Activity implements Urlinterface {

	private TextView question;
	private ImageView question_number;
	private Button one_btn;
	private Button two_btn;
	private List<String> questionlist;
	private List<Boolean> boolean_list;
	private List<Boolean> user_boolean;
	private String type = "2";// 任务类型 2表示十速挑战
	private int index = 0;// 题目索引
	private int img_index = 10;// 图片索引
	private int time = 0;// 做题时间
	private String notice;
	private Timer timer;// 计时器
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

				question.setText(questionlist.get(index));

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

		initialize();
		questionlist = new ArrayList<String>();
		boolean_list = new ArrayList<Boolean>();
		user_boolean = new ArrayList<Boolean>();
		questionlist.add("马龙是狗?");
		questionlist.add("马龙是人?");
		questionlist.add("马龙是逗比?");
		questionlist.add("马龙是狗?");
		questionlist.add("马龙是人?");
		questionlist.add("马龙是逗比?");
		questionlist.add("马龙是狗?");
		questionlist.add("马龙是人?");
		questionlist.add("马龙是逗比?");
		questionlist.add("马龙是狗?");
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
		boolean_list.add(true);
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

	TimerTask task = new TimerTask() {
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};

	public void onclick(View v) {
		if (img_index > 1) {
			boolean tf = false;
			switch (v.getId()) {
			case R.id.one_btn:
				one_btn.setBackgroundResource(R.drawable.loginbtn_lv);
				two_btn.setBackgroundResource(R.drawable.loginbtn_hui);
				tf = true;
				break;
			case R.id.two_btn:
				two_btn.setBackgroundResource(R.drawable.loginbtn_lv);
				one_btn.setBackgroundResource(R.drawable.loginbtn_hui);
				tf = false;
				break;
			}
			if (tf == boolean_list.get(index)) {
				user_boolean.add(true);
			} else {
				user_boolean.add(false);
			}
			img_index -= 1;
			index += 1;
			handler.sendEmptyMessage(1);
		}else{
			Log.i("aaa", time+"");
			timer.cancel();
		}
	}
}