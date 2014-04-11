package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;
import com.comdosoft.ExerciseBook.pojo.DictationPojo;
import com.comdosoft.ExerciseBook.pojo.MoveLRPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Soundex_Levenshtein;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

/**
 * @作者 马龙
 * @时间 2014-4-10 下午3:50:29
 */
public class AnswerDictationBeginActivity extends AnswerBaseActivity implements
		OnClickListener, ExerciseBookParams, OnPreparedListener,
		OnCompletionListener, Urlinterface {

	private int rightCount = 0;
	private int linearLayoutIndex = 0;
	private int mesLinearLayoutIndex = 0;
	private int testIndex = 0;
	private boolean mesFlag = false;
	private boolean playFlag = false;

	// private String JSON =
	// "{ \"specified_time\": \"100\", \"questions\": [ { \"id\": \"284\", \"branch_questions\": [ { \"id\": \"181\", \"content\": \"This is an apple!\", \"resource_url\": \"/question_packages/201402/questions_package_222/media_181.mp3\" } ] }, { \"id\": \"285\", \"branch_questions\": [ { \"id\": \"182\", \"content\": \"Why is google\", \"resource_url\": \"/question_packages/201402/questions_package_222/media_181.mp3\" } ] } ] }";
	private String REG = "(?i)[^a-zA-Z0-9\u4E00-\u9FA5]";
	private String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
	private String vowelREG = "[aeiouAEIOU]";
	private String symbol;
	private String mp3URL;

	private List<Integer> indexList = new ArrayList<Integer>();
	private List<DictationPojo> dictationList = new ArrayList<DictationPojo>();
	private List<EditText> etList = new ArrayList<EditText>();
	private List<TextView> tvList = new ArrayList<TextView>();
	private List<LinearLayout> linearLayoutList = new ArrayList<LinearLayout>();
	private List<LinearLayout> mesLinearLayoutList = new ArrayList<LinearLayout>();
	private Map<String, Integer> errorMap = new HashMap<String, Integer>();
	private List<MoveLRPojo> moveList = new ArrayList<MoveLRPojo>();

	private LinearLayout editLinearLayout;
	private TextView mesText;
	private TextView recordText;
	private ImageView mPlayImg;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private LayoutParams etlp;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mPlayImg.setImageResource(R.drawable.xiao1);
				break;
			case 2:
				mPlayImg.setImageResource(R.drawable.xiao2);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.question_dictation_begin);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		editLinearLayout = (LinearLayout) findViewById(R.id.question_dictation_linearLayout);
		recordText = (TextView) findViewById(R.id.question_dictation_tv);
		mesText = (TextView) findViewById(R.id.question_dictation_mes);
		mPlayImg = (ImageView) findViewById(R.id.question_dictation_play);
		mPlayImg.setOnClickListener(this);

		setQuestionType(0);
		setTruePropEnd();

		etlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		etlp.leftMargin = 10;

		analysisJSON(json);

		updateView();
	}

	// 多写提示
	public void setRecordMes() {
		Log.i("Ax", getRecordMes().length + "-mes_size");
		String[] str = getRecordMes();
		if (str.length > 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("你需要多写的词:");
			for (int i = 1; i < str.length; i++) {
				sb.append("\n" + filterString(str[i]));
			}
			mesText.setText(sb.toString());
			mesText.setVisibility(View.VISIBLE);
		}
	}

	// 初始化
	public void updateView() {
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
		moveList.clear();

		playFlag = false;

		// 获取当前大&小题数据
		mp3URL = mQuestList.get(mQindex).get(mBindex).getPath();
		String content = mQuestList.get(mQindex).get(mBindex).getContent();
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

		setPage(mBindex + 1, mQuestList.get(mQindex).size());
		if (status == 2) {
			setRecordMes();
			recordText.setText(mQuestList.get(mQindex).get(mBindex)
					.getContent());
			recordText.setVisibility(View.VISIBLE);
			editLinearLayout.setVisibility(View.GONE);
		}
	}

	// 添加答题格子
	public void initView(final int i) {
		String value = dictationList.get(i).getValue();
		EditText et = new EditText(this);
		if (status == 2) {
			et.setFocusable(false);
			et.setFocusableInTouchMode(false);
			et.setText(value);
			et.setTextColor(Color.rgb(53, 207, 143));
		}
		et.setInputType(InputType.TYPE_TEXT_VARIATION_URI
				| InputType.TYPE_TEXT_FLAG_MULTI_LINE
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		et.setId(R.id.aa);
		et.setOnTouchListener(new MyTouch(i));
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
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		RelativeLayout rl = (RelativeLayout) inflater.inflate(
				R.layout.question_dictation_begin_mes_item, null);
		RelativeLayout r = (RelativeLayout) rl
				.findViewById(R.id.question_dictation_mes_rl);
		TextView et = (TextView) rl.findViewById(R.id.question_item_mesText);
		ImageView left = (ImageView) rl.findViewById(R.id.left_move);
		ImageView right = (ImageView) rl.findViewById(R.id.right_move);

		String value = dictationList.get(i).getValue();
		String s = value.substring(value.length() - 1, value.length());
		if (isChinesePunctuation(s.charAt(0)) || isEnglistPunctuation(s)) {
			value = value.substring(0, value.length() - 1);
		}
		et.setText(value);
		et.setTextSize(16);

		LayoutParams rllp = new LayoutParams(dictationList.get(i).getValue()
				.length() * 20 + 80, LayoutParams.WRAP_CONTENT);
		r.setLayoutParams(rllp);

		left.setOnClickListener(this);
		right.setOnClickListener(this);

		right.setVisibility(View.INVISIBLE);
		left.setVisibility(View.INVISIBLE);
		et.setVisibility(View.INVISIBLE);

		if (i == 0 || i % 4 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
			linear.setOrientation(LinearLayout.HORIZONTAL);
			mesLinearLayoutList.add(linear);
			if (i > 0) {
				mesLinearLayoutIndex++;
			}
		}
		moveList.add(new MoveLRPojo(left, right));
		tvList.add(et);
		mesLinearLayoutList.get(mesLinearLayoutIndex).addView(rl);
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

	// 是否包含数字
	public static boolean hasDigit(String content) {
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
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
					String path = jb.getString("resource_url");
					mBranchList.add(new AnswerBasePojo(question_id,
							branch_question_id, answer, eb.getPath() + "/"
									+ path));
				}
				mQuestList.add(mBranchList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 检查算法
	public void check() {
		mesFlag = false;
		rightCount = 0;
		// 检查
		if (getCheckText().equals("检查")) {
			for (int i = 0; i < etList.size(); i++) {
				String s = etList.get(i).getText().toString();
				if (s != null && !s.equals("")) {
					if (ExerciseBookTool.isChinese(s)) {
						errorMap.put(dictationList.get(i).getValue(), 1);
						dictationList.get(i).setFlag(0);
						tvList.get(i).setVisibility(View.INVISIBLE);
						etList.get(i).setTextColor(Color.rgb(255, 0, 0));
					}
					String answerStr = filterString(dictationList.get(i)
							.getValue());
					if (hasDigit(answerStr)) {
						if (answerStr.equals(filterString(s))) {
							rightCount++;
							dictationList.get(i).setFlag(1);
							tvList.get(i).setVisibility(View.INVISIBLE);
							etList.get(i).setTextColor(Color.rgb(146, 184, 27));
						} else {
							errorMap.put(dictationList.get(i).getValue(), 1);
							dictationList.get(i).setFlag(0);
							tvList.get(i).setVisibility(View.INVISIBLE);
							etList.get(i).setTextColor(Color.rgb(255, 0, 0));
						}
					} else {
						int value = Soundex_Levenshtein.dragonEngine(
								filterString(s), filterString(dictationList
										.get(i).getValue()));
						if (filterString(dictationList.get(i).getValue())
								.equals(filterString(s))) {
							// 全对
							rightCount++;
							dictationList.get(i).setFlag(1);
							tvList.get(i).setVisibility(View.INVISIBLE);
							etList.get(i).setTextColor(Color.rgb(53, 207, 143));
						} else if (value > 6) {
							// 半对
							mesFlag = true;
							errorMap.put(dictationList.get(i).getValue(), 1);
							dictationList.get(i).setFlag(1);
							etList.get(i).setTextColor(Color.rgb(00, 00, 00));
							tvList.get(i).setVisibility(View.VISIBLE);
						} else {
							// 错误
							errorMap.put(dictationList.get(i).getValue(), 1);
							dictationList.get(i).setFlag(0);
							tvList.get(i).setVisibility(View.INVISIBLE);
							etList.get(i).setTextColor(Color.rgb(245, 21, 58));
						}
					}
				} else {
					errorMap.put(dictationList.get(i).getValue(), 1);
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
				setCheckText("下一题");
			}

			// 计算正确率 只计算第一次检查
			if (firstRatioFlag) {
				ratio = (100 * rightCount) / etList.size();
				rightCount = 0;
				firstRatioFlag = false;
			}
		} else {
			// Next
			setCheckText("检查");
			if (status == 0) {
				AnswerBasePojo aop = mQuestList.get(mQindex).get(mBindex);
				saveAnswerJson(getAnswer(), ratio, aop.getQuestions_id(),
						aop.getBranch_questions_id());
			} else {
				calculateIndexAndUpdateView();
			}
			errorMap.clear();
		}
	}

	public String getAnswer() {
		StringBuffer error = new StringBuffer();
		StringBuffer answer = new StringBuffer();

		for (Entry<String, Integer> m : errorMap.entrySet()) {
			error.append(";||;").append(m.getKey());
		}

		for (int i = 0; i < etList.size(); i++) {
			answer.append(etList.get(i).getText().toString()).append(" ");
		}

		if (answer.length() > 0) {
			answer.delete(answer.length() - 1, answer.length());
		}

		return answer.append(error.toString()).toString();
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

	public void move(int index, int type) {
		int left = -1;
		int right = -1;
		for (int i = 0; i < etList.size(); i++) {
			EditText et = etList.get(i);
			if (et.getText().toString().equals("")) {
				if (i < index) {
					if (i > left) {
						left = i;
					}
				} else if (i > index && right == -1) {
					right = i;
				}
			}
			if (i != index) {
				moveList.get(i).getLeftMove().setVisibility(View.GONE);
				moveList.get(i).getRightMove().setVisibility(View.GONE);
			}
		}

		if (type == 2) {
			if (left >= 0) {
				moveList.get(index).getLeftMove().setVisibility(View.VISIBLE);
			} else {
				moveList.get(index).getLeftMove().setVisibility(View.GONE);
			}
			if (right >= 0) {
				moveList.get(index).getRightMove().setVisibility(View.VISIBLE);
			} else {
				moveList.get(index).getRightMove().setVisibility(View.GONE);
			}
		}

		if (type == 0) {
			if (left >= 0) {
				for (int i = left; i < index; i++) {
					etList.get(i).setText(
							etList.get(i + 1).getText().toString());
				}
				etList.get(index).setText("");
			}
		} else if (type == 1) {
			if (right >= 0) {
				for (int i = right; i > index; i--) {
					etList.get(i).setText(
							etList.get(i - 1).getText().toString());
				}
				etList.get(index).setText("");
			}
		}
	}

	class MyTouch implements OnTouchListener {
		private int index = 0;

		public MyTouch() {
		}

		public MyTouch(int index) {
			super();
			this.index = index;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.aa:
				testIndex = index;
				move(index, 2);
				break;
			}
			return false;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			if (status != 2) {
				check();
			} else {
				calculateIndexAndUpdateView();
				nextRecord();
			}
			break;
		case R.id.question_dictation_play:
			if (!playFlag) {
				new MyMediaPlay().start();
				playFlag = true;
			} else if (mediaPlayer.isPlaying()) {
				handler.sendEmptyMessage(2);
				mediaPlayer.pause();
			} else {
				handler.sendEmptyMessage(1);
				mediaPlayer.start();
			}
			break;
		case R.id.left_move:
			move(testIndex, 0);
			moveList.get(testIndex).getLeftMove().setVisibility(View.GONE);
			moveList.get(testIndex).getRightMove().setVisibility(View.GONE);
			break;
		case R.id.right_move:
			move(testIndex, 1);
			moveList.get(testIndex).getLeftMove().setVisibility(View.GONE);
			moveList.get(testIndex).getRightMove().setVisibility(View.GONE);
			break;
		case R.id.base_propTime:
			super.onClick(v);
			break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		handler.sendEmptyMessage(1);
		mp.start();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		handler.sendEmptyMessage(2);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
