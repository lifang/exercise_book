package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.pojo.QuestionPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.PredicateLayout;
import com.comdosoft.ExerciseBook.tools.Soundex_Levenshtein;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.google.gson.Gson;

/**
 * 作者: 张秀楠 时间：2014-4-9 上午11:49:11
 */
@SuppressWarnings("deprecation")
public class SpeakBeginActivity extends AnswerBaseActivity implements
		Urlinterface, OnPreparedListener, OnCompletionListener, OnInitListener,
		OnUtteranceCompletedListener, OnClickListener {
	public String content;// 记录本题正确答案
	private MediaPlayer player;
	private PredicateLayout PredicateLayout;
	public List<TextView> text_list;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;// 语音code
	private static final int REQ_TTS_STATUS_CHECK = 0;// tts code
	private TextToSpeech mTts;
	public List<String> str_list;
	public int number;// 朗读次数
	private TextView question_speak_tishi;
	private Map<Integer, String> ok_speak;
	public MediaRecorder mediaRecorder;
	private boolean Speak_type = false;
	private ExerciseBook eb;
	private List<QuestionPojo> branch_questions;
	private int index = 0;
	public String error_str = "";// 记录错误的词
	private ProgressDialog prodialog;
	private ImageView question_speak_img;
	private String path;
	private Gson gson;
	private int ratio = 0;
	private String json;
	private AnswerJson answerJson;
	private int qid;
	private boolean playFlag = false;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Builder builder = new Builder(SpeakBeginActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 0:
				index += 1;
				question_speak_tishi.setVisibility(View.GONE);
				setPage(index + 1, branch_questions.size());
				PredicateLayout.removeAllViews();
				Speak_type = false;
				number = 0;
				Log.i("aaa", branch_questions.get(index).getContent());
				content = branch_questions.get(index).getContent();
				SetTextView();
				break;
			case 7:
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.xiao1));
				break;
			case 8:
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.xiao2));
				break;
			case 9:
				prodialog.dismiss();
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_begin);
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_propTime).setOnClickListener(this);
		// 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
		super.mQuestionType = 1;
		super.setStart();
		setCheckText("下一题");
		eb = (ExerciseBook) getApplication();
		if (eb.getTime_number() <= 0 || status == 1) {
			setTimePropEnd();// 禁用道具
		}
		setTruePropEnd();// 禁用道具
		qid = eb.getQuestion_id();
		gson = new Gson();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		json = intent.getStringExtra("json");
		int time = intent.getIntExtra("time", 0);
		setUseTime(time);
		super.specified_time = intent.getIntExtra("specified_time", 0);
		initialize();
		SetTextView();
	}

	// 初始化
	public void initialize() {
		branch_questions = eb.getQuestion_list();
		content = branch_questions.get(0).getContent();
		setPage(1, branch_questions.size());
		PredicateLayout = (PredicateLayout) findViewById(R.id.question_speak_content);
		question_speak_tishi = (TextView) findViewById(R.id.question_speak_tishi);
		question_speak_tishi.setVisibility(View.GONE);
		question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
		player = new MediaPlayer();

	}

	// 设置textview
	public void SetTextView() {
		text_list = new ArrayList<TextView>();
		String[] str = content.split(" ");
		for (int i = 0; i < str.length; i++) {
			View view1 = View.inflate(this, R.layout.question_speak_begin_item,
					null);
			LinearLayout layout = (LinearLayout) view1
					.findViewById(R.id.layout);
			TextView text = (TextView) view1.findViewById(R.id.text);
			text.setText(str[i].toString());
			text_list.add(text);
			PredicateLayout.addView(layout);
		}
	}

	public void onclicks(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.question_speak_img:// 播放音频
			if (branch_questions.get(index).getUrl() != "null") {
				path = eb.getPath() + "/"
						+ branch_questions.get(index).getUrl();
				if (player.isPlaying()) {// 正在播放
					stop();
				} else {
					if (playFlag) {
						handler.sendEmptyMessage(7);
						player.start();
					} else {
						playFlag = true;
						prodialog = new ProgressDialog(SpeakBeginActivity.this);
						prodialog.setCanceledOnTouchOutside(false);
						prodialog.setMessage("正在缓冲...");
						prodialog.show();
						new Thread(new setPlay()).start();
					}
				}
			} else {// 如果音频为空 就使用TTS朗读
				if (mTts != null) {
					if (mTts.isSpeaking()) {
						handler.sendEmptyMessage(8);
						onPause();
					} else {
						handler.sendEmptyMessage(7);
						Intent checkIntent = new Intent();
						checkIntent
								.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
						startActivityForResult(checkIntent,
								REQ_TTS_STATUS_CHECK);
					}
				} else {
					handler.sendEmptyMessage(7);
					Intent checkIntent = new Intent();
					checkIntent
							.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
					startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
				}
			}
			break;
		case R.id.speak:// 语音
			try {
				number += 1;
				// 通过Intent传递语音识别的模式，开启语音
				Intent speak_intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				// 语言模式和自由模式的语音识别
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				// 提示语音开始
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
				// 开始语音识别

				startActivityForResult(speak_intent,
						VOICE_RECOGNITION_REQUEST_CODE);

			} catch (Exception e) {
				Builder builder = new Builder(SpeakBeginActivity.this);
				builder.setTitle("提示");
				builder.setMessage("您的设备未安装语音引擎,点击确定开始安装。");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (ExerciseBookTool.copyApkFromAssets(
										SpeakBeginActivity.this,
										"VoiceSearch.apk", Environment
												.getExternalStorageDirectory()
												.getAbsolutePath()
												+ "/VoiceSearch.apk")) {
									Intent intent = new Intent(
											Intent.ACTION_VIEW);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setDataAndType(
											Uri.parse("file://"
													+ Environment
															.getExternalStorageDirectory()
															.getAbsolutePath()
													+ "/VoiceSearch.apk"),
											"application/vnd.android.package-archive");
									SpeakBeginActivity.this
											.startActivity(intent);
								}
							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
			break;
		}

	}

	//
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 回调获取从谷歌得到的数据
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// stopService(service_intent);
				// 取得语音的字符
				ok_speak = new HashMap<Integer, String>();// 用于记录正确的词
				ArrayList<String> results = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				Log.i("suanfa", "->" + results);
				String speak = getSpeakStr(results);
				Log.i("suanfa", "语音返回--->" + speak);
				str_list = new ArrayList<String>();
				content = content.replaceAll(
						"(?i)[^a-zA-Z0-9\u4E00-\u9FA5\\s]", "");// 去除标点符号
				content = content.replaceAll("\\s", " ");
				String[] ok_arr = content.split(" ");
				Log.i("suanfa", "正确答案->" + content);
				String[] item = speak.split(" ");
				for (int i = 0; i < item.length; i++) {
					str_list.add(item[i]);
				}

				if (!speak.equals("")) {
					List<int[]> code_list = Soundex_Levenshtein.Engine2(
							content, str_list);

					Log.i("suanfa", code_list.size() + "");
					if (code_list.size() > 0) {
						for (int i = 0; i < code_list.size(); i++) {
							if (code_list.get(i)[1] >= 7) {
								ok_speak.put(code_list.get(i)[0],
										ok_arr[code_list.get(i)[0]]);
								text_list.get(code_list.get(i)[0])
										.setTextColor(
												getResources().getColor(
														R.color.work_end));
							} else {
								if (!error_str
										.contains(ok_arr[code_list.get(i)[0]])) {
									error_str += ok_arr[code_list.get(i)[0]]
											+ ";||;";
								}
								text_list.get(code_list.get(i)[0])
										.setTextColor(
												getResources().getColor(
														R.color.juhuang));
							}
						}

					} else {
						for (int i = 0; i < str_list.size(); i++) {
							text_list.get(i).setTextColor(
									getResources().getColor(R.color.juhuang));
						}
					}
					Log.i(tag, ok_speak.size() + "-" + str_list.size());
					question_speak_tishi.setVisibility(View.VISIBLE);
					if (ok_speak.size() == ok_arr.length) {
						question_speak_tishi
								.setText(R.string.question_speak_tishi_ok);
					} else {
						question_speak_tishi
								.setText(R.string.question_speak_tishi);
					}
					if ((ok_speak.size() * 2) >= ok_arr.length || number >= 4) {// 设置如果正确率为50%就可以下一题
						Speak_type = true;
						setButtonOver();

					} else {
						Speak_type = false;
					}

					Log.i("aaa", ok_speak.size() + ":->" + ok_arr.length);
					if (number == 1) {
						ratio = (ok_speak.size() * 100) / ok_arr.length;
						Log.i("aaa", "ratio:" + ratio);
					}
				} else {
					Toast.makeText(getApplicationContext(), "语音不清晰,请再认真朗读一次吧",
							Toast.LENGTH_SHORT).show();
					number -= 1;
				}
			} else {
				number -= 1;
			}
		}

		if (requestCode == REQ_TTS_STATUS_CHECK) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
			// 这个返回结果表明TTS Engine可以用
			{
				mTts = new TextToSpeech(this, this);
				Toast.makeText(SpeakBeginActivity.this, "TTS语音正常启动..",
						Toast.LENGTH_SHORT).show();
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				// 需要的语音数据已损坏
				install_tts();
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				// 缺少需要语言的语音数据
				install_tts();
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
			// 缺少需要语言的发音数据
			{
				// 这三种情况都表明数据有错,重新下载安装需要的数据
				install_tts();
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				// 检查失败
			default:
				Toast.makeText(SpeakBeginActivity.this, "TTS语音启动失败..",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

		if (requestCode == 1) {
			Log.i("aaa", "resultCode:" + resultCode);
			Intent intent = new Intent();
			switch (resultCode) {
			case 0:
				eb.setQuestion_item(0);
				eb.setUsertime(0);
				if (eb.getActivity_item() == 0) {
					intent.setClass(SpeakBeginActivity.this,
							HomeWorkIngActivity.class);
				} else {
					intent.setClass(SpeakBeginActivity.this,
							RecordMainActivity.class);
				}
				startActivity(intent);
				SpeakBeginActivity.this.finish();
				break;
			case 1:
				eb.setQuestion_item(0);
				eb.setUsertime(0);
				intent.putExtra("path", path);
				intent.putExtra("json", json);
				intent.putExtra("status", 1);
				intent.setClass(SpeakBeginActivity.this,
						SpeakPrepareActivity.class);
				startActivity(intent);
				SpeakBeginActivity.this.finish();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class setPlay implements Runnable {
		public void run() {
			play(path);
		}
	}

	/**
	 * 播放音乐
	 * 
	 * @param playPosition
	 */
	private void play(String path) {
		try {
			Log.i("linshi", path + "/" + index);
			player.reset();// 把各项参数恢复到初始状态
			/**
			 * 通过MediaPlayer.setDataSource()
			 * 的方法,将URL或文件路径以字符串的方式传入.使用setDataSource ()方法时,要注意以下三点:
			 * 1.构建完成的MediaPlayer 必须实现Null 对像的检查.
			 * 2.必须实现接收IllegalArgumentException 与IOException
			 * 等异常,在很多情况下,你所用的文件当下并不存在. 3.若使用URL 来播放在线媒体文件,该文件应该要能支持pragressive
			 * 下载.
			 */
			player.setDataSource(path);
			player.prepare();// 进行缓冲
			player.setOnPreparedListener(this);
			player.setOnCompletionListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPrepared(MediaPlayer mp) {
		handler.sendEmptyMessage(9);
		handler.sendEmptyMessage(7);
		player.start();// 开始播放
	}

	public void stop() {
		handler.sendEmptyMessage(8);
		if (player.isPlaying()) {
			player.pause();
		}
	}

	protected void onStart() {
		if (player == null)
			player = new MediaPlayer();
		super.onStart();
	}

	// 停止音频
	protected void onStop() {
		if (player.isPlaying()) {// 正在播放
			player.stop();
		}
		super.onStop();
	}

	protected void onPause() {
		super.onPause();
		if (mTts != null)
		// activity暂停时也停止TTS
		{
			mTts.stop();
		}
	}

	//
	// 销毁音频
	protected void onDestroy() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
		super.onDestroy();
	}

	public void onCompletion(MediaPlayer mp) {
		Log.i("linshi", "over");
		handler.sendEmptyMessage(8);
	}

	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
			mTts.setSpeechRate(0.8f);
			// 设置发音语言
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			// 判断语言是否可用
			{
				Toast.makeText(SpeakBeginActivity.this, "语音不可用",
						Toast.LENGTH_SHORT).show();
				// speakBtn.setEnabled(false);
			} else {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "0");
				mTts.speak(content, TextToSpeech.QUEUE_ADD, params);
				// speakBtn.setEnabled(true);
				mTts.setOnUtteranceCompletedListener(this);
			}
		}
	}

	public void onUtteranceCompleted(String utteranceId) {
		handler.sendEmptyMessage(8);
	}

	// 0为继续 1为全部做完 2为本小题做完
	public int setAnswerJson(String answer_history, String answer, int ratio,
			int id) {

		int type = 0;
		answerJson = gson.fromJson(answer_history, AnswerJson.class);
		answerJson.update = ExerciseBookTool.getTimeIng();
		answerJson.reading.setUpdate_time(ExerciseBookTool.getTimeIng());
		int q_item = Integer.valueOf(answerJson.reading.getQuestions_item());
		int b_item = Integer.valueOf(answerJson.reading.getBranch_item());

		b_item += 1;
		answerJson.reading.setBranch_item(b_item + "");
		answerJson.reading.setUse_time(getUseTime() + "");
		if (answerJson.reading.getQuestions().size() == 0) {
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(qid + "",
					new ArrayList<Branch_AnswerPoJo>());
			answerJson.reading.getQuestions().add(aq);
			q_item += 1;
			answerJson.reading.setQuestions_item(q_item + "");
		}

		answerJson.reading.getQuestions().get(q_item).getBranch_questions()
				.add(new Branch_AnswerPoJo(id + "", answer, ratio + ""));
		if (q_item + 1 == eb.getList().size()
				&& b_item + 1 == eb.getBranch_number()) {// 结束
			answerJson.reading.setStatus("1");
			type = 1;
		} else if (b_item + 1 == eb.getBranch_number()) {// 本小题做完
			q_item += 1;
			answerJson.reading.setQuestions_item(q_item + "");
			answerJson.reading.setBranch_item("-1");
			Answer_QuestionsPojo aq = new Answer_QuestionsPojo(eb.getList()
					.get(q_item).getId()
					+ "", new ArrayList<Branch_AnswerPoJo>());
			answerJson.reading.getQuestions().add(aq);
			type = 2;
		}

		String str = gson.toJson(answerJson);
		ExerciseBookTool.writeFile(path, str);
		Log.i("linshi", str);
		return type;
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			eb.setQuestion_item(0);
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			int type;
			if (Speak_type == true || number >= 4) {
				stop();
				try {
					if (status == 0) {
						String answer_history = ExerciseBookTool
								.getAnswer_Json_history(path);
						type = setAnswerJson(answer_history, error_str, ratio,
								branch_questions.get(index).getId());
					} else {
						type = Again();
					}

					calculateRatio(ratio);
					Log.i("aaa", type + "-type");
					switch (type) {// 0为下一小题 1为全部做完 2为本小题做完
					case 0:
						error_str = "";
						handler.sendEmptyMessage(0);
						break;
					case 1:
						Log.i("suanfa", "结束");
						eb.setUsertime(0);
						roundOver();
						break;
					case 2:
						eb.setUsertime(getUseTime());
						intent.putExtra("path", path);
						intent.putExtra("json", json);
						intent.setClass(SpeakBeginActivity.this,
								SpeakPrepareActivity.class);
						startActivity(intent);
						SpeakBeginActivity.this.finish();
						break;
					}

				} catch (Exception e) {
				}
			} else {
				Toast.makeText(SpeakBeginActivity.this, "请先答完本题",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.base_propTime:
			// 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
			if (eb.getTime_number() > 0) {
				PropJson(0, branch_questions.get(index).getId());
			} else {
				Toast.makeText(SpeakBeginActivity.this,
						R.string.prop_number_error, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private int Again() {
		int type = 0;
		if (eb.getQuestion_item() + 1 == eb.getList().size()
				&& index + 1 == eb.getBranch_number()) {// 结束
			eb.setQuestion_item(0);
			type = 1;
		} else if (index + 1 == eb.getBranch_number()) {// 本小题做完
			eb.setQuestion_item(eb.getQuestion_item() + 1);
			type = 2;
		}
		return type;
	}

	public String getSpeakStr(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String[] arr = list.get(i).split(" ");
			if (!StrArr(arr)) {
				return list.get(i);
			}
		}
		return "";
	}

	public boolean StrArr(String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (ExerciseBookTool.isChinese(arr[i])) {
				return true;
			}
		}
		return false;
	}

	public void setButtonOver() {
		if (status == 0) {
			String answer_history = ExerciseBookTool
					.getAnswer_Json_history(path);
			JSONObject obj;
			try {
				obj = new JSONObject(answer_history);
				int q_item = Integer.valueOf(obj.getJSONObject("reading")
						.getString("questions_item"));
				int b_item = Integer.valueOf(obj.getJSONObject("reading")
						.getString("branch_item"));
				Log.i("aaa", q_item + "/" + b_item);
				if (b_item + 2 >= eb.getBranch_number()) {
					setCheckText("完成");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			int status = Again();
			if (status == 1) {
				setCheckText("完成");
			}
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.close();
			eb.setUsertime(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
