package com.comdosoft.ExerciseBook;

import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
public class Table_TabHost extends Activity
{
	private LinearLayout middleLayout;
	private ImageView img_tab_now;
	private ExerciseBook eb;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.table);
		eb=(ExerciseBook) getApplication();
		img_tab_now=(ImageView) findViewById(R.id.img_tab_now);
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
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(getApplicationContext());     
		JPushInterface.setAlias(getApplicationContext(), "a123", null);
	}
	@Override
	protected void onResume() {
		super.onResume();
//		JPushInterface.onResume(getApplicationContext());
	}
	@Override
	protected void onPause() {
		super.onPause();
//		JPushInterface.onPause(getApplicationContext());
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

}