package com.comdosoft.ExerciseBook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comdosoft.ExerciseBook.Adapter.LabelAdapter;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MCardTag extends Activity implements Urlinterface, Serializable {
	LinearLayout biaoqian;
	RelativeLayout tagll;
	TextView biaoqianet;
	ListView biaoqianlv;
	TextView newTv;
	List<tags> tagsList;
	List<Integer> mytags;
	String student_id;
	String school_class_id;
	List<knowledges_card> MyMap;
	String id;
	int index;
	ExerciseBook eb;
	int width;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cardtag);
		width = getWindowManager().getDefaultDisplay().getWidth();
		eb = (ExerciseBook) getApplication();
		MyMap = eb.getAllmap();
		tagsList = eb.getTagsList();
		mytags = eb.getTagsarr();
		Bundle extras = getIntent().getExtras();
		id = extras.getString("getid");
		index = extras.getInt("index");
		tagll = (RelativeLayout) findViewById(R.id.tagrl);
		biaoqian = (LinearLayout) findViewById(R.id.biaoqian);
		if (width == 800) {
			switch (index) {
			case 0:
				tagll.setGravity(Gravity.LEFT);
				tagll.setPadding(33, 280, 0, 0);
				break;
			case 1:
				tagll.setGravity(Gravity.RIGHT);
				tagll.setPadding(0, 280, 23, 0);
				break;
			case 2:
				tagll.setGravity(Gravity.LEFT);
				tagll.setPadding(33, 670, 0, 0);
				break;
			case 3:
				tagll.setGravity(Gravity.RIGHT);
				tagll.setPadding(0, 670, 23, 0);
				break;
			}
		} else if (width == 1200) {
			switch (index) {
			case 0:
				tagll.setGravity(Gravity.LEFT);
				tagll.setPadding(53, 280, 0, 0);
				break;
			case 1:
				tagll.setGravity(Gravity.RIGHT);
				tagll.setPadding(0, 280, 53, 0);
				break;
			case 2:
				tagll.setGravity(Gravity.LEFT);
				tagll.setPadding(53, 600, 0, 0);
				break;
			case 3:
				tagll.setGravity(Gravity.RIGHT);
				tagll.setPadding(0, 600, 53, 0);
				break;
			}
		}
		biaoqianlv = (ListView) findViewById(R.id.biaoqianlv);
		newTv = (TextView) findViewById(R.id.newTv);
		biaoqianet = (TextView) findViewById(R.id.biaoqianet);
		biaoqianlv.setDividerHeight(0);
		LabelAdapter adapter = new LabelAdapter(getApplicationContext(), index,
				tagsList, mytags, student_id, school_class_id, id);
		biaoqianlv.setAdapter(adapter);
		biaoqian.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			}
		});
		set();
	}

	public List<tags> findbiaoqian(String et) {
		List<tags> findlist = new ArrayList<tags>();
		for (int i = 0; i < tagsList.size(); i++) {
			if (tagsList.get(i).getName().indexOf(et) != -1) {
				findlist.add(tagsList.get(i));
			}
		}
		return findlist;
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.finish();
		eb.setAllmap(null);
		eb.setTagsList(null);
		return super.onTouchEvent(event);
	}

	public void set() {
		final Handler handler1 = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					biaoqianet.setText("");
					LabelAdapter adapter = new LabelAdapter(
							getApplicationContext(), index, tagsList, mytags,
							student_id, school_class_id, id);
					biaoqianlv.setAdapter(adapter);
					break;
				case 1:

					break;
				default:
					break;
				}
			}
		};
		biaoqianet.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (findbiaoqian(biaoqianet.getText().toString()).size() <= 2) {
					newTv.setVisibility(View.VISIBLE);
					newTv.setText("新建'" + biaoqianet.getText().toString() + "'");
					newTv.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Thread thread = new Thread() {
								String json;

								public void run() {
									try {
										// viewPager = (ViewPager)
										// findViewById(R.id.guidePages);
										Map<String, String> map = new HashMap<String, String>();
										map.put("student_id", student_id);
										map.put("school_class_id",
												school_class_id);
										map.put("knowledge_card_id", id);
										map.put("name", String
												.valueOf(biaoqianet.getText()));
										json = ExerciseBookTool.sendGETRequest(
												create_card_tag, map);
										JSONObject jsonobject2 = new JSONObject(
												json);
										if (jsonobject2.getString("status")
												.equals("success")) {
											JSONObject jsonobject3 = jsonobject2
													.getJSONObject("cardtag");
											String card_bag_id = jsonobject3
													.getString("card_bag_id");
											String created_at = jsonobject3
													.getString("created_at");
											String id = jsonobject3
													.getString("id");
											String name = jsonobject3
													.getString("name");
											String update_at = jsonobject3
													.getString("updated_at");
											tagsList.add(new tags(card_bag_id,
													created_at, id, name,
													update_at));
											MyMap.get(index).getTagsarr()
													.add(Integer.valueOf(id));
										} else {

										}
										handler1.sendEmptyMessage(0);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							};
							thread.start();
						}
					});
				}
				LabelAdapter adapter = new LabelAdapter(
						getApplicationContext(), index, tagsList, mytags,
						student_id, school_class_id, id);
				biaoqianlv.setAdapter(adapter);
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}
}
