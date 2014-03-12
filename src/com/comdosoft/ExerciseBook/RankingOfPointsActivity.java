package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.pojo.Ranking;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.ImageMemoryCache;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class RankingOfPointsActivity extends Activity {
	private ListView rankingLv;
	private ExerciseBook exerciseBook;
	private ImageView close_activity;
	private TextView rank_title;
	private int pub_id = 1; // 做题的 序号
	private int types = 1; // 做题的 序号
	private static ImageMemoryCache memoryCache;
	private List<Ranking> rankinglist = new ArrayList<Ranking>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.ranking_points);
		memoryCache = HomePageMainActivity.instance.memoryCache;
		Intent intent = getIntent();//
		types = intent.getIntExtra("types", -1); // 获得上个页面传过来的
		pub_id = intent.getIntExtra("pub_id", -1);
		// 做题的
		// 序号

		exerciseBook = (ExerciseBook) getApplication();
		rank_title = (TextView) findViewById(R.id.rank_title);
		rank_title.setText(Urlinterface.namearr[types] + "当日排名");
		rankingLv = (ListView) findViewById(R.id.rankingLv);
		close_activity = (ImageView) findViewById(R.id.rank_close);// 关闭当前页面

		// 关闭当前页面
		close_activity.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));
//		rankinglist.add(new Ranking("", "dingzuoqiang", "1234567890"));

		if (ExerciseBookTool.isConnect(RankingOfPointsActivity.this)) {
			Thread thread = new Thread(new get_rank());
			thread.start();

		} else {
			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.INTERNET, 0).show();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				final String res = (String) msg.obj;
				try {
					JSONObject obj = new JSONObject(res);

					String status = obj.getString("status");
					String notice = obj.getString("notice");
					if ("success".equals(status)) {
						
//						{"status":"success","notice":"\u83b7\u53d6\u5b8c\u6210\uff01",
//						"record_details":[{"avatar_url":"/assets/default_avater.jpg","name":"nan","score":1,"student_id":1}]}
						
						JSONArray jsonarray = obj.getJSONArray("record_details");
						for (int i = 0; i < jsonarray.length(); i++) {
							JSONObject jsonobject2 = jsonarray.getJSONObject(i);
							String avatar_url = jsonobject2
									.getString("avatar_url");
							String name = jsonobject2.getString("name");
							String point = jsonobject2.getString("score");
							rankinglist
									.add(new Ranking(avatar_url, name, point));
						}
					}else {
						Toast.makeText(getApplicationContext(), notice, 0).show();
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "获取排名失败", 0).show();
				}
				rankingLv.setAdapter(new RankingAdapter(getApplication(),
						rankinglist));
				rankingLv.setDivider(null);

				break;
			case 1:
				Toast.makeText(RankingOfPointsActivity.this,
						msg.obj.toString(), 1).show();
				break;
			case 7:
				Toast.makeText(getApplicationContext(),
						ExerciseBookParams.INTERNET, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	class get_rank implements Runnable {
		public void run() {
			try {
				HashMap<String, String> mp = new HashMap<String, String>();
				mp.put("types", String.valueOf(types));
				mp.put("pub_id", String.valueOf(pub_id));
				String json = ExerciseBookTool.sendGETRequest(
						Urlinterface.get_rankings, mp);
				Message msg = new Message();// 创建Message 对象
				msg.what = 0;
				msg.obj = json;
				handler.sendMessage(msg);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "获取排名失败", 0).show();
				handler.sendEmptyMessage(7);
			}
		}
	}

	// 自定义 适配器
	public class RankingAdapter extends BaseAdapter {
		private List<Ranking> rankinglist;
		private Context context;

		public RankingAdapter(Context context, List<Ranking> rankinglist) {
			this.context = context;
			this.rankinglist = rankinglist;
		}

		public int getCount() {
			return rankinglist.size();
		}

		public Object getItem(int position) {
			return rankinglist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.ranking_item, null);
			TextView mingci = (TextView) convertView
					.findViewById(R.id.ranking_mingci);
			ImageView rank_face = (ImageView) convertView
					.findViewById(R.id.rank_face);
			TextView name = (TextView) convertView
					.findViewById(R.id.ranking_name);
			TextView points = (TextView) convertView
					.findViewById(R.id.ranking_point);

			int po = position + 1;
			String poS = po + "";
			poS = poS.substring(poS.length() - 1);
			int IntpoS = Integer.parseInt(poS);
			if (IntpoS == 1) {
				mingci.setText(po + "st");
			} else if (IntpoS == 2) {
				mingci.setText(po + "nd");
			} else if (IntpoS == 3) {
				mingci.setText(po + "rd");
			} else {
				mingci.setText(po + "th");
			}
			Ranking rank = rankinglist.get(position);

			// 设置头像
			if (rank.getAvatar_url().equals("")
					|| rank.getAvatar_url().equals("null")) {
			} else {
				String url = Urlinterface.IP + rank.getAvatar_url();
				Bitmap result = memoryCache.getBitmapFromCache(url);
				if (result == null) {
					ExerciseBookTool.set_bk(url, rank_face, memoryCache);
				} else {
					rank_face.setImageDrawable(new BitmapDrawable(result));
				}
			}
			name.setText(rank.getName());
			points.setText(rank.getPoint());
			return convertView;
		}
	}

}
