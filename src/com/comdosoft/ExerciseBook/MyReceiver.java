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
import cn.jpush.android.api.JPushInterface;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class MyReceiver extends BroadcastReceiver implements Urlinterface
{
	private int type;
	private int school_class_id;
	private String school_class_name;
	ExerciseBook eb;
	public void onReceive(Context context, Intent intent) {
		 Bundle bundle = intent.getExtras();
		 eb =  (ExerciseBook) context.getApplicationContext();
		 if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
	            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
	            Log.d("bbb", "[MyReceiver] 接收Registration Id : " + regId);
	            //send the Registration Id to your server...
	                        
	        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
	        	Log.d("bbb", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
	        
	        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
	        	
				try {
					Log.d("bbb", "[MyReceiver] 接收到推送下来的通知");
					int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
					Log.d("bbb", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
					String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
					String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		        	String content = bundle.getString(JPushInterface.EXTRA_ALERT);
					SharedPreferences sharedPreferences = context.getSharedPreferences("replyMenu", 0);
					Editor editor = sharedPreferences.edit();//获取编辑器
					JSONObject jsonobject = new JSONObject(extras);
					type=jsonobject.getInt("type");
					int class_id =jsonobject.getInt("class_id");
					switch (type) {
					case 0:
//						editor.putBoolean("ReplyMenu", false);
						editor.putString(class_id+"ReplyMenu", "exist");
						break;
					case 1:
//						editor.putBoolean("ReplyMenu", false);
						editor.putString(class_id+"ReplyMenu", "exist");
						break;
					case 2:
//						editor.putBoolean("HomeWorkMenu", false);
						editor.putString(class_id+"HomeWorkMenu", "exist");
						break;
					}
					editor.commit();//提交修改
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
	            Log.d("bbb", "[MyReceiver] 用户点击打开了通知");
	        	//打开自定义的Activity
		        	String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		        	String content = bundle.getString(JPushInterface.EXTRA_ALERT);
		        	String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		        	Log.i("bbb", "Title : " + title + "  " + "Content : " + content+"fudai:"+extras);
//		       Title : 超级作业本  Content : ding：888888fudai:{"type":1,"class_id":107}
		        	JSONObject jsonobject;
					try {
						jsonobject = new JSONObject(extras);
						type=jsonobject.getInt("type");
						school_class_id =jsonobject.getInt("class_id");
						school_class_name=jsonobject.getString("class_name");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
						
					
		        	SharedPreferences preferences = context.getSharedPreferences(SHARED,
							Context.MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("school_class_id", school_class_id+"");
					editor.putString("school_class_name", school_class_name);
					editor.commit();
					SharedPreferences sharedPreferences = context.getSharedPreferences("replyMenu", 0);
					clearActivity();
					switch (type) {
					case 0:
						if (1==1) {
						Editor editor0 = sharedPreferences.edit();//获取编辑器
						editor0.putString(school_class_id + "ReplyMenu", "none");
						editor0.commit();
						Intent intent0=new Intent(context,MessageActivity.class);
						intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						context.startActivity(intent0);
						}else {
							SharedPreferences preferences0 = context.getSharedPreferences(SHARED,
									Context.MODE_PRIVATE);
							Editor editor0 = preferences.edit();
							editor0.putString("user_id", "");
							editor0.putString("school_class_id", "");
							editor0.putString("id", "");
							editor0.commit();

							Intent intent4 = new Intent(context, LoginActivity.class);
							context.startActivity(intent4);
						}
						break;
					case 1:
						Editor editor1 = sharedPreferences.edit();//获取编辑器
						editor1.putString(school_class_id + "ReplyMenu", "none");
						editor1.commit();
						Intent intent2=new Intent(context,ReplyListViewActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent2);
						break;
					case 2:
						Editor editor2 = sharedPreferences.edit();//获取编辑器
						editor2.putString(school_class_id + "HomeWorkMenu", "none");
						editor2.commit();
						Intent intent3=new Intent(context,HomeWorkIngActivity.class);
						intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent3);
						break;
					}
					
					
					
					
	        	
	        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
	            Log.d("bbb", "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
	            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
	        	
	        } else {
	        	Log.d("bbb", "[MyReceiver] Unhandled intent - " + intent.getAction());
	        }
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