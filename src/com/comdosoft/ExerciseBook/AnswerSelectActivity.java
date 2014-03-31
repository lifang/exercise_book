package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;
import com.comdosoft.ExerciseBook.pojo.AnswerSelectItemPojo;
import com.comdosoft.ExerciseBook.tools.AnswerTools;

public class AnswerSelectActivity extends AnswerBaseActivity implements
		OnItemClickListener, OnClickListener {

	private String[] letterArr = new String[] { "A", "B", "C", "D", "E", "F" };
	private StringBuffer mAnswer = new StringBuffer();
	// private String json =
	// "{  \"selecting\": {\"specified_time\": \"100\", \"question_types\": \"6\", \"questions\": [{\"id\": \"284\",\"branch_questions\": [ {\"id\": \"181\", \"content\": \"This is ___ apple!\", \"option\": \"a;||;an\", \"answer\": \"an;||;a\" },{\"id\": \"181\", \"content\": \"<file>apple.jpg</file>Why he is ___ Google!\", \"option\": \"apple;||;banana;||;orange;||;pear\", \"answer\": \"apple;||;banana\"},{\"id\": \"181\", \"content\": \"<file>apple.mp3</file>\", \"option\": \"one;||;two;||;three\", \"answer\": \"two\"}, {\"id\": \"181\", \"content\": \"<file>apple.jpg</file>Pears have white flesh and thin green or yellow skin.\", \"option\": \"iPhone;||;S5;||;Xperia\", \"answer\": \"iPhone\"},{\"id\": \"181\", \"content\": \"Dad.come set here!\", \"option\": \"ZhangDaCa;||;ChenLong\", \"answer\": \"ZhangDaCa\"}]}]}}";
	private List<String> answerOption = new ArrayList<String>();
	private Map<Integer, String> checkMap = new HashMap<Integer, String>();
	private ListView listView;
	private AnswerSelectAdapter selectAdapter;
	public static List<AnswerSelectItemPojo> asipList = new ArrayList<AnswerSelectItemPojo>();
	private ImageView answerImg;
	private ImageView answerLaba;
	private TextView answerText;
	private LinearLayout answer_select_answerLinearLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.answer_select);
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_propTrue).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.answer_select_listview);
		answerImg = (ImageView) findViewById(R.id.answer_select_img);
		answerLaba = (ImageView) findViewById(R.id.answer_select_laba);
		answerText = (TextView) findViewById(R.id.answer_select_text);
		answer_select_answerLinearLayout = (LinearLayout) findViewById(R.id.answer_select_answerLinearLayout);

		setQuestionType(2);

		Log.i("aaa", "jsonselect--" + json);

		analysisJson(json);

		answerOption = mQuestList.get(mQindex).get(mBindex).getOption();
		selectAdapter = new AnswerSelectAdapter(this, answerOption);
		if (amp.getStatus() == 1 && status > 1) {
			selectAdapter.setOptionAndAnswerList(1, answerOption, mQuestList
					.get(mQindex).get(mBindex).getAnswer());
		} else {
			listView.setOnItemClickListener(this);
		}
		listView.setAdapter(selectAdapter);
		listView.setDividerHeight(10);

	}

	// 设置选择类型
	public void setSelectType(int i) {
		switch (i) {
		// 听力
		case 0:
			answer_select_answerLinearLayout.setVisibility(View.GONE);
			answerLaba.setVisibility(View.VISIBLE);
			break;
		// 文本
		case 1:
			answer_select_answerLinearLayout.setVisibility(View.VISIBLE);
			answerImg.setVisibility(View.GONE);
			answerLaba.setVisibility(View.GONE);
			break;
		// 图片
		case 2:
			answer_select_answerLinearLayout.setVisibility(View.VISIBLE);
			answerImg.setVisibility(View.VISIBLE);
			answerLaba.setVisibility(View.GONE);
			break;
		}
	}

	// 判断正确
	public int check() {
		int count = 0;
		List<String> arr = mQuestList.get(mQindex).get(mBindex).getAnswer();
		List<String> option = mQuestList.get(mQindex).get(mBindex).getOption();
		Iterator<Entry<Integer, String>> it = checkMap.entrySet().iterator();
		while (it.hasNext()) {
			String answer = it.next().getValue();
			// mAnswer.append(answer).append(" ");
			for (int i = 0; i < option.size(); i++) {
				if (answer.equals(option.get(i))) {
					mAnswer.append(letterArr[i]).append(" ");
				}
			}
			for (int j = 0; j < arr.size(); j++) {
				if (answer.equals(arr.get(j))) {
					count++;
				}
			}
		}
		mAnswer.delete(mAnswer.length() - 1, mAnswer.length());
		if (count == arr.size() && checkMap.size() == 1 && arr.size() == 1) {
			return 0;
		} else if (count == arr.size() && checkMap.size() > 1 && arr.size() > 1
				&& checkMap.size() == arr.size()) {
			return 0;
		} else if (count < arr.size() && arr.size() > 1) {
			return 1;
		} else if (count > arr.size()) {
			return 2;
		}
		return 2;
	}

	public void rightAnswer() {
		if (amp.getStatus() == 0) {
			AnswerBasePojo sp = mQuestList.get(mQindex).get(mBindex);
			List<String> option = sp.getOption();
			List<String> answer = sp.getAnswer();
			for (int i = 0; i < option.size(); i++) {
				boolean flag = false;
				AnswerSelectItemPojo asip = asipList.get(i);
				for (int j = 0; j < answer.size(); j++) {
					if (option.get(i).equals(answer.get(j))) {
						flag = true;
					}
				}
				if (flag) {
					asip.getLinearLayout().setBackgroundResource(
							R.drawable.answer_select_item_check_style);
					asip.setStatus(1);
					checkMap.put(i, answerOption.get(i));
				} else {
					asip.getLinearLayout().setBackgroundResource(
							R.drawable.answer_select_item_style);
					asip.setStatus(0);
					checkMap.remove(i);
				}
			}
		}
	}

	@Override
	public void updateView() {
		super.updateView();
		asipList.clear();
		setPage(mBindex + 1, mQuestList.get(mQindex).size());
		mAnswer.delete(0, mAnswer.length());
		AnswerBasePojo sp = mQuestList.get(mQindex).get(mBindex);
		answerOption = sp.getOption();

		if (amp.getStatus() == 1 && status > 1) {
			selectAdapter.setOptionAndAnswerList(1, sp.getOption(),
					sp.getAnswer());
		} else {
			selectAdapter.setOptionList(answerOption);
		}

		selectAdapter.notifyDataSetChanged();
		listView.setAdapter(selectAdapter);
		setSelectType(sp.getType());
		switch (sp.getType()) {
		case 0:
			break;
		case 1:
			answerText.setText(sp.getContent());
			break;
		case 2:
			answerText.setText(sp.getContent());
			break;
		}
		checkMap.clear();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_check_linearlayout:
			if (amp.getStatus() == 0) {
				if (checkMap.size() == 0) {
					Toast.makeText(getApplicationContext(), "请选择答案!", 0).show();
				} else {
					switch (check()) {
					case 0:
						ratio = 100;
						Toast.makeText(getApplicationContext(), "正确!", 0)
								.show();
						break;
					case 1:
						Toast.makeText(getApplicationContext(), "半对!", 0)
								.show();
						break;
					case 2:
						Toast.makeText(getApplicationContext(), "错误!", 0)
								.show();
						break;
					}
					if (status == 0) {
						AnswerBasePojo aop = mQuestList.get(mQindex).get(
								mBindex);
						saveAnswerJson(mAnswer.toString(), ratio,
								aop.getQuestions_id(),
								aop.getBranch_questions_id());
					} else {
						calculateIndexAndUpdateView();
					}
				}
			} else {
				nextRecord();
				calculateIndexAndUpdateView();
			}
			break;
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_propTrue:
			rightAnswer();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		AnswerSelectItemPojo asip = asipList.get(arg2);
		if (asip.getStatus() == 0) {
			asip.getLinearLayout().setBackgroundResource(
					R.drawable.answer_select_item_check_style);
			asip.setStatus(1);
			asipList.set(arg2, asip);
			checkMap.put(arg2, answerOption.get(arg2));
		} else {
			asip.getLinearLayout().setBackgroundResource(
					R.drawable.answer_select_item_style);
			asip.setStatus(0);
			asipList.set(arg2, asip);
			checkMap.remove(arg2);
		}
	}

	// 解析选择题JSON
	public void analysisJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			specified_time = jsonObject.getInt("specified_time");
			JSONArray jArr = new JSONArray(jsonObject.getString("questions"));
			for (int i = 0; i < jArr.length(); i++) {
				List<AnswerBasePojo> list = new ArrayList<AnswerBasePojo>();
				JSONObject jo = jArr.getJSONObject(i);
				int questions_id = jo.getInt("id");
				JSONArray jsonArr = new JSONArray(
						jo.getString("branch_questions"));
				for (int j = 0; j < jsonArr.length(); j++) {
					JSONObject jb = jsonArr.getJSONObject(j);
					String s = jb.getString("content");
					int branch_questions_id = jb.getInt("id");
					int type = AnswerTools.getSelectType(s);
					String content = AnswerTools.getSelectContent(s);
					List<String> arr = AnswerTools.getSelectOption(jb
							.getString("option"));
					List<String> answer = AnswerTools.getSelectAnswer(jb
							.getString("answer"));

					if (type != 0) {
						String path = AnswerTools.getSelectPath(s);
						String fileName = AnswerTools.getSelectPath(path);
						list.add(new AnswerBasePojo(questions_id,
								branch_questions_id, type, content, fileName,
								arr, answer));
					} else {
						list.add(new AnswerBasePojo(questions_id,
								branch_questions_id, type, content, arr, answer));
					}
				}
				mQuestList.add(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
