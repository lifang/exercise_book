package com.comdosoft.ExerciseBook;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MyReceiver extends BroadcastReceiver implements Urlinterface {
	private int type;
	private int school_class_id;
	private String school_class_name;
	private String student_id; // json 中的
	private String id; // 本地存的

	ExerciseBook eb;

	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		eb = (ExerciseBook) context.getApplicationContext();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d("bbb", "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d("bbb",
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			
				Log.d("bbb", "[MyReceiver] 接收到推送下来的通知");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d("Max", "[MyReceiver] 用户点击打开了通知");

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d("bbb",
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			Log.d("Max",
					"[MyReceiver] Unhandled intent - " + intent.getAction());
		}

		//

	}

	private void cleardate(Context context) {
		SharedPreferences preferences0 = context.getSharedPreferences(SHARED,
				Context.MODE_PRIVATE);
		Editor editor0 = preferences0.edit();
		editor0.putString("user_id", "");
		editor0.putString("student_id", "");
		editor0.putString("school_class_id", "");
		editor0.putString("id", "");
		editor0.commit();

	}

	// 关闭上个主界面
	public void clearActivity() {
		List<Activity> activityList = eb.getActivityList();
		for (int i = 0; i < activityList.size(); i++) {
			activityList.get(i).finish();
		}
		eb.setActivityList();
	}
}