package com.bst.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	// private static final Logger log =
	// LoggerFactory.getLogger(DateUtil.class);

	public static String getCurrentTimeStr() {
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(currentTime);
	}
	
	public static String getCurrentTimeStr2() {
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(currentTime);
	}
	public static String getCurrentTimeStr3() {
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(currentTime);
	}
	
	public static String getCurrentTimeStr(String ft) {
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat(ft);
		return format.format(currentTime);
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String parseTimestampToString(Timestamp timestamp, String ft) {
		String result = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat(ft);
			result = df.format(timestamp);
		} catch (Exception e) {
			// log.debug(e.getMessage(), e);
		}
		return result;
	}

	public static Timestamp parseStringToTimestamp(String timestamp, String ft) {
		Timestamp result = null;
		try {
			if (timestamp != null && !"".equals(timestamp)) {
				SimpleDateFormat df = new SimpleDateFormat(ft);
				SimpleDateFormat dfResult = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = df.parse(timestamp);
				result = Timestamp.valueOf(dfResult.format(date));
			}
		} catch (Exception e) {
			// log.debug(e.getMessage(), e);
		}
		return result;
	}

	public static Timestamp parseDateToTimestamp(Date date, String ft) {
		Timestamp timesamp = null;
		if (date != null) {
			try {
				SimpleDateFormat df = new SimpleDateFormat(ft);
				String result = df.format(date);
				timesamp = Timestamp.valueOf(result);
			} catch (Exception e) {
				// log.debug(e.getMessage(), e);
			}
		}
		return timesamp;
	}

	public static String parseDateToString(Date date, String ft) {
		String result = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat(ft);
			result = df.format(date);
		} catch (Exception e) {
			// log.debug(e.getMessage(),e);
		}
		return result;
	}

	public static String monthLessOne(String dateString) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = dateFormat.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.MONTH,-1);//日期减1个月
			return dateFormat.format(cal.getTime());
		} catch (Exception e) {
			return "0000-00-00";
		}
	}
	
	public static String monthAddOne(String dateString) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = dateFormat.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.MONTH,1);//日期加1个月
			return dateFormat.format(cal.getTime());
		} catch (Exception e) {
			return "0000-00-00";
		}
	}
	

	/**
	 * 计算当前月的开始时间
	 */
	public static String getStartDate4Month() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;// 当前月份
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDate = cal.getTime();
		return dateFormat.format(firstDate);
	}

	/**
	 * 计算当前月的结束时间
	 * 
	 * @return
	 */
	public static String getEndDate4Month() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;// 当前月份
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDate = cal.getTime();
		return dateFormat.format(lastDate);
	}

	public static int getSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		int length = timestamp.length();
		if (length > 3) {
			return Integer.valueOf(timestamp.substring(0, length - 3));
		} else {
			return 0;
		}
	}

	public static String getLastDay(String datadate) throws Exception {
		Date date = null;
		String day_last = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		date = format.parse(datadate);

		// 创建日历
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置为该月第一天
		calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
		day_last = format.format(calendar.getTime());
		return day_last;
	}

	public static String getFirstDay(String datadate) throws Exception {
		Date date = null;
		String day_first = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		date = format.parse(datadate);

		// 创建日历
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		day_first = format.format(calendar.getTime());
		return day_first;
	}
}
