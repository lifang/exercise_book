package com.comdosoft.ExerciseBook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
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
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class RegistrationActivity extends Activity implements Urlinterface {
	private EditText reg_nicheng;//
	private EditText reg_xingming; //
	private EditText reg_banjiyanzhengma;
	private ProgressDialog prodialog;
	private ExerciseBook exerciseBook;
	private String open_id = "asfds"; // QQ 的 open id

	/* 请求码 */

	private String json = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_reg);
		exerciseBook = (ExerciseBook) getApplication();
		// Date d = new Date();
		// open_id = d.toString();
		// 上面两句代码 用来获得不一样的 qq_uid，，，测试 用，，后期删除

		Intent intent = getIntent();//
		open_id = intent.getStringExtra("open_id"); // 获得上个页面传过来的 QQ openid

		reg_nicheng = (EditText) findViewById(R.id.reg_nicheng);
		reg_xingming = (EditText) findViewById(R.id.reg_xingming);
		reg_banjiyanzhengma = (EditText) findViewById(R.id.reg_banjiyanzhengma);

	}
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}



	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				final String res = (String) msg.obj;
				if (json.length() != 0) {
					JSONObject array;

					try {
						array = new JSONObject(json);
						String status = array.getString("status");
						String notice = array.getString("notice");

						if ("success".equals(status)) {
							JSONObject student = array.getJSONObject("student"); // 获得学生的信息
							String id = student.getString("id");
							String user_id = student.getString("user_id");
							Log.i("linshi", user_id + "------");
							String avatar_url = student.getString("avatar_url"); // 获取本人头像昂所有在地址
							String name = student.getString("name");
							String nick_name = student.getString("nickname");
							// service.save(id, user_id, nick_name, nick_name,
							// avatar_url);
							JSONObject class1 = array.getJSONObject("class"); // 或得班级信息
							// 获取class_name
							String school_class_name = class1.getString("name");
							String school_class_id = class1.getString("id");
							Log.i(tag, user_id + "------" + school_class_id);
							SharedPreferences preferences = getSharedPreferences(
									SHARED, Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString("name", name);
							editor.putString("user_id", user_id);
							editor.putString("id", id);
							editor.putString("avatar_url", avatar_url);
							editor.putString("nickname", nick_name);
							editor.putString("school_class_id", school_class_id);
							editor.putString("school_class_name",school_class_name);
							editor.commit();
							exerciseBook.setClass_id(school_class_id);
							exerciseBook.setUser_id(Integer.parseInt(user_id));

							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
							// 添加页面跳转 跳到班级页面
							Intent intent = new Intent();
							intent.setClass(
									RegistrationActivity.this,
									com.comdosoft.ExerciseBook.HomePageMainActivity.class);//
							startActivity(intent);
							RegistrationActivity.this.finish();

						} else {

							Intent it = new Intent(RegistrationActivity.this,
									ErrorDisplay.class);
							it.putExtra("notice", notice);
							startActivity(it);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 7:
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;

			}
		}
	};

	// 确认按钮 点击时触发的方法
	public String reg_queren(View v) throws Exception {

		String nickname = reg_nicheng.getText().toString();
		String name = reg_xingming.getText().toString();
		String verification_code = reg_banjiyanzhengma.getText().toString();
		String nicknames = nickname.replaceAll(" ", "");
		String names = name.replaceAll(" ", "");
		String verification_codes = verification_code.replaceAll(" ", "");
		
		if (nickname.length() == 0 || name.length() == 0
				|| verification_code.length() == 0|| nicknames.equals("")|| names.equals("")|| verification_codes.equals("")) {
			Intent it = new Intent(RegistrationActivity.this,
					ErrorDisplay.class);
			it.putExtra("notice", "编辑框不能为空");
			startActivity(it);

		} else {

			Thread thread = new Thread() {
				public void run() {
					try {

						String nickname = reg_nicheng.getText().toString();
						String name = reg_xingming.getText().toString();
						String verification_code = reg_banjiyanzhengma
								.getText().toString();
						MultipartEntity entity = new MultipartEntity();
						entity.addPart("open_id", new StringBody(open_id));

						entity.addPart("nickname", new StringBody(nickname,
								Charset.forName("UTF-8")));
						entity.addPart("name",
								new StringBody(name, Charset.forName("UTF-8")));
						entity.addPart("verification_code", new StringBody(
								verification_code));

						json = ExerciseBookTool.sendPhostimg(
								Urlinterface.RECORD_PERSON_INFO, entity);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = json;
						mHandler.sendMessage(msg);

					} catch (Exception e) {
						mHandler.sendEmptyMessage(7);
					}
				}
			};

			if (ExerciseBookTool.isConnect(RegistrationActivity.this)) {
				prodialog = new ProgressDialog(RegistrationActivity.this);
				prodialog.setMessage(ExerciseBookParams.PD_REG);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, 0).show();
			}

		}
		return null;
	}

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
