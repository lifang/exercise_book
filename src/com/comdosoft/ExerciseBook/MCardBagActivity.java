package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.media.MediaPlayer.OnCompletionListener;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MCardBagActivity extends Table_TabHost implements Urlinterface,
		Serializable {
	public List<tags> tagsList;
	public Map<Integer, List<knowledges_card>> Allmap;
	public Map<Integer, List<View>> FontCard;
	public Map<Integer, List<View>> BackCard;
	private List<knowledges_card> cardList;
	private int allsize;
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
	int num = 0;

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
		initbtn();
		getKonwledges();
		btnlistClick();
	}

	public void initbtn() {
		btnList = new ArrayList<Button>();
		cardbagEt = (EditText) findViewById(R.id.cardbagEt);
		cardbatFind = (ImageView) findViewById(R.id.cardbatFind);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		btnList.add(button4);
		btnList.add(button1);
		btnList.add(button2);
		btnList.add(button3);
		Allmap = new HashMap<Integer, List<knowledges_card>>();
		cardList = new ArrayList<knowledges_card>();
		tagsList = new ArrayList<tags>();
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
				showdialog();
				Thread thread = new Thread() {
					public void run() {
						try {
							viewPager = (ViewPager) findViewById(R.id.guidePages);
							Map<String, String> map = new HashMap<String, String>();
							map.put("student_id", student_id);
							map.put("school_class_id", school_class_id);
							map.put("name", String.valueOf(cardbagEt.getText()));
							String json = ExerciseBookTool.sendGETRequest(
									get_knowledges_card, map);
							pageAdapter = new GuidePageAdapter();
							parsejson(json, false);
							handler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
		});
		btnList.get(0).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getKonwledges();
			}
		});
		for (int i = 1; i < btnList.size(); i++) {
			final String mistype = String.valueOf(i);
			btnList.get(i).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showdialog();
					Thread thread = new Thread() {
						String json;

						public void run() {
							try {
								viewPager = (ViewPager) findViewById(R.id.guidePages);
								Map<String, String> map = new HashMap<String, String>();
								map.put("student_id", student_id);
								map.put("school_class_id", school_class_id);
								map.put("mistake_types", mistype);
								json = ExerciseBookTool.sendGETRequest(
										get_knowledges_card, map);
								parsejson(json, false);
								handler.sendEmptyMessage(1);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					thread.start();
				}
			});
		}
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
					if (json==null||"".equals(json)) {
						
					}else {
						parsejson(json, true);
					}
					
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
//			{"tags":[{"id":1,"created_at":"2014-03-09T17:06:53+08:00","updated_at":"2014-03-17T17:06:59+08:00","card_bag_id":1,"name":"bag1"},{"id":2,"created_at":"2014-03-12T09:29:40+08:00","updated_at":"2014-03-12T09:29:40+08:00","card_bag_id":1,"name":"第一个"},{"id":3,"created_at":"2014-03-12T09:48:53+08:00","updated_at":"2014-03-12T09:48:53+08:00","card_bag_id":1,"name":null},{"id":4,"created_at":"2014-03-12T10:04:52+08:00","updated_at":"2014-03-12T10:04:52+08:00","card_bag_id":1,"name":"一饿的"},{"id":5,"created_at":"2014-03-12T10:05:19+08:00","updated_at":"2014-03-12T10:05:19+08:00","card_bag_id":1,"name":"一饿的地方 "},{"id":6,"created_at":"2014-03-12T10:07:33+08:00","updated_at":"2014-03-12T10:07:33+08:00","card_bag_id":1,"name":"似懂非懂分"},{"id":7,"created_at":"2014-03-12T10:09:25+08:00","updated_at":"2014-03-12T10:09:25+08:00","card_bag_id":1,"name":"就看看"},{"id":8,"created_at":"2014-03-12T10:15:23+08:00","updated_at":"2014-03-12T10:15:23+08:00","card_bag_id":1,"name":"第er个"},{"id":9,"created_at":"2014-03-12T10:16:08+08:00","updated_at":"2014-03-12T10:16:08+08:00","card_bag_id":1,"name":"第x个"},{"id":10,"created_at":"2014-03-12T10:18:05+08:00","updated_at":"2014-03-12T10:18:05+08:00","card_bag_id":1,"name":"第y个"},{"id":11,"created_at":"2014-03-12T10:20:55+08:00","updated_at":"2014-03-12T10:20:55+08:00","card_bag_id":1,"name":"hh"},{"id":12,"created_at":"2014-03-19T10:51:54+08:00","updated_at":"2014-03-19T10:51:54+08:00","card_bag_id":1,"name":"??"},{"id":13,"created_at":"2014-03-19T10:55:14+08:00","updated_at":"2014-03-19T10:55:14+08:00","card_bag_id":1,"name":"???"},{"id":14,"created_at":"2014-03-19T11:02:20+08:00","updated_at":"2014-03-19T11:02:20+08:00","card_bag_id":1,"name":"????"},{"id":15,"created_at":"2014-03-19T11:09:21+08:00","updated_at":"2014-03-19T11:09:21+08:00","card_bag_id":1,"name":"fghhg"},{"id":16,"created_at":"2014-03-19T11:11:30+08:00","updated_at":"2014-03-19T11:11:30+08:00","card_bag_id":1,"name":"fghh"},{"id":17,"created_at":"2014-03-19T11:12:38+08:00","updated_at":"2014-03-19T11:12:38+08:00","card_bag_id":1,"name":"a"},{"id":18,"created_at":"2014-03-19T11:14:17+08:00","updated_at":"2014-03-19T11:14:17+08:00","card_bag_id":1,"name":"b"},{"id":19,"created_at":"2014-03-19T11:15:32+08:00","updated_at":"2014-03-19T11:15:32+08:00","card_bag_id":1,"name":"c"},{"id":20,"created_at":"2014-03-19T11:17:51+08:00","updated_at":"2014-03-19T11:17:51+08:00","card_bag_id":1,"name":"d"},{"id":21,"created_at":"2014-03-19T11:19:27+08:00","updated_at":"2014-03-19T11:19:27+08:00","card_bag_id":1,"name":"e"},{"id":22,"created_at":"2014-03-19T11:20:38+08:00","updated_at":"2014-03-19T11:20:38+08:00","card_bag_id":1,"name":"f"},{"id":23,"created_at":"2014-03-19T11:23:13+08:00","updated_at":"2014-03-19T11:23:13+08:00","card_bag_id":1,"name":"g"},{"id":24,"created_at":"2014-03-19T11:25:36+08:00","updated_at":"2014-03-19T11:25:36+08:00","card_bag_id":1,"name":"h"},{"id":25,"created_at":"2014-03-19T11:33:26+08:00","updated_at":"2014-03-19T11:33:26+08:00","card_bag_id":1,"name":"j"},{"id":26,"created_at":"2014-03-19T11:34:05+08:00","updated_at":"2014-03-19T11:34:05+08:00","card_bag_id":1,"name":"k"},{"id":27,"created_at":"2014-03-19T11:39:44+08:00","updated_at":"2014-03-19T11:39:44+08:00","card_bag_id":1,"name":"l"},{"id":28,"created_at":"2014-03-19T11:42:02+08:00","updated_at":"2014-03-19T11:42:02+08:00","card_bag_id":1,"name":"o"},{"id":29,"created_at":"2014-03-19T11:49:29+08:00","updated_at":"2014-03-19T11:49:29+08:00","card_bag_id":1,"name":"第sdfds"},{"id":30,"created_at":"2014-03-19T11:49:49+08:00","updated_at":"2014-03-19T11:49:49+08:00","card_bag_id":1,"name":"第三四十"},{"id":31,"created_at":"2014-03-19T11:50:34+08:00","updated_at":"2014-03-19T11:50:34+08:00","card_bag_id":1,"name":"??????"},{"id":32,"created_at":"2014-03-19T11:50:55+08:00","updated_at":"2014-03-19T11:50:55+08:00","card_bag_id":1,"name":"共用回家"},{"id":33,"created_at":"2014-03-19T13:55:15+08:00","updated_at":"2014-03-19T13:55:15+08:00","card_bag_id":1,"name":"tty"},{"id":34,"created_at":"2014-03-19T13:56:03+08:00","updated_at":"2014-03-19T13:56:03+08:00","card_bag_id":1,"name":"fas"},{"id":35,"created_at":"2014-03-19T13:57:28+08:00","updated_at":"2014-03-19T13:57:28+08:00","card_bag_id":1,"name":"iyt"},{"id":36,"created_at":"2014-03-19T14:00:53+08:00","updated_at":"2014-03-19T14:00:53+08:00","card_bag_id":1,"name":"uuiiy"},{"id":37,"created_at":"2014-03-19T14:05:51+08:00","updated_at":"2014-03-19T14:05:51+08:00","card_bag_id":1,"name":"rty"},{"id":38,"created_at":"2014-03-19T14:08:49+08:00","updated_at":"2014-03-19T14:08:49+08:00","card_bag_id":1,"name":"rtyu"},{"id":39,"created_at":"2014-03-19T14:09:57+08:00","updated_at":"2014-03-19T14:09:57+08:00","card_bag_id":1,"name":"jh"},{"id":40,"created_at":"2014-03-19T14:12:59+08:00","updated_at":"2014-03-19T14:12:59+08:00","card_bag_id":1,"name":"dfgt"},{"id":41,"created_at":"2014-03-19T14:17:05+08:00","updated_at":"2014-03-19T14:17:05+08:00","card_bag_id":1,"name":"errt"},{"id":42,"created_at":"2014-03-19T14:18:31+08:00","updated_at":"2014-03-19T14:18:31+08:00","card_bag_id":1,"name":"uhnk"},{"id":43,"created_at":"2014-03-19T14:20:19+08:00","updated_at":"2014-03-19T14:20:19+08:00","card_bag_id":1,"name":"bgjnf"},{"id":44,"created_at":"2014-03-19T14:21:37+08:00","updated_at":"2014-03-19T14:21:37+08:00","card_bag_id":1,"name":"xxdfv"},{"id":45,"created_at":"2014-03-19T14:22:47+08:00","updated_at":"2014-03-19T14:22:47+08:00","card_bag_id":1,"name":"bcxzs"},{"id":46,"created_at":"2014-03-19T14:28:52+08:00","updated_at":"2014-03-19T14:28:52+08:00","card_bag_id":1,"name":"rtyy"},{"id":47,"created_at":"2014-03-25T17:14:46+08:00","updated_at":"2014-03-25T17:14:46+08:00","card_bag_id":1,"name":"秋雨鱼"},{"id":48,"created_at":"2014-03-25T17:16:14+08:00","updated_at":"2014-03-25T17:16:14+08:00","card_bag_id":1,"name":"秋"},{"id":49,"created_at":"2014-03-28T10:47:46+08:00","updated_at":"2014-03-28T10:47:46+08:00","card_bag_id":1,"name":"ffff"}],"notice":"获取成功！！","status":"success","knowledges_card":[{"card_bag_id":1,"answer":null,"branch_question_id":556,"mistake_types":2,"content":"This is an apple!","id":2,"card_tags_id":[3],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ww","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_186.mp3","question_id":357,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":554,"mistake_types":1,"content":"This is an apple4!","id":3,"card_tags_id":[1,1,1,29,30],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ww","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_184.mp3","question_id":356,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":551,"mistake_types":1,"content":"This is an apple1!","id":4,"card_tags_id":[1,3,6,10,11,5,7,8,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"qq","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_181.mp3","question_id":356,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":536,"mistake_types":2,"content":"I write very hard","id":5,"card_tags_id":[1,2,3,5],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"qq","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/62\/questions_package_275\/media_536.wav","question_id":351,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":538,"mistake_types":2,"content":"I write you a work","id":6,"card_tags_id":[7,4,1,5],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ww","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/62\/questions_package_274\/media_538.wav","question_id":352,"full_text":null,"options":null},{"card_bag_id":1,"answer":"apple","branch_question_id":558,"mistake_types":3,"content":"<file>apple.jpg<\/file>","id":7,"card_tags_id":[11,1,4],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ee","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":null,"question_id":358,"full_text":null,"options":"apple;||;banana;||;orange"},{"card_bag_id":1,"answer":"apple","branch_question_id":559,"mistake_types":3,"content":"<file>apple.wav<\/file>","id":8,"card_tags_id":[],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ee","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":null,"question_id":358,"full_text":null,"options":"apple;||;banana;||;orange"},{"card_bag_id":1,"answer":null,"branch_question_id":553,"mistake_types":1,"content":"This is an apple3!","id":9,"card_tags_id":[],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"ee","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_183.mp3","question_id":356,"full_text":null,"options":null},{"card_bag_id":1,"answer":null,"branch_question_id":552,"mistake_types":1,"content":"This is an apple2!","id":10,"card_tags_id":[],"updated_at":"2014-03-12T15:58:49+08:00","your_answer":"e","created_at":"2014-03-12T15:58:49+08:00","types":null,"resource_url":"\/question_packages\/201402\/questions_package_222\/media_182.mp3","question_id":356,"full_text":null,"options":null}]}
			if (jsonobject.getString("status").equals("success")) {
				JSONArray jsonarray = jsonobject
						.getJSONArray("knowledges_card");
				allsize = jsonarray.length();
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
					Log.i("2222222222222222222",i+"");
					JSONObject jsonobject2 = tags.getJSONObject(i);
					String card_bag_id = jsonobject2.getString("card_bag_id");
					String created_at = jsonobject2.getString("created_at");
					String id = jsonobject2.getString("id");
					String name = jsonobject2.getString("name");
					String update_at = jsonobject2.getString("updated_at");
					tagsList.add(new tags(card_bag_id, created_at, id, name,
							update_at));
					Log.i("22----------",i+"");
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
//				page = 0;
				setViewPager();
				progressDialog.dismiss();
				GuidePageAdapter gpa = new GuidePageAdapter();
				viewPager.setAdapter(gpa);
				
				viewPager.setCurrentItem(page);
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			case 2:
				setViewPager();
				viewPager.setAdapter(new GuidePageAdapter());
				viewPager.setCurrentItem(page);
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
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
			oneClick();
		}
		if (Allmap.size() == 0) {
			TextView textview = new TextView(MCardBagActivity.this);
			textview.setText("没有找到符合条件的数据");
			textview.setGravity(Gravity.CENTER);
			textview.setTextSize(33);
			viewList.add(textview);
		}

	}

	public void NoClick(int page, int index) {
		List<View> fontlist = FontCard.get(page);
		List<View> backlist = FontCard.get(page);
		for (int i = 0; i < fontlist.size(); i++) {
			if (i != index) {
				backlist.get(i).setClickable(false);
				fontlist.get(i).setClickable(false);
			}
		}
	}

	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	// 根据错误类型，卡包正面分割 content
	public String checkAns(String str, String types) {
		String content = null;
		int types2=0;
		if ("null".equals(types)) {
			types2=-1;
		}else {
			types2= Integer.parseInt(types);
		}
		String[] strarr;
		switch (types2) {
		case 0:
			if (str.indexOf(";||;") != -1) {
				strarr = str.split(";\\|\\|;");
				for (int i = 0; i < strarr.length; i++) {
					content += strarr[i] + " ";
				}
				content = content.substring(4, content.length() - 5);
			} else {
				content = str.substring(0, str.lastIndexOf(";&&;"));
			}
			Log.i("asd", "正面听力case0:" + content);
			return content;
		case 1:
			return str;
		case 2:
			return str;
		case 3:
			return str;
		case 4:
			strarr = str.split("  ");
			for (int i = 0; i < strarr.length; i++) {
				String[] strarr1 = strarr[i].split(" ");
				for (int j = 0; j < strarr1.length; j++) {
					content += strarr1[j] + " ";
				}
				content += "\n";
			}
			return content.substring(4, content.length());
		case 5:
			return str;
		case 6:
			strarr = str.split(" ");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i] + " ";
			}
			return content.substring(4, content.length());
		}
		return null;
	}

	public void oneClick() {
		for (int i = 0; i < FontCard.size(); i++) {
			for (int j = 0; j < FontCard.get(i).size(); j++) {
				final int j1 = j;
				final View view = FontCard.get(i).get(j);
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v1) {
						mindex = j1;
						NoClick(page, j1);
						applyRotation(0, 90, view);
					}
				});
			}
		}
	}

	public String setWrong(String miskatype) {
		switch (Integer.valueOf(miskatype)) {
		case 1:
			return "读错";
		case 2:
			return "拼错";
		case 3:
			return "选错";
		default:
			break;
		}
		return null;
	}

	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	// 背面
	public String setback(String str, String types) {
		String content = null;
		int types2=0;
		if ("null".equals(types)) {
			types2=-1;
		}else {
			types2= Integer.parseInt(types);
		}
//		String content = null;
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
					content += strarr1[j] + " ";
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
	public void settypes5(String full_text, String options) {
		String content = null;
		String[] arrs = options.split(";\\|\\|;");
		full_text = ExerciseBookTool.del_tag(full_text);
		String[] textarr = full_text.split("[[sign]]");
		for (int i = 0; i < textarr.length; i++) {
			content += textarr[i];
			for (int j = 0; j < arrs.length; j++) {
				content += (Html.fromHtml("<u>" + arrs[j] + "</u>"));
			}
		}
	}

	public void setFontCard(ViewGroup v1, final knowledges_card card,
			final int index, final int page) {
		TextView reson = null;
		TextView wronganswer;
		ImageView fontIv;
		TextView rightanswer;
		TextView youranswer;
		TextView rightanswers;
		ViewGroup v = v1;
		TextView bqtv; // 显示已添加的标签
		ImageView rightIv;
		if (PageBool[page][index]) {
			bqtv = (TextView) v.findViewById(R.id.bqtv1);
			String bqtvStr = "v.";
			for (int i = 0; i < tagsList.size(); i++) {
				for (int j = 0; j < card.getTagsarr().size(); j++) {
					if (card.getTagsarr().get(j) == Integer.valueOf(tagsList
							.get(i).getId())) {
						bqtvStr = bqtvStr + tagsList.get(i).getName() + ",";
					}
				}
			}

			if (bqtvStr.length() > 2) {
				bqtvStr = bqtvStr.substring(0, bqtvStr.length() - 1);
				bqtv.setVisibility(View.VISIBLE);
				bqtv.setText(bqtvStr);
			}

			reson = (TextView) v.findViewById(R.id.reson);
			wronganswer = (TextView) v.findViewById(R.id.youranswer);
			fontIv = (ImageView) v.findViewById(R.id.fontIv);
			rightanswer = (TextView) v.findViewById(R.id.rightanswer);
			youranswer = (TextView) v.findViewById(R.id.answer);
			reson.setText(setWrong(card.getMistake_types())); // 错误类型
			if (card.getTypes().equals("0") || card.getTypes().equals("1")
					|| card.getTypes().equals("4")) {
				rightanswer.setVisibility(View.GONE);
				youranswer.setVisibility(View.GONE);
			} else {
				rightanswer.setText("正确答案");
				youranswer.setText(card.getAnswer());
			}

			wronganswer.setText(checkAns(card.getYour_answer(), // 你的错误
					card.getTypes()));
			fontIv.setOnClickListener(new OnClickListener() { // 标签按钮监听
//					Integer.valueOf((card.getTypes() == null || card.getTypes()
//							.equals("null")) ? "0" : card.getTypes())));
//			fontIv.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					Log.i("asd", "page:" + page + "index:" + index);
					Intent intent = new Intent(MCardBagActivity.this,
							MCardTag.class);
					Bundle mBundle = new Bundle();
					eb.setAllmap(Allmap.get(page + 1));
					tagsList = eb.getTagsList();
					eb.setTagsarr(card.getTagsarr());
					num = index;
					mBundle.putInt("page", page);
					mBundle.putInt("index", index);
					mBundle.putString("getid", card.getId());
					intent.putExtras(mBundle);
					startActivityForResult(intent, 1);
				}
			});
		} else {
			ImageView cardbatread;
			ImageView cardbatdel;
			reson = (TextView) v.findViewById(R.id.backreson);
			rightanswers = (TextView) v.findViewById(R.id.rightanswerb);
			cardbatread = (ImageView) v.findViewById(R.id.cardbatread);
			cardbatdel = (ImageView) v.findViewById(R.id.cardbatdel);
			rightIv = (ImageView) v.findViewById(R.id.rightIv);
			String playerIP = IP + card.getResource_url();
			reson.setText("原题:");
			rightanswers.setText(setback(card.getContent(),
					card.getTypes()));
			if (card.getTypes().equals("0") || card.getTypes().equals("1")
					|| card.getTypes().equals("3")) {
				cardbatread.setVisibility(View.GONE);
			}
			if (card.getTypes().equals("3")) {
				if ((card.getContent().indexOf("<file>") != -1)) {
					if ((card.getContent().indexOf(".mp3") != -1)
							|| (card.getContent().indexOf(".amr") != -1)
							|| (card.getContent().indexOf(".wav") != -1)) {
						playerIP = IP
								+ card.getContent().substring(
										"<file>".length(),
										card.getContent()
												.lastIndexOf("</file>"));
					} else if ((card.getContent().indexOf(".png") != -1)
							|| (card.getContent().indexOf(".jpg") != -1)) {
						rightIv.setVisibility(View.VISIBLE);
						String url = IP
								+ card.getContent().substring(
										"<file>".length(),
										card.getContent()
												.lastIndexOf("</file>"));
						Bitmap result = memoryCache.getBitmapFromCache(url);
						if (result == null) {
							ExerciseBookTool.set_bk(url, rightIv, memoryCache);
						} else {
							rightIv.setImageDrawable(new BitmapDrawable(result));
						}
						cardbatread.setVisibility(View.GONE);
					}
					rightanswers.setText(card.getContent().substring(
							card.getContent().lastIndexOf("</file>")
									+ "</file>".length(),
							card.getContent().length())
							+ setback(card.getOptions(),
									card.getTypes()));
				} else {
					rightanswers.setText(card.getContent()
							+ "\n"
							+ setback(card.getOptions(),
									card.getTypes()));
				}
			} else if (card.getResource_url().equals(""))
				cardbatread.setVisibility(View.GONE);
			if (card.getTypes().equals("5")) {
				settypes5(card.getFull_text(), card.getOptions());
			}
			final String IP2 = playerIP;
			cardbatread.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						mediaplay.setDataSource(IP2);
						mediaplay.prepare();
						mediaplay.start();
						mediaplay
								.setOnCompletionListener(new OnCompletionListener() {
									public void onCompletion(MediaPlayer mp) {
										mediaplay.release();
									}
								});
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
			cardbatdel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Dialog dialog = new AlertDialog.Builder(
							MCardBagActivity.this)
							.setTitle("提示")
							.setMessage("您确认要删除么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											Thread thread = new Thread() {
												public void run() {
													try {
														Map<String, String> map = new HashMap<String, String>();
														map.put("knowledges_card_id",
																card.getId());
														String json = ExerciseBookTool
																.sendGETRequest(
																		delete_knowledges_card,
																		map);
														JSONObject jsonobj = new JSONObject(
																json);
														String notice = jsonobj
																.getString("notice");
														Message msg = new Message();
														if (jsonobj
																.getString(
																		"status")
																.equals("success")) {
															Map<String, String> map1 = new HashMap<String, String>();
															map1.put(
																	"student_id",
																	student_id);
															map1.put(
																	"school_class_id",
																	school_class_id);
															json = ExerciseBookTool
																	.sendGETRequest(
																			get_knowledges_card,
																			map1);
															parsejson(json,
																	false);
															handler.sendEmptyMessage(2);
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
											thread.start();
										}
									})
							.setNegativeButton("取消",
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

	private void applyRotation(float start, float end, View selectview) {
		// // 计算中心点
		final float centerX = selectview.getWidth() / 2.0f;
		final float centerY = selectview.getHeight() / 2.0f;
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		// 设置监听
		rotation.setAnimationListener(new DisplayNextView());
		selectview.startAnimation(rotation);
	}

	private final class DisplayNextView implements Animation.AnimationListener {

		public void onAnimationStart(Animation animation) {
		}

		// 动画结束
		public void onAnimationEnd(Animation animation) {
			List<View> fontlist = FontCard.get(page);
			if (mediaplay.isPlaying()) {
				mediaplay.stop();
			}
			List<knowledges_card> cardl = Allmap.get(page + 1);
			Log.i("3", "page:" + page + "mindex:" + mindex + "FontCard:"
					+ FontCard.get(page).size());
			View view;
			ViewGroup viewgroup = (ViewGroup) fontlist.get(mindex);
			if (PageBool[page][mindex]) {
				PageBool[page][mindex] = false;
				view = (ViewGroup) inflater2.inflate(
						R.layout.cardbag_gridview_back, null);
			} else {
				PageBool[page][mindex] = true;
				view = (ViewGroup) inflater2.inflate(
						R.layout.cardbag_grdiview_iteam, null);
			}
			if (width == 800) {
				view.setPadding(33, 23, 23, 23);
			} else {
				view.setPadding(53, 23, 23, 23);
			}
			viewgroup.removeAllViews();
			viewgroup.addView(view);
			setFontCard((ViewGroup) view, cardl.get(mindex), mindex, page);
			for (int i = 0; i < fontlist.size(); i++) {
				fontlist.get(i).setClickable(true);
			}
			final float centerX = viewgroup.getWidth() / 2.0f;
			final float centerY = viewgroup.getHeight() / 2.0f;
			Rotate3dAnimation rotation = null;
			viewgroup.requestFocus();
			rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, 310.0f,
					false);
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			// 开始动画
			viewgroup.startAnimation(rotation);
		}

		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case -12:
//			List<tags> tagsList2 = eb.getTagsList();
//			TextView tv = (TextView) FontCard.get(page).get(num)
//					.findViewById(R.id.bqtv1);
//			tv.setVisibility(View.VISIBLE);
//			String bqtvStr = "v.";
////			Allmap.get(page + 1).get(mindex).setTagsarr(eb.getTagsarr());
//			for (int i = 0; i < tagsList2.size(); i++) {
//				for (int j = 0; j < eb.getTagsarr().size(); j++) {
//					if (eb.getTagsarr().get(j) == Integer.valueOf(tagsList.get(
//							i).getId())) {
//						bqtvStr = bqtvStr + tagsList.get(i).getName() + ",";
//					}
//				}
//			}
//
//			if (bqtvStr.length() > 2) {
//				bqtvStr = bqtvStr.substring(0, bqtvStr.length() - 1);
//				tv.setVisibility(View.VISIBLE);
//				tv.setText(bqtvStr);
//			}
			initbtn();
			getKonwledges();
			btnlistClick();
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
