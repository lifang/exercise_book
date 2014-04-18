package com.comdosoft.ExerciseBook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginActivity extends Activity implements OnClickListener,
		Urlinterface {

	private static final String SCOPE = "get_user_info";
	private static final String APP_ID = "101003848";
	private String mes;
	private String json;
	private String openid;
	private Tencent mTencent;
	private SharedPreferences sp;
	private ProgressDialog mPd;
	private ExerciseBook eb;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mPd.dismiss();
			Toast.makeText(getApplicationContext(), mes, 0).show();
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				intent.putExtra("open_id", openid);
				intent.setClass(getApplicationContext(), InputKeyActivity.class);
				break;
			case 2:
				intent.setClass(getApplicationContext(), Appstart.class);
				break;
			}
			LoginActivity.this.finish();
			startActivity(intent);
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findViewById(R.id.qq_login).setOnClickListener(this);
		eb = (ExerciseBook) getApplication();
		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		mPd = new ProgressDialog(LoginActivity.this);
		mPd.setMessage("正在登录...");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		sp = getSharedPreferences(SHARED, MODE_PRIVATE);
		if (!sp.getString("user_id", "").equals("")
				&& !sp.getString("school_class_id", "").equals("")) {
			String validtime = sp.getString("validtime", "");
			Boolean vt = ExerciseBookTool.Comparison_Time(str, validtime);
			if (vt) { // 当前时间小于 有效期
				LoginActivity.this.finish();
				Intent intent = new Intent(getApplicationContext(),
						Appstart.class);
				startActivity(intent);
			}

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
	}

	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener();
			mTencent.login(this, SCOPE, listener);
		} else {
			mTencent.logout(this);
		}
	}

	public void analyzeJson(String json) {
		try {
			JSONObject jo = new JSONObject(json);
			// {"class":{"tearcher_id":51,"tearcher_name":"ding","id":107,"period_of_validity":"2014-08-01 23:59:59","name":"小丁英语课堂"},"follow_microposts_id":[589,588,587,586,585,584,582,581,580,579,578,576,590,577],"microposts":{"page":1,"pages_count":2,"details_microposts":[{"content":"爸爸妈妈","user_types":1,"avatar_url":"\/avatars\/students\/2014-04\/student_73.jpg","new_created_at":"2014-04-08
			// 16:48:12","follow_microposts_count":null,"name":"若相守","user_id":130,"reply_microposts_count":0,"micropost_id":683},{"content":"空军建军节啦","user_types":1,"avatar_url":"\/avatars\/students\/2014-04\/student_73.jpg","new_created_at":"2014-03-31
			// 14:03:44","follow_microposts_count":null,"name":"若相守","user_id":130,"reply_microposts_count":50,"micropost_id":604},{"content":"出来","user_types":1,"avatar_url":"\/avatars\/students\/2014-04\/student_73.jpg","new_created_at":"2014-03-31
			// 13:27:39","follow_microposts_count":null,"name":"若相守","user_id":130,"reply_microposts_count":15,"micropost_id":601},{"content":"哈哈还差哈哈","user_types":1,"avatar_url":"\/avatars\/students\/2014-04\/student_73.jpg","new_created_at":"2014-03-31
			// 12:30:55","follow_microposts_count":null,"name":"若相守","user_id":130,"reply_microposts_count":13,"micropost_id":595},{"content":"20","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:13:31","follow_microposts_count":0,"name":"ding","user_id":115,"reply_microposts_count":10,"micropost_id":592},{"content":"19","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:12:33","follow_microposts_count":0,"name":"ding","user_id":115,"reply_microposts_count":3,"micropost_id":591},{"content":"18","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:12:12","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":3,"micropost_id":590},{"content":"17","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:11:43","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":589},{"content":"16","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:10:52","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":588},{"content":"15","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:10:39","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":587},{"content":"14","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:10:02","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":586},{"content":"13","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:09:40","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":585},{"content":"12","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 12:05:58","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":584},{"content":"11","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:59:20","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":582},{"content":"10","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:59:01","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":581},{"content":"9","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:58:31","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":580},{"content":"8","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:56:32","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":579},{"content":"7","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:56:17","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":578},{"content":"6","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:54:41","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":577},{"content":"5","user_types":0,"avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","new_created_at":"2014-03-31
			// 11:54:28","follow_microposts_count":1,"name":"ding","user_id":115,"reply_microposts_count":0,"micropost_id":576}]},"student":{"id":73,"avatar_url":"\/avatars\/students\/2014-04\/student_73.jpg","user_id":130,"nickname":"dingdin","name":"若相守"},"notice":"登录成功！","status":"success"}
			String status = jo.getString("status");
			mes = jo.getString("notice");
			if (status.equals("error")) {
				mHandler.sendEmptyMessage(1);
			} else {
				JSONObject student = jo.getJSONObject("student"); // 获得学生的信息
				String id = student.getString("id");
				String user_id = student.getString("user_id");
				Log.i("linshi", user_id + "------");
				String avatar_url = student.getString("avatar_url"); // 获取本人头像昂所有在地址
				String name = student.getString("name");
				String nick_name = student.getString("nickname");
				String edunumber = student.getString("s_no");//  学号
				String edu_number="";
				if ("null".equals(edunumber)||edunumber.equals("")) {
					edu_number="";
				}else {
					edu_number=edunumber;
				}

				JSONObject class1 = jo.getJSONObject("class"); // 或得班级信息

				String school_class_id = class1.getString("id");
				String school_class_name = class1.getString("name");
				String validtime = class1.getString("period_of_validity");
				Log.i("Ax", user_id + "----------------" + school_class_id);
				SharedPreferences preferences = getSharedPreferences(SHARED,
						Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("name", name);
				Log.i("aaa", user_id + "----------------" + name);
				editor.putString("user_id", user_id);
				editor.putString("id", id);
				editor.putString("avatar_url", avatar_url);
				editor.putString("nickname", nick_name);
				editor.putString("school_class_id", school_class_id);
				editor.putString("school_class_name", school_class_name);
				editor.putString("validtime", validtime);
				editor.putString("edu_number", edu_number);
				editor.commit();
				eb.setClass_id(school_class_id);
				eb.setUser_id(Integer.parseInt(user_id));
				mHandler.sendEmptyMessage(2);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class MyThread extends Thread {
		public void run() {
			super.run();
			Map<String, String> map = new HashMap<String, String>();
			map.put("open_id", openid);
			json = ExerciseBookTool.doPost(QQ_LOGIN, map);
			Log.i("linshi", json);
			analyzeJson(json);
		}
	}

	private class BaseUiListener implements IUiListener {
		public void onComplete(JSONObject response) {
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {
			try {
				mPd.show();
				openid = values.getString("openid");
				Log.i("linshi", openid);
				JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
				JPushInterface.init(getApplicationContext());
				JPushInterface.setAlias(getApplicationContext(), openid, null);
				new MyThread().start();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void onError(UiError e) {
		}

		public void onCancel() {
		}

	}

	public void onClick(View v) {
		onClickLogin();
	}

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
