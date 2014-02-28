package com.comdosoft.ExerciseBook.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ExerciseBookTool implements Urlinterface {

	private static int connectTimeOut = 5000;
	private static int readTimeOut = 10000;
	private static String requestEncoding = "UTF-8";

	// 分割时间 带时分秒
	public static String divisionTime(String timeStr) {
		int temp1 = timeStr.indexOf("T");
		int temp2 = timeStr.lastIndexOf("+");
		return timeStr.substring(0, temp1) + " "
		+ timeStr.substring(temp1 + 1, temp2);
	}

	// asses拷贝文件到sd卡
	public static boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	// 判断网络
	public static boolean isConnect(Context context) {

		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {

				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();

				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}
		return false;
	}

	// 删除sharedprefs
	static public void del_Sharedprefs(String filename, String package_name) {
		File file = new File("/data/data/" + package_name.toString()
				+ "/shared_prefs", filename + ".xml");
		if (file.exists()) {
			file.delete();
		}
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// 判断SharedPreferences是否存在
	static public boolean IsSharedprefs(String package_name) {
		File file = new File("/data/data/" + package_name.toString()
				+ "/shared_prefs", SHARED + ".xml");
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	// 分割日期
	public static int[] getDateArray(String date) {
		int[] arr = new int[3];
		String[] strarr = date.split("-");
		for (int i = 0; i < strarr.length; i++) {
			arr[i] = Integer.valueOf(strarr[i]);
		}
		return arr;
	}

	// 判断是否存在集合中
	public static boolean getExist(String one, List<String> two) {
		for (int i = 0; i < two.size(); i++) {
			if (one.equals(two.get(i))) {
				return true;
			}
		}
		return false;
	}

	// 返回存在集合中的索引
	public static int getFidIndex(String one, List<String> two) {
		int s = 0;
		for (int i = 0; i < two.size(); i++) {
			if (one.equals(two.get(i))) {
				s = i;
			}
		}
		return s;
	}

	// 判断sd卡是否可用
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// get请求
	public static String sendGETRequest(String path, Map<String, String> map)
			throws Exception {
		String json = "error";
		StringBuilder url = new StringBuilder(path);
		url.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			url.append(entry.getKey()).append("=").append(entry.getValue());
			url.append("&");
		}
		url.deleteCharAt(url.length() - 1);
		Log.i("homework", url.toString());
		HttpURLConnection conn = (HttpURLConnection) new URL(url.toString())
		.openConnection();
		conn.setConnectTimeout(8000);
		conn.setReadTimeout(8000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream is = conn.getInputStream();
			byte[] data = readInputStream(is);
			is.close();
			json = new String(data);
		}
		Log.i("homework", json);
		return json;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public static InputStream getImageViewInputStream(String img_url)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(img_url); // 服务器地址
		if (url != null) {
			// 打开连接
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(3000);// 设置网络连接超时的时间为3秒
			httpURLConnection.setRequestMethod("GET"); // 设置请求方法为GET
			httpURLConnection.setDoInput(true); // 打开输入流
			int responseCode = httpURLConnection.getResponseCode(); // 获取服务器响应值
			if (responseCode == HttpURLConnection.HTTP_OK) { // 正常连接
				inputStream = httpURLConnection.getInputStream(); // 获取输入流
			}
		}
		return inputStream;
	}

	// 上传文件
	public static String sendPhostimg(String url, MultipartEntity entity) {
		String json = "";
		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response;
		Log.i("linshi", url + "");
		try {
			response = httpClient.execute(post);
			int stateCode = response.getStatusLine().getStatusCode();
			Log.i("linshi", stateCode + "");
			if (stateCode == HttpStatus.SC_OK) {
				HttpEntity result = response.getEntity();
				InputStream is = result.getContent();
				byte[] data = readInputStream(is);
				is.close();
				json = new String(data);
				return json;
			}
			Log.i("linshi", "退出上传");
			post.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String doPost(String reqUrl, Map parameters) {
		String tempLine = "";
		Log.i(tag, "doPost方法");
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator iter = parameters.entrySet().iterator(); iter
					.hasNext();) {
				Entry element = (Entry) iter.next();
				params.append(element.getKey().toString());
				params.append("=");
				params.append(URLEncoder.encode(element.getValue().toString(),
						ExerciseBookTool.requestEncoding));
				params.append("&");
			}
			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}
			URL url = new URL(reqUrl);
			Log.i("linshi", url.toString());
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			url_con.setConnectTimeout(connectTimeOut);
			url_con.setReadTimeout(readTimeOut);
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			tempLine = rd.readLine();
			rd.close();
			in.close();
		} catch (IOException e) {

		}
		Log.i("linshi", tempLine);
		return tempLine;
	}

	/**
	 * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static int getConnectTimeOut() {
		return ExerciseBookTool.connectTimeOut;
	}

	/**
	 * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
	 */
	public static int getReadTimeOut() {
		return ExerciseBookTool.readTimeOut;
	}

	/**
	 * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
	 */
	public static String getRequestEncoding() {
		return requestEncoding;
	}

	/**
	 * @param connectTimeOut
	 * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
	 */
	public static void setConnectTimeOut(int connectTimeOut) {
		ExerciseBookTool.connectTimeOut = connectTimeOut;
	}

	/**
	 * @param readTimeOut
	 * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
	 */
	public static void setReadTimeOut(int readTimeOut) {
		ExerciseBookTool.readTimeOut = readTimeOut;
	}

	/**
	 * @param requestEncoding
	 * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
	 */
	public static void setRequestEncoding(String requestEncoding) {
		ExerciseBookTool.requestEncoding = requestEncoding;
	}

	public static boolean isNull(ArrayList<ArrayList<String>> data) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).get(2).equals("")) {
				return false;
			}
		}
		return true;
	}

	// 给 listview 设置高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高

			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度.

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// params.height = totalHeight + (listView.getDividerHeight() *
		// (listAdapter.getCount() - 1))+157;
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount()));
		// listView.getDividerHeight()获取子项间分隔符占用的高度

		listView.setLayoutParams(params);
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	// 完整的判断中文汉字和符号
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}


	/*
	 * 设置头像
	 */
	public static void set_background(final String uri, final ImageView imageView) {

		final Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					Drawable drawable = (Drawable) msg.obj;
					imageView.setImageDrawable(drawable);
					break;
				default:
					break;
				}
			}
		};

		Thread thread = new Thread() {
			public void run() {
				HttpClient hc = new DefaultHttpClient();

				HttpGet hg = new HttpGet(uri);//
				Drawable face_drawable;
				try {
					HttpResponse hr = hc.execute(hg);
					Bitmap bm = BitmapFactory.decodeStream(hr.getEntity()
							.getContent());
					face_drawable = new BitmapDrawable(bm);
					Message msg = new Message();// 创建Message 对象
					msg.what = 0;
					msg.obj = face_drawable;
					mHandler.sendMessage(msg);
				} catch (Exception e) {

				}

			}
		};		
		thread.start();
	}
}
