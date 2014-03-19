package com.comdosoft.ExerciseBook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.pojo.Branch_PoJo;
import com.comdosoft.ExerciseBook.pojo.ClozePojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.PredicateLayout;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

public class Cloze_HistoryActivity extends AnswerBaseActivity implements
		Urlinterface, OnClickListener {

	public String path = "";
	public String sdpath = Environment.getExternalStorageDirectory() + "/";
	private String content = "";
	private PredicateLayout myLayout;
	private String[] str;
	private int specified_time;
	private List<Branch_PoJo> Branchlist;
	private List<ClozePojo> list;
	private ClozePojo cloze;
	private int index = 0;
	private AnswerJson answerJson;
	private List<Branch_AnswerPoJo> Branch_list = new ArrayList<Branch_AnswerPoJo>();
	private Gson gson;
	private AnswerPojo answer;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				cloze = list.get(index);
				Branch_list.clear();
				if (answer.getQuestions() != null) {
					Branch_list = answer.getQuestions().get(index)
							.getBranch_questions();
					setAccuracyAndUseTime(100,
							Integer.valueOf(answer.getUse_time()));
				} else {
					setAccuracyAndUseTime(0, 0);
				}

				myLayout.removeAllViews();
				content = cloze.getContent();
				setTextView();
				break;
			case 1:
				break;
			case 2:
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloze_history);
		setTimePropEnd();// 禁用道具
		setTruePropEnd();// 禁用道具
		setCheck("下一个");
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		gson = new Gson();
		initialize();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		String json = intent.getStringExtra("json");
		SetJson(json);
		SetAnswer();
	}

	private void SetAnswer() {
		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			answer = ExerciseBookTool.getAnswer(json2, "cloze");
		}
		handler.sendEmptyMessage(0);
	}

	// 初始化
	public void initialize() {
		myLayout = (PredicateLayout) findViewById(R.id.myLayout);
	}

	private void setTextView() {
		str = content.split("\\[\\[tag\\]\\]");
		for (int i = 0; i < str.length; i++) {
			View view1 = View.inflate(this, R.layout.cloze_view, null);
			TextView text = (TextView) view1.findViewById(R.id.tv);
			text.setText(str[i].toString());
			Spinner spinner = (Spinner) view1.findViewById(R.id.spinner);

			if (i != str.length - 1) {
				String Opption = cloze.getList().get(i).getOpption();
				String[] Opption_str = Opption.split(";\\|\\|;");
				final List<String> str_list = getlist(Opption_str);// 设置默认显示为空
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						R.layout.spinnerview, str_list) {
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						View view = getLayoutInflater().inflate(
								R.layout.my_spinner, parent, false);
						TextView label = (TextView) view
								.findViewById(R.id.label);
						label.setText(str_list.get(position));
						// ImageView iv = (ImageView)
						// view.findViewById(R.id.iv);
						if (position == 0) {
							label.setVisibility(View.GONE);
						} else {
							label.setBackgroundColor(getResources().getColor(
									R.color.spinner_color));
							// iv.setVisibility(View.GONE);
						}
						// 这里ViewGroup parent就是设置整个下拉列表框的
						parent.setFadingEdgeLength(0); // 设置模糊边界
						// parent.setOverScrollMode(View.OVER_SCROLL_NEVER);//
						// 滚动的
						// parent.setBackgroundResource(R.drawable.sprin);// 背景
						return view;
					}
				};
				// adapter.setDropDownViewResource(R.layout.my_spinner);
				spinner.setAdapter(adapter);
				int item = getItem(str_list, cloze.getList().get(i).getAnswer());
				spinner.setSelection(item);
			}
			if (i == str.length - 1) {
				spinner.setVisibility(View.GONE);
			}
			myLayout.addView(view1);
		}
	}

	public int getItem(List<String> list, String value) {
		int item = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(value)) {
				item = i;
			}
		}
		return item;
	}

	private void SetJson(String json) {
		list = new ArrayList<ClozePojo>();
		if (json != "") {
			try {
				JSONObject time_limit = new JSONObject(json);
				specified_time = time_limit.getInt("specified_time");
				Log.i("aaa", specified_time + "--");
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					for (int i = 0; i < questions.length(); i++) {
						JSONObject jo = questions.getJSONObject(i);
						JSONArray jsonarr = jo.getJSONArray("branch_questions");
						Branchlist = new ArrayList<Branch_PoJo>();
						for (int j = 0; j < jsonarr.length(); j++) {
							JSONObject item = jsonarr.getJSONObject(j);
							Branch_PoJo tl = new Branch_PoJo(item.getInt("id"),
									item.getString("opption"),
									item.getString("answer"));
							Branchlist.add(tl);
						}

						ClozePojo lp = new ClozePojo(jo.getInt("id"),
								jo.getString("content"), Branchlist);
						Log.i("suanfa", "id--" + jo.getInt("id"));
						list.add(lp);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(Cloze_HistoryActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private List<String> getlist(String[] str) {
		List<String> strlist = new ArrayList<String>();
		strlist.add("");
		for (int i = 0; i < str.length; i++) {
			strlist.add(str[i]);
		}
		return strlist;
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			Cloze_HistoryActivity.this.finish();
			intent.setClass(Cloze_HistoryActivity.this,
					HomeWorkIngActivity.class);
			startActivity(intent);
			break;
		case R.id.base_check_linearlayout:
			if (index + 1 < list.size()) {
				index += 1;
				handler.sendEmptyMessage(0);
			} else {
				MyDialog("没有更多历史记录了,点击确定退出!", 1, Cloze_HistoryActivity.this);
			}
			break;
		}
	}

	// 自定义dialog设置
	private void MyDialog(String title, final int type, final Context context) {
		// type :0表示退出 1表示结束
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog);
		dialog.setCancelable(true);

		ImageView dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);

		TextView title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
		title_tv.setText(title);
		Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
		dialog_ok.setText("确定");
		Button dialog_no = (Button) dialog.findViewById(R.id.dialog_no);
		dialog_no.setText("取消");
		dialog_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				((Activity) context).finish();
				Intent intent = new Intent(context.getApplicationContext(),
						HomeWorkIngActivity.class);
				startActivity(intent);
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (type == 1) {
			dialog_no.setVisibility(View.GONE);
			dialog_ok.setBackgroundColor(getResources().getColor(R.color.lvse));
		} else {
			dialog_img.setVisibility(View.GONE);
		}
		dialog.show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Cloze_HistoryActivity.this.finish();
			Intent intent = new Intent();
			intent.setClass(Cloze_HistoryActivity.this,
					HomeWorkIngActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}