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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.HomepageAllActivity.ZiAdapter;
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

public class HomepageMyselfActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener, Urlinterface,
		OnGestureListener {
	private ExerciseBook exerciseBook;
	private String user_id = "130"; // 学生 id 上面 会传过来的 学生student_id，
	private String id = "73";
	private String school_class_id = "83";
	private ProgressDialog prodialog;
	// -------------------------------------------------------------------
	private String json = "{\"status\":\"success\",\"notice\":\"\u767b\u9646\u6210\u529f\uff01\",\"student\":{\"id\":66,\"name\":\"hrueieurh \",\"user_id\":66,\"nickname\":\"yeueieiri \",\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\"},\"class\":{\"id\":1,\"name\":\"1401\",\"tearcher_name\":\"fgf\",\"tearcher_id\":1},\"classmates\":[{\"avatar_url\":\"/assets/default_avater.jpg\",\"id\":1,\"name\":\"nan\",\"nickname\":\"zxn\"}],\"task_messages\":[],\"microposts\":{\"page\":1,\"pages_count\":4,\"details_microposts\":[{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"ww\u6211\u6d4b\u5b89\u5fbdi\u2026\u2026\n\n\u6d4b\u8bd5\u4e00\u4e0b\",\"created_at\":\"2014-01-27T14:12:02+08:00\",\"micropost_id\":145,\"name\":\"hrueieurh \",\"reply_microposts_count\":1,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"556355265335 \",\"created_at\":\"2014-01-23T07:17:25+08:00\",\"micropost_id\":104,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"Babbitt \",\"created_at\":\"2014-01-23T02:58:48+08:00\",\"micropost_id\":103,\"name\":\"hrueieurh \",\"reply_microposts_count\":4,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u5173\u4e8e\u52a0\u5f3a\",\"created_at\":\"2014-01-22T10:11:11+08:00\",\"micropost_id\":99,\"name\":\"???\",\"reply_microposts_count\":6,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"\u7684\u7684\u7684\",\"created_at\":\"2014-01-22T09:47:22+08:00\",\"micropost_id\":97,\"name\":\"hrueieurh \",\"reply_microposts_count\":5,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"\u5927\u5927\u65b9\u65b9\u53d1\",\"created_at\":\"2014-01-22T09:47:12+08:00\",\"micropost_id\":96,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"hjk\",\"created_at\":\"2014-01-22T06:43:12+08:00\",\"micropost_id\":95,\"name\":\"???\",\"reply_microposts_count\":19,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"ffg\",\"created_at\":\"2014-01-22T02:57:52+08:00\",\"micropost_id\":94,\"name\":\"???\",\"reply_microposts_count\":6,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"UI o\",\"created_at\":\"2014-01-21T10:48:36+08:00\",\"micropost_id\":93,\"name\":\"???\",\"reply_microposts_count\":2,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"rrrrr\",\"created_at\":\"2014-01-21T10:13:22+08:00\",\"micropost_id\":86,\"name\":\"hrueieurh \",\"reply_microposts_count\":16,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"rrrrr\",\"created_at\":\"2014-01-21T10:13:21+08:00\",\"micropost_id\":85,\"name\":\"hrueieurh \",\"reply_microposts_count\":7,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"content\":\"dffff\",\"created_at\":\"2014-01-21T10:13:19+08:00\",\"micropost_id\":84,\"name\":\"hrueieurh \",\"reply_microposts_count\":0,\"user_id\":66,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u5feb\u4e86\uff0c\u5feb\u4e86\u5feb\u4e86\",\"created_at\":\"2014-01-21T06:46:09+08:00\",\"micropost_id\":73,\"name\":\"??\",\"reply_microposts_count\":0,\"user_id\":8,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"dddddd\",\"created_at\":\"2014-01-20T06:24:07+08:00\",\"micropost_id\":72,\"name\":\"???\",\"reply_microposts_count\":3,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"ggjjkkjhghhjjj\",\"created_at\":\"2014-01-20T04:41:44+08:00\",\"micropost_id\":71,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u98ce\u98ce\u5149\u5149\u97e9\u56fd\u521a\u521a\u597d\u53d1\u98ce\u98ce\u5149\u5149\u5730\u65b9\u98ce\u683c\u56de\u5bb6\u98ce\u683c\u54c8\u54c8\u54c8\u98ce\u683c\u5475\u5475\u7684\u611f\u89c9\u7684\u98ce\u683c\u4e2a\",\"created_at\":\"2014-01-20T02:52:47+08:00\",\"micropost_id\":70,\"name\":\"???\",\"reply_microposts_count\":1,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u89c4\u5212\u5c40\",\"created_at\":\"2014-01-18T10:57:36+08:00\",\"micropost_id\":69,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u4f1a\",\"created_at\":\"2014-01-18T10:41:09+08:00\",\"micropost_id\":68,\"name\":\"???\",\"reply_microposts_count\":1,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"\u4e2a\u770b\u8fc7\",\"created_at\":\"2014-01-18T10:40:54+08:00\",\"micropost_id\":67,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1},{\"avatar_url\":\"/assets/default_avater.jpg\",\"content\":\"yyuu\",\"created_at\":\"2014-01-18T10:33:45+08:00\",\"micropost_id\":66,\"name\":\"???\",\"reply_microposts_count\":0,\"user_id\":9,\"user_types\":1}]},\"daily_tasks\":[{\"id\":2,\"name\":\"2014-1-20\u4f5c\u4e1a\",\"start_time\":\"2014-01-20T00:00:00+08:00\",\"end_time\":\"2014-01-30T00:00:00+08:00\",\"question_packages_url\":\"/question_package_1.js\",\"listening_schedule\":\"1/4\",\"reading_schedule\":\"0/4\"}],\"follow_microposts_id\":[86,97,97,96,95,93,71,93,71,99],\"messages\":[{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;jjj\",\"created_at\":\"2014-01-26T12:32:23+08:00\",\"id\":554,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;uui\",\"created_at\":\"2014-01-26T12:32:07+08:00\",\"id\":553,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;huu\",\"created_at\":\"2014-01-26T12:31:59+08:00\",\"id\":552,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[fgh]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;jjii\",\"created_at\":\"2014-01-26T12:30:43+08:00\",\"id\":550,\"micropost_id\":85,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u5173\u6ce8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:38+08:00\",\"id\":208,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:30+08:00\",\"id\":189,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u5173\u6ce8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:04:30+08:00\",\"id\":198,\"micropost_id\":86,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;a\",\"created_at\":\"2014-01-23T03:04:00+08:00\",\"id\":187,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;CA\",\"created_at\":\"2014-01-23T03:03:46+08:00\",\"id\":186,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ding\",\"created_at\":\"2014-01-23T03:01:09+08:00\",\"id\":185,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;@\",\"created_at\":\"2014-01-23T03:00:51+08:00\",\"id\":184,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:00:29+08:00\",\"id\":183,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;ca\",\"created_at\":\"2014-01-23T03:00:29+08:00\",\"id\":182,\"micropost_id\":103,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;abc\",\"created_at\":\"2014-01-23T02:07:00+08:00\",\"id\":180,\"micropost_id\":99,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[xhxksn ]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;''''\",\"created_at\":\"2014-01-23T02:05:37+08:00\",\"id\":175,\"micropost_id\":97,\"sender_avatar_url\":\"/avatars/students/2014-01/student_66.jpg\",\"sender_name\":\"hrueieurh \",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u54c8\u54c8\u54c8\u5c31\",\"created_at\":\"2014-01-22T10:09:41+08:00\",\"id\":163,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u53cd\u5f39\u6709\",\"created_at\":\"2014-01-22T10:09:22+08:00\",\"id\":162,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u89c4\u5212\",\"created_at\":\"2014-01-22T10:09:15+08:00\",\"id\":161,\"micropost_id\":97,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:41+08:00\",\"id\":159,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:40+08:00\",\"id\":157,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:39+08:00\",\"id\":153,\"micropost_id\":85,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:34+08:00\",\"id\":144,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:32+08:00\",\"id\":135,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:30+08:00\",\"id\":126,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66},{\"content\":\"[[???]]\u56de\u590d\u4e86\u60a8\u7684\u6d88\u606f\uff1a;||;\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\u554a\",\"created_at\":\"2014-01-22T08:18:28+08:00\",\"id\":117,\"micropost_id\":86,\"sender_avatar_url\":\"/assets/default_avater.jpg\",\"sender_name\":\"???\",\"user_id\":66}]}";
	public PullToRefreshView mPullToRefreshView;
	public LinearLayout Linear_layout;

	private String classname;
	private int child_page = 1; // 子消息 分页加载的 第几页
	private int child_pages_count = 1;// 子消息总页数
	private int page=1;// 当前第几页
	private int pages_count=1;// 总页数
	private String reciver_id = "";
	private String reciver_types = "";
	private String micropost_id = "";

	private int list_item;// list集合的最后一位索引
	private int user_types = 1;
	public List<Boolean> gk_list;// 主消息 点击操作 开关集合
	public List<Boolean> reply_gk_list;// 回复 点击操作 开关集合
	public List<RelativeLayout> item_huifu;// 回复开关集合
	private List<Micropost> list;
	private List<String> care;
	private List<ListView> list_list;
	private List<ZiAdapter> ziAdapter_list;
	private List<EditText> Reply_edit_list;
	private int focus = -1;
	private List<TextView> guanzhu_count_list;// 关注数 集合
	private List<TextView> huifu_count_list;// 回复数 集合
	private List<HorizontalScrollView> HorizontalScrollView_list;// 滑动块 集合
	private ArrayList<Child_Micropost> child_list;
	private GestureDetector gd;
	private static ImageMemoryCache memoryCache;
	// 事件状态
	private final char FLING_CLICK = 0;
	private final char FLING_LEFT = 1;
	private final char FLING_RIGHT = 2;
	private char flingState = FLING_CLICK;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				final String json_1 = (String) msg.obj;
				parseJson_Myself(json_1);
				init();
				break;
			case 2:
				focus = -1;
				list.clear();
				click_list();
				final String json_all = (String) msg.obj;
				parseJson_Myself(json_all);
				for (int i = 0; i < list.size(); i++) {
					setlayout(i);
				}
				mPullToRefreshView.onHeaderRefreshComplete();
				break;
			case 3:
				final String json_all2 = (String) msg.obj;

				parseJson_Myself(json_all2);

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
		gd = new GestureDetector(this);
		memoryCache = HomePageMainActivity.instance.memoryCache;
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);

		 user_id = preferences.getString("user_id", null);
		 id = preferences.getString("id", null);
		school_class_id = preferences.getString("school_class_id", null);

		item_huifu = new ArrayList<RelativeLayout>();
		guanzhu_count_list = new ArrayList<TextView>();
		huifu_count_list = new ArrayList<TextView>();
		HorizontalScrollView_list = new ArrayList<HorizontalScrollView>();
		ziAdapter_list = new ArrayList<ZiAdapter>();
		Reply_edit_list = new ArrayList<EditText>();
		list = new ArrayList<Micropost>();
		list_list = new ArrayList<ListView>();
		gk_list = new ArrayList<Boolean>();
		reply_gk_list = new ArrayList<Boolean>();
	}

	protected void onResume() {
		super.onResume();
		int refresh = exerciseBook.getRefresh();
		if (refresh == 1) {

			if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {
				prodialog = new ProgressDialog(HomepageMyselfActivity.this);
				prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				class_button_myself();
			} else {
				handler.sendEmptyMessage(7);
			}
		}
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

	public void setlayout(final int i) {
		final Micropost mess = list.get(i);
		ZiAdapter Adapter = new ZiAdapter();
		ziAdapter_list.add(Adapter);
		final View convertView = LayoutInflater.from(
				HomepageMyselfActivity.this).inflate(R.layout.class_layout,
				null);
		final HorizontalScrollView hSView = (HorizontalScrollView) convertView
				.findViewById(R.id.hsv);
		final View action = convertView.findViewById(R.id.ll_action);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		final RelativeLayout re = (RelativeLayout) convertView
				.findViewById(R.id.user_left);
		LayoutParams lp = re.getLayoutParams();
		lp.width = dm.widthPixels;
		ImageView face = (ImageView) convertView.findViewById(R.id.user_face); // 头像

		ImageView huifu_img = (ImageView) convertView
				.findViewById(R.id.huifu_item); // 回复图标
		TextView Micropost_senderName = (TextView) convertView
				.findViewById(R.id.message_senderName); // 谁发的
		TextView guanzhu_count = (TextView) convertView
				.findViewById(R.id.guanzhu_count); // 关注数
		TextView huifu_count = (TextView) convertView
				.findViewById(R.id.huifu_count); // 回复数
		ImageView button1 = (ImageView) convertView
				.findViewById(R.id.shanchu_button); // 删除按钮
		ImageView button2 = (ImageView) convertView
				.findViewById(R.id.guanzhu_button); // 关注按钮
		ImageView button3 = (ImageView) convertView
				.findViewById(R.id.huifu_button); // 回复按钮
		TextView Micropost_content = (TextView) convertView
				.findViewById(R.id.micropost_content); // 消息内容
		TextView Micropost_date = (TextView) convertView
				.findViewById(R.id.micropost_date); // 日期

		final ListView listView2 = (ListView) convertView// 子消息的list
				.findViewById(R.id.aa);//
		list_list.add(listView2);
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
//			ExerciseBookTool.set_background(IP + mess.getAvatar_url(), face);
			String url = IP + mess.getAvatar_url();
			// ExerciseBookTool.set_background(url, face);

			Bitmap result = memoryCache.getBitmapFromCache(url);
			if (result == null) {
				Log.i("aa", " 网络网络"+i);
				ExerciseBookTool.set_bk(url, face,memoryCache);
			} else {
				
				Log.i("aa", " 缓存缓存缓存缓存"+i);
				face.setImageDrawable(new BitmapDrawable(result));
			}
		}

		Micropost_senderName.setText(mess.getName()); // 发消息的人
		Micropost_content.setText(mess.getContent()); // 消息内容
		Micropost_date.setText(divisionTime(mess.getCreated_at())); // 消息日期
		String mic_id = mess.getId();

		// 回复
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reciver_id = mess.getUser_id();
				reciver_types = mess.getUser_types();
				Intent intentp = new Intent();
				intentp.setClass(HomepageMyselfActivity.this,
						com.comdosoft.ExerciseBook.tools.OpenInputMethod.class);//
				startActivityForResult(intentp, 0);
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
						HomepageMyselfActivity.this)
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
						focus = i;
						micropost_id = mess.getId();//
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
			re.setBackgroundResource(R.color.before_click);
		} else {
			re.setBackgroundResource(R.color.after_click);
		}
		Linear_layout.addView(convertView);
	}

	// 加载更多
	public void onFooterRefresh(PullToRefreshView view) {
		// focus = -1;
		page = page + 1;
		if (page <= pages_count) {
			list_item = list.size();
			Thread thread = new Thread() {
				public void run() {// 全部 页面加载 更多

					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", page + "");
					String result = "";
					try {
						result = ExerciseBookTool.sendGETRequest(
								Urlinterface.MY_MICROPOSTS, map);
					} catch (Exception e1) {
						mPullToRefreshView.onHeaderRefreshComplete();
						handler.sendEmptyMessage(7);
					}
					Message msg = new Message();// 创建Message 对象
					msg.what = 3;
					msg.obj = result;
					handler.sendMessage(msg);

				}
			};

			if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {
				thread.start();
			} else {
				mPullToRefreshView.onHeaderRefreshComplete();
				handler.sendEmptyMessage(7);
			}

		} else {
			Toast.makeText(getApplicationContext(), "已经是最后一页了..",
					Toast.LENGTH_SHORT).show();
			mPullToRefreshView.onFooterRefreshComplete();
		}
	}

	// 刷新
	public void onHeaderRefresh(PullToRefreshView view) {
		shuaxin();
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
			LayoutInflater inflater = HomepageMyselfActivity.this
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

			child_view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "----", 0).show();
				}
			});
			// 设置监听事件
			hSView2.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						switch (flingState) {
						case FLING_LEFT:
							// flingState = FLING_CLICK;
							// // return false;
							// break;
							// // 处理右滑事件
						case FLING_RIGHT:
							// flingState = FLING_CLICK;
							// hSView2.smoothScrollTo(0, 0);
							// // return false;
							// break;
							// // 处理点击事件
						case FLING_CLICK:
							// hSView2.smoothScrollTo(action2.getWidth(), 0);
							// Toast.makeText(getApplicationContext(),
							// "Click Item:" + position2,
							// Toast.LENGTH_SHORT).show();
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

			if (position2 != 0) {
				ImageView child_bottom = (ImageView) child_view
						.findViewById(R.id.child_jiantou);
				child_bottom.setVisibility(View.GONE);
			}
			final Child_Micropost child_Micropost = child_list.get(position2);
			if (child_Micropost.getSender_avatar_url() != null) { // 设置头像
//				ExerciseBookTool.set_background(
//						IP + child_Micropost.getSender_avatar_url(), face);
				String url = IP + child_Micropost.getSender_avatar_url();
				// ExerciseBookTool.set_background(url, face);

				Bitmap result = memoryCache.getBitmapFromCache(url);
				if (result == null) {
					Log.i("aa", " 适配器    网络网络"+position2);
					ExerciseBookTool.set_bk(url, face,memoryCache);
				} else {
					
					Log.i("aa", " 适配器   缓存缓存缓存缓存"+position2);
					face.setImageDrawable(new BitmapDrawable(result));
				}
			}
			Micropost_who.setText(child_Micropost.getSender_name()); // 回复人
			Micropost_ToWho.setText(child_Micropost.getReciver_name()); // 接收人
			Micropost_date
					.setText(divisionTime(child_Micropost.getCreated_at())); // 时间
			Micropost_content.setText(child_Micropost.getContent()); // 消息内容
			if (user_id.equals(child_Micropost.getSender_id())) {// 自己回复的帖子现实删除按钮
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
							HomepageMyselfActivity.this)
							.setTitle("提示")
							.setMessage("您确认要删除么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (ExerciseBookTool
													.isConnect(HomepageMyselfActivity.this)) {
												thread.start();
											} else {
												Toast.makeText(
														getApplicationContext(),
														ExerciseBookParams.INTERNET,
														0).show();
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
							HomepageMyselfActivity.this,
							com.comdosoft.ExerciseBook.tools.OpenInputMethod.class);//
					startActivityForResult(intentp, 0);
				}
			});
			return child_view;
		}
	}

	/*
	 * 解析 我的 模块中的 主消息
	 */
	void parseJson_Myself(String json3) {

		if ("error".equals(json3)) {
		} else {
			JSONObject array;
			try {
				array = new JSONObject(json3);
				Boolean status = array.getBoolean("status");
				String notice = array.getString("notice");
				if (true == status) {
					String micropostsListJson = array
							.getString("details_microposts");
					page = Integer.parseInt(array.getString("page"));
					pages_count = Integer.parseInt(array
							.getString("pages_count"));
					parseJson_details_microposts(micropostsListJson);
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
				String created_at = o.getString("created_at");
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
					JSONArray jsonArray2 = new JSONArray(reply_microposts);
					for (int i = 0; i < jsonArray2.length(); ++i) {
						JSONObject o = (JSONObject) jsonArray2.get(i);
						String id = o.getString("id");
						String sender_id = o.getString("sender_id");
						String sender_types = o.getString("sender_types");
						String sender_name = o.getString("sender_name");
						String sender_avatar_url = o
								.getString("sender_avatar_url");
						String content = o.getString("content");
						String reciver_name = o.getString("reciver_name");
						// String reciver_avatar_url = o
						// .getString("reciver_avatar_url");
						String created_at = o.getString("created_at");
						Child_Micropost child = new Child_Micropost(id,
								sender_id, sender_types, sender_name,
								sender_avatar_url, content, reciver_name,
								created_at);
						child_list.add(child);
					}
				} else {
					Toast.makeText(getApplicationContext(), notice,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// 分割时间
	public String divisionTime(String timeStr) {
		timeStr = timeStr.replace("-", "/");
		int temp1 = timeStr.indexOf("T");
		int temp2 = timeStr.lastIndexOf("+");
		String s = timeStr.substring(temp1 + 1, temp2);
		int temp3 = s.lastIndexOf(":");
		return timeStr.substring(0, temp1) + "  " + s.substring(0, temp3);
	}

	/*
	 * 删除 主消息
	 */
	public void del_micropost(final int i, final Micropost mess) {
		prodialog = new ProgressDialog(HomepageMyselfActivity.this);
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
							if ("success".equals(status)) {// 删除成功的话,刷新界面
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
		if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {
			thread.start();
		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, 0).show();
		}
	}

	// 回复
	public void setButton_huifu(final String reply_edit) {
		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
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
								final Handler mHandler = new Handler() {
									public void handleMessage(
											android.os.Message msg) {
										switch (msg.what) {
										case 0:
											prodialog.dismiss();
											final String json7 = (String) msg.obj;
											child_list = new ArrayList<Child_Micropost>();
											parseJson_childMicropost(json7);
											reply_gk_list.clear();
											for (int j = 0; j < child_list
													.size(); j++) {
												reply_gk_list.add(true);
											}
											int a = Integer
													.parseInt(list
															.get(focus)
															.getReply_microposts_count()) + 1;

											list.get(focus)
													.setReply_microposts_count(
															a + "");
											huifu_count_list.get(focus)
													.setText(a + "");
											list_list.get(focus).setAdapter(
													ziAdapter_list.get(focus));
											ExerciseBookTool
													.setListViewHeightBasedOnChildren(list_list
															.get(focus));
											exerciseBook.setRefresh(1);
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
											map.put("micropost_id",
													micropost_id);
											String js2 = ExerciseBookTool
													.sendGETRequest(
															Urlinterface.get_reply_microposts,
															map);
											Message msg = new Message();// 创建Message对象
											msg.what = 0;
											msg.obj = js2;
											mHandler.sendMessage(msg);
										} catch (Exception e) {
											handler.sendEmptyMessage(7);
										}
									}
								};
								if (ExerciseBookTool
										.isConnect(HomepageMyselfActivity.this)) {

									thread.start();
								} else {
									Toast.makeText(getApplicationContext(),
											ExerciseBookParams.INTERNET, 0)
											.show();
								}
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
			if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {
				prodialog = new ProgressDialog(HomepageMyselfActivity.this);
				prodialog.setMessage("正在回复...");
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, 0).show();
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
//						lookMore.setVisibility(View.VISIBLE);
						listView2.setVisibility(View.VISIBLE);

					} 
					if(child_pages_count>=2){
						lookMore.setVisibility(View.VISIBLE);
					}else {
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
			

			if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {
				
				if (Integer.parseInt(mess.getReply_microposts_count()) > 0) {
					prodialog = new ProgressDialog(HomepageMyselfActivity.this);
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
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, 0).show();
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
		Log.i("linshi", 1 + "/");
		if (child_page <= child_pages_count) {
			Log.i("linshi", 2 + "/");
			Log.i("linshi", child_page + "/");
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
			if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {
				prodialog = new ProgressDialog(HomepageMyselfActivity.this);
				prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
				prodialog.setCanceledOnTouchOutside(false);
				prodialog.show();
				thread.start();
			} else {
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, 0).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "已是最后一页",
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * 获得 "我的" 第一页消息
	 */
	public void class_button_myself() {
	

		page = 1;
		list = new ArrayList<Micropost>();
		json = "";

		Thread thread = new Thread() {
			public void run() {
				try {
					Map<String, String> map = new HashMap<String, String>();
					map.put("user_id", user_id);
					map.put("school_class_id", school_class_id);
					map.put("page", page + "");
					json = ExerciseBookTool.sendGETRequest(
							Urlinterface.MY_MICROPOSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 0;
					msg.obj = json;
					handler.sendMessage(msg);
					handler.sendEmptyMessage(4);// 关闭prodialog
				} catch (Exception e) {
					mPullToRefreshView.onHeaderRefreshComplete();
					handler.sendEmptyMessage(7);
				}
			}
		};

		if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {

			thread.start();
		} else {
			mPullToRefreshView.onHeaderRefreshComplete();
			handler.sendEmptyMessage(7);
		}
	}

	/*
	 * 重新加载当前页面中的数据
	 */
	public void shuaxin() {

		Thread thread = new Thread() {
			public void run() {// 获得第一页信息

				Map<String, String> map = new HashMap<String, String>();
				map.put("user_id", user_id);
				map.put("school_class_id", school_class_id);
				map.put("page", "1");
				String result = "";
				try {
					result = ExerciseBookTool.sendGETRequest(
							Urlinterface.MY_MICROPOSTS, map);
					Message msg = new Message();// 创建Message 对象
					msg.what = 2;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (Exception e1) {
					mPullToRefreshView.onHeaderRefreshComplete();
					handler.sendEmptyMessage(7);
				}
			}
		};
		if (ExerciseBookTool.isConnect(HomepageMyselfActivity.this)) {

			thread.start();
		} else {
			mPullToRefreshView.onHeaderRefreshComplete();
			handler.sendEmptyMessage(7);
		}
	}

	/*
	 * 清空 各个集合中的数据
	 */
	public void click_list() {
		Linear_layout.removeAllViews();
		Reply_edit_list.clear();
		gk_list.clear();
		ziAdapter_list.clear();
		list_list.clear();
		guanzhu_count_list.clear();
		huifu_count_list.clear();
		HorizontalScrollView_list.clear();
		reply_gk_list.clear();
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
	@Override
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
