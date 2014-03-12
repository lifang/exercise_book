package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.comdosoft.ExerciseBook.pojo.AnswerSelectItemPojo;

public class AnswerSelectAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<String> list = new ArrayList<String>();
	private String[] letterArr = new String[] { "A", "B", "C", "D", "E", "F" };

	public AnswerSelectAdapter(Context context, List<String> list) {
		super();
		this.mInflater = LayoutInflater.from(context);
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
		AnswerSelectActivity.asipList.add(new AnswerSelectItemPojo(
				holder.linearLayout, 0));

		return convertView;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public final class ViewHolder {
		public TextView text;
		public TextView img;
		public LinearLayout linearLayout;
	}

}
