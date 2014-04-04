package com.comdosoft.ExerciseBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;
import com.comdosoft.ExerciseBook.pojo.AnswerWirePojo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

//2014年4月1日 10:45:06
public class AnswerWireActivity extends AnswerBaseActivity {

	private int coordinateIndex = 0;
	private int last = -1;
	private int height = 0;
	// private String JSON =
	// "{    \"lining\":{\"specified_time\": \"100\",  \"questions\":[ {\"id\": \"284\",  \"branch_questions\": [{\"id\": \"181\", \"content\": \"This is<=>an apple;||;A<=>B;||;ZhangDaCa<=>Dog\"}]},{\"id\": \"285\", \"branch_questions\": [{\"id\": \"182\", \"content\": \"C<=>D;||;Chen<=>Long;||;Gao<=>Shi\"}]}, {\"id\": \"285\", \"branch_questions\": [ {\"id\": \"182\", \"content\": \"Ma<=>Long;||;123<=>456;||;1111<=>2222\"} ]},  {\"id\": \"291\",\"branch_questions\": [ {\"id\": \"182\", \"content\": \"ZhangDaCa<=>ZXN;||;ChenLong<=>CL;||;MaLong<=>ML\"}]}] }}";
	private StringBuffer sb = new StringBuffer();

	private LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);
	private LinearLayout leftLinearLayout;
	private LinearLayout rightLinearLayout;
	private ImageView imgCanvas;

	// 存储题目选项对象
	private ArrayList<AnswerWirePojo> wireList = new ArrayList<AnswerWirePojo>();
	// 题目默认排序
	private List<String> answerList = new ArrayList<String>();
	// 题目随机排序
	private List<String> orderAnswerList = new ArrayList<String>();
	// 存储用户连线索引
	private List<Integer[]> coordinate = new ArrayList<Integer[]>();
	// 存储正确答案索引
	private List<Integer[]> trueList = new ArrayList<Integer[]>();
	// 存储坐标
	private int[][] xyArr = new int[][] { { 0, 140 }, { 273, 140 }, { 0, 470 },
			{ 273, 470 }, { 0, 800 }, { 273, 800 } };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.answer_wire);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(
				new MyClick());
		leftLinearLayout = (LinearLayout) findViewById(R.id.answer_wireLeft);
		rightLinearLayout = (LinearLayout) findViewById(R.id.answer_wireRight);
		imgCanvas = (ImageView) findViewById(R.id.answer_wire_canvas);
		lp.topMargin = 50;

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		height = displayMetrics.heightPixels;
		if (height == 1205 || height == 1216) {
			lp.topMargin = 30;
			xyArr = new int[][] { { 0, 100 }, { 200, 100 }, { 0, 330 },
					{ 200, 330 }, { 0, 560 }, { 200, 560 } };
		}

		setQuestionType(4);

		analysisJSON(json);

		updateView();
	}

	public void updateView() {
		answerList.clear();
		orderAnswerList.clear();
		coordinate.clear();
		wireList.clear();
		last = -1;
		leftLinearLayout.removeAllViews();
		rightLinearLayout.removeAllViews();

		initData();

		for (int i = 0; i < answerList.size(); i += 2) {
			initLeftView(i);
			initRightView(i + 1);
		}

		if (status == 2) {
			coordinate = trueList;
			for (int j = 0; j < answerList.size(); j++) {
				setCheckStatusForIndex(j);
			}
		}

		imgCanvas.setImageBitmap(drawView());
	}

	public void analysisJSON(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			specified_time = jsonObject.getInt("specified_time");
			JSONArray jArr = new JSONArray(jsonObject.getString("questions"));
			for (int i = 0; i < jArr.length(); i++) {
				List<AnswerBasePojo> list = new ArrayList<AnswerBasePojo>();
				JSONObject jo = jArr.getJSONObject(i);
				int question_id = jo.getInt("id");
				JSONArray jsonArr = new JSONArray(
						jo.getString("branch_questions"));
				for (int j = 0; j < jsonArr.length(); j++) {
					JSONObject jb = jsonArr.getJSONObject(j);
					int branch_question_id = jb.getInt("id");
					String answer = jb.getString("content");
					list.add(new AnswerBasePojo(question_id,
							branch_question_id, answer));
				}
				mQuestList.add(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 初始化数据
	public void initData() {
		String[] arr = mQuestList.get(mQindex).get(mBindex).getContent()
				.split(";\\|\\|;");

		for (int i = 0; i < arr.length; i++) {
			answerList.add(arr[i].split("<=>")[0]);
			answerList.add(arr[i].split("<=>")[1]);
		}

		List<String> left = new ArrayList<String>();
		List<String> right = new ArrayList<String>();

		List<String> orderLeft = new ArrayList<String>();
		List<String> orderRight = new ArrayList<String>();

		for (int i = 0; i < answerList.size(); i++) {
			if (i % 2 == 0) {
				left.add(answerList.get(i));
			} else {
				right.add(answerList.get(i));
			}
		}

		Random r = new Random(System.currentTimeMillis());

		for (int i = 0; i < left.size(); i++) {
			int index = r.nextInt(left.size());
			orderLeft.add(left.get(index));
			left.remove(index);
			i = i - 1;
		}

		for (int i = 0; i < right.size(); i++) {
			int index = r.nextInt(right.size());
			orderRight.add(right.get(index));
			right.remove(index);
			i = i - 1;
		}

		for (int i = 0; i < answerList.size() / 2; i++) {
			orderAnswerList.add(orderLeft.get(i));
			orderAnswerList.add(orderRight.get(i));
		}

		for (int i = 0; i < orderAnswerList.size(); i += 2) {
			int leftIndex = 0;
			for (int j = 0; j < orderAnswerList.size(); j++) {
				if (answerList.get(i).equals(orderAnswerList.get(j))) {
					leftIndex = j;
				}
			}

			int index = 0;
			for (int j = 0; j < orderAnswerList.size(); j++) {
				if (answerList.get(i + 1).equals(orderAnswerList.get(j))) {
					index = j;
				}
			}
			trueList.add(new Integer[] { leftIndex, index });
		}
	}

	public void initLeftView(int i) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		LinearLayout left = (LinearLayout) inflater.inflate(
				R.layout.answer_wire_item, null);
		TextView leftText = (TextView) left
				.findViewById(R.id.answer_wire_item_text);
		leftText.setOnClickListener(new MyClick(i));
		leftText.setText(orderAnswerList.get(i));
		left.setLayoutParams(lp);
		if (status == 2) {
			left.setClickable(false);
		}
		leftLinearLayout.addView(left);
		wireList.add(new AnswerWirePojo(leftText, 0));
	}

	public void initRightView(int i) {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		LinearLayout right = (LinearLayout) inflater.inflate(
				R.layout.answer_wire_item, null);
		TextView rightText = (TextView) right
				.findViewById(R.id.answer_wire_item_text);
		rightText.setOnClickListener(new MyClick(i));
		rightText.setText(orderAnswerList.get(i));
		right.setLayoutParams(lp);
		if (status == 2) {
			right.setClickable(false);
		}
		rightLinearLayout.addView(right);
		wireList.add(new AnswerWirePojo(rightText, 0));
	}

	public Bitmap drawView() {
		Bitmap bitmap;
		if (height == 1205 || height == 1216) {
			bitmap = Bitmap.createBitmap(200, 660, Config.ARGB_8888);
		} else {
			bitmap = Bitmap.createBitmap(273, 940, Config.ARGB_8888);
		}
		Canvas canvas = new Canvas(bitmap);
		canvas.drawARGB(0, 0, 0, 0);
		Paint paint = new Paint();
		paint.setFakeBoldText(true);
		paint.setAntiAlias(true);
		paint.setColor(Color.rgb(53, 207, 143));
		paint.setStrokeWidth(4);
		for (int i = 0; i < coordinate.size(); i++) {
			int last = coordinate.get(i)[0];
			int index = coordinate.get(i)[1];
			canvas.drawLine(xyArr[last][0], xyArr[last][1], xyArr[index][0],
					xyArr[index][1], paint);
		}
		return bitmap;
	}

	public void check(int type) {
		List<Integer[]> intList = new ArrayList<Integer[]>();
		intList = trueList;
		int count = 0;
		if (type == 0) {
			for (int i = 0; i < coordinate.size(); i++) {
				int left = coordinate.get(i)[0];
				int right = coordinate.get(i)[1];
				String answer = orderAnswerList.get(left)
						+ orderAnswerList.get(right);

				for (int j = 0; j < answerList.size(); j += 2) {
					if ((answerList.get(j) + answerList.get(j + 1))
							.equals(answer)) {
						count++;
					}
				}

				sb.append(orderAnswerList.get(left)).append("<=>")
						.append(orderAnswerList.get(right)).append(";||;");

				for (int j = 0; j < intList.size(); j++) {
					if (left == intList.get(j)[0] && right == intList.get(j)[1]) {
						intList.remove(j);
					}
				}
			}

			if (count == coordinate.size()) {
				ratio = 100;
				MyPlayer(true);
			} else {
				MyPlayer(false);
			}
		}

		if (sb.length() > 0) {
			sb.delete(sb.length() - 4, sb.length());
		}

		if (type == 1) {
			// 使用道具
			if (coordinateIndex < answerList.size() / 2) {
				Integer[] arr = intList.get(coordinateIndex++);
				setCheckStatusForIndex(arr[0]);
				setCheckStatusForIndex(arr[1]);
				calculateCoordinate(arr);
				coordinate.add(arr);
				imgCanvas.setImageBitmap(drawView());
				count++;
			}
		}
	}

	public void calculateCoordinate(Integer[] arr) {
		for (int i = 0; i < coordinate.size(); i++) {
			if (coordinate.get(i)[0] == arr[0]
					|| coordinate.get(i)[1] == arr[1]) {
				coordinate.remove(i);
				i++;
			}
		}
	}

	public void setCancelStatusForIndex(int index) {
		if (index >= 0) {
			AnswerWirePojo indexAwp = wireList.get(index);
			indexAwp.getTv().setBackgroundResource(
					R.drawable.answer_wire_item_style);
		}
	}

	public void setCheckStatusForIndex(int index) {
		if (index >= 0) {
			AnswerWirePojo indexAwp = wireList.get(index);
			indexAwp.getTv().setBackgroundResource(
					R.drawable.answer_wire_item_check_style);
		}
	}

	@Override
	public void rightAnswer() {
		check(1);
	}

	class MyClick implements OnClickListener {
		private int index;

		public MyClick() {
		}

		public MyClick(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.answer_wire_item_text:

				AnswerWirePojo indexAwp = wireList.get(index);
				indexAwp.getTv().setBackgroundResource(
						R.drawable.answer_wire_item_check_style);

				// 暂时保留
				if (index % 2 != 0 && last % 2 != 0) {
					// if (last != -1) {
					boolean lastFlag = false;
					boolean indexFlag = false;
					for (int i = 0; i < coordinate.size(); i++) {
						if (last == coordinate.get(i)[1]) {
							lastFlag = true;
						} else if (index == coordinate.get(i)[1]) {
							indexFlag = true;
						}
					}
					if (lastFlag && !indexFlag) {
						setCancelStatusForIndex(index);
					} else if (indexFlag && !lastFlag) {
						setCancelStatusForIndex(last);
					} else if (lastFlag && indexFlag) {

					} else {
						setCancelStatus();
					}
					last = -1;
					// } else {
					// setCancelStatusForIndex(index);
					// }
				}

				if (last % 2 == 0 && index % 2 == 0) {
					boolean lastFlag = false;
					boolean indexFlag = false;
					for (int i = 0; i < coordinate.size(); i++) {
						if (last == coordinate.get(i)[0]) {
							lastFlag = true;
						} else if (index == coordinate.get(i)[0]) {
							indexFlag = true;
						}
					}
					if (lastFlag && !indexFlag) {
						setCancelStatusForIndex(index);
					} else if (indexFlag && !lastFlag) {
						setCancelStatusForIndex(last);
					} else if (lastFlag && indexFlag) {

					} else {
						setCancelStatus();
					}
					last = -1;
				} else {
					if (last == -1) {
						last = index;
					}
					if (last % 2 == 0 && index % 2 != 0 || last % 2 != 0
							&& index % 2 == 0) {
						wired();
					} else {
						last = index;
					}
				}
				// if (index % 2 != 0) {
				// last = -1;
				// }
				break;
			case R.id.base_check_linearlayout:
				if (status != 2) {
					if (coordinate.size() == answerList.size() / 2) {
						coordinateIndex = 0;
						check(0);
						AnswerBasePojo aop = mQuestList.get(mQindex).get(
								mBindex);
						saveAnswerJson(sb.toString(), ratio,
								aop.getQuestions_id(),
								aop.getBranch_questions_id());
						sb.delete(0, sb.length());
					} else {
						Toast.makeText(getApplicationContext(), "请连接未完成的题", 0)
								.show();
					}
				} else {
					nextRecord();
					calculateIndexAndUpdateView();
				}
				break;
			}
		}

		public void wired() {
			boolean flag = false;
			boolean a = false;
			boolean b = false;
			for (int i = 0; i < coordinate.size(); i++) {
				if (coordinate.get(i)[0] == last
						&& coordinate.get(i)[1] == index
						|| (coordinate.get(i)[1] == last && coordinate.get(i)[0] == index)) {
					setCancelStatus();
					coordinate.remove(i);
					i = i - 1;
					flag = true;
				} else if (coordinate.get(i)[0] == last
						|| (coordinate.get(i)[1] == last)) {
					flag = true;
					a = true;
					setCancelStatusForIndex(index);
				} else if (coordinate.get(i)[0] == index
						|| coordinate.get(i)[1] == index) {
					flag = true;
					b = true;
					setCancelStatusForIndex(last);
				}
			}

			if (!flag) {
				setCheckStatus();
				if (last % 2 == 0) {
					coordinate.add(new Integer[] { last, index });
				} else {
					coordinate.add(new Integer[] { index, last });
				}
			}

			if (a && b) {
				setCheckStatus();
			}

			imgCanvas.setImageBitmap(drawView());
			last = -1;
		}

		public void setCheckStatus() {
			setCheckStatusForIndex(index);
			setCheckStatusForIndex(last);
		}

		public void setCancelStatus() {
			setCancelStatusForIndex(index);
			if (last != -1) {
				setCancelStatusForIndex(last);
			}
		}
	}
}
