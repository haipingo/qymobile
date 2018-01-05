package com.bst.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bst.Application;
import com.bst.BaseActivity;
import com.bst.R;
import com.bst.utils.BitmapUtils;
import com.fr.android.activity.LoadAppFromWelcomeActivity;
import com.fr.android.platform.oem.IFOEMUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MainActivity extends BaseActivity {

	@ViewInject(R.id.leftBackIcon)
	private ImageView leftBackIcon;
	@ViewInject(R.id.name)
	private TextView name;
	@ViewInject(R.id.photo)
	private ImageView photo;

	private Application app = Application.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		initData();
	}

	private void initData() {
		leftBackIcon.setVisibility(View.GONE);
		String user = app.getUser("user");
		name.setText(user);
		photo.setImageBitmap(BitmapUtils.base64Tobitmap(app
				.getUser("photobase64")));
	}

	@OnClick({ R.id.personalCentorLayout, R.id.personAttendanceLayout,
			R.id.noteLayout, R.id.managementLayout, R.id.communicationLayout,
			R.id.attendanceLayout , R.id.managementLayouta})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personalCentorLayout:// 个人中心
			startActivity(new Intent(getBaseContext(),
					PersonalCentorActivity.class));
			break;
		case R.id.personAttendanceLayout:// 个人考勤
			startActivity(new Intent(getBaseContext(),
					PersonAttendanceActivity.class));
			break;
		case R.id.noteLayout:// 文案笔记
			startActivity(new Intent(getBaseContext(),
					ActivityPanel.class));
			break;
		case R.id.managementLayout:// 任务管理

			break;
		case R.id.communicationLayout:// 通讯交流

			break;
		case R.id.attendanceLayout:// 集体考勤
			startActivity(new Intent(getBaseContext(), AttendanceActivity.class));

			break;

			case R.id.managementLayouta:// 报表
				IFOEMUtils.setUseGuidePage(false); // 在第一次安装加载时, 是否启用引导页.
				Intent welcomeIntent = new Intent(this, LoadAppFromWelcomeActivity.class);
				welcomeIntent.putExtra("username", "demo"); // 数据决策系统 用户名
				welcomeIntent.putExtra("password", "demo"); // 数据决策系统 用户密码
				welcomeIntent.putExtra("serverIp", "http://www.finereporthelp.com:8889/app/ReportServer"); // 数据决策系统 地址ip
				welcomeIntent.putExtra("serverName", "demo服务器"); // 数据决策系统 名称

				startActivity(welcomeIntent);
				break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (app.getUser("photobase64") != null) {
			photo.setImageBitmap(BitmapUtils.base64Tobitmap(app
					.getUser("photobase64")));
		}
	}
}
