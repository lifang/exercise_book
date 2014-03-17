package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.AnswerOrderPojo;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.google.gson.Gson;

public class AnswerOrderActivity extends AnswerBaseActivity {

	private String json = "{ \"sort\": {\"specified_time\" :\"100\", \"questions\": [{\"id\": \"284\", \"branch_questions\": [ {\"id\": \"181\", \"content\": \"This is an apple\"},{\"id\": \"185\", \"content\": \"2 3 4 5\"},{\"id\": \"182\", \"content\": \"This is apple\"}]  }, {\"id\": \"285\", \"branch_questions\": [ {\"id\": \"183\", \"content\": \"why is Google\"},{\"id\": \"186\", \"content\": \"1 2 3\"},{\"id\": \"184\", \"content\": \"How are you\"} ] }]}}";
	private int mAnswerIndex = 0;
	private int mSubjectIndex = 0;
	private int mOptionIndex = 0;
	private int specified_time = 0;
	private int mQindex = 0;
	private int mBindex = 0;
	private String mAnswerStr;
	private StringBuffer mAnswer = new StringBuffer();
	private List<List<AnswerOrderPojo>> mQuestList = new ArrayList<List<AnswerOrderPojo>>();
	private List<String> mAnswerList = new ArrayList<String>();
	private List<String> answerList = new ArrayList<String>();
	private List<LinearLayout> mSubjectList = new ArrayList<LinearLayout>();
	private List<EditText> mSubjectEditList = new ArrayList<EditText>();
	private List<LinearLayout> mOptionList = new ArrayList<LinearLayout>();
	private List<TextView> mOptionTextList = new ArrayList<TextView>();
	private List<TextView> mOptionOrderTextList = new ArrayList<TextView>();

	private Gson gson;
	private int ratio = 0;
	private AnswerJson answerJson;

	private LayoutParams etlp;
	private LayoutParams layoutlp;
	private LinearLayout mSubjectLinearLayout;
	private LinearLayout mOptionLinearLayout;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.answer_order);
		mSubjectLinearLayout = (LinearLayout) findViewById(R.id.answer_order_subject_linearLayout);
		mOptionLinearLayout = (LinearLayout) findViewById(R.id.answer_order_option_linearLayout);

		findViewById(R.id.answer_order_back)
				.setOnClickListener(new MyOnClick());
		findViewById(R.id.answer_order_again).setOnClickListener(
				new MyOnClick());
		findViewById(R.id.base_check_linearlayout).setOnClickListener(
				new MyOnClick());

		etlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		etlp.leftMargin = 20;
		layoutlp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		layoutlp.topMargin = 20;

		gson = new Gson();

		Intent intent = getIntent();
		json = intent.getStringExtra("json");

		analysisJSON(json);

		initData();
	}

	public void initView(int i) {
		if (i == 0 || i % 3 == 0) {
			LinearLayout layout = new LinearLayout(getApplicationContext());
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setGravity(Gravity.CENTER);
			layout.setLayoutParams(layoutlp);
			mSubjectList.add(layout);

			LinearLayout optionLayout = new LinearLayout(
					getApplicationContext());
			optionLayout.setOrientation(LinearLayout.HORIZONTAL);
			optionLayout.setGravity(Gravity.CENTER);
			optionLayout.setLayoutParams(layoutlp);
			mOptionList.add(optionLayout);
			if (i > 0) {
				mSubjectIndex++;
				mOptionIndex++;
			}
		}

		EditText et = new EditText(this);
		et.setWidth(257);
		et.setHeight(97);
		et.setSingleLine(true);
		et.setFocusable(false);
		et.setFocusableInTouchMode(false);
		et.setLayoutParams(etlp);
		et.setGravity(Gravity.CENTER);
		et.setTextSize(28);
		et.setBackgroundResource(R.drawable.answer_order_item_style);
		mSubjectEditList.add(et);
		mSubjectList.get(mSubjectIndex).addView(et);

		TextView tv = new TextView(this);
		tv.setId(R.id.aa);
		tv.setText(answerList.get(i));
		tv.setWidth(216);
		tv.setHeight(82);
		tv.setLayoutParams(etlp);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.rgb(255, 255, 255));
		tv.setTextSize(28);
		tv.setBackgroundResource(R.drawable.answer_wire_item_style);
		tv.setOnClickListener(new MyOnClick(i));
		mOptionTextList.add(tv);
		mOptionList.get(mOptionIndex).addView(tv);
	}

	public void analysisJSON(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json).getJSONObject("sort");
			specified_time = jsonObject.getInt("specified_time");
			JSONArray jArr = new JSONArray(jsonObject.getString("questions"));
			for (int i = 0; i < jArr.length(); i++) {
				List<AnswerOrderPojo> mBranchList = new ArrayList<AnswerOrderPojo>();
				JSONObject jo = jArr.getJSONObject(i);
				int question_id = jo.getInt("id");
				JSONArray jsonArr = new JSONArray(
						jo.getString("branch_questions"));
				for (int j = 0; j < jsonArr.length(); j++) {
					JSONObject jb = jsonArr.getJSONObject(j);
					int branch_question_id = jb.getInt("id");
					String answer = jb.getString("content");
					mBranchList.add(new AnswerOrderPojo(question_id,
							branch_question_id, answer));
				}
				mQuestList.add(mBranchList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void initData() {
		mAnswerList.clear();
		answerList.clear();
		mSubjectList.clear();
		mOptionList.clear();
		mSubjectEditList.clear();
		mOptionTextList.clear();

		mSubjectLinearLayout.removeAllViews();
		mOptionLinearLayout.removeAllViews();
		mAnswer.delete(0, mAnswer.length());
		mAnswerIndex = 0;
		mSubjectIndex = 0;
		mOptionIndex = 0;

		String[] arr = mQuestList.get(mQindex).get(mBindex).getAnswer()
				.split(" ");
		for (int i = 0; i < arr.length; i++) {
			mAnswer.append(arr[i]);
			answerList.add(arr[i]);
		}

		List<String> list = new ArrayList<String>();
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < answerList.size(); i++) {
			int index = r.nextInt(answerList.size());
			list.add(answerList.get(index));
			answerList.remove(index);
			i--;
		}
		answerList = list;

		for (int i = 0; i < answerList.size(); i++) {
			initView(i);
		}

		for (int i = 0; i < mSubjectList.size(); i++) {
			mSubjectLinearLayout.addView(mSubjectList.get(i));
			mOptionLinearLayout.addView(mOptionList.get(i));
		}
	}

	public void check() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mAnswerList.size(); i++) {
			sb.append(mAnswerList.get(i));
		}
		mAnswerStr = sb.toString();
		if (mAnswer.toString().equals(mAnswerStr)) {
			Toast.makeText(getApplicationContext(), "true", 0).show();
		} else {
			Toast.makeText(getApplicationContext(), "false", 0).show();
		}
	}

	class MyOnClick implements OnClickListener {

		private int index;

		public MyOnClick() {
		}

		public MyOnClick(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.aa:
				if (mAnswerIndex < answerList.size()) {
					EditText et = mSubjectEditList.get(mAnswerIndex++);
					String text = answerList.get(index);
					et.setText(text);
					et.setBackgroundResource(R.drawable.answer_wire_item_check_style);

					TextView tv = mOptionTextList.get(index);
					tv.setClickable(false);
					tv.setBackgroundResource(R.drawable.answer_order_item_check_style);
					mOptionOrderTextList.add(tv);
					mAnswerList.add(text);
				}
				break;
			case R.id.answer_order_back:
				if (mAnswerIndex > 0) {
					backAnswer(--mAnswerIndex);
					mAnswerList.remove(mAnswerIndex);
					int size = mOptionOrderTextList.size() - 1;
					TextView tv = mOptionOrderTextList.get(size);
					tv.setBackgroundResource(R.drawable.answer_wire_item_style);
					tv.setClickable(true);
					mOptionOrderTextList.remove(size);
				}
				break;
			case R.id.answer_order_again:
				mAnswerIndex = 0;
				mAnswerList.clear();
				mOptionOrderTextList.clear();
				for (int i = 0; i < mSubjectEditList.size(); i++) {
					backAnswer(i);
					mOptionTextList.get(i).setBackgroundResource(
							R.drawable.answer_wire_item_style);
					mOptionTextList.get(i).setClickable(true);
				}
				break;
			case R.id.base_check_linearlayout:
				check();
				if (mQindex == mQuestList.size() - 1
						&& mBindex == mQuestList.get(mQindex).size() - 1) {
					Log.i("Ax", "last");
				}
				if (mQindex < mQuestList.size()
						&& mBindex < mQuestList.get(mQindex).size()) {
					if (++mBindex >= mQuestList.get(mQindex).size()
							&& mQindex < mQuestList.size() - 1) {
						mBindex = 0;
						mQindex++;
					}
					if (mBindex < mQuestList.get(mQindex).size()) {
						// AnswerOrderPojo aop = mQuestList.get(mQindex).get(
						// mBindex);
						// setAnswerJson(mAnswerStr, 80, aop.getQuestion_id(),
						// aop.getBranch_questionid());
						initData();
						Log.i("Ax", mQindex + "--" + mBindex);
					}
				}
				break;
			}
		}
	}

	public void backAnswer(int i) {
		EditText edit = mSubjectEditList.get(i);
		edit.setBackgroundResource(R.drawable.answer_order_item_style);
		edit.setText("");
	}

	// 0为继续 1为全部做完 2为本小题做完
	public int setAnswerJson(String answer, int ratio, int qid, int bid) {
		int type = 0;
		String answer_history = ExerciseBookTool
				.getAnswer_Json_history("/sdcard/Exercisebook_app/73/85/130/answer.js");
		Log.i("Ax", "json--" + answer_history);
		answerJson = gson.fromJson(answer_history, AnswerJson.class);
		answerJson.reading.setUpdate_time("2014-03-12 08:00:00");
		// int q_item = Integer.valueOf(answerJson.reading.getQuestions_item());
		// int b_item = Integer.valueOf(answerJson.reading.getBranch_item());

		answerJson.reading.setQuestions_item(mQindex + "");
		answerJson.reading.setBranch_item(mBindex + "");
		answerJson.reading.setUse_time(getSecond() + "");

		if (answerJson.reading.getQuestions().size() == 0) {
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(qid + "",
					new ArrayList<Branch_AnswerPoJo>());
			answerJson.reading.getQuestions().add(aq);
		}

		answerJson.reading.getQuestions().get(mQindex).getBranch_questions()
				.add(new Branch_AnswerPoJo(bid + "", answer, ratio + ""));

		if (mQindex == mQuestList.size() - 1
				&& mBindex == mQuestList.get(mQindex).size() - 1) {// 结束
			answerJson.reading.setStatus("1");
			type = 1;
		} else if (mBindex == mQuestList.get(mQindex).size() - 1) {// 本小题做完
			answerJson.reading.setQuestions_item(mQindex + 1 + "");
			answerJson.reading.setBranch_item("0");
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(mQuestList
					.get(mQindex + 1).get(0).getQuestion_id()
					+ "", new ArrayList<Branch_AnswerPoJo>());
			answerJson.reading.getQuestions().add(aq);
			type = 2;
		}

		String str = gson.toJson(answerJson);
		try {
			ExerciseBookTool.writeFile(
					"/sdcard/Exercisebook_app/73/85/130/answer.js", str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return type;
	}

}
