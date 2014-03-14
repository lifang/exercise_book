package com.comdosoft.ExerciseBook.tools;

import java.util.List;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.comdosoft.ExerciseBook.pojo.ListeningPojo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ExerciseBook extends Application {

	private int today_newer_id;
	private int MainItem;
	private int refresh;
	private boolean Mneu;
	private String uid;
	private String class_id;
	private String work_id;
	private List<ListeningPojo> list;
	private int questions_index;
	private int branch_item_index;
	private int user_id;
	private int menu_num;
	private List<Activity> activityList = new LinkedList<Activity>();

	public ExerciseBook() {
		this.setMainItem(0);
		this.setRefresh(1);
		this.setQuestions_index(0);
		this.setBranch_item_index(0);
		this.setMenu_num(0);
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList() {
		this.activityList = new LinkedList<Activity>();
	}

	public int getMenu_num() {
		return menu_num;
	}

	public void setMenu_num(int menu_num) {
		this.menu_num = menu_num;
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

	public int getQuestions_index() {
		return questions_index;
	}

	public void setQuestions_index(int questions_index) {
		this.questions_index = questions_index;
	}

	public int getBranch_item_index() {
		return branch_item_index;
	}

	public void setBranch_item_index(int branch_item_index) {
		this.branch_item_index = branch_item_index;
	}

	public boolean isMneu() {
		return Mneu;
	}

	public void setMneu(boolean mneu) {
		Mneu = mneu;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getWork_id() {
		return work_id;
	}

	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}

	public List<ListeningPojo> getList() {
		return list;
	}

	public void setList(List<ListeningPojo> list) {
		this.list = list;
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