package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.pojo.ClassPojo;
import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;


/**
 * @作者 丁作强
 * @时间 2014-4-23 上午9:43:21
 */
public class CardTypeActivity extends Activity {
	private LinearLayout layout;
	private ListView switchclassLv;
	private ExerciseBook exerciseBook;
	// TYPES_NAME = {0 => "听力", 1 => "朗读", 2 => "十速挑战", 3 => "选择", 4 => "连线", 5
	// => "完型填空", 6 => "排序"}
	private static final String[] m={"默认","听写","朗读","十速","选择","连线","完型","排序"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.card_type);

		exerciseBook = (ExerciseBook) getApplication();
		exerciseBook.getActivityList().add(this);
		switchclassLv = (ListView) findViewById(R.id.switchclassLv);
		layout = (LinearLayout) findViewById(R.id.switchclass);
		switchclassLv.setAdapter(new CardAdapter(
				getApplication(), m));
		switchclassLv.setDivider(null);
	}


	// 点击空白处 本界面消失
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				switchclassLv.setAdapter(new CardAdapter(
						getApplication(), m));
				switchclassLv.setDivider(null);
				break;
			case 1:
				Toast.makeText(CardTypeActivity.this, msg.obj.toString(), 1)
						.show();
				break;
			}
		}
	};

	// 自定义 适配器
	public class CardAdapter extends BaseAdapter {
		private String[] m1;
		private Context context;

		public CardAdapter(Context context, String[] m1) {
			this.context = context;
			this.m1 = m1;
		}

		public int getCount() {
			return m1.length;
		}

		public Object getItem(int position) {
			return m1[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			TextView scTv = null;
			ImageView class_dangqian = null;
			convertView = inflater.inflate(R.layout.switchclass_one, null);
			class_dangqian = (ImageView) convertView
					.findViewById(R.id.dangqian_class);
			
			scTv = (TextView) convertView.findViewById(R.id.switchclass_oneTv);

			scTv.setText(m1[position]);
			scTv.setGravity(Gravity.CENTER);
			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new Thread() {
						public void run() {
							Intent intent2 = new Intent();

							intent2.putExtra("cardtype",m1[position]);
							// 通过调用setResult方法返回结果给前一个activity。
							CardTypeActivity.this.setResult(-10, intent2);
							CardTypeActivity.this.finish();
						}
					}.start();
				}
			});

			return convertView;
		}
	}

	

}
