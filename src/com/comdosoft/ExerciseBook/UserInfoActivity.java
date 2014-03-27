package com.comdosoft.ExerciseBook;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.HomePageMainActivity.mod_avatar;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class UserInfoActivity extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;
	private RelativeLayout imageView1,imageView2;
	private TextView userinfo_username;
	private TextView userinfo_classname;
	public static UserInfoActivity instance = null;
	private String nickName = "丁作强";// 用户昵称
	private String classname = "三年级二班";
	private String id = "73";
	private String school_class_id = "15";
	private ProgressDialog prodialog;
	private TextView userinfo_youyi2, userinfo_jingzhun2, userinfo_xunsu2,
			userinfo_jiezu2;
	private ImageView userinfo_youyi1, userinfo_jingzhun1, userinfo_xunsu1,
			userinfo_jiezu1;

	ArrayList list;
	private int width;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (list.size()==4) {
					init();
				}

				break;

			case 7:
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.userinfo);
		instance = this;
		SharedPreferences preferences = getSharedPreferences(
				Urlinterface.SHARED, Context.MODE_PRIVATE);
		classname = preferences.getString("school_class_name", "");
		nickName = preferences.getString("nickname", "");
		 id = preferences.getString("id", null);
		 school_class_id = preferences.getString("school_class_id", null);
		 Display display = this.getWindowManager().getDefaultDisplay();
			width = display.getWidth();
		list = new ArrayList();

		userinfo_youyi2 = (TextView) findViewById(R.id.userinfo_youyi2); // 优异
		userinfo_jingzhun2 = (TextView) findViewById(R.id.userinfo_jingzhun2); // 精准
		userinfo_xunsu2 = (TextView) findViewById(R.id.userinfo_xunsu2); // 迅速
		userinfo_jiezu2 = (TextView) findViewById(R.id.userinfo_jiezu2); // 捷足

		userinfo_youyi1 = (ImageView) findViewById(R.id.userinfo_youyi1); // 优异
		userinfo_jingzhun1 = (ImageView) findViewById(R.id.userinfo_jingzhun1); // 精准
		userinfo_xunsu1 = (ImageView) findViewById(R.id.userinfo_xunsu1); // 迅速
		userinfo_jiezu1 = (ImageView) findViewById(R.id.userinfo_jiezu1); // 捷足

		userinfo_username = (TextView) findViewById(R.id.userinfo_username); // 用户昵称
		userinfo_classname = (TextView) findViewById(R.id.userinfo_classname); // 班级名
		userinfo_username.setText(nickName);
		userinfo_classname.setText(classname);
		imageView1 = (RelativeLayout) findViewById(R.id.imageView1);// 修改用户名图标
		imageView1.setOnClickListener(listener);
		imageView2 = (RelativeLayout) findViewById(R.id.imageView2);// 切换班级图标
		imageView2.setOnClickListener(listener2);
		layout = (LinearLayout) findViewById(R.id.main_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击页面外的地方消失",
						Toast.LENGTH_SHORT).show();
			}
		});
		if (ExerciseBookTool.isConnect(UserInfoActivity.this)) {

			Thread thread = new Thread(new get_my_archivements());
			thread.start();

		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, 0).show();
		}

	}

	/*
	 * 获得当前成就
	 */
	class get_my_archivements implements Runnable {
		public void run() {
			try {

				list = new ArrayList();

				Map<String, String> map = new HashMap<String, String>();
				map.put("student_id", id);
				map.put("school_class_id", school_class_id);

				String json = ExerciseBookTool.sendGETRequest(
						Urlinterface.get_my_achievements, map);
				setJson(json);
				handler.sendEmptyMessage(0);

			} catch (Exception e) {
				handler.sendEmptyMessage(7);
			}
		}
	}

	/*
	 * 解析 json字符串
	 */
	private void setJson(String json) {

		try {
			JSONObject obj = new JSONObject(json);

			String status = obj.getString("status");
			String notice = obj.getString("notice");
			if ("success".equals(status)) {

				JSONArray archivements = obj.getJSONArray("archivements");
				for (int i = 0; i < archivements.length(); ++i) {
					JSONObject o = (JSONObject) archivements.get(i);
					int archivement_types = o.getInt("archivement_types");
					int archivement_score = o.getInt("archivement_score");

					list.add(archivement_types, archivement_score);
				}

			} else {
				// Toast.makeText(getApplicationContext(), notice,
				// Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void init() {

		int num0 = (Integer) list.get(0); // 优异
		userinfo_youyi2.setText("LV" + num0 / 100);
		LayoutParams laParams0 = (LayoutParams) userinfo_youyi1
				.getLayoutParams();
		if (width==1200) {
			laParams0.width = (int) ((num0 % 100) * 2.01);
		}else {
			laParams0.width = (int) ((num0 % 100) * 1.35);
		}
		userinfo_youyi1.setLayoutParams(laParams0);

		int num1 = (Integer) list.get(1); // 精准
		userinfo_jingzhun2.setText("LV" + num1 / 100);
		LayoutParams laParams1 = (LayoutParams) userinfo_jingzhun1
				.getLayoutParams();
		if (width==1200) {
			laParams1.width = (int) ((num1 % 100) * 2.01);
		}else {
			laParams1.width = (int) ((num1 % 100) * 1.35);
		}
		userinfo_jingzhun1.setLayoutParams(laParams1);

		int num2 = (Integer) list.get(2); // 迅速
		userinfo_xunsu2.setText("LV" + num2 / 100);
		LayoutParams laParams2 = (LayoutParams) userinfo_xunsu1
				.getLayoutParams();
		if (width==1200) {
			laParams2.width = (int) ((num2 % 100) * 2.01);
		}else {
			laParams2.width = (int) ((num2 % 100) * 1.35);
		}
		userinfo_xunsu1.setLayoutParams(laParams2);

		int num3 = (Integer) list.get(3); // 捷足
		userinfo_jiezu2.setText("LV" + num3 / 100);
		LayoutParams laParams3 = (LayoutParams) userinfo_jiezu1
				.getLayoutParams();
		if (width==1200) {
			laParams3.width = (int) ((num3 % 100) * 2.01);
		}else {
			laParams3.width = (int) ((num3 % 100) * 1.35);
		}
		userinfo_jiezu1.setLayoutParams(laParams3);

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
			 Bundle bundle = data.getExtras();
			 String content = bundle.getString("content");
			 modifyName(content);
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
							array2 = new JSONObject(json2);
							Boolean status = array2.getBoolean("status");
							String notice = array2.getString("notice");

							if (status == true) {
								userinfo_username.setText(content);
								HomePageMainActivity.instance.userName
										.setText(content);
								SharedPreferences preferences = getSharedPreferences(Urlinterface.SHARED,
										 Context.MODE_PRIVATE);
										 Editor editor = preferences.edit();
										 editor.putString("nickname", content);
										 editor.commit();

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

		class mod_nickname implements Runnable {
			public void run() {
				try {
					
					MultipartEntity entity = new MultipartEntity();

					entity.addPart("student_id", new StringBody(id));
					entity.addPart("nickname", new StringBody(content,
							Charset.forName("UTF-8")));

					 String js1 = ExerciseBookTool.sendPhostimg(
								Urlinterface.MODIFY_PERSON_INFO, entity);
					 Message msg = new Message();// 创建Message 对象
					 msg.what = 0;
					 msg.obj = js1;
					 mHandler.sendMessage(msg);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"修改昵称失败", 0).show();
					mHandler.sendEmptyMessage(7);
				}
			}
		}
		if (ExerciseBookTool.isConnect(UserInfoActivity.this)) {
			prodialog = new ProgressDialog(UserInfoActivity.this);
			prodialog.setMessage("正在修改...");
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
			Thread thread = new Thread(new mod_nickname());
			thread.start();
		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, 0).show();
		}
	}
	

	
}
