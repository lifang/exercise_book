package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
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
	private String type = "2";//任务类型     2表示十速挑战

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ten_speed);

		initialize();
		questionlist = new ArrayList<String>();
		questionlist.add("马龙是狗?");
		questionlist.add("马龙是人?");
		questionlist.add("马龙是逗比?");
		// question_number.
	}

	// 初始化
	public void initialize() {
		question = (TextView) findViewById(R.id.question);// 題目
		question_number = (ImageView) findViewById(R.id.question_number);// 數量
		one_btn = (Button) findViewById(R.id.one_btn);
		two_btn = (Button) findViewById(R.id.two_btn);
	}

	public void onclick(View v) {
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

	}
}