package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class CardBagActivity extends Table_TabHost implements Urlinterface {
	private List<knowledges_card> cardList;
	private List<knowledges_card> pageList;
	private int page;
	private View selectview;
	private View selectview2;
	private String student_id = "1";
	private String school_class_id = "1";
	private int count;
	private LinearLayout selectLinearlayout;
	LayoutInflater flater;
	private List<View> viewList;
	private ViewPager viewPager;
	private ImageView imageView;
	private ImageView[] imageViews;
	private ViewGroup group;
	private View ViewGroup;
	Boolean[][] allbool = new Boolean[6][4];
	Boolean[][] biaoqianbool = new Boolean[6][4];
	List<tags> tagsList;
	private int allsize;
	public Map<Integer, List> Allmap;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardbag);
		getKonwledges();
	}

	public void getKonwledges() {
		Thread thread = new Thread() {
			public void run() {
				try {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 4; j++) {
							allbool[i][j] = true;
							biaoqianbool[i][j] = true;
						}
					}
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", "1");
					map.put("school_class_id", "1");
					String json = ExerciseBookTool.sendGETRequest(
							get_knowledges_card, map);
					parsejson(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	@SuppressLint("UseSparseArrays")
	private void parsejson(String json) {
		// cardList.clear();
		int count1 = 0;
		Allmap = new HashMap<Integer, List>();
		cardList = new ArrayList<knowledges_card>();
		tagsList = new ArrayList<tags>();
		try {
			JSONObject jsonobject = new JSONObject(json);
			if (jsonobject.getString("status").equals("success")) {
				JSONArray jsonarray = jsonobject
						.getJSONArray("knowledges_card");
				for (int i = 0; i < jsonarray.length(); i++) {
					allsize = jsonarray.length();
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);
					String answer = jsonobject2.getString("answer");
					String branch_question_id = jsonobject2
							.getString("branch_question_id");
					String card_bag_id = jsonobject2.getString("card_bag_id");
					JSONArray tagsarray = jsonobject2
							.getJSONArray("card_tags_id");
					List<Integer> intlist=new ArrayList<Integer>();
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
						Log.i("asd", "CardListsize--" + cardList.size());
						cardList = new ArrayList<knowledges_card>();
					}
				}
				if (jsonarray.length() - count1 * 4 <= 4) {
					count1++;
					Allmap.put(count1, cardList);
					Log.i("asd", "CardListsize--" + cardList.size());
					cardList = new ArrayList<knowledges_card>();
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
				handler.sendEmptyMessage(1);
			} else {
				Message msg = new Message();
				msg.what = 0;
				msg.obj = jsonobject.getString("notice");
				handler.sendMessage(msg);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(CardBagActivity.this, String.valueOf(msg.obj),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				setViewPager();
				break;
			case 2:
				getKonwledges();
				break;
			case 3:
				break;
			default:
				break;
			}
		}
	};

	public void setViewPager() {
		group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		viewList = new ArrayList<View>();
		int pageCount;
		if (allsize % 4 == 0) {
			pageCount = allsize / 4;
		} else {
			pageCount = allsize / 4 + 1;
		}
		LayoutInflater inflater = getLayoutInflater();
		for (int i = 1; i <= pageCount; i++) {
			View v = inflater.inflate(R.layout.cardbag_gridview, null);
			viewList.add(v);
		}
		group.removeAllViews();
		imageViews = new ImageView[viewList.size()];
		Log.i("aa", viewList.size() + "aa//" + imageViews.length);
		LinearLayout imageL;
		// imageL.removeAllViews();
		for (int i = 0; i < viewList.size(); i++) {
			imageL = new LinearLayout(CardBagActivity.this);
			imageL.setLayoutParams(new LayoutParams(40, 40));
			imageView = new ImageView(CardBagActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(10, 0, 10, 0);
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.page_inicator_focused);
			}
			imageL.addView(imageViews[i]);
			group.addView(imageL);
		}
		viewPager.setOffscreenPageLimit(6);
		viewPager.setAdapter(new GuidePageAdapter());
		page = 1;
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}
	public class LabelAdapter extends BaseAdapter 
	{
		//		private Context content;
		private String card_id;
		private List<Integer> useadp;
		public LabelAdapter(String card_id,List<Integer> usetags)
		{
			this.card_id=card_id;
			this.useadp=usetags;
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
		public void refresh(List<Integer> d) {    
			useadp = d;    
			notifyDataSetChanged();    
		}    
		class Holder {
			ImageView img;
			TextView tv;
		}
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			Holder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.biaoqian_iteam, null);
				holder = new Holder();
				holder.img = (ImageView) convertView.findViewById(R.id.bq_iteam_iv);
				holder.tv = (TextView) convertView.findViewById(R.id.bq_iteam_tv);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			Log.i("2", useadp.size()+"---适配器中useadp:"+useadp.size());
			for (int i = 0; i < useadp.size(); i++) {
				if (useadp.get(i) == Integer.valueOf(tagsList.get(position).getId())) {
					holder.img.setImageDrawable(getApplicationContext().getResources().getDrawable(
							R.drawable.biaoqianf));
				}
			}
			holder.tv.setText(tagsList.get(position).getName());
			holder.tv.setOnClickListener(new OnClickListener() {
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
								if (jsonobject.getString("status")
										.equals("success")) {
									int type = jsonobject.getInt("type");
									Message msg = new Message();
									switch (type) {
									case 0:
										msg.obj = "错误";
										Log.i("bbb", "错误");
										break;
									case 1:
										msg.obj = "删除成功";
										Log.i("bbb", "删除成功");
										useadp.remove(position);
										break;
									case 2:
										msg.obj = "添加成功";
										useadp.add(position);
										Log.i("bbb", "添加成功");
										break;
									}
									msg.what = 3;
									handler1.sendMessage(msg);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					};
					thread.start();
				}
			});
			return convertView;
		}
		Handler handler1 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 3:
					notifyDataSetChanged();
					break;
				}
			}
		};
	}

	private class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(viewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(viewList.get(arg1), arg1);
			View v;

			switch (arg1) {
			case 0:
				page = 1;
				count = 0;
				cz();
				if (setpageList(0) != null)
					pageList = setpageList(0);
				Log.i("asd", Allmap.get(1).size() + "addViewcase0");
				v = viewList.get(0);
				ViewGroup = v;
				setFontNews(v, pageList);
				break;
			case 1:
				page = 2;
				count = 0;
				cz();

				if (setpageList(1) != null)
					pageList = setpageList(1);
				Log.i("asd", Allmap.get(2).size() + "addViewcase1");
				v = viewList.get(1);
				ViewGroup = v;
				setFontNews(v, pageList);
				break;
			case 2:
				page = 3;
				count = 0;
				cz();
				v = viewList.get(2);
				if (setpageList(2) != null)
					pageList = setpageList(2);
				Log.i("asd", Allmap.get(3).size() + "addViewcase2");
				ViewGroup = v;
				setFontNews(v, pageList);
				break;
			case 3:
				page = 4;
				count = 0;
				cz();
				v = viewList.get(3);
				if (setpageList(3) != null)
					pageList = setpageList(3);
				ViewGroup = v;
				setFontNews(v, pageList);
				break;
			case 4:
				page = 5;
				count = 0;
				cz();
				v = viewList.get(4);
				if (setpageList(4) != null)
					pageList = setpageList(4);
				ViewGroup = v;
				setFontNews(v, pageList);
				break;
			case 5:
				page = 6;
				count = 0;
				cz();
				v = viewList.get(5);
				if (setpageList(5) != null)
					pageList = setpageList(5);
				ViewGroup = v;
				setFontNews(v, pageList);
				break;
			default:
				break;
			}
			return viewList.get(arg1);
		}

		@SuppressWarnings("unchecked")
		public List<knowledges_card> setpageList(int i) {
			if (i > Allmap.size()) {
			} else {
				return Allmap.get(i + 1);
			}
			return null;

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
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
			page = arg0 + 1;
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

	public void cz() {
		for (int i = 0; i < 4; i++) {
			allbool[page][i] = true;
		}
	}

	// 设置单个页面
	public void setFontText(View v, final knowledges_card card) {
		TextView reson;
		TextView wronganswer;
		ImageView fontIv;
		TextView rightanswer;
		TextView youranswer;
		TextView rightanswers;
		ImageView cardbatread;
		ImageView cardbatdel;
		final LinearLayout linearll;
		EditText biaoqianet;
		final ListView listView;
		final String id = card.getId();
		if (allbool[page][count]) {
			reson = (TextView) v.findViewById(R.id.reson);
			wronganswer = (TextView) v.findViewById(R.id.wronganswer);
			fontIv = (ImageView) v.findViewById(R.id.fontIv);
			rightanswer = (TextView) v.findViewById(R.id.rightanswer);
			youranswer = (TextView) v.findViewById(R.id.youranswer);
			linearll = (LinearLayout) v.findViewById(R.id.biaoqian);
			biaoqianet = (EditText) v.findViewById(R.id.biaoqianet);
			listView = (ListView) v.findViewById(R.id.biaoqianlv);
			listView.setDividerHeight(0);

			reson.setText(findmisR(Integer.valueOf(card.getMistake_types())));
			wronganswer.setText(card.getAnswer());
			rightanswer.setText("正确答案:");
			youranswer.setText(card.getYour_answer());
			fontIv.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (biaoqianbool[page][count]) {
						linearll.setVisibility(View.VISIBLE);
						biaoqianbool[page][count] = false;
						Adapter listad=new LabelAdapter(card.getCard_bag_id(), card.getTagsarr());
						Log.i("bbb", card.getTagsarr().size()+"个人的tags");
						listView.setAdapter((ListAdapter) listad);
					} else {
						linearll.setVisibility(View.GONE);
						biaoqianbool[page][count] = true;
					}
				
				}
			});
		} else {
			reson = (TextView) v.findViewById(R.id.reson);
			rightanswers = (TextView) v.findViewById(R.id.rightanswers);
			cardbatread = (ImageView) v.findViewById(R.id.cardbatread);
			cardbatdel = (ImageView) v.findViewById(R.id.cardbatdel);
			reson.setText("原题:");
			rightanswers.setText(card.getContent() + "");
			cardbatdel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.i("bbb", "删除点击");
					Thread thread = new Thread() {
						public void run() {
							try {
								Map<String, String> map = new HashMap<String, String>();
								map.put("knowledges_card_id", id);
								Log.i("homework", "page:" + page + "/count:"
										+ count + "/id:" + id);
								String json = ExerciseBookTool.sendGETRequest(
										delete_knowledges_card, map);
								Log.i("homework", json);
								JSONObject jsonobj = new JSONObject(json);
								String notice = jsonobj.getString("notice");
								Message msg = new Message();
								if (jsonobj.getString("status").equals(
										"success")) {

									handler.sendEmptyMessage(2);
								} else {
									msg.what = 0;
									msg.obj = jsonobj.getString("notice");
									handler.sendMessage(msg);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
				}
			});
			cardbatread.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.i("homework", "cardbatread点击");
				}
			});
		}
	}

	public int findmisR(int type) {
		switch (type) {
		case 0:
			return R.string.reson1;
		case 1:
			return R.string.reson2;
		case 2:
			return R.string.reson3;
		case 3:
			return R.string.reson4;
		case 4:
			return R.string.reson5;
		default:
			break;
		}
		return 0;
	}

	public void setFontNews(final View viewGroup, List<knowledges_card> parkList) {
		flater = getLayoutInflater();
		LinearLayout l1 = (LinearLayout) viewGroup.findViewById(R.id.inll1);
		LinearLayout l2 = (LinearLayout) viewGroup.findViewById(R.id.inll2);
		LinearLayout l3 = (LinearLayout) viewGroup.findViewById(R.id.inll3);
		LinearLayout l4 = (LinearLayout) viewGroup.findViewById(R.id.inll4);
		int size = 0;
		switch (pageList.size()) {
		case 4:
			break;
		case 3:
			l4.setVisibility(View.GONE);
			break;
		case 2:
			l4.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			break;
		case 1:
			l4.setVisibility(View.GONE);
			l3.setVisibility(View.GONE);
			l2.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		View v = viewGroup.findViewById(R.id.include1);
		if (size < parkList.size()) {
			setFontText(v, parkList.get(size));
			size++;
		}
		l1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				count = 0;
				selectLinearlayout = (LinearLayout) viewGroup
						.findViewById(R.id.inll1);
				if (allbool[page][0]) {
					selectview2 = flater.inflate(
							R.layout.cardbag_gridview_back, null);
					allbool[page][0] = false;
				} else {
					selectview2 = flater.inflate(
							R.layout.cardbag_grdiview_iteam, null);
					allbool[page][0] = true;
				}
				ViewGroup = viewGroup;
				selectview = viewGroup.findViewById(R.id.inll1);
				applyRotation(0, 90);
			}
		});

		View v1 = viewGroup.findViewById(R.id.include2);
		if (size < parkList.size()) {
			setFontText(v1, parkList.get(size));
			size++;
		}
		l2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				count = 1;
				selectLinearlayout = (LinearLayout) viewGroup
						.findViewById(R.id.inll2);
				if (allbool[page][1]) {
					selectview2 = flater.inflate(
							R.layout.cardbag_gridview_back, null);
					allbool[page][1] = false;
				} else {
					selectview2 = flater.inflate(
							R.layout.cardbag_grdiview_iteam, null);
					allbool[page][1] = true;
				}
				ViewGroup = viewGroup;
				selectview = viewGroup.findViewById(R.id.inll2);
				applyRotation(0, 90);
			}
		});

		View v2 = viewGroup.findViewById(R.id.include3);
		if (size < parkList.size()) {
			setFontText(v2, parkList.get(size));
			size++;
		}
		l3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				count = 2;
				selectLinearlayout = (LinearLayout) viewGroup
						.findViewById(R.id.inll3);
				if (allbool[page][2]) {
					selectview2 = flater.inflate(
							R.layout.cardbag_gridview_back, null);
					allbool[page][2] = false;
				} else {
					selectview2 = flater.inflate(
							R.layout.cardbag_grdiview_iteam, null);
					allbool[page][2] = true;
				}
				ViewGroup = viewGroup;
				selectview = viewGroup.findViewById(R.id.inll3);
				applyRotation(0, 90);
			}
		});

		View v4 = viewGroup.findViewById(R.id.include4);
		if (size < parkList.size()) {
			setFontText(v4, parkList.get(size));
			size++;
		}
		l4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("homework", "点击次数");
				count = 3;
				selectLinearlayout = (LinearLayout) viewGroup
						.findViewById(R.id.inll4);
				if (allbool[page][3]) {
					selectview2 = flater.inflate(
							R.layout.cardbag_gridview_back, null);
					allbool[page][3] = false;
				} else {
					selectview2 = flater.inflate(
							R.layout.cardbag_grdiview_iteam, null);
					allbool[page][3] = true;
				}
				ViewGroup = viewGroup;
				selectview = viewGroup.findViewById(R.id.inll4);
				applyRotation(0, 90);
			}
		});
	}

	private void applyRotation(float start, float end) {
		// 计算中心点
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
			selectLinearlayout.post(new SwapViews());
			selectLinearlayout.removeAllViews();
			selectLinearlayout.addView(selectview2);
			List<knowledges_card> ca = Allmap.get(page);
			Log.i("homework", "切换背景page:" + page + "/count:" + count);
			switch (count) {
			case 0:
				View v = ViewGroup.findViewById(R.id.inll1);
				setFontText(v, ca.get(0));
				break;
			case 1:
				View v1 = ViewGroup.findViewById(R.id.inll2);
				setFontText(v1, ca.get(1));
				break;
			case 2:
				View v2 = ViewGroup.findViewById(R.id.inll3);
				setFontText(v2, ca.get(2));
				break;
			case 3:
				View v3 = ViewGroup.findViewById(R.id.inll4);
				setFontText(v3, ca.get(3));
				break;
			}
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private final class SwapViews implements Runnable {
		public void run() {
			final float centerX = selectview.getWidth() / 2.0f;
			final float centerY = selectview.getHeight() / 2.0f;
			Rotate3dAnimation rotation = null;
			selectview.requestFocus();
			rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, 310.0f,
					false);
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			// 开始动画
			selectview.startAnimation(rotation);
		}
	}
}
