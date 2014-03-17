package com.comdosoft.ExerciseBook.tools;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.comdosoft.ExerciseBook.pojo.ListeningPojo;
import com.comdosoft.ExerciseBook.pojo.QuestionPojo;

public class ExerciseBook extends Application {

	private int today_newer_id;
	private int MainItem;
	private int refresh;
	private boolean Mneu;
	private String uid;
	private String class_id;
	private String work_id;
	private List<ListeningPojo> list;
	private int user_id;
	private int menu_num;
	private List<Activity> activityList = new LinkedList<Activity>();
	private String work_end_dath;
	private List<QuestionPojo> question_list;
	private int question_id;
	private int branch_number;
	private int question_item;

	public ExerciseBook() {
		this.setMneu(true);
		this.setMainItem(0);
		this.setRefresh(1);
		this.setMenu_num(0);
		this.setQuestion_item(0);
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

	public String getWork_end_dath() {
		return work_end_dath;
	}

	public void setWork_end_dath(String work_end_dath) {
		this.work_end_dath = work_end_dath;
	}

	public List<QuestionPojo> getQuestion_list() {
		return question_list;
	}

	public void setQuestion_list(List<QuestionPojo> question_list) {
		this.question_list = question_list;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public int getBranch_number() {
		return branch_number;
	}

	public void setBranch_number(int branch_number) {
		this.branch_number = branch_number;
	}

	public int getQuestion_item() {
		return question_item;
	}

	public void setQuestion_item(int question_item) {
		this.question_item = question_item;
	}

	public void onCreate() {
		super.onCreate();
		// initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// context).threadPriority(Thread.NORM_PRIORITY - 2)
		// .denyCacheImageMultipleSizesInMemory()
		// .discCacheFileNameGenerator(new Md5FileNameGenerator())
		// .tasksProcessingOrder(QueueProcessingType.LIFO)
		// .writeDebugLogs() // Remove for release app
		// .build();
		// // Initialize ImageLoader with configuration.
		// ImageLoader.getInstance().init(config);
	}

}