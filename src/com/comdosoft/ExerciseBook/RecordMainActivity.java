package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.WorkJson;

public class RecordMainActivity extends Activity implements Urlinterface {

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

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				if (work_list.size() != 0) {
					number = work_list.size();
					pager.setAdapter(new ImagePagerAdapter());
				} else {
					ll.setVisibility(View.GONE);
				}
				break;
			case 1:
				prodialog.dismiss();
				Toast.makeText(RecordMainActivity.this, notice,
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				prodialog.dismiss();
				break;
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_main);
		eb = (ExerciseBook) getApplication();
		initialize();

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
		// pager.setAdapter(new ImagePagerAdapter());
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
			map.put("student_id", 2 + "");
			map.put("school_class_id", 15 + "");
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
				Toast.makeText(RecordMainActivity.this, "解析数据出现问题",
						Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(2);
			}
		}
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
		}
	}
}