package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.WorkJson;
import com.google.gson.Gson;

public class HomeWorkIngActivity extends Table_TabHost implements Urlinterface {
	private String id;
	private String school_class_id;
	private String json = "{\"status\":\"success\",\"notice\":\"\u83b7\u53d6\u6210\u529f\uff01\",\"tasks\":[{\"id\":130,\"name\":\"\",\"start_time\":\"2014-03-12T14:44:45+08:00\",\"question_types\":[0,1,2,3,4,5,6],\"finish_types\":[1,2,5],\"end_time\":\"2014-03-13T18:00:00+08:00\",\"question_packages_url\":\"/que_ps/question_p_264/resourse.zip\"}],\"knowledges_cards_count\":null}";
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
	private String path;
	private List<String> json_list;
	private List<Boolean> typeList;
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
				Toast.makeText(getApplicationContext(), "网络连接异常!",
						Toast.LENGTH_SHORT).show();
				mylayout.setVisibility(View.GONE);
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homework_ing);
		eb = (ExerciseBook) getApplication();// 初始化
		eb.getActivityList().add(this);
		typeList = new ArrayList<Boolean>();
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		id = preferences.getString("id", "0");
		school_class_id = preferences.getString("school_class_id", "0");
		Log.i("aaa", id + "/" + school_class_id);
		eb.setUid(id);
		eb.setClass_id(school_class_id);
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

	private void getJsonPath() {
		path = Environment.getExternalStorageDirectory() + "/"
				+ "Exercisebook_app/" + eb.getUid() + "/" + eb.getClass_id()
				+ "/" + eb.getWork_id();
		File file = new File(path + "/questions.js");
		if (file.exists()) {
			Log.i("linshi", "获取json");
			String json = ExerciseBookTool.getJson(path + "/questions.js");
			SetJson(json);
		}
	}

	private void SetJson(String json) {
		json_list = new ArrayList<String>();
		try {
			JSONObject obj = new JSONObject(json);
			json_list.add(obj.getString("listening"));
			json_list.add(obj.getString("reading"));
			json_list.add(obj.getString("time_limit"));
			json_list.add(obj.getString("selecting"));
			json_list.add(obj.getString("lining"));
			json_list.add(obj.getString("cloze"));
			json_list.add(obj.getString("sort"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void initAnswer() {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(path + "/answer.js");
			if (!file.exists()) {
				file.createNewFile();
				Log.i("linshi", path + "/answer.js");
				AnswerJson answer = new AnswerJson(eb.getWork_id(), "0",
						new String[] {}, new AnswerPojo("0", "", "-1", "-1",
								"0", new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()));
				// String json=JSONArray.fromObject(answer);
				Gson gson = new Gson();
				String result = gson.toJson(answer);
				Log.i("linshi", result);
				ExerciseBookTool.writeFile(path + "/answer.js", result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setlayout(final int i) {
		View view = View.inflate(this, R.layout.work_item, null);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		TextView work_name = (TextView) view.findViewById(R.id.work_name);
		TextView top = (TextView) view.findViewById(R.id.top);
		ImageView over_img = (ImageView) view.findViewById(R.id.over_img);
		work_name.setText(namearr[questiontype_list.get(i)].toString());
		layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				startDekaron(i);// 跳转到答题页面
			}
		});
		top.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Log.i("aaa", "====================");
				Intent intent = new Intent(HomeWorkIngActivity.this,
						RankingOfPointsActivity.class);
				intent.putExtra("types", questiontype_list.get(i));
				intent.putExtra("pub_id", Integer.valueOf(eb.getWork_id()));
				startActivity(intent);
			}
		});
		if (ExerciseBookTool.getExist(questiontype_list.get(i), finish_list)) {
			typeList.add(true);
			over_img.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
		} else {
			typeList.add(false);
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
			// String json;
			try {
				// json = ExerciseBookTool.sendGETRequest(get_newer_task, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					Log.i("linshi", work_list.size() + "-size");
					if (work_list.size() != 0) {
						eb.setWork_id(work_list.get(0).getId() + "");
						getJsonPath();
						initAnswer();// 初始化answer
					}
					handler.sendEmptyMessage(0);
				} else {
					notice = obj.getString("notice");
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				// handler.sendEmptyMessage(2);
				// e.printStackTrace();
			}
		}
	}

	// 跳转到挑战
	/**
	 * 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
	 * 
	 * @param i
	 */
	public void startDekaron(int i) {
		if (typeList.get(i)) {// 已完成
			MyDialog(i);
		} else {
			Start_Acvivity(i);
		}
	}

	public void Start_Acvivity(int i) {// 做题跳转
		switch (questiontype_list.get(i)) {
		case 0:

			intent.setClass(this, AnswerDictationBeginActivity.class);
			break;
		case 1:
			intent.setClass(this, SpeakPrepareActivity.class);
			if (typeList.get(i)) {// 已完成
				eb.setHistory_type(true);
			} else {
				eb.setHistory_type(false);
			}
			break;
		case 2:
			intent.setClass(this, TenSpeedActivity.class);
			break;
		case 3:
			String json = "{  \"selecting\": {\"specified_time\": \"100\", \"question_types\": \"6\", \"questions\": [{\"id\": \"284\",\"branch_questions\": [ {\"id\": \"181\", \"content\": \"This is ___ apple!\", \"option\": \"a;||;an\", \"answer\": \"an;||;a\" },{\"id\": \"181\", \"content\": \"<file>apple.jpg</file>Why he is ___ Google!\", \"option\": \"apple;||;banana;||;orange;||;pear\", \"answer\": \"apple;||;banana\"},{\"id\": \"181\", \"content\": \"<file>apple.mp3</file>\", \"option\": \"one;||;two;||;three\", \"answer\": \"two\"}, {\"id\": \"181\", \"content\": \"<file>apple.jpg</file>Pears have white flesh and thin green or yellow skin.\", \"option\": \"iPhone;||;S5;||;Xperia\", \"answer\": \"iPhone\"},{\"id\": \"181\", \"content\": \"Dad.come set here!\", \"option\": \"ZhangDaCa;||;ChenLong\", \"answer\": \"ZhangDaCa\"}]}]}}";
			String path = "/sdcard/Exercisebook_app/73/85/130/answer.js";
			intent.putExtra("json", json);
			intent.putExtra("path", path);
			intent.putExtra("status", 0);
			intent.setClass(this, AnswerSelectActivity.class);
			break;
		case 4:
			intent.setClass(this, AnswerWireActivity.class);
			break;
		case 5:
			intent.setClass(this, ClozeActivity.class);
			break;
		case 6:
			intent.setClass(this, AnswerOrderActivity.class);
			break;
		}
		intent.putExtra("json", json_list.get(i));
		intent.putExtra("path", path + "/answer.js");
		Log.i("aaa", json_list.get(i));
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		startActivity(intent);
		this.finish();
	}

	public void Start_History_Acvivity(int i) {// 历史记录跳转
		switch (questiontype_list.get(i)) {
		case 0:
			intent.setClass(this, SpeakPrepareActivity.class);
			break;
		case 1:
			intent.setClass(this, SpeakPrepareActivity.class);
			if (typeList.get(i)) {// 已完成
				eb.setHistory_type(true);
			} else {
				eb.setHistory_type(false);
			}
			break;
		case 2:
			intent.setClass(this, Ten_HistoryActivity.class);
			break;
		case 3:
			intent.setClass(this, AnswerSelectActivity.class);
			break;
		case 4:
			intent.setClass(this, AnswerWireActivity.class);
			break;
		case 5:
			intent.setClass(this, Cloze_HistoryActivity.class);
			break;
		case 6:
			intent.setClass(this, AnswerOrderActivity.class);
			break;
		}
		intent.putExtra("json", json_list.get(i));
		intent.putExtra("path", path + "/answer.js");
		Log.i("aaa", json_list.get(i));
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		startActivity(intent);
		this.finish();
	}

	// 自定义dialog设置
	private void MyDialog(final int type) {
		// type :0表示退出 1表示结束
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog_main);
		dialog.setCancelable(true);

		ImageView close = (ImageView) dialog.findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button history = (Button) dialog.findViewById(R.id.history);
		Button working = (Button) dialog.findViewById(R.id.working);
		history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Start_History_Acvivity(type);				
			}
		});
		working.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Start_Acvivity(type);		
			}
		});
		dialog.show();
	}

	public void onclick(View v) {
		this.finish();
		intent.setClass(HomeWorkIngActivity.this, RecordMainActivity.class);
		startActivity(intent);
	}
}