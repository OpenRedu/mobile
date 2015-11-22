package br.ufpe.cin.openredu.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import br.ufpe.cin.openredu.util.cache.CacheController;
import br.ufpe.cin.openredu.util.cache.HttpManager;
import br.ufpe.cin.openredu.util.cache.IOUtilities;


public class ImageLoader {

	private static final String TAG = "ImageLoader";

	//TODO Add memory cache
	public static Bitmap loadBitmap(Context applicationContext, String url) throws Exception {
		return loadBitmap(applicationContext, url, true);
	}

	public static Bitmap loadBitmap(Context applicationContext, String url, boolean useCache) throws Exception {
		return loadBitmap(applicationContext, url, useCache, 0, 0);
	}

	public static Bitmap loadBitmap(Context applicationContext, String url, boolean useCache, int reqWidth, int reqHeight) throws Exception {

		url = url.replace(" ", "%20");

		Log.v(TAG, "Loading image: " + url);

		CacheController cacheController = CacheController.getInstance(applicationContext);

		if (useCache) {

			//First, try to load from the offline cache
			try {
				InputStream rawData = cacheController.getCachedFileOnDisk(url);
				return BitmapFactory.decodeStream(rawData);


			} catch (FileNotFoundException e) {

				//If there is no cached file, try to load from the assets folder
				try {
					InputStream rawData = cacheController.getCachedOnAssets(url);
					return decodeSampledBitmapFromStream(rawData, useCache, reqWidth, reqHeight);

				} catch (IOException e1) {

					//If there is no asset, load from the web
					return loadFromWeb(url, cacheController, useCache, reqWidth, reqHeight);
				}
			}

		} else {
			return loadFromWeb(url, cacheController, useCache, reqWidth, reqHeight);
		}
	}

	public static Bitmap decodeSampledBitmapFromStream(InputStream is, boolean useCache, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(useCache, options.outWidth, options.outHeight, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(is, null, options);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int id, boolean useCache, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, id, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(useCache, options.outWidth, options.outHeight, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, id, options);
	}

	public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, boolean useCache, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, offset, length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(useCache, options.outWidth, options.outHeight, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, offset, length, options);
	}

	private static int calculateInSampleSize(boolean useCache, int srcWidth, int srcHeight, int desiredWidth, int desiredHeight) {

		int inSampleSize = 1;

		if (desiredWidth > 0 && desiredHeight > 0 && srcWidth > desiredWidth && srcHeight > desiredHeight) {

			int srcDimension;
			int desiredDimension;

			if (srcWidth > srcHeight) {
				srcDimension = srcWidth;
				desiredDimension = desiredWidth;
			} else {
				srcDimension = srcHeight;
				desiredDimension = desiredHeight;
			}

			if (useCache) {
				inSampleSize = Math.round((float) srcDimension / (float) desiredDimension);

			} else {
				//If we won't cache, use powers of 2 (faster to decode)
				while (srcDimension / 2 > desiredDimension) {
					srcDimension /= 2;
					inSampleSize *= 2;
				}
			}
		}

		return inSampleSize;
	}

	private static Bitmap loadFromWeb(String url, CacheController cacheController, boolean useCache, int reqWidth, int reqHeight) {

		Bitmap bitmap = null;

		final HttpGet get = new HttpGet(url);
		HttpEntity entity = null;

		try {
			final HttpResponse response = HttpManager.execute(get);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				entity = response.getEntity();

				InputStream in = null;
				OutputStream out = null;

				try {
					in = entity.getContent();

					final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
					out = new BufferedOutputStream(dataStream, IOUtilities.IO_BUFFER_SIZE);
					IOUtilities.copy(in, out);
					out.flush();

					final byte[] data = dataStream.toByteArray();

					bitmap = decodeSampledBitmapFromByteArray(data, 0, data.length, useCache, reqWidth, reqHeight);

					if (useCache) {

						//Cache the sampled bitmap, not the whole data from the web
						ByteArrayOutputStream cacheOS = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, cacheOS);
						cacheController.cache(url, cacheOS.toByteArray());
						cacheOS.close();
					}

				} catch (IOException e) {
					Log.e(TAG, "Could not load image from " + url, e);
				} finally {
					IOUtilities.closeStream(in);
					IOUtilities.closeStream(out);
				}
			} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
				entity = response.getEntity();
			}
		} catch (IOException e) {
			Log.e(TAG, "Could not load image from " + url, e);
		} finally {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e) {
					Log.e(TAG, "Could not load image from " + url, e);
				}
			}
		}

		return bitmap;
	}

}
