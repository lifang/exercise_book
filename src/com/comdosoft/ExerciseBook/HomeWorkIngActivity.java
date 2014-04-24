package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.WorkJson;

public class HomeWorkIngActivity extends Table_TabHost implements Urlinterface {
	private String id;
	private String school_class_id;
	// private String json =
	// "{\"status\":\"success\",\"notice\":\"\u83b7\u53d6\u6210\u529f\uff01\",\"tasks\":[{\"id\":130,\"name\":\"\",\"start_time\":\"2014-03-12T14:44:45+08:00\",\"question_types\":[0,1,2,3,4,5,6],\"finish_types\":[2],\"end_time\":\"2014-03-13T18:00:00+08:00\",\"question_packages_url\":\"/que_ps/question_p_264/resourse.zip\"}],\"knowledges_cards_count\":10}";
	private ExerciseBook eb;
	private int linear_item = 0;
	private List<LinearLayout> linearList = new ArrayList<LinearLayout>();
	private Intent intent = new Intent();
	private ProgressDialog prodialog;
	private String notice;
	private List<WorkPoJo> work_list = new ArrayList<WorkPoJo>();
	static HomeWorkIngActivity instance = null;
	private String path;
	private String downPath;
	private List<String> json_list;
	private List<Boolean> typeList;
	private boolean cardType = false;
	private int status;
	private boolean cancelUpdate;
	private Dialog mDownloadDialog;
	private int progress;
	private ProgressBar mProgress;
	private String download_name;
	private boolean out_time;
	private int layout_index;// 记录点击
	private int width;
	private ViewPager pager;
	private ImageView left;
	private ImageView right;
	private List<String> pathList;
	private List<String> downloadList;
	private int number;
	private ImagePagerAdapter ipa;
	private List<Integer> question_type;
	private LinearLayout ll;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			Builder builder = new Builder(HomeWorkIngActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				if (work_list.size() != 0) {
					number = work_list.size();
					pager.setAdapter(ipa);
				} else {
					ll.setVisibility(View.GONE);
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
				Toast.makeText(HomeWorkIngActivity.this, notice,
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				prodialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接异常!",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
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
			case 4:
				prodialog.setMessage("正在更新答题记录,请稍后...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				downPath = IP
						+ work_list.get(pager.getCurrentItem()).getAnswer_url();
				download_name = "student_" + eb.getUid() + ".json";
				downloadApk();
				break;
			case 5:
				mProgress.setProgress(progress);
				break;
			case 6:
				prodialog.dismiss();
				ExerciseBookTool.UpdateJsonTime(
						work_list.get(pager.getCurrentItem()).getUpdated_at(),
						pathList.get(pager.getCurrentItem()) + "/student_"
								+ eb.getUid() + ".json");
				startDekaron(layout_index);
				break;
			}
		};
	};

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homework_ing);
		eb = (ExerciseBook) getApplication();// 初始化
		eb.getActivityList().add(this);
		eb.setMenu_num(1);
		typeList = new ArrayList<Boolean>();
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		id = preferences.getString("id", "0");
		school_class_id = preferences.getString("school_class_id", "0");
		Log.i("aaa", id + "/" + school_class_id);
		eb.setUid(id);
		eb.setClass_id(school_class_id);
		initialize();// 初始化
		instance = this;
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		prodialog = new ProgressDialog(HomeWorkIngActivity.this);
		prodialog.setMessage("正在获取最新作业");
		prodialog.setCanceledOnTouchOutside(false);
		prodialog.show();
		Thread thread = new Thread(new get_newer_task());
		thread.start();
	}

	// 初始化
	public void initialize() {
		ll = (LinearLayout) findViewById(R.id.ll);
		ipa = new ImagePagerAdapter();
		pager = (ViewPager) findViewById(R.id.vPager);
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
	}

	private void getJsonPath() {
		File file = new File(pathList.get(pager.getCurrentItem())
				+ "/questions.json");
		if (file.exists()) {
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
				Intent intent = new Intent(HomeWorkIngActivity.this,
						RankingOfPointsActivity.class);
				intent.putExtra("types", questiontype_list.get(i));
				intent.putExtra("pub_id", Integer.valueOf(pojo.getId()));
				startActivity(intent);
			}
		});

		layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
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
				Log.i("suanfa", "保存路径:" + pathList.get(pager.getCurrentItem()));
				Log.i("suanfa", "下载路径:" + pojo.getQuestion_packages_url());
				Log.i("suanfa", "Question_types:"
						+ pojo.getQuestion_types().size() + "/"
						+ pojo.getQuestion_types().get(i));
				Log.i("suanfa", "Finish_types:" + pojo.getFinish_types().size());
				out_time = ExerciseBookTool.Comparison_Time(
						ExerciseBookTool.getTimeIng(),
						work_list.get(pager.getCurrentItem()).getEnd_time());
				if (ExerciseBookTool.FileExist(
						pathList.get(pager.getCurrentItem()), "questions.json")) {// 判断question文件是否存在
					getJsonPath();
					if (ExerciseBookTool.FileExist(
							pathList.get(pager.getCurrentItem()), "student_"
									+ eb.getUid() + ".json")) {// 判断answer文件是否存在
						startDekaron(i);
					} else {
						Log.i("suanfa", "answer文件不存在,正在下载");
						handler.sendEmptyMessage(4);
					}
				} else {
					handler.sendEmptyMessage(3);
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
			LinearLayout linear = new LinearLayout(HomeWorkIngActivity.this);
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

	class get_newer_task implements Runnable {

		public void run() {
			pathList = new ArrayList<String>();
			downloadList = new ArrayList<String>();
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", id);
			map.put("school_class_id", school_class_id);
			String json;
			try {
				json = ExerciseBookTool.sendGETRequest(get_newer_task, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					Map<Integer, Integer> number = WorkJson.getProp(json);
					eb.setTrue_number(number.get(1));
					eb.setTime_number(number.get(0));
					if (work_list.size() != 0) {
						for (int i = 0; i < work_list.size(); i++) {
							String path = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "Exercisebook_app/"
									+ eb.getUid()
									+ "/"
									+ eb.getClass_id()
									+ "/"
									+ work_list.get(i).getId();
							String downPath = IP
									+ work_list.get(i)
											.getQuestion_packages_url();
							pathList.add(path);
							downloadList.add(downPath);
							if (work_list.get(i).getUpdated_at().equals("null")) {// 如果Updated_at等于null说明第一次做
								ExerciseBookTool.initAnswer(pathList.get(i),
										work_list.get(i).getId() + "",
										eb.getUid());// 初始化answer
							}
						}
					}
					handler.sendEmptyMessage(0);
				} else {
					notice = obj.getString("notice");
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				// handler.sendEmptyMessage(2);
				// e.printStackTrace();
			}
		}
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
			handler.sendEmptyMessage(4);
		} else {
			if (typeList.get(i) || out_time == false) {// 已完成
				MyDialog(i, question_type);
			} else {
				if (cardType) {
					status = 0;
					Start_Acvivity(i, question_type);
				} else {
					Builder builder = new Builder(HomeWorkIngActivity.this);
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
		Log.i("Max", json_list.size() + "");
		Log.i("Max", questiontype_list.size() + "-" + questiontype_list.get(i));
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
			intent.setClass(this, AnswerDictationBeginActivity.class);
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
		Log.i("Ax", questiontype_list.size() + ",.,.");
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
		AlertDialog.Builder builder = new Builder(HomeWorkIngActivity.this);
		builder.setTitle("正在下载");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater
				.from(HomeWorkIngActivity.this);
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
					if (download_name.equals("resourse.zip")) {
						ExerciseBookTool.unZip(
								pathList.get(pager.getCurrentItem()) + "/"
										+ download_name,
								pathList.get(pager.getCurrentItem()));
						getJsonPath();
					} else {
						handler.sendEmptyMessage(6);
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(HomeWorkIngActivity.this, "解压文件发生异常",
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

	public void onclick(View v) {
		intent.setClass(HomeWorkIngActivity.this, RecordMainActivity.class);
		startActivity(intent);
		HomeWorkIngActivity.this.finish();
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