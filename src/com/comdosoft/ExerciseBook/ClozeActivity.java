package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.pojo.Branch_PoJo;
import com.comdosoft.ExerciseBook.pojo.ClozePojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.MyspinnerAdapter;
import com.comdosoft.ExerciseBook.tools.PredicateLayout;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

public class ClozeActivity extends AnswerBaseActivity implements Urlinterface,
		OnClickListener {

	private ExerciseBook eb;
	public String path = "";
	public String sdpath = Environment.getExternalStorageDirectory() + "/";
	private String content = "";
	private PredicateLayout myLayout;
	private String[] str;
	private int specified_time;
	private List<Branch_PoJo> Branchlist;
	private List<ClozePojo> list;
	private ClozePojo cloze;
	private Map<Integer, String> user_select;
	private int index = 0;
	private int questions_item;
	private int status;
	private int question_id;
	private AnswerJson answerJson;
	private Gson gson;
	private int propItem = 0;
	private List<TextView> tv_list;
	private boolean Check = false;
	private PopupWindow popupWindow;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.i("aaa", "---");
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
		setContentView(R.layout.cloze);
		setTimePropEnd();// 禁用道具
		// setTruePropEnd();// 禁用道具
		eb = (ExerciseBook) getApplication();
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_propTrue).setOnClickListener(this);
		gson = new Gson();
		initialize();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		String json = intent.getStringExtra("json");
		status = intent.getIntExtra("status", 1);
		SetJson(json);
		SetAnswer();
		Finish_Json();
	}

	private void SetAnswer() {
		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			SetAnswer_Json(json2);
			switch (status) {
			case 0:
				if (questions_item == -1) {// 大题索引为-1表示没做
					Log.i("suanfa", "没有做过");
					cloze = list.get(0);
					question_id = list.get(0).getId();
				} else {
					cloze = list.get(index);
					question_id = list.get(index).getId();
				}
				break;
			case 1:
				cloze = list.get(index);
				question_id = list.get(index).getId();
				break;
			}
		}
		handler.sendEmptyMessage(0);
	}

	// 初始化
	public void initialize() {
		myLayout = (PredicateLayout) findViewById(R.id.myLayout);
	}

	private void setTextView() {
		str = content.split("\\[\\[tag\\]\\]");
		user_select = new HashMap<Integer, String>();
		tv_list = new ArrayList<TextView>();
		for (int i = 0; i < str.length; i++) {
			View view1 = View.inflate(this, R.layout.cloze_view, null);
			TextView text = (TextView) view1.findViewById(R.id.tv);
			text.setText(str[i].toString());
			final TextView spinner = (TextView) view1
					.findViewById(R.id.spinner);
			if (i != str.length - 1) {
				String Opption = cloze.getList().get(i).getOpption();
				final String[] Opption_str = Opption.split(";\\|\\|;");
				spinner.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						showWindow(spinner, Opption_str);
					}
				});
			}
			if (i == str.length - 1) {
				spinner.setVisibility(View.GONE);
			} else {
				tv_list.add(spinner);
			}
			myLayout.addView(view1);
		}
	}

	// 获取历史索引
	private void SetAnswer_Json(String json) {
		if (json != "") {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject cloze = obj.getJSONObject("cloze");
				questions_item = cloze.getInt("questions_item");
				int use_time = cloze.getInt("use_time");
				setUseTime(use_time);
				setStart();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
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
				Toast.makeText(ClozeActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private List<String> getlist(String[] str) {
		List<String> strlist = new ArrayList<String>();
		// strlist.add("");
		for (int i = 0; i < str.length; i++) {
			strlist.add(str[i]);
		}
		return strlist;
	}

	// 0为继续 1为全部做完 2为本小题做完
	public int setAnswerJson(String answer_history,
			Map<Integer, String> answer, int id) {
		int type = 0;
		answerJson = gson.fromJson(answer_history, AnswerJson.class);
		answerJson.cloze.setUpdate_time("2014-03-12 08:00:00");
		int q_item = Integer.valueOf(answerJson.cloze.getQuestions_item());
		int b_item = Integer.valueOf(answerJson.cloze.getBranch_item());
		Log.i("aaa", b_item + "/" + q_item);

		b_item += 1;
		answerJson.cloze.setBranch_item(b_item + "");
		answerJson.cloze.setUse_time(getUseTime() + "");
		Answer_QuestionsPojo aq = new Answer_QuestionsPojo(id + "",
				new ArrayList<Branch_AnswerPoJo>());
		answerJson.cloze.getQuestions().add(aq);
		q_item += 1;
		answerJson.cloze.setQuestions_item(q_item + "");
		for (Map.Entry<Integer, String> entry : answer.entrySet()) {
			int ratio = 0;
			if (entry.getValue().equals(
					cloze.getList().get(entry.getKey()).getAnswer())) {
				ratio = 100;
			}
			Log.i("aaa", "ratio:"
					+ cloze.getList().get(entry.getKey()).getAnswer());
			answerJson.cloze
					.getQuestions()
					.get(q_item)
					.getBranch_questions()
					.add(new Branch_AnswerPoJo(cloze.getList()
							.get(entry.getKey()).getId()
							+ "", entry.getValue(), ratio + ""));
		}
		Log.i("aaa", q_item + "/" + list.size());
		if (q_item + 1 == list.size()) {// 结束
			answerJson.cloze.setStatus("1");
			type = 1;
		}
		String str = gson.toJson(answerJson);
		try {
			ExerciseBookTool.writeFile(path, str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return type;
	}

	private int Again() {
		int type = 0;
		if (index + 1 == list.size()) {// 结束
			type = 1;
		}
		return type;
	}

	public void showWindow(final View position, final String[] Opption_str) {

		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.mypinner_dropdown, null);
		ListView listView = (ListView) layout.findViewById(R.id.listView);
		MyspinnerAdapter adapter = new MyspinnerAdapter(ClozeActivity.this,
				Opption_str);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(position);
		popupWindow.setWidth(position.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		popupWindow.showAsDropDown(position, 0, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// position.setBackgroundResource(R.drawable.preference_single_item);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				((TextView) position).setText(Opption_str[arg2]);
				popupWindow.dismiss();
				popupWindow = null;
				if (!Opption_str[arg2].equals("")) {
					user_select.put(arg2, Opption_str[arg2]);
				}
			}
		});

	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			Log.i("linshi", user_select.size() + "/" + cloze.getList().size());
			if (user_select.size() != cloze.getList().size()) {
				Toast.makeText(ClozeActivity.this, "请先完成本题!",
						Toast.LENGTH_SHORT).show();
			} else {
				if (Check) {
					Check = false;
					setCheckText("检查");
					propItem = 0;
					int type = 0;
					String answer_history = ExerciseBookTool
							.getAnswer_Json_history(path);
					try {
						if (status == 0) {
							Log.i("aaa", question_id + "");
							type = setAnswerJson(answer_history, user_select,
									question_id);
						} else {
							type = Again();
						}
						Log.i("aaa", type + "-type");
						switch (type) {// 0为下一小题 1为全部做完 2为本小题做完
						case 0:
							index += 1;
							SetAnswer();
							break;
						case 1:
							prodialog.show();
							if (Finish_Json()) {
								intent.putExtra("precision", ExerciseBookTool
										.getRatio(path, "cloze"));// 正确率100时获取精准成就
								intent.putExtra("use_time", getUseTime());// 用户使用的时间
								intent.putExtra("specified_time",
										specified_time);// 任务基础时间
								intent.setClass(ClozeActivity.this,
										WorkEndActivity.class);
								ClozeActivity.this.startActivityForResult(
										intent, 1);
							}
							break;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Check = true;
					setCheckText("下一个");
				}
			}
			break;
		case R.id.base_propTrue:
			Log.i("linshi", propItem + "----" + tv_list.size());
			if (propItem < tv_list.size()) {
				tv_list.get(propItem).setText(
						cloze.getList().get(propItem).getAnswer());
				tv_list.get(propItem).setTextColor(
						getResources().getColor(R.color.work_end));
				user_select.put(propItem, cloze.getList().get(propItem)
						.getAnswer());
				if (propItem + 1 == tv_list.size()) {
					Check = true;
					setCheckText("下一个");
				} else {
					propItem += 1;
				}
			}
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			Intent intent = new Intent();
			switch (resultCode) {
			case 0:
				ClozeActivity.this.finish();
				intent.setClass(ClozeActivity.this, HomeWorkIngActivity.class);
				startActivity(intent);
				break;
			case 1:
				index = 0;
				SetAnswer();
				break;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}