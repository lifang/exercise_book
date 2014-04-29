package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.MessageActivity.ViewHolder;
import com.comdosoft.ExerciseBook.pojo.Reply;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.OpenInputMethod;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView.OnFooterRefreshListener;
import com.comdosoft.ExerciseBook.tools.PullToRefreshView.OnHeaderRefreshListener;
import com.comdosoft.ExerciseBook.tools.Urlinterface;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * @作者 丁作强
 * @时间 2014-4-29 上午9:50:55
 */
public class ReplyListViewActivity extends Table_TabHost implements
		OnHeaderRefreshListener, OnFooterRefreshListener, Urlinterface,
		OnGestureListener {
	private List<Reply> replyList = new ArrayList<Reply>();
	private int page = 1;
	private String user_id;
	private String school_class_id;
	private int mShowPosition = -1;
	private Boolean isShow = false;
	private GestureDetector gd;
	private final char FLING_CLICK = 0;
	private final char FLING_LEFT = 1;
	private final char FLING_RIGHT = 2;
	private char flingState = FLING_CLICK;
	private List<HorizontalScrollView> HorizontalScrollView_list;// 主消息 滑动块 集合
	public List<Boolean> gk_list;// 主消息 点击操作 开关集合
	private String replyContent;
	private String micropost_id;
	private String reciver_id;
	private String reciver_types;
	private TextView topTv2;
	private ExerciseBook exerciseBook;
	private ProgressDialog prodialog;
	static boolean active = false;
	String json;
	ImageMemoryCache memoryCache;
	public static ReplyListViewActivity instance = null;
	List<Boolean> listbool = new ArrayList<Boolean>();
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0://   第一次加载
				prodialog.dismiss();
				final String json_all2 = (String) msg.obj;

				if (json_all2.length() == 0) {
				} else {
					replyList = new ArrayList<Reply>();
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
				Toast.makeText(ReplyListViewActivity.this,
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
				Toast.makeText(ReplyListViewActivity.this,
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
	private Handler handler = null;

	public PullToRefreshView mPullToRefreshView;
	public LinearLayout Linear_layout;
	private int list_item;// list集合的最后一位索引
	private TextView class_middle_null_message;// 没有消息时显示 提示信息

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_listview);
		gd = new GestureDetector(this);
		exerciseBook = (ExerciseBook) getApplication();
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		user_id = preferences.getString("user_id", "121");
		school_class_id = preferences.getString("school_class_id", "106");
		handler = new Handler();
		memoryCache = exerciseBook.getMemoryCache();
		class_middle_null_message = (TextView) findViewById(R.id.class_middle_null_message);
		gk_list = new ArrayList<Boolean>();
		HorizontalScrollView_list = new ArrayList<HorizontalScrollView>();
		topTv2 = (TextView) findViewById(R.id.topTv2);
		topTv2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ReplyListViewActivity.this.finish();
				Intent intent = new Intent(ReplyListViewActivity.this,
						MessageActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});
		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {
			page = 1;
			prodialog = new ProgressDialog(ReplyListViewActivity.this);
			prodialog.setMessage(ExerciseBookParams.PD_CLASS_INFO);
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
			Thread thread = new Thread(new get_news());
			thread.start();

		} else {
			handler1.sendEmptyMessage(7);
		}
		active = true;
		instance = this;
		exerciseBook = (ExerciseBook) getApplication();
		exerciseBook.getActivityList().add(this);
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

		final Reply rep = replyList.get(i);
		final View convertView = LayoutInflater
				.from(ReplyListViewActivity.this).inflate(
						R.layout.reply_layout_iteam, null);

		final HorizontalScrollView hSView = (HorizontalScrollView) convertView
				.findViewById(R.id.hsv2);

		ImageView use_face = (ImageView) convertView
				.findViewById(R.id.child_user_face);
		TextView sender = (TextView) convertView
				.findViewById(R.id.child_message_senderName);
		TextView reciver = (TextView) convertView
				.findViewById(R.id.child_message_reciverName);
		TextView date = (TextView) convertView
				.findViewById(R.id.child_message_date);
		TextView content = (TextView) convertView
				.findViewById(R.id.child_micropost_content);
		ImageView imgbtn1 = (ImageView) convertView
				.findViewById(R.id.child_micropost_huifu);
		ImageView imgbtn2 = (ImageView) convertView
				.findViewById(R.id.child_micropost_delete);
		final View ll_action2 = convertView.findViewById(R.id.ll_action2);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		gk_list.add(true);
		HorizontalScrollView_list.add(hSView);
		hSView.setOnTouchListener(new View.OnTouchListener() {

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
		
		
		if (rep.getSender_avatar_url().length() > 4) {
			// ExerciseBookTool.set_background(Urlinterface.IP
			// + replyList.get(position).getSender_avatar_url(),
			// holder.use_face);
			String url = IP + rep.getSender_avatar_url();
			Bitmap result = memoryCache.getBitmapFromCache(url);
			if (result == null) {
				ExerciseBookTool.set_bk(url, use_face, memoryCache);
				Log.i("aa", " 适配器    网络网络   " + i);
			} else {
				Log.i("aa", " 缓存缓存缓存缓存   " + i);
				use_face.setImageDrawable(new BitmapDrawable(result));
			}
			// String url = IP + rep.getSender_avatar_url();
			// imageLoader.displayImage(url, holder.use_face, options,
			// animateFirstListener);
		}
		imgbtn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				micropost_id = rep.getMicropost_id();
				reciver_id = rep.getReciver_id();
				reciver_types = rep.getReciver_types();
				Intent intent = new Intent(ReplyListViewActivity.this,
						OpenInputMethod.class);
				startActivityForResult(intent, 0);
			}
		});
		imgbtn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Dialog dialog = new AlertDialog.Builder(
						ReplyListViewActivity.this)
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
		sender.setText(rep.getSender_name());
		reciver.setText(rep.getStatus());
		content.setText(rep.getContent());
		date.setText(rep.getCreated_at());
		RelativeLayout vew = (RelativeLayout) convertView
				.findViewById(R.id.child_user_left);

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
		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {
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
		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {
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

		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {
			page = 1;
			Log.i("aaa", "onLoadMore（）--page：" + page);
			Thread thread = new Thread(new get_news1());
			thread.start();

		} else {
			mPullToRefreshView.onFooterRefreshComplete();
			handler1.sendEmptyMessage(7);
		}
	}

	// 解析获取到的Json
	public int getNewsJson(String json) {
		try {
			JSONObject jsonobject = new JSONObject(json);

			String status = (String) jsonobject.get("status");
			if (status.equals("success")) {
				// [{"id":1378,"content":"[[布尔]]回复了您的消息：;||;nani","reciver_types":0,"sender_avatar_url":"\/avatars\/teachers\/2014-03\/teacher_75_2.jpg","reciver_id":158,"created_at":"2014-04-02T15:25:15+08:00","user_id":130,"sender_name":"布尔","micropost_id":647},{"id":1325,"content":"[[布尔]]回复了您的消息：结婚","reciver_types":0,"sender_avatar_url":"\/avatars\/teachers\/2014-03\/teacher_75_2.jpg","reciver_id":158,"created_at":"2014-04-01T17:21:04+08:00","user_id":130,"sender_name":"布尔","micropost_id":605},{"id":1318,"content":"[[布尔]]回复了您的消息：换个号","reciver_types":0,"sender_avatar_url":"\/avatars\/teachers\/2014-03\/teacher_75_2.jpg","reciver_id":158,"created_at":"2014-04-01T16:55:02+08:00","user_id":130,"sender_name":"布尔","micropost_id":605},{"id":1314,"content":"[[布尔]]回复了您的消息：将卡卡卡","reciver_types":0,"sender_avatar_url":"\/avatars\/teachers\/2014-03\/teacher_75_2.jpg","reciver_id":158,"created_at":"2014-04-01T16:52:15+08:00","user_id":130,"sender_name":"布尔","micropost_id":605},{"id":1304,"content":"[[wm]]回复了您的消息：qqqqqq","reciver_types":1,"sender_avatar_url":"\/avatars\/students\/2014-04\/student_92.jpg","reciver_id":161,"created_at":"2014-04-01T16:29:01+08:00","user_id":130,"sender_name":"wm","micropost_id":605},{"id":1302,"content":"[[wm]]回复了您的消息：ggggw","reciver_types":1,"sender_avatar_url":"\/avatars\/students\/2014-04\/student_92.jpg","reciver_id":161,"created_at":"2014-04-01T16:28:09+08:00","user_id":130,"sender_name":"wm","micropost_id":605},{"id":1300,"content":"[[charles]]回复了您的消息：hhhhh","reciver_types":1,"sender_avatar_url":"\/assets\/default_avater.jpg","reciver_id":163,"created_at":"2014-04-01T16:26:12+08:00","user_id":130,"sender_name":"charles","micropost_id":605},{"id":1298,"content":"[[charles]]回复了您的消息：ghhjj","reciver_types":1,"sender_avatar_url":"\/assets\/default_avater.jpg","reciver_id":163,"created_at":"2014-04-01T16:24:58+08:00","user_id":130,"sender_name":"charles","micropost_id":605}]
				JSONArray jsonarray = jsonobject.getJSONArray("messages");
				for (int i = 0; i < jsonarray.length(); i++) {
					Log.i("aaa", "getNewsJson----" + i);
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);
					List<String> liststr = divisionStr(jsonobject2
							.getString("content"));
					Log.i("aaa", "getNewsJson----" + i);
					String jsonstatus = liststr.get(0);
					String content = liststr.get(1);

					String created_at = ExerciseBookTool
							.divisionTime2(jsonobject2
									.getString("new_created_at"));
					String id = jsonobject2.getString("id");

					String micropost_id = jsonobject2.getString("micropost_id");
					String reciver_id = jsonobject2.getString("reciver_id");
					String reciver_types = jsonobject2
							.getString("reciver_types");
					String sender_avatar_url = jsonobject2
							.getString("sender_avatar_url");
					String sender_name = jsonobject2.getString("sender_name");
					String user_id = jsonobject2.getString("user_id");
					listbool.add(false);
					replyList.add(new Reply(id, micropost_id, user_id,
							reciver_id, reciver_types, sender_avatar_url,
							sender_name, jsonstatus, content, created_at));

				}
				return replyList.size();
			} else {
				String notic = (String) jsonobject.get("notic");
				Toast.makeText(getApplicationContext(), notic, 1).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * 获得第一页信息
	 */
	class get_news implements Runnable {
		public void run() {
			try {
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("user_id", user_id);
				mp.put("school_class_id", school_class_id);
				mp.put("page", "1");
				String json = ExerciseBookTool.sendGETRequest(
						Urlinterface.get_News, mp);

				Message msg = new Message();// 创建Message 对象
				msg.what = 0;
				msg.obj = json;
				handler1.sendMessage(msg);
			} catch (Exception e) {
				handler1.sendEmptyMessage(7);
			}
		}
	}
	class get_news1 implements Runnable {
		public void run() {
			try {
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("user_id", user_id);
				mp.put("school_class_id", school_class_id);
				mp.put("page", "1");
				String json = ExerciseBookTool.sendGETRequest(
						Urlinterface.get_News, mp);

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
				Log.i("aaa", "get_news2");
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("user_id", user_id);
				mp.put("school_class_id", school_class_id);
				mp.put("page", page + "");
				String json = ExerciseBookTool.sendGETRequest(
						Urlinterface.get_News, mp);

				Message msg = new Message();// 创建Message 对象
				msg.what = 4;
				msg.obj = json;
				handler1.sendMessage(msg);

			} catch (Exception e) {
				handler1.sendEmptyMessage(7);
			}
		}
	}

	// 分割content
	public List<String> divisionStr(String str) {
		// "content":"[[ding]]回复了您的消息：;||;we we"
		// "content":"[[ding]]回复了您的消息：沃尔沃","reciver_types":0,"sender_avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","reciver_id":115,"created_at":"2014-04-01T16:57:20+08:00","user_id":130,"sender_name":"ding","micropost_id":604}
		// [[若相守]]回复了您：;||;兔兔啦咯了
		List<String> list = new ArrayList<String>();
		int temp1 = str.indexOf("[[");
		int temp2 = str.indexOf("]]");
		int temp3 = str.indexOf("：");
		// String st1 = str.substring(temp3 + 3, temp3 + 7);
		// list.add(str.substring(temp2 + 2, temp3 + 2));
		// if (";||;".equals(st1)) {
		// list.add(str.substring(temp3 + 7, str.length()));
		// } else {
		// list.add(str.substring(temp3 + 3, str.length()));
		// }
		String st1 = str.substring(temp3 + 1, temp3 + 2);
		list.add(str.substring(temp2 + 5, temp3));
		if (";".equals(st1)) {
			list.add(str.substring(temp3 + 5, str.length()));
		} else {
			list.add(str.substring(temp3 + 1, str.length()));
		}

		return list;
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 5:
			Bundle bundle = data.getExtras();
			replyContent = bundle.getString("content");
			Thread thread = new Thread() {
				public void run() {
					try {
						HashMap<String, String> mp = new HashMap<String, String>();
						mp.put("sender_id", user_id);
						mp.put("school_class_id", school_class_id);
						mp.put("sender_types", "1");
						mp.put("content", replyContent);
						mp.put("micropost_id", micropost_id);
						mp.put("reciver_id", reciver_id);
						mp.put("reciver_types", reciver_types);
						String json = ExerciseBookTool
								.doPost(reply_message, mp);
						JSONObject jsonobject = new JSONObject(json);
						String notice = jsonobject.getString("notice");
						Message msg = new Message();
						msg.obj = notice;
						msg.what = 2;
						handler1.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			};
			thread.start();
			break;
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

	/*
	 * 加载 头像
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public void del(final int position) {

		Thread thread = new Thread() {
			public void run() {
				try {
					HashMap<String, String> mp = new HashMap<String, String>();
					mp.put("user_id", user_id);
					mp.put("school_class_id", school_class_id);
					mp.put("message_id", replyList.get(position).getId());
					String json = ExerciseBookTool.sendGETRequest(
							delete_message, mp);
					JSONObject jsonobject = new JSONObject(json);
					String notice = jsonobject.getString("notice");
					if (jsonobject.getString("status").equals("success")) {
						replyList.remove(position);
					}
					Message msg = new Message();
					msg.obj = notice;
					msg.what = 3;
					handler1.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {
			prodialog = new ProgressDialog(ReplyListViewActivity.this);
			prodialog.setMessage("正在删除消息");
			prodialog.setCanceledOnTouchOutside(false);
			prodialog.show();
			thread.start();
		} else {
			handler1.sendEmptyMessage(7);
		}
	}

}