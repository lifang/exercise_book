package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

/**
 * @作者 丁作强
 * @时间 2014-4-17 下午12:52:08
 */
public class MCardBagActivity extends Table_TabHost implements Urlinterface,
		Serializable {
	public List<tags> tagsList;
	public Map<Integer, List<knowledges_card>> Allmap;
	public Map<Integer, List<View>> FontCard;
	public Map<Integer, List<View>> BackCard;
	private List<knowledges_card> cardList;
	private ViewPager viewPager;
	private ImageView imageView;
	private ImageView[] imageViews;
	private ViewGroup group;
	private List<View> viewList;
	private int page = 0;
	private int mindex;
	Boolean[][] ListBool;
	Boolean[][] PageBool;
	LayoutInflater inflater;
	LayoutInflater inflater2;
	private String student_id;
	private String school_class_id;
	private EditText cardbagEt;
	private ImageView cardbatFind;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private ExerciseBook eb;
	private ProgressDialog progressDialog;
	List<Button> btnList;
	GuidePageAdapter pageAdapter;
	MediaPlayer mediaplay;
	ViewGroup viewgroup;
	List<View> visList;
	int width;
	Boolean fanzhuan = true;
	int btnNum = -1;
	private RelativeLayout spin;
	private TextView spinner_text;

	// private static final String[] m={"A型","B型","O型","AB型","其他"};
	// private ArrayAdapter<String> adapter;
	@Override
	@SuppressWarnings("rawtypes")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardbag);
		eb = (ExerciseBook) getApplication();
		width = getWindowManager().getDefaultDisplay().getWidth();
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		student_id = preferences.getString("id", "70");
		school_class_id = preferences.getString("school_class_id", "109");
		mediaplay = new MediaPlayer();
		eb.getActivityList().add(this);
		btnList = new ArrayList<Button>();
		cardbagEt = (EditText) findViewById(R.id.cardbagEt);
		cardbatFind = (ImageView) findViewById(R.id.cardbatFind);
		button1 = (Button) findViewById(R.id.button1);// 读错
		button2 = (Button) findViewById(R.id.button2);// 拼错
		button3 = (Button) findViewById(R.id.button3);// 选错
		button4 = (Button) findViewById(R.id.button4); // 默认
		btnList.add(button4);
		btnList.add(button1);
		btnList.add(button2);
		btnList.add(button3);

		// 将可选内容与ArrayAdapter连接起来
		// adapter = new
		// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);

		spin = (RelativeLayout) findViewById(R.id.spinner);
		spinner_text = (TextView) findViewById(R.id.spinner_text);
		spin.setOnClickListener(listener);

		initbtn();
		getKonwledges();
		btnlistClick();
	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intentp = new Intent();
			intentp.setClass(MCardBagActivity.this, CardTypeActivity.class);//
			startActivityForResult(intentp, 0);
		}
	};

	public void initbtn() {
		if (mediaplay.isPlaying()) {
			mediaplay.stop();
		}
		mindex = 0;
		fanzhuan = true;
		Allmap = new HashMap<Integer, List<knowledges_card>>();
		cardList = new ArrayList<knowledges_card>();
		tagsList = new ArrayList<tags>();
		FontCard = new HashMap<Integer, List<View>>();
	}

	public void showdialog() {
		progressDialog = new ProgressDialog(MCardBagActivity.this);
		progressDialog.setMessage("正在请求数据...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	public void btnlistClick() {
		cardbatFind.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Thread thread = new Thread() {
					public void run() {
						try {
							viewPager = (ViewPager) findViewById(R.id.guidePages);
							Map<String, String> map = new HashMap<String, String>();
							map.put("student_id", student_id);
							map.put("school_class_id", school_class_id);
							String s = String.valueOf(cardbagEt.getText());
							String s1 = URLEncoder.encode(s, "utf-8");
							map.put("name", s1);
							String json = ExerciseBookTool.sendGETRequest(
									search_tag_card, map);
							page = 0;
							initbtn();
							pageAdapter = new GuidePageAdapter();
							parsejson(json, false);
							handler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				if (mediaplay.isPlaying()) {
					mediaplay.stop();
				}
				if (ExerciseBookTool.isConnect(MCardBagActivity.this)) {
					if (cardbagEt.getText().toString().length() == 0) {
						Toast.makeText(getApplicationContext(), "搜索框不能为空",
								Toast.LENGTH_SHORT).show();
					} else {
						showdialog();
//						changeBtn(-1);
						thread.start();
					}
				} else {
					handler.sendEmptyMessage(7);
				}
			}
		});
	}

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public void getKonwledges() {
		showdialog();
		Thread thread = new Thread() {
			public void run() {
				try {
					viewPager = (ViewPager) findViewById(R.id.guidePages);
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", student_id);
					map.put("school_class_id", school_class_id);
					String json = ExerciseBookTool.sendGETRequest(
							get_knowledges_card, map);
					pageAdapter = new GuidePageAdapter();
					if (json == null || "".equals(json)) {

					} else {
						parsejson(json, false);
					}
					handler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	private void parsejson(String json, Boolean flag) {
		int count1 = 0;
		Allmap.clear();
		cardList.clear();
		tagsList.clear();
		try {
			JSONObject jsonobject = new JSONObject(json);
			// {"tags":[{"id":1,"created_at":"2014-03-09T17:06:53+08:00","updated_at":"2014-03-17T17:06:59+08:00","card_bag_id":1,"name":"bag1"},{"id":2,"created_at":"2014-03-12T09:29:40+08:00","updated_at":"2014-03-12T09:29:40+08:00","card_bag_id":1,"name":"第一个"},{"id":3,"created_at":"2014-03-12T09:48:53+08:00","updated_at":"2014-03-12T09:48:53+08:00","card_bag_id":1,"name":null},{"id":4,"created_at":"2014-03-12T10:04:52+08:00","updated_at":"2014-03-12T10:04:52+08:00","card_bag_id":1,"name":"一饿的"},{"id":5,"created_at":"2014-03-12T10:05:19+08:00","updated_at":"2014-03-12T10:05:19+08:00","card_bag_id":1,"name":"一饿的地方 "},{"id":6,"created_at":"2014-03-12T10:07:33+08:00","updated_at":"2014-03-12T10:07:33+08:00","card_bag_id":1,"name":"似懂非懂分"},{"id":7,"created_at":"2014-03-12T10:09:25+08:00","updated_at":"2014-03-12T10:09:25+08:00","card_bag_id":1,"name":"就看看"},{"id":8,"created_at":"2014-03-12T10:15:23+08:00","updated_at":"2014-03-12T10:15:23+08:00","card_bag_id":1,"name":"第er个"},{"id":9,"created_at":"2014-03-12T10:16:08+08:00","updated_at":"2014-03-12T10:16:08+08:00","card_bag_id":1,"name":"第x个"},{"id":10,"created_at":"2014-03-12T10:18:05+08:00","updated_at":"2014-03-12T10:18:05+08:00","card_bag_id":1,"name":"第y个"},{"id":11,"created_at":"2014-03-12T10:20:55+08:00","updated_at":"2014-03-12T10:20:55+08:00","card_bag_id":1,"name":"hh"},{"id":12,"created_at":"2014-03-19T10:51:54+08:00","updated_at":"2014-03-19T10:51:54+08:00","card_bag_id":1,"name":"??"},{"id":13,"created_at":"2014-03-19T10:55:14+08:00","updated_at":"2014-03-19T10:55:14+08:00","card_bag_id":1,"name":"???"},{"id":14,"created_at":"2014-03-19T11:02:20+08:00","updated_at":"2014-03-19T11:02:20+08:00","card_bag_id":1,"name":"????"},{"id":15,"created_at":"2014-03-19T11:09:21+08:00","updated_at":"2014-03-19T11:09:21+08:00","card_bag_id":1,"name":"fghhg"},{"id":16,"created_at":"2014-03-19T11:11:30+08:00","updated_at":"2014-03-19T11:11:30+08:00","card_bag_id":1,"name":"fghh"},{"id":17,"created_at":"2014-03-19T11:12:38+08:00","updated_at":"2014-03-19T11:12:38+08:00","card_bag_id":1,"name":"a"},{"id":18,"created_at":"2014-03-19T11:14:17+08:00","updated_at":"2014-03-19T11:14:17+08:00","card_bag_id":1,"name":"b"},{"id":19,"created_at":"2014-03-19T11:15:32+08:00","updated_at":"2014-03-19T11:15:32+08:00","card_bag_id":1,"name":"c"},{"id":20,"created_at":"2014-03-19T11:17:51+08:00","updated_at":"2014-03-19T11:17:51+08:00","card_bag_id":1,"name":"d"},{"id":21,"created_at":"2014-03-19T11:19:27+08:00","updated_at":"2014-03-19T11:19:27+08:00","card_bag_id":1,"name":"e"},{"id":22,"created_at":"2014-03-19T11:20:38+08:00","updated_at":"2014-03-19T11:20:38+08:00","card_bag_id":1,"name":"f"},{"id":23,"created_at":"2014-03-19T11:23:13+08:00","updated_at":"2014-03-19T11:23:13+08:00","card_bag_id":1,"name":"g"},{"id":24,"created_at":"2014-03-19T11:25:36+08:00","updated_at":"2014-03-19T11:25:36+08:00","card_bag_id":1,"name":"h"},{"id":25,"created_at":"2014-03-19T11:33:26+08:00","updated_at":"2014-03-19T11:33:26+08:00","card_bag_id":1,"name":"j"},{"id":26,"created_at":"2014-03-19T11:34:05+08:00","updated_at":"2014-03-19T11:34:05+08:00","card_bag_id":1,"name":"k"},{"id":27,"created_at":"2014-03-19T11:39:44+08:00","updated_at":"2014-03-19T11:39:44+08:00","card_bag_id":1,"name":"l"},{"id":28,"created_at":"2014-03-19T11:42:02+08:00","updated_at":"2014-03-19T11:42:02+08:00","card_bag_id":1,"name":"o"},{"id":29,"created_at":"2014-03-19T11:49:29+08:00","updated_at":"2014-03-19T11:49:29+08:00","card_bag_id":1,"name":"第sdfds"},{"id":30,"created_at":"2014-03-19T11:49:49+08:00","updated_at":"2014-03-19T11:49:49+08:00","card_bag_id":1,"name":"第三四十"},{"id":31,"created_at":"2014-03-19T11:50:34+08:00","updated_at":"2014-03-19T11:50:34+08:00","card_bag_id":1,"name":"??????"},{"id":32,"created_at":"2014-03-19T11:50:55+08:00","updated_at":"2014-03-19T11:50:55+08:00","card_bag_id":1,"name":"共用回家"},{"id":33,"created_at":"2014-03-19T13:55:15+08:00","updated_at":"2014-03-19T13:55:15+08:00","card_bag_id":1,"name":"tty"},{"id":34,"created_at":"2014-03-19T13:56:03+08:00","updated_at":"2014-03-19T13:56:03+08:00","card_bag_id":1,"name":"fas"},{"id":35,"created_at":"2014-03-19T13:57:28+08:00","updated_at":"2014-03-19T13:57:28+08:00","card_bag_id":1,"name":"iyt"},{"id":36,"created_at":"2014-03-19T14:00:53+08:00","updated_at":"2014-03-19T14:00:53+08:00","card_bag_id":1,"name":"uuiiy"},{"id":37,"created_at":"2014-03-19T14:05:51+08:00","updated_at":"2014-03-19T14:05:51+08:00","card_bag_id":1,"name":"rty"},{"id":38,"created_at":"2014-03-19T14:08:49+08:00","updated_at":"2014-03-19T14:08:49+08:00","card_bag_id":1,"name":"rtyu"},{"id":39,"created_at":"2014-03-19T14:09:57+08:00","updated_at":"2014-03-19T14:09:57+08:00","card_bag_id":1,"name":"jh"},{"id":40,"created_at":"2014-03-19T14:12:59+08:00","updated_at":"2014-03-19T14:12:59+08:00","card_bag_id":1,"name":"dfgt"},{"id":41,"created_at":"2014-03-19T14:17:05+08:00","updated_at":"2014-03-19T14:17:05+08:00","card_bag_id":1,"name":"errt"},{"id":42,"created_at":"2014-03-19T14:18:31+08:00","updated_at":"2014-03-19T14:18:31+08:00","card_bag_id":1,"name":"uhnk"},{"id":43,"created_at":"2014-03-19T14:20:19+08:00","updated_at":"2014-03-19T14:20:19+08:00","card_bag_id":1,"name":"bgjnf"},{"id":44,"created_at":"2014-03-19T14:21:37+08:00","updated_at":"2014-03-19T14:21:37+08:00","card_bag_id":1,"name":"xxdfv"},{"id":45,"created_at":"2014-03-19T14:22:47+08:00","updated_at":"2014-03-19T14:22:47+08:00","card_bag_id":1,"name":"bcxzs"},{"id":46,"created_at":"2014-03-19T14:28:52+08:00","updated_at":"2014-03-19T14:28:52+08:00","card_bag_id":1,"name":"rtyy"},{"id":47,"created_at":"2014-03-25T17:14:46+08:00","updated_at":"2014-03-25T17:14:46+08:00","card_bag_id":1,"name":"秋雨鱼"},{"id":48,"created_at":"2014-03-25T17:16:14+08:00","updated_at":"2014-03-25T17:16:14+08:00","card_bag_id":1,"name":"秋"},{"id":49,"created_at":"2014-03-28T10:47:46+08:00","updated_at":"2014-03-28T10:47:46+08:00","card_bag_id":1,"name":"ffff"}],"notice":"获取成功！！","status":"success","knowledges_card":[{"card_bag_id":1,"answer":null,"branch_question_id":556,"mistake_types":2,"content":"This is an apple!","id":2,"card_tags_id":[3],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ww","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_186.mp3","question_id":357,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":554,"mistake_types":1,"content":"This
			// is an
			// apple4!","id":3,"card_tags_id":[1,1,1,29,30],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ww","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_184.mp3","question_id":356,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":551,"mistake_types":1,"content":"This
			// is an
			// apple1!","id":4,"card_tags_id":[1,3,6,10,11,5,7,8,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"qq","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_181.mp3","question_id":356,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":536,"mistake_types":2,"content":"I
			// write very
			// hard","id":5,"card_tags_id":[1,2,3,5],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"qq","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/62\/questions_package_275\/media_536.wav","question_id":351,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":538,"mistake_types":2,"content":"I
			// write you a
			// work","id":6,"card_tags_id":[7,4,1,5],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ww","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/62\/questions_package_274\/media_538.wav","question_id":352,"full_text":null,"options":null},{"card_bag_id":1,"answer":"apple","branch_question_id":558,"mistake_types":3,"content":"<file>apple.jpg<\/file>","id":7,"card_tags_id":[11,1,4],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ee","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":null,"question_id":358,"full_text":null,"options":"apple;||;banana;||;orange"},{"card_bag_id":1,"answer":"apple","branch_question_id":559,"mistake_types":3,"content":"<file>apple.wav<\/file>","id":8,"card_tags_id":[],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ee","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":null,"question_id":358,"full_text":null,"options":"apple;||;banana;||;orange"},{"card_bag_id":1,"answer":null,"branch_question_id":553,"mistake_types":1,"content":"This
			// is an
			// apple3!","id":9,"card_tags_id":[],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ee","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_183.mp3","question_id":356,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":552,"mistake_types":1,"content":"This
			// is an
			// apple2!","id":10,"card_tags_id":[],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"e","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_182.mp3","question_id":356,"full_text":null,"options":null}]}
			if (jsonobject.getString("status").equals("success")) {
				JSONArray jsonarray = jsonobject
						.getJSONArray("knowledges_card");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);
					String id = jsonobject2.getString("id");
					String card_bag_id = jsonobject2.getString("card_bag_id");
					String mistake_types = jsonobject2
							.getString("mistake_types");
					String branch_question_id = jsonobject2
							.getString("branch_question_id");
					String your_answer = jsonobject2.getString("your_answer");
					String created_at = jsonobject2.getString("created_at");
					String updated_at = jsonobject2.getString("updated_at");
					String content = jsonobject2.getString("content");
					String question_id = jsonobject2.getString("question_id");
					String resource_url = jsonobject2.getString("resource_url");
					String types = jsonobject2.getString("types");
					String answer = jsonobject2.getString("answer");

					String options = jsonobject2.getString("options");
					String full_text = jsonobject2.getString("full_text");
					JSONArray tagsarray = jsonobject2 // 当前卡片 已选中的 标签 id
							.getJSONArray("card_tags_id");
					List<Integer> intlist = new ArrayList<Integer>();
					for (int j = 0; j < tagsarray.length(); j++) {
						intlist.add(tagsarray.getInt(j));
					}

					cardList.add(new knowledges_card(id, card_bag_id,
							mistake_types, branch_question_id, your_answer,
							created_at, updated_at, content, question_id,
							resource_url, types, answer, options, full_text,
							intlist));
					if ((i + 1) % 4 == 0) {
						count1++;
						Allmap.put(count1, cardList);
						cardList = new ArrayList<knowledges_card>();
					}

				}
				if (cardList.size() != 0) {
					count1++;
					Allmap.put(count1, cardList);
				}
				JSONArray tags = jsonobject.getJSONArray("tags");// 全部标签
				for (int i = 0; i < tags.length(); i++) {
					Log.i("2222222222222222222", i + "");
					JSONObject jsonobject2 = tags.getJSONObject(i);
					String card_bag_id = jsonobject2.getString("card_bag_id");
					String created_at = jsonobject2.getString("created_at");
					String id = jsonobject2.getString("id");
					String name = jsonobject2.getString("name");
					String update_at = jsonobject2.getString("updated_at");
					tagsList.add(new tags(card_bag_id, created_at, id, name,
							update_at));
					Log.i("22----------", i + "");
				}
				eb.setTagsList(tagsList);
				if (flag) {
					handler.sendEmptyMessage(1);
				}
			} else {
				Message msg = new Message();
				msg.what = 0;
				msg.obj = jsonobject.getString("notice");
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				progressDialog.dismiss();
				Toast.makeText(MCardBagActivity.this, String.valueOf(msg.obj),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				// page = 0;
				cardbagEt.setText("");

				setViewPager();
				progressDialog.dismiss();
				GuidePageAdapter gpa = new GuidePageAdapter();
				viewPager.setAdapter(gpa);

				viewPager.setCurrentItem(page);
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			case 2:
				Toast.makeText(MCardBagActivity.this, String.valueOf(msg.obj),
						Toast.LENGTH_SHORT).show();
				setViewPager();
				viewPager.setAdapter(new GuidePageAdapter());
				int a = 4 * (page - 1);
				if (a == cardList.size()) {
					viewPager.setCurrentItem(page - 1);
				} else {
					viewPager.setCurrentItem(page);
				}

				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			case 7:
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	public void setViewPager() {
		group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		FontCard = new HashMap<Integer, List<View>>();
		viewList = new ArrayList<View>();
		int pageCount = Allmap.size();
		PageBool = new Boolean[pageCount][4];
		inflater2 = LayoutInflater.from(this);
		imageViews = new ImageView[Allmap.size()];
		LinearLayout imageL;
		FontCard.clear();
		viewList.clear();
		group.removeAllViews();
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[i] = null;
		}
		for (int i = 0; i < Allmap.size(); i++) {
			List<View> fontlist = new ArrayList<View>();
			if (Allmap.size() > 1) {
				// 增加下面的小图标
				imageL = new LinearLayout(MCardBagActivity.this);
				imageL.setLayoutParams(new LayoutParams(40, 40));
				imageView = new ImageView(MCardBagActivity.this);
				imageView.setLayoutParams(new LayoutParams(20, 20));
				imageView.setPadding(10, 0, 10, 0);
				imageViews[i] = imageView;
				if (i == 0) {
					imageView.setBackgroundResource(R.drawable.page_indicator);
				} else {
					imageView
							.setBackgroundResource(R.drawable.page_inicator_focused);
				}
				imageL.addView(imageView);
				group.addView(imageL);
			}
			// 图标结束
			ViewGroup view = (android.view.ViewGroup) inflater2.inflate(
					R.layout.cardbag_gridview, null); // 增加viewpager试图
			LinearLayout linear = null;
			List<knowledges_card> list1 = Allmap.get(i + 1);
			for (int j = 0; j < Allmap.get(i + 1).size(); j++) {
				PageBool[i][j] = true; // 所有卡片的T/F
				ViewGroup cardview = (android.view.ViewGroup) inflater2
						.inflate(R.layout.cardbag_gridview, null);
				ViewGroup fontview = (ViewGroup) inflater2.inflate(
						R.layout.cardbag_grdiview_iteam, null);
				if ((j + 1) % 2 != 0) {
					linear = new LinearLayout(MCardBagActivity.this);
					linear.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					linear.setOrientation(LinearLayout.HORIZONTAL);
				}
				if (width == 800) {
					fontview.setPadding(33, 23, 23, 23);
				} else {
					fontview.setPadding(53, 23, 23, 23);
				}
				((ViewGroup) cardview).addView(fontview);
				linear.addView(cardview);
				fontlist.add(cardview);
				if ((j + 1) % 2 != 0) {
					view.addView(linear);
				}
				setFontCard(fontview, list1.get(j), j, i);
			}
			FontCard.put(i, fontlist);
			eb.setFontCard(FontCard);
			viewList.add(view);
		}
		if (Allmap.size() == 0) {
			TextView textview = new TextView(MCardBagActivity.this);
			textview.setText("没有找到符合条件的数据");
			textview.setGravity(Gravity.CENTER);
			textview.setTextSize(33);
			viewList.add(textview);
		}

	}

	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	// 根据错误类型，卡包正面分割 content
	public String checkAns(String str, String types) {
		String content = null;
		int types2 = 0;
		if ("null".equals(types)) {
			types2 = -1;
		} else {
			types2 = Integer.parseInt(types);
		}
		String[] strarr;
		switch (types2) {
		case 0: // "your_answer": "where;||;Where?",
			if (str.indexOf(";||;") != -1) {
				strarr = str.split(";\\|\\|;");
				// for (int i = 0; i < strarr.length; i++) {
				content += strarr[0] + " ";
				// }
				content = content.substring(4, content.length());
			} else {
				if (str.indexOf(";&&;") != -1) {
					content = str.substring(0, str.lastIndexOf(";&&;"));
				} else {
					content = str.substring(0, str.length());
				}

			}
			Log.i("asd", "正面听力case0:" + content);
			return content;
		case 1: // "your_answer": "Dad;||;come;||;and;||;sit;||;here;||;",
			strarr = str.split(";\\|\\|;");
			// for (int i = 0; i < strarr.length; i++) {
			// content += strarr[i] + " ";
			// }
			content += strarr[0] + " ";
			content = content.substring(4, content.length());
			return content;
		case 2:
			return str;
		case 3:
			if (str.indexOf(";||;") != -1) {
				strarr = str.split(";\\|\\|;");
				for (int i = 0; i < strarr.length; i++) {
					content += strarr[i] + " ";
				}
			} else {
				content += str;
			}
			content = content.substring(4, content.length());
			return content;
		case 4: // "your_answer": "333<=>555;||;555<=>333;||;444<=>444",
			// "your_answer": "</><=>%$&!@#$;||;%$&!@#$<=>***;||;***<=></>",
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i].replace("<=>", "--") + "\n";
			}
			content = content.substring(4, content.length());
			return content;
		case 5:
			return str;
		case 6:
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i] + " ";
			}
			content = content.substring(4, content.length());
			return content;
		}
		return null;
	}

	public String FormatAns(String str) {
		String content = "";

		String[] strarr;

		if (str.indexOf(";||;") != -1) {
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i] + " ";
			}
		} else {
			content += str;
		}
		content = content.substring(0, content.length());
		return content;

	}

	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	public String setType(String type) {
		switch (Integer.valueOf(type)) {
		case 0:
			return "听写:";
		case 1:
			return "朗读:";
		case 2:
			return "十速:";
		case 3:
			return "选择:";
		case 4:
			return "连线:";
		case 5:
			return "完型:";
		case 6:
			return "排序:";
		default:
			break;
		}
		return null;
	}

	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	public String setback(String str, String types) {
		String content = null;
		int types2 = 0;
		if ("null".equals(types)) {
			types2 = -1;
		} else {
			types2 = Integer.parseInt(types);
		}
		// String content = null;
		String[] strarr;
		switch (types2) {
		case 0:
			return str;
		case 1:
			return str;
		case 2:
			return str;
		case 3:
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i] + "\n";
			}
			return content.substring(4, content.length());
		case 4:
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				String[] strarr1 = strarr[i].split("<=>");
				for (int j = 0; j < strarr1.length; j++) {
					content += strarr1[j];
					if (j == 0) {
						content += "--";
					}
				}
				content += "\n";
			}
			return content.substring(4, content.length());
		case 6:
			return str;
		}
		return str;
	}

	// 完形填空
	public String settypes5(String full_text, String answer,
			TextView Lookcontent) {

		List arrs = new ArrayList<String>();
		JSONArray answerarray;
		try {
			answerarray = new JSONArray(answer);
			for (int j = 0; j < answerarray.length(); j++) {
				JSONObject ob = answerarray.getJSONObject(j);
				String contentStr = ob.getString("content");
				arrs.add("<u>" + ob.getString("answer") + "</u>");
				Log.i("22----------", ob.getString("answer") + "");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String content = "";
		full_text = full_text.replaceAll("\\[\\[sign\\]\\]",
				" \\[\\[sign\\]\\] ");
		full_text = ExerciseBookTool.del_tag(full_text);
		String[] textarr = full_text.split("\\[\\[sign\\]\\]");
		for (int i = 0; i < textarr.length; i++) {

			Lookcontent.append(textarr[i]);
			if (i <= arrs.size() - 1) {
				Lookcontent.append(Html.fromHtml("<u>" + "___" + "</u>"));
				// content += arrs.get(i);
			}
			// content += textarr[i];
			// if (i <= arrs.size() - 1) {
			// content += arrs.get(i);
			// }
			// for (int j = 0; j < arrs.length; j++) {
			// content += (Html.fromHtml("<u>" + arrs[j] + "</u>"));
			// }
		}
		// content = full_text.replace("[[sign]]", "___");
		return content;
	}

	public void setFontCard(ViewGroup v1, final knowledges_card card,
			final int index, final int page) {
		TextView reson = null;
		TextView all_content;
		ImageView look_all_mes, cardbatread, rightIv;
		ImageView cardbatdel;
		TextView rightanswer;
		TextView rightanswers;
		ViewGroup v = v1;
		if (PageBool[page][index]) {

			reson = (TextView) v.findViewById(R.id.reson);
			all_content = (TextView) v.findViewById(R.id.youranswer);// 题目 原文
			look_all_mes = (ImageView) v.findViewById(R.id.look_all_mes);// 完形填空查看全文
			cardbatdel = (ImageView) v.findViewById(R.id.cardbatdel); // 删除按钮
			cardbatread = (ImageView) v.findViewById(R.id.cardbatread);// 音频按钮
			rightIv = (ImageView) v.findViewById(R.id.rightIv);
			reson.setText(setType(card.getTypes())); // 题目类型
			// all_content.setText(checkAns(card.getYour_answer(), // 你的错误
			// card.getTypes()));
			String playerIP = IP + card.getResource_url();
			if (card.getResource_url().equals("")
					|| card.getResource_url().equals("null")) {
			} else {
				cardbatread.setVisibility(View.VISIBLE);
				final String IP2 = playerIP;
				cardbatread.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						try {
							if (mediaplay.isPlaying()) {
								mediaplay.stop();
							}
							mediaplay = new MediaPlayer();
							mediaplay.setDataSource(IP2);
							mediaplay.prepare();
							mediaplay.start();
							// mediaplay
							// .setOnCompletionListener(new
							// OnCompletionListener() {
							// public void onCompletion(MediaPlayer mp) {
							// mediaplay.release();
							// }
							// });
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}

			if (card.getTypes().equals("5")) { // 完形填空 显示 查看全部 按钮
				all_content.setText("");
				look_all_mes.setVisibility(View.VISIBLE);
				settypes5(card.getFull_text(), card.getAnswer(), all_content);
			} else {
				all_content
						.setText(setback(card.getContent(), card.getTypes()));
			}
			if (card.getTypes().equals("3")) {
				if ((card.getContent().indexOf("<file>") != -1)) { // 包含文件
																	// 信息，处理后再显示
					if ((card.getContent().indexOf(".mp3") != -1)
							|| (card.getContent().indexOf(".amr") != -1)
							|| (card.getContent().indexOf(".wav") != -1)) {
						cardbatread.setVisibility(View.VISIBLE); // 显示 音频 按钮
						playerIP = IP
								+ card.getContent().substring(
										"<file>".length(),
										card.getContent()
												.lastIndexOf("</file>"));
					} else if ((card.getContent().indexOf(".png") != -1)
							|| (card.getContent().indexOf(".jpg") != -1)) {
						final String url = IP
								+ card.getContent().substring(
										"<file>".length(),
										card.getContent()
												.lastIndexOf("</file>"));
						rightIv.setVisibility(View.VISIBLE); // 显示 查看图片 按钮
						rightIv.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								LayoutInflater inflater = LayoutInflater
										.from(MCardBagActivity.this);
								View imgEntryView = inflater.inflate(
										R.layout.answer_select_dialog, null); // 加载自定义的布局文件
								final AlertDialog dialog = new AlertDialog.Builder(
										MCardBagActivity.this).create();
								ImageView img = (ImageView) imgEntryView
										.findViewById(R.id.large_image);
								Bitmap result = memoryCache
										.getBitmapFromCache(url);
								if (result == null) {
									ExerciseBookTool.set_bk(url, img,
											memoryCache);
								} else {
									img.setImageDrawable(new BitmapDrawable(
											result));
								}
								// img.setImageDrawable(Drawable.createFromPath(url));
								dialog.setView(imgEntryView); // 自定义dialog
								dialog.show();
								// 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
								imgEntryView
										.setOnClickListener(new OnClickListener() {
											public void onClick(View paramView) {
												dialog.cancel();
											}
										});

							}
						});

						// Bitmap result = memoryCache.getBitmapFromCache(url);
						// if (result == null) {
						// ExerciseBookTool.set_bk(url, rightIv, memoryCache);
						// } else {
						// rightIv.setImageDrawable(new BitmapDrawable(result));
						// }
						cardbatread.setVisibility(View.GONE);
					}
					String st = card.getContent().substring(
							card.getContent().lastIndexOf("</file>")
									+ "</file>".length(),
							card.getContent().length());
					all_content.setText(st);
					if (st.length() == 0) {
						all_content.setText(setback(card.getOptions(),
								card.getTypes()));
					}

				} else { // 不包含文件 信息，直接显示原题
					all_content.setText(card.getContent());
					// + setback(card.getOptions(), card.getTypes()));
				}
			}
			// if (card.getTypes().equals("1")) {
			// rightanswer.setVisibility(View.GONE);
			// answer.setVisibility(View.GONE);
			// } else {
			// rightanswer.setText("正确答案");
			//
			// if (card.getTypes().equals("0") || card.getTypes().equals("4")) {
			// answer.setText(setback(card.getContent(), card.getTypes()));
			// } else if (card.getTypes().equals("5")) {
			//
			// JSONArray answerarray;
			// try {
			// answerarray = new JSONArray(card.getAnswer());
			// for (int j = 0; j < answerarray.length(); j++) {
			// JSONObject ob = answerarray.getJSONObject(j);
			// String contentStr = ob.getString("content");
			// if (card.getContent().equals(contentStr)) {
			// answer.setText(ob.getString("answer"));
			// break;
			// }
			// }
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// } else {
			// String ans = FormatAns(card.getAnswer());
			// answer.setText(ans);
			// }
			//
			// }

			look_all_mes.setOnClickListener(new OnClickListener() { // 完形查看全文
																	// 按钮监听
						public void onClick(View arg0) {
							Log.i("asd", "page:" + page + "index:" + index);
							Intent intent = new Intent(MCardBagActivity.this,
									LookAllContentActivity.class);
							intent.putExtra("content", card.getContent());
							intent.putExtra("types", card.getTypes());
							intent.putExtra("full_text", card.getFull_text());
							intent.putExtra("answer", card.getAnswer());
							startActivity(intent);
						}
					});

			cardbatdel.setOnClickListener(new OnClickListener() { // 删除按钮
						public void onClick(View v) {
							Dialog dialog = new AlertDialog.Builder(
									MCardBagActivity.this)
									.setTitle("提示")
									.setMessage("您确认要删除么?")
									.setPositiveButton(
											"确认",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													del(card.getId() + "");
												}
											})
									.setNegativeButton(
											"取消",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											}).create();
							dialog.show();

						}
					});

		}
	}

	/*
	 * 删除 卡片
	 */
	public void del(final String id) {
		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("knowledges_card_id", id);
					String json = ExerciseBookTool.sendGETRequest(
							delete_knowledges_card, map);
					JSONObject jsonobj = new JSONObject(json);
					String notice = jsonobj.getString("notice");
					Message msg = new Message();
					if (jsonobj.getString("status").equals("success")) {
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("student_id", student_id);
						map1.put("school_class_id", school_class_id);
						json = ExerciseBookTool.sendGETRequest(
								get_knowledges_card, map1);
						parsejson(json, false);

						msg.what = 2;
						msg.obj = notice;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = notice;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
		if (ExerciseBookTool.isConnect(MCardBagActivity.this)) {

			thread.start();
		} else {
			handler.sendEmptyMessage(7);
		}
	}

	private class GuidePageAdapter extends PagerAdapter {
		public int getCount() {
			return viewList.size();
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			if (arg1 < viewList.size()) {
				((ViewPager) arg0).removeView(viewList.get(arg1));
			}
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(viewList.get(arg1), 0);
			return viewList.get(arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}

		public void finishUpdate(View arg0) {
		}
	}

	private class GuidePageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int arg0) {
			page = arg0;
			Log.i("asd", "pageseke:" + page);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_inicator_focused);
				}
			}
		}
	}

	public int cardType(String type) {

		if (type.equals("默认")) {
			return -1;
		} else if (type.equals("听写")) {
			return 0;
		} else if (type.equals("朗读")) {
			return 1;
		} else if (type.equals("十速")) {
			return 2;
		} else if (type.equals("选择")) {
			return 3;
		} else if (type.equals("连线")) {
			return 4;
		} else if (type.equals("完型")) {
			return 5;
		} else if (type.equals("排序")) {
			return 6;
		}
		return -1;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case -10:
			Bundle bundle = data.getExtras();
			String cardtype = bundle.getString("cardtype");
			spinner_text.setText(cardtype);
			showdialog();
			initbtn();
			final int mistype = cardType(cardtype);
			Thread thread = new Thread() {
				String json;

				public void run() {
					try {
						viewPager = (ViewPager) findViewById(R.id.guidePages);
						Map<String, String> map = new HashMap<String, String>();
						map.put("student_id", student_id);
						map.put("school_class_id", school_class_id);
						if (mistype!=-1) {
							map.put("mistake_types", mistype+"");
						}
						json = ExerciseBookTool.sendGETRequest(
								get_knowledges_card, map);
						initbtn();
						page = 0;
						parsejson(json, false);
						handler.sendEmptyMessage(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			//
			thread.start();
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);

	}


}
