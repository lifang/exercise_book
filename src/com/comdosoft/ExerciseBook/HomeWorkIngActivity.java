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
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private LinearLayout mylayout;
	private int linear_item = 0;
	private List<LinearLayout> linearList = new ArrayList<LinearLayout>();
	private Intent intent = new Intent();
	private ProgressDialog prodialog;
	private String notice;
	private List<Integer> questiontype_list = new ArrayList<Integer>();
	private List<Integer> finish_list = new ArrayList<Integer>();
	private List<WorkPoJo> work_list = new ArrayList<WorkPoJo>();
	private TextView start_time;
	private TextView end_time;
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
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				if (work_list.size() != 0) {
					start_time.setText("发布时间为"
							+ work_list.get(0).getStart_time().split(" ")[1]);
					end_time.setText(work_list.get(0).getEnd_time().split(" ")[1]);
					questiontype_list = work_list.get(0).getQuestion_types();
					finish_list = work_list.get(0).getFinish_types();
					eb.setToday_newer_id(work_list.get(0).getId());
					if (questiontype_list.size() != 0) {
						for (int i = 0; i < questiontype_list.size(); i++) {
							setlayout(i);
						}
					} else {
						mylayout.setVisibility(View.GONE);
					}
				} else {
					mylayout.setVisibility(View.GONE);
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
				mylayout.setVisibility(View.GONE);
				break;
			case 3:
				Builder builder = new Builder(HomeWorkIngActivity.this);
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
		setContentView(R.layout.homework_ing);
		eb = (ExerciseBook) getApplication();// 初始化
		eb.getActivityList().add(this);
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
		int width = display.getWidth();
		int height = display.getHeight();
		Log.i(id, width + "/" + height);

		prodialog = new ProgressDialog(HomeWorkIngActivity.this);
		prodialog.setMessage("正在获取最新作业");
		prodialog.setCanceledOnTouchOutside(false);
		prodialog.show();
		Thread thread = new Thread(new get_newer_task());
		thread.start();
	}

	// 初始化
	public void initialize() {
		mylayout = (LinearLayout) findViewById(R.id.mylayout);
		start_time = (TextView) findViewById(R.id.start_time);
		end_time = (TextView) findViewById(R.id.end_time);
	}

	private void getJsonPath() {
		File file = new File(path + "/questions.json");
		if (file.exists()) {
			Log.i("linshi", "获取json");
			String jsons = ExerciseBookTool.getJson(path + "/questions.json");
			SetJson(jsons);
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

	public void setlayout(final int i) {
		View view = View.inflate(this, R.layout.work_item, null);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		TextView work_name = (TextView) view.findViewById(R.id.work_name);
		TextView top = (TextView) view.findViewById(R.id.top);
		ImageView over_img = (ImageView) view.findViewById(R.id.over_img);
		work_name.setText(namearr[questiontype_list.get(i)].toString());
		layout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				startDekaron(i);// 跳转到答题页面
			}
		});
		top.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(HomeWorkIngActivity.this,
						RankingOfPointsActivity.class);
				intent.putExtra("types", questiontype_list.get(i));
				intent.putExtra("pub_id", Integer.valueOf(eb.getWork_id()));
				startActivity(intent);
			}
		});
		if (ExerciseBookTool.getExist(questiontype_list.get(i), finish_list)) {
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
					"img" + (questiontype_list.get(i) + 1)).get(null);
		} catch (Exception e) {
			imgid = 0;
		}
		imageView.setBackgroundResource(imgid);

		AbsListView.LayoutParams param = new AbsListView.LayoutParams(210, 220);
		layout.setLayoutParams(param);
		if (i == 0 || i % 3 == 0) {
			LinearLayout linear = new LinearLayout(getApplicationContext());
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
			Map<String, String> map = new HashMap<String, String>();
			map.put("student_id", id);
			map.put("school_class_id", school_class_id);
			String json;
			try {
				json = ExerciseBookTool.sendGETRequest(get_newer_task, map);
				JSONObject obj = new JSONObject(json);
				if (obj.getString("status").equals("success")) {
					work_list = WorkJson.json(json);
					int[] number = WorkJson.getProp(json);
					eb.setTrue_number(number[0]);
					eb.setTime_number(number[1]);
					if (work_list.size() != 0) {
						eb.setWork_number(work_list.get(0).getQuestion_types()
								.size());
						cardType = work_list.get(0).getNumber() < 20 ? true
								: false;
						eb.setWork_id(work_list.get(0).getId() + "");
						path = Environment.getExternalStorageDirectory() + "/"
								+ "Exercisebook_app/" + eb.getUid() + "/"
								+ eb.getClass_id() + "/" + eb.getWork_id();
						eb.setPath(path);
						downPath = IP
								+ work_list.get(0).getQuestion_packages_url();
						getJsonPath();
						ExerciseBookTool.initAnswer(path, eb.getWork_id());// 初始化answer
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

	// 跳转到挑战
	/**
	 * 0 =>听力 1=>朗读 2 =>十速 3=>选择 4=>连线 5=>完形 6=>排序
	 * 
	 * @param i
	 */

	public void startDekaron(int i) {
		if (ExerciseBookTool.FileExist(path)) {// 判断文件是否存在
			eb.setActivity_item(0);
			if (typeList.get(i)) {// 已完成
				MyDialog(i);
			} else {
				if (cardType) {
					status = 0;
					Start_Acvivity(i);
				} else {
					Builder builder = new Builder(HomeWorkIngActivity.this);
					builder.setTitle("提示");
					builder.setMessage("您的卡包已满,先清除几张再回来答题吧");
					builder.setNegativeButton("确定", null);
					builder.show();
				}
			}
		} else {
			handler.sendEmptyMessage(3);
		}
	}

	public void Start_Acvivity(int i) {// 做题跳转
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
		intent.putExtra("json", json_list.get(questiontype_list.get(i)));
		intent.putExtra("path", path + "/answer.json");
		intent.putExtra("type", 0);// 0 今日任务列表跳转 1历史记录列表跳转
		intent.putExtra("status", status);// 0表示第一次做 1表示重做 2历史
		Log.i("aaa", json_list.get(questiontype_list.get(i)));
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		startActivity(intent);
		this.finish();
	}

	public void Start_History_Acvivity(int i) {// 历史记录跳转
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
		intent.putExtra("path", path + "/answer.json");
		intent.putExtra("type", 0);// 0 今日任务列表跳转 1历史记录列表跳转
		intent.putExtra("status", status);// 0表示第一次做 1表示重做 2历史
		Log.i("aaa", json_list.get(questiontype_list.get(i)));
		eb.setWork_end_dath(work_list.get(0).getEnd_time());
		startActivity(intent);
		this.finish();
	}

	// 自定义dialog设置
	private void MyDialog(final int type) {
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
				Start_History_Acvivity(type);
			}
		});
		working.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				status = 1;// 0表示第一次做 1表示重做 2历史
				Start_Acvivity(type);
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

					File file = new File(path);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(path, "resourse.zip");
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
					ExerciseBookTool.unZip(path + "/resourse.zip", path);
					getJsonPath();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(HomeWorkIngActivity.this, "解压文件发生异常",
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

	public void onclick(View v) {
		this.finish();
		intent.setClass(HomeWorkIngActivity.this, RecordMainActivity.class);
		startActivity(intent);
	}
}