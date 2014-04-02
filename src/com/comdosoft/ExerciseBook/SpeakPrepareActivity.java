package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.comdosoft.ExerciseBook.pojo.HistoryPojo;
import com.comdosoft.ExerciseBook.pojo.ListHistoryPojo;
import com.comdosoft.ExerciseBook.pojo.ListeningPojo;
import com.comdosoft.ExerciseBook.pojo.QuestionPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class SpeakPrepareActivity extends AnswerBaseActivity implements
		Urlinterface, OnPreparedListener, OnCompletionListener, OnInitListener,
		OnUtteranceCompletedListener, OnClickListener {
	private int mp3Index = 0;
	private String content = "";
	private LinearLayout layout;
	private TextView img_title;
	private MediaPlayer player;
	private String message;
	private ProgressDialog prodialog;
	private List<ListeningPojo> list;
	private List<ListHistoryPojo> list_history;
	private List<QuestionPojo> questionlist;
	private List<HistoryPojo> question_history;
	private ExerciseBook eb;
	private List<String> mp3List;
	private ImageView question_speak_img;
	private boolean playFlag = false;
	private int index;
	private List<TextView> tvlist;
	private static final int REQ_TTS_STATUS_CHECK = 0;// tts code
	private TextToSpeech mTts;
	private boolean mTts_static = false;
	private HashMap<String, String> params = new HashMap<String, String>();
	public String sdpath = Environment.getExternalStorageDirectory() + "/";
	public String path = "";
	private int specified_time;
	private int questions_item;
	private int branch_item;
	private int mystatus;
	private int type;
	private String json;
	private int use_time = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Builder builder = new Builder(SpeakPrepareActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:

				break;
			case 2:
				builder.setMessage(message);
				builder.setPositiveButton("确定", null);
				builder.show();
				break;
			case 3:
				player.stop();
				player.release();
				player = null;
				break;
			case 4:
				Log.i("linshi", "kai");
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.yuting1));
				break;
			case 5:
				Log.i("linshi", "guan");
				question_speak_img.setImageDrawable(getResources().getDrawable(
						R.drawable.yuting2));
				break;
			case 6:
				prodialog.dismiss();
				break;
			case 7:
				prodialog = new ProgressDialog(SpeakPrepareActivity.this);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.setMessage("正在缓冲...");
				prodialog.show();
				break;
			case 8:
				for (int i = 0; i < tvlist.size(); i++) {
					tvlist.get(i)
							.setTextColor(
									getResources().getColor(
											R.color.question_title_bg1));
				}
				Toast.makeText(getApplicationContext(), "点击右上角开始按钮进入答题界面",
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				for (int j = 0; j < tvlist.size(); j++) {
					if (j == index) {
						tvlist.get(index).setTextColor(
								getResources().getColor(R.color.lvse));
					} else {
						tvlist.get(j).setTextColor(
								getResources().getColor(
										R.color.question_title_bg1));
					}
				}
				break;
			case 10:
				for (int i = 0; i < tvlist.size(); i++) {
					tvlist.get(i)
							.setTextColor(
									getResources().getColor(
											R.color.question_title_bg1));
				}
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_speak_prepare);
		eb = (ExerciseBook) getApplication();
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		setTimePropEnd();// 禁用道具
		setTruePropEnd();// 禁用道具
		setTimeGone();
		setCheckText("开始");
		initialize();
		tvlist = new ArrayList<TextView>();

		Intent intent = getIntent();

		path = intent.getStringExtra("path");
		json = intent.getStringExtra("json");
		mystatus = intent.getIntExtra("status", 2);
		Log.i("suanfa", json);
		SetQuestionsJson(json);

		Log.i("suanfa", "list集合size：" + list.size());
		File answer_file = new File(path);
		if (answer_file.exists()) {
			String json2 = ExerciseBookTool.getJson(path);
			SetAnswer_Json(json2);
			switch (mystatus) {
			case 0:
				if (questions_item == -1) {// 大题索引为-1表示没做
					Log.i("suanfa", "没有做过");
					type = 0;
					questionlist = list.get(0).getQuesttionList();
					eb.setQuestion_id(list.get(0).getId());
					eb.setBranch_number(list.get(0).getQuesttionList().size());
				} else {
					type = 1;
					Log.i("suanfa", "没做完");
					questionlist = list.get(questions_item).getQuesttionList();
					eb.setQuestion_id(list.get(questions_item).getId());
					Log.i("suanfa", "id/" + list.get(questions_item).getId());
					eb.setBranch_number(list.get(questions_item)
							.getQuesttionList().size());
				}
				break;
			case 1:
				type = 2;
				Log.i("suanfa", "做完了");
				questionlist = list.get(eb.getQuestion_item())
						.getQuesttionList();
				eb.setQuestion_id(list.get(eb.getQuestion_item()).getId());
				eb.setBranch_number(list.get(eb.getQuestion_item())
						.getQuesttionList().size());
				break;
			case 2:
				// setType(1);
				type = 2;
				questionlist = list.get(eb.getQuestion_item())
						.getQuesttionList();
				eb.setQuestion_id(list.get(eb.getQuestion_item()).getId());
				eb.setBranch_number(list.get(eb.getQuestion_item())
						.getQuesttionList().size());
				break;
			}
		}
		if (questionlist.size() > 0) {
			for (int i = 0; i < questionlist.size(); i++) {
				setTextView(i);
			}
		}
	}

	// 初始化
	public void initialize() {
		layout = (LinearLayout) findViewById(R.id.tvll);
		// question_speak_content = (TextView)
		// findViewById(R.id.question_speak_content);
		question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
		img_title = (TextView) findViewById(R.id.tou);
		player = new MediaPlayer();
	}

	// 解析json
	private void SetQuestionsJson(String json) {
		list = new ArrayList<ListeningPojo>();
		if (json != "") {
			try {
				JSONObject time_limit = new JSONObject(json);
				specified_time = time_limit.getInt("specified_time");
				Log.i("suanfa", specified_time + "--");
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					Log.i("suanfa", questions.length() + "--");
					for (int i = 0; i < questions.length(); i++) {
						JSONObject jo = questions.getJSONObject(i);
						JSONArray jsonarr = jo.getJSONArray("branch_questions");
						questionlist = new ArrayList<QuestionPojo>();
						for (int j = 0; j < jsonarr.length(); j++) {
							JSONObject item = jsonarr.getJSONObject(j);
							QuestionPojo tl = new QuestionPojo(
									item.getInt("id"),
									item.getString("content"),
									item.getString("resource_url"));
							questionlist.add(tl);
						}
						ListeningPojo lp = new ListeningPojo(jo.getInt("id"),
								questionlist);
						Log.i("suanfa", "id--" + jo.getInt("id"));
						list.add(lp);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		eb.setList(list);
	}

	// 解析json
	private void SetAnswer_Json(String json) {
		list_history = new ArrayList<ListHistoryPojo>();
		if (json != "") {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject time_limit = obj.getJSONObject("reading");
				questions_item = time_limit.getInt("questions_item");
				branch_item = time_limit.getInt("branch_item");
				use_time = time_limit.getInt("use_time");
				setStart();
				Log.i("aaa", specified_time + "--" + branch_item);
				JSONArray questions = time_limit.getJSONArray("questions");
				if (questions.length() > 0) {
					for (int i = 0; i < questions.length(); i++) {
						JSONObject jo = questions.getJSONObject(i);
						JSONArray jsonarr = jo.getJSONArray("branch_questions");
						HistoryPojo tl;
						question_history = new ArrayList<HistoryPojo>();
						for (int j = 0; j < jsonarr.length(); j++) {
							JSONObject item = jsonarr.getJSONObject(j);
							tl = new HistoryPojo(item.getInt("id"),
									item.getString("answer"),
									item.getInt("ratio"));
							question_history.add(tl);
						}
						ListHistoryPojo lp = new ListHistoryPojo(
								jo.getInt("id"), question_history);
						list_history.add(lp);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(SpeakPrepareActivity.this, "解析json发生错误",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 添加textview
	private void setTextView(int i) {
		View view1 = View.inflate(this, R.layout.text_view, null);
		TextView tv = (TextView) view1.findViewById(R.id.tv);
		tv.setText(questionlist.get(i).getContent());
		tvlist.add(tv);
		layout.addView(view1);
	}

	public void onclicks(View v) {
		// boolean staick = false;
		// if (ExerciseBookTool.isConnect(getApplicationContext())) {
		// mp3List = new ArrayList<String>();
		// for (int i = 0; i < questionlist.size(); i++) {
		// if (questionlist.get(i).getUrl() == "") {
		// staick = true;
		// }
		// mp3List.add(IP + questionlist.get(i).getUrl());
		// }
		// 从文件系统播放
		// if (staick) {
		if (mTts != null) {
			if (mTts.isSpeaking()) {
				mTts_static = false;
				handler.sendEmptyMessage(5);
				handler.sendEmptyMessage(10);
				onPause();
			} else {
				mTts_static = true;
				handler.sendEmptyMessage(4);
				index = 0;
				handler.sendEmptyMessage(9);
				// 检查TTS数据是否已经安装并且可用
				Intent checkIntent = new Intent();
				checkIntent
						.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
				startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
			}
		} else {
			mTts_static = true;
			handler.sendEmptyMessage(4);
			index = 0;
			handler.sendEmptyMessage(9);
			// 检查TTS数据是否已经安装并且可用
			Intent checkIntent = new Intent();
			checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
		}
		// }
		// else {
		// if (player.isPlaying()) {// 暂停播放
		// stop();
		// } else {
		// if (mp3Index >= mp3List.size()) {
		// mp3Index = 0;
		// handler.sendEmptyMessage(7);
		// new Thread(new setPlay()).start();
		// } else if (playFlag) {
		// handler.sendEmptyMessage(4);
		// player.start();
		// } else {
		// playFlag = true;
		// handler.sendEmptyMessage(7);
		// new Thread(new setPlay()).start();
		// }
		// }
		// }
		// } else {
		// Toast.makeText(getApplicationContext(),
		// ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
		// }
	}

	/**
	 * 播放音乐
	 * 
	 * @param playPosition
	 */
	class setPlay implements Runnable {
		public void run() {
			index = 0;
			Log.i("aaa", index + "-index");
			play(mp3List.get(mp3Index));
		}
	}

	private void play(String path) {
		try {
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 回调获取从谷歌得到的数据
		if (requestCode == REQ_TTS_STATUS_CHECK) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
			// 这个返回结果表明TTS Engine可以用
			{
				mTts = new TextToSpeech(this, this);
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
				Toast.makeText(SpeakPrepareActivity.this, "您需要安装TTS框架",
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
				Toast.makeText(SpeakPrepareActivity.this, "TTS语音启动失败..",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
				Toast.makeText(SpeakPrepareActivity.this, "语音不可用",
						Toast.LENGTH_SHORT).show();
				// speakBtn.setEnabled(false);
			} else {
				for (int i = 0; i < questionlist.size(); i++) {
					params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
							String.valueOf(i));
					mTts.speak(questionlist.get(i).getContent(),
							TextToSpeech.QUEUE_ADD, params);
				}
				mTts.setOnUtteranceCompletedListener(this);
				// speakBtn.setEnabled(true);
			}
		}
	}

	public void onPrepared(MediaPlayer mp) {
		handler.sendEmptyMessage(6);
		handler.sendEmptyMessage(4);
		handler.sendEmptyMessage(9);
		mp.start();
	}

	public void stop() {
		handler.sendEmptyMessage(5);
		if (player.isPlaying()) {
			player.pause();
		}
	}

	protected void onStart() {
		if (player == null)
			player = new MediaPlayer();
		super.onStart();
	}

	// 暂停音频
	protected void onStop() {
		if (player.isPlaying()) {// 正在播放
			player.pause();// 暂停
		}
		super.onStop();
	}

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

	protected void onPause() {
		super.onPause();
		if (mTts != null)
		// activity暂停时也停止TTS
		{
			mTts.stop();
		}
	}

	// 音频播放结束后继续播放下一个音频,直到所有音频播放完毕
	public void onCompletion(MediaPlayer mp) {
		try {
			if (++mp3Index < mp3List.size()) {
				index = mp3Index;
				Log.i("aaa", index + "-index");
				mp.reset();
				mp.setDataSource(mp3List.get(mp3Index));
				mp.prepare();
				mp.setOnPreparedListener(this);
				mp.setOnCompletionListener(this);
				// handler.sendEmptyMessage(9);
			} else {
				handler.sendEmptyMessage(5);
				handler.sendEmptyMessage(8);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onUtteranceCompleted(String arg0) {
		Log.i("linshi", arg0 + "=<");
		int size = Integer.valueOf(arg0);
		if (size + 1 == questionlist.size()) {
			handler.sendEmptyMessage(5);
			handler.sendEmptyMessage(8);
		} else {
			if (mTts_static) {
				index = size + 1;
				handler.sendEmptyMessage(9);
			} else {
				handler.sendEmptyMessage(10);
			}
		}
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.base_check_linearlayout:
			// 0没做过----1做过了但没做完----2表示做完了
			switch (type) {
			case 0:
				eb.setQuestion_list(questionlist);
				break;
			case 1:
				if (branch_item == -1) {// 本大题的小题还没做
					eb.setQuestion_list(list.get(questions_item)
							.getQuesttionList());
				} else {// 删掉做过的题目
					for (int i = 0; i < branch_item + 1; i++) {
						questionlist.remove(0);
					}
					eb.setQuestion_list(questionlist);
				}
				break;
			case 2:
				eb.setQuestion_list(questionlist);
				break;
			}
			intent.putExtra("specified_time", specified_time);
			intent.putExtra("path", path);
			intent.putExtra("json", json);
			intent.putExtra("time", use_time);
			intent.putExtra("status", mystatus);
			Log.i("suanfa", status + "-prepare");
			if (status == 2) {
				intent.setClass(SpeakPrepareActivity.this,
						SpeakHistoryActivity.class);
			} else {
				intent.setClass(SpeakPrepareActivity.this,
						SpeakBeginActivity.class);
			}
			startActivity(intent);
			SpeakPrepareActivity.this.finish();
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
