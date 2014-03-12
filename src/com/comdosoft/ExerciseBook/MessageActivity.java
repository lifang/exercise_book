package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.ReplyListView.IXListViewListener;
import com.comdosoft.ExerciseBook.pojo.SysMessage;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MessageActivity extends Table_TabHost implements
		IXListViewListener, Urlinterface, OnGestureListener {
	private ReplyListView mListView;
	private List<SysMessage> replyList = new ArrayList<SysMessage>();;
	private Handler mHandler;
	private int start = 0;
	private String page = "1";
	private static int refreshCnt = 0;
	private String user_id = "1";
	private String school_class_id = "1";
	private int mShowPosition = -1;
	private Boolean isShow = false;
	ReplyAdapter madapter = new ReplyAdapter();
	private GestureDetector gd;
	private final char FLING_CLICK = 0;
	private final char FLING_LEFT = 1;
	private final char FLING_RIGHT = 2;
	private char flingState = FLING_CLICK;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_listview);
		gd = new GestureDetector(this);
		mListView = (ReplyListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		get_News();
		// mListView.setPullLoadEnable(false);
		// mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mListView.setDividerHeight(0);
		mHandler = new Handler();
	}

	public void getnews() {
		new Thread() {
			public void run() {
				if (ExerciseBookTool.isConnect(MessageActivity.this)) {
					get_News();
				} else {
					handler1.sendEmptyMessage(1);
				}
			}
		}.start();
	}

	Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mListView.setAdapter(madapter);
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "未开启网络",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	// 解析获取到的Json
	public int getNewsJson(String json) {
		try {
			Log.i("aa", "解析字符串");
			JSONObject jsonobject = new JSONObject(json);
			String status = (String) jsonobject.get("status");
			if (status.equals("success")) {
				JSONArray jsonarray = jsonobject.getJSONArray("sysmessage");
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject jsonobject2 = jsonarray.getJSONObject(i);

					String content = jsonobject2.getString("content");
					String created_at = divisionTime(jsonobject2
							.getString("created_at"));
					String id = jsonobject2.getString("id");
					String class_id = jsonobject2.getString("student_id");
					replyList.add(new SysMessage(content, created_at, id,
							class_id));
				}
				Log.i("aa", replyList.size() + "");
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

	// 请求获取和我相关的消息
	public void get_News() {
		Thread thread = new Thread() {
			public void run() {
				if (ExerciseBookTool.isConnect(MessageActivity.this)) {
					try {
						if (!httpGetNews(user_id, school_class_id).equals(null)) {
							getNewsJson(httpGetNews(user_id, school_class_id));
							handler1.sendEmptyMessage(0);
						}
					} catch (Exception e) {
						handler1.sendEmptyMessage(1);
					}

				}
			}

		};
		thread.start();
	}

	// HTTP请求
	public String httpGetNews(String user_id, String school_class_id) {
		try {
			HashMap<String, String> mp = new HashMap<String, String>();
			mp.put("student_id", user_id);
			mp.put("page", page);
			String json = ExerciseBookTool.sendGETRequest(
					Urlinterface.get_sysmessage, mp);
			return json;
		} catch (Exception e) {
			handler1.sendEmptyMessage(1);
		}
		return null;
	}

	// 分割content
	public List<String> divisionStr(String str) {
		List<String> list = new ArrayList<String>();
		int temp1 = str.indexOf(";||;");
		int temp2 = str.lastIndexOf("]]");
		list.add(str.substring(temp2 + 2, temp1));
		list.add(str.substring(temp1 + 4, str.length()));
		return list;
	}

	// 分割时间
	public String divisionTime(String timeStr) {
		int temp1 = timeStr.indexOf("T");
		int temp2 = timeStr.lastIndexOf("+");
		return timeStr.substring(0, temp1) + " "
				+ timeStr.substring(temp1 + 1, temp2);
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				replyList.clear();
				// mAdapter.notifyDataSetChanged();
				page = "1";
				get_News();
				madapter = new ReplyAdapter();
				mListView.setAdapter(madapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				int num = Integer.valueOf(page);
				num++;
				page = String.valueOf(num);
				get_News();
				mListView.setAdapter(madapter);
				onLoad();
			}
		}, 2000);
	}

	public static class ViewHolder {
		public HorizontalScrollView hSView;
		public TextView date;
		public TextView content;
		public ImageView imgbtn2;
		private View ll_action2;
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
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.message_layout_iteam,
						null);
				holder = new ViewHolder();
				holder.hSView = (HorizontalScrollView) convertView
						.findViewById(R.id.hsv2);
				holder.date = (TextView) convertView
						.findViewById(R.id.child_message_date);
				holder.content = (TextView) convertView
						.findViewById(R.id.child_micropost_content);
				holder.imgbtn2 = (ImageView) convertView
						.findViewById(R.id.child_micropost_delete);
				holder.ll_action2 = convertView.findViewById(R.id.ll_action2);
				View vew = convertView.findViewById(R.id.child_user_left);

				if (position % 2 == 0) {
					vew.setBackgroundResource(R.color.before_click);
				} else {
					vew.setBackgroundResource(R.color.huse);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imgbtn2.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (ExerciseBookTool.isConnect(MessageActivity.this)) {

						Thread thread = new Thread() {
							public void run() {
								try {
									HashMap<String, String> mp = new HashMap<String, String>();
									mp.put("user_id", user_id);
									mp.put("sys_message_id",
											replyList.get(position).getId());
									mp.put("school_class_id",
											replyList.get(position)
													.getClass_id());
									String json = ExerciseBookTool
											.doPost(Urlinterface.delete_sys_message,
													mp);
									JSONObject jsonobejct = new JSONObject(json);
									String status = jsonobejct
											.getString("status");
									String notice = jsonobejct
											.getString("notice");
									if (status.equals("success")) {
										Toast.makeText(getApplicationContext(),
												notice, Toast.LENGTH_SHORT)
												.show();
										replyList.remove(position);
										madapter.notifyDataSetChanged();
									} else {
										Toast.makeText(getApplicationContext(),
												notice, Toast.LENGTH_SHORT)
												.show();
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						thread.start();
					}
				}

			});

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
			holder.content.setText(replyList.get(position).getContent());
			holder.date.setText(replyList.get(position).getCreated_at());
			return convertView;
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
}
