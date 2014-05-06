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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
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
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.pojo.ClassPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class LookAllContentActivity extends Activity implements Urlinterface {
	private ImageView layout;
	private ListView switchclassLv;
	private ExerciseBook exerciseBook;
	private ImageView add_newclass;
	private String id = "73";
	private String school_class_id = "83";
	private String content = "";
	private String types = "";
	private String full_text = "";
	String answer = "";
	ImageMemoryCache memoryCache;
	TextView Lookcontent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.lookallcontent);
		memoryCache = new ImageMemoryCache(this);
		exerciseBook = (ExerciseBook) getApplication();
		exerciseBook.getActivityList().add(this);
		Intent intent = getIntent();//
		types = intent.getStringExtra("types"); // 获得上个页面传过来的
		content = intent.getStringExtra("content");
		full_text = intent.getStringExtra("full_text");
		answer = intent.getStringExtra("answer");

		Lookcontent = (TextView) findViewById(R.id.lookcontent);
		layout = (ImageView) findViewById(R.id.look_all_close);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LookAllContentActivity.this.finish();
			}
		});

		if (types.equals("5")) {
			Lookcontent.setText("");
			settypes5(full_text, answer, Lookcontent);
			// Lookcontent.setText(settypes5(full_text,answer));
			// Lookcontent.setText(Html.fromHtml("<u>"+"hahaha"+"</u>"));
		} else {
			Lookcontent.setText(setback(content, types));
		}

	}

	// 完形填空
	// 完形填空
	public String settypes5(String full_text, String answer,
			TextView Lookcontent) {
//		tv_msg.append(content.substring(count, start));

//		tv_msg.append(Html.fromHtml("<a href=" + start + " ><u>" + key

//		+ "</u></a>"));

		List arrs = new ArrayList<String>();
		JSONArray answerarray;
		try {
			answerarray = new JSONArray(answer);
			for (int j = 0; j < answerarray.length(); j++) {
				JSONObject ob = answerarray.getJSONObject(j);
				String contentStr = ob.getString("content");
				arrs.add(ob.getString("answer"));
				Log.i("22----------", ob.getString("answer") + "");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		full_text = full_text.replaceAll("\\[\\[sign\\]\\]",
				" \\[\\[sign\\]\\] ");
		full_text = ExerciseBookTool.del_tag(full_text);
		String[] textarr = full_text.split("\\[\\[sign\\]\\]");
		for (int i = 0; i < textarr.length; i++) {
			
			Lookcontent.append(textarr[i]);
			if (i <= arrs.size() - 1) {
				if (((i+1)+"").equals(content)) {
					Lookcontent.append("____");
				}else {
					Lookcontent.append(Html.fromHtml("<u>" + arrs.get(i)+ "</u>"));
				}
			}
		}
		// content = full_text.replace("[[sign]]", "___");
		return null;
	}

	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	// 背面
	public String setback(String str, String types) {
		String content = null;
		int types2 = 0;
		if ("null".equals(types)) {
			types2 = -1;
		} else {
			types2 = Integer.parseInt(types);
		}
		// String content = null;
		String[] strarr;
		switch (types2) {
		case 0:
			return str;
		case 1:
			return str;
		case 2:
			return str;
		case 3:
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i] + "\n";
			}
			return content.substring(4, content.length());
		case 4:
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				String[] strarr1 = strarr[i].split("<=>");
				for (int j = 0; j < strarr1.length; j++) {
					content += strarr1[j] + " ";
				}
				content += "\n";
			}
			return content.substring(4, content.length());
		case 6:
			return str;
		}
		return str;
	}

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

//	// 点击空白处 本界面消失
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		finish();
//		return true;
//	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				Toast.makeText(LookAllContentActivity.this, msg.obj.toString(),
						1).show();
				break;
			}
		}
	};

	// 关闭上个主界面
	public void clearActivity() {
		List<Activity> activityList = exerciseBook.getActivityList();
		for (int i = 0; i < activityList.size(); i++) {
			activityList.get(i).finish();
		}
		exerciseBook.setActivityList();
	}

}
