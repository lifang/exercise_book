package com.comdosoft.ExerciseBook;

import java.io.File;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.HomePageMainActivity.mod_avatar;
import com.comdosoft.ExerciseBook.tools.CircularImage;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.comdosoft.ExerciseBook.R;

import cn.jpush.android.api.JPushInterface;
import com.comdosoft.ExerciseBook.R;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import android.widget.TextView;
import android.widget.Toast;

public class Table_TabHost extends Activity
{
	private LinearLayout middleLayout;
	private LinearLayout img_tab_now;
	private ExerciseBook eb;
	private String nickName = "丁作"; // 用户昵称
	TextView userName;//
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
			default:
				break;

			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.table);
		eb=(ExerciseBook) getApplication();
		memoryCache = new ImageMemoryCache(this);
		SharedPreferences preferences = getSharedPreferences(Urlinterface.SHARED,
				Context.MODE_PRIVATE);
		avatar_url = preferences.getString("avatar_url", "");
		nickName = preferences.getString("nickname", "");
		id = preferences.getString("id", null);
		instance=this;
		active=true;
		img_tab_now=(LinearLayout) findViewById(R.id.img_tab_now);
		img_tab_now.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(eb.isMneu())
				{
					
					Intent  intent = new Intent(Table_TabHost.this,LeftMenu.class);
					startActivity(intent);
					eb.setMneu(false);
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); 
				}
			}});
		middleLayout = (LinearLayout)findViewById(R.id.middle_layout);
		userName = (TextView) findViewById(R.id.user_name);
		userName.setText(nickName);

		userInfo = (LinearLayout) findViewById(R.id.user_button);
		faceImage = (CircularImage) findViewById(R.id.user_face);
		if (ExerciseBookTool.isConnect(getApplicationContext())) {
			if (avatar_url != null || avatar_url.length() != 0) { // 设置头像
//				ExerciseBookTool.set_background(Urlinterface.IP + avatar_url,
//						faceImage);
				String url = Urlinterface.IP + avatar_url;
				Bitmap result = memoryCache.getBitmapFromCache(url);
				if (result == null) {
					ExerciseBookTool.set_bk(url, faceImage, memoryCache);
				} else {
					faceImage.setImageDrawable(new BitmapDrawable(result));
				}
			}
		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
		}
		faceImage.setOnClickListener(listener);
		userInfo.setOnClickListener(listener2);
	}
	public void setContentView(int layoutId)
	{
		View middleView = getLayoutInflater().inflate(layoutId, null);
		if (null != middleLayout)
		{
			middleLayout.removeAllViews();
			middleLayout.addView(middleView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
				Toast.makeText(getApplicationContext(),
						"修改头像失败", 0).show();
				mHandler.sendEmptyMessage(7);
			}
		}
	}
	

}