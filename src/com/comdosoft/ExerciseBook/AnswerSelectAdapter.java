package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.comdosoft.ExerciseBook.pojo.AnswerSelectItemPojo;

/**
 * @作者 马龙
 * @时间 2014-4-9 下午4:29:04
 */
public class AnswerSelectAdapter extends BaseAdapter {

	private int status = 0;
	private int type = 0;
	private LayoutInflater mInflater;
	private Map<Integer, String> checkMap = new HashMap<Integer, String>();
	private List<String> answerList = new ArrayList<String>();
	private List<String> list = new ArrayList<String>();
	private String[] letterArr = new String[] { "A", "B", "C", "D", "E", "F" };

	public AnswerSelectAdapter(Context context, List<String> list) {
		super();
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	public AnswerSelectAdapter(int type, Context context,
			List<String> answerList, List<String> list) {
		super();
		this.type = type;
		this.mInflater = LayoutInflater.from(context);
		this.answerList = answerList;
		this.list = list;
	}

	public AnswerSelectAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.answer_select_item, null);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.answer_select_item_linearLayout);
			holder.text = (TextView) convertView
					.findViewById(R.id.answer_select_item_textView);
			holder.img = (TextView) convertView
					.findViewById(R.id.answer_select_item_textImg);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(list.get(position));
		holder.img.setText(letterArr[position]);
		holder.linearLayout
				.setBackgroundResource(R.drawable.answer_select_item_style);

		if (status == 1) {
			for (int i = 0; i < answerList.size(); i++) {
				if (answerList.get(i).equals(list.get(position))) {
					holder.text.setTextColor(Color.rgb(53, 207, 143));
				}
			}

			for (Entry<Integer, String> m : checkMap.entrySet()) {
				if (m.getKey() == position) {
					boolean flag = false;
					for (int i = 0; i < answerList.size(); i++) {
						if (m.getValue().equals(answerList.get(i))) {
							flag = true;
						}
					}
					if (flag) {
						holder.text.setTextColor(Color.rgb(53, 207, 143));
					} else {
						holder.text.setTextColor(Color.rgb(227, 20, 39));
					}
					holder.linearLayout
							.setBackgroundResource(R.drawable.answer_select_item_check_style);
				}
			}
		}

		if (type == 1) {
			for (int i = 0; i < answerList.size(); i++) {
				if (answerList.get(i).equals(list.get(position))) {
					holder.linearLayout
							.setBackgroundResource(R.drawable.answer_select_item_check_style);
				}
			}
		}

		AnswerSelectActivity.asipList.add(new AnswerSelectItemPojo(
				holder.linearLayout, 0));

		return convertView;
	}

	public void setOptionList(List<String> list) {
		this.list = list;
	}

	public void setOptionAndAnswerList(int type, List<String> list,
			List<String> answer) {
		this.type = type;
		this.list = list;
		this.answerList = answer;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCheckMap(Map<Integer, String> checkMap) {
		this.checkMap = checkMap;
	}

	public final class ViewHolder {
		public TextView text;
		public TextView img;
		public LinearLayout linearLayout;
	}

}
