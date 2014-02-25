package com.comdosoft.ExerciseBook;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class HomePageSendMessage extends Activity{
	private ProgressDialog prodialog;
	private EditText sendmesset;
	private Button sendmess_btn;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_sendmessage);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		sendmess_btn=(Button) findViewById(R.id.sendmess_btn);
		sendmesset=(EditText) findViewById(R.id.sendmesset);
		sendmess_btn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				// TODO Auto-generated method stub
				class_fabiao();
			}
			
		});
	}
	public void class_fabiao() {
		final String fabiaoContents = sendmesset.getText().toString();
		String kongge = fabiaoContents.replaceAll(" ", "");
		if (fabiaoContents.length() == 0 || kongge.equals("")) {
			Toast.makeText(getApplicationContext(), "内容不能为空",
					Toast.LENGTH_SHORT).show();
		} else {
			Thread thread = new Thread() {
				public void run() {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", fabiaoContents);
						map.put("user_id", "1");
						map.put("user_types", "1");
						map.put("school_class_id", "66");
						String senderjson = ExerciseBookTool.doPost(
								Urlinterface.NEWS_RELEASE, map);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = senderjson;
						class_fabiaoHandler.sendMessage(msg);
					} catch (Exception e) {
					}
				}
			};
			if (ExerciseBookTool.isConnect(HomePageSendMessage.this)) {
				prodialog = new ProgressDialog(HomePageSendMessage.this);
				prodialog.setMessage("正在发表...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
//				Toast.makeText(getApplicationContext(),
//						HomeWorkParams.INTERNET, 0).show();
			}
		}
	}
	 Handler class_fabiaoHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			prodialog.dismiss();
			switch (msg.what) {
			case 0:
				final String json1 = (String) msg.obj;
				if (json1.length() != 0) {
					JSONObject array1;
					try {
						array1 = new JSONObject(json1);//
						String status = array1.getString("status");
						String notice = array1.getString("notice");
						if ("success".equals(status)) {
							HomePageMainActivity.instance.tabhost.setCurrentTab(0);
						} else {
							Toast.makeText(getApplicationContext(), notice,
									Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}
	};
}
