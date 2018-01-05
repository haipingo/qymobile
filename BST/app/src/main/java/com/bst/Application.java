package com.bst;

/**
 * 
 */

import java.util.Stack;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.content.SharedPreferences;

public class Application extends android.app.Application {

	private static Stack<Activity> activityStack;
	private static Application singleton;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		singleton = this;
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		// 自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
		// 包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
		SDKInitializer.setCoordType(CoordType.BD09LL);
	}

	// Returns the application instance
	public static Application getInstance() {
		return singleton;
	}

	public static Stack<Activity> getActivityStack() {
		return activityStack;
	}

	public static void setActivityStack(Stack<Activity> activityStack) {
		Application.activityStack = activityStack;
	}

	/**
	 * add Activity 锟斤拷锟紸ctivity锟斤拷栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * get current Activity 锟斤拷取锟斤拷前Activity锟斤拷栈锟斤拷锟斤拷锟揭伙拷锟窖癸拷锟侥ｏ拷
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷前Activity锟斤拷栈锟斤拷锟斤拷锟揭伙拷锟窖癸拷锟侥ｏ拷
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 锟斤拷锟斤拷指锟斤拷锟斤拷Activity
	 */
	public static void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 锟斤拷锟斤拷指锟斤拷锟斤拷锟斤拷锟斤拷Activity
	 */
	public static void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
				// System.out.println("------true-------");
			}
		}
	}

	public Boolean activityIsLive(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
		// int pid = android.os.Process.myPid();
		// android.os.Process.killProcess(pid);
	}

	private SharedPreferences sharedPreferences;

	public String getSettings() {
		sharedPreferences = getApplicationContext().getSharedPreferences(
				"setup", Activity.MODE_PRIVATE);
		String deviceNo = sharedPreferences.getString("deviceNo", null);
		return deviceNo;
	};

	public void saveSettings(String deviceNo) {
		if (sharedPreferences == null) {
			sharedPreferences = getApplicationContext().getSharedPreferences(
					"setup", Activity.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString("deviceNo", deviceNo).commit();
	};

	public void saveUser(String key, String value) {
		if (sharedPreferences == null) {
			sharedPreferences = getApplicationContext().getSharedPreferences(
					"setup", Activity.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}

	public String getUser(String key) {
		sharedPreferences = getApplicationContext().getSharedPreferences(
				"setup", Activity.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, null);
		return value;
	}
}
