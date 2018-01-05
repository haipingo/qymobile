package com.bst.location;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 百度定位工具类 A.K.
 * 
 * @author Administrator
 * 
 */
public class LocationTool {
	public static enum StoreItem {
		ClocData, // 定位数据
		SlocType, // 定位方式
		SlocAdd
		// 定位地址
	}

	protected LocationClient mLocClient = null;

	private Map<StoreItem, Object> locData = null;
	private Context context;
	private LocationClientOption option = new LocationClientOption();

	public LocationTool(Context context,
			CommonLocationListener cLocationListener) {
		this.context = context;
		locData = new HashMap<StoreItem, Object>();
		mLocClient = new LocationClient(this.context);
		cLocationListener.setClient(mLocClient);
		mLocClient.registerLocationListener(cLocationListener);
		option.setOpenGps(true);// 打开gps
		//option.setCoorType("bd09ll"); // 设置坐标类型
		option.setCoorType("gcj02"); // 设置坐标类型(大地坐标系)
		option.setScanSpan(5000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}

	public Map<StoreItem, Object> getLocData() {
		return locData;
	}

	/*
	 * 默认火星坐标系
	 */
	public void setCoorType(String coordinate) {
		option.setCoorType(coordinate);
	}

	/**
	 * 请求一次定位
	 * 
	 * @param et
	 */
	public void requestOnce() {
		mLocClient.start();
		mLocClient.requestLocation();
	}

	/**
	 * 开启
	 * 
	 * @param et
	 */
	public void start() {
		mLocClient.start();
	}

	public void request() {
		mLocClient.requestLocation();
	}

	public void stop() {
		mLocClient.stop();
	}

}
