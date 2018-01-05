package com.bst.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.util.support.Base64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {
	/**
	 * bitmap转换成base64字符串
	 * 
	 * @param cameraBitmap
	 * @return
	 */
	public static String bitmapTobase64(Bitmap cameraBitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		while (baos.toByteArray().length / 1024 > 60) { // 循环判断如果压缩后图片是否大于设定值kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		byte[] datas = baos.toByteArray();
		return Base64.encodeBytes(datas);
	}

	public static Bitmap base64Tobitmap(String base64) {
		Bitmap bitmap = null;
		try {
			byte[] datas = Base64.decode(base64);
			bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 把图片byte流转换成bitmap
	 */
	public static Bitmap byteToBitmap(byte[] data) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		int i = 0;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				b = BitmapFactory
						.decodeByteArray(data, 0, data.length, options);
				break;
			}
			i += 1;
		}
		return b;
	}
}
