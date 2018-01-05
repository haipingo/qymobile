package com.bst.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.util.support.Base64;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.bst.Application;
import com.bst.BaseActivity;
import com.bst.R;
import com.bst.biz.Const;
import com.bst.location.CommonLocationListener;
import com.bst.location.LocationTool;
import com.bst.thread.BaseInterfaceThread;
import com.bst.utils.BitmapUtils;
import com.bst.utils.DateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AttAddFaceActivity extends BaseActivity {
	@ViewInject(R.id.leftTv)
	private TextView leftTv;
	@ViewInject(R.id.surfaceview)
	private SurfaceView surfaceView;
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private Camera.Parameters mParameters = null;
	private static final String TAG = "ansen";

	@ViewInject(R.id.selectCamera)
	private TextView selectCamera;

	private String longitude;
	private String latitude;
	private Application app = Application.getInstance();
	LocationTool location;

	private int type = -1;

	@Override
	protected void handle(Message msg) {
		super.handle(msg);
		stopDialog();
		switch (msg.what) {
		case Const.SYSTEM_RESPONSE_SUCCESS:
			Toast.makeText(getBaseContext(), "添加考勤成功", Toast.LENGTH_LONG)
					.show();
//			finish();
//			if (type == -1) {// 来自集体考勤
//				startActivity(new Intent(getBaseContext(),
//						PersonAttendanceActivity.class));
//			} else {
//				Application.getInstance().finishActivity(
//						SelectAttdanceActivity.class);
//				startActivity(new Intent(getBaseContext(),
//						PersonAttendanceActivity.class));
//			}
			this.setResult(3000, getIntent());
			finish();
			break;
		case 10000:
			Bundle bundle = msg.getData();
			byte[] data = bundle.getByteArray("data");
			// int width = bundle.getInt("width");
			// int height = bundle.getInt("height");
			if (data == null) {
				return;
			}

			Bitmap cameraBitmap = BitmapUtils.byteToBitmap(data);
			Matrix matrix = new Matrix();
			matrix.preRotate(270);
			cameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
					cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix,
					true);
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos2);
			byte[] datas = baos2.toByteArray();
			JSONObject json = new JSONObject();
			String url = null;
			try {
				 url = Const.baseUrl + "//bmk/solar/addchecking";
				json.put("precision", longitude);
				json.put("dimensionality", latitude);
				json.put("userid", app.getUser("id"));
				json.put("checking", app.getUser("user"));
				json.put("checkingtime", DateUtil.getCurrentTimeStr3());
				json.put("photo", Base64.encodeBytes(datas));

				if (type == 1) {// 来自集体考勤
					 url = Const.baseUrl + "//bmk/solar/addexecutor";
					json.put("executor", getIntent().getStringExtra("executor"));
					json.put("checking", getIntent().getStringExtra("username"));
				}

				Map<String, String> map = new HashMap<String, String>();
				map.put("data", json.toString());

				showDialog("请稍候，请求中....").show();
				BaseInterfaceThread.postRequestInterface(
						AttAddFaceActivity.this, handler, url, map);
			} catch (Exception e) {
			}

			break;

		default:

			Toast.makeText(getBaseContext(), "添加考勤faid", Toast.LENGTH_LONG)
					.show();
			break;
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_attaddface);
		ViewUtils.inject(this);
		type = getIntent().getIntExtra("type", -1);
		initData();
		MyListener li = new MyListener();
		location = new LocationTool(getApplicationContext(), li);
		// location.requestOnce();
		location.start();

		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setFixedSize(176, 144); // 设置Surface分辨率
		surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
		surfaceView.getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数
	}

	private void initData() {
		leftTv.setText("添加考勤");
	}

	@OnClick({ R.id.leftTopLayout, R.id.selectCamera, R.id.shutPic })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTopLayout:
			finish();
			break;
		case R.id.selectCamera:
			change();
			break;
		case R.id.shutPic:
			if (mCamera != null) {
				// 获取到拍照的图片数据后回调PictureCallback,PictureCallback可以对相片进行保存或传入网络
				mCamera.takePicture(null, null, new MyPictureCallback());
			}
			break;
		default:
			break;
		}
	}

	class MyListener extends CommonLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			super.onReceiveLocation(location);
			latitude = Double.valueOf(location.getLatitude()).toString();
			longitude = Double.valueOf(location.getLongitude()).toString();
			System.out.println("latitude:" + latitude + "  longitude:"
					+ longitude);
		}
	}

	/**
	 * 显示surfaceView 数据的接口
	 */
	private class SurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Log.d(TAG, "surfaceChanged");
			mParameters = mCamera.getParameters(); // 获取各项参数
			mParameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
			mParameters.setPreviewSize(width, height); // 设置预览大小
			mParameters.setPreviewFrameRate(5); // 设置每秒显示4帧
			mParameters.setPictureSize(width, height); // 设置保存的图片尺寸
			mParameters.setJpegQuality(80); // 设置照片质量
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "surfaceCreated");
			mHolder = holder;// SurfaceHolder是系统提供用来设置surfaceView的对象
			try {
				mCamera = Camera.open(1); // 打开摄像头
				mCamera.setDisplayOrientation(90);
				mCamera.setPreviewDisplay(holder); // 通过surfaceview显示取景画面
				// mCamera.setDisplayOrientation(getPreviewDegree(tes.this));//
				// 设置相机的方向
				mCamera.cancelAutoFocus();
				mCamera.startPreview(); // 开始预览
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "surfaceCreated >>  Exception: " + e.getMessage());
			}

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d(TAG, "surfaceDestroyed");
			if (mCamera != null) {
				mCamera.release(); // 释放照相机
				mCamera = null;
			}
		}
	}

	private int cameraPosition = 0;// 0代表前置摄像头，1代表后置摄像头

	public void change() {
		// 切换前后摄像头
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
			if (cameraPosition == 1) {
				// 现在是后置，变更为前置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																					// CAMERA_FACING_BACK后置
					mCamera.stopPreview();// 停掉原来摄像头的预览
					mCamera.release();// 释放资源
					mCamera = null;// 取消原来摄像头
					mCamera = Camera.open(i);// 打开当前选中的摄像头
					try {
						mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
					} catch (IOException e) {
						e.printStackTrace();
					}
					mCamera.setDisplayOrientation(90);
					mCamera.startPreview();// 开始预览
					cameraPosition = 0;
					break;
				}
			} else {
				// 现在是前置， 变更为后置
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																				// CAMERA_FACING_BACK后置
					mCamera.stopPreview();// 停掉原来摄像头的预览
					mCamera.release();// 释放资源
					mCamera = null;// 取消原来摄像头
					mCamera = Camera.open(i);// 打开当前选中的摄像头
					try {
						mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mCamera.setDisplayOrientation(90);
					mCamera.startPreview();// 开始预览
					cameraPosition = 1;
					break;
				}
			}

		}
	}

	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private final class MyPictureCallback implements Camera.PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {

				Bundle bundle = new Bundle();
				bundle.putByteArray("data", data);
				Message message = new Message();
				message.setData(bundle);
				message.what = 10000;
				handler.sendMessage(message);
				// Toast.makeText(getApplicationContext(), "success",
				// Toast.LENGTH_SHORT).show();
				camera.startPreview(); // 拍完照后，重新开始预览

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void finish() {
		location.stop();
		super.finish();
	}
}
