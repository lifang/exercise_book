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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.ReplyListView.IXListViewListener;
import com.comdosoft.ExerciseBook.pojo.Reply;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.OpenInputMethod;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class ReplyListViewActivity extends Table_TabHost implements
		IXListViewListener, Urlinterface, OnGestureListener {
	private ReplyListView mListView;
	private List<Reply> replyList = new ArrayList<Reply>();
	private Handler mHandler;
	private int start = 0;
	private int page = 1;
	private static int refreshCnt = 0;
	private String user_id;
	private String school_class_id;
	private int mShowPosition = -1;
	private Boolean isShow = false;
	ReplyAdapter madapter = new ReplyAdapter();
	private GestureDetector gd;
	private final char FLING_CLICK = 0;
	private final char FLING_LEFT = 1;
	private final char FLING_RIGHT = 2;
	private char flingState = FLING_CLICK;
	private String replyContent;
	private String micropost_id;
	private String reciver_id;
	private String reciver_types;
	private TextView topTv2;
	private ExerciseBook exerciseBook;
	private ProgressDialog prodialog;
	static boolean active = false;
	String json;
	public static ReplyListViewActivity instance = null;
	List<Boolean> listbool = new ArrayList<Boolean>();
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				prodialog.dismiss();
				final String json_all2 = (String) msg.obj;

				if (json_all2.length() == 0) {
				} else {
					replyList = new ArrayList<Reply>();
					getNewsJson(json_all2);
				}
				int a = replyList.size();
				if (a == 0) {
					Toast.makeText(ReplyListViewActivity.this, "暂无任何通知",
							Toast.LENGTH_SHORT).show();
				} else {
					mListView.setAdapter(madapter);
				}
				onLoad();

				break;
			case 1:
				Toast.makeText(ReplyListViewActivity.this, "未开启网络",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				prodialog.dismiss();
				// madapter.notifyDataSetChanged();
				mListView.setAdapter(madapter);
				Toast.makeText(ReplyListViewActivity.this,
						String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			case 4:
				final String json4 = (String) msg.obj;

				if (json4.length() == 0) {
				} else {
					getNewsJson(json4);
				}
				 handler.post(runnableUi);  
//				madapter.notifyDataSetChanged();
//				onLoad();
				break;
			}
		}
	};
	private Handler handler=null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_listview);
		gd = new GestureDetector(this);
		SharedPreferences preferences = getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		user_id = preferences.getString("user_id", "121");
		school_class_id = preferences.getString("school_class_id", "106");
		handler=new Handler();  
		mListView = (ReplyListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
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
			handler1.sendEmptyMessage(1);
			onLoad();
		}
		active = true;
		instance = this;
		exerciseBook = (ExerciseBook) getApplication();
		exerciseBook.getActivityList().add(this);
		mListView.setXListViewListener(this);
		mListView.setDividerHeight(0);
		mHandler = new Handler();
	}
	
	 // 构建Runnable对象，在runnable中更新界面  
    Runnable   runnableUi=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
			madapter.notifyDataSetChanged();
			onLoad();
        }  
          
    }; 

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
				onLoad();
				handler1.sendEmptyMessage(1);
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
				onLoad();
				handler1.sendEmptyMessage(1);
			}
		}
	}

	// 分割content
	public List<String> divisionStr(String str) {
		// "content":"[[ding]]回复了您的消息：;||;we we"
		// "content":"[[ding]]回复了您的消息：沃尔沃","reciver_types":0,"sender_avatar_url":"\/avatars\/students\/2014-04\/student_17.jpg","reciver_id":115,"created_at":"2014-04-01T16:57:20+08:00","user_id":130,"sender_name":"ding","micropost_id":604}
//		[[若相守]]回复了您：;||;兔兔啦咯了
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
		list.add(str.substring(temp2 + 5, temp3 ));
		if (";".equals(st1)) {
			list.add(str.substring(temp3 + 5, str.length()));
		} else {
			list.add(str.substring(temp3 + 1, str.length()));
		}

		return list;
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {

		// SharedPreferences userInfo = getSharedPreferences("replyMenu", 0);
		// Editor editor = userInfo.edit();// 获取编辑器
		// editor.putBoolean("ReplyMenu", true);
		// editor.commit();
		page = 1;
		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {

			Thread thread = new Thread(new get_news());
			thread.start();

		} else {
			handler1.sendEmptyMessage(1);
			onLoad();
		}
	}

	@Override
	public void onLoadMore() {

		Log.i("aaa", "onLoadMore（）");
		if (ExerciseBookTool.isConnect(ReplyListViewActivity.this)) {
			page = page + 1;
			Log.i("aaa", "onLoadMore（）--page：" + page);
			Thread thread = new Thread(new get_news2());
			thread.start();

		} else {
			handler1.sendEmptyMessage(1);
			onLoad();
		}

	}

	public static class ViewHolder {
		public HorizontalScrollView hSView;
		public ImageView use_face;
		public TextView sender;
		public TextView reciver;
		public TextView date;
		public TextView content;
		public ImageView imgbtn1;
		public ImageView imgbtn2;
		private View ll_action2;
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

	class ReplyAdapter extends BaseAdapter {
		ExerciseBook hw = (ExerciseBook) getApplication();

		public ReplyAdapter() {
		}

		public ReplyAdapter(Context context) {
		}

		public int getCount() {
			return replyList.size();
		}

		public Object getItem(int position) {
			return replyList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			final int showPosition = position;
			ViewHolder holder = null;
			final Reply rep = replyList.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.reply_layout_iteam,
						null);
				holder = new ViewHolder();

				holder.hSView = (HorizontalScrollView) convertView
						.findViewById(R.id.hsv2);

				holder.use_face = (ImageView) convertView
						.findViewById(R.id.child_user_face);
				holder.sender = (TextView) convertView
						.findViewById(R.id.child_message_senderName);
				holder.reciver = (TextView) convertView
						.findViewById(R.id.child_message_reciverName);
				holder.date = (TextView) convertView
						.findViewById(R.id.child_message_date);
				holder.content = (TextView) convertView
						.findViewById(R.id.child_micropost_content);
				holder.imgbtn1 = (ImageView) convertView
						.findViewById(R.id.child_micropost_huifu);
				holder.imgbtn2 = (ImageView) convertView
						.findViewById(R.id.child_micropost_delete);
				holder.ll_action2 = convertView.findViewById(R.id.ll_action2);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			convertView.setOnTouchListener(new View.OnTouchListener() {
				final int showPosition = position;

				public boolean onTouch(View v, MotionEvent event) {
					ViewHolder viewHolder = (ViewHolder) v.getTag();
					int scrollX = viewHolder.hSView.getScrollX();
					int actionW = viewHolder.ll_action2.getWidth();
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						if (scrollX == 0) {
							viewHolder.hSView.smoothScrollTo(actionW, 0);
							mShowPosition = showPosition;
							notifyDataSetChanged();
							isShow = true;
						} else if (scrollX < actionW / 2) {
							viewHolder.hSView.smoothScrollTo(0, 0);
							isShow = false;
						} else if (scrollX == actionW || scrollX >= actionW / 2) {
							if (isShow) {
								viewHolder.hSView.smoothScrollTo(0, 0);
								isShow = false;
							} else {
								viewHolder.hSView.smoothScrollTo(actionW, 0);
								mShowPosition = showPosition;
								notifyDataSetChanged();
								isShow = true;
							}
						}
						return true;
					}
					return false;

				}
			});
			if (holder.hSView.getScrollX() != 0 && position != mShowPosition) {
				holder.hSView.scrollTo(0, 0);
			}
			if (holder.hSView.getScrollX() != 0) {
				holder.hSView.scrollTo(0, 0);
			}
			if (rep.getSender_avatar_url().length() > 4) {
				// ExerciseBookTool.set_background(Urlinterface.IP
				// + replyList.get(position).getSender_avatar_url(),
				// holder.use_face);
				String url = IP + rep.getSender_avatar_url();
				// ExerciseBookTool.set_background(url, face);
				Bitmap result = memoryCache.getBitmapFromCache(url);
				if (result == null) {
					ExerciseBookTool.set_bk(url, holder.use_face, memoryCache);
				} else {

					holder.use_face
							.setImageDrawable(new BitmapDrawable(result));
				}
			}
			holder.imgbtn1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					micropost_id = rep.getMicropost_id();
					reciver_id = rep.getReciver_id();
					reciver_types = rep.getReciver_types();
					Intent intent = new Intent(ReplyListViewActivity.this,
							OpenInputMethod.class);
					startActivityForResult(intent, 0);
				}
			});
			holder.imgbtn2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Dialog dialog = new AlertDialog.Builder(
							ReplyListViewActivity.this)
							.setTitle("提示")
							.setMessage("您确认要删除么?")
							.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											del(position);
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
			holder.sender.setText(rep.getSender_name());
			holder.reciver.setText(rep.getStatus());
			holder.content.setText(rep.getContent());
			holder.date.setText(rep.getCreated_at());
			RelativeLayout vew = (RelativeLayout) convertView
					.findViewById(R.id.child_user_left);

			if (position % 2 == 0) {
				vew.setBackgroundResource(R.color.before_click);
			} else {
				vew.setBackgroundResource(R.color.huse);
			}
			return convertView;
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
				handler1.sendEmptyMessage(1);
			}
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