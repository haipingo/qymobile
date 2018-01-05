package com.bst.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bst.BaseActivity;
import com.bst.R;
import com.bst.adapter.SelectAttAdapter;
import com.bst.bean.User;
import com.bst.biz.Const;
import com.bst.thread.BaseInterfaceThread;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SelectAttdanceActivity extends BaseActivity implements OnItemClickListener {
	@ViewInject(R.id.listView)
	private ListView listView;
	private SelectAttAdapter adapter;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.rightTv)
	private TextView rightTv;
	@ViewInject(R.id.addAtt)
	private Button addAtt;
	
	private List<User> list = new ArrayList<User>();

	@Override
	protected void handle(Message msg) {
		super.handle(msg);
		stopDialog();
		if (Const.SYSTEM_RESPONSE_SUCCESS == msg.what) {
			String reslut = (String) msg.obj;
			List<User> list2 = new ArrayList<User>();
			try {
				JSONObject jobj = new JSONObject(reslut);
				String flag = jobj.getString("succee");
				if ("1".equals(flag)) {
					JSONArray jy = jobj.getJSONArray("users");
					for (int i = 0; i < jy.length(); i++) {
						JSONObject sub = jy.getJSONObject(i);
						User user = new User();
						user.setId(sub.getString("id"));
						user.setPhoto(sub.getString("photobase64"));
						user.setName(sub.getString("user"));
						list2.add(user);
					}
					list.removeAll(list);
					list.addAll(list2);
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getApplicationContext(), "网络异常！",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"json数据解析异常" + e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_attendance);
		intent=getIntent();
		ViewUtils.inject(this);
		initData();
		getAllUsers();
	}

	private void getAllUsers() {
		String url = Const.baseUrl + "//bmk/solar/gerAllUsers";
		showDialog("请稍候，请求中....").show();
		BaseInterfaceThread.postRequestInterface(SelectAttdanceActivity.this,
				handler, url, null);
	}

	private void initData() {
		title.setText("选择考勤人");
		rightTv.setText("确定");
		adapter = new SelectAttAdapter(getBaseContext(), list);
		adapter.setSelectPositon(-1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		addAtt.setVisibility(View.GONE);
	}

	@OnClick({ R.id.leftLayout, R.id.rightTv })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rightTv:
			if (adapter.getSelectPositon() == -1) {
				Toast.makeText(getBaseContext(), "请先选择考勤人哦", Toast.LENGTH_LONG)
						.show();
				return;
			}
			Intent intent = new Intent(getBaseContext(),
					AttAddFaceActivity.class);
			intent.putExtra("type", 1);
			intent.putExtra("executor", list.get(adapter.getSelectPositon())
					.getId());
			intent.putExtra("username", list.get(adapter.getSelectPositon())
					.getName());
			startActivityForResult(intent, 2000);
			break;
		case R.id.leftLayout:
			finish();
			break;

		default:
			break;
		}
	}

	private Intent intent;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.setResult(3000, intent);
		finish();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		adapter.setSelectPositon(position);
		adapter.notifyDataSetChanged();
	}
}
