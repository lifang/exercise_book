package com.comdosoft.ExerciseBook.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;

import com.comdosoft.ExerciseBook.pojo.ListeningPojo;
import com.comdosoft.ExerciseBook.pojo.QuestionPojo;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
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
	private int user_id;
	private int menu_num;
	private List<Activity> activityList = new LinkedList<Activity>();
	private String work_end_dath;
	private List<QuestionPojo> question_list;
	private int question_id;
	private int branch_number;
	private int question_item;
	private boolean history_type;
	private int propTime_Number;
	private String path;
	private int activity_item;
	private int work_number;
	private List<knowledges_card> Allmap;
	public List<tags> tagsList;
	public List<Integer> Tagsarr;
	private int Time_number;
	private int True_number;
	Map<Integer, List<View>> FontCard;
	private int usertime;
	ImageMemoryCache memoryCache;
	private boolean Update;

	public Map<Integer, List<View>> getFontCard() {
		return FontCard;
	}

	public void setFontCard(Map<Integer, List<View>> fontCard) {
		FontCard = new HashMap<Integer, List<View>>();
		FontCard = fontCard;
	}

	public ExerciseBook() {
		this.setMneu(true);
		this.setMainItem(0);
		this.setRefresh(1);
		this.setMenu_num(1);
		this.setQuestion_item(0);
		this.setMneu(true);
		this.setHistory_type(false);
		this.setPropTime_Number(0);
		this.setUsertime(0);
		this.setUpdate(true);
	}

	public int getWork_number() {
		return work_number;
	}

	public void setWork_number(int work_number) {
		this.work_number = work_number;
	}

	public int getActivity_item() {
		return activity_item;
	}

	public void setActivity_item(int activity_item) {
		this.activity_item = activity_item;
	}

	public List<knowledges_card> getAllmap() {
		return Allmap;
	}

	public void setAllmap(List<knowledges_card> allmap) {
		Allmap = allmap;
	}

	public List<tags> getTagsList() {
		return tagsList;
	}

	public void setTagsList(List<tags> tagsList0) {
		tagsList = new ArrayList<tags>();
		this.tagsList = tagsList0;
	}

	public List<Integer> getTagsarr() {
		return Tagsarr;
	}

	public void setTagsarr(List<Integer> tagsarr) {
		Tagsarr = tagsarr;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public boolean isHistory_type() {
		return history_type;
	}

	public void setHistory_type(boolean history_type) {
		this.history_type = history_type;
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

	public int getPropTime_Number() {
		return propTime_Number;
	}

	public void setPropTime_Number(int propTime_Number) {
		this.propTime_Number = propTime_Number;
	}

	public int getTime_number() {
		return Time_number;
	}

	public void setTime_number(int time_number) {
		Time_number = time_number;
	}

	public int getTrue_number() {
		return True_number;
	}

	public void setTrue_number(int true_number) {
		True_number = true_number;
	}

	public int getUsertime() {
		return usertime;
	}

	public void setUsertime(int usertime) {
		this.usertime = usertime;
	}

	public ImageMemoryCache getMemoryCache() {
		return memoryCache;
	}

	public void setMemoryCache(ImageMemoryCache memoryCache) {
		this.memoryCache = memoryCache;
	}

	public boolean isUpdate() {
		return Update;
	}

	public void setUpdate(boolean update) {
		Update = update;
	}

	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		memoryCache = new ImageMemoryCache(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
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