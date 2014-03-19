package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.comdosoft.ExerciseBook.pojo.AnswerSelectItemPojo;

public class AnswerSelectAdapter extends BaseAdapter {

	private int type = 0;
	private LayoutInflater mInflater;
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

	public final class ViewHolder {
		public TextView text;
		public TextView img;
		public LinearLayout linearLayout;
	}

}
