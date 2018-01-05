package com.bst.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lidroid.xutils.util.LogUtils;

@SuppressWarnings("deprecation")
public class MyHttpClient {
	private HttpClient httpClient;

	public MyHttpClient() {
		httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 25 * 1000);/* 连接超时 */
		params.setParameter(HttpConnectionParams.SO_TIMEOUT, 25 * 1000);/* 请求超时 */
		// /* 连接超时 */
		// HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
		// 25 * 1000);
		// /* 请求超时 */
		// HttpConnectionParams.setSoTimeout(httpClient.getParams(), 25 * 1000);
	}

	/**
	 * httpPost请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String httpPassPost(String url, Map<?, ?> map) throws Exception {
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "soapui");
		if (map != null) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Iterator<?> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				params.add(new BasicNameValuePair(entry.getKey().toString(),
						entry.getValue().toString()));

			}
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}

		String strResult = null;
		HttpResponse httpResponse = httpClient.execute(post);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			// LogUtils.println("strResult:" + strResult);
			/* 读返回数据 */
			strResult = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("strResult" + strResult);

		} else {
			strResult = "Error Response: "
					+ httpResponse.getStatusLine().toString();
		}
		return strResult;
	}

	public static String GetSessionID() {
		String SessionID = "";
		if (SessionID == "") {
			String arr = "qwertyuiopasdfghjklzxcvbnm1234";
			for (int i = 0; i < 24; i++) {
				int index = (int) (30 * Math.random());
				SessionID += arr.substring(index, index + 1);

			}
		}
		return SessionID;
	}
}
