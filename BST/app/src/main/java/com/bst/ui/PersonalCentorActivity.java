package com.bst.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bst.Application;
import com.bst.BaseActivity;
import com.bst.R;
import com.bst.biz.Const;
import com.bst.thread.BaseInterfaceThread;
import com.bst.utils.BitmapUtils;
import com.bst.utils.ImageCompressHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PersonalCentorActivity extends BaseActivity {

	@ViewInject(R.id.leftTopLayout)
	private LinearLayout leftTopLayout;
	@ViewInject(R.id.leftTv)
	private TextView leftTv;
	@ViewInject(R.id.name)
	private TextView name;
	@ViewInject(R.id.bstNum)
	private TextView bstNum;
	@ViewInject(R.id.sex)
	private TextView sex;
	@ViewInject(R.id.signature)
	private TextView signature;
	@ViewInject(R.id.photo)
	private ImageView photo;

	private Application app = Application.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String base64String = null;

	@Override
	protected void handle(Message msg) {
		if (Const.SYSTEM_RESPONSE_SUCCESS == msg.what) {
			String reslut = (String) msg.obj;
			try {
				JSONObject jobj = new JSONObject(reslut);
				String flag = jobj.getString("succee");
				if ("1".equals(flag)) {
					Application app = Application.getInstance();
					app.saveUser("photobase64", base64String);
				} else if ("0".equals(flag)) {
					Toast.makeText(getApplicationContext(), "图像保存失败！",
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"json数据解析异常" + e.getMessage(), Toast.LENGTH_LONG);
			}
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_personal_centor);
		ViewUtils.inject(this);
		initData();
	}

	private void initData() {
		leftTv.setText("个人中心");
		String user = app.getUser("user");
		String sexa = app.getUser("sex");
		String position = app.getUser("position");
		String department = app.getUser("department");

		name.setText(user);
		bstNum.setText(position);
		sex.setText(sexa);
		signature.setText(department);
		photo.setImageBitmap(BitmapUtils.base64Tobitmap(app
				.getUser("photobase64")));
	}

	@OnClick({ R.id.leftTopLayout, R.id.photo })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTopLayout:
			finish();
			break;
		case R.id.photo:
			showSelectorDialog();
			break;
		default:
			break;
		}
	}

	Dialog showDialog;
	@ViewInject(R.id.pic)
	private TextView pic;
	@ViewInject(R.id.camera)
	private TextView camera;
	@ViewInject(R.id.cancel)
	private TextView cancel;

	private void showSelectorDialog() {
		View view = LayoutInflater.from(getBaseContext()).inflate(
				R.layout.show_selector_photo_dialog, null);
		showDialog = new AlertDialog.Builder(PersonalCentorActivity.this)
				.create();
		showDialog.setCanceledOnTouchOutside(false);
		showDialog.setCancelable(false);
		showDialog.show();
		showDialog.setContentView(view);
		cancel = (TextView) view.findViewById(R.id.cancel);
		camera = (TextView) view.findViewById(R.id.camera);
		pic = (TextView) view.findViewById(R.id.pic);

		cancel.setOnClickListener(onClickListener);
		camera.setOnClickListener(onClickListener);
		pic.setOnClickListener(onClickListener);
	}

	protected final static int CHOOSE_PICTURE = 10;
	protected final static int CAMERA_PICTURE = 20;
	protected static final int resultCode = 4000;
	private File file = null;

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cancel:
				showDialog.dismiss();
				break;
			case R.id.pic:
				// 调用android的图库
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, CHOOSE_PICTURE);
				break;
			case R.id.camera:
				try {
					file = new File(Environment.getExternalStorageDirectory()
							.getAbsolutePath()
							+ File.separator
							+ "bst"
							+ "photo.jpg");
					if (!file.exists())
						try {
							file.createNewFile();
						} catch (IOException e) {
							Toast.makeText(getBaseContext(), "没有找到储存目录",
									Toast.LENGTH_LONG).show();
						}

				} catch (ActivityNotFoundException e) {
					Toast.makeText(getBaseContext(), "没有找到储存目录",
							Toast.LENGTH_LONG).show();
				}
				Uri u = Uri.fromFile(file);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, CAMERA_PICTURE);
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case CHOOSE_PICTURE:// 本地图片
			showDialog.dismiss();
			// 直接调用默认的图库程序
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			// Bitmap bitmapPic=BitmapFactory.decodeFile(picturePath);
			// photo.setImageBitmap(bitmapPic);
			Bitmap bitmapPic = ImageCompressHelper.getimage(picturePath);
			photo.setImageBitmap(ImageCompressHelper.getimage(picturePath));
			base64String = BitmapUtils.bitmapTobase64(bitmapPic);
			// 进行阿里识别或提交服务
			savePhoto(base64String);

			break;

		case CAMERA_PICTURE:// 相机拍照
			showDialog.dismiss();
			// Bitmap
			// bitmapCamera=BitmapFactory.decodeFile(file.getAbsolutePath());
			// photo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

			Bitmap bitmapCamera = ImageCompressHelper.getimage(file
					.getAbsolutePath());
			photo.setImageBitmap(bitmapCamera);
			base64String = BitmapUtils.bitmapTobase64(bitmapCamera);
			// 进行阿里识别或提交服务
			savePhoto(base64String);
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void savePhoto(String photo) {
		BaseInterfaceThread bift = new BaseInterfaceThread();
		String url = Const.baseUrl + "//bmk/solar/upload";
		String data = "{\"userid\":\"" + app.getUser("id")
				+ "\",\"photobase\":\"" + photo + "\"}";
		Map<String, String> map = new HashMap<String, String>();
		map.put("data", data);
		bift.postRequestInterface(PersonalCentorActivity.this, handler, url,
				map);
	}

	private void face(String base64String) {

		try {
			BaseInterfaceThread bift = new BaseInterfaceThread();
			String url = Const.baseUrl + "//bmk/solar/addchecking";
			JSONObject jo = new JSONObject();
			jo.put("userid", app.getUser("id"));
			jo.put("checking", app.getUser("user"));
			jo.put("checkingtime", sdf.format(new Date()));
			jo.put("precision", "11111");
			jo.put("dimensionality", "22222");
			jo.put("photo", base64String);
			Map<String, String> map = new HashMap<String, String>();
			map.put("data", jo.toString());
			bift.postRequestInterface(PersonalCentorActivity.this, handler,
					url, map);
		} catch (Exception e) {
		}
	}
}
