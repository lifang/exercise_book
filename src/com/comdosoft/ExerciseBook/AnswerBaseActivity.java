package com.comdosoft.ExerciseBook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

// 答题父类
public class AnswerBaseActivity extends Activity {

	private String[] answerArr = new String[] { "你的搭配: ", "你的选择: ", "你的排序: ",
			"你的作答: " };
	private int second;
	private int type = 0;
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
	private boolean flag = true;
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

		if (type == 0) {
			mTimer.start();
			// new Timer().start();
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

	// 设置子布局View
	public void setContentView(int layoutId) {
		View middleView = getLayoutInflater().inflate(layoutId, null);
		if (null != middleLayout) {
			middleLayout.removeAllViews();
			middleLayout.addView(middleView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
	}

	// 设置题目类型 type 0答题 1历史
	public void setType(int type) {
		this.type = type;
	}

	// 获取时间(秒)
	public int getSecond() {
		return second;
	}

	// 设置时间
	public void setSecond(int s) {
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

}
