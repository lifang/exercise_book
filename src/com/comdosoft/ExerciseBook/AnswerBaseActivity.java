package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;
import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.AnswerMyPojo;
import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

// 答题父类
public class AnswerBaseActivity extends Activity implements OnClickListener,
		Urlinterface {
	public ExerciseBook eb;
	public int mQindex = 0;
	public int mBindex = 0;
	public int mRecordIndex = 0;
	public int ratio = 0;
	public int status = 0;
	public int specified_time = 0;
	private int mQuestionType = 0;
	private int mRatio = 0;
	private int second;
	public int type = 0;
	private boolean flag = true;
	private String recordMes;
	public String json;
	public String path;
	private String[] answerArr = new String[] { "你的作答: ", " ", "你的选择: ",
			"你的选择: ", "你的搭配: ", "你的选择: ", "你的排序: " };
	private String[] questionArr = new String[] { "listening", "reading",
			"time_limit", "selecting", "lining", "cloze", "sort" };

	public List<String> mRecoirdAnswer = new ArrayList<String>();
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
	private boolean answer_boolean = false;
	public AnswerMyPojo amp;
	private Gson gson = new Gson();
	private AnswerJson answerJson;
	private Thread mTimer = new Timer();
	public ProgressDialog prodialog;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				timeText.setText(timeSecondToString(second));
				break;
			case 2:
				prodialog.dismiss();
				Toast.makeText(AnswerBaseActivity.this, "提交答题记录发生错误",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				prodialog.dismiss();
				MyDialog("确认退出吗？", 0);
				break;
			case 4:
				prodialog.dismiss();
				startEndActivity();
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.answer_base);
		eb = (ExerciseBook) getApplication();
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_propTime).setOnClickListener(this);
		prodialog = new ProgressDialog(AnswerBaseActivity.this);
		prodialog.setMessage("正在保存作业");
		prodialog.setCanceledOnTouchOutside(false);
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

		Intent intent = getIntent();
		json = intent.getStringExtra("json");
		path = intent.getStringExtra("path");
		status = intent.getIntExtra("status", 2);
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
		mQuestionType = mQuestionType == 7 ? 0 : mQuestionType;
		amp = getAnswerItem(ExerciseBookTool.getAnswer_Json_history(path));

		mQindex = amp.getQuestions_item();
		mBindex = amp.getBranch_item();

		if (type != 7) {
			setUseTime(amp.getUse_time());
			setType(amp.getStatus());

			if (amp.getStatus() == 1) {
				mRecoirdAnswer = amp.getAnswer();
				mRecoirdRatio = amp.getRatio();
				nextRecord();
			}
		}
	}

	// 设置答题|记录 type 0答题 1历史
	public void setType(int type) {
		this.type = type;
		if (type == 0) {
			if (mQuestionType != 7) {
				mTimer.start();
			}
			base_time_linearlayout.setVisibility(View.VISIBLE);
			base_history_linearlayout.setVisibility(View.GONE);
			base_answer_linearlayout.setVisibility(View.GONE);
		} else if (type == 1 && status > 1) {
			setCheckText("下一个");
			propTrue.setImageResource(R.drawable.base_prop3);
			propTime.setImageResource(R.drawable.base_prop4);
			base_time_linearlayout.setVisibility(View.GONE);
			base_history_linearlayout.setVisibility(View.VISIBLE);
			base_answer_linearlayout.setVisibility(View.VISIBLE);
		}
		if (status == 1) {
			// 重做
			setUseTime(0);
			mTimer.start();
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
	public String getCheckText() {
		return check.getText().toString();
	}

	// 设置检查按钮文本
	public void setCheckText(String text) {
		check.setText(text);
	}

	// 设置页码
	public void setPage(int current, int size) {
		page.setText(current + "/" + size);
	}

	// 设置答题记录 正确率和使用时间
	public void setAccuracyAndUseTime(int accuracy, int useTime) {
		accuracy = accuracy > 0 ? accuracy : 0;
		useTime = useTime > 0 ? useTime : 0;
		accuracyText.setText(accuracy + "%");
		useTimeText.setText(timeSecondToString(useTime));
	}

	// 设置自己的答案
	public void setMyAnswer(String s) {
		if (s == null || s.equals("")) {
			base_answer_text.setText("没有答题记录!");
		} else {
			base_answer_text.setText(answerArr[mQuestionType] + s);
		}
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

	public String getRecordMes() {
		return recordMes;
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

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			close();
			// mHandler.sendEmptyMessage(3);
			break;
		}
	}

	public void close() {
		prodialog.show();
		if (Finish_Json()) {
			mHandler.sendEmptyMessage(3);
		} else {
			mHandler.sendEmptyMessage(2);
		}
	}

	// 切换下一历史记录
	public void nextRecord() {
		setAccuracyAndUseTime(mRecoirdRatio.get(mRecordIndex),
				amp.getUse_time());
		setMyAnswer(mRecoirdAnswer.get(mRecordIndex));
		if (mRecoirdRatio.size() > 0 && mRecoirdAnswer.size() > 0) {
			setAccuracyAndUseTime(mRecoirdRatio.get(mRecordIndex),
					amp.getUse_time());
			if (mQuestionType == 3) {
				recordMes = null;
				String s[] = mRecoirdAnswer.get(mRecordIndex).split(";&&;");
				if (s.length > 1) {
					recordMes = s[1];
				}
				setMyAnswer(s[0].replaceAll(";\\|\\|;", " "));
			} else {
				setMyAnswer(mRecoirdAnswer.get(mRecordIndex));
			}
		} else {
			setAccuracyAndUseTime(0, 0);
			setMyAnswer("");
		}

		if (mRecordIndex < mRecoirdAnswer.size() - 1) {
			mRecordIndex++;
		}
	}

	public boolean Finish_Json() {
		new Thread(new Runnable() {
			public void run() {
				MultipartEntity entity = new MultipartEntity();
				try {
					entity.addPart("student_id", new StringBody(eb.getUid()));
					entity.addPart("school_class_id",
							new StringBody(eb.getClass_id()));
					entity.addPart("publish_question_package_id",
							new StringBody(eb.getWork_id()));
					entity.addPart("answer_file", new FileBody(new File(path)));
					Log.i("linshi", eb.getUid() + "/" + eb.getClass_id() + "/"
							+ eb.getWork_id() + "/" + path);
					String answer_json = ExerciseBookTool.sendPhostimg(
							finish_question_packge, entity);
					if (!answer_json.equals("")) {
						if (new JSONObject(answer_json).getString("status")
								.equals("success")) {
							answer_boolean = true;
							Log.i("Ax", answer_boolean + "--boolean");
							mHandler.sendEmptyMessage(4);
						}
					}
				} catch (Exception e) {
					mHandler.sendEmptyMessage(2);
				}
			}
		}).start();
		return answer_boolean;
	}

	// 计算索引&更新View
	public void calculateIndexAndUpdateView() {
		if (mQindex == mQuestList.size() - 1
				&& mBindex == mQuestList.get(mQindex).size() - 1) {
			// 最后一题
			if (amp.getStatus() == 1 && status > 1) {
				// 历史记录
				MyDialog("没有更多历史记录了,点击确定退出!", 1);
			} else {
				// 答题
				roundOver();
			}
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

	// 子类实现父类刷新视图方法
	public void updateView() {
	}

	// 通关界面
	public void roundOver() {
		if (status == 0) {
			prodialog.show();
			Finish_Json();
		} else {
			startEndActivity();
		}
	}

	public void startEndActivity() {
		Intent intent = new Intent();
		intent.putExtra("precision", ExerciseBookTool.getRatio(path,
				questionArr[mQuestionType], mRatio));// 正确率100时
		intent.putExtra("use_time", getUseTime());// 用户使用的时间
		intent.putExtra("specified_time", specified_time);// 任务基础时间
		intent.setClass(this, WorkEndActivity.class);
		startActivityForResult(intent, 1);
	}

	public AnswerPojo getAnswerPojo() {
		switch (mQuestionType) {
		case 0:
			return answerJson.listening;
		case 1:
			return answerJson.reading;
		case 2:
			return answerJson.time_limit;
		case 3:
			return answerJson.selecting;
		case 4:
			return answerJson.lining;
		case 5:
			return answerJson.cloze;
		case 6:
			return answerJson.sort;
		}
		return null;
	}

	// 保存答题记录 /sdcard/Exercisebook_app/73/85/130/answer.js
	public void saveAnswerJson(String answer, int ratio, int qid, int bid) {
		String answer_history = ExerciseBookTool.getAnswer_Json_history(path);
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

		mRatio = ratio;
		calculateIndexAndUpdateView();

		ap.setQuestions_item(mQindex + "");
		ap.setBranch_item(mBindex + "");

		String str = gson.toJson(answerJson);
		this.ratio = 0;
		try {
			ExerciseBookTool.writeFile(path, str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 自定义dialog设置
	public void MyDialog(String title, final int dialog_type) {
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
				Intent inent = new Intent();
				if (type == 0) {
					inent.setClass(AnswerBaseActivity.this,
							HomeWorkIngActivity.class);
				} else {
					inent.setClass(AnswerBaseActivity.this,
							RecordMainActivity.class);
				}
				startActivity(inent);
				AnswerBaseActivity.this.finish();
			}
		});
		dialog_no.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (dialog_type == 1) {
			dialog_no.setVisibility(View.GONE);
			dialog_ok.setBackgroundColor(getResources().getColor(R.color.lvse));
		} else {
			dialog_img.setVisibility(View.GONE);
		}
		dialog.show();
	}

	// 获取答题记录对象
	public AnswerMyPojo getAnswerItem(String json) {
		try {
			if (json != "") {
				JSONObject obj = new JSONObject(json);
				JSONObject jo = obj.getJSONObject(questionArr[mQuestionType]);
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

	// 编辑道具json文件
	public void PropJson(int type, int branch_id, int question_type) {
		String answer_history = ExerciseBookTool.getAnswer_Json_history(path);
		answerJson = gson.fromJson(answer_history, AnswerJson.class);
		if (type == 1) {// 减时卡
			Toast.makeText(AnswerBaseActivity.this, "成功使用减时卡减去5秒时间！",
					Toast.LENGTH_SHORT).show();
			mQuestionType = question_type;
			int utime = getUseTime() - 5;
			if (utime < 0) {
				utime = 0;
			}
			setUseTime(utime);
			getAnswerPojo().setUse_time(utime + "");
		}
		answerJson.props.get(type).getBranch_id().add(branch_id);
		String str = gson.toJson(answerJson);
		try {
			ExerciseBookTool.writeFile(path, str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("Ax", "resultCode-" + resultCode);
		if (requestCode == 1) {
			switch (resultCode) {
			case 0:
				Intent intent = new Intent();
				intent.setClass(this, HomeWorkIngActivity.class);
				startActivity(intent);
				finish();
				break;
			case 1:
				status = 1;
				mQindex = 0;
				mBindex = 0;
				setUseTime(0);
				updateView();
				break;
			}
		}
	}
}