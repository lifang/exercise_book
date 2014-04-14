package com.comdosoft.ExerciseBook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

/**
 * @作者 丁作强
 * @时间 2014-4-12 上午9:35:32
 */
public class InputKeyActivity extends Activity implements Urlinterface {
	private EditText inputKey;//  激活码
	private Button   queren;  //    确认 
	private Button   nextStep;  //    跳过
	private ProgressDialog prodialog;
	private ExerciseBook exerciseBook;
	private String open_id = "asfds"; // QQ 的 open id

	/* 请求码 */

	private String json = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_intputkey);
		exerciseBook = (ExerciseBook) getApplication();

		Intent intent = getIntent();//
		open_id = intent.getStringExtra("open_id"); // 获得上个页面传过来的 QQ openid

		inputKey = (EditText) findViewById(R.id.reg_inputkey);
		queren = (Button) findViewById(R.id.inputkey_queren);
		nextStep = (Button) findViewById(R.id.inputkey_nextstep);
		queren.setOnClickListener(listener);
		nextStep.setOnClickListener(listener2);
	}
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			String inputKeyStr = inputKey.getText().toString();
			String inputKeyStr2 = inputKeyStr.replaceAll(" ", "");
			
			if (inputKeyStr.length() == 0|| inputKeyStr2.equals("")) {
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.MES_ISNULL, Toast.LENGTH_SHORT).show();
				

			} else {
				Intent it = new Intent(InputKeyActivity.this,
						InputClassCodeActivity.class);
				it.putExtra("key", inputKeyStr);
				it.putExtra("open_id", open_id);
				startActivity(it);
				InputKeyActivity.this.finish();
			}

		}
	};
	private View.OnClickListener listener2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.putExtra("open_id", open_id);
			intentp.setClass(InputKeyActivity.this, RegistrationActivity.class);//
			startActivity(intentp);
			InputKeyActivity.this.finish();
		}
	};

	
	

	public String inStream2String(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return new String(baos.toByteArray());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
