package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comdosoft.ExerciseBook.pojo.ClassStu;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class LeftMenu extends Activity implements Urlinterface {
	LinearLayout linear = null;
	private View ll1;
	private View ll2;
	private View ll3;
	private View ll4;
	private View ll5;
	private View ll6; // 退出teachll
	private LinearLayout allLL;
	private LinearLayout teachll;
	private ImageView hwImg;
	private ImageView rImg;
	private ImageView classImg; // 左侧班级按钮
	private ImageView teachIV; // 班级老师头像
	private ImageView classback; // 班级缩图
	private TextView teachname;
	private GridView ClassStuGv;
	private boolean isClass = true;
	private String teacher_name;
	private String teacher_avatar_url;
	private Thread T1 = null;
	List<ClassStu> classStu;
	private boolean isStu_cj = true;
	SharedPreferences userInfo;
	ExerciseBook eb;
	String student_id;
	String school_class_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.left_menu);
		userInfo = getSharedPreferences("replyMenu", 0);
		LeftMenu.this.setFinishOnTouchOutside(true);
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		student_id = preferences.getString("id", "1");
		school_class_id = preferences.getString("school_class_id", "1");
		Invit();
		linear = (LinearLayout) findViewById(R.id.linear);
		ClickLis();
		eb = (ExerciseBook) getApplication();
	}

	public void setBackColor() {
		switch (eb.getMenu_num()) {
		case 0:
			ll1.setBackgroundColor(getResources().getColor(R.color.select_menu));
			break;
		case 1:
			ll2.setBackgroundColor(getResources().getColor(R.color.select_menu));
			break;
		case 2:
			ll3.setBackgroundColor(getResources().getColor(R.color.select_menu));
			break;
		case 3:
			ll4.setBackgroundColor(getResources().getColor(R.color.select_menu));
			break;
		default:
			break;
		}
	}

	public void ClickLis() {
		classback.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isClass) {
					ll5.setVisibility(View.VISIBLE);
					isClass = false;
				} else {
					ll5.setVisibility(View.GONE);
					isClass = true;
				}
			}

		});
		classImg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isClass) {
					if (T1 != null) {
						if (T1.isAlive())
							return;
					}
					T1 = new Thread(thread);
					T1.start();
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							546, 1718);
					allLL.setLayoutParams(params);
				} else {
					ll5.setVisibility(View.GONE);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							206, 1718);
					allLL.setLayoutParams(params);
					isClass = true;
				}
			}
		});
		allLL.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
		teachll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
		ll1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LeftMenu.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				eb.setMneu(true);
				eb.setMenu_num(0);
				clearActivity();
				Intent intent = new Intent(LeftMenu.this,
						HomePageMainActivity.class);
				startActivity(intent);
			}
		});
		ll2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LeftMenu.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				eb.setMenu_num(1);
				eb.setMneu(true);
				Editor editor = userInfo.edit();// 获取编辑器
				editor.putBoolean("HomeWorkMenu", true);
				editor.commit();
				clearActivity();
				Intent intent = new Intent(LeftMenu.this,
						HomeWorkIngActivity.class);
				startActivity(intent);
			}
		});
		ll3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LeftMenu.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				Intent intent = new Intent(LeftMenu.this,
						ReplyListViewActivity.class);
				eb.setMenu_num(2);
				clearActivity();
				startActivity(intent);
				eb.setMneu(true);
				Editor editor = userInfo.edit();// 获取编辑器
				editor.putBoolean("ReplyMenu", true);
				editor.commit();
			}

		});
		ll4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				LeftMenu.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				eb.setMenu_num(3);
				eb.setMneu(true);
				clearActivity();
				Intent intent = new Intent(LeftMenu.this,
						MCardBagActivity.class);
				startActivity(intent);
			}
		});
		ll6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				LeftMenu.this.finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				eb.setMneu(true);
				eb.setMenu_num(0);
				clearActivity();
				SharedPreferences preferences = getSharedPreferences(SHARED,
						Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("user_id", "");
				editor.putString("school_class_id", "");
				editor.putString("id", "");
				editor.commit();

				Intent intent = new Intent(LeftMenu.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}

	// 请求服务器数据
	Thread thread = new Thread() {
		public void run() {
			try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("school_class_id", school_class_id);
				map.put("student_id", student_id);
				String json = ExerciseBookTool.sendGETRequest(getClass, map);
				ChangeViewMessage(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	public void ChangeViewMessage(String str) {
		try {
			JSONObject jsonobject = new JSONObject(str);
			if (jsonobject.get("status").equals("success")) {
				classStu = new ArrayList<ClassStu>();
				teacher_name = jsonobject.getString("teacher_name");
				teacher_avatar_url = jsonobject.getString("teacher_avatar_url");
				JSONArray jsonArray = jsonobject.getJSONArray("classmates");
				for (int i = 0; i < jsonArray.length(); i++) {
					ClassStu stu = new ClassStu();
					JSONObject jsonobject2 = jsonArray.getJSONObject(i);
					String id = jsonobject2.getString("id");
					String name = jsonobject2.getString("name");
					String avatar_url = jsonobject2.getString("avatar_url");
					JSONArray jsonArray2 = jsonobject2
							.getJSONArray("archivement");
					stu.setId(id);
					stu.setName(name);
					stu.setAvatar_url(avatar_url);
					for (int j = 0; j < jsonArray2.length(); j++) {
						JSONObject jsonobject3 = jsonArray2.getJSONObject(j);
						String archivement_score = jsonobject3
								.getString("archivement_score");
						String types = jsonobject3
								.getString("archivement_types");
						switch (Integer.valueOf(types)) {
						case 0:
							stu.setSuccess_1(insertArchivement(archivement_score));
							break;
						case 1:
							stu.setSuccess_2(insertArchivement(archivement_score));
							break;
						case 2:
							stu.setSuccess_3(insertArchivement(archivement_score));
							break;
						case 3:
							stu.setSuccess_4(insertArchivement(archivement_score));
							break;
						}
					}
					classStu.add(stu);
				}
				handler1.sendEmptyMessage(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public int insertArchivement(String score) {
		int Score = Integer.valueOf(score);
		if (Score / 100 < 0) {
			return 0;
		} else {
			return Score / 100;
		}
	}

	Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ll5.setVisibility(View.VISIBLE);
				isClass = false;
				ClassStuGv.setColumnWidth(1);
				ClassStuGv.setAdapter(new StuGvAdapter());
				if (teacher_avatar_url.length() > 4) {
					ExerciseBookTool.set_background(Urlinterface.IP
							+ teacher_avatar_url, teachIV);
				}
				teachname.setText(teacher_name);
				break;
			}
		}
	};

	// 关闭上个主界面
	public void clearActivity() {
		List<Activity> activityList = eb.getActivityList();
		for (int i = 0; i < activityList.size(); i++) {
			activityList.get(i).finish();
		}
		eb.setActivityList();
	}

	// 初始化参数
	public void Invit() {
		eb = (ExerciseBook) getApplication();
		ll1 = findViewById(R.id.ll1);
		ll2 = findViewById(R.id.ll2);
		ll3 = findViewById(R.id.ll3);
		ll4 = findViewById(R.id.ll4);
		allLL = (LinearLayout) findViewById(R.id.allLinear);
		ll5 = findViewById(R.id.menuclassll);
		hwImg = (ImageView) findViewById(R.id.leftmenu_12red);
		rImg = (ImageView) findViewById(R.id.leftmenu_13red);
		teachll = (LinearLayout) findViewById(R.id.teachll);
		ll6 = findViewById(R.id.ll6);
		classback = (ImageView) findViewById(R.id.classback);
		classImg = (ImageView) findViewById(R.id.classMenu);
		teachIV = (ImageView) findViewById(R.id.teacherIm);
		teachname = (TextView) findViewById(R.id.teachname);
		ClassStuGv = (GridView) findViewById(R.id.classstugv);
		boolean homeWork = true;
		if (userInfo.getBoolean("HomeWorkMenu", true)) {
			homeWork = true;
		} else {
			homeWork = false;
		}
		boolean reply = userInfo.getBoolean("ReplyMenu", true);
		if (!homeWork) {
			hwImg.setVisibility(View.VISIBLE);
		}
		if (!reply) {
			rImg.setVisibility(View.VISIBLE);
		}
		setBackColor();
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.finish();
		eb.setMneu(true);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		return super.onTouchEvent(event);
	}

	public static class ViewHolder {
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public ImageView Ib;
		public View iteam_stu;
	}

	class StuGvAdapter extends BaseAdapter {

		public int getCount() {
			return classStu.size();
		}

		public Object getItem(int position) {
			return classStu.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.classstu_item, parent,
						false);
				holder = new ViewHolder();
				holder.iteam_stu = convertView.findViewById(R.id.stu_item);
				final View iteam_cj = convertView.findViewById(R.id.stu_cj);
				holder.tv1 = (TextView) convertView
						.findViewById(R.id.teachname);
				holder.tv2 = (TextView) convertView
						.findViewById(R.id.success_1);
				holder.tv3 = (TextView) convertView
						.findViewById(R.id.success_2);
				holder.tv4 = (TextView) convertView
						.findViewById(R.id.success_3);
				holder.tv5 = (TextView) convertView
						.findViewById(R.id.success_4);
				holder.Ib = (ImageView) convertView
						.findViewById(R.id.teacherIm);
				holder.iteam_stu.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (isStu_cj) {
							iteam_cj.setVisibility(View.VISIBLE);
							isStu_cj = false;
						} else {
							iteam_cj.setVisibility(View.GONE);
							isStu_cj = true;
						}

					}
				});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv1.setText(classStu.get(position).getName());
			if (teacher_avatar_url.length() > 4) {
				ExerciseBookTool.set_background(
						Urlinterface.IP
								+ classStu.get(position).getAvatar_url(),
						holder.Ib);
			}
			holder.tv2.setText("Lv " + classStu.get(position).getSuccess_1());
			holder.tv3.setText("Lv " + classStu.get(position).getSuccess_2());
			holder.tv4.setText("Lv " + classStu.get(position).getSuccess_3());
			holder.tv5.setText("Lv " + classStu.get(position).getSuccess_4());
			return convertView;
		}

	}
}
