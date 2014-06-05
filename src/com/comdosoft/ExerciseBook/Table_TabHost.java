package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.CircularImage;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class Table_TabHost extends Activity implements Urlinterface {
	private LinearLayout middleLayout;
	private LinearLayout img_tab_now;
	private ExerciseBook eb;
	private String nickName = "丁作"; // 用户昵称
	private String name = "丁作";
	TextView userName, activity_title;//
	private String edu_number = "";
	private ImageView faceImage;
	private LinearLayout userInfo;
	private String avatar_url = "/avatars/students/2014-02/student_73.jpg"; // 用户头像
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private String id = "73"; // 用户 id，，切记 不是 user_id
	private String json = "";
	private String uri;
	private ProgressDialog prodialog;
	static boolean active = false;
	public static Table_TabHost instance = null;
	ImageMemoryCache memoryCache;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog DownloadDialog;
	private boolean cancelUpdate = false;
	/* 下载保存路径 */
	private String SavePath;
	/* 记录进度条数量 */
	private int progress;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				final String res = (String) msg.obj;
				if (res.length() != 0) {
					JSONObject array;

					try {
						array = new JSONObject(res);
						Boolean status = array.getBoolean("status");
						String notice = array.getString("notice");

						if (status == true) {
							memoryCache.removeBitmap(Urlinterface.IP
									+ avatar_url);
							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 7;// 7就代表容量变为以前容量的1/7
							Bitmap bm = BitmapFactory.decodeFile(uri, options);
							memoryCache.addBitmapToCache(Urlinterface.IP
									+ avatar_url, bm);
							faceImage.setImageDrawable(new BitmapDrawable(bm));

							File file = new File(uri);
							if (file.exists()) {
								file.delete();
							}
							eb.setRefresh(1);
						} else {
							Toast.makeText(getApplicationContext(), notice, 0)
									.show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;
			case 7:
				prodialog.dismiss();
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			case 8:

				Builder builder = new Builder(Table_TabHost.this);
				builder.setTitle("提示");
				builder.setMessage("检测到新版本,您需要更新吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								showDownloadDialog_table();
							}
						});
				builder.setNegativeButton("下次再说",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								eb.setUpdate(false);
							}
						}).show();
				break;
			case 9:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case 10:
				// 安装文件
				installApk();
				break;
			default:
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.table);
		eb = (ExerciseBook) getApplication();
		// memoryCache = new ImageMemoryCache(this);
		memoryCache = eb.getMemoryCache();
		SharedPreferences preferences = getSharedPreferences(
				Urlinterface.SHARED, Context.MODE_PRIVATE);
		avatar_url = preferences.getString("avatar_url", "");
		nickName = preferences.getString("nickname", "");
		name = preferences.getString("name", "");
		edu_number = preferences.getString("edu_number", "");
		id = preferences.getString("id", null);
		instance = this;
		active = true;
		img_tab_now = (LinearLayout) findViewById(R.id.img_tab_now);
		img_tab_now.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// if(eb.isMneu())
				// {

				Intent intent = new Intent(Table_TabHost.this, LeftMenu.class);
				startActivity(intent);
				// eb.setMneu(false);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				// }
			}
		});
		middleLayout = (LinearLayout) findViewById(R.id.middle_layout);
		userName = (TextView) findViewById(R.id.user_name);
		if (edu_number.equals("")) {
			userName.setText(nickName);
		} else {
			userName.setText(name);

		}
		activity_title = (TextView) findViewById(R.id.activity_title);
		activity_title.setText(Urlinterface.left_menu[eb.getMenu_num() - 1]);
		Log.i("Ax",
				Urlinterface.left_menu[eb.getMenu_num() - 1] + "/"
						+ eb.getMenu_num());
		userInfo = (LinearLayout) findViewById(R.id.user_button);
		faceImage = (CircularImage) findViewById(R.id.user_face);
		if (ExerciseBookTool.isConnect(getApplicationContext())) {
			if (avatar_url.length() > 4) { // 设置头像
				String url = Urlinterface.IP + avatar_url;
				Bitmap result = memoryCache.getBitmapFromCache(url);
				if (result == null) {
					ExerciseBookTool.set_bk(url, faceImage, memoryCache);
				} else {
					faceImage.setImageDrawable(new BitmapDrawable(result));
				}
			}
		}
		faceImage.setOnClickListener(listener);
		userInfo.setOnClickListener(listener2);
		String guide = preferences.getString("guide", "");
		if (guide.equals("")) {

			Editor editor = preferences.edit();
			editor.putString("guide", "guide");
			editor.commit();
			Intent intentp = new Intent();
			intentp.setClass(Table_TabHost.this, GuidePageActivity.class);//
			startActivityForResult(intentp, 0);
		}

		if (ExerciseBookTool.isConnect(Table_TabHost.this)) {
			if (eb.isUpdate()) {
				GetCurrent_Version.start();
			}
		}

	}

	public void setContentView(int layoutId) {
		View middleView = getLayoutInflater().inflate(layoutId, null);
		if (null != middleLayout) {
			middleLayout.removeAllViews();
			middleLayout.addView(middleView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intentp = new Intent();
			intentp.setClass(Table_TabHost.this, SettingPhoto.class);//
			startActivityForResult(intentp, 0);
		}
	};
	private View.OnClickListener listener2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.setClass(Table_TabHost.this, UserInfoActivity.class);//
			startActivityForResult(intentp, 0);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		// 如果是直接从相册获取
		case -11:

			Bundle bundle = data.getExtras();
			uri = bundle.getString("uri");

			// 此处调用上传头像的方法，若成功则设置 头像
			if (ExerciseBookTool.isConnect(Table_TabHost.this)) {
				prodialog = new ProgressDialog(Table_TabHost.this);
				prodialog.setMessage("正在提交数据...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				Thread thread = new Thread(new mod_avatar());
				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, 0).show();
			}

			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	class mod_avatar implements Runnable {
		public void run() {
			try {

				MultipartEntity entity = new MultipartEntity();

				entity.addPart("student_id", new StringBody(id));
				File f = new File(uri);

				Log.i("suanfa", f.getPath() + "");
				if (f.exists()) {
					entity.addPart("avatar", new FileBody(new File(uri)));
				}

				json = ExerciseBookTool.sendPhostimg(
						Urlinterface.MODIFY_PERSON_INFO, entity);
				Log.i("suanfa", json);
				Message msg = new Message();// 创建Message 对象
				msg.what = 0;
				msg.obj = json;
				mHandler.sendMessage(msg);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "修改头像失败", 0).show();
				mHandler.sendEmptyMessage(7);
			}
		}
	}

	Thread GetCurrent_Version = new Thread() {
		public void run() {
			Map<String, String> mp = new HashMap<String, String>();
			try {
				String json = ExerciseBookTool.sendGETRequest(version, mp);
				Log.i("aa", "版本号:" + json);
				if (!json.equals("")) {
					JSONObject obj = new JSONObject(json);
					double version = obj.getDouble("current_version");
					if (version > Urlinterface.current_version) {
						mHandler.sendEmptyMessage(8);
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public void showDownloadDialog_table() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(Table_TabHost.this);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(Table_TabHost.this);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		DownloadDialog = builder.create();
		DownloadDialog.setCanceledOnTouchOutside(false);
		DownloadDialog.show();
		// 现在文件
		downloadApk_table();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	public class downloadApkThread_table extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					Log.i("suanfa", sdpath);
					SavePath = sdpath + "download";
					URL url = new URL(fileurl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(SavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(SavePath, filename);
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
						mHandler.sendEmptyMessage(9);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(10);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			DownloadDialog.dismiss();
		}
	};

	/**
	 * 下载apk文件
	 */
	public void downloadApk_table() {
		// 启动新线程下载软件
		new downloadApkThread_table().start();
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(SavePath, filename);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivity(i);
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