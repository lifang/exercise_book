package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;

/**
 * @作者 马龙
 * @时间 2014-4-10 上午10:28:58
 */
public class AnswerOrderActivity extends AnswerBaseActivity {

	// private String json =
	// "{ \"sort\": {\"specified_time\" :\"100\", \"questions\": [{\"id\": \"284\", \"branch_questions\": [ {\"id\": \"181\", \"content\": \"This is an apple\"},{\"id\": \"182\", \"content\": \"2 3 4 5\"},{\"id\": \"183\", \"content\": \"This is orange\"}]  }, {\"id\": \"285\", \"branch_questions\": [ {\"id\": \"184\", \"content\": \"why is Google\"},{\"id\": \"185\", \"content\": \"1 2 3\"},{\"id\": \"186\", \"content\": \"How are you\"} ] }]}}";
	private int mAnswerIndex = 0;
	private int mSubjectIndex = 0;
	private int mOptionIndex = 0;
	private String[] answerArr = new String[] {};
	private String mAnswerStr;
	private StringBuffer mAnswer = new StringBuffer();
	private List<String> mAnswerList = new ArrayList<String>();
	private List<String> answerList = new ArrayList<String>();
	private List<LinearLayout> mSubjectList = new ArrayList<LinearLayout>();
	private List<EditText> mSubjectEditList = new ArrayList<EditText>();
	private List<LinearLayout> mOptionList = new ArrayList<LinearLayout>();
	private List<TextView> mOptionTextList = new ArrayList<TextView>();
	private List<TextView> mOptionOrderTextList = new ArrayList<TextView>();

	private TextView answer_order_back;
	private TextView answer_order_again;
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
		answer_order_back = (TextView) findViewById(R.id.answer_order_back);
		answer_order_again = (TextView) findViewById(R.id.answer_order_again);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(
				new MyOnClick());
		answer_order_again.setOnClickListener(new MyOnClick());
		answer_order_back.setOnClickListener(new MyOnClick());

		setQuestionType(6);

		etlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		etlp.leftMargin = 20;
		layoutlp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		layoutlp.topMargin = 20;

		analysisJSON(json);

		updateView();
	}

	public void rightAnswer() {
		for (int i = 0; i < mSubjectEditList.size(); i++) {
			EditText et = mSubjectEditList.get(i);
			et.setText(answerArr[i]);
			et.setBackgroundResource(R.drawable.answer_wire_item_check_style);

			TextView tv = mOptionTextList.get(i);
			tv.setBackgroundResource(R.drawable.answer_order_item_check_style);

			if (status != 2) {
				mOptionOrderTextList.add(tv);
				mAnswerList.add(answerArr[i]);
			} else {
				tv.setClickable(false);
			}
		}
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
		et.setWidth(240);
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
			JSONObject jsonObject = new JSONObject(json);
			specified_time = jsonObject.getInt("specified_time");
			JSONArray jArr = new JSONArray(jsonObject.getString("questions"));
			for (int i = 0; i < jArr.length(); i++) {
				List<AnswerBasePojo> mBranchList = new ArrayList<AnswerBasePojo>();
				JSONObject jo = jArr.getJSONObject(i);
				int question_id = jo.getInt("id");
				JSONArray jsonArr = new JSONArray(
						jo.getString("branch_questions"));
				for (int j = 0; j < jsonArr.length(); j++) {
					JSONObject jb = jsonArr.getJSONObject(j);
					int branch_question_id = jb.getInt("id");
					String answer = jb.getString("content");
					mBranchList.add(new AnswerBasePojo(question_id,
							branch_question_id, answer));
				}
				mQuestList.add(mBranchList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void updateView() {
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

		if (status == 2) {
			answer_order_back.setVisibility(View.GONE);
			answer_order_again.setVisibility(View.GONE);
		}

		answerArr = mQuestList.get(mQindex).get(mBindex).getContent()
				.split(" ");

		for (int i = 0; i < answerArr.length; i++) {
			mAnswer.append(answerArr[i]);
			answerList.add(answerArr[i]);
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

		if (status == 2) {
			rightAnswer();
		}
	}

	public void check() {
		StringBuffer sb = new StringBuffer();
		StringBuffer mSb = new StringBuffer();
		for (int i = 0; i < mAnswerList.size(); i++) {
			sb.append(mAnswerList.get(i)).append(";||;");
			mSb.append(mAnswerList.get(i));
			if (mAnswerList.get(i).equals(answerArr[i])) {
				mSubjectEditList.get(i).setTextColor(Color.rgb(53, 207, 143));
			} else {
				mSubjectEditList.get(i).setTextColor((Color.rgb(227, 20, 39)));
			}
		}
		mAnswerStr = sb.toString();
		mAnswerStr = mAnswerStr.substring(0, mAnswerStr.length() - 4);
		if (mAnswer.toString().equals(mSb.toString())) {
			ratio = 100;
			MyPlayer(true);
		} else {
			MyPlayer(false);
		}
	}

	// 后退一步
	public void backAnswer(int i) {
		EditText edit = mSubjectEditList.get(i);
		edit.setBackgroundResource(R.drawable.answer_order_item_style);
		edit.setText("");
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
					// et.setBackgroundResource(R.drawable.answer_wire_item_check_style);

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
				if (status != 2) {
					if (getCheckText().equals("检查")) {
						if (mAnswerList.size() == answerList.size()) {
							if (mQindex == mQuestList.size() - 1
									&& mBindex == mQuestList.get(mQindex)
											.size() - 1) {
								setCheckText("完成");
							} else {
								setCheckText("下一题");
							}
							check();
						} else {
							Toast.makeText(getApplicationContext(),
									"请完成未选择的题!", 0).show();
						}
					} else {
						if (mQindex == mQuestList.size() - 1
								&& mBindex == mQuestList.get(mQindex).size() - 1) {
							setCheckText("完成");
						} else {
							setCheckText("检查");
						}
						if (status == 0) {
							AnswerBasePojo aop = mQuestList.get(mQindex).get(
									mBindex);
							saveAnswerJson(mAnswerStr, ratio,
									aop.getQuestions_id(),
									aop.getBranch_questions_id());
						} else {
							calculateIndexAndUpdateView();
						}
					}
				} else {
					calculateIndexAndUpdateView();
					nextRecord();
				}
				break;
			}
		}
	}

}
