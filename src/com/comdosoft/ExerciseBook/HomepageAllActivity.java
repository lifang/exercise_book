package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.pojo.Child_Micropost;
import com.comdosoft.ExerciseBook.pojo.Micropost;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView.OnFooterRefreshListener;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView.OnHeaderRefreshListener;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

/**
 * @作者 丁作强
 * @时间 2014-4-17 下午4:18:16
 */
public class HomepageAllActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener, Urlinterface,
		OnGestureListener {
	private ExerciseBook exerciseBook;
	private String user_id = "130"; // 学生 user_id
	private String id = "73";// 学生student_id，
	private String school_class_id = "83"; // 班级 id
	private ProgressDialog prodialog;
	// -------------------------------------------------------------------
	private String json = "{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\",\"student\":{\"id\":66,\"name\":\"hrueieurh \",\"user_id\":66,\"nickname\":\"yeueieiri \",\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\"},\"class\":{\"id\":1,\"name\":\"1401\",\"tearcher_name\":\"fgf\",\"tearcher_id\":1},\"classmates\":[{\"avatar_url\":\"/assets/default_avater.jpg\",\"id\":1,\"name\":\"nan\",\"nickname\":\"zxn\"}],\"task_messages\":[],\"microposts\":{\"page\":1,\"pages_count\":4,\"details_microposts\":[{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"ww\u6211\u6d4b\u5b89\u5fbdi\u2026\u2026\n\n\u6d4b\u8bd5\u4e00\u4e0b\",\"created_at\":\"2014-01-27T14:12:02+08:00\",\"micropost_id\":145,\"name\":\"hrueieurh \",\"reply_microposts_count\":1,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"556355265335 \",\"created_at\":\"2014-01-23T07:17:25+08:00\",\"micropost_id\":104,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"Babbitt \",\"created_at\":\"2014-01-23T02:58:48+08:00\",\"micropost_id\":103,\"name\":\"hrueieurh \",\"reply_microposts_count\":4,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u5173\u4e8e\u52a0\u5f3a\",\"created_at\":\"2014-01-22T10:11:11+08:00\",\"micropost_id\":99,\"name\":\"???\",\"reply_microposts_count\":6,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"\u7684\u7684\u7684\",\"created_at\":\"2014-01-22T09:47:22+08:00\",\"micropost_id\":97,\"name\":\"hrueieurh \",\"reply_microposts_count\":5,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"\u5927\u5927\u65b9\u65b9\u53d1\",\"created_at\":\"2014-01-22T09:47:12+08:00\",\"micropost_id\":96,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"hjk\",\"created_at\":\"2014-01-22T06:43:12+08:00\",\"micropost_id\":95,\"name\":\"???\",\"reply_microposts_count\":19,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"ffg\",\"created_at\":\"2014-01-22T02:57:52+08:00\",\"micropost_id\":94,\"name\":\"???\",\"reply_microposts_count\":6,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"UI o\",\"created_at\":\"2014-01-21T10:48:36+08:00\",\"micropost_id\":93,\"name\":\"???\",\"reply_microposts_count\":2,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"rrrrr\",\"created_at\":\"2014-01-21T10:13:22+08:00\",\"micropost_id\":86,\"name\":\"hrueieurh \",\"reply_microposts_count\":16,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"rrrrr\",\"created_at\":\"2014-01-21T10:13:21+08:00\",\"micropost_id\":85,\"name\":\"hrueieurh \",\"reply_microposts_count\":7,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"dffff\",\"created_at\":\"2014-01-21T10:13:19+08:00\",\"micropost_id\":84,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u5feb\u4e86\uff0c\u5feb\u4e86\u5feb\u4e86\",\"created_at\":\"2014-01-21T06:46:09+08:00\",\"micropost_id\":73,\"name\":\"??\",\"reply_microposts_count\":0,\"user_id\":8,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"dddddd\",\"created_at\":\"2014-01-20T06:24:07+08:00\",\"micropost_id\":72,\"name\":\"???\",\"reply_microposts_count\":3,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"ggjjkkjhghhjjj\",\"created_at\":\"2014-01-20T04:41:44+08:00\",\"micropost_id\":71,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u98ce\u98ce\u5149\u5149\u97e9\u56fd\u521a\u521a\u597d\u53d1\u98ce\u98ce\u5149\u5149\u5730\u65b9\u98ce\u683c\u56de\u5bb6\u98ce\u683c\u54c8\u54c8\u54c8\u98ce\u683c\u5475\u5475\u7684\u611f\u89c9\u7684\u98ce\u683c\u4e2a\",\"created_at\":\"2014-01-20T02:52:47+08:00\",\"micropost_id\":70,\"name\":\"???\",\"reply_microposts_count\":1,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u89c4\u5212\u5c40\",\"created_at\":\"2014-01-18T10:57:36+08:00\",\"micropost_id\":69,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u4f1a\",\"created_at\":\"2014-01-18T10:41:09+08:00\",\"micropost_id\":68,\"name\":\"???\",\"reply_microposts_count\":1,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u4e2a\u770b\u8fc7\",\"created_at\":\"2014-01-18T10:40:54+08:00\",\"micropost_id\":67,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"yyuu\",\"created_at\":\"2014-01-18T10:33:45+08:00\",\"micropost_id\":66,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1}]},\"daily_tasks\":[{\"id\":2,\"name\":\"2014-1-20\u4f5c\u4e1a\",\"start_time\":\"2014-01-20T00:00:00+08:00\",\"end_time\":\"2014-01-30T00:00:00+08:00\",\"question_packages_url\":\"/question_package_1.js\",\"listening_schedule\":\"1/4\",\"reading_schedule\":\"0/4\"}],\"follow_microposts_id\":[86,97,97,96,95,93,71,93,71,99],\"messages\":[{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;jjj\",\"created_at\":\"2014-01-26T12:32:23+08:00\",\"id\":554,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;uui\",\"created_at\":\"2014-01-26T12:32:07+08:00\",\"id\":553,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;huu\",\"created_at\":\"2014-01-26T12:31:59+08:00\",\"id\":552,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;jjii\",\"created_at\":\"2014-01-26T12:30:43+08:00\",\"id\":550,\"micropost_id\":85,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u5173\u6ce8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:38+08:00\",\"id\":208,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:30+08:00\",\"id\":189,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u5173\u6ce8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:30+08:00\",\"id\":198,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;a\",\"created_at\":\"2014-01-23T03:04:00+08:00\",\"id\":187,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;CA\",\"created_at\":\"2014-01-23T03:03:46+08:00\",\"id\":186,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ding\",\"created_at\":\"2014-01-23T03:01:09+08:00\",\"id\":185,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;@\",\"created_at\":\"2014-01-23T03:00:51+08:00\",\"id\":184,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:00:29+08:00\",\"id\":183,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:00:29+08:00\",\"id\":182,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;abc\",\"created_at\":\"2014-01-23T02:07:00+08:00\",\"id\":180,\"micropost_id\":99,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;''''\",\"created_at\":\"2014-01-23T02:05:37+08:00\",\"id\":175,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u54c8\u54c8\u54c8\u5c31\",\"created_at\":\"2014-01-22T10:09:41+08:00\",\"id\":163,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u53cd\u5f39\u6709\",\"created_at\":\"2014-01-22T10:09:22+08:00\",\"id\":162,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u89c4\u5212\",\"created_at\":\"2014-01-22T10:09:15+08:00\",\"id\":161,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:41+08:00\",\"id\":159,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:40+08:00\",\"id\":157,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:39+08:00\",\"id\":153,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:34+08:00\",\"id\":144,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:32+08:00\",\"id\":135,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:30+08:00\",\"id\":126,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:28+08:00\",\"id\":117,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66}]}";
	public PullToRefreshView mPullToRefreshView;
	public LinearLayout Linear_layout;
	private int child_page = 1; // 子消息 分页加载的 第几页
	private int child_pages_count;// 子消息总页数
	private int page = 1;// 当前第几页
	private int pages_count = 1;// 总页数
	private String reciver_id = "";
	private String reciver_types = "";
	private String micropost_id = "";
	private int list_item;// list集合的最后一位索引
	private int user_types = 1; // 用户类型 学生 默认为 1
	public List<Boolean> gk_list;// 主消息 点击操作 开关集合
	public List<Boolean> reply_gk_list;// 回复 点击操作 开关集合
	public List<RelativeLayout> item_huifu;// 回复开关集合
	private List<Micropost> list; // 主消息 集合
	private ArrayList<Child_Micropost> child_list; // 子消息 集合
	private List<String> care;
	private List<ImageView> guanzhu_list;// 关注按钮 集合
	private List<TextView> guanzhu_count_list;// 关注数 集合
	private List<TextView> huifu_count_list;// 回复数 集合
	private List<HorizontalScrollView> HorizontalScrollView_list;// 主消息 滑动块 集合

	private List<ListView> list_list; //

	private List<ZiAdapter> ziAdapter_list;
	private String avatar_url;
	private String user_name;
	private String nick_name;
	private int focus = -1;

	private static ImageMemoryCache memoryCache; // 图片 缓存

	private GestureDetector gd; // 手势监听
	// 事件状态
	private final char FLING_CLICK = 0;
	private final char FLING_LEFT = 1;
	private final char FLING_RIGHT = 2;
	private char flingState = FLING_CLICK;
	public static HomepageAllActivity instance = null;
	int errS = 0;
	private Handler handler = new Handler() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				final String json = (String) msg.obj;
				setJson(json);
				if (errS==1) {
					init();
				}
				
				break;
			case 2:
				focus = -1;
				list.clear();
				click_list();
				final String json_all = (String) msg.obj;
				parseJson_all(json_all);
				for (int i = 0; i < list.size(); i++) {
					setlayout(i);
				}
				mPullToRefreshView.onHeaderRefreshComplete();
				break;
			case 3:
				final String json_all2 = (String) msg.obj;

				parseJson_all(json_all2);

				for (int i = list_item; i < list.size(); i++) {
					setlayout(i);
				}
				mPullToRefreshView.onFooterRefreshComplete();
				break;
			case 4:
				prodialog.dismiss();
				break;

			case 7:
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_middle);
		exerciseBook = (ExerciseBook) getApplication();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		instance = this;
		gd = new GestureDetector(this);
		// memoryCache = HomePageMainActivity.instance.memoryCache;
		memoryCache = exerciseBook.getMemoryCache();
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
//		exerciseBook.setMenu_num(3);
		user_id = preferences.getString("user_id", "130");
		id = preferences.getString("id", "73");
		school_class_id = preferences.getString("school_class_id", "83");

		initialize(); // 初始化参数
		page = 1;
		// if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
		// prodialog = new ProgressDialog(HomepageAllActivity.this);
		// prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
		// prodialog.setCanceledOnTouchOutside(false);
		// prodialog.show();
		// Thread thread = new Thread(new get_class_info());
		// thread.start();
		//
		// } else {
		// handler.sendEmptyMessage(7);
		// }
	}

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		// page = 1;
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);

		user_id = preferences.getString("user_id", "130");
		id = preferences.getString("id", "73");
		school_class_id = preferences.getString("school_class_id", "83");
		int refresh = exerciseBook.getRefresh();
		if (refresh == 1) {
			if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
				prodialog = new ProgressDialog(HomepageAllActivity.this);
				prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				Thread thread = new Thread(new get_class_info());
				thread.start();

			} else {
				handler.sendEmptyMessage(7);
			}
		}
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public void init() {
		item_huifu = new ArrayList<RelativeLayout>();

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		Linear_layout = (LinearLayout) findViewById(R.id.layout);
		click_list();
		Log.i("aaa", list.size() + "====");
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				setlayout(i);
			}
		} else {
			Toast.makeText(getApplicationContext(), "暂无记录", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * 动态加载
	 * 
	 * */
	public void setlayout(final int i) {

		final Micropost mess = list.get(i);
		ZiAdapter Adapter = new ZiAdapter();
		ziAdapter_list.add(Adapter);
		final View convertView = LayoutInflater.from(HomepageAllActivity.this)
				.inflate(R.layout.class_layout, null);
		final HorizontalScrollView hSView = (HorizontalScrollView) convertView
				.findViewById(R.id.hsv);
		final View action = convertView.findViewById(R.id.ll_action);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		final RelativeLayout re = (RelativeLayout) convertView
				.findViewById(R.id.user_left);
		LayoutParams lp = re.getLayoutParams();
		lp.width = dm.widthPixels;
		final ImageView face = (ImageView) convertView
				.findViewById(R.id.user_face); // 头像
		ImageView huifu_img = (ImageView) convertView
				.findViewById(R.id.huifu_item); // 回复图标
		TextView Micropost_senderName = (TextView) convertView
				.findViewById(R.id.message_senderName); // 谁发的
		TextView guanzhu_count = (TextView) convertView
				.findViewById(R.id.guanzhu_count); // 关注数
		TextView huifu_count = (TextView) convertView
				.findViewById(R.id.huifu_count); // 回复数
		RelativeLayout button1 = (RelativeLayout) convertView
				.findViewById(R.id.shanchu_button); // 删除按钮
		RelativeLayout button2 = (RelativeLayout) convertView
				.findViewById(R.id.guanzhu_button); // 关注按钮
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.guanzhu_button2); // 关注按钮
		RelativeLayout button3 = (RelativeLayout) convertView
				.findViewById(R.id.huifu_button); // 回复按钮
		TextView Micropost_content = (TextView) convertView
				.findViewById(R.id.micropost_content); // 消息内容
		TextView Micropost_date = (TextView) convertView
				.findViewById(R.id.micropost_date); // 日期
		final ListView listView2 = (ListView) convertView// 子消息的list
				.findViewById(R.id.aa);//
		list_list.add(listView2);
		guanzhu_list.add(imageView);
		guanzhu_count_list.add(guanzhu_count);
		huifu_count_list.add(huifu_count);
		gk_list.add(true);
		HorizontalScrollView_list.add(hSView);
		final Button lookMore = (Button) convertView
				.findViewById(R.id.lookMore); // 查看更多
		lookMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setlookMore(listView2, mess);
			}
		});

		guanzhu_count.setText(mess.getCareCount()); // 关注数
		huifu_count.setText(mess.getReply_microposts_count()); // 回复数
		Log.i("linshi", IP + mess.getAvatar_url());
		// 设置头像
		if (mess.getAvatar_url().equals("")
				|| mess.getAvatar_url().equals("null")) {
		} else {
			String url = IP + mess.getAvatar_url();
			// ExerciseBookTool.set_background(url, face);
			Bitmap result = memoryCache.getBitmapFromCache(url);
			if (result == null) {
				Log.i("aa", " 网络网络" + i);
				ExerciseBookTool.set_bk(url, face, memoryCache);
			} else {

				Log.i("aa", " 缓存缓存缓存缓存" + i);
				face.setImageDrawable(new BitmapDrawable(result));
			}
		}

		Micropost_senderName.setText(mess.getName()); // 发消息的人
		Micropost_content.setText(mess.getContent()); // 消息内容
		Micropost_date.setText(ExerciseBookTool.divisionTime2(mess
				.getCreated_at())); // 消息日期
		String mic_id = mess.getId();
		for (int j = 0; j < care.size(); j++) {
			String a = (String) care.get(j);
			if (a.equals(mic_id)) {
				imageView.setBackgroundResource(R.drawable.homepage_guanzhu2);
			}
		}
		// 回复
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reciver_id = mess.getUser_id();
				reciver_types = mess.getUser_types();
				Intent intentp = new Intent();
				intentp.setClass(HomepageAllActivity.this,
						com.comdosoft.ExerciseBook.tools.OpenInputMethod.class);//
				startActivityForResult(intentp, 0);
			}
		});
		/**
		 * 关注
		 */
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				add_concern(i, mess);
			}
		});
		/**
		 * 删除
		 */
		if (user_id.equals(mess.getUser_id())) { // 主消息删除按钮 只是在本人时显示
			button1.setVisibility(View.VISIBLE);
			button2.setVisibility(View.GONE);
		} else {
			button1.setVisibility(View.GONE);
			button2.setVisibility(View.VISIBLE);
		}
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Dialog dialog = new AlertDialog.Builder(
						HomepageAllActivity.this)
						.setTitle("提示")
						.setMessage("您确认要删除么?")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										del_micropost(i, mess);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create();
				dialog.show();
			}
		});
		final RelativeLayout layout1 = (RelativeLayout) convertView
				.findViewById(R.id.child_micropost); // 回复界面
		item_huifu.add(layout1);

		// 设置监听事件
		hSView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:

					switch (flingState) {
					case FLING_LEFT:
					case FLING_RIGHT:
					case FLING_CLICK:
						micropost_id = mess.getId();//
						focus = i;
						if (gk_list.get(i) == true) {
							hSView.smoothScrollTo(action.getWidth(), 0);
							for (int j = 0; j < gk_list.size(); j++) {
								if (j != i) {
									HorizontalScrollView_list.get(j)
											.smoothScrollTo(0, 0);
								}
							}
						} else {
							hSView.smoothScrollTo(0, 0);
						}
						setHuiFu(i, mess, layout1, listView2, lookMore);
						break;
					}
				}
				return false;
			}
		});
		if (i % 2 == 0) {
			re.setBackgroundResource(R.color.before_click);// 背景白色
		} else {
			re.setBackgroundResource(R.color.after_click);// 背景灰色
		}
		Linear_layout.addView(convertView);
	}

	/*
	 * 上拉加载更多
	 */
	public void onFooterRefresh(PullToRefreshView view) {
		page = page + 1;
		if (page <= pages_count) {
			list_item = list.size();
			Thread thread = new Thread() {
				public void run() {// 全部 页面加载 更多

					Map<String, String> map = new HashMap<String, String>();
					map.put("student_id", id);
					map.put("school_class_id", school_class_id);
					map.put("page", page + "");
					String result = "";
					try {
						result = ExerciseBookTool.sendGETRequest(
								Urlinterface.GET_MICROPOSTS, map);
					} catch (Exception e1) {
						mPullToRefreshView.onFooterRefreshComplete();
						handler.sendEmptyMessage(7);
					}
					Message msg = new Message();// 创建Message 对象
					msg.what = 3;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			};

			if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
				thread.start();
			} else {
				mPullToRefreshView.onFooterRefreshComplete();
				handler.sendEmptyMessage(7);
			}
		} else {
			Toast.makeText(getApplicationContext(), "已经是最后一页了..",
					Toast.LENGTH_SHORT).show();
			mPullToRefreshView.onFooterRefreshComplete();
		}
	}

	/*
	 * 下拉刷新
	 */
	public void onHeaderRefresh(PullToRefreshView view) {
		if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
			shuaxin();
		} else {

			handler.sendEmptyMessage(7);
			mPullToRefreshView.onHeaderRefreshComplete();
		}

	}

	/*
	 * 子消息 适配器
	 */
	public class ZiAdapter extends BaseAdapter {

		public int getCount() {
			return child_list.size();// 数据总数
		}

		public Object getItem(int position2) {
			return child_list.get(position2);
		}

		public long getItemId(int position2) {
			return position2;
		}

		public View getView(final int position2, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = HomepageAllActivity.this
					.getLayoutInflater();
			View child_view = inflater.inflate(R.layout.child_micropost_item,
					null);
			final HorizontalScrollView hSView2 = (HorizontalScrollView) child_view
					.findViewById(R.id.hsv2);
			final View action2 = child_view.findViewById(R.id.ll_action2);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			final RelativeLayout re = (RelativeLayout) child_view
					.findViewById(R.id.child_user_left);
			LayoutParams lp = re.getLayoutParams();
			lp.width = dm.widthPixels;
			// 设置监听事件
			hSView2.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:

						switch (flingState) {
						case FLING_LEFT:
						case FLING_RIGHT:
							// 处理点击事件
						case FLING_CLICK:

							if (reply_gk_list.get(position2) == true) {

								hSView2.smoothScrollTo(action2.getWidth(), 0);
								reply_gk_list.set(position2, false);
								for (int j = 0; j < reply_gk_list.size(); j++) {
									if (j != position2) {
										reply_gk_list.set(j, true);
										((HorizontalScrollView) list_list
												.get(focus).getChildAt(j)
												.findViewById(R.id.hsv2))
												.smoothScrollTo(0, 0);
									}
								}
							} else {
								reply_gk_list.set(position2, true);
								hSView2.smoothScrollTo(0, 0);
							}
							break;
						}
					}
					return false;
				}
			});

			ImageView face = (ImageView) child_view
					.findViewById(R.id.child_user_face); // 头像
			TextView Micropost_who = (TextView) child_view
					.findViewById(R.id.child_message_senderName); // 回复人 张三
			TextView Micropost_ToWho = (TextView) child_view
					.findViewById(R.id.child_message_reciverName); // 接收人 李四
			TextView Micropost_date = (TextView) child_view
					.findViewById(R.id.child_message_date); // 时间
			TextView Micropost_content = (TextView) child_view
					.findViewById(R.id.child_micropost_content); // 内容
			ImageView delete = (ImageView) child_view
					.findViewById(R.id.child_micropost_delete); // 删除
			ImageView reply = (ImageView) child_view
					.findViewById(R.id.child_micropost_huifu); // 回复
			ImageView good = (ImageView) child_view
					.findViewById(R.id.good_item); // 点赞
			final Child_Micropost child_Micropost = child_list.get(position2);
			if (child_Micropost.getGood().equals("1")) {
				good.setVisibility(View.VISIBLE);
			} else {
				good.setVisibility(View.GONE);
			}

			if (position2 != 0) {
				ImageView child_bottom = (ImageView) child_view
						.findViewById(R.id.child_jiantou);
				child_bottom.setVisibility(View.GONE);
			}

			if (child_Micropost.getSender_avatar_url().length() > 4) { // 设置头像
				// ExerciseBookTool.set_background(
				// IP + child_Micropost.getSender_avatar_url(), face);
				String url = IP + child_Micropost.getSender_avatar_url();
				// ExerciseBookTool.set_background(url, face);

				Bitmap result = memoryCache.getBitmapFromCache(url);
				if (result == null) {
					Log.i("aa", " 适配器    网络网络" + position2);
					ExerciseBookTool.set_bk(url, face, memoryCache);
				} else {

					Log.i("aa", " 适配器   缓存缓存缓存缓存" + position2);
					face.setImageDrawable(new BitmapDrawable(result));
				}
			}
			Micropost_who.setText(child_Micropost.getSender_name()); // 回复人
			Micropost_ToWho.setText(child_Micropost.getReciver_name()); // 接收人
			Micropost_date.setText(ExerciseBookTool
					.divisionTime2(child_Micropost.getCreated_at())); // 时间
			Micropost_content.setText(child_Micropost.getContent()); // 消息内容
			if (user_id.equals(child_Micropost.getSender_id())
					|| user_id.equals(list.get(focus).getUser_id())) {// 自己回复的帖子现实删除按钮
				delete.setVisibility(View.VISIBLE);
			}
			delete.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					final int item = position2;
					Log.i("linshi", item + "--------------");
					final Handler mHandler = new Handler() {
						public void handleMessage(android.os.Message msg) {
							switch (msg.what) {
							case 0:
								final String json8 = (String) msg.obj;
								if (json8.length() != 0) {
									JSONObject array;
									try {
										array = new JSONObject(json8);//
										String status = array
												.getString("status");
										String notice = array
												.getString("notice");
										if ("success".equals(status)) {
											int a = Integer
													.parseInt(list
															.get(focus)
															.getReply_microposts_count()) - 1;
											huifu_count_list.get(focus)
													.setText(a + "");
											list.get(focus)
													.setReply_microposts_count(
															a + "");
											child_list.remove(item);
											reply_gk_list.remove(item);
											// ziAdapter_list.get(focus)
											// .notifyDataSetChanged();
											ExerciseBookTool
													.setListViewHeightBasedOnChildren(list_list
															.get(focus));
										}
										Toast.makeText(getApplicationContext(),
												notice, Toast.LENGTH_SHORT)
												.show();
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								break;
							default:
								break;
							}
						}
					};
					final Thread thread = new Thread() {
						public void run() {
							try {
								Map<String, String> map = new HashMap<String, String>();
								map.put("reply_micropost_id",
										child_Micropost.getId());
								String child_delete_json = ExerciseBookTool
										.doPost(Urlinterface.DELETE_REPLY_POSTS,
												map);
								Message msg = new Message();// 创建Message 对象
								msg.what = 0;
								msg.obj = child_delete_json;
								mHandler.sendMessage(msg);
							} catch (Exception e) {
								handler.sendEmptyMessage(7);
							}
						}
					};

					Dialog dialog = new AlertDialog.Builder(
							HomepageAllActivity.this)
							.setTitle("提示")
							.setMessage("您确认要删除么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (ExerciseBookTool
													.isConnect(HomepageAllActivity.this)) {
												thread.start();
											} else {
												handler.sendEmptyMessage(7);
											}
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).create();
					dialog.show();
				}
			});

			reply.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					reciver_id = child_Micropost.getSender_id();
					reciver_types = child_Micropost.getSender_types();
					Intent intentp = new Intent();
					intentp.setClass(
							HomepageAllActivity.this,
							com.comdosoft.ExerciseBook.tools.OpenInputMethod.class);//
					startActivityForResult(intentp, 0);

				}
			});
			return child_view;
		}
	}

	/*
	 * 解析 get_class_info 方法返回的 json字符串
	 */
	/**
	 * @param json
	 */
	private void setJson(String json) {
		try {
			JSONObject obj = new JSONObject(json);

			String status = obj.getString("status");
			String notice = obj.getString("notice");

			if ("success".equals(status)) {
				errS=1;
				// // 学生信息
				JSONObject student = obj.getJSONObject("student"); // 获得学生的信息
				// id = student.getString("id");
				// user_id = student.getString("user_id");
				avatar_url = student.getString("avatar_url"); // 获取本人头像昂所有在地址
				String edunumber = student.getString("s_no");// 学号
				String edu_number = "";

				user_name = student.getString("name");
				nick_name = student.getString("nickname");
				if ("null".equals(edunumber) || edunumber.equals("")) {
					edu_number = "";
					HomePageMainActivity.instance.userName.setText(nick_name);
				} else {
					edu_number = edunumber;
					HomePageMainActivity.instance.userName.setText(user_name);
				}
				JSONObject microposts = obj.getJSONObject("microposts");
				page = Integer.parseInt(microposts.getString("page"));
				pages_count = Integer.parseInt(microposts
						.getString("pages_count"));
				String details_microposts = microposts
						.getString("details_microposts");
				// page":1,"pages_count":2,"details_microposts":
				parseJson_details_microposts(details_microposts);
				// 班级头像和名字
				JSONObject class1 = obj.getJSONObject("class"); // 或得班级信息
				String class_name = class1.getString("name"); // 获取class_name
				String validtime = class1.getString("period_of_validity");

				SharedPreferences preferences = getSharedPreferences(SHARED,
						Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("avatar_url", avatar_url);
				editor.putString("school_class_name", class_name);
				editor.putString("name", user_name);
				editor.putString("nickname", nick_name);
				editor.putString("id", id);
				editor.putString("user_id", user_id);
				editor.putString("school_class_id", school_class_id);
				editor.putString("validtime", validtime);
				editor.putString("edu_number", edu_number);
				editor.commit();
				care = new ArrayList<String>();
				JSONArray follow_microposts_id = obj
						.getJSONArray("follow_microposts_id");
				for (int i = 0; i < follow_microposts_id.length(); ++i) {
					String fmi = follow_microposts_id.getInt(i) + "";
					care.add(fmi);
				}
				Toast.makeText(getApplicationContext(), notice,
						Toast.LENGTH_SHORT).show();
			} else {
				SharedPreferences preferences = getSharedPreferences(SHARED,
						Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("user_id", "");
				editor.putString("student_id", "");
				editor.putString("school_class_id", "");
				editor.putString("id", "");
				editor.putString("edu_number", "");

				editor.commit();
				HomePageMainActivity.instance.finish();
				Intent it = new Intent(HomepageAllActivity.this,
						LoginActivity.class);
				it.putExtra("notice", notice);
				startActivity(it);
				Toast.makeText(getApplicationContext(), notice,
						Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 解析 全部 模块中的 主消息
	 */
	void parseJson_all(String json3) {

		if ("error".equals(json3)) {
		} else {
			JSONObject array3;
			try {
				array3 = new JSONObject(json3);
				String status = array3.getString("status");
				String notice = array3.getString("notice");
				if ("success".equals(status)) {
					String micropostsListJson = array3.getString("microposts");
					JSONObject microposts = new JSONObject(micropostsListJson);
					page = Integer.parseInt(microposts.getString("page"));
					pages_count = Integer.parseInt(microposts
							.getString("pages_count"));
					String details_microposts = microposts
							.getString("details_microposts");
					JSONArray follow_microposts_id = array3
							.getJSONArray("follow_microposts_id");
					for (int i = 0; i < follow_microposts_id.length(); ++i) {
						String fmi = follow_microposts_id.getInt(i) + "";
						care.add(fmi);
					}
					parseJson_details_microposts(details_microposts);
				} else {
					Toast.makeText(getApplicationContext(), notice,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 解析 json 中， "details_microposts" 部分的 数据
	 */
	void parseJson_details_microposts(String details_microposts) {

		JSONArray jsonArray2;
		try {
			jsonArray2 = new JSONArray(details_microposts);
			for (int i = 0; i < jsonArray2.length(); ++i) {
				JSONObject o = (JSONObject) jsonArray2.get(i);
				String micropost_id = o.getString("micropost_id");
				String user_id = o.getString("user_id");
				String user_types = o.getString("user_types");
				String name = o.getString("name");
				String content = o.getString("content");
				String avatar_url = o.getString("avatar_url");
				String created_at = o.getString("new_created_at");
				String careCount = o.getString("follow_microposts_count"); // 关注数
				if (careCount.equals("null")) {
					careCount = "0";
				}
				String reply_microposts_count = o
						.getString("reply_microposts_count");
				Micropost micropost = new Micropost(micropost_id, user_id,
						user_types, name, content, avatar_url, created_at,
						reply_microposts_count, careCount);
				list.add(micropost);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 解析 回复 模块中的 子消息
	 */
	void parseJson_childMicropost(String json3) {
		if ("error".equals(json3)) {
		} else {
			JSONObject array;
			try {
				array = new JSONObject(json3);
				String status = array.getString("status");
				String notice = array.getString("notice");
				if ("success".equals(status)) {
					String micropostsListJson = array
							.getString("reply_microposts");
					JSONObject microposts = new JSONObject(micropostsListJson);
					child_page = Integer.parseInt(microposts.getString("page"));
					child_pages_count = Integer.parseInt(microposts
							.getString("pages_count"));
					String reply_microposts = microposts
							.getString("reply_microposts");
					parse_reply_microposts(reply_microposts);
				} else {
					Toast.makeText(getApplicationContext(), notice,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 解析 子消息 中消息部分
	 */

	public void parse_reply_microposts(String reply_microposts) {

		try {
			JSONArray jsonArray2 = new JSONArray(reply_microposts);
			for (int i = 0; i < jsonArray2.length(); ++i) {
				JSONObject o = (JSONObject) jsonArray2.get(i);
				String id = o.getString("id");
				String sender_id = o.getString("sender_id");
				String sender_types = o.getString("sender_types");
				String sender_name = o.getString("sender_name");
				String sender_avatar_url = o.getString("sender_avatar_url");
				String content = o.getString("content");
				String reciver_name = o.getString("reciver_name");
				String created_at = o.getString("new_created_at");
				String good = o.getString("praise");
				Child_Micropost child = new Child_Micropost(id, sender_id,
						sender_types, sender_name, sender_avatar_url, content,
						reciver_name, created_at, good);
				child_list.add(child);
			}
		} catch (Exception e) {
		}
	}

	/*
	 * 添加关注
	 */
	public void add_concern(final int i, final Micropost mess) {
		prodialog = new ProgressDialog(HomepageAllActivity.this);
		prodialog.setMessage("正在操作...");
		prodialog.setCanceledOnTouchOutside(false);
		prodialog.show();
		final Handler gzHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				JSONObject jsonobject;
				prodialog.dismiss();
				switch (msg.what) {
				case 0:
					try {
						jsonobject = new JSONObject(msg.obj.toString());
						String status = jsonobject.getString("status");
						String notic = jsonobject.getString("notice");
						if (status.equals("success")) {
							care.add(mess.getId().toString());
							guanzhu_list.get(i).setBackgroundResource(
									R.drawable.homepage_guanzhu2);
							int a = Integer
									.parseInt((String) guanzhu_count_list
											.get(i).getText()) + 1;
							guanzhu_count_list.get(i).setText(a + "");

						}
						Toast.makeText(getApplicationContext(), notic,
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 1:
					try {
						jsonobject = new JSONObject(msg.obj.toString());
						String status = jsonobject.getString("status");
						String notic = jsonobject.getString("notice");
						if (status.equals("success")) {
							care.remove(mess.getId().toString());
							guanzhu_list.get(i).setBackgroundResource(
									R.drawable.homepage_guanzhu);
							int a = Integer
									.parseInt((String) guanzhu_count_list
											.get(i).getText()) - 1;
							guanzhu_count_list.get(i).setText(a + "");
						}
						Toast.makeText(getApplicationContext(), notic,
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};
		Thread gzthread = new Thread() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void run() {
				try {
					HashMap<String, String> mp = new HashMap();
					mp.put("user_id", String.valueOf(user_id));
					mp.put("micropost_id", String.valueOf(mess.getId()));
					String str = "";
					Message msg = new Message();// 创建Message 对象
					boolean a = false;
					for (int j = 0; j < care.size(); j++) {
						String str2 = care.get(j).toString();
						String str3 = mess.getId().toString();
						if (str2.equals(str3)) {
							a = true;
							break;
						}
					}
					if (a) {
						str = ExerciseBookTool.sendGETRequest(
								Urlinterface.unfollow, mp);
						msg.what = 1;
						msg.obj = str;

					} else {
						str = ExerciseBookTool.sendGETRequest(
								Urlinterface.add_concern, mp);
						msg.what = 0;
						msg.obj = str;
					}
					gzHandler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendEmptyMessage(7);
				}
			}
		};
		if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
			gzthread.start();
		} else {
			handler.sendEmptyMessage(7);
		}
	}

	/*
	 * 删除 主消息
	 */
	public void del_micropost(final int i, final Micropost mess) {
		prodialog = new ProgressDialog(HomepageAllActivity.this);
		prodialog.setMessage("正在删除消息");
		prodialog.setCanceledOnTouchOutside(false);
		prodialog.show();
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					final String json5 = (String) msg.obj;
					prodialog.dismiss();
					if (json5.equals("error")) {
					} else {
						JSONObject array;
						try {
							array = new JSONObject(json5);//
							String status = array.getString("status");
							String notice = array.getString("notice");
							if ("success".equals(status)) {
								// 删除成功的话,刷新界面
								focus = -1;
								list.remove(i);
								click_list();
								for (int i = 0; i < list.size(); i++) {
									setlayout(i);
								}
							}
							Toast.makeText(getApplicationContext(), notice,
									Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		};
		json = "";
		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("micropost_id", mess.getId() + "");
					json = ExerciseBookTool.sendGETRequest(
							Urlinterface.DELETE_POSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 0;
					msg.obj = json;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendEmptyMessage(7);
				}
			}
		};
		if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {

			thread.start();
		} else {
			handler.sendEmptyMessage(7);
		}
	}

	// 回复
	public void setButton_huifu(final String reply_edit) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				prodialog.dismiss();
				switch (msg.what) {
				case 0:
					final String json2 = (String) msg.obj;
					if (json2.length() == 0) {
					} else {
						JSONObject array2;
						try {
							array2 = new JSONObject(json2);//
							String status = array2.getString("status");
							String notice = array2.getString("notice");

							if ("success".equals(status)) {
								Toast.makeText(getApplicationContext(), notice,
										Toast.LENGTH_SHORT).show();
								String replymicropost = array2
										.getString("replymicropost");
								JSONArray jsonArray2 = new JSONArray(
										replymicropost);
								for (int i = 0; i < jsonArray2.length(); ++i) {
									JSONObject o = (JSONObject) jsonArray2
											.get(i);
									String id = o.getString("id");
									String sender_id = o.getString("sender_id");
									String sender_types = o
											.getString("sender_types");
									String sender_name = o
											.getString("sender_name");
									String sender_avatar_url = o
											.getString("sender_avatar_url");
									String content = o.getString("content");
									String reciver_name = o
											.getString("reciver_name");
									String created_at = o
											.getString("new_created_at");
									String good = o.getString("praise");
									Child_Micropost child = new Child_Micropost(
											id, sender_id, sender_types,
											sender_name, sender_avatar_url,
											content, reciver_name, created_at,
											good);
									child_list.add(0, child);
								}
								reply_gk_list.add(true);
								int a = Integer.parseInt(list.get(focus)
										.getReply_microposts_count()) + 1;

								list.get(focus).setReply_microposts_count(
										a + "");
								huifu_count_list.get(focus).setText(a + "");
								// list_list.get(focus).setAdapter(
								// ziAdapter_list.get(focus));
								ExerciseBookTool
										.setListViewHeightBasedOnChildren(list_list
												.get(focus));
								exerciseBook.setRefresh(1);
							} else {
								Toast.makeText(getApplicationContext(), notice,
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		};

		String kongge = reply_edit.replaceAll(" ", "");
		if (reply_edit.length() == 0 || kongge.equals("")) {
			Toast.makeText(getApplicationContext(), R.string.edit_null,
					Toast.LENGTH_SHORT).show();
		} else {
			Thread thread = new Thread() {
				public void run() {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("content", reply_edit);
						map.put("sender_id", user_id);
						map.put("sender_types", user_types + "");
						map.put("micropost_id", micropost_id);
						map.put("reciver_id", reciver_id);
						map.put("reciver_types", reciver_types);
						map.put("school_class_id", school_class_id);
						Log.i("bbb", "sender_id:" + user_id + "sender_types"
								+ user_types + "micropost_id" + micropost_id
								+ "reciver_id" + reciver_id + "reciver_types"
								+ reciver_types + "school_class_id"
								+ school_class_id);
						String js1 = ExerciseBookTool.doPost(
								Urlinterface.reply_message, map);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = js1;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						handler.sendEmptyMessage(7);
					}
				}
			};
			if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
				prodialog = new ProgressDialog(HomepageAllActivity.this);
				prodialog.setMessage("正在回复...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
				prodialog.dismiss();
				handler.sendEmptyMessage(7);
			}
		}
	}

	// 回复隐藏变显示
	public void setHuiFu(int i, final Micropost mess, RelativeLayout layout1,
			final ListView listView2, final Button lookMore) {

		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {

				switch (msg.what) {
				case 0:
					prodialog.dismiss();
					if (child_list.size() > 0) {// 如果没有子消息，隐藏加载更多按钮
						// lookMore.setVisibility(View.VISIBLE);
						listView2.setVisibility(View.VISIBLE);

					}
					if (child_pages_count >= 2) {
						lookMore.setVisibility(View.VISIBLE);
					} else {
						lookMore.setVisibility(View.GONE);
					}
					listView2.setAdapter(ziAdapter_list.get(focus));
					reply_gk_list.clear();
					for (int j = 0; j < child_list.size(); j++) {
						reply_gk_list.add(true);
					}
					ExerciseBookTool
							.setListViewHeightBasedOnChildren(listView2);
					break;
				default:
					break;
				}
			}
		};
		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("micropost_id", mess.getId());
					String reply = ExerciseBookTool.sendGETRequest(
							Urlinterface.get_reply_microposts, map);
					parseJson_childMicropost(reply);
					mHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					handler.sendEmptyMessage(7);
				}
			}
		};

		if (gk_list.get(i) == true) {
			gk_list.set(i, false);

			for (int j = 0; j < item_huifu.size(); j++) {
				if (j != i) {
					item_huifu.get(j).setVisibility(View.GONE);
				}
			}
			for (int j = 0; j < gk_list.size(); j++) {
				if (j != i) {
					gk_list.set(j, true);
				}
			}
			focus = i;
			layout1.setVisibility(View.VISIBLE);
			listView2.setVisibility(View.VISIBLE);

			if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {

				if (Integer.parseInt(mess.getReply_microposts_count()) > 0) {
					prodialog = new ProgressDialog(HomepageAllActivity.this);
					prodialog.setMessage("正在加载中");
					prodialog.setCanceledOnTouchOutside(false);
					prodialog.show();
					child_list = new ArrayList<Child_Micropost>();
					thread.start();
				} else {
					child_list = new ArrayList<Child_Micropost>();
					mHandler.sendEmptyMessage(0);
				}
			} else {
				handler.sendEmptyMessage(7);
			}
		} else {
			gk_list.set(i, true);
			layout1.setVisibility(View.GONE);
		}
	}

	// 子消息加载更多
	public void setlookMore(final ListView listview, final Micropost mess) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					prodialog.dismiss();
					final String json6 = (String) msg.obj;
					int a = child_list.size();
					parseJson_childMicropost(json6);
					for (int j = a; j < child_list.size(); j++) {
						reply_gk_list.add(true);
					}
					ziAdapter_list.get(focus).notifyDataSetChanged();
					ExerciseBookTool.setListViewHeightBasedOnChildren(listview);
					break;
				default:
					break;
				}
			}
		};
		child_page = child_page + 1;
		if (child_page <= child_pages_count) {
			Thread thread = new Thread() {
				public void run() {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put("micropost_id", mess.getId());
						map.put("page", child_page + "");
						String js = ExerciseBookTool.sendGETRequest(
								Urlinterface.get_reply_microposts, map);
						Message msg = new Message();// 创建Message
													// 对象
						msg.what = 0;
						msg.obj = js;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						handler.sendEmptyMessage(7);
					}
				}
			};
			if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
				prodialog = new ProgressDialog(HomepageAllActivity.this);
				prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
				prodialog.dismiss();
				handler.sendEmptyMessage(7);
			}
		} else {
			Toast.makeText(getApplicationContext(), "已是最后一页",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * 获得当前班级的所有信息
	 */
	class get_class_info implements Runnable {
		public void run() {
			try {

				list = new ArrayList<Micropost>();
				Map<String, String> map = new HashMap<String, String>();
				map.put("student_id", id);
				map.put("school_class_id", school_class_id);
				json = ExerciseBookTool.sendGETRequest(
						Urlinterface.get_class_info, map);
				Message msg = new Message();// 创建Message 对象
				msg.what = 0;
				msg.obj = json;
				handler.sendMessage(msg);

				// handler.sendEmptyMessage(0);
			} catch (Exception e) {
				prodialog.dismiss();
				handler.sendEmptyMessage(7);
			}
		}
	}

	/*
	 * 重新加载当前页面中的数据
	 */
	public void shuaxin() {

		Thread thread = new Thread() {
			public void run() {// 获得第一页信息
				Map<String, String> map = new HashMap<String, String>();
				map.put("student_id", id);
				map.put("school_class_id", school_class_id);
				map.put("page", "1");
				try {
					String result = ExerciseBookTool.sendGETRequest(
							Urlinterface.GET_MICROPOSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 2;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (Exception e1) {
					handler.sendEmptyMessage(7);
					mPullToRefreshView.onHeaderRefreshComplete();
				}
			}
		};
		if (ExerciseBookTool.isConnect(HomepageAllActivity.this)) {
			SharedPreferences preferences = getSharedPreferences(SHARED,
					Context.MODE_PRIVATE);
			id = preferences.getString("id", "73");
			school_class_id = preferences.getString("school_class_id", "83");
			thread.start();
		} else {
			handler.sendEmptyMessage(7);
			mPullToRefreshView.onHeaderRefreshComplete();
		}
	}

	/*
	 * 初始化 参数
	 */
	public void initialize() {
		item_huifu = new ArrayList<RelativeLayout>();
		guanzhu_list = new ArrayList<ImageView>();
		guanzhu_count_list = new ArrayList<TextView>();
		huifu_count_list = new ArrayList<TextView>();
		ziAdapter_list = new ArrayList<ZiAdapter>();

		list = new ArrayList<Micropost>();
		list_list = new ArrayList<ListView>();
		gk_list = new ArrayList<Boolean>();
		HorizontalScrollView_list = new ArrayList<HorizontalScrollView>();
		reply_gk_list = new ArrayList<Boolean>();
	}

	/*
	 * 清空 各个集合中的数据
	 */
	public void click_list() {
		Linear_layout.removeAllViews();
		guanzhu_list.clear();
		gk_list.clear();
		reply_gk_list.clear();
		ziAdapter_list.clear();
		list_list.clear();
		guanzhu_count_list.clear();
		huifu_count_list.clear();
		HorizontalScrollView_list.clear();
	}

	/**
	 * 覆写此方法，以解决ListView滑动被屏蔽问题
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		this.gd.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 覆写此方法，以使用手势识别
	 */
	public boolean onTouchEvent(MotionEvent event) {
		Log.v("MY_TAG", "onTouchEvent");
		return this.gd.onTouchEvent(event);
	}

	/**
	 * 参数解释： e1：第1个ACTION_DOWN MotionEvent e2：最后一个ACTION_MOVE MotionEvent
	 * vX：X轴上的移动速度，像素/秒 vY：Y轴上的移动速度，像素/秒 触发条件 ：
	 * X坐标位移大于minX，Y坐标位移小于maxY，移动速度大于minV像素/秒
	 * 
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
		final int minX = 120, maxY = 20, minV = 10;
		int x1 = (int) e1.getX(), x2 = (int) e2.getX();
		int y1 = (int) e1.getY(), y2 = (int) e2.getY();

		if (Math.abs(x1 - x2) > minX && Math.abs(y1 - y2) < maxY
				&& Math.abs(vX) > minV) {
			if (x1 > x2) {
				Log.v("MY_TAG", "Fling Left");
				flingState = FLING_LEFT;
			} else {
				Log.v("MY_TAG", "Fling Right");
				flingState = FLING_RIGHT;
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float dX, float dY) {
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 5:
			Bundle bundle = data.getExtras();
			String content = bundle.getString("content");
			setButton_huifu(content);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
