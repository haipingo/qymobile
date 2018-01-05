package com.bst.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bst.Application;
import com.bst.BaseActivity;
import com.bst.R;
import com.bst.biz.Const;
import com.bst.thread.BaseInterfaceThread;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginActivity extends BaseActivity {
	@ViewInject(R.id.leftBackIcon)
	private ImageView leftBackIcon;

	@ViewInject(R.id.pwd)
	private EditText pwd;
	@ViewInject(R.id.name)
	private EditText name;
	@ViewInject(R.id.loginButton)
	private Button loginButton;

	@Override
	protected void handle(Message msg) {
		stopDialog();
		if(Const.SYSTEM_RESPONSE_SUCCESS == msg.what) {
			String reslut = (String)msg.obj;
			try {
				JSONObject jobj = new JSONObject(reslut);
				String flag = jobj.getString("succee");
				if("1".equals(flag)) {
					Application app = Application.getInstance();
					app.saveUser("id", jobj.getString("id"));
					app.saveUser("user", jobj.getString("user"));
					app.saveUser("passwd", jobj.getString("passwd"));
					app.saveUser("sex", jobj.getString("sex"));
					app.saveUser("position", jobj.getString("position"));
					app.saveUser("department", jobj.getString("department"));
					try {
						app.saveUser("photobase64", jobj.getString("photobase64"));
					} catch (Exception e) {
						app.saveUser("photobase64", "null");
					}
					startActivity(new Intent(getBaseContext(), MainActivity.class));
					finish();
				} else if("0".equals(flag)) {
					Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"json数据解析异常" + e.getMessage(), Toast.LENGTH_LONG);
			}
		} else {
			Toast.makeText(getApplicationContext(), "登录失败！", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		initData(); 
	}

	private void initData() {
		loginButton.setBackgroundColor(Color.parseColor("#0077C9"));
		leftBackIcon.setVisibility(View.GONE);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				startActivity(new Intent(getBaseContext(),AttendanceInfoActivity.class));
				
				String userName = name.getText().toString().trim();
				String passwd = pwd.getText().toString().trim();
				if(userName == null || "".equals(userName)) {
					Toast.makeText(getApplicationContext(), "用户名不能为空！", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(passwd == null || "".equals(passwd)) {
					Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_LONG).show();
					return;
				}

				showDialog("请求中，请稍候").show();
				BaseInterfaceThread bift = new BaseInterfaceThread();
				String url =Const.baseUrl+"//bmk/solar/authlogin";
				String data = "{\"user\":\"" + userName + "\",\"passwd\":\"" + passwd +"\"}";
				Map<String, String> map = new HashMap<String, String>();
				map.put("data", data);
				bift.postRequestInterface(LoginActivity.this, handler, url, map);
			}
		});
	}
}
