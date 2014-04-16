package com.comdosoft.ExerciseBook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
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
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.pojo.Branch_PoJo;
import com.comdosoft.ExerciseBook.pojo.ClozePojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.FlowLayout;
import com.comdosoft.ExerciseBook.tools.MyspinnerAdapter;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

/**
 * 作者: 张秀楠 时间：2014-4-11 下午5:22:00
 */
public class ClozeActivity extends AnswerBaseActivity implements Urlinterface,
		OnClickListener {

	private ExerciseBook eb;
	public String path = "";
	public String sdpath = Environment.getExternalStorageDirectory() + "/";
	private String content = "";
	private FlowLayout myLayout;
	private String[] str;
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
	private int select_item = 0;
	private int width;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.i("aaa", "---");
				if (eb.getTime_number() <= 0 || status == 1) {
					setTimePropEnd();// 禁用道具
				} else {
					setTruePropShow();
				}
				if (eb.getTrue_number() <= 0 || status == 1) {
					setTruePropEnd();// 禁用道具
				} else {
					setTruePropShow();
				}
				setPage(index + 1, list.size());
				myLayout.removeAllViews();
				content = cloze.getContent();
				content = ExerciseBookTool.del_tag(content);
				setTextView();
				break;
			case 1:
				break;
			case 2:
				break;
			}
		};
	};

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloze);

		eb = (ExerciseBook) getApplication();
		// 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
		super.mQuestionType = 5;
		setStart();
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_propTrue).setOnClickListener(this);
		findViewById(R.id.base_propTime).setOnClickListener(this);
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		gson = new Gson();
		initialize();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		String json = intent.getStringExtra("json");
		status = intent.getIntExtra("status", 1);
		Log.i("Max", status + "");

		Log.i("suanfa", eb.getTrue_number() + "/" + eb.getTime_number());
		SetJson(json);
		SetAnswer();
		setButtonOver();

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
					cloze = list.get(questions_item + 1);
					question_id = list.get(questions_item + 1).getId();
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
		myLayout = (FlowLayout) findViewById(R.id.myLayout);
	}

	private void setTextView() {
		content = content.replaceAll("\\[\\[sign\\]\\]", " [[sign]] ");
		Log.i("suanfa", content + "==");
		content = content.replaceAll("\\s", " ");
		Log.i("suanfa", content + "==");
		str = content.split(" ");
		user_select = new HashMap<Integer, String>();
		tv_list = new ArrayList<TextView>();
		for (int i = 0; i < str.length; i++) {
			View view1;
			if (str[i].equals("[[sign]]")) {
				view1 = View.inflate(this, R.layout.text_spinner, null);
				final TextView spinner = (TextView) view1
						.findViewById(R.id.spinner);
				String Opption = cloze.getList().get(select_item).getOpption();
				final String[] Opption_str = Opption.split(";\\|\\|;");
				final int item = select_item;
				spinner.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Log.i("suanfa", "item:" + item);
						showWindow(spinner, Opption_str, item);
					}
				});
				if (select_item + 1 < cloze.getList().size()) {
					select_item += 1;
				}
				tv_list.add(spinner);
			} else {
				view1 = View.inflate(this, R.layout.cloze_view, null);
				TextView text = (TextView) view1.findViewById(R.id.tv);
				text.setText(str[i].toString());
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
				specified_time = cloze.getInt("specified_time");
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
					Log.i("aaa", questions.length() + "--");
					for (int i = 0; i < questions.length(); i++) {
						JSONObject jo = questions.getJSONObject(i);
						JSONArray jsonarr = jo.getJSONArray("branch_questions");
						Log.i("aaa", 1 + "--");
						Branchlist = new ArrayList<Branch_PoJo>();
						for (int j = 0; j < jsonarr.length(); j++) {
							JSONObject item = jsonarr.getJSONObject(j);
							Log.i("aaa", item.getInt("id") + "--");
							Log.i("aaa", item.getString("answer") + "--");
							Log.i("aaa", item.getString("options") + "--");
							Branch_PoJo tl = new Branch_PoJo(item.getInt("id"),
									item.getString("options"),
									item.getString("answer"));
							Branchlist.add(tl);
						}
						Log.i("suanfa", "id--" + jo.getInt("id"));
						ClozePojo lp = new ClozePojo(jo.getInt("id"),
								jo.getString("full_text"), Branchlist);
						list.add(lp);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(ClozeActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 0为继续 1为全部做完 2为本小题做完
	public int setAnswerJson(String answer_history,
			Map<Integer, String> answer, int id) {
		int type = 0;
		answerJson = gson.fromJson(answer_history, AnswerJson.class);
		answerJson.update = ExerciseBookTool.getTimeIng();
		answerJson.cloze.setUpdate_time(ExerciseBookTool.getTimeIng());
		int q_item = Integer.valueOf(answerJson.cloze.getQuestions_item());
		int b_item = Integer.valueOf(answerJson.cloze.getBranch_item());
		Log.i("aaa", "--");

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
			answerJson.cloze
					.getQuestions()
					.get(q_item)
					.getBranch_questions()
					.add(new Branch_AnswerPoJo(cloze.getList()
							.get(entry.getKey()).getId()
							+ "", entry.getValue(), ratio + ""));
			calculateRatio(ratio);
		}
		Log.i("aaa", "an:" + q_item + "/" + list.size());
		if (q_item + 1 == list.size()) {// 结束
			answerJson.cloze.setStatus("1");
			type = 1;
		}
		String str = gson.toJson(answerJson);
		ExerciseBookTool.writeFile(path, str);
		return type;
	}

	private int Again() {
		int type = 0;
		if (index + 1 == list.size()) {// 结束
			type = 1;
		}
		for (Map.Entry<Integer, String> entry : user_select.entrySet()) {
			int ratio = 0;
			Log.i("suanfa",
					entry.getValue() + "-"
							+ cloze.getList().get(entry.getKey()).getAnswer());
			if (entry.getValue().equals(
					cloze.getList().get(entry.getKey()).getAnswer())) {
				ratio = 100;
			}
			calculateRatio(ratio);
		}
		return type;
	}

	@SuppressWarnings("deprecation")
	public void showWindow(final View position, final String[] Opption_str,
			final int item) {

		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.mypinner_dropdown, null);
		ListView listView = (ListView) layout.findViewById(R.id.listView);
		MyspinnerAdapter adapter = new MyspinnerAdapter(ClozeActivity.this,
				Opption_str);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(position);
		if (width == 1200) {
			popupWindow.setWidth(400);
		} else {
			popupWindow.setWidth(250);
		}
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		popupWindow.showAsDropDown(position, 0, 0);
		// popupWindow.setOnDismissListener(new OnDismissListener() {
		// public void onDismiss() {
		// position.setBackgroundResource(R.drawable.preference_single_item);
		// }
		// });
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				((TextView) position).setText(Opption_str[arg2]);
				popupWindow.dismiss();
				popupWindow = null;
				if (!Opption_str[arg2].equals("")) {
					user_select.put(item, Opption_str[arg2]);
				}
			}
		});

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			Log.i("linshi", user_select.size() + "/" + cloze.getList().size());
			if (user_select.size() != cloze.getList().size()) {
				Toast.makeText(ClozeActivity.this, "还有未填的选项哦!",
						Toast.LENGTH_SHORT).show();
			} else {
				if (Check) {
					Check = false;
					setStart();
					setCheckText("检查");
					propItem = 0;
					int type = 0;
					String answer_history = ExerciseBookTool
							.getAnswer_Json_history(path);
					Log.i("aaa", 1 + "-error");
					try {
						if (status == 0) {
							Log.i("aaa", question_id + "");
							type = setAnswerJson(answer_history, user_select,
									question_id);
						} else {
							Log.i("aaa", 2 + "-error");
							type = Again();
							Log.i("aaa", type + "error-type");
						}
						Log.i("aaa", type + "-type");
						switch (type) {// 0为下一小题 1为全部做完 2为本小题做完
						case 0:
							index += 1;
							select_item = 0;
							SetAnswer();
							break;
						case 1:
							roundOver();
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Check = true;
					setPause();
					setCheckText("下一题");
					setButtonOver();
					int true_number = 0;
					for (Map.Entry<Integer, String> entry : user_select
							.entrySet()) {
						tv_list.get(entry.getKey()).setTextColor(
								Color.rgb(227, 20, 39));
						if (entry.getValue()
								.equals(cloze.getList().get(entry.getKey())
										.getAnswer())) {
							tv_list.get(entry.getKey()).setTextColor(
									getResources().getColor(R.color.work_end));
							true_number += 1;
						}
					}
					Log.i("suanfa", true_number + ",number");
					if (true_number == cloze.getList().size()) {
						MyPlayer(true);
					} else {
						MyPlayer(false);
					}
				}
			}
			break;
		case R.id.base_propTrue:
			if (eb.getTrue_number() > 0) {// 判断显示答案的道具数量是否大于0
				if (propItem < tv_list.size()) {
					PropJson(1, cloze.getList().get(propItem).getId());// 1道具类型
																		// --0显示答案
																		// 1时间
																		// ,
																		// 2--小题id
																		// ,3--任务类型
					tv_list.get(propItem).setText(
							cloze.getList().get(propItem).getAnswer());
					tv_list.get(propItem).setTextColor(
							getResources().getColor(R.color.work_end));
					user_select.put(propItem, cloze.getList().get(propItem)
							.getAnswer());
					if (propItem + 1 == tv_list.size()) {
						Check = true;
						setButtonOver();
						setTruePropEnd();// 禁用道具
					} else {
						propItem += 1;
					}
				}
			} else {
				Toast.makeText(ClozeActivity.this, R.string.prop_number_error,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.base_propTime:
			if (eb.getTime_number() > 0) {// 判断显示答案的道具数量是否大于0
				// 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
				PropJson(0, cloze.getList().get(propItem).getId());// 1道具类型
																	// --0显示答案
																	// 1时间 ,
																	// 2--小题id
																	// ,3--任务类型
			} else {
				Toast.makeText(ClozeActivity.this, R.string.prop_number_error,
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	public void setButtonOver() {
		if (index + 1 >= list.size()) {
			setCheckText("完成");
		} else {
			setCheckText("下一题");
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			Intent intent = new Intent();
			switch (resultCode) {
			case 0:
				if (eb.getActivity_item() == 0) {
					intent.setClass(ClozeActivity.this,
							HomeWorkIngActivity.class);
				} else {
					intent.setClass(ClozeActivity.this,
							RecordMainActivity.class);
				}
				startActivity(intent);
				ClozeActivity.this.finish();
				break;
			case 1:
				index = 0;
				select_item = 0;
				status = 1;
				setTimePropEnd();// 禁用道具
				setTruePropEnd();// 禁用道具
				user_select.clear();
				SetAnswer();
				break;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}