package com.comdosoft.ExerciseBook.tools;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ExerciseBook extends Application {

	private int today_newer_id;
	private int MainItem;
	private int refresh;
	private boolean Mneu;

	public ExerciseBook() {
		this.setMainItem(0);
		this.setRefresh(1);
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	public int getMainItem() {
		return MainItem;
	}

	public void setMainItem(int mainItem) {
		MainItem = mainItem;
	}

	public int getToday_newer_id() {
		return today_newer_id;
	}

	public void setToday_newer_id(int today_newer_id) {
		this.today_newer_id = today_newer_id;
	}

	public boolean isMneu() {
		return Mneu;
	}

	public void setMneu(boolean mneu) {
		Mneu = mneu;
	}

	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}