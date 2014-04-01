package com.comdosoft.ExerciseBook;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver
{
	private int type;
	private int school_class_id;
	public void onReceive(Context context, Intent intent) {
		 Bundle bundle = intent.getExtras();
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
		        	Log.i("bbb", "Title : " + title + "  " + "Content : " + content+"fudai:"+extras);
					SharedPreferences sharedPreferences = context.getSharedPreferences("replyMenu", 0);
					Editor editor = sharedPreferences.edit();//获取编辑器
					JSONObject jsonobject = new JSONObject(extras);
					type=jsonobject.getInt("type");
					school_class_id =jsonobject.getInt("class_id");
					Log.i("asd",type+"<---tye");
					switch (type) {
					case 0:
						editor.putBoolean("ReplyMenu", false);
						Log.i("aa", "ReplyMenu=false");
						break;
					case 1:
						editor.putBoolean("ReplyMenu", false);
						Log.i("aa", "ReplyMenu=false");
						break;
					case 2:
						editor.putBoolean("HomeWorkMenu", false);
						Log.i("aa", "HomeWorkMenu=false");
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
					switch (type) {
					case 0:
						Intent intent0=new Intent(context,MessageActivity.class);
						intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						context.startActivity(intent0);
						break;
					case 1:
						Intent intent2=new Intent(context,ReplyListViewActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent2);
						break;
					case 2:
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
}