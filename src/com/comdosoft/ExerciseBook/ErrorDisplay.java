package com.comdosoft.ExerciseBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class ErrorDisplay extends Activity implements Urlinterface {

	private String notice = ""; //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.error_display);

		Intent intent = getIntent();//
		notice = intent.getStringExtra("notice");
		TextView error = (TextView) findViewById(R.id.error);
		error.setText(notice);

	}
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	// 班级验证码错误 时    fanhui 触发的方法
	public String button_fanhui(View v) throws Exception {
		ErrorDisplay.this.finish();
		return null;
	}
}
