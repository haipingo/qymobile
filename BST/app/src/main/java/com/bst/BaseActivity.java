package com.bst;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class BaseActivity extends Activity {
	private ProgressDialog dialog;

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			handle(msg);
		};
	};

	protected void handle(Message msg) {
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Application.getInstance().addActivity(this);
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
		// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Application.getInstance().finishActivity(this);
	}

	/**
	 * 显示加载dialog
	 * 
	 * @param content
	 *            提示内容
	 */
	public ProgressDialog showDialog(String content) {
		stopDialog();
		dialog = new ProgressDialog(BaseActivity.this);
		dialog.setMessage(content);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		return dialog;
	}

	/**
	 * 隐藏加载dialog
	 */
	public void stopDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
