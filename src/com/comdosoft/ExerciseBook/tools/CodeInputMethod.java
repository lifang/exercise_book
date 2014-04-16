package com.comdosoft.ExerciseBook.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.R;

public class CodeInputMethod extends Activity {
	// private MyDialog dialog;
	private EditText edittext;
	private Button queding;
	private ExerciseBook exerciseBook;
	TextView hasnum;// 用来显示剩余字数
	int num = 60;// 限制的最大字数　　

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.inputmethod_code);
		exerciseBook = (ExerciseBook) getApplication();
		edittext = (EditText) findViewById(R.id.edittext1);
		queding = (Button) findViewById(R.id.queding1);
		queding.setOnClickListener(listener);
		Intent intent = getIntent();//
		String hint = intent.getStringExtra("hint");
		if ("".equals(hint) || hint == null) {

		} else {
			edittext.setHint(hint);
		}
		

	}

	
	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String reply_edit = edittext.getText().toString();

			String kongge = reply_edit.replaceAll(" ", "");
			if (reply_edit.length() == 0 || kongge.equals("")) {
				Toast.makeText(getApplicationContext(), R.string.edit_null,
						Toast.LENGTH_SHORT).show();
			} else {
				exerciseBook.setRefresh(0);
				Intent intent2 = new Intent();
				intent2.putExtra("content", reply_edit);
				// 通过调用setResult方法返回结果给前一个activity。
				CodeInputMethod.this.setResult(5, intent2);
				// 关闭当前activity
				CodeInputMethod.this.finish();
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
