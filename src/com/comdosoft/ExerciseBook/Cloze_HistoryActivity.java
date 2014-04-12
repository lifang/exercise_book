package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.Branch_PoJo;
import com.comdosoft.ExerciseBook.pojo.ClozePojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.FlowLayout;
import com.comdosoft.ExerciseBook.tools.MyspinnerAdapter;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class Cloze_HistoryActivity extends AnswerBaseActivity implements
		Urlinterface, OnClickListener {

	public String path = "";
	public String sdpath = Environment.getExternalStorageDirectory() + "/";
	private String content = "";
	private FlowLayout myLayout;
	private String[] str;
	private int specified_time;
	private List<Branch_PoJo> Branchlist;
	private List<ClozePojo> list;
	private ClozePojo cloze;
	private int index = 0;
	private TextView spinner;
	private int select_item;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setPage(index + 1, list.size());
				setButtonOver();
				cloze = list.get(index);
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloze_history);
		setTimePropEnd();// 禁用道具
		setTruePropEnd();// 禁用道具
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		setType(1);
		setQuestionType(5);
		setCheckText("下一个");
		initialize();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		String json = intent.getStringExtra("json");
		Log.i("suanfa", path);
		SetJson(json);
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
		for (int i = 0; i < str.length; i++) {
			View view1;

			if (str[i].equals("[[sign]]")) {
				view1 = View.inflate(this, R.layout.text_spinner, null);
				spinner = (TextView) view1.findViewById(R.id.spinner);
				spinner.setText(cloze.getList().get(select_item).getAnswer());
				if (select_item + 1 < cloze.getList().size()) {
					select_item += 1;
				}
			} else {
				view1 = View.inflate(this, R.layout.cloze_view, null);
				TextView text = (TextView) view1.findViewById(R.id.tv);
				text.setText(str[i].toString());
			}
			myLayout.addView(view1);
		}
	}

	@SuppressWarnings("deprecation")
	public void showWindow(final View position, final String[] Opption_str) {

		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.mypinner_dropdown, null);
		ListView listView = (ListView) layout.findViewById(R.id.listView);
		MyspinnerAdapter adapter = new MyspinnerAdapter(
				Cloze_HistoryActivity.this, Opption_str);
		listView.setAdapter(adapter);
		PopupWindow popupWindow = new PopupWindow(position);
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
	}

	private void SetJson(String json) {
		list = new ArrayList<ClozePojo>();
		Log.i("suanfa", json);
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
									item.getString("options"),
									item.getString("answer"));
							Branchlist.add(tl);
						}

						ClozePojo lp = new ClozePojo(jo.getInt("id"),
								jo.getString("full_text"), Branchlist);
						Log.i("suanfa", "id--" + jo.getInt("id"));
						list.add(lp);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(Cloze_HistoryActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
			handler.sendEmptyMessage(0);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			if (index + 1 < list.size()) {
				index += 1;
				select_item = 0;
				nextRecord();
				handler.sendEmptyMessage(0);
			} else {
				MyDialog("没有更多历史记录了,点击确定退出!", 1);
			}
			break;
		}
	}

	public void setButtonOver() {
		if (index + 1 >= list.size()) {
			setCheckText("完成");
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MyDialog("确认退出吗？", 0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}