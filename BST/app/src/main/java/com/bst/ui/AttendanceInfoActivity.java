package com.bst.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bst.BaseActivity;
import com.bst.R;
import com.bst.bean.CheckingBean;
import com.bst.utils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AttendanceInfoActivity extends BaseActivity {

	@ViewInject(R.id.leftLayout)
	private RelativeLayout leftLayout;

	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.addr)
	private TextView addr;
	@ViewInject(R.id.time)
	private TextView time;
	@ViewInject(R.id.state)
	private TextView state;
	@ViewInject(R.id.photo)
	private ImageView photo;

	@ViewInject(R.id.bmapView)
	private MapView mMapView;

	private CheckingBean checkingBean;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_attendance_info);
		ViewUtils.inject(this);
		checkingBean = (CheckingBean) getIntent().getSerializableExtra(
				"checkingBean");
		initData();
		leftLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private GeoCoder geoCoder;

	private void initData() {
		title.setText("考勤详情");
		if (checkingBean != null) {
			addr.setText("iiii");
			time.setText(checkingBean.getCheckingtime());
			state.setText(checkingBean.getCode());
			photo.setImageBitmap(BitmapUtils.base64Tobitmap(checkingBean
					.getPhoto()));
		}
		// latitude:22.635427 longitude:114.018388
		BaiduMap mBaidumap = mMapView.getMap();
		// 设定中心点坐标
//		 LatLng cenpt = new LatLng(19.911285, 109.680919);

		LatLng cenpt = new LatLng(Double.valueOf(checkingBean
				.getDimensionality()), Double.valueOf(checkingBean
				.getPrecision()));

		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18)
				.build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaidumap.setMapStatus(mMapStatusUpdate);
		// 开启定位图层
		mBaidumap.setMyLocationEnabled(true);
		// 创建地理编码检索实例
		geoCoder = GeoCoder.newInstance();
		myLatLng(cenpt);
	}

	/**
	 * 经纬度转换成地址
	 */

	private void myLatLng(LatLng latLng) {
		// 设置反地理经纬度坐标,请求位置时,需要一个经纬度
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		// 设置地址或经纬度反编译后的监听,这里有两个回调方法,
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

				// addre = "地址："+reverseGeoCodeResult.getAddress();
				// Log.i(TAG,
				// "onGetReverseGeoCodeResult: "+reverseGeoCodeResult.getAddress());
			}

			/**
			 * 
			 * @param reverseGeoCodeResult
			 */
			@Override
			public void onGetReverseGeoCodeResult(
					ReverseGeoCodeResult reverseGeoCodeResult) {

				if (reverseGeoCodeResult == null
						|| reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(AttendanceInfoActivity.this, "找不到该地址！",
							Toast.LENGTH_SHORT).show();
				} else {
					// Toast.makeText(AttendanceInfoActivity.this,
					// "地址：" + reverseGeoCodeResult.getAddress(),
					// Toast.LENGTH_SHORT).show();
					addr.setText(reverseGeoCodeResult.getAddress());
				}

			}
		});
	}
}
