package com.comdosoft.ExerciseBook;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.HomepageAllActivity.get_class_info;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

/**
 * @作者 丁作强
 * @时间 2014-4-12 下午1:53:23
 */
public class MCardTag extends Activity implements Urlinterface, Serializable {
	LinearLayout biaoqian;
	RelativeLayout tagll;
	TextView biaoqianet;
	ListView biaoqianlv;
	TextView newTv;
	List<tags> tagsList = new ArrayList<tags>();;
	List<tags> add_tagsList; // 新增标签
	List<Integer> mytags;
	String student_id;
	String school_class_id;
	List<knowledges_card> MyMap;
	String id;
	int index;
	ExerciseBook eb;
	int width;
	List<tags> findlist;
	private ProgressDialog prodialog;

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
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		student_id = preferences.getString("id", "70");
		school_class_id = preferences.getString("school_class_id", "109");
		tagll = (RelativeLayout) findViewById(R.id.tagrl);
		biaoqian = (LinearLayout) findViewById(R.id.biaoqian);
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
		show();
		set(); // 搜索知识点 文本框 biaoqianet 设置监听
	}
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	public List<tags> findbiaoqian(String et) {
		findlist = new ArrayList<tags>();
		for (int i = 0; i < tagsList.size(); i++) {
			if (tagsList.get(i).getName().indexOf(et) != -1) {
				findlist.add(tagsList.get(i));
			}
		}
		return findlist;
	}

	public Boolean isExist(String et) {
		for (int i = 0; i < tagsList.size(); i++) {
			if (tagsList.get(i).getName().equals(et)) {
				return false;
			}
		}
		return true;
	}

	public void show() {
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
				tagll.setPadding(53, 450, 0, 0);
				break;
			case 1:
				tagll.setGravity(Gravity.RIGHT);
				tagll.setPadding(0, 450, 45, 0);
				break;
			case 2:
				tagll.setGravity(Gravity.LEFT);
				tagll.setPadding(53, 1000, 0, 0);
				break;
			case 3:
				tagll.setGravity(Gravity.RIGHT);
				tagll.setPadding(0, 1000, 45, 0);
				break;
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.finish();
		// eb.setAllmap(null);
		// eb.setTagsList(null);
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
					prodialog.dismiss();
					biaoqianet.setText("");
					LabelAdapter adapter2 = new LabelAdapter(
							getApplicationContext(), index, add_tagsList,
							mytags, student_id, school_class_id, id);
					biaoqianlv.setAdapter(adapter2);
					break;
				default:
					break;
				}
			}
		};
		biaoqianet.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (biaoqianet.getText().length() > 0) {
					newTv.setVisibility(View.VISIBLE);
				} else {
					newTv.setVisibility(View.GONE);
				}

				String reply_edit = biaoqianet.getText().toString();
				String kongge = reply_edit.replaceAll(" ", "");
				if (isExist(kongge)) {
					newTv.setText("新建'" + biaoqianet.getText().toString() + "'");
				}else {
					newTv.setVisibility(View.GONE);
				}

				newTv.setOnClickListener(new OnClickListener() { // 新建标签
					public void onClick(View v) {
						Thread thread = new Thread() {
							String json;

							public void run() {
								try {
									add_tagsList = new ArrayList<tags>();
									// viewPager = (ViewPager)
									// findViewById(R.id.guidePages);
									Map<String, String> map = new HashMap<String, String>();
									map.put("student_id", student_id);
									map.put("school_class_id", school_class_id);
									map.put("knowledge_card_id", id);
									String s = String.valueOf(biaoqianet
											.getText());
									String s1 = URLEncoder.encode(s, "utf-8");
									map.put("name", s1);
									json = ExerciseBookTool.sendGETRequest(
											create_card_tag, map);
									JSONObject jsonobject2 = new JSONObject(
											json);
									if (jsonobject2.getString("status").equals(
											"success")) {
										JSONObject jsonobject3 = jsonobject2
												.getJSONObject("cardtag");
										String card_bag_id = jsonobject3
												.getString("card_bag_id");
										String created_at = jsonobject3
												.getString("created_at");
										String id = jsonobject3.getString("id");
										String name = jsonobject3
												.getString("name");
										String update_at = jsonobject3
												.getString("updated_at");
										tagsList.add(new tags(card_bag_id,
												created_at, id, name, update_at));
										add_tagsList
												.add(new tags(card_bag_id,
														created_at, id, name,
														update_at));
										eb.setTagsList(tagsList);
										MyMap.get(index).getTagsarr()
												.add(Integer.valueOf(id));
										mytags.add(Integer.valueOf(id));
										eb.setTagsarr(mytags);
										Intent intent3 = new Intent();
										eb.setTagsarr(mytags);
										// 通过调用setResult方法返回结果给前一个activity。
										MCardTag.this.setResult(-12, intent3);
									} else {

									}
									handler1.sendEmptyMessage(1);
								} catch (Exception e) {
									prodialog.dismiss();
								}
							}
						};
						String reply_edit = biaoqianet.getText().toString();
						String kongge = reply_edit.replaceAll(" ", "");
						if (reply_edit.length() == 0 || kongge.equals("")) {
							Toast.makeText(getApplicationContext(),
									R.string.edit_null, Toast.LENGTH_SHORT)
									.show();
						} else {
							if (ExerciseBookTool.isConnect(MCardTag.this)) {
								prodialog = new ProgressDialog(MCardTag.this);
								prodialog.setMessage("新建标签...");
								prodialog.setCanceledOnTouchOutside(false);
								prodialog.show();
								thread.start();
							} else {
								Toast.makeText(getApplicationContext(),
										ExerciseBookParams.INTERNET, 0).show();
							}
						}
					}
				});
				// if (findbiaoqian(biaoqianet.getText().toString()).size() <=
				// 2) {
				// }
				findbiaoqian(biaoqianet.getText().toString());
				LabelAdapter adapter = new LabelAdapter(
						getApplicationContext(), index, findlist, mytags,
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

	public class LabelAdapter extends BaseAdapter implements Urlinterface {
		public List<tags> tagsList; // 标签集合
		public List<Integer> cardlist; // 选中的标签 id
		Context content;
		knowledges_card myList;
		List<Integer> mytags;
		private String student_id;
		private String school_class_id;
		private String card_id; // / 卡片 id

		public LabelAdapter(Context content, int index, List<tags> tagsList,
				List<Integer> card, String student_id, String school_class_id,
				String card_id) {
			this.content = content;
			this.tagsList = tagsList;
			this.cardlist = card;
			this.student_id = student_id;
			this.school_class_id = school_class_id;
			this.card_id = card_id;
			mytags = new ArrayList<Integer>();
			for (int i = 0; i < cardlist.size(); i++) {
				mytags.add(cardlist.get(i));
			}
			// Log.i("2",mytags.size()+"1!!"+tagsList.size());
		}

		public int getCount() {
			return tagsList.size();
		}

		public Object getItem(int position) {
			return tagsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		class Holder {
			ImageView img;
			TextView tv;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(content);
			Holder holder = null;
			convertView = inflater.inflate(R.layout.biaoqian_iteam, null);
			holder = new Holder();
			holder.img = (ImageView) convertView.findViewById(R.id.bq_iteam_iv);
			holder.tv = (TextView) convertView.findViewById(R.id.bq_iteam_tv);
			convertView.setPadding(0, 10, 0, 10);
			for (int i = 0; i < mytags.size(); i++) {
				if (mytags.get(i) == Integer.valueOf(tagsList.get(position)
						.getId())) {
					holder.img.setImageDrawable(content.getResources()
							.getDrawable(R.drawable.biaoqianf));
				}
			}
			holder.tv.setText(tagsList.get(position).getName());
			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Thread thread = new Thread() {
						public void run() {
							try {
								Map<String, String> map = new HashMap<String, String>();
								map.put("student_id", student_id);
								map.put("school_class_id", school_class_id);
								map.put("knowledge_card_id", card_id);
								map.put("card_tag_id", tagsList.get(position)
										.getId());
								String json = ExerciseBookTool.sendGETRequest(
										knoledge_tag_relation, map);
								JSONObject jsonobject = new JSONObject(json);
								if (jsonobject.getString("status").equals(
										"success")) {
									int type = jsonobject.getInt("type");
									Message msg = new Message();
									switch (type) {
									case 0:
										prodialog.dismiss();
										msg.obj = "错误";
										break;
									case 1:
										prodialog.dismiss();
										msg.obj = "删除标签成功";
										mytags.remove(setList(tagsList.get(
												position).getId()));
										Intent intent2 = new Intent();
										eb.setTagsarr(mytags);
										// 通过调用setResult方法返回结果给前一个activity。
										MCardTag.this.setResult(-12, intent2);
										break;
									case 2:
										prodialog.dismiss();
										msg.obj = "添加标签成功";
										mytags.add(Integer.valueOf(tagsList
												.get(position).getId()));
										Intent intent3 = new Intent();
										eb.setTagsarr(mytags);
										// 通过调用setResult方法返回结果给前一个activity。
										MCardTag.this.setResult(-12, intent3);
										break;
									}
									msg.what = 0;
									handler1.sendMessage(msg);
								}
							} catch (Exception e) {
								prodialog.dismiss();
							}

						}
					};
					if (ExerciseBookTool.isConnect(MCardTag.this)) {
						prodialog = new ProgressDialog(MCardTag.this);
						prodialog.setMessage(ExerciseBookParams.PD_REG);
						prodialog.setCanceledOnTouchOutside(false);
						prodialog.show();
						thread.start();

					} else {
						Toast.makeText(getApplicationContext(),
								ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
			return convertView;

		}

		public int setList(String id) {
			for (int i = 0; i < mytags.size(); i++) {
				if (mytags.get(i) == Integer.valueOf(id)) {
					return i;
				}
			}
			return 0;
		}

		Handler handler1 = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					notifyDataSetChanged();
					Toast.makeText(content, String.valueOf(msg.obj),
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
	}

}
