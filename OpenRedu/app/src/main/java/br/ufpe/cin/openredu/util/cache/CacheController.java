package br.ufpe.cin.openredu.util.cache;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.util.Log;

public class CacheController {

	private static CacheController instance;

	private Context mContext;
	private FileManager fileManager;
	private Map<String, SoftReference<LoadableModel>> resourceCache;
	private Map<String, Date> cacheTimestamps;

	public static CacheController getInstance(Context ctx) {
		if (instance == null) {
			instance = new CacheController(ctx);
		}

		return instance;
	}

	public CacheController(Context ctx) {
		mContext = ctx;
		this.fileManager = new FileManager(ctx);
		this.resourceCache = new HashMap<String, SoftReference<LoadableModel>>();
		this.cacheTimestamps = new HashMap<String, Date>();
	}

	private static String getFileName(String url) {

		return String.valueOf( url.hashCode() );
	}

	public void cache(String key, LoadableModel model) {

		if (model.cacheDuration <= 0) {
			Log.v("CacheController", "Skipping cache for " + key);
			return;
		}

		String fileName = getFileName(key);

		//Cache on memory
		SoftReference<LoadableModel> ref = new SoftReference<LoadableModel>(model);
		this.resourceCache.put(fileName, ref);
		this.cacheTimestamps.put(fileName, new Date());

		//Cache on disk
		try {
			FileOutputStream fos = this.fileManager.openFileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(model);

		} catch (IOException e) {
			Log.w("CacheController", "Couldn't cache " + key + " on disk. Does this application have write permissions?");
			e.printStackTrace();
		}
	}

	public void cache(String key, InputStream is) {

		String fileName = getFileName(key);

		//Cache on disk
		try {
			FileOutputStream fos = this.fileManager.openFileOutputStream(fileName);

			byte[] buffer = new byte[1024];
			int length;

			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}

			is.close();
			fos.close();

		} catch (IOException e) {
			Log.w("CacheController", "Couldn't cache " + key + " on disk. Does this application have write permissions?");
			e.printStackTrace();
		}
	}

	public void cache(String key, byte[] byteArray) {

		String fileName = getFileName(key);

		//Cache on disk
		try {
			FileOutputStream fos = this.fileManager.openFileOutputStream(fileName);
			fos.write(byteArray);
			fos.close();

		} catch (IOException e) {
			Log.w("CacheController", "Couldn't cache " + key + " on disk. Does this application have write permissions?");
			e.printStackTrace();
		}
	}

	public LoadableModel getCachedOnMemory(String key) {

		String fileName = getFileName(key);

		if (this.expired(fileName)) {

			//Remove reference from the map
			this.resourceCache.remove(fileName);
			return null;
		}

		return this.resourceCache.get(fileName).get();
	}

	public LoadableModel getCachedOnDisk(String key) {

		try {
			InputStream is = getCachedFileOnDisk(key);
			ObjectInputStream ois = new ObjectInputStream(is);
			return (LoadableModel) ois.readObject();

		} catch (Exception e) {
			return null;
		}
	}

	public InputStream getCachedFileOnDisk(String key) throws FileNotFoundException {
		String fileName = getFileName(key);
		return this.fileManager.openFileInputStream(fileName);
	}

	public InputStream getCachedOnAssets(String key) throws IOException {
		String fileName = getFileName(key);
		return mContext.getResources().getAssets().open(fileName);
	}

	private boolean expired(String key) {

		SoftReference<LoadableModel> reference = this.resourceCache.get(key);

		if (reference == null) {
			return true;
		}

		LoadableModel resource = reference.get();

		if (resource == null || resource.cacheDuration == 0) {
			return true;
		}

		Date cTimestamp = new Date();
		Date resTimestamp = this.cacheTimestamps.get(key);

		return resTimestamp == null
				|| (cTimestamp.getTime() - resTimestamp.getTime()) > resource.cacheDuration;
	}

	/**
	 * Copies the contents of one object into another.
	 * @return True if the copy went well, false otherwise.
	 */
	public static boolean copy(Object src, Object dest) {

		Class<?> srcClass = src.getClass();
		Class<?> destClass = dest.getClass();

		//Check if the two objects are from the same class
		if (!srcClass.getName().equals(destClass.getName())) {
			return false;
		}

		Field[] srcFields = srcClass.getDeclaredFields();

		for (int i = 0; i < srcFields.length; i++) {
			Field srcField = srcFields[i];

			//Copy the equal fields
			try {
				Field destField = destClass.getDeclaredField(srcField.getName());
				destField.set(dest, srcField.get(src));

			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}

		return true;
	}

	/* Testing Stuff * /
	private static class CopyableWebModel extends MM_LoadableModel {

		public String f1;
		public int f2;
		public boolean f3;
		public float f4;
		public double f5;
		public char f6;
		public long f7;
		public short f8;
		public byte f9;

		private String f10;
		private int f11;
		private boolean f12;
		private float f13;
		private double f14;
		private char f15;
		private long f16;
		private short f17;
		private byte f18;

		public String[] f19;

		public CopyableWebModel(String url) {
			super(url);
		}

		public void fill() {
			this.f1 = "l97khmw";
			this.f10 = "09865puhdx";
			this.f2 = this.f11 = 7;
			this.f3 = this.f12 = true;
			this.f4 = this.f13 = 0.05f;
			this.f5 = this.f14 = 0.3;
			this.f6 = this.f15 = 'b';
			this.f7 = this.f16 = 6706137252070781868L;
			this.f8 = this.f17 = 16535;
			this.f9 = this.f18 = 0x09;

			this.f19 = new String[]{"rcnthd", "nohteudcoe"};
		}
	}

	public static void main(String[] args) {

		CopyableWebModel src = new CopyableWebModel("TESTing");
		src.fill();
		CopyableWebModel dest = new CopyableWebModel("TESTing dest");

		copy(src, dest);
		boolean x = false;
	}
	/* */
}
