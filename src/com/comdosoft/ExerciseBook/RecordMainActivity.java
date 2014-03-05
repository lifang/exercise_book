package com.comdosoft.ExerciseBook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.calendar_item.CalendarAdapter;
import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.WorkJson;

public class RecordMainActivity extends Table_TabHost implements Urlinterface,
		OnGestureListener {
	private String id = "73";
	private String school_class_id = "85";
	private ViewPager pager;
	private List<LinearLayout> linearList = new ArrayList<LinearLayout>();
	private List<Integer> questiontype_list = new ArrayList<Integer>();
	private List<Integer> finish_list = new ArrayList<Integer>();
	private List<WorkPoJo> work_list = new ArrayList<WorkPoJo>();
	private int linear_item = 0;
	private int number;
	private Intent intent = new Intent();
	private ProgressDialog prodialog;
	private String notice;
	private ExerciseBook eb;
	private LinearLayout ll;
	private String date;

	private GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private Bundle bd = null;// 发送参数
	private Bundle bun = null;// 接收参数
	private String state = "";
	private LinearLayout date_ll;
	public boolean date_type = false;
	private TextView tishi;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				if (work_list.size() != 0) {
					number = work_list.size();
					pager.setAdapter(new ImagePagerAdapter());
				} else {
					tishi.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				prodialog.dismiss();
				tishi.setVisibility(View.VISIBLE);
				Toast.makeText(RecordMainActivity.this, notice,
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				prodialog.dismiss();
				tishi.setVisibility(View.VISIBLE);
				Toast.makeText(RecordMainActivity.this, "解析数据出现问题",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_main);
		eb = (ExerciseBook) getApplication();
		initialize();
		initDate();
		prodialog = new ProgressDialog(RecordMainActivity.this);
		prodialog.setMessage("正在获取过往作业记录");
		prodialog.setCanceledOnTouchOutside(false);
		prodialog.show();
		Thread thread = new Thread(new get_more_tasks());
		thread.start();
	}

	// 初始化
	public void initialize() {
		pager = (ViewPager) findViewById(R.id.vPager);
		ll = (LinearLayout) findViewById(R.id.ll);
		date_ll = (LinearLayout) findViewById(R.id.date_ll);
		// pager.setAdapter(new ImagePagerAdapter());
		date_ll.setVisibility(View.GONE);
		tishi = (TextView) findViewById(R.id.tishis);
	}

	// 初始化日历
	public void initDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		bd = new Bundle();// out
		bun = getIntent().getExtras();// in
		if (bun != null && bun.getString("state").equals("ruzhu")) {
			state = bun.getString("state");
			System.out.println("%%%%%%" + state);
		} else if (bun != null && bun.getString("state").equals("lidian")) {
			state = bun.getString("state");
			System.out.println("|||||||||||" + state);
		}
		gestureDetector = new GestureDetector(this);
		// bd=new Bundle();
		calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear,
				year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);

		topText = (TextView) findViewById(R.id.tv_month);
		addTextToTopTextView(topText);
	}

	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("STATE_POSITION", pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		ImagePagerAdapter() {
			inflater = getLayoutInflater();
		}

		public void finishUpdate(View container) {
		}

		public int getCount() {
			return number;
		}

		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View container) {
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);

		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public Object instantiateItem(View arg0, int arg1) {
			View nl = inflater.inflate(R.layout.record_page_item,
					(ViewGroup) arg0, false);
			TextView time = (TextView) nl.findViewById(R.id.start_date);
			time.setText(work_list.get(arg1).getStart_time() + " 发布");
			LinearLayout mylayout = (LinearLayout) nl
					.findViewById(R.id.mylayout);
			linearList.clear();
			linear_item = 0;
			// Log.i("linshi", work_list.get(arg1).toString()+ "");
			for (int i = 0; i < work_list.get(arg1).getQuestion_types().size(); i++) {

				setlayout(i, mylayout, work_list.get(arg1));
			}
			((ViewPager) arg0).addView(nl, 0);
			return nl;
		}
	}

	public void setlayout(int i, LinearLayout mylayout, WorkPoJo pojo) {
		View view = View.inflate(this, R.layout.work_item, null);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		TextView work_name = (TextView) view.findViewById(R.id.work_name);
		TextView top = (TextView) view.findViewById(R.id.top);
		ImageView over_img = (ImageView) view.findViewById(R.id.over_img);
		work_name.setText(namearr[pojo.getQuestion_types().get(i)].toString());
		top.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Log.i("linshi", "textview");
			}
		});

		if (ExerciseBookTool.getExist(pojo.getQuestion_types().get(i),
				pojo.getFinish_types())) {
			over_img.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
		} else {
			over_img.setVisibility(View.GONE);
			top.setVisibility(View.GONE);
		}

		int imgid = 0;
		imageView.setBackgroundResource(0);
		try {// 利用java反射动态设置图片
			imgid = (Integer) R.drawable.class.getField(
					"img" + (pojo.getQuestion_types().get(i) + 1)).get(null);
		} catch (Exception e) {
			imgid = 0;
		}
		imageView.setBackgroundResource(imgid);

		AbsListView.LayoutParams param = new AbsListView.LayoutParams(300, 320);
		layout.setLayoutParams(param);
		if (i == 0 || i % 3 == 0) {
			LinearLayout linear = new LinearLayout(RecordMainActivity.this);
			linear.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.topMargin = 20;
			linear.setLayoutParams(lp);
			linearList.add(linear);
			mylayout.addView(linear);
			if (i > 0) {
				linear_item++;
			}
		}
		linearList.get(linear_item).addView(view);
	}

	// 获取过往记录
	class get_more_tasks implements Runnable {
		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("today_newer_id", eb.getToday_newer_id() + "");
			map.put("student_id", id);
			map.put("school_class_id", school_class_id);
			String json;
			try {
				json = ExerciseBookTool.sendGETRequest(get_more_tasks, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					for (int i = 0; i < work_list.get(0).getQuestion_types()
							.size(); i++) {
						Log.i("linshi", work_list.get(0).getQuestion_types()
								.get(i)
								+ "=-");
					}
					handler.sendEmptyMessage(0);
				} else {
					notice = obj.getString("notice");
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(2);
			}
		}
	}

	// 根据日期获取过往记录
	class search_tasks implements Runnable {

		public void run() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", date);
			map.put("student_id", id);
			map.put("school_class_id", school_class_id);
			Log.i("linshi", "date:" + date);
			String json;
			try {
				json = ExerciseBookTool.doPost(get_more_tasks, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(2);
			}
		}
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			// 像左滑动
			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);

			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			// 向右滑动
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);

			return true;
		}
		return false;
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 选择菜单
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			// 跳转到今天
			jumpMonth = 0;
			jumpYear = 0;
			addGridView(); // 添加一个gridView
			year_c = Integer.parseInt(currentDate.split("-")[0]);
			month_c = Integer.parseInt(currentDate.split("-")[1]);
			day_c = Integer.parseInt(currentDate.split("-")[2]);
			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);

			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// 添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年")
				.append(calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	// 添加gridview
	private void addGridView() {

		gridView = (GridView) findViewById(R.id.gridview);

		gridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return RecordMainActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView中的每一个item的点击事件

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position + 7
						&& position <= endPosition - 7) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					// //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					date = scheduleYear + "-" + scheduleMonth + "-"
							+ scheduleDay;
					Toast.makeText(
							RecordMainActivity.this,
							scheduleYear + "-" + scheduleMonth + "-"
									+ scheduleDay, Toast.LENGTH_SHORT).show();
					date_ll.setVisibility(View.GONE);
					date_type = false;
					prodialog.show();
					new Thread(new search_tasks()).start();
				}
			}
		});
	}

	public void onclick(View v) {
		int item = pager.getCurrentItem();
		switch (v.getId()) {
		case R.id.left:
			if (item > 0) {
				pager.setCurrentItem(item - 1);
			}
			break;
		case R.id.right:
			if (item + 1 < number) {
				pager.setCurrentItem(item + 1);
			}
			break;
		case R.id.select_date:
			if (date_type) {
				date_ll.setVisibility(View.GONE);
				date_type = false;
			} else {
				date_ll.setVisibility(View.VISIBLE);
				date_type = true;
			}
			break;
		case R.id.exit:
			RecordMainActivity.this.finish();
			intent.setClass(RecordMainActivity.this, HomeWorkIngActivity.class);
			startActivity(intent);
			break;
		}
	}
}