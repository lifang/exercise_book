package com.comdosoft.ExerciseBook;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;

import cn.jpush.android.api.JPushInterface;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

/**
 * @作者 丁作强
 * @时间 2014-4-23 上午9:44:06
 */
public class Appstart extends Activity {
	public ExerciseBook eb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.appstart);
		eb = (ExerciseBook) getApplication();
		eb.setMenu_num(1);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(Appstart.this,
						HomeWorkIngActivity.class);
				startActivity(intent);
				Appstart.this.finish();
			}
		}, 3000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
}