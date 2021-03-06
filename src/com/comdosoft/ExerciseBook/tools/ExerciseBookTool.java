package com.comdosoft.ExerciseBook.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import com.comdosoft.ExerciseBook.pojo.AnswerJson;
import com.comdosoft.ExerciseBook.pojo.AnswerPojo;
import com.comdosoft.ExerciseBook.pojo.Answer_QuestionsPojo;
import com.comdosoft.ExerciseBook.pojo.Branch_AnswerPoJo;
import com.comdosoft.ExerciseBook.pojo.PropPojo;
import com.comdosoft.ExerciseBook.pojo.WorkPoJo;
import com.google.gson.Gson;

public class ExerciseBookTool implements Urlinterface {

	private static int connectTimeOut = 5000;
	private static int readTimeOut = 10000;
	private static String requestEncoding = "UTF-8";
	private static Bitmap bm = null;

	public static List<Boolean> getTypeList(WorkPoJo pojo) {
		List<Boolean> list = new ArrayList<Boolean>();
		for (int i = 0; i < pojo.getQuestion_types().size(); i++) {
			if (ExerciseBookTool.getExist(pojo.getQuestion_types().get(i),
					pojo.getFinish_types())) {
				list.add(true);
			} else {
				list.add(false);
			}
		}
		return list;
	}

	public static String timeSecondToString(int t) {
		StringBuffer sb = new StringBuffer();
		if (t >= 60) {
			sb.append(t / 60);
			sb.append("'");
			if (t % 60 < 10) {
				sb.append(0);
			}
			sb.append(t % 60);
		} else {
			sb.append(t);
		}
		sb.append("\"");
		return sb.toString();
	}

	public static void UpdateJsonTime(String time, String url) {
		Gson gson = new Gson();
		String answer_history = ExerciseBookTool.getAnswer_Json_history(url);
		AnswerJson answerJson = gson.fromJson(answer_history, AnswerJson.class);
		answerJson.update = time;
		String str = gson.toJson(answerJson);
		ExerciseBookTool.writeFile(url, str);
	}

	// 判断是否包含标点符号
	public boolean isNotChinese(String str) {
		if (str.equals("") || str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]");
		return pattern.matcher(str.trim()).find();
	}

	// 过去answer中的时间
	public static String getAnswerTime(String path) {
		Gson gson = new Gson();
		Log.i("linshi", "---1");
		String answer_history = ExerciseBookTool.getAnswer_Json_history(path);
		Log.i("Ax", answer_history);
		AnswerJson answerJson = gson.fromJson(answer_history, AnswerJson.class);
		return answerJson.update;
	}

	// 比较时间大小
	public static boolean Comparison_Time(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() >= dt2.getTime()) {
				Log.i("suanfa", "a比b大");
				return false;
			} else {
				Log.i("suanfa", "a比b小");
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	public static String getTimeIng() {// 获取当前时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	public static String del_tag(String str) {// 去除HTML标签
		Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(str);
		String content = m_html.replaceAll(""); // 过滤html标签
		return content;
	}

	/**
	 * 解压一个压缩文档 到指定位置
	 * 
	 * @param zipFileString
	 *            压缩包的名字
	 * @param outPathString
	 *            指定的路径
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void unZip(String unZipfileName, String mDestPath)
			throws Exception {
		FileOutputStream fileOut = null;
		ZipInputStream zipIn = null;
		ZipEntry zipEntry = null;
		File file = null;
		int readedBytes = 0;
		byte buf[] = new byte[4096];
		zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(
				unZipfileName)));
		while ((zipEntry = zipIn.getNextEntry()) != null) {
			file = new File(mDestPath + "/" + zipEntry.getName());
			if (zipEntry.isDirectory()) {
				file.mkdirs();
			} else {
				// 如果指定文件的目录不存在,则创建之.
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				fileOut = new FileOutputStream(file);
				while ((readedBytes = zipIn.read(buf)) > 0) {
					fileOut.write(buf, 0, readedBytes);
				}
				fileOut.close();
			}
			zipIn.closeEntry();
		}
	}

	// 初始化answer文件
	public static void initAnswer(String path, String id, String uid) {
		List<PropPojo> propList = new ArrayList<PropPojo>();
		for (int i = 0; i < 2; i++) {
			propList.add(new PropPojo(i + "", new ArrayList<Integer>()));
		}
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(path + "/student_" + uid + ".json");
			if (!file.exists()) {
				file.createNewFile();
				Log.i("linshi", path + "/student_" + uid + ".json");
				AnswerJson answer = new AnswerJson(id, "0",
						"0000-00-00 00:00:00", propList, new AnswerPojo("0",
								"", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()),
						new AnswerPojo("0", "", "-1", "-1", "0",
								new ArrayList<Answer_QuestionsPojo>()));
				// String json=JSONArray.fromObject(answer);
				Gson gson = new Gson();
				String result = gson.toJson(answer);
				Log.i("linshi", result);
				ExerciseBookTool.writeFile(path + "/student_" + uid + ".json",
						result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取历史记录
	public static AnswerPojo getAnswer(String json, String key) {
		AnswerPojo answer = new AnswerPojo();
		try {
			List<Answer_QuestionsPojo> list_history = new ArrayList<Answer_QuestionsPojo>();
			JSONObject obj = new JSONObject(json);
			JSONObject keyJson = obj.getJSONObject(key);
			int questions_item = keyJson.getInt("questions_item");
			int branch_item = keyJson.getInt("branch_item");
			int status = keyJson.getInt("status");
			int use_time = keyJson.getInt("use_time");
			JSONArray questions = keyJson.getJSONArray("questions");
			if (questions.length() > 0) {
				for (int i = 0; i < questions.length(); i++) {
					JSONObject jo = questions.getJSONObject(i);
					JSONArray jsonarr = jo.getJSONArray("branch_questions");
					Branch_AnswerPoJo tl;
					List<Branch_AnswerPoJo> question_history = new ArrayList<Branch_AnswerPoJo>();
					for (int j = 0; j < jsonarr.length(); j++) {
						JSONObject item = jsonarr.getJSONObject(j);
						tl = new Branch_AnswerPoJo(item.getInt("id") + "",
								item.getString("answer"), item.getInt("ratio")
										+ "");
						question_history.add(tl);
					}
					Answer_QuestionsPojo lp = new Answer_QuestionsPojo(
							jo.getInt("id") + "", question_history);
					list_history.add(lp);
				}
				answer = new AnswerPojo(status + "", "", questions_item + "",
						branch_item + "", use_time + "", list_history);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return answer;
	}

	// 获取历史索引
	private static int[] getAnswer_Item(String json) {
		int[] arr = new int[3];
		if (json != "") {
			try {
				JSONObject obj = new JSONObject(json);
				JSONObject cloze = obj.getJSONObject("cloze");
				arr[0] = cloze.getInt("questions_item");
				arr[1] = cloze.getInt("branch_item");
				arr[2] = cloze.getInt("status");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return arr;
	}

	// 获取历史answer
	public static String getAnswer_Json_history(String path) {
		return ExerciseBookTool.getJson(path);
	}

	// 返回calendar格式的时间
	public static Calendar getCalender_time(String date) {
		Calendar wrok_day = Calendar.getInstance();
		String day = date.split(" ")[0];
		String[] dayarr = day.split("-");
		String t = date.split(" ")[1];
		String[] timearr = t.split(":");
		wrok_day.set(Integer.valueOf(dayarr[0]),
				Integer.valueOf(dayarr[1]) - 1, Integer.valueOf(dayarr[2]),
				Integer.valueOf(timearr[0]), Integer.valueOf(timearr[1]),
				Integer.valueOf(timearr[2]));
		return wrok_day;
	}

	// 计算正确率
	public static int getRatio(String path, String key) {
		List<Integer> ratio = new ArrayList<Integer>();
		String answer_history = getJson(path);
		while (answer_history == null || answer_history.equals("")) {
			answer_history = getJson(path);
		}
		Log.i("Ax", path + "--" + key);
		Log.i("Ax", "json--" + answer_history);
		try {
			JSONObject obj = new JSONObject(answer_history);
			JSONObject js = obj.getJSONObject(key);
			JSONArray arr = js.getJSONArray("questions");
			if (arr.length() == 0) {
				return -10;
			}
			for (int i = 0; i < arr.length(); i++) {
				JSONObject item = arr.getJSONObject(i);
				JSONArray ar = item.getJSONArray("branch_questions");
				for (int j = 0; j < ar.length(); j++) {
					JSONObject o = ar.getJSONObject(j);
					ratio.add(Integer.parseInt(o.getString("ratio")));
				}
			}
		} catch (JSONException e) {
			getRatio(path, key);
			Log.i("Ax", "JSONException-" + e.toString() + "--" + e.getMessage());
		}
		int size = 0;
		for (int i = 0; i < ratio.size(); i++) {
			size += ratio.get(i);
		}
		Log.i("Ax", size + "--" + ratio.size() + "-ratio-size-");
		return size / ratio.size();
	}

	// 下载路径判断
	public static boolean FileExist(String path, String filename) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		File file2 = new File(path + "/" + filename);
		if (!file2.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 新建Json文件时，写入json数据
	 * 
	 * @param filePath
	 * @param sets
	 * @throws IOException
	 */
	public static void writeFile(String filePath, String sets) {
		try {
			FileWriter fw = new FileWriter(filePath);
			PrintWriter out = new PrintWriter(fw);
			out.write(sets);
			out.println();
			fw.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 读取本地json文件
	public static String getJson(String path) {
		Log.i("Ax", path);
		StringBuilder stringBuilder = new StringBuilder();
		InputStream in;
		BufferedReader bf;
		try {
			File f = new File(path);// 这是对应文件名
			in = new BufferedInputStream(new FileInputStream(f));
			bf = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
			bf.close();
			in.close();
		} catch (IOException e) {
			Log.i("Ax", e.getMessage());
			Log.i("Ax", "读取json文件发生错误");
		}
		return stringBuilder.toString();
	}

	// 分割时间 带时分秒 2014-03-21 13:14:15
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
	public static boolean getExist(int one, List<Integer> two) {
		for (int i = 0; i < two.size(); i++) {
			if (one == two.get(i)) {
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
		String json = "";
		StringBuilder url = new StringBuilder(path);
		url.append("?");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			url.append(entry.getKey()).append("=").append(entry.getValue());
			url.append("&");
		}
		url.deleteCharAt(url.length() - 1);
		Log.i("aaa", url.toString());
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
		Log.i("aaa", json);
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
			Log.i("linshi", "linshi-------dopost---try");
			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			tempLine = rd.readLine();
			rd.close();
			in.close();
			Log.i("linshi", tempLine);
		} catch (IOException e) {
			Log.i("linshi", "发生异常");
			e.printStackTrace();
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
	public static boolean isChinese(String str) {
		if (str.equals("") || str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]");
		return pattern.matcher(str.trim()).find();
	}

	/*
	 * 设置头像
	 */
	public static void set_background(final String url,
			final ImageView imageView) {

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

				HttpGet hg = new HttpGet(url);//
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

	public static void set_bk(final String url, final ImageView imageView,
			final ImageMemoryCache memoryCache) {

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

				Drawable face_drawable;
				try {
					// HttpResponse hr = hc.execute(hg);
					// Bitmap bm = BitmapFactory.decodeStream(hr.getEntity()
					// .getContent());
					// Bitmap bm = getURLimage(url);

					Log.i("linshi------------", url);
					
					URL myurl = new URL(url);
					// 获得连接
					HttpURLConnection conn = (HttpURLConnection) myurl
							.openConnection();
					conn.setConnectTimeout(6000);// 设置超时
					conn.setDoInput(true);
					conn.setUseCaches(false);// 不缓存
					conn.connect();
					InputStream is = conn.getInputStream();// 获得图片的数据流
//						bm =decodeSampledBitmapFromStream(is,150,150);
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = false;
					// options.outWidth = 159;
					// options.outHeight = 159;
					options.inSampleSize = 1;
					bm = BitmapFactory.decodeStream(is, null, options);
					
					is.close();
					if (bm!=null) {
						Log.i("linshi", bm.getWidth()+"---"+bm.getHeight());
						memoryCache.addBitmapToCache(url, bm);
						face_drawable = new BitmapDrawable(bm);
						Message msg = new Message();// 创建Message 对象
						msg.what = 0;
						msg.obj = face_drawable;
//						msg.obj = bm;
						mHandler.sendMessage(msg);
					
					}
					 
				} catch (Exception e) {
					Log.i("linshi", "发生异常");
					// Log.i("linshi", url);
				}

			}
		};

		thread.start();

	}

	/*
	 * 分割时间 2014/03/21 13:14:15
	 */
	public static String divisionTime2(String timeStr) {
		timeStr = timeStr.replace("-", "/");
		if (timeStr.length() < 22) {
			return timeStr;
		} else {
			int temp1 = timeStr.indexOf("T");
			int temp2 = timeStr.lastIndexOf("+");
			String s = timeStr.substring(temp1 + 1, temp2);
			int temp3 = s.lastIndexOf(":");
			return timeStr.substring(0, temp1) + "  " + s.substring(0, temp3);
		}

	}

	// 加载图片
	public static Bitmap getURLimage(String url) {
		Bitmap bmp = null;
		try {
			URL myurl = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不缓存
			conn.connect();
			InputStream is = conn.getInputStream();// 获得图片的数据流
			bmp = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}
	
	public static synchronized Bitmap decodeSampledBitmapFromStream(  
	        InputStream in, int reqWidth, int reqHeight) {  
	  
	    // First decode with inJustDecodeBounds=true to check dimensions  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;  
	    BitmapFactory.decodeStream(in, null, options);  
	  
	    // Calculate inSampleSize  
	    options.inSampleSize = calculateInSampleSize(options, reqWidth,  
	            reqHeight);  
	  
	    // Decode bitmap with inSampleSize set  
	    options.inJustDecodeBounds = false;  
	    return BitmapFactory.decodeStream(in, null, options);  
	}  
	  
	/** 
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} 
	 * object when decoding bitmaps using the decode* methods from 
	 * {@link BitmapFactory}. This implementation calculates the closest 
	 * inSampleSize that will result in the final decoded bitmap having a width 
	 * and height equal to or larger than the requested width and height. This 
	 * implementation does not ensure a power of 2 is returned for inSampleSize 
	 * which can be faster when decoding but results in a larger bitmap which 
	 * isn't as useful for caching purposes. 
	 *  
	 * @param options 
	 *            An options object with out* params already populated (run 
	 *            through a decode* method with inJustDecodeBounds==true 
	 * @param reqWidth 
	 *            The requested width of the resulting bitmap 
	 * @param reqHeight 
	 *            The requested height of the resulting bitmap 
	 * @return The value to be used for inSampleSize 
	 */  
	public static int calculateInSampleSize(BitmapFactory.Options options,  
	        int reqWidth, int reqHeight) {  
	    // Raw height and width of image  
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	  
	    //先根据宽度进行缩小  
	    while (width / inSampleSize > reqWidth) {  
	        inSampleSize++;  
	    }  
	    //然后根据高度进行缩小  
	    while (height / inSampleSize > reqHeight) {  
	        inSampleSize++;  
	    }  
	    return inSampleSize;  
	}  
	
}
