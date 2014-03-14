package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.comdosoft.ExerciseBook.pojo.DictationPojo;
import com.comdosoft.ExerciseBook.pojo.QuestionPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ListeningQuestionList;
import com.comdosoft.ExerciseBook.tools.Soundex_Levenshtein;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

// 拼写答题    马龙    2014年2月12日
public class AnswerDictationBeginActivity extends AnswerBaseActivity implements
		OnClickListener, ExerciseBookParams, OnPreparedListener,
		OnCompletionListener, Urlinterface {

	private String REG = "(?i)[^a-zA-Z0-9\u4E00-\u9FA5]";
	private String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
	private String vowelREG = "[aeiouAEIOU]";
	private String symbol;
	private int editTextIndex = 0;
	private int linearLayoutIndex = 0;
	private int mesLinearLayoutIndex = 0;
	private int smallIndex;
	private int bigIndex = 0;
	private int class_id;
	private int student_id;
	private int publish_question_package_id;
	private String log;
	private String mp3URL;
	private boolean mesFlag = false;
	private boolean playFlag = false;
	private List<Integer> indexList = new ArrayList<Integer>();
	private List<QuestionPojo> qpList = new ArrayList<QuestionPojo>();
	private List<DictationPojo> dictationList = new ArrayList<DictationPojo>();
	private List<EditText> etList = new ArrayList<EditText>();
	private List<TextView> tvList = new ArrayList<TextView>();
	private List<LinearLayout> linearLayoutList = new ArrayList<LinearLayout>();
	private List<LinearLayout> mesLinearLayoutList = new ArrayList<LinearLayout>();
	private Map<String, Integer> errorMap = new HashMap<String, Integer>();
	private LinearLayout editLinearLayout;
	private TextView mesText;
	private TextView page;
	private TextView check;
	private ImageView mPlayImg;
	private StringBuffer answer = new StringBuffer();
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private ProgressDialog mPd;
	private ExerciseBook homeWork;
	private LayoutParams etlp;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mPd.dismiss();
				break;
			case 2:
				mPlayImg.setImageResource(R.drawable.dictation_laba4);
				break;
			case 3:
				mPlayImg.setImageResource(R.drawable.dictation_laba3);
				break;
			case 4:
				mPd.setMessage("正在缓冲...");
				mPd.show();
				break;
			case 5:
				etList.get(editTextIndex)
						.setWidth(
								etList.get(editTextIndex).getText().toString()
										.length() * 20 + 80);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.question_dictation_begin);
		editLinearLayout = (LinearLayout) findViewById(R.id.question_dictation_linearLayout);
		mesText = (TextView) findViewById(R.id.question_dictation_mes);
		check = (TextView) findViewById(R.id.base_checkText);
		mPlayImg = (ImageView) findViewById(R.id.question_dictation_play);
		mPlayImg.setOnClickListener(this);

		mPd = new ProgressDialog(this);
		mPd.setMessage(ExerciseBookParams.PD_FINISH_QUESTION);

		homeWork = (ExerciseBook) getApplication();
//		homeWork.setNewsFlag(true);
//		publish_question_package_id = homeWork.getP_q_package_id();
		student_id = homeWork.getUser_id();
//		class_id = homeWork.getClass_id();

		smallIndex = ListeningQuestionList.Small_Index;
		etlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		etlp.leftMargin = 10;

		init();
	}

	// 初始化
	public void init() {
		// 清除数据
		mesFlag = false;
		linearLayoutIndex = 0;
		mesLinearLayoutIndex = 0;
		etList.clear();
		tvList.clear();
		dictationList.clear();
		linearLayoutList.clear();
		mesLinearLayoutList.clear();
		editLinearLayout.removeAllViews();
		mesText.setVisibility(LinearLayout.GONE);

		playFlag = false;

		handler.sendEmptyMessage(3);

		// 获取已答过题目记录数
		bigIndex = ListeningQuestionList.Record_Count;

		// 获取小题数据
		qpList = ListeningQuestionList.getListeningPojo(bigIndex)
				.getQuesttionList();

		// 获取当前大&小题数据
		mp3URL = IP + qpList.get(smallIndex).getUrl();
		String content = qpList.get(smallIndex).getContent();
		String[] sArr = content.split(" ");
		for (int i = 0; i < sArr.length; i++) {
			String s = sArr[i];
			if (s != null && !s.equals("") && !isEnglistPunctuation(s)) {
				dictationList.add(new DictationPojo(s, 0));
			}
		}

		for (int i = 0; i < dictationList.size(); i++) {
			initView(i);
		}
		for (int i = 0; i < linearLayoutList.size(); i++) {
			editLinearLayout.addView(linearLayoutList.get(i));
			editLinearLayout.addView(mesLinearLayoutList.get(i));
		}

		setPage(1 + smallIndex++, qpList.size());
	}

	// 添加答题格子
	public void initView(final int i) {
		String value = dictationList.get(i).getValue();
		EditText et = new EditText(getApplicationContext());
		// et.setText(filterString(value));
		et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		et.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					if (i + 1 >= etList.size()) {
						v.setImeOptions(EditorInfo.IME_ACTION_DONE);
					} else {
						etList.get(i + 1).requestFocus();
					}
				}
				return true;
			}
		});
		et.setMaxLines(1);
		et.setSingleLine(true);
		int width = value.length() * 20 + 80;
		et.setWidth(width > 200 ? 200 : width);
		et.setHeight(40);
		et.setGravity(Gravity.CENTER);
		et.setLayoutParams(etlp);
		et.setSingleLine(true);
		if (i == 0 || i % 4 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.topMargin = 10;
			linear.setLayoutParams(lp);
			linearLayoutList.add(linear);
			if (i > 0) {
				linearLayoutIndex++;
			}
		}
		etList.add(et);
		linearLayoutList.get(linearLayoutIndex).addView(et);

		symbol = value.substring(value.length() - 1, value.length());
		if (isEnglistPunctuation(symbol)
				|| isChinesePunctuation(symbol.charAt(0))) {
			initSymbol(symbol);
		}

		initMesView(i);
	}

	// 添加标点符号
	public void initSymbol(String symbol) {
		TextView tv = new TextView(getApplicationContext());
		tv.setText(symbol);
		tv.setTextSize(24);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.topMargin = 10;
		lp.leftMargin = 10;
		tv.setLayoutParams(lp);
		linearLayoutList.get(linearLayoutIndex).addView(tv);
	}

	// 半对提示
	public void initMesView(int i) {
		TextView et = new TextView(getApplicationContext());
		String value = dictationList.get(i).getValue();
		String s = value.substring(value.length() - 1, value.length());
		if (isChinesePunctuation(s.charAt(0)) || isEnglistPunctuation(s)) {
			value = value.substring(0, value.length() - 1);
		}
		et.setText(value);
		et.setWidth(value.length() * 20 + 80);
		et.setHeight(20);
		et.setTextSize(16);
		et.setTextColor(Color.rgb(110, 107, 107));
		et.setGravity(Gravity.CENTER);
		et.setLayoutParams(etlp);
		et.setSingleLine(true);
		et.setVisibility(View.INVISIBLE);
		if (i == 0 || i % 4 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			mesLinearLayoutList.add(linear);
			if (i > 0) {
				mesLinearLayoutIndex++;
			}
		}
		tvList.add(et);
		mesLinearLayoutList.get(mesLinearLayoutIndex).addView(et);
	}

	// 判断是否英文符号
	public boolean isEnglistPunctuation(String s) {
		Pattern pattern4 = Pattern.compile("\\p{Punct}+");
		Matcher matcher4 = pattern4.matcher(s);
		if (matcher4.matches()) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否中文符号
	public boolean isChinesePunctuation(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
			return true;
		} else {
			return false;
		}
	}

	// 过滤英文数字以外的字符
	public String filterString(String s) {
		s = s.replaceAll(REG, "");
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		s = m.replaceAll("").trim();
		return s;
	}

	// 检查算法
	public void check() {
		mesFlag = false;
		// 检查
		if (check.getText().toString().equals("检查")) {
			for (int i = 0; i < etList.size(); i++) {
				String s = etList.get(i).getText().toString();
				if (s != null && !s.equals("")) {
					if (ExerciseBookTool.isChinese(s)) {
						errorMap.put(dictationList.get(i).getValue(), 1);
						dictationList.get(i).setFlag(0);
						answer.append(s).append("-!-");
						tvList.get(i).setVisibility(View.INVISIBLE);
						etList.get(i).setTextColor(Color.rgb(255, 0, 0));
					}

					int value = Soundex_Levenshtein.dragonEngine(
							filterString(s), filterString(dictationList.get(i)
									.getValue()));
					if (filterString(dictationList.get(i).getValue()).equals(
							filterString(s))) {
						// 全对
						dictationList.get(i).setFlag(1);
						tvList.get(i).setVisibility(View.INVISIBLE);
						etList.get(i).setTextColor(Color.rgb(53, 207, 143));
					} else if (value > 6) {
						// 半对
						mesFlag = true;
						dictationList.get(i).setFlag(1);
						etList.get(i).setTextColor(Color.rgb(00, 00, 00));
						tvList.get(i).setVisibility(View.VISIBLE);
					} else {
						// 错误
						errorMap.put(dictationList.get(i).getValue(), 1);
						dictationList.get(i).setFlag(0);
						answer.append(s).append("-!-");
						tvList.get(i).setVisibility(View.INVISIBLE);
						etList.get(i).setTextColor(Color.rgb(245, 21, 58));
					}
				} else {
					dictationList.get(i).setFlag(0);
				}
			}

			// 答错提示剩余单词
			indexList.clear();
			for (int i = 0; i < dictationList.size(); i++) {
				if (dictationList.get(i).getFlag() == 0) {
					indexList.add(i);
				}
			}
			if (indexList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				if (mesFlag) {
					sb.append(QUESTION_DICTATION_ERROR_MES_TWO);
				} else {
					sb.append(QUESTION_DICTATION_ERROR_MES);
				}
				mesText.setVisibility(LinearLayout.VISIBLE);
				Random r = new Random(System.currentTimeMillis());
				String value = dictationList.get(
						indexList.get(r.nextInt(indexList.size()))).getValue();
				String s = value.substring(value.length() - 1, value.length());
				if (isChinesePunctuation(s.charAt(0))
						|| isEnglistPunctuation(s)) {
					value = value.substring(0, value.length() - 1);
				}

				Pattern p = Pattern.compile(vowelREG);
				Matcher m = p.matcher(value);
				sb.append("\n◆" + (m.replaceAll("_")));
				mesText.setText(sb.toString());
			} else {
				if (mesFlag) {
					mesText.setVisibility(LinearLayout.VISIBLE);
					mesText.setText(QUESTION_DICTATION_ERROR_MES_ONE);
				}
				check.setText("继续");
			}

		} else {
			// Next
			if (bigIndex < ListeningQuestionList.getListeningPojoList().size()) {
				if (answer.length() > 0) {
					answer.delete(answer.length() - 3, answer.length());
				}
				mPd.setMessage(ExerciseBookParams.PD_FINISH_QUESTION);
				mPd.show();
				new MyTrehad(bigIndex, smallIndex).start();
				ListeningQuestionList.Small_Index = ListeningQuestionList.Small_Index + 1;
				if (smallIndex == qpList.size()) {
					// 切换大题
					bigIndex++;
					smallIndex = 0;

					ListeningQuestionList.Small_Index = 0;
					ListeningQuestionList.Record_Count = ListeningQuestionList.Record_Count + 1;

					if (bigIndex == ListeningQuestionList
							.getListeningPojoList().size()) {
						mPd.show();
//						if (homeWork.getHistory_item() == homeWork
//								.getQuestion_allNumber()) {
//							new SendWorkOver().start();
//						} else if (homeWork.getQuestion_allNumber() == 0) {
//							new SendWorkOver().start();
//						}
//						MyDialog("恭喜完成今天的听写作业!", "确认", "取消", 2);
						return;
					}

//					MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
					return;
				}
				check.setText("检查");
				init();
			}
		}
	}

	// 提交答题记录
	class MyTrehad extends Thread {
		private int bIndex, sIndex;

		public void MyThread() {
		}

		public MyTrehad(int bIndex, int sIndex) {
			super();
			this.bIndex = bIndex;
			this.sIndex = sIndex;
		}

		public void run() {
			super.run();
			StringBuffer sb = new StringBuffer();
			Iterator iterator = errorMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator
						.next();
				sb.append(entry.getKey()).append("-!-");
			}
			if (sb.length() > 0) {
				sb.delete(sb.length() - 3, sb.length());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", student_id + "");
			map.put("school_class_id", class_id + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			map.put("question_id",
					ListeningQuestionList.getListeningPojo(bIndex).getId() + "");
			map.put("branch_question_id", qpList.get(sIndex - 1).getId() + "");
			map.put("answer", sb.toString() + "");
			map.put("question_types", "0");

			log = ExerciseBookTool.doPost(RECORD_ANSWER_INFO, map);
			Log.i("Ax", log);
			Log.i("Ax", "error:" + sb.toString());
			answer.delete(0, answer.length());
			sb.delete(0, sb.length());
			errorMap.clear();
			handler.sendEmptyMessage(1);
		}
	}

	class SendWorkOver extends Thread {
		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", student_id + "");
			map.put("school_class_id", class_id + "");
			map.put("publish_question_package_id", publish_question_package_id
					+ "");
			log = ExerciseBookTool.doPost(FINISH_QUESTION_PACKGE, map);
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);
		}
	}

	public void myFinish() {
		finish();
		Intent intent = new Intent();
		intent.setClass(AnswerDictationBeginActivity.this, HomeWorkIngActivity.class);
		startActivity(intent);
	}


	// 播放音频
	public void playerAmr() {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mp3URL);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	class MyMediaPlay extends Thread {
		@Override
		public void run() {
			super.run();
			playerAmr();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
//			MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
			break;
		case R.id.base_check_linearlayout:
			check();
			break;
		case R.id.question_dictation_play:
			if (!playFlag) {
				new MyMediaPlay().start();
				playFlag = true;
				handler.sendEmptyMessage(4);
			} else if (mediaPlayer.isPlaying()) {
				handler.sendEmptyMessage(3);
				mediaPlayer.pause();
			} else {
				handler.sendEmptyMessage(2);
				mediaPlayer.start();
			}
			break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		handler.sendEmptyMessage(1);
		handler.sendEmptyMessage(2);
		mp.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		handler.sendEmptyMessage(3);
	}

	// 销毁音频
	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

	// 停止音频
	protected void onStop() {
		if (mediaPlayer.isPlaying()) {// 正在播放
			mediaPlayer.stop();// 停止
		}
		super.onStop();
	}

}
