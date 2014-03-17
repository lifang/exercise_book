package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.comdosoft.ExerciseBook.Adapter.LabelAdapter;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MCardBagActivity extends Table_TabHost implements Urlinterface
{
	List<tags> tagsList;
	public Map<Integer, List> Allmap;
	public Map<Integer,List<View>> clickmap;
	private List<knowledges_card> cardList;
	private int allsize;
	private ViewPager viewPager;
	private ImageView imageView;
	private ImageView[] imageViews;
	private ViewGroup group;
	private View ViewGroup;
	private List<View> viewList;
	private int page=0;
	private int mindex;
	Boolean[][] ListBool;
	Boolean[][] PageBool;
	LayoutInflater inflater ;
	private String student_id;
	private String school_class_id;
	private EditText cardbagEt;
	private ImageView cardbatFind;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	List<Button>  btnList;
	GuidePageAdapter pageAdapter;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardbag);
		SharedPreferences preferences	= getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		btnList=new ArrayList<Button>();
		cardbagEt=(EditText) findViewById(R.id.cardbagEt);
		cardbatFind=(ImageView) findViewById(R.id.cardbatFind);
		button1=(Button) findViewById(R.id.button1);
		btnList.add(button1);
		button2=(Button) findViewById(R.id.button2);
		btnList.add(button2);
		button3=(Button) findViewById(R.id.button3);
		btnList.add(button3);
		button4=(Button) findViewById(R.id.button4);
		btnList.add(button4);
		Allmap = new HashMap<Integer, List>();
		cardList = new ArrayList<knowledges_card>();
		tagsList = new ArrayList<tags>();
		getKonwledges();
		student_id = preferences.getString("id", "1");
		school_class_id = preferences.getString("school_class_id", "1");
		for(int i=0;i<btnList.size();i++)
		{
			final String mistype=String.valueOf(i);
			btnList.get(i).setOnClickListener(new OnClickListener(){
				public void onClick(View v) {
					Thread thread = new Thread() {
						String json;
						public void run() {
							try {
								viewPager = (ViewPager) findViewById(R.id.guidePages);
								Map<String, String> map = new HashMap<String, String>();
								map.put("student_id", "1");
								map.put("school_class_id", "1");
								map.put("mistake_types", mistype);
								json = ExerciseBookTool.sendGETRequest(
										get_knowledges_card, map);
								parsejson(json,false);
								handler.sendEmptyMessage(1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
					
					
//					viewPager.getAdapter().notifyDataSetChanged();
				}
			});
		}

		cardbatFind.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				Thread thread = new Thread() {
					public void run() {
						try {
							viewPager = (ViewPager) findViewById(R.id.guidePages);
							Map<String, String> map = new HashMap<String, String>();
							map.put("student_id", "1");
							map.put("school_class_id", "1");
							map.put("name",String.valueOf(cardbagEt.getText()));
							String json = ExerciseBookTool.sendGETRequest(
									get_knowledges_card, map);
							pageAdapter=new GuidePageAdapter();
							parsejson(json,false);
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
	public void getKonwledges() {
		Thread thread = new Thread() {
			public void run() {
				try {
					viewPager = (ViewPager) findViewById(R.id.guidePages);
					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", "1");
					map.put("school_class_id", "1");
					String json = ExerciseBookTool.sendGETRequest(
							get_knowledges_card, map);
					pageAdapter=new GuidePageAdapter();
					parsejson(json,true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	private void parsejson(String json,Boolean flag) {
		// cardList.clear();
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
						Log.i("asd", "CardListsize--" + cardList.size());
						cardList = new ArrayList<knowledges_card>();
					}
				}
				count1++;
				Allmap.put(count1, cardList);
				Log.i("asd", "CardListsize--" + cardList.size());
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
				if(flag)
				{
					handler.sendEmptyMessage(1);
				}
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
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(MCardBagActivity.this, String.valueOf(msg.obj),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				setViewPager();
				viewPager.setAdapter(new GuidePageAdapter());
				viewPager.setOnPageChangeListener(new GuidePageChangeListener());
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
	public void setViewPager()
	{
		Log.i("2", " setAllmap:"+Allmap.size());
		group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		clickmap=new HashMap<Integer, List<View>>();
		viewList = new ArrayList<View>();
		int pageCount=Allmap.size();
		ListBool=new Boolean[pageCount][4];
		PageBool=new Boolean[pageCount][4];
		inflater= getLayoutInflater();
		imageViews = new ImageView[Allmap.size()];
		LinearLayout imageL;
		//		page = 1;
		viewList.clear();
		group.removeAllViews();
		for(int i=0;i<imageViews.length;i++)
		{
			imageViews[i]=null;
		}
		for(int i=0;i<Allmap.size();i++)
		{
			
			List<View> listv=new ArrayList<View>();
			//增加下面的小图标
			imageL = new LinearLayout(MCardBagActivity.this);
			imageL.setLayoutParams(new LayoutParams(40, 40));
			imageView = new ImageView(MCardBagActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(10, 0, 10, 0);
			imageViews[i] = imageView;
			Log.i("1", "i:"+i);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.page_indicator);
			} else {
				imageView
				.setBackgroundResource(R.drawable.page_inicator_focused);
			}
			imageL.addView(imageView);
			group.addView(imageL);
			//图标结束
			ViewGroup view = (android.view.ViewGroup) inflater.inflate(R.layout.cardbag_gridview, null);			//增加viewpager试图
			LinearLayout linear = null;
			List<knowledges_card> list1=Allmap.get(i+1);
			for(int j=0;j<Allmap.get(i+1).size();j++)
			{
				Log.i("1", "j:"+j);
				ListBool[i][j]=true;	//卡片集合
				PageBool[i][j]=true;    //所有卡片的T/F
				if((j+1)%2!=0)
				{
					linear=new LinearLayout(MCardBagActivity.this);
					linear.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					linear.setOrientation(LinearLayout.HORIZONTAL);
				}
				ViewGroup v=(android.view.ViewGroup) inflater.inflate(R.layout.cardbag_grdiview_iteam, null);
				v.setPadding(53, 23, 23, 23);
				linear.addView(v);
				listv.add(v);
				if((j+1)%2!=0)
				{
					view.addView(linear);
				}
				setFontCard((android.view.ViewGroup) v,list1.get(j), j,i);
			}
			clickmap.put(i, listv);
			viewList.add(view);
		}	
		Log.i("asd", "ViewList.size:"+viewList.size());
		
	}
	public void NoClick(int page,int index)
	{
		List<View> list=clickmap.get(page);
		for(int i=0;i<list.size();i++)
		{
			if(i!=index)
			{
				list.get(i).setClickable(false);
			}
		}
	}
	public void setFontCard(ViewGroup v,knowledges_card card,final int index,final int page)
	{
		TextView reson;
		TextView wronganswer;
		ImageView fontIv;
		TextView rightanswer;
		TextView youranswer;
		TextView rightanswers;
		ListView biaoqianlv;
		final LinearLayout biaoqian;
		v.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				mindex=index;
				NoClick(page,index);
				applyRotation(0, 90, v);
			}
		});
		reson = (TextView) v.findViewById(R.id.reson);
		wronganswer = (TextView) v.findViewById(R.id.youranswer);
		fontIv = (ImageView) v.findViewById(R.id.fontIv);
		rightanswer = (TextView) v.findViewById(R.id.rightanswer);
		youranswer = (TextView) v.findViewById(R.id.answer);
		fontIv=(ImageView) v.findViewById(R.id.fontIv);
		biaoqian=(LinearLayout) v.findViewById(R.id.biaoqian);
		biaoqianlv=(ListView) v.findViewById(R.id.biaoqianlv);
		biaoqianlv.setDividerHeight(0);
		LabelAdapter adapter=new LabelAdapter(getApplicationContext(),page,index,tagsList,Allmap,student_id,school_class_id,card.getId());
		biaoqianlv.setAdapter(adapter);
		reson.setText(card.getMistake_types());
		wronganswer.setText(card.getYour_answer());
		rightanswer.setText("正确答案");
		youranswer.setText(card.getAnswer()+"/."+index);
		fontIv.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0) {
				Log.i("3", "page:"+page+"/index:"+index+"!!!"+ListBool[page][index]);
				if(ListBool[page][index])
				{
					biaoqian.setVisibility(View.VISIBLE);
					ListBool[page][index]=false;
				}
				else
				{
					biaoqian.setVisibility(View.GONE);
					ListBool[page][index]=true;
				}

			}
		});
	}
	public void setBackCard(View v,final int index,final int page)
	{

		List<knowledges_card> cardl=Allmap.get(page+1);
		final knowledges_card	card=cardl.get(index);
		TextView reson;
		TextView rightanswers;
		ImageView cardbatread;
		ImageView cardbatdel;
		reson=(TextView) findViewById(R.id.reson);
		rightanswers=(TextView) findViewById(R.id.rightanswerb);
		cardbatread=(ImageView) findViewById(R.id.cardbatread);
		cardbatdel=(ImageView) findViewById(R.id.cardbatdel);
		if(card.getResource_url().equals(""))
			cardbatread.setVisibility(View.GONE);

		reson.setText("原题:");
		Log.i("asd", card.getContent()+"---Content");
		rightanswers.setText(card.getContent());
		cardbatdel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				getKonwledges();
				Thread thread = new Thread() {
					public void run() {
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("knowledges_card_id", card.getId());
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
			return POSITION_NONE;  
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			Log.i("asd", viewList.size()+"!!!"+arg1);
//			((ViewPager) arg0).removeView(viewList.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(viewList.get(arg1), arg1);
			return viewList.get(arg1);
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
	private void applyRotation(float start, float end,View selectview) {
		//		// 计算中心点
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

		//		// 动画结束
		public void onAnimationEnd(Animation animation) {

			Log.i("3","page:"+page+"mindex:"+mindex+"clickmap:"+clickmap.size());
			List<View> list=clickmap.get(page);
			List<knowledges_card> cardl=Allmap.get(page+1);
			ViewGroup v1=(android.view.ViewGroup) list.get(mindex);
			LayoutInflater inflater2 = getLayoutInflater();;
			View v = null;

			v1.removeAllViews();
			if(PageBool[page][mindex])
			{
				v= inflater2.inflate(R.layout.cardbag_gridview_back, null);
				Log.i("asd","反面操作"+v.findViewById(R.id.reson).getContext());
				v1.addView(v);
				setBackCard(v1, mindex, page);
				PageBool[page][mindex]=false;
			}
			else
			{
				v=inflater2.inflate(R.layout.cardbag_grdiview_iteam, null);
				v1.addView(v);
				setFontCard(v1,cardl.get(mindex), mindex, page);
				PageBool[page][mindex]=true;
			}

			for(int i=0;i<list.size();i++)
			{
				list.get(i).setClickable(true);
			}
			//			selectLinearlayout.post(new SwapViews());
			final float centerX = v1.getWidth() / 2.0f;
			final float centerY = v1.getHeight() / 2.0f;
			Rotate3dAnimation rotation = null;
			v1.requestFocus();
			rotation = new Rotate3dAnimation(-90, 0, centerX, centerY, 310.0f,
					false);
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			// 开始动画
			v1.startAnimation(rotation);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	//	private final class SwapViews implements Runnable {
	//		public void run() {
	//
	//		}
	//	}
}
