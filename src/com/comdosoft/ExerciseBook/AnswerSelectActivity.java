package com.comdosoft.ExerciseBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.comdosoft.ExerciseBook.pojo.AnswerBasePojo;
import com.comdosoft.ExerciseBook.pojo.AnswerSelectItemPojo;
import com.comdosoft.ExerciseBook.tools.AnswerTools;

/** 
* @作者 马龙 
* @时间 2014-4-11 下午6:16:25 
*/ 
public class AnswerSelectActivity extends AnswerBaseActivity implements
		OnItemClickListener, OnClickListener, OnPreparedListener {

	// private String json =
	// "{  \"selecting\": {\"specified_time\": \"100\", \"question_types\": \"6\", \"questions\": [{\"id\": \"284\",\"branch_questions\": [ {\"id\": \"181\", \"content\": \"This is ___ apple!\", \"option\": \"a;||;an\", \"answer\": \"an;||;a\" },{\"id\": \"181\", \"content\": \"<file>apple.jpg</file>Why he is ___ Google!\", \"option\": \"apple;||;banana;||;orange;||;pear\", \"answer\": \"apple;||;banana\"},{\"id\": \"181\", \"content\": \"<file>apple.mp3</file>\", \"option\": \"one;||;two;||;three\", \"answer\": \"two\"}, {\"id\": \"181\", \"content\": \"<file>apple.jpg</file>Pears have white flesh and thin green or yellow skin.\", \"option\": \"iPhone;||;S5;||;Xperia\", \"answer\": \"iPhone\"},{\"id\": \"181\", \"content\": \"Dad.come set here!\", \"option\": \"ZhangDaCa;||;ChenLong\", \"answer\": \"ZhangDaCa\"}]}]}}";
	private boolean playFlag = false;
	public boolean truePropFlag = false;
	private StringBuffer mAnswer = new StringBuffer();
	private List<String> answerOption = new ArrayList<String>();
	private Map<Integer, String> checkMap = new HashMap<Integer, String>();
	private ListView listView;
	private AnswerSelectAdapter selectAdapter;
	public static List<AnswerSelectItemPojo> asipList = new ArrayList<AnswerSelectItemPojo>();
	private ImageView answerImg;
	private ImageView answerLaba;
	private TextView answerText;
	private LinearLayout answer_select_answerLinearLayout;
	private MediaPlayer mediaPlayer = new MediaPlayer();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.answer_select);
		findViewById(R.id.base_back_linearlayout).setOnClickListener(this);
		findViewById(R.id.base_check_linearlayout).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.answer_select_listview);
		answerImg = (ImageView) findViewById(R.id.answer_select_img);
		answerLaba = (ImageView) findViewById(R.id.answer_select_laba);
		answerLaba.setOnClickListener(this);
		answerText = (TextView) findViewById(R.id.answer_select_text);
		answer_select_answerLinearLayout = (LinearLayout) findViewById(R.id.answer_select_answerLinearLayout);

		setQuestionType(3);

		analysisJson(json);

		answerOption = mQuestList.get(mQindex).get(mBindex).getOption();
		selectAdapter = new AnswerSelectAdapter(this, answerOption);
		if (status != 2) {
			listView.setOnItemClickListener(this);
		}
		listView.setDividerHeight(10);
		updateView();
	}

	// 设置选择类型
	public void setSelectType(int i, String content) {
		switch (i) {
		// 听力
		case 0:
			answer_select_answerLinearLayout.setVisibility(View.GONE);
			answerLaba.setVisibility(View.VISIBLE);
			break;
		// 文本
		case 1:
			answerText.setText(content);
			answer_select_answerLinearLayout.setVisibility(View.VISIBLE);
			answerImg.setVisibility(View.GONE);
			answerLaba.setVisibility(View.GONE);
			break;
		// 图片
		case 2:
			Log.i("Ax", "img" + mQuestList.get(mQindex).get(mBindex).getPath());
			answerText.setText(content);
			answerImg.setImageDrawable(Drawable.createFromPath(mQuestList
					.get(mQindex).get(mBindex).getPath()));
			answer_select_answerLinearLayout.setVisibility(View.VISIBLE);
			answerImg.setVisibility(View.VISIBLE);
			answerLaba.setVisibility(View.GONE);
			break;
		}
	}

	// 判断正确
	public int check() {
		int count = 0;
		List<String> arr = mQuestList.get(mQindex).get(mBindex).getAnswer();
		Iterator<Entry<Integer, String>> it = checkMap.entrySet().iterator();
		while (it.hasNext()) {
			String answer = it.next().getValue();
			mAnswer.append(answer).append(";||;");
			for (int j = 0; j < arr.size(); j++) {
				if (answer.equals(arr.get(j))) {
					count++;
				}
			}
		}
		mAnswer.delete(mAnswer.length() - 4, mAnswer.length());
		if (count == arr.size() && checkMap.size() == 1 && arr.size() == 1) {
			return 0;
		} else if (count == arr.size() && checkMap.size() > 1 && arr.size() > 1
				&& checkMap.size() == arr.size()) {
			return 0;
		} else if (count < arr.size() && arr.size() > 1) {
			return 1;
		} else if (count > arr.size()) {
			return 2;
		}
		return 2;
	}

	public void rightAnswer() {
		if (status == 0) {
			AnswerBasePojo sp = mQuestList.get(mQindex).get(mBindex);
			List<String> option = sp.getOption();
			List<String> answer = sp.getAnswer();
			for (int i = 0; i < option.size(); i++) {
				boolean flag = false;
				AnswerSelectItemPojo asip = asipList.get(i);
				for (int j = 0; j < answer.size(); j++) {
					if (option.get(i).equals(answer.get(j))) {
						flag = true;
					}
				}
				if (flag) {
					asip.getLinearLayout().setBackgroundResource(
							R.drawable.answer_select_item_check_style);
					asip.setStatus(1);
					checkMap.put(i, answerOption.get(i));
				} else {
					asip.getLinearLayout().setBackgroundResource(
							R.drawable.answer_select_item_style);
					asip.setStatus(0);
					checkMap.remove(i);
				}
			}
		}
	}

	@Override
	public void updateView() {
		super.updateView();
		truePropFlag = false;

		if (status == 0 && eb.getTrue_number() > 0) {
			setTruePropShow();
		}

		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}

		playFlag = false;
		asipList.clear();
		setPage(mBindex + 1, mQuestList.get(mQindex).size());
		mAnswer.delete(0, mAnswer.length());
		AnswerBasePojo sp = mQuestList.get(mQindex).get(mBindex);
		answerOption = sp.getOption();

		if (status > 1) {
			selectAdapter.setOptionAndAnswerList(1, sp.getOption(),
					sp.getAnswer());
		} else {
			selectAdapter.setOptionList(answerOption);
		}

		selectAdapter.setStatus(0);
		selectAdapter.notifyDataSetChanged();
		listView.setAdapter(selectAdapter);
		setSelectType(sp.getType(), sp.getContent());
		checkMap.clear();
	}

	public void showSelectAnswer() {
		AnswerBasePojo sp = mQuestList.get(mQindex).get(mBindex);
		selectAdapter.setCheckMap(checkMap);
		selectAdapter.setStatus(1);
		selectAdapter.setOptionAndAnswerList(0, sp.getOption(), sp.getAnswer());
		selectAdapter.notifyDataSetChanged();
		listView.setAdapter(selectAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_check_linearlayout:
			if (status != 2) {
				if (getCheckText().equals("检查")) {
					if (checkMap.size() == 0) {
						Toast.makeText(getApplicationContext(), "请选择答案!", 0)
								.show();
					} else {
						setPause();
						if (mQindex == mQuestList.size() - 1
								&& mBindex == mQuestList.get(mQindex).size() - 1) {
							setCheckText("完成");
						} else {
							setCheckText("下一题");
						}
						switch (check()) {
						case 0:
							ratio = 100;
							MyPlayer(true);
							break;
						case 1:
							MyPlayer(false);
							break;
						case 2:
							MyPlayer(false);
							break;
						}
						showSelectAnswer();
					}
				} else {
					setStart();
					if (mQindex == mQuestList.size() - 1
							&& mBindex == mQuestList.get(mQindex).size() - 1) {
						setCheckText("完成");
					} else {
						setCheckText("检查");
					}
					if (status == 0) {
						AnswerBasePojo aop = mQuestList.get(mQindex).get(
								mBindex);
						saveAnswerJson(mAnswer.toString(), ratio,
								aop.getQuestions_id(),
								aop.getBranch_questions_id());
					} else {
						calculateIndexAndUpdateView();
					}
				}
			} else {
				calculateIndexAndUpdateView();
				nextRecord();
			}
			break;
		case R.id.base_back_linearlayout:
			super.onClick(v);
			break;
		case R.id.answer_select_laba:
			if (!playFlag) {
				new MyMediaPlay().start();
				playFlag = true;
			} else if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			} else {
				mediaPlayer.start();
			}
			break;
		case R.id.base_propTime:
			super.onClick(v);
			break;
		case R.id.base_propTrue:
			if (eb.getTrue_number() > 0) {
				rightAnswer();
				if (!truePropFlag) {
					truePropFlag = true;
					setTruePropEnd();
					rightAnswer();
					PropJson(1, mQuestList.get(mQindex).get(mBindex)
							.getBranch_questions_id());
				}
			} else {
				setTruePropEnd();
				Toast.makeText(getApplicationContext(), "道具数量不足!", 0).show();
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		AnswerSelectItemPojo asip = asipList.get(arg2);
		if (asip.getStatus() == 0) {
			asip.getLinearLayout().setBackgroundResource(
					R.drawable.answer_select_item_check_style);
			asip.setStatus(1);
			asipList.set(arg2, asip);
			checkMap.put(arg2, answerOption.get(arg2));
		} else {
			asip.getLinearLayout().setBackgroundResource(
					R.drawable.answer_select_item_style);
			asip.setStatus(0);
			asipList.set(arg2, asip);
			checkMap.remove(arg2);
		}
	}

	// 播放音频
	public void playerAmr() {
		try {
			Log.i("Ax", "mp3:" + mQuestList.get(mQindex).get(mBindex).getPath());
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mQuestList.get(mQindex).get(mBindex)
					.getPath());
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	class MyMediaPlay extends Thread {
		@Override
		public void run() {
			super.run();
			playerAmr();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	// 销毁音频
	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

	// 停止音频
	protected void onStop() {
		if (mediaPlayer.isPlaying()) {// 正在播放
			mediaPlayer.stop();// 停止
		}
		super.onStop();
	}

	// 解析选择题JSON
	public void analysisJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			specified_time = jsonObject.getInt("specified_time");
			JSONArray jArr = new JSONArray(jsonObject.getString("questions"));
			for (int i = 0; i < jArr.length(); i++) {
				List<AnswerBasePojo> list = new ArrayList<AnswerBasePojo>();
				JSONObject jo = jArr.getJSONObject(i);
				int questions_id = jo.getInt("id");
				JSONArray jsonArr = new JSONArray(
						jo.getString("branch_questions"));
				for (int j = 0; j < jsonArr.length(); j++) {
					JSONObject jb = jsonArr.getJSONObject(j);
					String s = jb.getString("content");
					int branch_questions_id = jb.getInt("id");
					int type = AnswerTools.getSelectType(s);
					String content = AnswerTools.getSelectContent(s);
					List<String> arr = AnswerTools.getSelectOption(jb
							.getString("options"));
					List<String> answer = AnswerTools.getSelectAnswer(jb
							.getString("answer"));

					if (type != 1) {
						String path = AnswerTools.getSelectPath(s);
						String fileName = AnswerTools.getSelectFileName(path);

						list.add(new AnswerBasePojo(questions_id,
								branch_questions_id, type, content, eb
										.getPath() + "/" + fileName, arr,
								answer));
					} else {
						list.add(new AnswerBasePojo(questions_id,
								branch_questions_id, type, content, arr, answer));
					}
				}
				mQuestList.add(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
