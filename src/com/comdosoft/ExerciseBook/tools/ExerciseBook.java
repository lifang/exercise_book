package com.comdosoft.ExerciseBook.tools;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ExerciseBook extends Application {
<<<<<<< HEAD
	private int Question_allNumber;
	//	private List<QuestionPojo> branch_questions;
	//	private List<ListeningPojo> question_list;
	private int question_id;
	private int question_index;
	private List<List<String>> question_history;
	private int p_q_package_id;
	private int q_package_id;
	private boolean work_history;// 查看历史开关
	private int mainItem;
	private int user_id;
	private int class_id;
	private int message_id;
	private String noselect_message;
	private int newCount;
	private boolean newsFlag;
	private int lastcount;
	private int hw_number;
	private int work_date_item;
	private int history_item;
	private int menu_count=0;
	private int menu_num;
	private boolean MessageChoice;
	public int getMenu_num() {
		return menu_num;
	}
	public void setMenu_num(int menu_num) {
		this.menu_num = menu_num;
	}

	public boolean isMessageChoice() {
		return MessageChoice;
	}
	public void setMessageChoice(boolean messageChoice) {
		MessageChoice = messageChoice;
	}
	public int getMenu_count() {
		return menu_count;
	}

=======
>>>>>>> c385b6f7e2d36790129952b8972f3fb33b16c7fe

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

<<<<<<< HEAD

	public void setLastcount(int lastcount) {
		this.lastcount = lastcount;
	}

	public boolean isNewsFlag() {
		return newsFlag;
	}

	public void setNewsFlag(boolean newsFlag) {
		this.newsFlag = newsFlag;
	}

	public ExerciseBook() {
		this.menu_num=0;
		this.setMessageChoice(true);
		this.setMneu(true);
		this.setRefresh(1);
		this.setNewsFlag(true);
		this.setNewCount(0);
		this.setQuestion_index(0);
		this.setHistory_item(0);
		this.setMainItem(0);
		this.setMessage_id(-1);
		this.setWork_date_item(0);
	}

	public int getNewCount() {
		return newCount;
	}

	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}

	public String getNoselect_message() {
		return noselect_message;
	}

	public void setNoselect_message(String noselect_message) {
		this.noselect_message = noselect_message;
	}

	public int getQuestion_index() {
		return question_index;
	}

	public void setQuestion_index(int question_index) {
		this.question_index = question_index;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	//	public List<QuestionPojo> getBranch_questions() {
	//		return branch_questions;
	//	}
	//
	//	public void setBranch_questions(List<QuestionPojo> branch_questions) {
	//		this.branch_questions = branch_questions;
	//	}

	public int getQuestion_allNumber() {
		return Question_allNumber;
	}

	public void setQuestion_allNumber(int question_allNumber) {
		Question_allNumber = question_allNumber;
	}

	//	public List<ListeningPojo> getQuestion_list() {
	//		return question_list;
	//	}
	//
	//	public void setQuestion_list(List<ListeningPojo> question_list) {
	//		this.question_list = question_list;
	//	}

	public List<List<String>> getQuestion_history() {
		return question_history;
	}

	public void setQuestion_history(List<List<String>> question_history) {
		this.question_history = question_history;
	}

	public boolean isWork_history() {
		return work_history;
	}

	public void setWork_history(boolean work_history) {
		this.work_history = work_history;
	}

=======
>>>>>>> c385b6f7e2d36790129952b8972f3fb33b16c7fe
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