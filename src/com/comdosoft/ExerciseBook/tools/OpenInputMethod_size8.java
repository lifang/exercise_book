package com.comdosoft.ExerciseBook.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.R;

public class OpenInputMethod_size8 extends Activity {
	// private MyDialog dialog;
	private EditText edittext;
	private Button queding;
	private ExerciseBook exerciseBook;
	TextView hasnum;// 用来显示剩余字数
	int num = 8;// 限制的最大字数　　

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.inputmethod);
		exerciseBook = (ExerciseBook) getApplication();
		exerciseBook.getActivityList().add(this);
		edittext = (EditText) findViewById(R.id.edittext);
		queding = (Button) findViewById(R.id.queding);
		queding.setOnClickListener(listener);
		hasnum = (TextView) findViewById(R.id.tv_num);
		hasnum.setText(num + "");
		hasnum.setText(" 剩余 " + num+" 字" );
		edittext.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			public void afterTextChanged(Editable s) {
				int number = num - s.length();
				hasnum.setText(" 剩余 " + number+" 字" );
				selectionStart = edittext.getSelectionStart();
				selectionEnd = edittext.getSelectionEnd();
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					edittext.setText(s);
					edittext.setSelection(tempSelection);//设置光标在最后
				}
			}
		});
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
				OpenInputMethod_size8.this.setResult(8, intent2);
				// 关闭当前activity
				OpenInputMethod_size8.this.finish();

			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
