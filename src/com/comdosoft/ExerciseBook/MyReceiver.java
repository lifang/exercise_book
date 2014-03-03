package com.comdosoft.ExerciseBook;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent) {
		 Bundle bundle = intent.getExtras();
		 if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
	            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
	            Log.d("bbb", "[MyReceiver] 接收Registration Id : " + regId);
	            //send the Registration Id to your server...
	                        
	        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
	        	Log.d("bbb", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
	        
	        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
	            Log.d("bbb", "[MyReceiver] 接收到推送下来的通知");
	            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
	            Log.d("bbb", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
	        	
	        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
	            Log.d("bbb", "[MyReceiver] 用户点击打开了通知");
	            
	        	//打开自定义的Activity
		        try {
		        	String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		        	String content = bundle.getString(JPushInterface.EXTRA_ALERT);
		        	String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		        	Log.i("aa", "Title : " + title + "  " + "Content : " + content+"fudai:"+extras);
					JSONObject jsonobject=new JSONObject(extras);
					String type=jsonobject.getString("type");
					switch (Integer.valueOf(type)) {
					case 0:
						Intent intent0=new Intent(context,HomeWorkIngActivity.class);
						intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						context.startActivity(intent0);
						break;
					case 1:
						Intent intent2=new Intent(context,HomePageMainActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent2);
						break;
					case 2:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
	        	
	        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
	            Log.d("bbb", "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
	            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
	        	
	        } else {
	        	Log.d("bbb", "[MyReceiver] Unhandled intent - " + intent.getAction());
	        }
	}
}