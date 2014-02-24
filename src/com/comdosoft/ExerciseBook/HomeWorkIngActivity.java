package com.comdosoft.ExerciseBook;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class HomeWorkIngActivity extends Activity implements Urlinterface {

	private ExerciseBook eb;
	private LinearLayout homework_ing;
	private GridView gridview;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Builder builder = new Builder(HomeWorkIngActivity.this);
			builder.setTitle("提示");
			switch (msg.what) {
			case 1:
				break;
			case 2:
				break;
			}
		};
	};
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homework_ing);
		eb = (ExerciseBook) getApplication();// 初始化
		initialize();// 初始化

	}

	// 初始化
	public void initialize() {
		homework_ing = (LinearLayout) findViewById(R.id.homework_ing);
		gridview = (GridView)findViewById(R.id.gridview);
		gridview.setAdapter(new CityAdapter());
	}

	public class CityAdapter extends BaseAdapter {
		public int getCount() {
			return 7;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(
						R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}
			switch (position) {
			case 0:
				imageView.setBackgroundResource(R.drawable.img1);
				break;
			case 1:
				imageView.setBackgroundResource(R.drawable.img2);
				break;
			case 2:
				imageView.setBackgroundResource(R.drawable.img3);
				break;
			case 3:
				imageView.setBackgroundResource(R.drawable.img4);
				break;
			case 4:
				imageView.setBackgroundResource(R.drawable.img5);
				break;
			case 5:
				imageView.setBackgroundResource(R.drawable.img6);
				break;
			case 6:
				imageView.setBackgroundResource(R.drawable.img7);
				break;
			}
			return imageView;
		}
	}
}