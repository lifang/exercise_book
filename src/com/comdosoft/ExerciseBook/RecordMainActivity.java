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

import android.annotation.SuppressLint;
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
import android.view.Display;
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
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.calendar_item.CalendarAdapter;
import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.WorkJson;

public class RecordMainActivity extends Table_TabHost implements Urlinterface,
		OnGestureListener, android.view.View.OnClickListener {
	// private String json =
	// "{\"status\": \"success\",\"notice\": \"\u83b7\u53d6\u6210\u529f\uff01\",\"tasks\": [{\"id\": 130,\"name\": \"\",\"start_time\": \"2014-03-12T14: 44: 45+08: 00\",\"question_types\": [0,1,2,3,4,5,6],\"finish_types\": [2,5],\"end_time\": \"2014-03-13T18: 00: 00+08: 00\",\"question_packages_url\": \"/que_ps/question_p_264/resourse.zip\"},{\"id\": 131,\"name\": \"\",\"start_time\": \"2014-03-12T14: 44: 45+08: 00\",\"question_types\": [0,1,2,3,4,5,6],\"finish_types\": [2,5],\"end_time\": \"2014-03-13T18: 00: 00+08: 00\",\"question_packages_url\": \"/que_ps/question_p_264/resourse.zip\"}],\"knowledges_cards_count\": 10}";
	private ViewPager pager;
	private List<LinearLayout> linearList = new ArrayList<LinearLayout>();
	private List<WorkPoJo> work_list = new ArrayList<WorkPoJo>();
	private int linear_item = 0;
	private int number;
	private Intent intent = new Intent();
	private ProgressDialog prodialog;
	private String notice;
	private ExerciseBook eb;
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
	private LinearLayout date_ll;
	public boolean date_type = false;
	public ImagePagerAdapter ipa;
	private TextView tishi;
	private ProgressBar mProgress;
	private boolean cancelUpdate;
	private AlertDialog mDownloadDialog;
	private int status;
	private boolean cardType;
	private int progress;
	private List<String> pathList;
	private List<String> downloadList;
	private boolean out_time;// 是否超时
	private String downPath;
	private String download_name;
	private int layout_index;
	private List<Boolean> typeList;
	private List<Integer> question_type;
	private boolean unzip_type = true;
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
				if (work_list.size() < 2) {
					left.setVisibility(View.GONE);
					right.setVisibility(View.GONE);
				} else {
					left.setVisibility(View.VISIBLE);
					right.setVisibility(View.VISIBLE);
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
				Toast.makeText(RecordMainActivity.this, "网络发生异常",
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
								downPath = downloadList.get(pager
										.getCurrentItem());
								download_name = "resourse.zip";
								showDownloadDialog();
							}
						});
				builder.setNegativeButton("下次再说", null).show();
				break;
			case 5:
				mProgress.setProgress(progress);
				break;
			case 6:
				prodialog.setMessage("正在更新答题记录,请稍后...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				downPath = IP
						+ work_list.get(pager.getCurrentItem()).getAnswer_url();
				download_name = "student_" + eb.getUid() + ".json";
				downloadApk();
				break;
			case 7:
				prodialog.dismiss();
				ExerciseBookTool.UpdateJsonTime(
						work_list.get(pager.getCurrentItem()).getUpdated_at(),
						pathList.get(pager.getCurrentItem()) + "/student_"
								+ eb.getUid() + ".json");
				startDekaron(layout_index);
				break;
			case 8:
				initDate();
				break;
			}
		};
	};
	private int width;
	private ImageView left;
	private ImageView right;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_main);
		eb = (ExerciseBook) getApplication();
		eb.getActivityList().add(this);
		initialize();
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
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
		date_ll = (LinearLayout) findViewById(R.id.date_ll);
		findViewById(R.id.btn_prev_month).setOnClickListener(this);
		findViewById(R.id.btn_next_month).setOnClickListener(this);
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		date_ll.setVisibility(View.GONE);
		tishi = (TextView) findViewById(R.id.tishis);
	}

	// 初始化日历
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public void initDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		gestureDetector = new GestureDetector(this);
		calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear,
				year_c, month_c, day_c, work_list);
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
				TextView chapter = (TextView) nl.findViewById(R.id.chapter);
				chapter.setText(work_list.get(arg1).getName());
				LinearLayout mylayout = (LinearLayout) nl
						.findViewById(R.id.mylayout);
				linearList.clear();
				linear_item = 0;
				final List<Integer> questiontype_list = work_list.get(arg1)
						.getQuestion_types();
				cardType = work_list.get(arg1).getNumber() < 20 ? true : false;
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
				Intent intent = new Intent(RecordMainActivity.this,
						RankingOfPointsActivity.class);
				intent.putExtra("types", questiontype_list.get(i));
				intent.putExtra("pub_id", Integer.valueOf(pojo.getId()));
				startActivity(intent);
			}
		});

		layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (eb.getActive_status().equals("1")
						|| eb.getActive_status().equals("null")) {
					eb.setWork_number(number);
					eb.setWork_id(work_list.get(pager.getCurrentItem()).getId()
							+ "");
					eb.setActivity_item(1);
					typeList = ExerciseBookTool.getTypeList(pojo);
					question_type = questiontype_list;
					layout_index = i;
					Log.i("suanfa", "worknumber:" + number);
					Log.i("suanfa", "类型:" + typeList.size());
					Log.i("suanfa", "任务ID:" + pojo.getId());
					Log.i("suanfa",
							"保存路径:" + pathList.get(pager.getCurrentItem()));
					Log.i("suanfa", "下载路径:" + pojo.getQuestion_packages_url());
					Log.i("suanfa", "Question_types:"
							+ pojo.getQuestion_types().size() + "/"
							+ pojo.getQuestion_types().get(i));
					Log.i("suanfa", "Finish_types:"
							+ pojo.getFinish_types().size());
					out_time = ExerciseBookTool.Comparison_Time(
							ExerciseBookTool.getTimeIng(),
							work_list.get(pager.getCurrentItem()).getEnd_time());
					if (ExerciseBookTool.FileExist(
							pathList.get(pager.getCurrentItem()),
							"questions.json")) {// 判断question文件是否存在
						getJsonPath();
						if (ExerciseBookTool.FileExist(
								pathList.get(pager.getCurrentItem()),
								"student_" + eb.getUid() + ".json")) {// 判断answer文件是否存在
							startDekaron(i);
						} else {
							Log.i("suanfa", "answer文件不存在,正在下载");
							handler.sendEmptyMessage(6);
						}
					} else {
						handler.sendEmptyMessage(4);
					}
				} else {
					Toast.makeText(RecordMainActivity.this, "暂未进行激活，无法作答作业",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		int imgid = 0;
		imageView.setBackgroundResource(0);
		try {// 利用java反射动态设置图片
			imgid = (Integer) R.drawable.class.getField(
					"img" + (pojo.getQuestion_types().get(i) + 1)).get(null);
		} catch (Exception e) {
			imgid = 0;
		}
		imageView.setBackgroundResource(imgid);

		if (ExerciseBookTool.getExist(pojo.getQuestion_types().get(i),
				pojo.getFinish_types())) {// 排行标志的隐藏与显示
			over_img.setVisibility(View.VISIBLE);
			top.setVisibility(View.VISIBLE);
		} else {
			over_img.setVisibility(View.GONE);
			top.setVisibility(View.GONE);
		}
		AbsListView.LayoutParams param;
		if (width == 1200) {
			param = new AbsListView.LayoutParams(290, 300);
		} else {
			param = new AbsListView.LayoutParams(190, 200);
		}
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

	public boolean getUpdateTime() {
		if (!work_list.get(pager.getCurrentItem()).getUpdated_at()
				.equals("null")) {// 如果Updated_at等于null说明第一次做
			String answer_time = ExerciseBookTool.getAnswerTime(pathList
					.get(pager.getCurrentItem())
					+ "/student_"
					+ eb.getUid()
					+ ".json");
			Log.i("suanfa",
					"answertime:"
							+ answer_time
							+ "/"
							+ work_list.get(pager.getCurrentItem())
									.getUpdated_at());
			return ExerciseBookTool.Comparison_Time(answer_time,
					work_list.get(pager.getCurrentItem()).getUpdated_at());
		}
		return false;
	}

	public void startDekaron(int i) {
		if (getUpdateTime()) {
			handler.sendEmptyMessage(6);
		} else {
			if (typeList.get(i) || out_time == false) {// 已完成
				MyDialog(i, question_type);
			} else {
				if (cardType) {
					status = 0;
					Start_Acvivity(i, question_type);
				} else {
					Builder builder = new Builder(RecordMainActivity.this);
					builder.setTitle("提示");
					builder.setMessage("您的卡包已满,先清除几张再回来答题吧");
					builder.setNegativeButton("确定", null);
					builder.show();
				}
			}
		}
	}

	public void Start_Acvivity(int i, List<Integer> questiontype_list) {// 做题跳转
		switch (questiontype_list.get(i)) {
		case 0:
			intent.setClass(this, AnswerDictationPrepareActivity.class);
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
				+ "/student_" + eb.getUid() + ".json");
		intent.putExtra("type", 0);// 0 今日任务列表跳转 1历史记录列表跳转
		intent.putExtra("status", status);// 0表示第一次做 1表示重做 2历史
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		eb.setPath(pathList.get(pager.getCurrentItem()));
		startActivity(intent);
		this.finish();
	}

	public void Start_History_Acvivity(int i, List<Integer> questiontype_list) {// 历史记录跳转
		switch (questiontype_list.get(i)) {
		case 0:
			intent.setClass(this, AnswerDictationPrepareActivity.class);
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
				+ "/student_" + eb.getUid() + ".json");
		intent.putExtra("type", 0);// 0 今日任务列表跳转 1历史记录列表跳转
		intent.putExtra("status", status);// 0表示第一次做 1表示重做 2历史
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		eb.setPath(pathList.get(pager.getCurrentItem()));
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
				unzip_type = false;
				File f = new File(pathList.get(pager.getCurrentItem())
						+ "/questions.json");
				if (f.exists()) {
					boolean cg = f.delete();
					Log.i("suanfa", cg + "");
				}
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
					unzip_type = true;
					cancelUpdate = false;
					Log.i("suanfa", downPath);
					URL url = new URL(downPath);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();
					Log.i("suanfa", "1====");
					File file = new File(pathList.get(pager.getCurrentItem()));
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					Log.i("suanfa", "2====");
					File apkFile = new File(
							pathList.get(pager.getCurrentItem()), download_name);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					Log.i("suanfa", "3====");
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						// 更新进度
						if (download_name.equals("resourse.zip")) {
							progress = (int) (((float) count / length) * 100);
							handler.sendEmptyMessage(5);
						}
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
					if (unzip_type = true) {
						if (download_name.equals("resourse.zip")) {
							ExerciseBookTool.unZip(
									pathList.get(pager.getCurrentItem()) + "/"
											+ download_name,
									pathList.get(pager.getCurrentItem()));
							getJsonPath();
						} else {
							handler.sendEmptyMessage(7);
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(RecordMainActivity.this, "解压文件发生异常",
						Toast.LENGTH_SHORT).show();
			}

			if (download_name.equals("resourse.zip")) {
				// 取消下载对话框显示
				mDownloadDialog.dismiss();
			}
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
					Map<Integer, Integer> number = WorkJson.getProp(json);
					eb.setTrue_number(number.get(1));
					eb.setTime_number(number.get(0));
					for (int i = 0; i < work_list.size(); i++) {
						String path = Environment.getExternalStorageDirectory()
								+ "/" + "Exercisebook_app/" + eb.getUid() + "/"
								+ eb.getClass_id() + "/"
								+ work_list.get(i).getId();
						String downPath = IP
								+ work_list.get(i).getQuestion_packages_url();
						pathList.add(path);
						downloadList.add(downPath);
						if (work_list.get(i).getUpdated_at().equals("null")) {// 如果Updated_at等于null说明第一次做
							ExerciseBookTool.initAnswer(pathList.get(i),
									work_list.get(i).getId() + "", eb.getUid());// 初始化answer
						}
					}
					handler.sendEmptyMessage(8);
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
					Map<Integer, Integer> number = WorkJson.getProp(json);
					eb.setTrue_number(number.get(0));
					eb.setTime_number(number.get(1));
					for (int i = 0; i < work_list.size(); i++) {
						String path = Environment.getExternalStorageDirectory()
								+ "/" + "Exercisebook_app/" + eb.getUid() + "/"
								+ eb.getClass_id() + "/"
								+ work_list.get(i).getId();
						String downPath = IP
								+ work_list.get(i).getQuestion_packages_url();
						pathList.add(path);
						downloadList.add(downPath);
						if (work_list.get(i).getUpdated_at().equals("null")) {// 如果Updated_at等于null说明第一次做
							ExerciseBookTool.initAnswer(pathList.get(i),
									work_list.get(i).getId() + "", eb.getUid());// 初始化answer
						}
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
					jumpYear, year_c, month_c, day_c, work_list);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);

			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			// 向右滑动
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c, work_list);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);

			return true;
		}
		return false;
	}

	/**
	 * 创建菜单
	 */
	@SuppressWarnings("static-access")
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
					jumpYear, year_c, month_c, day_c, work_list);
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

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_month:
			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c, work_list);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);
			break;
		case R.id.btn_prev_month:
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			calV = new CalendarAdapter(this, getResources(), jumpMonth,
					jumpYear, year_c, month_c, day_c, work_list);
			gridView.setAdapter(calV);
			addTextToTopTextView(topText);
			break;
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

}