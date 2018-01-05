package com.bst.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bst.BaseActivity;
import com.bst.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ActivityPanel extends BaseActivity {
	@ViewInject(R.id.rightIcon)
	private ImageView rightIcon;
	@ViewInject(R.id.bottomLayout2)
	private LinearLayout bottomLayout2;
	@ViewInject(R.id.leftTv)
	private TextView leftTv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activty_panel);
		ViewUtils.inject(this);
		initData();
	}

	private void initData() {
		rightIcon.setBackgroundResource(R.drawable.navigation_bar_more_icon);
		leftTv.setText("新建笔记");
	}

	Boolean isShow = false;

	@OnClick({ R.id.leftTopLayout, R.id.addLayout })
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTopLayout:
			finish();
			break;
		case R.id.addLayout:
			if (isShow) {
				isShow = false;
				bottomLayout2.setVisibility(View.VISIBLE);
			} else {
				isShow = true;
				bottomLayout2.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
}
