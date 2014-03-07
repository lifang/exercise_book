package com.comdosoft.ExerciseBook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.comdosoft.ExerciseBook.tools.ExerciseBook;
import com.comdosoft.ExerciseBook.tools.ExerciseBookParams;
import com.comdosoft.ExerciseBook.tools.ExerciseBookTool;
import com.comdosoft.ExerciseBook.tools.Urlinterface;

public class SettingPhoto extends Activity implements Urlinterface {

	// 设置 界面
	private ImageView faceImage;
	private EditText nickname;//
	private EditText name; //
	private View layout;// 选择头像界面
	private String json = "";
	String delUri = "";
	String photoStr = Environment.getExternalStorageDirectory()
	+ "/1" + IMAGE_FILE_NAME;
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */

	private Bitmap bm = null;
	private String nameS = ""; //
	private String nicknameS = ""; //
	private String id = "8"; // 用户 id，，切记 不是 user_id
	private String user_id = "8"; // 用户 id，，切记 不是 user_id
	private String avatar_url = ""; // 用户头像
	private ExerciseBook exerciseBook;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.settingphoto);
		exerciseBook = (ExerciseBook) getApplication();
		File file = new File(photoStr);

		if (file.exists()) {
			file.delete();

		}

	}

	/**
	 * 拍照上传
	 */
	public void set_paizhaoshangchuan(View v) {

		try {

			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 判断存储卡是否可以用，可用进行存储
			if (ExerciseBookTool.isHasSdcard()) {

				File file = new File(Environment.getExternalStorageDirectory()
						+ "/" + IMAGE_FILE_NAME);

				if (file.exists()) {
					file.delete();

				}
				file = new File(Environment.getExternalStorageDirectory() + "/"
						+ IMAGE_FILE_NAME);
				if (!file.exists()) {
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									IMAGE_FILE_NAME)));

				}

			}

			startActivityForResult(intentFromCapture, 2);
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(),
					ExerciseBookParams.MES_CAPTURE, 0).show();
		}

	}

	/**
	 * 从相册选择
	 */
	public void set_congxiangce(View v) {

		Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);

		/**
		 * 下面这句话，与其它方式写是一样的效果，如果：
		 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * intent.setType(""image/*");设置数据类型
		 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
		 */
		intentFromGallery.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intentFromGallery, 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != 0) {

			switch (requestCode) {
			// 如果是直接从相册获取
			case 1:
				startPhotoZoom(data.getData());
				break;
			// 如果是调用相机拍照时
			case 2:

				if (ExerciseBookTool.isHasSdcard()) {
					File temp = new File(
							Environment.getExternalStorageDirectory() + "/"
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(temp));
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}

				break;
			// 取得裁剪后的图片
			case 3:
				/**
				 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
				 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
				 * 
				 */
				if (data != null) {
					getImageToView(data);
				}
				break;
			default:
				break;

			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		delUri = getAbsoluteImagePath(uri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 186);
		intent.putExtra("outputY", 186);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			// faceImage.setBackgroundDrawable(drawable);
			
			File file = new File(photoStr);

			try {

				if (file.exists()) {
					file.delete();

				}

				file.createNewFile();
				FileOutputStream stream = new FileOutputStream(file);
				ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] buf = stream1.toByteArray(); // 将图片流以字符串形式存储下来
				// byte[] buf = s.getBytes();
				stream.write(buf);
				stream.close();
//				delStr(delUri);
				exerciseBook.setRefresh(0);
				Intent intent2 = new Intent();

				intent2.putExtra("uri",photoStr);
				// 通过调用setResult方法返回结果给前一个activity。
				SettingPhoto.this.setResult(RESULT_OK, intent2);
				// 关闭当前activity
				SettingPhoto.this.finish();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	protected void delStr(String del) {
		String str1 = del;
		int temp2 = str1.lastIndexOf(".");
		String s1 = str1.substring(0, temp2) + "~2";
		String s2 = str1.substring(temp2, str1.length());
		str1 = s1 + s2;
		File file = new File(str1);

		if (file.exists()) {
			file.delete();

		}

	}

	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
}
