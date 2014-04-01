package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
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
	// private String json =
	// "{\"status\": \"success\",\"notice\": \"\u83b7\u53d6\u6210\u529f\uff01\",\"tasks\": [{\"id\": 130,\"name\": \"\",\"start_time\": \"2014-03-12T14: 44: 45+08: 00\",\"question_types\": [0,1,2,3,4,5,6],\"finish_types\": [2,5],\"end_time\": \"2014-03-13T18: 00: 00+08: 00\",\"question_packages_url\": \"/que_ps/question_p_264/resourse.zip\"},{\"id\": 131,\"name\": \"\",\"start_time\": \"2014-03-12T14: 44: 45+08: 00\",\"question_types\": [0,1,2,3,4,5,6],\"finish_types\": [2,5],\"end_time\": \"2014-03-13T18: 00: 00+08: 00\",\"question_packages_url\": \"/que_ps/question_p_264/resourse.zip\"}],\"knowledges_cards_count\": 10}";
	private ViewPager pager;
	private List<LinearLayout> linearList = new ArrayList<LinearLayout>();
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
	private List<String> json_list;

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
	public ImagePagerAdapter ipa;
	private TextView tishi;
	private ProgressBar mProgress;
	private boolean cancelUpdate;
	private List<Boolean> typeList;
	private AlertDialog mDownloadDialog;
	private int status;
	private boolean cardType;
	private int progress;
	private List<String> pathList;
	private List<String> downloadList;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				if (work_list.size() != 0) {
					tishi.setVisibility(View.GONE);
					number = work_list.size();
					pager.setAdapter(ipa);
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
			case 3:
				Toast.makeText(RecordMainActivity.this, "检测到本地json格式有问题！",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Builder builder = new Builder(RecordMainActivity.this);
				builder.setTitle("提示");
				builder.setMessage("需要下载数据包才可以完成任务,确认下载吗?");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								showDownloadDialog();
							}
						});
				builder.setNegativeButton("下次再说", null).show();
				break;
			case 5:
				mProgress.setProgress(progress);
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
		ipa = new ImagePagerAdapter();
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
		} else if (bun != null && bun.getString("state").equals("lidian")) {
			state = bun.getString("state");
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
			if (work_list.size() > 0) {
				TextView time = (TextView) nl.findViewById(R.id.start_date);
				time.setText(work_list.get(arg1).getStart_time() + " 发布");
				LinearLayout mylayout = (LinearLayout) nl
						.findViewById(R.id.mylayout);
				linearList.clear();
				linear_item = 0;
				// Log.i("linshi", work_list.get(arg1).toString()+ "");
				typeList = new ArrayList<Boolean>();
				final List<Integer> questiontype_list = work_list.get(arg1)
						.getQuestion_types();
				finish_list = work_list.get(arg1).getFinish_types();
				cardType = work_list.get(arg1).getNumber() < 20 ? true : false;
				eb.setWork_id(work_list.get(arg1).getId() + "");
				ExerciseBookTool
						.initAnswer(pathList.get(arg1), eb.getWork_id());// 初始化answer
				for (int i = 0; i < work_list.get(arg1).getQuestion_types()
						.size(); i++) {
					setlayout(i, mylayout, work_list.get(arg1),
							questiontype_list, work_list.get(arg1)
									.getQuestion_types().size());
				}
			}
			((ViewPager) arg0).addView(nl, 0);
			return nl;
		}
	}

	public void setlayout(final int i, LinearLayout mylayout,
			final WorkPoJo pojo, final List<Integer> questiontype_list,
			final int number) {
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
		layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				eb.setWork_number(number);
				eb.setActivity_item(1);
				Log.i("aaa", "worknumber:" + number);
				Log.i("suanfa", "path:" + pathList.get(pager.getCurrentItem()));
				Log.i("suanfa", "path2:" + pojo.getQuestion_packages_url());
				if (ExerciseBookTool.FileExist(pathList.get(pager
						.getCurrentItem()))) {// 判断文件是否存在
					getJsonPath();
					if (typeList.get(i)) {// 已完成
						MyDialog(i, questiontype_list);
					} else {
						if (cardType) {
							status = 0;
							Start_Acvivity(i, questiontype_list);
						} else {
							Builder builder = new Builder(
									RecordMainActivity.this);
							builder.setTitle("提示");
							builder.setMessage("您的卡包已满,先清除几张再回来答题吧");
							builder.setNegativeButton("确定", null);
							builder.show();
						}
					}
				} else {
					handler.sendEmptyMessage(4);
				}
				Log.i("linshi", IP + pojo.getQuestion_packages_url());
			}

		});

		if (ExerciseBookTool.getExist(pojo.getQuestion_types().get(i),
				pojo.getFinish_types())) {
			typeList.add(true);
			over_img.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
		} else {
			typeList.add(false);
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

		AbsListView.LayoutParams param = new AbsListView.LayoutParams(210, 220);
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

	public void Start_Acvivity(int i, List<Integer> questiontype_list) {// 做题跳转
		switch (questiontype_list.get(i)) {
		case 0:
			intent.setClass(this, AnswerDictationBeginActivity.class);
			break;
		case 1:
			intent.setClass(this, SpeakPrepareActivity.class);
			break;
		case 2:
			intent.setClass(this, TenSpeedActivity.class);
			break;
		case 3:
			intent.setClass(this, AnswerSelectActivity.class);
			break;
		case 4:
			intent.setClass(this, AnswerWireActivity.class);
			break;
		case 5:
			intent.setClass(this, ClozeActivity.class);
			break;
		case 6:
			intent.setClass(this, AnswerOrderActivity.class);
			break;
		}
		Log.i("suanfa", json_list.get(questiontype_list.get(i)));
		intent.putExtra("json", json_list.get(questiontype_list.get(i)));
		intent.putExtra("path", pathList.get(pager.getCurrentItem())
				+ "/answer.json");
		intent.putExtra("type", 0);// 0 今日任务列表跳转 1历史记录列表跳转
		intent.putExtra("status", status);// 0表示第一次做 1表示重做 2历史
		eb.setWork_end_dath(work_list.get(0).getEnd_time());

		Log.i("suanfa", questiontype_list.get(i) + "");
		startActivity(intent);
		this.finish();
	}

	public void Start_History_Acvivity(int i, List<Integer> questiontype_list) {// 历史记录跳转
		switch (questiontype_list.get(i)) {
		case 0:
			intent.setClass(this, SpeakPrepareActivity.class);
			break;
		case 1:
			intent.setClass(this, SpeakPrepareActivity.class);
			break;
		case 2:
			intent.setClass(this, Ten_HistoryActivity.class);
			break;
		case 3:
			intent.setClass(this, AnswerSelectActivity.class);
			break;
		case 4:
			intent.setClass(this, AnswerWireActivity.class);
			break;
		case 5:
			intent.setClass(this, Cloze_HistoryActivity.class);
			break;
		case 6:
			intent.setClass(this, AnswerOrderActivity.class);
			break;
		}
		intent.putExtra("json", json_list.get(questiontype_list.get(i)));
		intent.putExtra("path", pathList.get(pager.getCurrentItem())
				+ "/answer.json");
		intent.putExtra("type", 0);// 0 今日任务列表跳转 1历史记录列表跳转
		intent.putExtra("status", status);// 0表示第一次做 1表示重做 2历史
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		startActivity(intent);
		this.finish();
	}

	// 自定义dialog设置
	private void MyDialog(final int type, final List<Integer> questiontype_list) {
		// type :0表示退出 1表示结束
		final Dialog dialog = new Dialog(this, R.style.Transparent);
		dialog.setContentView(R.layout.my_dialog_main);
		dialog.setCancelable(true);

		ImageView close = (ImageView) dialog.findViewById(R.id.close);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button history = (Button) dialog.findViewById(R.id.history);
		Button working = (Button) dialog.findViewById(R.id.working);
		history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				status = 2;
				Start_History_Acvivity(type, questiontype_list);
			}
		});
		working.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				status = 1;// 0表示第一次做 1表示重做 2历史
				Start_Acvivity(type, questiontype_list);
			}
		});
		dialog.show();
	}

	public void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(RecordMainActivity.this);
		builder.setTitle("正在下载");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater
				.from(RecordMainActivity.this);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk();
	}

	public class downloadApkThread extends Thread {

		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Log.i("suanfa",
							IP + downloadList.get(pager.getCurrentItem()));
					URL url = new URL(downloadList.get(pager.getCurrentItem()));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(pathList.get(pager.getCurrentItem()));
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(
							pathList.get(pager.getCurrentItem()),
							"resourse.zip");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						handler.sendEmptyMessage(5);
						if (numread <= 0) {
							// 下载完成
							// handler.sendEmptyMessage(5);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
					ExerciseBookTool.unZip(pathList.get(pager.getCurrentItem())
							+ "/resourse.zip",
							pathList.get(pager.getCurrentItem()));
					getJsonPath();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(RecordMainActivity.this, "解压文件发生异常",
						Toast.LENGTH_SHORT).show();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 下载文件
	 */
	public void downloadApk() {
		// 启动新线程下载文件
		new downloadApkThread().start();
	}

	// 获取过往记录
	class get_more_tasks implements Runnable {
		public void run() {
			pathList = new ArrayList<String>();
			downloadList = new ArrayList<String>();
			Map<String, String> map = new HashMap<String, String>();
			if (eb.getToday_newer_id() != 0) {
				map.put("today_newer_id", eb.getToday_newer_id() + "");
			}
			map.put("student_id", eb.getUid());
			map.put("school_class_id", eb.getClass_id());
			String json;
			try {
				json = ExerciseBookTool.sendGETRequest(get_more_tasks, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					for (int i = 0; i < work_list.size(); i++) {
						String path = Environment.getExternalStorageDirectory()
								+ "/" + "Exercisebook_app/" + eb.getUid() + "/"
								+ eb.getClass_id() + "/"
								+ work_list.get(i).getId();
						eb.setPath(path);
						String downPath = IP
								+ work_list.get(i).getQuestion_packages_url();
						pathList.add(path);
						downloadList.add(downPath);
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
			downloadList = new ArrayList<String>();
			pathList = new ArrayList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", date);
			map.put("student_id", eb.getUid());
			map.put("school_class_id", eb.getClass_id());
			Log.i("linshi", "date:" + date);
			String json;
			try {
				json = ExerciseBookTool.doPost(search_tasks, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					for (int i = 0; i < work_list.size(); i++) {
						String path = Environment.getExternalStorageDirectory()
								+ "/" + "Exercisebook_app/" + eb.getUid() + "/"
								+ eb.getClass_id() + "/"
								+ work_list.get(i).getId();
						String downPath = IP
								+ work_list.get(i).getQuestion_packages_url();
						pathList.add(path);
						downloadList.add(downPath);
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
		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
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
			public boolean onTouch(View v, MotionEvent event) {
				return RecordMainActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView中的每一个item的点击事件

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				work_list.clear();
				ipa.notifyDataSetChanged();
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

	private void getJsonPath() {
		File file = new File(pathList.get(pager.getCurrentItem())
				+ "/questions.json");
		if (file.exists()) {
			Log.i("linshi", "获取json" + pathList.get(pager.getCurrentItem()));
			String json = ExerciseBookTool.getJson(pathList.get(pager
					.getCurrentItem()) + "/questions.json");
			SetJson(json);
		}
	}

	private void SetJson(String json) {
		json_list = new ArrayList<String>();
		try {
			JSONObject obj = new JSONObject(json);
			json_list.add(obj.getString("listening"));
			json_list.add(obj.getString("reading"));
			json_list.add(obj.getString("time_limit"));
			json_list.add(obj.getString("selecting"));
			json_list.add(obj.getString("lining"));
			json_list.add(obj.getString("cloze"));
			json_list.add(obj.getString("sort"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RecordMainActivity.this.finish();
			Intent intent = new Intent();
			intent.setClass(RecordMainActivity.this, HomeWorkIngActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}