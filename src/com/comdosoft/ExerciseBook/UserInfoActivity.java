package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.Child_Micropost;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class UserInfoActivity extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;
	private ImageView imageView1;
	private ImageView imageView2;
	private TextView userinfo_username;
	private TextView userinfo_classname;
	public static UserInfoActivity instance = null;
	private String username = "丁作强";
	private String classname = "三年级二班";
	private String id;
	private ProgressDialog prodialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.userinfo);
		instance = this;
		// SharedPreferences preferences =
		// getSharedPreferences(Urlinterface.SHARED,
		// Context.MODE_PRIVATE);
		// classname= preferences.getString("school_class_name", "");
		// username = preferences.getString("name", "");
		// id = preferences.getString("id", null);

		userinfo_username = (TextView) findViewById(R.id.userinfo_username); // 用户名
		userinfo_classname = (TextView) findViewById(R.id.userinfo_classname); // 班级名
		userinfo_username.setText(username);
		userinfo_classname.setText(classname);
		imageView1 = (ImageView) findViewById(R.id.imageView1);// 修改用户名图标
		imageView1.setOnClickListener(listener);
		imageView2 = (ImageView) findViewById(R.id.imageView2);// 切换班级图标
		imageView2.setOnClickListener(listener2);
		layout = (LinearLayout) findViewById(R.id.main_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "点击别的地方消失",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.setClass(
					UserInfoActivity.this,
					com.comdosoft.ExerciseBook.tools.OpenInputMethod_size8.class);//
			startActivityForResult(intentp, 0);
		}
	};
	private View.OnClickListener listener2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.setClass(UserInfoActivity.this, SwitchClassActivity.class);//
			startActivity(intentp);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 8:
			// Bundle bundle = data.getExtras();
			// String content = bundle.getString("content");
			// modifyName(content);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	// 修改 name
	public void modifyName(final String content) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					prodialog.dismiss();
					final String json2 = (String) msg.obj;
					if (json2.length() == 0) {
					} else {
						JSONObject array2;
						try {
							array2 = new JSONObject(json2);//
							String status = array2.getString("status");
							String notice = array2.getString("notice");
							if ("success".equals(status)) {
								userinfo_username.setText(content);
								HomePageMainActivity.instance.userName.setText(content);
								
							}
							Toast.makeText(getApplicationContext(), notice,
									Toast.LENGTH_SHORT).show();

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

		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("content", content);
					map.put("student_id", id);

					// String js1 = ExerciseBookTool.doPost(
					// Urlinterface.modifyUserName, map);
					// Message msg = new Message();// 创建Message 对象
					// msg.what = 0;
					// msg.obj = js1;
					// mHandler.sendMessage(msg);
				} catch (Exception e) {
					// handler.sendEmptyMessage(7);
				}
			}
		};
		if (ExerciseBookTool.isConnect(UserInfoActivity.this)) {
			prodialog = new ProgressDialog(UserInfoActivity.this);
			prodialog.setMessage("正在修改...");
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
			thread.start();
		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, 0).show();
		}
	}
}
