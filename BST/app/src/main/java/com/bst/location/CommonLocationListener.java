package com.bst.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;


/**
 * 定位监听器
 * 
 * @author Administrator
 * 
 */
public class CommonLocationListener implements BDLocationListener {

	private LocationClient mLocClient;

	public CommonLocationListener() {
	}

	public void setClient(LocationClient mLocClient) {
		this.mLocClient = mLocClient;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null) {
			System.out.println("location---->null");
			return;
		}

		/*
		 * LocationData tempLocData = new LocationData(); tempLocData.latitude =
		 * location.getLatitude(); tempLocData.longitude =
		 * location.getLongitude(); // 如果不显示定位精度圈，将accuracy赋值为0即可
		 * tempLocData.accuracy = location.getRadius(); tempLocData.direction =
		 * location.getDerect();
		 */
	}

	protected void stop() {
		System.out.println("-----location end-----");
		mLocClient.stop();
	}

}