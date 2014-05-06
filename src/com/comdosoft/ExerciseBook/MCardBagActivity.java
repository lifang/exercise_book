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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
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
	private Handler handler = new Handler() {
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

	public void btnlistClick() { // 搜索框 设置监听
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
						spinner_text.setText("");
						showdialog();
						// changeBtn(-1);
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
//					JSONArray tagsarray = jsonobject2 // 当前卡片 已选中的 标签 id
//							.getJSONArray("card_tags_id");
//					List<Integer> intlist = new ArrayList<Integer>();
//					for (int j = 0; j < tagsarray.length(); j++) {
//						intlist.add(tagsarray.getInt(j));
//					}
					List<Integer> intlist = new ArrayList<Integer>();
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
//				JSONArray tags = jsonobject.getJSONArray("tags");// 全部标签
//				for (int i = 0; i < tags.length(); i++) {
//					Log.i("2222222222222222222", i + "");
//					JSONObject jsonobject2 = tags.getJSONObject(i);
//					String card_bag_id = jsonobject2.getString("card_bag_id");
//					String created_at = jsonobject2.getString("created_at");
//					String id = jsonobject2.getString("id");
//					String name = jsonobject2.getString("name");
//					String update_at = jsonobject2.getString("updated_at");
//					tagsList.add(new tags(card_bag_id, created_at, id, name,
//							update_at));
//					Log.i("22----------", i + "");
//				}
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
		String content = "";
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
				content += strarr[0] + "";
				// }
				content = content.substring(0, content.length());
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
			content += strarr[0] + "";
			content = content.substring(0, content.length());
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
			content = content.substring(0, content.length());
			return content;
		case 4: // "your_answer": "333<=>555;||;555<=>333;||;444<=>444",
			// "your_answer": "</><=>%$&!@#$;||;%$&!@#$<=>***;||;***<=></>",
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i].replace("<=>", "--") + "\n";
			}
			content = content.substring(0, content.length());
			return content;
		case 5:
			return str;
		case 6:
			strarr = str.split(";\\|\\|;");
			for (int i = 0; i < strarr.length; i++) {
				content += strarr[i] + " ";
			}
			content = content.substring(0, content.length());
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
		String content = "";
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
			return content.substring(0, content.length());
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
			return content.substring(0, content.length());
		case 6:
			return str;
		}
		return str;
	}

	// 完形填空
	public String settypes5(String full_text, String answer,
			TextView Lookcontent, String index) {

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
//		full_text = full_text.replaceAll("\\[\\[sign\\]\\]",
//				" \\[\\[sign\\]\\] ");
		full_text = ExerciseBookTool.del_tag(full_text);
		Log.i("22", full_text + "");

//		String[] textarr = full_text.split("\\[\\[sign\\]\\]");
//		for (int i = 0; i < textarr.length; i++) {
//
//			Lookcontent.append(textarr[i]);
//			if (i <= arrs.size() - 1) {
//				Lookcontent.append("___");
//				// content += arrs.get(i);
//			}
//		}
		String str = JiSuan(full_text,Integer.parseInt(index)).replaceAll("\\[\\[sign\\]\\]", "___");
		Lookcontent.append(str);
		return content;
	}

	
	public static String JiSuan(String content, int index) {

		String strnull = getStringCL(content);
		String[] str_arr = strnull.split(" ");
		int sign_item = 0;// sign是第几个
		int item = 0;// sign所在的索引
		for (int i = 0; i < str_arr.length; i++) {
			if (str_arr[i].equals("[[sign]]")) {
				sign_item += 1;
				if (sign_item == index) {
					item = i;
				}
			}
		}
		int left = left(str_arr, item);
		int right = right(str_arr, item);
		System.out.println(left + "-" + right);
		StringBuffer sb = new StringBuffer("");
		for (int i = left; i <= right; i++) {
			if (!str_arr[i].equals("")) {
				sb.append(str_arr[i]).append(" ");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb.toString());
		return sb.toString();
	}

	public static String getStringCL(String content) {// 处理字符串
		String str = content.replaceAll("\\[\\[sign\\]\\]", " [[sign]] ");
		for (int i = 0; i < BD.length; i++) {
			String bd = BD[i];
			if (str.contains(bd)) {
				str = str.replaceAll("\\" + bd, " " + bd + " ");// 前后追加空格
			}
		}
		String strnull = str.replaceAll("\\s", " ");
		System.out.println(strnull);
		return strnull;
	}

	public static int left(String[] str_arr, int item) {
		for (int i = item; i > 0; i--) {
			for (int j = 0; j < BD.length; j++) {
				if (str_arr[i].contains(BD[j])) {
					return i + 1;
				}
			}
		}
		return 0;
	}

	public static int right(String[] str_arr, int item) {
		for (int i = item; i < str_arr.length; i++) {
			for (int j = 0; j < BD.length; j++) {
				if (str_arr[i].contains(BD[j])) {
					return i;
				}
			}
		}
		return str_arr.length - 1;
	}

	public static String[] BD = new String[] { ",", ".", "!", "?","，","。","！","？" };
	
	/*
	 * 显示选项 正确 错误分别显示
	 */
	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	public void set_item(knowledges_card card, TextView textview) {
		String content = null;
		textview.setText("");
		int types2 = 0;
		if ("null".equals(card.getTypes())) {
			types2 = -1;
		} else {
			types2 = Integer.parseInt(card.getTypes());
		}
		String Str2 = "";
		// String content = null;
		String[] strarr1;
		String[] strarr2;
		switch (types2) {
		case 0:
		case 1:
		case 6:
			strarr1 = card.getContent().split(" ");
			String str = checkAns(card.getYour_answer(), types2 + "");
			str = str.replaceAll("  ", " ");
			str = str.replaceAll("  ", " ");
			strarr2 = str.split(" ");
			SpannableStringBuilder style1 = new SpannableStringBuilder(str);

			for (int i = 0; i < strarr2.length; i++) {
				if (strarr1[i].equals(strarr2[i])) {

					style1.setSpan(new ForegroundColorSpan(Color.GREEN),
							Str2.length(), Str2.length() + strarr2[i].length(),
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					Str2 = Str2 + strarr2[i] + " ";
				} else {
					style1.setSpan(new ForegroundColorSpan(Color.RED),
							Str2.length(), Str2.length() + strarr2[i].length(),
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					Str2 = Str2 + strarr2[i] + " ";
				}
			}
			textview.setText(style1);
			break;
		case 2:
			String s1 = card.getAnswer();
			String s2 = card.getYour_answer();
			String s3 = s1 + "\n" + s2;
			SpannableStringBuilder style2 = new SpannableStringBuilder(s3);
			style2.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					s1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			style2.setSpan(new ForegroundColorSpan(Color.RED), s1.length() + 1,
					s3.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			textview.setText(style2);
			break;
		case 3:
			String yourA_3[] = card.getYour_answer().split(";\\|\\|;");// 错误答案
			String rightA_3[] = card.getAnswer().split(";\\|\\|;"); // 正确答案
			String options_3[] = null; // 选项 数组
			options_3 = card.getOptions().split(";\\|\\|;");
			String s3_1 = getOptionsStr(options_3);
			String s3_2 = "";
			String s3_3 = "";
			SpannableStringBuilder style3 = new SpannableStringBuilder(s3_1);
			for (int i = 65; i < 65 + options_3.length; i++) {
				for (int j = 0; j < yourA_3.length; j++) {
					Log.i("22----------", yourA_3[j] + "------yourA_3[j]");
					Log.i("22----------", options_3[i - 65]
							+ "-------options_3[i - 65]");
					Log.i("22----------", s3_1 + "-------s3_1");
					if (yourA_3[j].equals(options_3[i - 65])) { // 先设置自己的错误答案颜色
						style3.setSpan(new ForegroundColorSpan(Color.RED),
								s3_2.length(), s3_2.length()
										+ options_3[i - 65].length() + 2,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

					}
				}
				s3_2 = s3_2 + (char) i + "." + options_3[i - 65] + "\n";
			}
			for (int i = 65; i < 65 + options_3.length; i++) {
				for (int j = 0; j < rightA_3.length; j++) {
					if (rightA_3[j].equals(options_3[i - 65])) { // 再设置正确的答案颜色
						style3.setSpan(new ForegroundColorSpan(Color.GREEN),
								s3_3.length(), s3_3.length()
										+ options_3[i - 65].length() + 2,
								Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}
				}
				s3_3 = s3_3 + (char) i + "." + options_3[i - 65] + "\n";
			}
			textview.setText(style3);

			break;
		case 4:
			Str2 = "";
			String s4_1 = card.getContent();
			String s4_2 = card.getYour_answer();
			s4_1 = s4_1.replaceAll("<=>", "--");
			strarr1 = s4_1.split(";\\|\\|;");
			s4_2 = s4_2.replaceAll("<=>", "--");
			strarr2 = s4_2.split(";\\|\\|;");
			String str4 = checkAns(card.getYour_answer(), types2 + "");

			SpannableStringBuilder style4 = new SpannableStringBuilder(str4);
			Boolean tf = false;
			for (int i = 0; i < strarr2.length; i++) {
				tf = false;
				for (int j = 0; j < strarr1.length; j++) {
					if (strarr2[i].equals(strarr1[j])) {
						tf = true;
						break;
					}
				}
				if (tf) {

					style4.setSpan(new ForegroundColorSpan(Color.GREEN),
							Str2.length(), Str2.length() + strarr2[i].length(),
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					Str2 = Str2 + strarr2[i] + "\n";
				} else {
					style4.setSpan(new ForegroundColorSpan(Color.RED),
							Str2.length(), Str2.length() + strarr2[i].length(),
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					Str2 = Str2 + strarr2[i] + "\n";
				}

			}
			textview.setText(style4);
			break;
		case 5: // A 65
			String yourA = card.getYour_answer();// 错误答案
			String rightA = ""; // 正确答案
			String options[] = null; // 选项 数组
			if (card.getOptions().indexOf(";||;") != -1) {
				options = card.getOptions().split(";\\|\\|;");
			} else {
				options[0] = card.getOptions();
			}
			String s5_1 = getOptionsStr(options);
			String s5_2 = "";
			JSONArray answerarray;
			try {
				answerarray = new JSONArray(card.getAnswer());
				for (int j = 0; j < answerarray.length(); j++) {
					JSONObject ob = answerarray.getJSONObject(j);
					String contentStr = ob.getString("content");
					if (card.getContent().equals(contentStr)) {
						rightA = ob.getString("answer");
						break;
					}
				}
			} catch (JSONException e) {
			}
			SpannableStringBuilder style5 = new SpannableStringBuilder(s5_1);

			for (int i = 65; i < 65 + options.length; i++) {
				if (rightA.equals(options[i - 65])) {

					style5.setSpan(new ForegroundColorSpan(Color.GREEN),
							s5_2.length(),
							s5_2.length() + options[i - 65].length() + 2,
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				} else if (yourA.equals(options[i - 65])) {
					style5.setSpan(new ForegroundColorSpan(Color.RED),
							s5_2.length(),
							s5_2.length() + options[i - 65].length() + 2,
							Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

				}
				s5_2 = s5_2 + (char) i + "." + options[i - 65] + "\n";
			}
			textview.setText(style5);

			break;
		}
	}

	public String getOptionsStr(String[] options) {
		String s = "";
		for (int i = 65; i < 65 + options.length; i++) {
			s = s + (char) i + "." + options[i - 65] + "\n";
		}
		return s;

	}

	/*
	 * 设置卡片内容
	 */
	public void setFontCard(ViewGroup v1, final knowledges_card card,
			final int index, final int page) {
		TextView reson = null;
		TextView all_content;
		ImageView look_all_mes, cardbatread, rightIv;
		ImageView cardbatdel;
		TextView select_item; // 正错误答案
		ViewGroup v = v1;
		if (PageBool[page][index]) {

			reson = (TextView) v.findViewById(R.id.reson);
			all_content = (TextView) v.findViewById(R.id.youranswer);// 题目 原文
			select_item = (TextView) v.findViewById(R.id.select_item);// 正错误答案
			look_all_mes = (ImageView) v.findViewById(R.id.look_all_mes);// 完形填空查看全文
			cardbatdel = (ImageView) v.findViewById(R.id.cardbatdel); // 删除按钮
			cardbatread = (ImageView) v.findViewById(R.id.cardbatread);// 音频按钮
			rightIv = (ImageView) v.findViewById(R.id.rightIv);
			reson.setText(setType(card.getTypes())); // 题目类型
			set_item(card, select_item);

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
				settypes5(card.getFull_text(), card.getAnswer(), all_content,card.getContent());
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
						if (mistype != -1) {
							map.put("mistake_types", mistype + "");
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
