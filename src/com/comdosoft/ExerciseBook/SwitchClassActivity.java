package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.HomepageAllActivity.get_class_info;
import com.comdosoft.ExerciseBook.pojo.ClassPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class SwitchClassActivity extends Activity {
	private LinearLayout layout;
	private ListView switchclassLv;
	private ExerciseBook exerciseBook;
	private ImageView add_newclass;
	private String id = "73";
	private String school_class_id = "83";

	private List<ClassPojo> classList = new ArrayList<ClassPojo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.switchclass);

		exerciseBook = (ExerciseBook) getApplication();
		switchclassLv = (ListView) findViewById(R.id.switchclassLv);
		layout = (LinearLayout) findViewById(R.id.switchclass);
		add_newclass = (ImageView) findViewById(R.id.add_newclass);// 加入新班级
		SharedPreferences sp = getSharedPreferences(Urlinterface.SHARED, 0);
		// id = sp.getString("id", "null");
		// school_class_id = sp.getString("school_class_id", "null");
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击别的地方本界面消失",
						Toast.LENGTH_SHORT).show();
			}
		});

		// 加入新班级
		add_newclass.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intentp = new Intent();
				intentp.setClass(SwitchClassActivity.this,
						com.comdosoft.ExerciseBook.tools.OpenInputMethod.class);//
				startActivityForResult(intentp, 0);
			}
		});

		Thread thread1 = new Thread() {
			public void run() {
				try {
					HashMap<String, String> mp = new HashMap<String, String>();
					mp.put("student_id", String.valueOf(id));
					String json = ExerciseBookTool.sendGETRequest(
							Urlinterface.get_class, mp);
					JSONObject jsonobject = new JSONObject(json);
					JSONArray jsonarray = jsonobject.getJSONArray("classes");
					for (int i = 0; i < jsonarray.length(); i++) {
						JSONObject jsonobject2 = jsonarray.getJSONObject(i);
						int class_id = (Integer) jsonobject2.get("class_id");
						String class_name = jsonobject2.getString("class_name");
						classList.add(new ClassPojo(class_id, class_name));
					}
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		if (ExerciseBookTool.isConnect(SwitchClassActivity.this)) {
			thread1.start();

		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, 0).show();
		}

	}

	// 点击空白处 本界面消失
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				switchclassLv.setAdapter(new SwitchClassAdapter(
						getApplication(), classList));
				break;
			case 1:
				Toast.makeText(SwitchClassActivity.this, msg.obj.toString(), 1)
						.show();
				break;
			}
		}
	};

	// 自定义 适配器
	public class SwitchClassAdapter extends BaseAdapter {
		private List<ClassPojo> classlist;
		private Context context;

		public SwitchClassAdapter(Context context, List<ClassPojo> classlist) {
			this.context = context;
			this.classlist = classlist;
		}

		public int getCount() {
			return classlist.size();
		}

		public Object getItem(int position) {
			return classlist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			TextView scTv = null;
			ImageView class_dangqian = null;
			convertView = inflater.inflate(R.layout.switchclass_one, null);
			class_dangqian = (ImageView) convertView
					.findViewById(R.id.dangqian_class);
			if (school_class_id.equals(classlist.get(position).getId() + "")) {
				class_dangqian.setVisibility(View.VISIBLE);
			}
			scTv = (TextView) convertView.findViewById(R.id.switchclass_oneTv);

			scTv.setText(classlist.get(position).getName());
			scTv.setGravity(Gravity.CENTER);
			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new Thread() {
						public void run() {

							exerciseBook.setMainItem(0);
							Intent intent = new Intent(
									SwitchClassActivity.this,
									HomePageMainActivity.class);
							SharedPreferences preferences = getSharedPreferences(
									Urlinterface.SHARED, Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							HomePageMainActivity.instance.finish();
							UserInfoActivity.instance.finish();
							editor.putString("school_class_id", String
									.valueOf(classlist.get(position).getId()));
							editor.putString("school_class_name", String
									.valueOf(classlist.get(position).getName()));
							editor.commit();
							startActivity(intent);
							SwitchClassActivity.this.finish();
						}
					}.start();
				}
			});

			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 5: // 加入新班级
			Bundle bundle = data.getExtras();
			final String content = bundle.getString("content");
			Thread thread = new Thread() {
				public void run() {
					try {
						HashMap<String, String> mp = new HashMap<String, String>();

						mp.put("verification_code", content);
						mp.put("student_id", id);
						String json = ExerciseBookTool.doPost(
								Urlinterface.Validation_into_class, mp);
						JSONObject jsonobject = new JSONObject(json);
						String status = jsonobject.getString("status");
						if (status.equals("error")) {
							String notice = jsonobject.getString("notice");
							Message msg = new Message();
							msg.what = 1;
							msg.obj = notice;
							handler.sendMessage(msg);

						} else if (status.equals("success")) {
							JSONObject jsonobject2 = jsonobject
									.getJSONObject("class");
							int id = jsonobject2.getInt("id");
							SharedPreferences preferences = getSharedPreferences(
									Urlinterface.SHARED, Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString("school_class_id",
									String.valueOf(id));
							editor.commit();
							exerciseBook.setMainItem(0);
							HomePageMainActivity.instance.finish();
							UserInfoActivity.instance.finish();
							Intent intent = new Intent(
									SwitchClassActivity.this,
									HomePageMainActivity.class);
							startActivity(intent);
							SwitchClassActivity.this.finish();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			if (ExerciseBookTool.isConnect(SwitchClassActivity.this)) {
				thread.start();

			} else {
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, 0).show();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
