package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.Adapter.LabelAdapter;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MCardBagActivity extends Table_TabHost implements Urlinterface {
	List<tags> tagsList;
	public Map<Integer, List<knowledges_card>> Allmap;
	public Map<Integer, List<View>> FontCard;
	public Map<Integer, List<View>> BackCard;
	private List<knowledges_card> cardList;
	private int allsize;
	private ViewPager viewPager;
	private ImageView imageView;
	private ImageView[] imageViews;
	private ViewGroup group;
	// private View ViewGroup;
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

	@Override
	@SuppressWarnings("rawtypes")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardbag);
		eb = (ExerciseBook) getApplication();
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		student_id = preferences.getString("id", "1");
		school_class_id = preferences.getString("school_class_id", "1");
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

	@Override
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
					parsejson(json, true);
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
				allsize = jsonarray.length();
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);
					String answer = jsonobject2.getString("answer");
					String branch_question_id = jsonobject2
							.getString("branch_question_id");
					String card_bag_id = jsonobject2.getString("card_bag_id");
					JSONArray tagsarray = jsonobject2
							.getJSONArray("card_tags_id");
					List<Integer> intlist = new ArrayList<Integer>();
					for (int j = 0; j < tagsarray.length(); j++) {
						intlist.add(tagsarray.getInt(j));
					}
					String content = jsonobject2.getString("content");
					String created_at = jsonobject2.getString("created_at");
					String full_text;
					if (jsonobject2.getString("full_text").equals(null)) {
						full_text = "";
					} else {
						full_text = jsonobject2.getString("full_text");
					}
					String id = jsonobject2.getString("id");
					String mistake_types = jsonobject2
							.getString("mistake_types");
					String options;
					if (jsonobject2.getString("options").equals(null)) {
						options = "";
					} else {
						options = jsonobject2.getString("options");
					}
					String question_id = jsonobject2.getString("question_id");
					String resource_ur = jsonobject2.getString("resource_url");

					String types;
					if (jsonobject2.getString("types").equals(null)) {
						types = "";
					} else {
						types = jsonobject2.getString("types");
					}
					String updated_at = jsonobject2.getString("updated_at");
					String your_answer = jsonobject2.getString("your_answer");

					cardList.add(new knowledges_card(answer,
							branch_question_id, card_bag_id, content,
							created_at, full_text, id, mistake_types, options,
							question_id, resource_ur, types, updated_at,
							your_answer, intlist));

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
				JSONArray tags = jsonobject.getJSONArray("tags");
				for (int i = 0; i < tags.length(); i++) {
					JSONObject jsonobject2 = tags.getJSONObject(i);
					String card_bag_id = jsonobject2.getString("card_bag_id");
					String created_at = jsonobject2.getString("created_at");
					String id = jsonobject2.getString("id");
					String name = jsonobject2.getString("name");
					String update_at = jsonobject2.getString("updated_at");
					tagsList.add(new tags(card_bag_id, created_at, id, name,
							update_at));
				}
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
				setViewPager();
				progressDialog.dismiss();
				viewPager.setAdapter(new GuidePageAdapter());
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			case 2:
				setViewPager();
				viewPager.setCurrentItem(viewPager.getCurrentItem());
				viewPager.setAdapter(new GuidePageAdapter());
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			case 3:
				setViewPager();
				viewPager.setAdapter(new GuidePageAdapter());
				viewPager
						.setOnPageChangeListener(new GuidePageChangeListener());
				break;
			default:
				break;
			}
		}
	};

	@SuppressWarnings("unused")
	public void setViewPager() {
		group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		FontCard = new HashMap<Integer, List<View>>();
		// BackCard = new HashMap<Integer, List<View>>();
		viewList = new ArrayList<View>();
		int pageCount = Allmap.size();
		ListBool = new Boolean[pageCount][4];
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
				ListBool[i][j] = true; // 标签集合
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
				fontview.setPadding(53, 23, 23, 23);
				((ViewGroup) cardview).addView(fontview);
				linear.addView(cardview);
				fontlist.add(cardview);
				if ((j + 1) % 2 != 0) {
					view.addView(linear);
				}
				setFontCard(fontview, list1.get(j), j, i);
			}
			FontCard.put(i, fontlist);
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

	public List<tags> findbiaoqian(String et) {
		List<tags> findlist = new ArrayList<tags>();
		for (int i = 0; i < tagsList.size(); i++) {
			if (tagsList.get(i).getName().indexOf(et) != -1) {
				findlist.add(tagsList.get(i));
			}
		}
		return findlist;
	}

	public void addCard(String json) {
		JSONObject jsonobject2;
		try {
			jsonobject2 = new JSONObject(json);
			if (jsonobject2.getString("status").equals("success")) {
				JSONObject jsonobject3 = jsonobject2.getJSONObject("cardtag");
				String card_bag_id = jsonobject3.getString("card_bag_id");
				String created_at = jsonobject3.getString("created_at");
				String id = jsonobject3.getString("id");
				String name = jsonobject3.getString("name");
				String update_at = jsonobject3.getString("updated_at");
				tagsList.add(new tags(card_bag_id, created_at, id, name,
						update_at));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

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

	public void setFontCard(ViewGroup v1, final knowledges_card card,
			final int index, final int page) {
		TextView reson = null;
		TextView wronganswer;
		ImageView fontIv;
		TextView rightanswer;
		TextView youranswer;
		TextView rightanswers;
		final EditText biaoqianet;
		final ListView biaoqianlv;
		final LinearLayout biaoqian;
		ViewGroup v = v1;
		final TextView newTv;
		TextView bqtv;

		if (PageBool[page][mindex]) {
			bqtv = (TextView) v.findViewById(R.id.bqtv);
			Ok: for (int i = 0; i < tagsList.size(); i++) {
				for (int j = 0; j < card.getTagsarr().size(); j++) {
					if (card.getTagsarr().get(j) == Integer.valueOf(tagsList
							.get(i).getId())) {
						bqtv.setVisibility(View.VISIBLE);
						bqtv.setText(tagsList.get(i).getName() + "...");
						break Ok;
					}
				}
			}
			reson = (TextView) v.findViewById(R.id.reson);
			wronganswer = (TextView) v.findViewById(R.id.youranswer);
			fontIv = (ImageView) v.findViewById(R.id.fontIv);
			rightanswer = (TextView) v.findViewById(R.id.rightanswer);
			youranswer = (TextView) v.findViewById(R.id.answer);
			fontIv = (ImageView) v.findViewById(R.id.fontIv);
			biaoqian = (LinearLayout) v.findViewById(R.id.biaoqian);
			biaoqianlv = (ListView) v.findViewById(R.id.biaoqianlv);
			biaoqianet = (EditText) v.findViewById(R.id.biaoqianet);
			newTv = (TextView) v.findViewById(R.id.newTv);
			final Handler handler1 = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case 0:
						biaoqianet.setText("");
						LabelAdapter adapter = new LabelAdapter(
								getApplicationContext(), page, index, tagsList,
								Allmap, student_id, school_class_id,
								card.getId());
						biaoqianlv.setAdapter(adapter);
						break;
					default:
						break;
					}
				}
			};
			biaoqianet.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (findbiaoqian(biaoqianet.getText().toString()).size() <= 2) {
						newTv.setVisibility(View.VISIBLE);
						newTv.setText("新建'" + biaoqianet.getText().toString()
								+ "'");
						newTv.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Thread thread = new Thread() {
									String json;

									public void run() {
										try {
											viewPager = (ViewPager) findViewById(R.id.guidePages);
											Map<String, String> map = new HashMap<String, String>();
											map.put("student_id", student_id);
											map.put("school_class_id",
													school_class_id);
											map.put("knowledge_card_id",
													card.getId());
											map.put("name", String
													.valueOf(biaoqianet
															.getText()));
											json = ExerciseBookTool
													.sendGETRequest(
															create_card_tag,
															map);
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
												tagsList.add(new tags(
														card_bag_id,
														created_at, id, name,
														update_at));
												card.getTagsarr().add(
														Integer.valueOf(id));
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
							getApplicationContext(), page, index,
							findbiaoqian(biaoqianet.getText().toString()),
							Allmap, student_id, school_class_id, card.getId());
					biaoqianlv.setAdapter(adapter);
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});
			biaoqianlv.setDividerHeight(0);
			reson.setText(card.getMistake_types());
			wronganswer.setText(card.getYour_answer());
			rightanswer.setText("正确答案");
			youranswer.setText(card.getAnswer() + "/.");
			fontIv.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					if (ListBool[page][index]) {
						LabelAdapter adapter = new LabelAdapter(
								getApplicationContext(), page, index, tagsList,
								Allmap, student_id, school_class_id, card
										.getId());
						biaoqianlv.setAdapter(adapter);
						biaoqian.setVisibility(View.VISIBLE);
						ListBool[page][index] = false;
					} else {
						biaoqianet.setText("");
						biaoqian.setVisibility(View.GONE);
						ListBool[page][index] = true;
					}

				}
			});
		} else {
			ImageView cardbatread;
			ImageView cardbatdel;
			reson = (TextView) v.findViewById(R.id.backreson);
			rightanswers = (TextView) v.findViewById(R.id.rightanswerb);
			cardbatread = (ImageView) v.findViewById(R.id.cardbatread);
			cardbatdel = (ImageView) v.findViewById(R.id.cardbatdel);
			if (card.getResource_url().equals(""))
				cardbatread.setVisibility(View.GONE);
			reson.setText("原题:");
			rightanswers.setText(card.getContent());
			cardbatread.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					try {
						mediaplay.setDataSource(IP + card.getResource_url());
						Log.i("1", IP + card.getResource_url());
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
					Thread thread = new Thread() {
						public void run() {
							try {
								Map<String, String> map = new HashMap<String, String>();
								map.put("knowledges_card_id", card.getId());
								String json = ExerciseBookTool.sendGETRequest(
										delete_knowledges_card, map);
								JSONObject jsonobj = new JSONObject(json);
								String notice = jsonobj.getString("notice");
								Message msg = new Message();
								if (jsonobj.getString("status").equals(
										"success")) {
									Allmap.get(page + 1).remove(index);
									if (Allmap.get(page + 1).size() == 0) {
										Allmap.remove(page + 1);
									}
									handler.sendEmptyMessage(3);
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
			});
		}
	}

	private class GuidePageAdapter extends PagerAdapter {
		public int getCount() {
			// Log.i("bbb", viewList.size()+":bbb");
			return viewList.size();
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			Log.i("bbb", viewList.size() + "!!" + arg1);
			if (arg1 < viewList.size()) {
				((ViewPager) arg0).removeView(viewList.get(arg1));
			}
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(viewList.get(arg1), arg1);
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

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			page = arg0;
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
			mediaplay.stop();
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
			view.setPadding(53, 23, 23, 23);
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
}
