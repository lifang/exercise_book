package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;
import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.AnswerMyPojo;
import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.google.gson.Gson;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

// 答题父类
public class AnswerBaseActivity extends Activity implements OnClickListener {

	public int mQindex = 0;
	public int mBindex = 0;
	private int mRecordIndex = 0;
	private int mQuestionType = 0;
	private int second;
	private int type = 0;
	public int ratio = 0;
	private boolean flag = true;
	private String[] answerArr = new String[] { "你的搭配: ", "你的选择: ", "你的排序: ",
			"你的作答: " };
	private String[] questionArr = new String[] { "selecting", "sort",
			"lining", "listening" };

	private List<String> mRecoirdAnswer = new ArrayList<String>();
	private List<Integer> mRecoirdRatio = new ArrayList<Integer>();
	public List<List<AnswerBasePojo>> mQuestList = new ArrayList<List<AnswerBasePojo>>();

	private LinearLayout middleLayout;
	private LinearLayout base_history_linearlayout;
	private LinearLayout base_time_linearlayout;
	private LinearLayout base_answer_linearlayout;
	private TextView page;
	private TextView check;
	private TextView timeText;
	private TextView useTimeText;
	private TextView accuracyText;
	private TextView base_answer_text;
	private ImageView propTrue;
	private ImageView propTime;

	public AnswerMyPojo amp;
	private Gson gson = new Gson();
	private AnswerJson answerJson;
	private Thread mTimer = new Timer();
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				timeText.setText(timeSecondToString(second));
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.answer_base);
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		middleLayout = (LinearLayout) findViewById(R.id.base_LinearLayout);
		base_time_linearlayout = (LinearLayout) findViewById(R.id.base_time_linearlayout);
		base_history_linearlayout = (LinearLayout) findViewById(R.id.base_history_linearlayout);
		base_answer_linearlayout = (LinearLayout) findViewById(R.id.base_answer_Linearlayout);
		propTrue = (ImageView) findViewById(R.id.base_propTrue);
		propTime = (ImageView) findViewById(R.id.base_propTime);
		page = (TextView) findViewById(R.id.base_page);
		check = (TextView) findViewById(R.id.base_checkText);
		timeText = (TextView) findViewById(R.id.base_timeText);
		accuracyText = (TextView) findViewById(R.id.base_accuracyText);
		useTimeText = (TextView) findViewById(R.id.base_useTimeText);
		base_answer_text = (TextView) findViewById(R.id.base_answer_text);

	}

	// 设置子布局View
	public void setContentView(int layoutId) {
		View middleView = getLayoutInflater().inflate(layoutId, null);
		if (null != middleLayout) {
			middleLayout.removeAllViews();
			middleLayout.addView(middleView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
	}

	// 设置题目类型
	public void setQuestionType(int type) {
		this.mQuestionType = type;
		amp = getAnswerItem(
				ExerciseBookTool
						.getAnswer_Json_history("/sdcard/Exercisebook_app/73/85/130/answer.js"),
				questionArr[type]);

		mQindex = amp.getQuestions_item();
		mBindex = amp.getBranch_item();

		setUseTime(amp.getUse_time());

		if (amp.getStatus() == 1) {
			setType(1);
			mRecoirdAnswer = amp.getAnswer();
			mRecoirdRatio = amp.getRatio();
			nextRecord();
		} else {
			setType(0);
		}
	}

	// 设置答题|记录 type 0答题 1历史
	public void setType(int type) {
		this.type = type;
		if (type == 0) {
			mTimer.start();
			base_time_linearlayout.setVisibility(View.VISIBLE);
			base_history_linearlayout.setVisibility(View.GONE);
			base_answer_linearlayout.setVisibility(View.GONE);
		} else {
			check.setText("下一个");
			propTrue.setImageResource(R.drawable.base_prop3);
			propTime.setImageResource(R.drawable.base_prop4);
			base_time_linearlayout.setVisibility(View.GONE);
			base_history_linearlayout.setVisibility(View.VISIBLE);
			base_answer_linearlayout.setVisibility(View.VISIBLE);
		}
	}

	// 获取时间(秒)
	public int getUseTime() {
		return second;
	}

	// 设置时间
	public void setUseTime(int s) {
		second = s;
		mHandler.sendEmptyMessage(1);
	}

	// 获取检查按钮文本
	public String getCheck() {
		return check.getText().toString();
	}

	// 设置检查按钮文本
	public void setCheck(String text) {
		check.setText(text);
	}

	// 设置页码
	public void setPage(int current, int size) {
		page.setText(current + "/" + size);
	}

	// 设置答题记录 正确率和使用时间
	public void setAccuracyAndUseTime(int accuracy, int useTime) {
		accuracyText.setText(accuracy + "%");
		useTimeText.setText(timeSecondToString(useTime));
	}

	// 设置自己的答案
	public void setMyAnswer(String s, int i) {
		base_answer_text.setText(answerArr[i] + s);
	}

	// 时间道具使用完
	public void setTimePropEnd() {
		propTime.setImageResource(R.drawable.base_prop4);
		propTime.setClickable(false);
	}

	// 正确道具使用完
	public void setTruePropEnd() {
		propTrue.setImageResource(R.drawable.base_prop3);
		propTrue.setClickable(false);
	}

	// 时间int秒数转string
	public String timeSecondToString(int t) {
		StringBuffer sb = new StringBuffer();
		if (t >= 60) {
			sb.append(t / 60);
			sb.append("'");
			if (t % 60 < 10) {
				sb.append(0);
			}
			sb.append(t % 60);
		} else {
			sb.append(t);
		}
		return sb.toString();
	}

	// 暂停计时
	public void setPause() {
		flag = false;
	}

	// 继续计时
	public void setStart() {
		flag = true;
		// 如果线程还没有开始过，就调用start。如果已经开始了，用start就会出错了
		if (mTimer.getState() == Thread.State.NEW) {
			mTimer.start();
		} else if (mTimer.getState() == Thread.State.TERMINATED) {
			// 如果线程已经被interrupt了，那么它的状态就是terminated。即这个线程完整的结束了。
			// 这时候就重新new一个线程，再start
			try {
				mTimer = new Timer();
				mTimer.start();
			} catch (Exception e) {
			}
		}
	}

	// 计时器线程
	class Timer extends Thread {
		@Override
		public void run() {
			try {
				while (flag) {
					Thread.sleep(1000);
					second++;
					mHandler.sendEmptyMessage(1);
				}
				if (!flag)
					this.interrupt();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			super.run();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			Intent inent = new Intent();
			if (type == 0) {
				inent.setClass(this, HomeWorkIngActivity.class);
			} else {
				inent.setClass(this, HomeWorkIngActivity.class);
			}
			startActivity(inent);
			break;
		}
	}

	// 切换下一记录
	public void nextRecord() {
		setAccuracyAndUseTime(mRecoirdRatio.get(mRecordIndex),
				amp.getUse_time());
		setMyAnswer(mRecoirdAnswer.get(mRecordIndex), 1);
		if (mRecordIndex < mRecoirdAnswer.size() - 1) {
			mRecordIndex++;
		}
	}

	// 计算索引&更新View
	public void calculateIndexAndUpdateView() {
		if (mQindex == mQuestList.size() - 1
				&& mBindex == mQuestList.get(mQindex).size() - 1) {
		} else if (mQindex < mQuestList.size()
				&& mBindex < mQuestList.get(mQindex).size()) {
			if (++mBindex >= mQuestList.get(mQindex).size()
					&& mQindex < mQuestList.size() - 1) {
				mBindex = 0;
				mQindex++;
			}
			if (mBindex < mQuestList.get(mQindex).size()) {
				updateView();
			}
		}
	}

	public void updateView() {
	}

	public AnswerPojo getAnswerPojo() {
		switch (mQuestionType) {
		case 0:
			return answerJson.selecting;
		case 1:
			return answerJson.sort;
		case 2:
			return answerJson.lining;
		case 3:
			return answerJson.listening;
		}
		return null;
	}

	// 保存答题记录
	public void saveAnswerJson(String answer, int ratio, int qid, int bid) {
		String answer_history = ExerciseBookTool
				.getAnswer_Json_history("/sdcard/Exercisebook_app/73/85/130/answer.js");
		answerJson = gson.fromJson(answer_history, AnswerJson.class);
		AnswerPojo ap = getAnswerPojo();
		ap.setUpdate_time("2014-03-17 08:00:00");
		ap.setUse_time(getUseTime() + "");

		if (ap.getQuestions().size() == 0) {
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(qid + "",
					new ArrayList<Branch_AnswerPoJo>());
			ap.getQuestions().add(aq);
		}

		ap.getQuestions().get(mQindex).getBranch_questions()
				.add(new Branch_AnswerPoJo(bid + "", answer, ratio + ""));

		if (mQindex == mQuestList.size() - 1
				&& mBindex == mQuestList.get(mQindex).size() - 1) {// 结束
			ap.setStatus("1");
		} else if (mBindex == mQuestList.get(mQindex).size() - 1) {// 本小题做完
			ap.setQuestions_item(mQindex + 1 + "");
			ap.setBranch_item("0");
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(mQuestList
					.get(mQindex + 1).get(0).getQuestions_id()
					+ "", new ArrayList<Branch_AnswerPoJo>());
			ap.getQuestions().add(aq);
		}

		calculateIndexAndUpdateView();

		ap.setQuestions_item(mQindex + "");
		ap.setBranch_item(mBindex + "");

		String str = gson.toJson(answerJson);
		this.ratio = 0;
		try {
			ExerciseBookTool.writeFile(
					"/sdcard/Exercisebook_app/73/85/130/answer.js", str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取答题记录对象
	public AnswerMyPojo getAnswerItem(String json, String type) {
		try {
			if (json != "") {
				JSONObject obj = new JSONObject(json);
				JSONObject jo = obj.getJSONObject(type);
				int status = jo.getInt("status");
				int use_time = jo.getInt("use_time");
				if (status == 0) {
					int questions_item = jo.getInt("questions_item");
					int branch_item = jo.getInt("branch_item");
					questions_item = questions_item == -1 ? 0 : questions_item;
					branch_item = branch_item == -1 ? 0 : branch_item;
					return new AnswerMyPojo(questions_item, branch_item,
							use_time, status);
				} else {
					List<String> answer = new ArrayList<String>();
					List<Integer> ratio = new ArrayList<Integer>();
					JSONArray ja = jo.getJSONArray("questions");
					for (int i = 0; i < ja.length(); i++) {
						JSONArray jArr = ja.getJSONObject(i).getJSONArray(
								"branch_questions");
						for (int j = 0; j < jArr.length(); j++) {
							answer.add(jArr.getJSONObject(j)
									.getString("answer"));
							ratio.add(jArr.getJSONObject(j).getInt("ratio"));
						}
					}
					return new AnswerMyPojo(status, use_time, answer, ratio);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
