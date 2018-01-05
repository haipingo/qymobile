package com.bst.thread;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bst.biz.Const;
import com.bst.http.MyHttpClient;

public class BaseInterfaceThread {
	/** 接口返回结果 */
	private static String result;

	public static String getResult() {
		return result;
	}

	/**
	 * 调用请求接口，通过handle回调对请求结果进行处理
	 * 
	 * @param handler
	 *            回调对象
	 * @param url
	 *            接口地址(完整的地址如http://ip:port/server/login)
	 * @param map
	 *            请求参数，key为参数名，value为参数值
	 */
	public static void postRequestInterface(final Context context,
			final Handler handler, final String url, final Map<?, ?> map) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				try {
					MyHttpClient client = new MyHttpClient();
					result = client.httpPassPost(url, map);
					if (result != null) {
						msg.what = Const.SYSTEM_RESPONSE_SUCCESS;
						msg.obj = result;
						handler.sendMessage(msg);
					} else {
						msg.what = Const.SYSTEM_RESPONSE_FAILURE;
						handler.sendMessage(msg);
					}
				} catch (ConnectTimeoutException e) {
					result = e.getMessage();
					msg.what = Const.SYSTEM_RESPONSE_EXCEPTION;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (TimeoutException e) {
					result = e.getMessage();
					msg.what = Const.SYSTEM_RESPONSE_EXCEPTION;
					msg.obj = result;
					handler.sendMessage(msg);
				} catch (Exception e) {
					System.out.println("Exception");
					result = e.getMessage();
					msg.what = Const.SYSTEM_RESPONSE_EXCEPTION;
					msg.obj = result;
					handler.sendMessage(msg);
				}

			}
		}).start();
	}
}
