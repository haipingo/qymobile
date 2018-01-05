package com.bst.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 图片压缩辅助类
 * 
 * 
 * @author Administrator
 * 
 */
public class ImageCompressHelper {
	/**
	 * 对要上传到服务器的图片进行压缩处理
	 * 
	 * @param srcPath
	 * @return
	 */
	public static byte[] upLoadPic(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float ww = 960f;
		float hh = 720f;
		int be = 1;
		if (w > 960 || h > 720) {
			be = (int) (w > h ? newOpts.outWidth / ww : newOpts.outHeight / hh);
		}
		newOpts.inSampleSize = be;
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// 进行压缩
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 80;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		return baos.toByteArray();

	}

	/**
	 * 通过图片路径获取到压缩后的图片
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		if (w == 0 || h == 0) {
			return null;
		}
		float hh = 200f;
		float ww = 200f;
		int be = 1;
		if (w <= ww || h <= hh) {
			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();
			// 计算缩放率
			float scaleWidth;
			float scaleHeight;
			if (w < ww && h < hh) {
				scaleWidth = (float) (250 / h);
				scaleHeight = (float) (250 / h);
			} else {
				if (w > h) {
					scaleWidth = w < 600 ? 1.5f : 1f;
					scaleHeight = (float) (250 / h);
				} else {
					scaleWidth = (float) (250 / w);
					scaleHeight = h < 600 ? 1.5f : 1f;
				}
			}
			// 缩放图片动作
			matrix.postScale(scaleWidth, scaleHeight);
			// 创建新的图片
			bitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(srcPath), 0,
					0, w, h, matrix, true);
		} else {
			be = w > h ? (int) (newOpts.outHeight / hh)
					: (int) (newOpts.outWidth / ww);
			newOpts.inSampleSize = be;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		}

		return compressImage(bitmap);
	}

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 80;
		while (baos.toByteArray().length / 1024 > 150) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 通过图片路径获取到压缩后的图片 仅供浏览缩略
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage4grid(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		float hh = 200f;
		float ww = dm.widthPixels / 5;
		int be = 1;

		if (h > hh) {
			be = (int) (newOpts.outHeight / hh);
		} else if (w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (be <= 0)
			be = 1;

		newOpts.inSampleSize = be;
		newOpts.inPurgeable = true;
		newOpts.inInputShareable = true;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);
	}

	public static Bitmap rotatePic(String path, Bitmap bitmap) {
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(path);
		} catch (IOException e) {
		}
		int orc = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_UNDEFINED);
		int degree = 0;
		if (orc == ExifInterface.ORIENTATION_ROTATE_90) {
			degree = 90;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
		} else if (orc == ExifInterface.ORIENTATION_ROTATE_270) {
			degree = 270;
		}
		Log.d("图片方向", "图片方向:" + degree);
		if (degree != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degree);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
		}
		return bitmap;
	}
}
