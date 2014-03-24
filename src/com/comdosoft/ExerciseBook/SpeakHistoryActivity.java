package com.comdosoft.ExerciseBook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.QuestionPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.PredicateLayout;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class SpeakHistoryActivity extends AnswerBaseActivity implements
		Urlinterface, OnPreparedListener, OnCompletionListener, OnInitListener,
		OnUtteranceCompletedListener, OnClickListener {
	public String content;// 记录本题正确答案
	private MediaPlayer player;
	private PredicateLayout PredicateLayout;
	public List<TextView> text_list;
	private static final int REQ_TTS_STATUS_CHECK = 0;// tts code
	private TextToSpeech mTts;
	public List<String> str_list;
	public int number;// 朗读次数
	private TextView question_speak_tishi;
	public MediaRecorder mediaRecorder;
	private ExerciseBook eb;
	private List<QuestionPojo> branch_questions;
	private int index = 0;
	public String error_str = "";// 记录错误的词
	private String path;
	private int ratio = 0;
	private List<Integer> ratio_list;
	private String json;
	private AnswerPojo answer;
	private ImageView question_speak_img;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Builder builder = new Builder(SpeakHistoryActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 0:
				PredicateLayout.removeAllViews();
				content = eb.getQuestion_list().get(index).getContent();
				if (answer.getQuestions() != null) {
					try {
						error_str = answer.getQuestions()
								.get(eb.getQuestion_item())
								.getBranch_questions().get(index).getAnswer();
					} catch (Exception e) {
						error_str = "";
					}
				} else {
					error_str = "";
				}
				SetTextView();
				SetUserSelect();
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
				// prodialog.dismiss();
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
		setTimePropEnd();// 禁用道具
		setTruePropEnd();// 禁用道具
		setType(1);
		setCheckText("下一个");
		eb = (ExerciseBook) getApplication();

		ratio_list = new ArrayList<Integer>();
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		json = intent.getStringExtra("json");
		initialize();
		SetAnswer();
	}

	// 初始化
	public void initialize() {
		branch_questions = eb.getQuestion_list();
		content = branch_questions.get(0).getContent();
		PredicateLayout = (PredicateLayout) findViewById(R.id.question_speak_content);
		question_speak_tishi = (TextView) findViewById(R.id.question_speak_tishi);
		question_speak_tishi.setGravity(Gravity.LEFT);
		question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
		ImageView speak = (ImageView) findViewById(R.id.speak);
		speak.setVisibility(View.GONE);
		player = new MediaPlayer();
	}

	private void SetAnswer() {
		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			answer = ExerciseBookTool.getAnswer(json2, "cloze");
		}
		handler.sendEmptyMessage(0);
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

	public void SetUserSelect() {
		StringBuffer sb = new StringBuffer("需要多度的词：");
		if (!error_str.equals("")) {
			String[] arr = error_str.split(";\\|\\|;");
			if (arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					sb.append("\n" + arr[i]);
				}
			} else {
				sb.append("\n暂无答题记录");
			}
			Log.i("aaa", arr.length + "-str");
		} else {
			sb.append("\n暂无答题记录");
		}
		question_speak_tishi.setText(sb.toString());
	}

	public void onclicks(View v) {
		switch (v.getId()) {
		case R.id.question_speak_img:// 播放音频
			if (mTts != null) {
				if (mTts.isSpeaking()) {
					handler.sendEmptyMessage(8);
					onPause();
				} else {
					handler.sendEmptyMessage(7);
					Intent checkIntent = new Intent();
					checkIntent
							.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
					startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
				}
			} else {
				handler.sendEmptyMessage(7);
				Intent checkIntent = new Intent();
				checkIntent
						.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
				startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
			}
			break;

		}

	}

	//
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 回调获取从谷歌得到的数据
		if (requestCode == REQ_TTS_STATUS_CHECK) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
			// 这个返回结果表明TTS Engine可以用
			{
				mTts = new TextToSpeech(this, this);
				Toast.makeText(SpeakHistoryActivity.this, "TTS语音正常启动..",
						Toast.LENGTH_SHORT).show();
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				// 需要的语音数据已损坏
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				// 缺少需要语言的语音数据
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
			// 缺少需要语言的发音数据
			{
				// 这三种情况都表明数据有错,重新下载安装需要的数据
				Toast.makeText(SpeakHistoryActivity.this, "您需要安装TTS框架",
						Toast.LENGTH_SHORT).show();
				Intent dataIntent = new Intent();
				dataIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(dataIntent);
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				// 检查失败
			default:
				Toast.makeText(SpeakHistoryActivity.this, "TTS语音启动失败..",
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
				SpeakHistoryActivity.this.finish();
				intent.setClass(SpeakHistoryActivity.this,
						HomeWorkIngActivity.class);
				startActivity(intent);
				break;
			case 1:
				eb.setQuestion_item(0);
				intent.putExtra("path", path);
				intent.putExtra("json", json);
				intent.setClass(SpeakHistoryActivity.this,
						SpeakPrepareActivity.class);
				startActivity(intent);
				SpeakHistoryActivity.this.finish();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//
	// class setPlay implements Runnable {
	// public void run() {
	// play(path);
	// }
	// }
	//
	// /**
	// * 播放音乐
	// *
	// * @param playPosition
	// */
	// private void play(String path) {
	// try {
	// Log.i("linshi", path + "/" + index);
	// player.reset();// 把各项参数恢复到初始状态
	// /**
	// * 通过MediaPlayer.setDataSource()
	// * 的方法,将URL或文件路径以字符串的方式传入.使用setDataSource ()方法时,要注意以下三点:
	// * 1.构建完成的MediaPlayer 必须实现Null 对像的检查.
	// * 2.必须实现接收IllegalArgumentException 与IOException
	// * 等异常,在很多情况下,你所用的文件当下并不存在. 3.若使用URL 来播放在线媒体文件,该文件应该要能支持pragressive
	// * 下载.
	// */
	// player.setDataSource(path);
	// player.prepare();// 进行缓冲
	// player.setOnPreparedListener(this);
	// player.setOnCompletionListener(this);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
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
			mTts.setSpeechRate(0.5f);
			// 设置发音语言
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			// 判断语言是否可用
			{
				Toast.makeText(SpeakHistoryActivity.this, "语音不可用",
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

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			eb.setQuestion_item(0);
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			if (eb.getQuestion_item() + 1 == eb.getList().size()
					&& index + 1 == eb.getBranch_number()) {// 结束
				eb.setQuestion_item(0);
				MyDialog("没有更多历史记录了,点击确定退出!", 1);
			} else if (index + 1 == eb.getBranch_number()) {// 本小题做完
				eb.setQuestion_item(eb.getQuestion_item() + 1);
				SpeakHistoryActivity.this.finish();
				intent.putExtra("path", path);
				intent.putExtra("json", json);
				intent.setClass(SpeakHistoryActivity.this,
						SpeakPrepareActivity.class);
				startActivity(intent);
			} else {
				index += 1;
				handler.sendEmptyMessage(0);
			}
			break;
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
