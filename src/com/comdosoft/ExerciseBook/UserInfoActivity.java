package com.comdosoft.ExerciseBook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UserInfoActivity extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	private ImageView imageView1;
	private ImageView imageView2;
	public static UserInfoActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.userinfo);
		instance = this;
		imageView1=(ImageView) findViewById(R.id.imageView1);//  修改用户名图标
		imageView1.setOnClickListener(listener);
		imageView2=(ImageView) findViewById(R.id.imageView2);//  切换班级图标
		imageView2.setOnClickListener(listener2);
		layout=(LinearLayout)findViewById(R.id.main_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "点击别的地方消失", 
						Toast.LENGTH_SHORT).show();	
			}
		});
	}
	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
//			Intent intentp = new Intent();
//			intentp.setClass(UserInfoActivity.this, SettingPhoto.class);//
//			startActivityForResult(intentp, 0);
		}
	};
	private View.OnClickListener listener2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intentp = new Intent();
			intentp.setClass(UserInfoActivity.this, SwitchClassActivity.class);//
			startActivity(intentp);
		}
	};
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 5:
//			Bundle bundle = data.getExtras();
//			String content = bundle.getString("content");
//			setButton_huifu(content);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
