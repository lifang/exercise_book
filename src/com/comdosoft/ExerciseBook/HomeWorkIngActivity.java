package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.WorkJson;

public class HomeWorkIngActivity extends Table_TabHost implements Urlinterface {
	private String id = "73";
	private String school_class_id = "85";
	private String json = "{\"status\": \"success\",\"notice\": \"获取成功！\",\"tasks\": [{\"id\": 97,\"name\": null,\"start_time\": \"2014-02-27T10:38:08+08:00\",\"question_types\": [0,1,4,5,6],\"finish_types\": [0],\"end_time\": \"2014-02-27T10:45:04+08:00\",\"question_packages_url\": \"/que_ps/question_p_234/resourse.zip\"}]}";
	private ExerciseBook eb;
	private LinearLayout mylayout;
	private int linear_item = 0;
	private List<LinearLayout> linearList = new ArrayList<LinearLayout>();
	private Intent intent = new Intent();
	private ProgressDialog prodialog;
	private String notice;
	private List<Integer> questiontype_list = new ArrayList<Integer>();
	private List<Integer> finish_list = new ArrayList<Integer>();
	private List<WorkPoJo> work_list = new ArrayList<WorkPoJo>();
	private TextView start_time;
	private TextView end_time;
	static HomeWorkIngActivity instance = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				if (work_list.size() != 0) {
					start_time.setText("发布时间为"
							+ work_list.get(0).getStart_time().split(" ")[1]);
					end_time.setText(work_list.get(0).getEnd_time().split(" ")[1]);
					questiontype_list = work_list.get(0).getQuestion_types();
					finish_list = work_list.get(0).getFinish_types();
					eb.setToday_newer_id(work_list.get(0).getId());
					if (questiontype_list.size() != 0) {
						for (int i = 0; i < questiontype_list.size(); i++) {
							setlayout(i);
						}
					} else {
						mylayout.setVisibility(View.GONE);
					}
				} else {
					mylayout.setVisibility(View.GONE);
				}
				break;
			case 1:
				prodialog.dismiss();
				Toast.makeText(HomeWorkIngActivity.this, notice,
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				prodialog.dismiss();
				Toast.makeText(getApplicationContext(), "解析数据出现问题",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homework_ing);
		eb = (ExerciseBook) getApplication();// 初始化
		initialize();// 初始化
		instance = this;
		prodialog = new ProgressDialog(HomeWorkIngActivity.this);
		prodialog.setMessage("正在获取最新作业");
		prodialog.setCanceledOnTouchOutside(false);
		prodialog.show();
		Thread thread = new Thread(new get_newer_task());
		thread.start();

	}

	// 初始化
	public void initialize() {
		mylayout = (LinearLayout) findViewById(R.id.mylayout);
		start_time = (TextView) findViewById(R.id.start_time);
		end_time = (TextView) findViewById(R.id.end_time);
	}

	public void setlayout(int i) {
		View view = View.inflate(this, R.layout.work_item, null);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		TextView work_name = (TextView) view.findViewById(R.id.work_name);
		TextView top = (TextView) view.findViewById(R.id.top);
		ImageView over_img = (ImageView) view.findViewById(R.id.over_img);
		work_name.setText(namearr[questiontype_list.get(i)].toString());
		layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				startDekaron();// 跳转到答题页面
			}
		});
		top.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Log.i("linshi", "textview");
			}
		});
		if (ExerciseBookTool.getExist(questiontype_list.get(i), finish_list)) {
			over_img.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
		} else {
			over_img.setVisibility(View.GONE);
			top.setVisibility(View.GONE);
		}

		int imgid = 0;
		imageView.setBackgroundResource(0);
		try {// 利用java反射动态设置图片
			imgid = (Integer) R.drawable.class.getField(
					"img" + (questiontype_list.get(i) + 1)).get(null);
		} catch (Exception e) {
			imgid = 0;
		}
		imageView.setBackgroundResource(imgid);

		AbsListView.LayoutParams param = new AbsListView.LayoutParams(300, 320);
		layout.setLayoutParams(param);
		if (i == 0 || i % 3 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.topMargin = 20;
			linear.setLayoutParams(lp);
			linearList.add(linear);
			mylayout.addView(linear);
			if (i > 0) {
				linear_item++;
			}
		}
		linearList.get(linear_item).addView(view);
	}

	class get_newer_task implements Runnable {
		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", id);
			map.put("school_class_id", school_class_id);
			String json;
			try {
				json = ExerciseBookTool.sendGETRequest(get_newer_task, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					handler.sendEmptyMessage(0);
				} else {
					notice = obj.getString("notice");
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(2);
			}
		}
	}

	// 跳转到挑战
	public void startDekaron() {
		this.finish();
		intent.setClass(this, TenSpeedActivity.class);
		startActivity(intent);
	}

	public void onclick(View v) {
		this.finish();
		intent.setClass(HomeWorkIngActivity.this, RecordMainActivity.class);
		startActivity(intent);
	}
}