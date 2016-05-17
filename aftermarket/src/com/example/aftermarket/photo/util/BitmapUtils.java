package com.example.aftermarket.photo.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class BitmapUtils {
	/**
	 * 根据View（主要是ImageView）的宽 和 高来计算Bitmap缩放比例。默认不缩放。
	 * 
	 * @param options
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	private static int computeScale(Options options, int viewWidth, int viewHeight) {
		int inSampleSize = 1;
		if (viewWidth <= 0 || viewHeight <= 0) {
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		// 假如 Bitmap的宽度或高度大于 我们设定图片的View的宽高，则计算缩放比例
		if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewHeight);
			// 为了保证图片不压缩变形，我们取宽高 比例 最小的那个
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize + 2;
	}

	/**
	 * 移除过时的图片缓存
	 * 
	 * @param path
	 * @return
	 */
	private static boolean removeExpiredCache(String path) {
		boolean bool = false;
		File file = new File(path);
		if (file.exists()) {
			if (System.currentTimeMillis() - file.lastModified() > 1296000000L) {
				file.delete();
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * 从网络端下载图片
	 * 
	 * @param url
	 *            网络图片的URL地址
	 * @return bitmap 图片bitmap结构
	 * 
	 */
	public static Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		InputStream is = null;
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(10000);
			is = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return bitmap;
	}

	/**
	 * <p>
	 * 已最节省内存的方式 ， 根据资源id获取Bitmap。
	 * </p>
	 * 
	 * @param resId
	 *            资源id。
	 * @param context
	 *            context。
	 * @return Bitmap。
	 */
	public final static Bitmap getBitmap(int resId, Context context) {
		// TODO Auto-generated method stub
		InputStream is = null;
		Bitmap img = null;
		try {
			Options opt = new Options();
			opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			is = context.getResources().openRawResource(resId);

			img = BitmapFactory.decodeStream(is, null, opt);
		} catch (NotFoundException e1) {
			Log.d("yimi", e1.getMessage());
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
		return img;
	}

	public static File getTempImage() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return tempFile;
		}
		return null;
	}

	public static File getTempImage(int num) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return tempFile;
		}
		return null;
	}

	public static String saveBitmap(Context context, Bitmap bitmap) {
		return saveBitmap(context, bitmap, "一米兼职");
	}

	/**
	 *
	 */
	// 保存拍摄的照片到手机的sd卡
	public static String saveBitmap(Context context, Bitmap bitmap, String dic) {
		String fileName = "";
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null; // 字节数组输出流
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
			String saveDir = Environment.getExternalStorageDirectory() + "/" + dic;
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir(); // 创建文件夹
			}
			fileName = saveDir + "/" + System.currentTimeMillis() + ".png";
			File file = new File(fileName);
			file.delete();
			if (!file.exists()) {
				file.createNewFile();// 创建文件
				Log.e("PicDir", file.getPath());
				Toast.makeText(context, "已保存至" + fileName, Toast.LENGTH_LONG).show();
			}
			// 将字节数组写入到刚创建的图片文件中
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return fileName;
	}

	/**
	 * TODO<根据图片Uri获取图片地址>
	 * 
	 * @return String
	 */
	public static String getImgPathByUri(Context context, Uri uri) {
		String path = "";
		try {
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			path = cursor.getString(columnIndex);
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return path;
	}

	/**
	 * 
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// ���ݺ���ȷ������ֵ
		if (options.outWidth > options.outHeight) {
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 480, 800);
		} else {
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 800, 480);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 传进来一个图片的路径，返回压缩后的file
	 * 
	 * @param PPath
	 * @return
	 */
	public static File path2SmallFile(String PPath) {

		try {
			//
			Bitmap bm = getSmallBitmap(PPath);

			String PPPath = PPath.replace(".", "压缩.");

			File newFile = new File(PPPath);

			FileOutputStream fos = new FileOutputStream(newFile);

			bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);

			return newFile;

		} catch (Exception e) {
			Log.e("压缩出错", "error", e);
			return null;
		}
	}

}
