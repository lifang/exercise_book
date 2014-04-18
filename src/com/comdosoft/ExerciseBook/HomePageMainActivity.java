package com.comdosoft.ExerciseBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.CircularImage;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

/**
 * @作者 丁作强
 * @时间 2014-4-17 上午11:34:27
 */
public class HomePageMainActivity extends TabActivity implements Urlinterface {
	TabHost tabhost;
	TabHost.TabSpec spec1, spec2, spec3, spec4;
	private ImageView allbottom, myselfbottom, focusbottom, senderbottom;
	private ImageView faceImage;
	private LinearLayout userInfo, logo;
	private Resources res;
	public Field mBottomLeftStrip;
	public Field mBottomRightStrip;
	private ExerciseBook exerciseBook;
	private int width;
	private ProgressDialog prodialog;
	/* 头像名称 */
	private String id = "73"; // 用户 id，，切记 不是 user_id
	private String json = "";
	private String uri;
	private String avatar_url = "/avatars/students/2014-02/student_73.jpg"; // 用户头像
	private String nickName = "丁作"; // 用户昵称
	private String name = "丁作";
	TextView userName;//
	private String edu_number = "";
	static boolean active = false;
	ImageMemoryCache memoryCache;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private boolean cancelUpdate = false;
	/* 下载保存路径 */
	private String mSavePath;
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
							exerciseBook.setRefresh(1);
						}
						Toast.makeText(getApplicationContext(), notice, 0)
								.show();
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "修改头像失败", 0)
								.show();
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

				Builder builder = new Builder(HomePageMainActivity.this);
				builder.setTitle("提示");
				builder.setMessage("检测到新版本,您需要更新吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								showDownloadDialog();
							}
						});
				builder.setNegativeButton("下次再说",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								exerciseBook.setUpdate(false);
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
	private List<Integer> new_idlist = new ArrayList<Integer>();
	public static HomePageMainActivity instance = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homewrok_main);
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		avatar_url = preferences.getString("avatar_url", "");
		nickName = preferences.getString("nickname", "");
		id = preferences.getString("id", null);
		name = preferences.getString("name", "");
		edu_number = preferences.getString("edu_number", "");
		// memoryCache = new ImageMemoryCache(this);
		active = true;
		exerciseBook = (ExerciseBook) getApplication();
		memoryCache = exerciseBook.getMemoryCache();
		exerciseBook.getActivityList().add(this);
		exerciseBook.setMainItem(0);
		instance = this;
		tabhost = getTabHost();
		res = getResources();
		logo = (LinearLayout) findViewById(R.id.img_tab_now);
		allbottom = (ImageView) findViewById(R.id.all_bottom);
		myselfbottom = (ImageView) findViewById(R.id.myself_bottom);
		focusbottom = (ImageView) findViewById(R.id.focus_bottom);
		senderbottom = (ImageView) findViewById(R.id.sender_bottom);
		userName = (TextView) findViewById(R.id.user_name);
		if (edu_number.equals("")) {
			userName.setText(nickName);
		} else {
			userName.setText(name);
		}

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
		logo.setOnClickListener(listener3);
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();

		Intent intent1 = new Intent(this, HomepageAllActivity.class);
		spec1 = tabhost.newTabSpec("spec1")
				.setIndicator("", res.getDrawable(R.drawable.all_2))
				.setContent(intent1);
		tabhost.addTab(spec1);

		Intent intent2 = new Intent(this, HomepageMyselfActivity.class);
		spec2 = tabhost.newTabSpec("spec2")
				.setIndicator("", res.getDrawable(R.drawable.myself_1))
				.setContent(intent2);
		tabhost.addTab(spec2);

		Intent intent3 = new Intent(this, HomepageFocusActivity.class);
		spec3 = tabhost.newTabSpec("spec3")
				.setIndicator("", res.getDrawable(R.drawable.sender_1))
				.setContent(intent3);
		tabhost.addTab(spec3);

		Intent intent4 = new Intent(this, HomePageSendMessage.class);
		spec4 = tabhost.newTabSpec("spec4")
				.setIndicator("", res.getDrawable(R.drawable.sender_1))
				.setContent(intent4);
		tabhost.addTab(spec4);

		tabhost.setCurrentTab(exerciseBook.getMainItem());
		updateTabStyle(tabhost);

		if (ExerciseBookTool.isConnect(HomePageMainActivity.this)) {
			if (exerciseBook.isUpdate()) {
				GetCurrent_Version.start();
			}
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

	private void updateTabStyle(final TabHost mTabHost) {
		final TabWidget tabWidget = mTabHost.getTabWidget();
		tabWidget.setBackgroundColor(res.getColor(R.color.top_huise));
		tabWidget.setGravity(Gravity.CENTER_VERTICAL);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			RelativeLayout tabView = (RelativeLayout) mTabHost.getTabWidget()
					.getChildAt(i);
			if (width == 800) {
				LayoutParams lp = new LayoutParams(170, 90);
				tabView.setLayoutParams(lp);
			}
			ImageView img = (ImageView) tabWidget.getChildAt(i).findViewById(
					android.R.id.icon);

			if (i == exerciseBook.getMainItem()) {
				tabView.setBackgroundColor(res.getColor(R.color.top_huise));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.all_2);
					allbottom.setVisibility(View.VISIBLE);
					break;
				case 1:
					img.setImageResource(R.drawable.myself_2);
					myselfbottom.setVisibility(View.VISIBLE);
					break;
				case 2:
					img.setImageResource(R.drawable.focus_2);
					focusbottom.setVisibility(View.VISIBLE);
					break;
				case 3:
					img.setImageResource(R.drawable.sender_2);
					senderbottom.setVisibility(View.VISIBLE);
					break;

				}
			} else {
				tabView.setBackgroundColor(res.getColor(R.color.top_huise));
				switch (i) {
				case 0:
					img.setImageResource(R.drawable.all_1);
					allbottom.setVisibility(View.GONE);
					break;
				case 1:
					img.setImageResource(R.drawable.myself_1);
					myselfbottom.setVisibility(View.GONE);
					break;
				case 2:
					img.setImageResource(R.drawable.focus_1);
					focusbottom.setVisibility(View.GONE);
					break;
				case 3:
					img.setImageResource(R.drawable.sender_1);
					senderbottom.setVisibility(View.GONE);
					break;

				}
			}
			if (width == 800) {
				img.setPadding(134, 20, 0, 0);
			} else {
				img.setPadding(200, 30, 0, 0);
			}
			// img.setPadding(0, 0, 0, 0);
			/**
			 * 此方法是为了去掉系统默认的色白的底角
			 * 
			 * 在 TabWidget中mBottomLeftStrip、mBottomRightStrip
			 * 都是私有变量，但是我们可以通过反射来获取
			 * 
			 * 由于Android 2.2，2.3的接口不同，加个判断
			 */

			if (Float.valueOf(Build.VERSION.RELEASE.substring(0, 3)) <= 2.1) {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.no));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.no));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

				// 如果是2.2,2.3版本开发,可以使用一下方法tabWidget.setStripEnabled(false)
				// tabWidget.setStripEnabled(false);

				// 但是很可能你开发的android应用是2.1版本，
				// tabWidget.setStripEnabled(false)编译器是无法识别而报错的,这时仍然可以使用上面的
				// 反射实现，但是代码的改改

				try {
					// 2.2,2.3接口是mLeftStrip，mRightStrip两个变量，当然代码与上面部分重复了
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.no));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.no));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					for (int i = 0; i < tabWidget.getChildCount(); i++) {
						RelativeLayout tabView = (RelativeLayout) mTabHost
								.getTabWidget().getChildAt(i);
						ImageView img = (ImageView) tabWidget.getChildAt(i)
								.findViewById(android.R.id.icon);

						// if (mTabHost.getCurrentTab() == 1) {
						// Log.i("aaa", "----");
						// flag_hw = false;
						// handler.sendEmptyMessage(1);
						// } else {
						// flag_hw = true;
						// }

						if (mTabHost.getCurrentTab() == i) {
							exerciseBook.setMainItem(i);
							tabView.setBackgroundColor(res
									.getColor(R.color.top_huise));
							switch (i) {
							case 0:

								img.setImageResource(R.drawable.all_2);
								allbottom.setVisibility(View.VISIBLE);
								break;
							case 1:

								img.setImageResource(R.drawable.myself_2);
								myselfbottom.setVisibility(View.VISIBLE);
								break;
							case 2:
								img.setImageResource(R.drawable.focus_2);
								focusbottom.setVisibility(View.VISIBLE);
								break;
							case 3:

								img.setImageResource(R.drawable.sender_2);
								senderbottom.setVisibility(View.VISIBLE);
								break;

							}
						} else {
							tabView.setBackgroundColor(res
									.getColor(R.color.top_huise));
							switch (i) {
							case 0:
								img.setImageResource(R.drawable.all_1);
								allbottom.setVisibility(View.GONE);
								break;
							case 1:
								img.setImageResource(R.drawable.myself_1);
								myselfbottom.setVisibility(View.GONE);
								break;
							case 2:
								img.setImageResource(R.drawable.focus_1);
								focusbottom.setVisibility(View.GONE);
								break;
							case 3:
								img.setImageResource(R.drawable.sender_1);
								senderbottom.setVisibility(View.GONE);
								break;
							}
						}
						if (width == 800) {
							img.setPadding(134, 20, 0, 0);
						} else {
							img.setPadding(200, 30, 0, 0);
						}

						// img.setPadding(0, 0, 0, 0);
					}
				}
			});
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intentp = new Intent();
			// intentp.setClass(HomePageMainActivity.this,
			// RankingOfPointsActivity.class);//
			intentp.setClass(HomePageMainActivity.this, SettingPhoto.class);//
			startActivityForResult(intentp, 0);
		}
	};
	private View.OnClickListener listener2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.setClass(HomePageMainActivity.this, UserInfoActivity.class);//
			startActivityForResult(intentp, 0);
		}
	};
	private View.OnClickListener listener3 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(HomePageMainActivity.this,
					LeftMenu.class);
			exerciseBook.setMneu(false);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
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
			if (ExerciseBookTool.isConnect(HomePageMainActivity.this)) {
				prodialog = new ProgressDialog(HomePageMainActivity.this);
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

	public void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(HomePageMainActivity.this);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater
				.from(HomePageMainActivity.this);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	public class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(fileurl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, filename);
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
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 下载apk文件
	 */
	public void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, filename);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivity(i);
	}

}