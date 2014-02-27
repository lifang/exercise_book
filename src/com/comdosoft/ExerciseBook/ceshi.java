package com.comdosoft.ExerciseBook;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;


@SuppressWarnings("deprecation")
public class ceshi extends TabActivity{
	private int screenWidth;			//屏幕宽度值
	private View content;				//主内容的布局
	private int leftEdge;				//menu最多可以滑到的左边缘	
	private int rightEdge=0;			//最多可以滑到的右边缘
	private boolean isMenuVisible=true;		//显示还是影藏
	private int menuPadding=400;	
	private LinearLayout.LayoutParams menuParams;		//menu布局的参数，更改leftMenu的值
	private ImageView img_tab_now;
	private View tabHostll;
	private View tabhostfl;
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 应用窗体显示状态操作该参数表示无标题  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉任务栏  
		setContentView(R.layout.ceshi);// 该语句必须在以上两句之后  
		initValues();
		Resources res = getResources(); // 得到资源对象  
		//步骤1：获得TabHost的对象，并进行初始化setup()  
		TabHost tabHost = getTabHost(); // 选项卡类似于java中的卡片布局  
		TabHost.TabSpec spec;  
		Intent intent;  
		intent = new Intent().setClass(this, HomeWorkIngActivity.class);// 启动音乐列表界面  
		// 步骤2：通过TabHost.TabSpec增加tab的一页，通过setContent()增加内容，通过setIndicator增加页的标签  
		spec = tabHost.newTabSpec("音乐")  
				.setIndicator("音乐", res.getDrawable(R.drawable.leftmenu_11))  
				.setContent(intent);  
		tabHost.addTab(spec);  

		intent = new Intent().setClass(this, HomePageMainActivity.class);  
		spec = tabHost.newTabSpec("艺术家")  
				.setIndicator("艺术家", res.getDrawable(R.drawable.leftmenu_12))  
				.setContent(intent);  
		tabHost.addTab(spec);  

		intent = new Intent().setClass(this, HomeWorkIngActivity.class);  
		spec = tabHost.newTabSpec("专辑")  
				.setIndicator("专辑", res.getDrawable(R.drawable.leftmenu_13))  
				.setContent(intent);  
		tabHost.addTab(spec);  
		intent = new Intent().setClass(this, HomeWorkIngActivity.class);  
		spec = tabHost.newTabSpec("最近播放")  
				.setIndicator("最近播放", res.getDrawable(R.drawable.leftmenu_14))  
				.setContent(intent);  
		tabHost.addTab(spec);  

		// 步骤3：可通过setCurrentTab(index)指定显示的页，从0开始计算。  
		TabWidget tabWidget = tabHost.getTabWidget();  
		for (int i = 0; i < tabWidget.getChildCount(); i++) {  
			View child = tabWidget.getChildAt(i);  
			LinearLayout.LayoutParams lp =(LayoutParams) child.getLayoutParams();   
			lp.width = LayoutParams.MATCH_PARENT;  
			lp.height = 80;  
			lp.weight = 0.0f;  
		}  
	} 
	public void initValues()
	{
		img_tab_now=(ImageView) findViewById(R.id.img_tab_now);
		WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
		screenWidth = window.getDefaultDisplay().getWidth();  
		tabHostll=(LinearLayout) findViewById(R.id.tall);
		tabhostfl=(LinearLayout) findViewById(R.id.tabfl);
		menuParams=(LinearLayout.LayoutParams) tabHostll.getLayoutParams();
//		menuPadding=screenWidth-tabHostll.getWidth();;
		
		Log.i("aa", "menuPadding:"+menuPadding);
		menuParams.width=screenWidth-menuPadding;
		Log.i("aa", menuParams.width+"<--menuParams");
		leftEdge=-menuParams.width;
		menuParams.leftMargin=leftEdge;
		tabhostfl.getLayoutParams().width=screenWidth;
		img_tab_now.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v) {
				if(isMenuVisible)
				{
					scrollToMenu();
					isMenuVisible=false;
				}
				else
				{
					scrollToContent();
					isMenuVisible=true;
				}
			}

		});

	}
	/** 
	 * 将屏幕滚动到选项卡界面，滚动速度设定为-30. 
	 */  
	private void scrollToMenu() {  
		new ScrollTask().execute(30);  
	}  

	/** 
	 * 将屏幕滚动到内容界面，滚动速度设定为-30. 
	 */  
	private void scrollToContent() {  
		new ScrollTask().execute(-30);  
	}  

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {  

		@Override  
		protected Integer doInBackground(Integer... speed) {  
			int leftMargin = menuParams.leftMargin;  
			// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。  
			while (true) {  
				leftMargin = leftMargin + speed[0];  
				if (leftMargin > rightEdge) {  
					leftMargin = rightEdge;  
					break;  
				}  
				if (leftMargin < leftEdge) {  
					leftMargin = leftEdge;  
					break;  
				}  
				publishProgress(leftMargin);  
				Log.i("aa",menuPadding +"<---menuPadding ");
				// 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。  
				sleep(20);  
			}  
			return leftMargin;  
		}  

		protected void onProgressUpdate(Integer... leftMargin) {  
			menuParams.leftMargin = leftMargin[0];  
			tabHostll.setLayoutParams(menuParams);  
		}  

		protected void onPostExecute(Integer leftMargin) {  
			menuParams.leftMargin = leftMargin;  
			tabHostll.setLayoutParams(menuParams);  
		}  
	}  

	/** 
	 * 使当前线程睡眠指定的毫秒数。 
	 *  
	 * @param millis 
	 *            指定当前线程睡眠多久，以毫秒为单位 
	 */  
	private void sleep(long millis) {  
		try {  
			Thread.sleep(millis);  
		} catch (InterruptedException e) {  
			e.printStackTrace();  
		}  
	}  
}  

