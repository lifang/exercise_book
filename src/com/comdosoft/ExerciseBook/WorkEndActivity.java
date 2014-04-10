package com.comdosoft.ExerciseBook;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

public class WorkEndActivity extends Activity implements Urlinterface {

	private ExerciseBook eb;
	private int precision;
	private int use_time;
	private int specified_time;
	private TextView work_ratio;
	private TextView work_time;
	private TextView work_jz;
	private TextView work_xs;
	private TextView work_jiezu;
	private LinearLayout cj;
	private int status;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.work_end);
		eb = (ExerciseBook) getApplication();

		Intent intent = getIntent();
		precision = intent.getIntExtra("precision", 0);
		use_time = intent.getIntExtra("use_time", 0);
		status = intent.getIntExtra("status", 0);
		specified_time = intent.getIntExtra("specified_time", 0);
		Log.i("suanfa", "specified_time:" + specified_time);
		android.view.WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		if (status == 0) {
			p.height = 850; // 高度设置为屏幕的
			p.width = 740; // 宽度设置为屏幕的
		} else {
			p.height = 450; // 高度设置为屏幕的
			p.width = 740; // 宽度设置为屏幕的
		}
		getWindow().setAttributes(p);
		initialize();
	}

	// 初始化
	public void initialize() {
		Calendar Window_day = Calendar.getInstance();
		cj = (LinearLayout) findViewById(R.id.cj);
		work_ratio = (TextView) findViewById(R.id.work_ratio);
		work_ratio.setText(precision + "%");
		work_time = (TextView) findViewById(R.id.work_time);
		work_time.setText(use_time + "");
		if (status == 0) {
			work_jz = (TextView) findViewById(R.id.work_jz);
			if (precision == 100) {
				work_jz.setText(R.string.work_end_str5);
			} else {
				work_jz.setText(R.string.work_end_str4);
			}
			work_xs = (TextView) findViewById(R.id.work_xs);
			if (use_time <= specified_time) {
				work_xs.setText(R.string.work_end_str7);
			} else {
				work_xs.setText(R.string.work_end_str6);
			}
			if (precision < 60) {
				work_xs.setText(R.string.work_end_str12);
			}
			work_jiezu = (TextView) findViewById(R.id.work_jiezu);
			String end_time = eb.getWork_end_dath();
			Window_day.add(Calendar.HOUR, 2); // 目前時間加2小時
			// a比c小,返回-1,
			// a与c相同,返回0
			// a比c大,返回1
			int zhi = Window_day.compareTo(ExerciseBookTool
					.getCalender_time(end_time));
			if (zhi == 1 && precision >= 60) {
				work_jiezu.setText(R.string.work_end_str8);
			} else if (zhi != 1 && precision < 60) {
				work_jiezu.setText(R.string.work_end_str13);
			} else {
				work_jiezu.setText(R.string.work_end_str9);
			}
		} else {
			cj.setVisibility(View.GONE);
		}
	}

	public void onclick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ok:
			WorkEndActivity.this.setResult(0);
			WorkEndActivity.this.finish();
			break;
		case R.id.letsgo:
			WorkEndActivity.this.setResult(1);
			WorkEndActivity.this.finish();
			break;
		}
	}
}