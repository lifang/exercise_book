package com.comdosoft.ExerciseBook;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.tools.CircularImage;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class HomePageMainActivity extends TabActivity implements Urlinterface {
	TabHost tabhost;
	TabHost.TabSpec spec1, spec2, spec3;
	private ImageView allbottom, myselfbottom, senderbottom;
	private ImageView faceImage;
	private LinearLayout userInfo;
	private Resources res;
	public Field mBottomLeftStrip;
	public Field mBottomRightStrip;
	private ExerciseBook exerciseBook;
	private int count;
	private boolean flag = true;
	private boolean flag_hw = true;
	private int lastCount;
	private int Size;
	private String num = "0";
	private int hw_num = 0;
	private int width;
	private ProgressDialog prodialog;
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	private String id = "8"; // 用户 id，，切记 不是 user_id
	private String json = "";
	private String uri;
	private String avatar_url = "/avatars/students/2014-02/student_73.jpg"; // 用户头像
	private String nickName = "丁作"; // 用户昵称
	 TextView userName;//  
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				// final String res = (String) msg.obj;
				// if (res.length() != 0) {
				// JSONObject array;
				//
				// try {
				// array = new JSONObject(res);
				// Boolean status = array.getBoolean("status");
				// String notice = array.getString("notice");
				//
				// if (status == true) {
				//
				// Toast.makeText(getApplicationContext(), notice, 0)
				// .show();
				// SharedPreferences preferences = getSharedPreferences(
				// SHARED, Context.MODE_PRIVATE);
				// Editor editor = preferences.edit();
				// // editor.putString("name", nameS);
				//
				//
				// editor.commit();
				 BitmapFactory.Options options = new BitmapFactory.Options();
				 options.inSampleSize = 7;//7就代表容量变为以前容量的1/7
				String uri = Environment.getExternalStorageDirectory()
				+ "/1" + IMAGE_FILE_NAME;
				 Bitmap bm = BitmapFactory.decodeFile(uri, options);
				 faceImage.setImageDrawable(new BitmapDrawable(bm));
					File file = new File(uri);

					if (file.exists()) {
						file.delete();
					}
				//
				// }else {
				// Toast.makeText(getApplicationContext(), notice, 0)
				// .show();
				// }
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// }
				break;
			case 7:
				prodialog.dismiss();
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
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
		File file = new File(Environment.getExternalStorageDirectory() + "/1"
				+ IMAGE_FILE_NAME);

		if (file.exists()) {
			file.delete();
		}

		exerciseBook = (ExerciseBook) getApplication();
		instance = this;
		tabhost = getTabHost();
		res = getResources();
		allbottom = (ImageView) findViewById(R.id.all_bottom);
		myselfbottom = (ImageView) findViewById(R.id.myself_bottom);
		senderbottom = (ImageView) findViewById(R.id.sender_bottom);
		userName=(TextView) findViewById(R.id.user_name);
		userName.setText(nickName);
		
		 userInfo = (LinearLayout) findViewById(R.id.user_button);
		faceImage = (CircularImage) findViewById(R.id.user_face);
		if (ExerciseBookTool.isConnect(getApplicationContext())) {
			if (avatar_url != null || avatar_url.length() != 0) { // 设置头像
				ExerciseBookTool.set_background(Urlinterface.IP + avatar_url,
						faceImage);
			}
		} else { 
			Toast.makeText(getApplicationContext(), ExerciseBookParams.INTERNET, 0)
					.show();
		}
		faceImage.setOnClickListener(listener);
		userInfo.setOnClickListener(listener2);
		Display display = this.getWindowManager().getDefaultDisplay();
		width = display.getWidth();

		Intent intent1 = new Intent(this, HomepageAllActivity.class);
//		 Intent intent1 = new Intent(this, SettingPhoto.class);
		spec1 = tabhost.newTabSpec("spec1")
				.setIndicator("", res.getDrawable(R.drawable.all_2))
				.setContent(intent1);
		tabhost.addTab(spec1);

		Intent intent2 = new Intent(this, HomepageMyselfActivity.class);
		spec2 = tabhost.newTabSpec("spec2")
				.setIndicator("", res.getDrawable(R.drawable.myself_1))
				.setContent(intent2);
		tabhost.addTab(spec2);

		Intent intent3 = new Intent(this, HomePageSendMessage.class);
		spec3 = tabhost.newTabSpec("spec3")
				.setIndicator("", res.getDrawable(R.drawable.sender_1))
				.setContent(intent3);
		tabhost.addTab(spec3);

		tabhost.setCurrentTab(exerciseBook.getMainItem());
		updateTabStyle(tabhost);

	}

	private void updateTabStyle(final TabHost mTabHost) {
		final TabWidget tabWidget = mTabHost.getTabWidget();
		tabWidget.setBackgroundColor(res.getColor(R.color.top_huise));
		tabWidget.setGravity(Gravity.CENTER_VERTICAL);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			RelativeLayout tabView = (RelativeLayout) mTabHost.getTabWidget()
					.getChildAt(i);
			// if (width == 800) {
			// LayoutParams lp = new LayoutParams(150, 90);
			// tabView.setLayoutParams(lp);
			// }
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
					img.setImageResource(R.drawable.sender_1);
					senderbottom.setVisibility(View.GONE);
					break;

				}
			}
			img.setPadding(254, 30, 0, 0);
//			img.setPadding(0, 0, 0, 0);
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
							
								hw_num = 0;
								// handler.sendEmptyMessage(1);
								img.setImageResource(R.drawable.myself_2);
								myselfbottom.setVisibility(View.VISIBLE);
								break;
							case 2:

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
								img.setImageResource(R.drawable.sender_1);
								senderbottom.setVisibility(View.GONE);
								break;
							}
						}
						img.setPadding(254, 30, 0, 0);
//						img.setPadding(0, 0, 0, 0);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		// 如果是直接从相册获取
		case RESULT_OK:

			Bundle bundle = data.getExtras();
			uri = bundle.getString("uri");

			// 此处调用上传头像的方法，若成功则设置 头像
			if (ExerciseBookTool.isConnect(HomePageMainActivity.this)) {
				prodialog = new ProgressDialog(HomePageMainActivity.this);
				prodialog.setMessage("正在提交数据...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
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

	Thread thread = new Thread() {
		public void run() {
			try {

				MultipartEntity entity = new MultipartEntity();

				 entity.addPart("student_id", new StringBody(id));
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/1" + IMAGE_FILE_NAME);

				Log.i("suanfa", f.getPath() + "");
				if (f.exists()) {
					entity.addPart(
							"avatar",
							new FileBody(new File(Environment
									.getExternalStorageDirectory()
									+ "/1"
									+ IMAGE_FILE_NAME)));
				}

				// json = ExerciseBookTool.sendPhostimg(
				// Urlinterface.MODIFY_PERSON_INFO, entity);
				Log.i("suanfa", json);
				Message msg = new Message();// 创建Message 对象
				msg.what = 0;
				msg.obj = json;
				mHandler.sendMessage(msg);

			} catch (Exception e) {
				mHandler.sendEmptyMessage(7);
			}
		}
	};

}