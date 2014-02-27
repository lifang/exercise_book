package com.comdosoft.ExerciseBook;


import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class BooksTable  extends Activity
{
	 private LinearLayout middleLayout;
	    
	    protected void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);// 应用窗体显示状态操作该参数表示无标题  
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉任务栏  
	        super.setContentView(R.layout.table);
	        middleLayout = (LinearLayout)findViewById(R.id.middle_layout);
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
