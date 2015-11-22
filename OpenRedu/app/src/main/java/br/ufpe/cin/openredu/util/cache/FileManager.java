package br.ufpe.cin.openredu.util.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

@SuppressLint("WorldReadableFiles")
public class FileManager {

	private final Context context;
	private boolean usingExternalStorage = false;
	private String externalMemoryPath;

	/**
	 * To use me, you must add the following permission to your AndroidManifest.xml:
	 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	 * 
	 * @param ctx Activity.getApplicationContext()
	 */
	public FileManager(Context ctx) {

		context = ctx;

		//Verify if there is any external
		if (isExternalStorageAvailable()) {
			usingExternalStorage = true;

			//Creates external memory path
			String androidDataDirectoryPath = Environment.getExternalStorageDirectory().getPath() + "/data/data/";
			externalMemoryPath = androidDataDirectoryPath + context.getPackageName() + "/";

			//Verifies and create if necessary the application directory
			File applicationDir = new File(externalMemoryPath);
			if (!applicationDir.exists()) {
				applicationDir.mkdirs();
			}
		}
	}

	private boolean isExternalStorageAvailable() {
		//External Storage State
		String state = Environment.getExternalStorageState();

		//Verify if there is any external media
		return (Environment.MEDIA_MOUNTED.equals(state));
	}

	/**
	 * Returns the file that corresponds to the param fileName and the current storage source.
	 * @param fileName File name to be found.
	 * @return Returns the file localized in the current storage source.
	 */
	private File getFile(String fileName) {
		File file = null;

		if (usingExternalStorage && isExternalStorageAvailable()) {
			file = new File(externalMemoryPath + fileName);
		} else if (!usingExternalStorage) {
			file = context.getFileStreamPath(fileName);
		}
		return file;
	}


	/**
	 * Returns all files under the current storage source.
	 * @return Returns all files localized in the current storage source.
	 */
	public File[] getAllFiles() {
		File[] files = null;

		if(usingExternalStorage && isExternalStorageAvailable()) {
			files = new File(externalMemoryPath).listFiles();
		} else if (!usingExternalStorage) {
			files = context.getFilesDir().listFiles();
		}

		return files;
	}

	/**
	 * Returns a boolean indicating whether this file can be found on the
	 * underlying file system.
	 * 
	 * @param fileName
	 *            File name to be verified
	 * @return true if this file exists, false otherwise
	 */
	public boolean exists(String fileName) {
		return getFile(fileName).exists();
	}

	/**
	 * Delete the file.
	 * @param fileName File name to be deleted.
	 */
	private void eraseFile(String fileName) {
		File file = getFile(fileName);
		file.delete();
	}

	/**
	 * YOU SHOULD CLOSE THIS InputStream!
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	public FileInputStream openFileInputStream(String fileName) throws FileNotFoundException {
		FileInputStream inputStream = null;

		if (usingExternalStorage && isExternalStorageAvailable()) {
			inputStream = new FileInputStream(new File(externalMemoryPath + fileName));
		} else if (!usingExternalStorage) {
			inputStream = context.openFileInput(fileName);
		}
		return inputStream;
	}

	/**
	 * YOU SHOULD CLOSE THIS OutputStream!
	 * Warning: files world writeable
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("WorldWriteableFiles")
	@SuppressWarnings("resource")
	public FileOutputStream openFileOutputStream(String fileName) throws IOException {
		FileOutputStream outputStream = null;

		if (usingExternalStorage && isExternalStorageAvailable()) {
			File f = new File(externalMemoryPath + fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			outputStream = new FileOutputStream(f);
		} else if (!usingExternalStorage) {
			outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		}
		return outputStream;
	}

	/**
	 * Clean old files.
	 * This methods removes all internal and external memory files that contains the this.extension extension.
	 */
	public void eraseAllFiles() {
		//Clean internal old files
		String[] files = context.fileList();
		for (String fileName : files) {
			eraseFile(fileName);
		}

		//Clean external old files
		if (isExternalStorageAvailable()) {
			File rootDirectory = new File(externalMemoryPath);
			File[] externalFiles = rootDirectory.listFiles();

			if (files != null) {
				for (File file : externalFiles) {
					file.delete();
				}
			}
		}
	}
}
