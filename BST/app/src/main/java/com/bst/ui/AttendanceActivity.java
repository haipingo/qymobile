package com.bst.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bst.Application;
import com.bst.BaseActivity;
import com.bst.R;
import com.bst.adapter.AttAdapter;
import com.bst.bean.CheckingBean;
import com.bst.biz.Const;
import com.bst.thread.BaseInterfaceThread;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class AttendanceActivity extends BaseActivity implements
		OnItemClickListener {
	@ViewInject(R.id.listView)
	private ListView listView;
	private AttAdapter adapter;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.rightTv)
	private TextView rightTv;

	private Application app = Application.getInstance();
	private List<CheckingBean> list = new ArrayList<CheckingBean>();

	@Override
	protected void handle(Message msg) {
		stopDialog();
		if (Const.SYSTEM_RESPONSE_SUCCESS == msg.what) {
			String reslut = (String) msg.obj;
			List<CheckingBean> list2 = new ArrayList<CheckingBean>();
			try {
				JSONObject jobj = new JSONObject(reslut);
				String flag = jobj.getString("succee");
				if ("1".equals(flag)) {
					JSONArray jy = jobj.getJSONArray("list");
					for (int i = 0; i < jy.length(); i++) {
						JSONObject sub = jy.getJSONObject(i);
						CheckingBean cbs = new CheckingBean();
						cbs.setId(sub.getString("id"));
						cbs.setUuid(sub.getString("uuid"));
						cbs.setUserId(sub.getString("userId"));
						cbs.setChecking(sub.getString("checking"));
						cbs.setCheckingtime(sub.getString("checkingtime"));
						cbs.setPhoto(sub.getString("photo"));
						cbs.setCode(sub.getString("code"));
						if (!sub.isNull("executor")) {
							cbs.setExecutor(sub.getString("executor"));
						}
						cbs.setPrecision(sub.getString("precisiona"));
						cbs.setDimensionality(sub.getString("dimensionality"));

						list2.add(cbs);
					}

					list.removeAll(list);
					list.addAll(list2);
					adapter.notifyDataSetChanged();

				} else {
					Toast.makeText(getApplicationContext(), "网络异常！",
							Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"json数据解析异常" + e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "网络异常！", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_attendance);
		ViewUtils.inject(this);
		initData();
	}

	private void initData() {
		title.setText("考勤列表");
		// rightTv.setText("对比照");
		GetData();
		adapter = new AttAdapter(getBaseContext(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@OnClick({ R.id.leftLayout, R.id.addAtt })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftLayout:
			finish();
			break;
		case R.id.addAtt:
			Intent intent = new Intent(getBaseContext(),
					SelectAttdanceActivity.class);
			startActivityForResult(intent, 2000);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		GetData();
	}

	private void GetData() {
		showDialog("请求中，请稍候").show();
		BaseInterfaceThread bift = new BaseInterfaceThread();
		String url = Const.baseUrl + "//bmk/solar/executor";
		String data = "{\"userid\":\"" + app.getUser("id") + "\",\"user\":\""
				+ app.getUser("user") + "\"}";
		Map<String, String> map = new HashMap<String, String>();
		map.put("data", data);
		bift.postRequestInterface(AttendanceActivity.this, handler, url, map);
	}

	@Override
	public void onItemClick(AdapterView<?> param, View view, int position,
			long arg3) {
		Intent intent = new Intent(getBaseContext(),
				AttendanceInfoActivity.class);
		intent.putExtra("checkingBean", list.get(position));
		startActivity(intent);
	}
}
