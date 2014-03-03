package com.comdosoft.ExerciseBook;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comdosoft.ExerciseBook.pojo.QuestionPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.PredicateLayout;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class SpeakBeginActivity extends Activity implements Urlinterface,
// OnPreparedListener, OnCompletionListener, OnInitListener,
		OnUtteranceCompletedListener {
	public String content;// 记录本题正确答案
	private TextView question_speak_title;
	private MediaPlayer player;
	private PredicateLayout PredicateLayout;
	public List<View> view_list;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;// 语音code
	private static final int REQ_TTS_STATUS_CHECK = 0;// tts code
	private TextToSpeech mTts;
	public List<String> str_list;
	public int number;// 播放次数
	private TextView question_speak_tishi;
	private Map<Integer, String> ok_speak;
	public MediaRecorder mediaRecorder;
	private boolean Speak_type = false;
	private int student_id;
	private int school_class_id;
	private int publish_question_package_id;
	private int branch_question_id;
	private ExerciseBook eb;
	private List<QuestionPojo> branch_questions;
	private int index = 0;
	private String message;
	public String error_str = "";// 记录错误的词
	private ProgressDialog prodialog;
	private int over_static;
	private ImageView question_speak_img;
	private String path;
	private boolean playFlag = false;
	private int width;
	private LinearLayout question_speak_next;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent = new Intent();
			Builder builder = new Builder(SpeakBeginActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 0:
				// prodialog.dismiss();
				// index += 1;
				// PredicateLayout.removeAllViews();
				// question_speak_title.setText((index + 1) + "/"
				// + branch_questions.size());
				// content = branch_questions.get(index).getContent();
				// SetTextView();
				break;
			// case 1:
			// prodialog.dismiss();
			// MyDialog("你已经答完本题确认继续下一题吗?", "确认", "取消", 1);
			// break;
			// case 2:
			// prodialog.dismiss();
			// eb.setQuestion_index(eb.getQuestion_index() + 1);
			// eb.setHistory_item(eb.getHistory_item() + 1);
			// SpeakBeginActivity.this.finish();
			// intent.setClass(SpeakBeginActivity.this,
			// SpeakPrepareActivity.class);
			// startActivity(intent);
			// break;
			// case 3:
			// prodialog.dismiss();
			// builder.setMessage(message);
			// builder.setPositiveButton("确定", null);
			// builder.show();
			// break;
			// case 4:
			// prodialog.dismiss();
			// SpeakBeginActivity.this.finish();
			// intent.setClass(SpeakBeginActivity.this,
			// ebMainActivity.class);
			// startActivity(intent);
			// break;
			// case 5:
			// // MyDialog("恭喜完成今天的朗读作业!", "确认", "取消", 2);
			// prodialog.dismiss();
			// builder.setMessage("提交作业失败");
			// builder.setPositiveButton("确定", null);
			// builder.show();
			// break;
			// case 6:
			// prodialog.dismiss();
			// MyDialog("恭喜完成今天的朗读作业!", "确认", "取消", 2);
			// break;
			// case 7:
			// question_speak_img.setImageDrawable(getResources().getDrawable(
			// R.drawable.jzlbgreen));
			// break;
			// case 8:
			// question_speak_img.setImageDrawable(getResources().getDrawable(
			// R.drawable.jzlb));
			// break;
			// case 9:
			// prodialog.dismiss();
			// break;
			// case 10:
			// question_speak_next.setBackgroundColor(getResources().getColor(
			// R.color.juhuang));
			// break;
			// case 11:
			// question_speak_next.setBackgroundColor(getResources().getColor(
			// R.color.button_hui));
			// break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.question_speak_begin);
		// eb = (eb) getApplication();
		// initialize();
		// SetTextView();
		// Display display = this.getWindowManager().getDefaultDisplay();
		// width = display.getWidth();
		// publish_question_package_id = eb.getP_q_package_id();
		// Log.i(tag, publish_question_package_id + "===");
		// student_id = eb.getUser_id();
		// school_class_id = eb.getClass_id();
		// eb.setNewsFlag(true);
	}

	// // 初始化
	// public void initialize() {
	// branch_questions = eb.getBranch_questions();
	// content = branch_questions.get(0).getContent();
	// question_speak_title = (TextView)
	// findViewById(R.id.question_speak_title);
	// question_speak_title.setText((index + 1) + "/"
	// + branch_questions.size());
	// PredicateLayout = (PredicateLayout)
	// findViewById(R.id.question_speak_content);
	// question_speak_tishi = (TextView)
	// findViewById(R.id.question_speak_tishi);
	// question_speak_tishi.setVisibility(View.GONE);
	// question_speak_img = (ImageView) findViewById(R.id.question_speak_img);
	// question_speak_next = (LinearLayout)
	// findViewById(R.id.question_speak_next);
	// player = new MediaPlayer();
	//
	// }
	//
	// // 设置textview
	// public void SetTextView() {
	// view_list = new ArrayList<View>();
	// String[] str = content.split(" ");
	// for (int i = 0; i < str.length; i++) {
	// View view1 = View.inflate(this, R.layout.question_speak_begin_item,
	// null);
	// LinearLayout layout = (LinearLayout) view1
	// .findViewById(R.id.layout);
	// TextView text = (TextView) view1.findViewById(R.id.text);
	// View color = (View) view1.findViewById(R.id.color);
	// text.setText(str[i].toString());
	// color.setBackgroundColor(getResources().getColor(R.color.shenhui));
	// view_list.add(color);
	// PredicateLayout.addView(layout);
	// }
	// }
	//
	// public void onclicks(View v) {
	// Intent intent = new Intent();
	// switch (v.getId()) {
	// case R.id.question_speak_exit:
	// MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
	// break;
	// case R.id.question_speak_next:
	// handler.sendEmptyMessage(11);
	// if (ExerciseBookTool.isConnect(getApplicationContext())) {
	// if (Speak_type == true) {
	// stop();
	// playFlag = false;
	// Speak_type = false;
	// question_speak_tishi.setVisibility(View.GONE);
	// int ye = 0;
	// if (eb.getHistory_item() >= eb
	// .getQuestion_list().size()) {
	// ye = eb.getQuestion_index();
	// } else {
	// ye = eb.getHistory_item();
	// }
	// if ((ye + 1) < eb.getQuestion_allNumber()) {
	// prodialog = new ProgressDialog(SpeakBeginActivity.this);
	// prodialog.setMessage(ebParams.PD_FINISH_QUESTION);
	// prodialog.setCanceledOnTouchOutside(false);
	// prodialog.show();
	// branch_question_id = branch_questions.get(index)
	// .getId();
	// if ((index + 1) < branch_questions.size()) {
	// over_static = 0;
	// } else {
	// over_static = 1;
	// }
	// Thread thread = new Thread(new Record_answer_info());// 记录小题
	// thread.start();
	// } else {
	// prodialog = new ProgressDialog(SpeakBeginActivity.this);
	// prodialog.setCanceledOnTouchOutside(false);
	// prodialog.setMessage(ebParams.PD_FINISH_QUESTION);
	// prodialog.show();
	// branch_question_id = branch_questions.get(index)
	// .getId();
	// if ((index + 1) < branch_questions.size()) {
	// over_static = 0;
	// } else {
	// over_static = 2;
	// // new Thread(new SendWorkOver()).start();// 记录大題
	// }
	// Thread thread = new Thread(new Record_answer_info());// 记录小题
	// thread.start();
	// }
	// } else {
	// Toast.makeText(SpeakBeginActivity.this, "请先完成本题作业!",
	// Toast.LENGTH_SHORT).show();
	// }
	// } else {
	// Toast.makeText(getApplicationContext(),
	// ebParams.INTERNET, Toast.LENGTH_SHORT).show();
	// }
	// break;
	// case R.id.question_speak_img:// 播放音频
	// if (ExerciseBookTool.isConnect(getApplicationContext())) {
	// // 从文件系统播放
	// if (branch_questions.get(index).getUrl() != "") {
	// path = IP + branch_questions.get(index).getUrl();
	// if (player.isPlaying()) {// 正在播放
	// stop();
	// } else {
	// if (playFlag) {
	// handler.sendEmptyMessage(7);
	// player.start();
	// } else {
	// playFlag = true;
	// prodialog = new ProgressDialog(
	// SpeakBeginActivity.this);
	// prodialog.setCanceledOnTouchOutside(false);
	// prodialog.setMessage("正在缓冲...");
	// prodialog.show();
	// new Thread(new setPlay()).start();
	// }
	// }
	// } else {// 如果音频为空 就使用TTS朗读
	// // 检查TTS数据是否已经安装并且可用
	// if (mTts != null) {
	// if (mTts.isSpeaking()) {
	// handler.sendEmptyMessage(8);
	// onPause();
	// } else {
	// handler.sendEmptyMessage(7);
	// Intent checkIntent = new Intent();
	// checkIntent
	// .setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	// startActivityForResult(checkIntent,
	// REQ_TTS_STATUS_CHECK);
	// }
	// } else {
	// handler.sendEmptyMessage(7);
	// Intent checkIntent = new Intent();
	// checkIntent
	// .setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	// startActivityForResult(checkIntent,
	// REQ_TTS_STATUS_CHECK);
	// }
	// }
	// } else {
	// Toast.makeText(getApplicationContext(),
	// ebParams.INTERNET, Toast.LENGTH_SHORT).show();
	// }
	// break;
	// case R.id.speak:// 语音
	// try {
	// // 通过Intent传递语音识别的模式，开启语音
	// Intent speak_intent = new Intent(
	// RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	// // 语言模式和自由模式的语音识别
	// intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	// RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	// // 提示语音开始
	// intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
	// // 开始语音识别
	//
	// startActivityForResult(speak_intent,
	// VOICE_RECOGNITION_REQUEST_CODE);
	//
	// } catch (Exception e) {
	// Builder builder = new Builder(SpeakBeginActivity.this);
	// builder.setTitle("提示");
	// builder.setMessage("您的设备未安装语音引擎,点击确定开始安装。");
	// builder.setPositiveButton("确定",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int which) {
	// if (ExerciseBookTool.copyApkFromAssets(
	// SpeakBeginActivity.this,
	// "VoiceSearch.apk", Environment
	// .getExternalStorageDirectory()
	// .getAbsolutePath()
	// + "/VoiceSearch.apk")) {
	// Intent intent = new Intent(
	// Intent.ACTION_VIEW);
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.setDataAndType(
	// Uri.parse("file://"
	// + Environment
	// .getExternalStorageDirectory()
	// .getAbsolutePath()
	// + "/VoiceSearch.apk"),
	// "application/vnd.android.package-archive");
	// SpeakBeginActivity.this
	// .startActivity(intent);
	// }
	// }
	// });
	// builder.setNegativeButton("取消", null);
	// builder.show();
	// }
	// break;
	// }
	// }
	//
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // 回调获取从谷歌得到的数据
	// if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
	// && resultCode == RESULT_OK) {
	// // stopService(service_intent);
	// // 取得语音的字符
	// ok_speak = new HashMap<Integer, String>();// 用于记录正确的词
	// ArrayList<String> results = data
	// .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	//
	// String speak = results.get(0);// 用户语音返回的字符串
	// Log.i("suanfa", "语音返回--->" + speak);
	// str_list = new ArrayList<String>();
	// content = content
	// .replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5\\s]", "");
	// ;// 去除标点符号
	// content = content.replaceAll("  ", " ");
	// String[] ok_arr = content.split(" ");
	// Log.i("suanfa", "正确答案->" + content);
	// String[] item = speak.split(" ");
	// for (int i = 0; i < item.length; i++) {
	// str_list.add(item[i]);
	// }
	//
	// List<int[]> code_list = Soundex_Levenshtein.Engine2(content,
	// str_list);
	// Log.i("suanfa", code_list.size() + "");
	// if (code_list.size() > 0) {
	// for (int i = 0; i < code_list.size(); i++) {
	// // Log.i(tag, str_list.get(code_list.get(i)[0]) + "->相似度:"
	// // + code_list.get(i)[1]);
	// if (code_list.get(i)[1] >= 7) {
	// ok_speak.put(code_list.get(i)[0],
	// ok_arr[code_list.get(i)[0]]);
	// view_list.get(code_list.get(i)[0]).setBackgroundColor(
	// getResources().getColor(R.color.lvse));
	// } else {
	// if (!error_str.contains(ok_arr[code_list.get(i)[0]])) {
	// Log.i("aaa", "1");
	// error_str += ok_arr[code_list.get(i)[0]] + "-!-";
	// }
	// Log.i("aaa", "1");
	// view_list.get(code_list.get(i)[0]).setBackgroundColor(
	// getResources().getColor(R.color.juhuang));
	// }
	// }
	//
	// } else {
	// for (int i = 0; i < str_list.size(); i++) {
	// view_list.get(i).setBackgroundColor(
	// getResources().getColor(R.color.juhuang));
	// }
	// }
	// Log.i(tag, ok_speak.size() + "-" + str_list.size());
	// question_speak_tishi.setVisibility(View.VISIBLE);
	// if (ok_speak.size() == ok_arr.length) {
	// question_speak_tishi.setText(R.string.question_speak_tishi_ok);
	// } else {
	// question_speak_tishi.setText(R.string.question_speak_tishi);
	// }
	// if ((ok_speak.size() * 2) >= ok_arr.length) {// 设置如果正确率为50%就可以下一题
	// Speak_type = true;
	// handler.sendEmptyMessage(10);
	// } else {
	// Speak_type = false;
	// handler.sendEmptyMessage(11);
	// }
	// Log.i("aaa", ok_speak.size() + "/" + ok_arr.length + "/"
	// + Speak_type);
	// }
	//
	// if (requestCode == REQ_TTS_STATUS_CHECK) {
	// switch (resultCode) {
	// case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
	// // 这个返回结果表明TTS Engine可以用
	// {
	// mTts = new TextToSpeech(this, this);
	// Toast.makeText(SpeakBeginActivity.this, "TTS语音正常启动..",
	// Toast.LENGTH_SHORT).show();
	// }
	// break;
	// case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
	// // 需要的语音数据已损坏
	// case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
	// // 缺少需要语言的语音数据
	// case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
	// // 缺少需要语言的发音数据
	// {
	// // 这三种情况都表明数据有错,重新下载安装需要的数据
	// Toast.makeText(SpeakBeginActivity.this, "您需要安装TTS框架",
	// Toast.LENGTH_SHORT).show();
	// Intent dataIntent = new Intent();
	// dataIntent
	// .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	// startActivity(dataIntent);
	// }
	// break;
	// case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
	// // 检查失败
	// default:
	// Toast.makeText(SpeakBeginActivity.this, "TTS语音启动失败..",
	// Toast.LENGTH_SHORT).show();
	// break;
	// }
	// }
	// super.onActivityResult(requestCode, resultCode, data);
	// }
	//
	// // 自定义dialog设置
	// private void MyDialog(String title, String btn_one, String Btn_two,
	// final int type) {
	// final Intent intent = new Intent();
	// final Dialog dialog = new Dialog(this, R.style.Transparent);
	// dialog.setContentView(R.layout.my_dialog);
	// dialog.setCancelable(true);
	//
	// ImageView dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
	//
	// TextView title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
	// title_tv.setText(title);
	// Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
	// dialog_ok.setText(btn_one);
	// Button dialog_no = (Button) dialog.findViewById(R.id.dialog_no);
	// dialog_no.setText(Btn_two);
	// dialog_ok.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// switch (type) {
	// case 0:
	// dialog.dismiss();
	// SpeakBeginActivity.this.finish();
	// intent.setClass(SpeakBeginActivity.this,
	// ebMainActivity.class);
	// startActivity(intent);
	// break;
	// case 1:
	// dialog.dismiss();
	// handler.sendEmptyMessage(2);
	//
	// break;
	// case 2:
	// dialog.dismiss();
	// eb.setQuestion_index(0);
	// SpeakBeginActivity.this.finish();
	// intent.setClass(SpeakBeginActivity.this,
	// ebMainActivity.class);
	// startActivity(intent);
	// break;
	// }
	// }
	// });
	// dialog_no.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// switch (type) {
	// case 0:
	// dialog.dismiss();
	// break;
	// case 1:
	// dialog.dismiss();
	// SpeakBeginActivity.this.finish();
	// intent.setClass(SpeakBeginActivity.this,
	// ebMainActivity.class);
	// startActivity(intent);
	// break;
	// }
	// }
	// });
	// if (type == 2) {
	// dialog_no.setVisibility(View.GONE);
	// dialog_ok.setBackgroundColor(getResources().getColor(R.color.lvse));
	// } else {
	// dialog_img.setVisibility(View.GONE);
	// }
	// dialog.show();
	// }
	//
	// // 记录小题的错词
	// class Record_answer_info implements Runnable {
	// public void run() {
	// Looper.prepare();
	// Log.i("linshi", "错词：" + error_str);
	// Log.i("linshi",
	// student_id + "->" + school_class_id + "->"
	// + publish_question_package_id + "->"
	// + eb.getQuestion_id() + "->"
	// + branch_question_id);
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("school_class_id", school_class_id + "");
	// map.put("student_id", student_id + "");
	// map.put("publish_question_package_id", publish_question_package_id
	// + "");
	// map.put("branch_question_id", branch_question_id + "");
	// map.put("question_id", eb.getQuestion_id() + "");
	// map.put("answer", error_str);
	// map.put("question_types", 1 + "");
	//
	// String json;
	// try {
	// json = ExerciseBookTool.doPost(RECORD_ANSWER_INFO, map);
	// JSONObject obj = new JSONObject(json);
	// if (obj.getString("status").equals("success")) {
	// switch (over_static) {
	// case 0:
	// handler.sendEmptyMessage(0);
	// break;
	// case 1:
	// handler.sendEmptyMessage(1);
	// break;
	// case 2:
	// Log.i(tag, ListeningQuestionList.Record_Count
	// + "***"
	// + ListeningQuestionList.getListeningPojoList()
	// .size());
	// if (ListeningQuestionList.Record_Count == ListeningQuestionList
	// .getListeningPojoList().size()) {
	// new Thread(new SendWorkOver()).start();
	// } else if (ListeningQuestionList.getListeningPojoList()
	// .size() == 0) {
	// new Thread(new SendWorkOver()).start();
	// } else {
	// handler.sendEmptyMessage(6);
	// }
	// break;
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Looper.loop();
	// }
	// }
	//
	// // 记录大题完成情况
	// class SendWorkOver implements Runnable {
	// public void run() {
	// Looper.prepare();
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("school_class_id", school_class_id + "");
	// map.put("student_id", student_id + "");
	// map.put("publish_question_package_id", publish_question_package_id
	// + "");
	// Log.i(tag, school_class_id + "-" + student_id + "-" + "-"
	// + publish_question_package_id);
	// String json;
	// try {
	// json = ExerciseBookTool.doPost(FINISH_QUESTION_PACKGE, map);
	// JSONObject obj = new JSONObject(json);
	// if (obj.getString("status").equals("success")) {
	// handler.sendEmptyMessage(6);
	//
	// } else {
	// message = obj.getString("notice");
	// handler.sendEmptyMessage(3);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Looper.loop();
	// }
	// }
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
	// public void onPrepared(MediaPlayer mp) {
	// handler.sendEmptyMessage(9);
	// handler.sendEmptyMessage(7);
	// player.start();// 开始播放
	// }
	//
	// public void stop() {
	// handler.sendEmptyMessage(8);
	// if (player.isPlaying()) {
	// player.pause();
	// }
	// }
	//
	// protected void onStart() {
	// if (player == null)
	// player = new MediaPlayer();
	// super.onStart();
	// }
	//
	// // 停止音频
	// protected void onStop() {
	// if (player.isPlaying()) {// 正在播放
	// player.stop();
	// }
	// super.onStop();
	// }
	//
	// protected void onPause() {
	// super.onPause();
	// if (mTts != null)
	// // activity暂停时也停止TTS
	// {
	// mTts.stop();
	// }
	// }
	//
	// // 销毁音频
	// protected void onDestroy() {
	// if (player != null) {
	// player.stop();
	// player.release();
	// player = null;
	// }
	// if (mTts != null) {
	// mTts.stop();
	// mTts.shutdown();
	// }
	// super.onDestroy();
	// }
	//
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// MyDialog("你还没有做完题,确认要退出吗?", "确认", "取消", 0);
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
	//
	// public void onCompletion(MediaPlayer mp) {
	// Log.i("linshi", "over");
	// handler.sendEmptyMessage(8);
	// }
	//
	// public void onInit(int status) {
	// if (status == TextToSpeech.SUCCESS) {
	// int result = mTts.setLanguage(Locale.US);
	// mTts.setSpeechRate(0.5f);
	// // 设置发音语言
	// if (result == TextToSpeech.LANG_MISSING_DATA
	// || result == TextToSpeech.LANG_NOT_SUPPORTED)
	// // 判断语言是否可用
	// {
	//
	// Toast.makeText(SpeakBeginActivity.this, "语音不可用",
	// Toast.LENGTH_SHORT).show();
	// // speakBtn.setEnabled(false);
	// } else {
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "0");
	// mTts.speak(content, TextToSpeech.QUEUE_ADD, params);
	// // speakBtn.setEnabled(true);
	// mTts.setOnUtteranceCompletedListener(this);
	// }
	// }
	// }

	public void onUtteranceCompleted(String utteranceId) {
		handler.sendEmptyMessage(8);
	}
}
