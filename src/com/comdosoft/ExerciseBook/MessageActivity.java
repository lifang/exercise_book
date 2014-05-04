package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.ReplyListView.IXListViewListener;
import com.comdosoft.ExerciseBook.pojo.SysMessage;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView.OnFooterRefreshListener;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView.OnHeaderRefreshListener;


public class MessageActivity extends Table_TabHost implements
OnHeaderRefreshListener, OnFooterRefreshListener, Urlinterface, OnGestureListener {
	private ReplyListView mListView;
	private List<SysMessage> replyList = new ArrayList<SysMessage>();;
	private Handler mHandler;
	private int start = 0;
	private int page = 1;
	private static int refreshCnt = 0;
	private String student_id ;
	private String user_id ;
	private String school_class_id;
	private int mShowPosition = -1;
	private Boolean isShow = false;

	private GestureDetector gd;
	private final char FLING_CLICK = 0;
	private final char FLING_LEFT = 1;
	private final char FLING_RIGHT = 2;
	private char flingState = FLING_CLICK;
	private TextView topTv1;
	private ExerciseBook exerciseBook;
	private ProgressDialog prodialog;
	
	private List<HorizontalScrollView> HorizontalScrollView_list;// 主消息 滑动块 集合
	public List<Boolean> gk_list;// 主消息 点击操作 开关集合
	public PullToRefreshView mPullToRefreshView;
	public LinearLayout Linear_layout;
	private int list_item;// list集合的最后一位索引
	private TextView class_middle_null_message;// 没有消息时显示 提示信息
	
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0://   第一次加载
				prodialog.dismiss();
				final String json_all2 = (String) msg.obj;

				if (json_all2.length() == 0) {
				} else {
					replyList = new ArrayList<SysMessage>();
					getNewsJson(json_all2);
				}
				init();

				break;
			case 1://   刷新
//				focus = -1;
				replyList.clear();
				click_list();
				final String json_all = (String) msg.obj;
				getNewsJson(json_all);
				if (replyList.size() != 0) {
					for (int i = 0; i < replyList.size(); i++) {
						setlayout(i);
					}
					class_middle_null_message.setVisibility(View.GONE);
				} else {
					class_middle_null_message.setVisibility(View.VISIBLE);
				}
				mPullToRefreshView.onHeaderRefreshComplete();
				break;
			case 2: //  回复
				Log.i("aa", String.valueOf(msg.obj));
				Toast.makeText(MessageActivity.this,
						String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			case 3://  删除
				prodialog.dismiss();
				click_list();
				if (replyList.size() != 0) {
					for (int i = 0; i < replyList.size(); i++) {
						setlayout(i);
					}
					class_middle_null_message.setVisibility(View.GONE);
				} else {
					class_middle_null_message.setVisibility(View.VISIBLE);
				}
				Toast.makeText(MessageActivity.this,
						String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				
				break;
			case 4: //  加载更多
				final String json4 = (String) msg.obj;

				if (json4.length() == 0) {
				} else {
					getNewsJson(json4);
				}
				if (replyList.size() != 0) {
					for (int i = list_item; i < replyList.size(); i++) {
						setlayout(i);
					}
					class_middle_null_message.setVisibility(View.GONE);
				} else {
					class_middle_null_message.setVisibility(View.VISIBLE);
				}
				mPullToRefreshView.onFooterRefreshComplete();
				break;
			case 7:
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	private Handler handler=null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_listview);
		gd = new GestureDetector(this);
		
		topTv1=(TextView) findViewById(R.id.topTv1);
		handler=new Handler(); 
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		student_id = preferences.getString("id", "1");
		user_id= preferences.getString("user_id", "1");
		school_class_id = preferences.getString("school_class_id", "1");
		
		class_middle_null_message = (TextView) findViewById(R.id.class_middle_null_message);
		gk_list = new ArrayList<Boolean>();
		HorizontalScrollView_list = new ArrayList<HorizontalScrollView>();
		
		topTv1.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) {
				MessageActivity.this.finish();
				Intent intent=new Intent(MessageActivity.this,ReplyListViewActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold); 
			}
		});
		exerciseBook = (ExerciseBook) getApplication();
		exerciseBook.getActivityList().add(this);
		
		if (ExerciseBookTool.isConnect(MessageActivity.this)) {
			page = 1;
			prodialog = new ProgressDialog(MessageActivity.this);
			prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
			Thread thread = new Thread(new get_news());
			thread.start();

		} else {
			handler1.sendEmptyMessage(7);
		
		}
	
	}
 
	
	// 解析获取到的Json
	public int getNewsJson(String json) {
		try {
//			String json2 = "{\"status\":\"success\",\"notice\":\"\u83b7\u53d6\u6210\u529f\uff01\uff01\",\"sysmessage\":[{\"content\":\"\u606d\u559c\u60a8\u83b7\u5f97\u6210\u5c31\u201c\u6377\u8db3\u201d\",\"created_at\":\"2014-04-01T16:46:05+08:00\",\"id\":31,\"school_class_id\":105,\"status\":0,\"student_id\":73,\"updated_at\":\"2014-04-01T16:46:05+08:00\"},{\"content\":\"\u606d\u559c\u60a8\u83b7\u5f97\u6210\u5c31\u201c\u8fc5\u901f\u201d\",\"created_at\":\"2014-04-01T16:45:47+08:00\",\"id\":30,\"school_class_id\":105,\"status\":0,\"student_id\":73,\"updated_at\":\"2014-04-01T16:45:47+08:00\"}]}";
			
//			{"status":"success","notice":"\u83b7\u53d6\u6210\u529f\uff01\uff01","sysmessage":[{"content":"\u606d\u559c\u60a8\u83b7\u5f97\u6210\u5c31\u201c\u6377\u8db3\u201d","created_at":"2014-04-01T16:46:05+08:00","id":31,"school_class_id":105,"status":0,"student_id":73,"updated_at":"2014-04-01T16:46:05+08:00"},{"content":"\u606d\u559c\u60a8\u83b7\u5f97\u6210\u5c31\u201c\u8fc5\u901f\u201d","created_at":"2014-04-01T16:45:47+08:00","id":30,"school_class_id":105,"status":0,"student_id":73,"updated_at":"2014-04-01T16:45:47+08:00"}]}
			JSONObject jsonobject = new JSONObject(json);
			String status = (String) jsonobject.get("status");
			if (status.equals("success")) {
				JSONArray jsonarray = jsonobject.getJSONArray("sysmessage");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);
					String content = jsonobject2.getString("content");
					String created_at = ExerciseBookTool.divisionTime2(jsonobject2
							.getString("new_created_at"));
					String id = jsonobject2.getString("id");
					String class_id = jsonobject2.getString("student_id");
					replyList.add(new SysMessage(content, created_at, id,
							class_id));
				
				}
				return replyList.size();
			} else {
				String notic = (String) jsonobject.get("notic");
				Toast.makeText(getApplicationContext(), notic,
						Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void init() {

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		Linear_layout = (LinearLayout) findViewById(R.id.layout);
		click_list();
		Log.i("aaa", replyList.size() + "====");
		if (replyList.size() != 0) {
			for (int i = 0; i < replyList.size(); i++) {
				setlayout(i);
			}
			class_middle_null_message.setVisibility(View.GONE);
		} else {
			class_middle_null_message.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 动态加载
	 * 
	 * */
	public void setlayout(final int i) {

		final SysMessage rep = replyList.get(i);
		final View convertView = LayoutInflater
				.from(MessageActivity.this).inflate(
						R.layout.message_layout_iteam, null);

		
		final HorizontalScrollView hSView = (HorizontalScrollView) convertView
				.findViewById(R.id.hsv2);
		TextView date = (TextView) convertView
				.findViewById(R.id.child_message_date);
		TextView content = (TextView) convertView
				.findViewById(R.id.child_micropost_content);
		ImageView imgbtn2 = (ImageView) convertView
				.findViewById(R.id.child_micropost_delete);
		final View ll_action2 = convertView.findViewById(R.id.ll_action2);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		gk_list.add(true);
		HorizontalScrollView_list.add(hSView);
		
		imgbtn2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				Dialog dialog = new AlertDialog.Builder(
						MessageActivity.this)
						.setTitle("提示")
						.setMessage("您确认要删除么?")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										del(i);
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

		
		convertView.setOnTouchListener(new View.OnTouchListener() {
			

			public boolean onTouch(View v, MotionEvent event) {
				int actionW = ll_action2.getWidth();
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					

					switch (flingState) {
					case FLING_LEFT:
					case FLING_RIGHT:
					case FLING_CLICK:
						if (gk_list.get(i) == true) {
							hSView.smoothScrollTo(actionW, 0);
							gk_list.set(i, false);
							for (int j = 0; j < gk_list.size(); j++) {
								if (j != i) {
									gk_list.set(j, true);
									HorizontalScrollView_list.get(j)
											.smoothScrollTo(0, 0);
								}
							}
						} else {
							gk_list.set(i, true);
							hSView.smoothScrollTo(0, 0);
						}
						break;
					}
				}
				return false;

			}
		});
		content.setText(replyList.get(i).getContent());
		date.setText(replyList.get(i).getCreated_at());
		RelativeLayout vew = (RelativeLayout) convertView.findViewById(R.id.child_user_left);

		if (i % 2 == 0) {
			vew.setBackgroundResource(R.color.before_click);
		} else {
			vew.setBackgroundResource(R.color.huse);
		}
		Linear_layout.addView(convertView);
	}
	
	/*
	 * 清空 各个集合中的数据
	 */
	public void click_list() {
		Linear_layout.removeAllViews();
		gk_list.clear();
		HorizontalScrollView_list.clear();
	}

	/*
	 * 上拉加载更多
	 */
	public void onFooterRefresh(PullToRefreshView view) {
		list_item = replyList.size();
		Log.i("aaa", "onLoadMore（）");
		if (ExerciseBookTool.isConnect(MessageActivity.this)) {
			page = page + 1;
			Log.i("aaa", "onLoadMore（）--page：" + page);
			Thread thread = new Thread(new get_news2());
			thread.start();

		} else {
			mPullToRefreshView.onFooterRefreshComplete();
			handler1.sendEmptyMessage(7);
		}
	}

	/*
	 * 下拉刷新
	 */
	public void onHeaderRefresh(PullToRefreshView view) {
		if (ExerciseBookTool.isConnect(MessageActivity.this)) {
			shuaxin();
		} else {

			handler1.sendEmptyMessage(7);
			mPullToRefreshView.onHeaderRefreshComplete();
		}

	}

	/*
	 * 重新加载页面中的数据
	 */
	public void shuaxin() {

		if (ExerciseBookTool.isConnect(MessageActivity.this)) {
			page = 1;
			Log.i("aaa", "onLoadMore（）--page：" + page);
			Thread thread = new Thread(new get_news1());
			thread.start();

		} else {
			mPullToRefreshView.onFooterRefreshComplete();
			handler1.sendEmptyMessage(7);
		}
	}

	
	
	
	/*
	 * 获得第一页信息
	 */
	class get_news implements Runnable {
		public void run() {
			try {
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("student_id", student_id);
				mp.put("school_class_id", school_class_id);
				mp.put("page","1" );
				String json = ExerciseBookTool
						.sendGETRequest(Urlinterface.get_sysmessage, mp);
				Message msg = new Message();// 创建Message 对象
				msg.what = 0;
				msg.obj = json;
				handler1.sendMessage(msg);
			} catch (Exception e) {
				
				handler1.sendEmptyMessage(7);
			}
		}
	}
	/*
	 * 获得第一页信息
	 */
	class get_news1 implements Runnable {
		public void run() {
			try {
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("student_id", student_id);
				mp.put("school_class_id", school_class_id);
				mp.put("page","1" );
				String json = ExerciseBookTool
						.sendGETRequest(Urlinterface.get_sysmessage, mp);
				Message msg = new Message();// 创建Message 对象
				msg.what = 1;
				msg.obj = json;
				handler1.sendMessage(msg);
			} catch (Exception e) {
				
				handler1.sendEmptyMessage(7);
			}
		}
	}
	/*
	 * 获得 更多 信息
	 */
	class get_news2 implements Runnable {
		public void run() {
			try {
				Log.i("aaa",  "get_news2");
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("student_id", student_id);
				mp.put("school_class_id", school_class_id);
				mp.put("page",page+"" );
				String json = ExerciseBookTool
						.sendGETRequest(Urlinterface.get_sysmessage, mp);

				Message msg = new Message();// 创建Message 对象
				msg.what = 4;
				msg.obj = json;
				handler1.sendMessage(msg);
			} catch (Exception e) {
				
				handler1.sendEmptyMessage(7);
			}
		}
	}

	public void del(final int position){
		
		
		Thread thread = new Thread() {
			public void run() {
				try {
					HashMap<String, String> mp = new HashMap<String, String>();
					mp.put("user_id", user_id);
					mp.put("sys_message_id",
							replyList.get(position).getId());
					mp.put("school_class_id",school_class_id);
					String json = ExerciseBookTool
							.doPost(Urlinterface.delete_sys_message,
									mp);
					JSONObject jsonobejct;
					jsonobejct = new JSONObject(json);
					String status = jsonobejct.getString("status");
					String notice = jsonobejct.getString("notice");
					if (status.equals("success")) {
						replyList.remove(position);
						
					} 
					Message msg=new Message();
					msg.obj=notice;
					msg.what=3;
					handler1.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		if (ExerciseBookTool.isConnect(MessageActivity.this)) {
			prodialog = new ProgressDialog(MessageActivity.this);
			prodialog.setMessage("正在删除消息");
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
		thread.start();	
		}else {
			handler1.sendEmptyMessage(7);
		}
		
	}



	public boolean dispatchTouchEvent(MotionEvent event) {
		this.gd.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 覆写此方法，以使用手势识别
	 */
	public boolean onTouchEvent(MotionEvent event) {
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
				flingState = FLING_LEFT;
			} else {
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
}
