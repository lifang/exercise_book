package com.comdosoft.ExerciseBook.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.R;
import com.comdosoft.ExerciseBook.pojo.knowledges_card;
import com.comdosoft.ExerciseBook.pojo.tags;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class LabelAdapter extends BaseAdapter implements Urlinterface {
	public List<tags> tagsList;
	public List<Integer> cardlist;
	Context content;
	knowledges_card myList;
	List<Integer> mytags;
	private String student_id;
	private String school_class_id;
	private String card_id;

	public LabelAdapter(Context content, int index,
			List<tags> tagsList, List<Integer> card, String student_id,
			String school_class_id, String card_id) {
		this.content = content;
		this.tagsList = tagsList;
		this.cardlist = card;
		this.student_id = student_id;
		this.school_class_id = school_class_id;
		this.card_id = card_id;
		mytags = new ArrayList<Integer>();
		for(int i=0;i<cardlist.size();i++)
		{
			mytags.add(cardlist.get(i));
		}
		Log.i("2",mytags.size()+"1!!"+tagsList.size());
	}

	public int getCount() {
		return tagsList.size();
	}

	public Object getItem(int position) {
		return tagsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	class Holder {
		ImageView img;
		TextView tv;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(content);
		Holder holder = null;
		convertView = inflater.inflate(R.layout.biaoqian_iteam, null);
		holder = new Holder();
		holder.img = (ImageView) convertView.findViewById(R.id.bq_iteam_iv);
		holder.tv = (TextView) convertView.findViewById(R.id.bq_iteam_tv);
		convertView.setPadding(0, 10, 0, 10);
		for (int i = 0; i < mytags.size(); i++) {
			if (mytags.get(i) == Integer
					.valueOf(tagsList.get(position).getId())) {
				holder.img.setImageDrawable(content.getResources().getDrawable(
						R.drawable.biaoqianf));
			}
		}
		holder.tv.setText(tagsList.get(position).getName());
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Thread thread = new Thread() {
					public void run() {
						try {
							Map<String, String> map = new HashMap<String, String>();
							map.put("student_id", student_id);
							map.put("school_class_id", school_class_id);
							map.put("knowledge_card_id", card_id);
							map.put("card_tag_id", tagsList.get(position)
									.getId());
							String json = ExerciseBookTool.sendGETRequest(
									knoledge_tag_relation, map);
							JSONObject jsonobject = new JSONObject(json);
							if (jsonobject.getString("status")
									.equals("success")) {
								int type = jsonobject.getInt("type");
								Message msg = new Message();
								switch (type) {
								case 0:
									msg.obj = "错误";
									break;
								case 1:
									msg.obj = "删除成功";
									mytags.remove(setList(tagsList.get(position).getId()));
									break;
								case 2:
									msg.obj = "添加成功";
									mytags.add(Integer.valueOf(tagsList.get(position).getId()));
									break;
								}
								msg.what = 0;
								handler1.sendMessage(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				};
				thread.start();
			}
		});
		return convertView;

	}
	public int setList(String id)
	{
		for (int i = 0; i < mytags.size(); i++) {
			if (mytags.get(i) == Integer
					.valueOf(id)) {
				return i;
			}
		}
		return 0;
	}
	Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				Toast.makeText(content, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}
