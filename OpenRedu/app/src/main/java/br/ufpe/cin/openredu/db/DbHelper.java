package br.ufpe.cin.openredu.db;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.developer.redu.models.Link;
import br.com.developer.redu.models.Status;
import br.com.developer.redu.models.Thumbnail;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.util.DateUtil;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "redu.db";
	private static final int DATABASE_VERSION = 1;

	private static List<WeakReference<DbHelperListener>> mListeners;
	
	// TABLE APP_USER
	private static class AppUserTable {
		public static final String NAME = "AppUser";
		
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_OLDEST_STATUSES_WERE_DOWNLOADED = "oldest_statuses_were_downloaded";
		
		public static final String CREATE = "CREATE TABLE "
				+ NAME + "(" 
				+ COLUMN_ID + " TEXT PRIMARY KEY, "
				+ COLUMN_OLDEST_STATUSES_WERE_DOWNLOADED + " INTEGER );";
	}
	
	// TABLE STATUS
	private static class StatusTable {
		public static final String NAME = "Status";
		
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_APP_USER_ID = "app_user_id";
		public static final String COLUMN_USER_ID = "user_id";
		public static final String COLUMN_TYPE = "type";
		public static final String COLUMN_ANSWERS_COUNT = "answers_count";
		public static final String COLUMN_LOGEABLE_TYPE = "logeable_type";
		public static final String COLUMN_CREATED_AT_IN_MILLIS = "created_at_in_millis";
		public static final String COLUMN_LAST_SEEN_AT_IN_MILLIS = "last_seen_at_in_millis";
		public static final String COLUMN_TEXT = "text";
		public static final String COLUMN_LECTURE_ALREADY_SEEN = "lecture_already_seen";
		public static final String COLUMN_LAST_SEEN = "last_seen";
		public static final String COLUMN_IGNORABLE_IN_SYNC = "ignorable_in_sync";
		
		public static final String CREATE = "CREATE TABLE "
				+ NAME + "(" 
				+ COLUMN_ID + " TEXT PRIMARY KEY, " 
				+ COLUMN_APP_USER_ID + " INTEGER, " 
				+ COLUMN_USER_ID + " INTEGER, " 
				+ COLUMN_TYPE + " TEXT NOT NULL, " 
				+ COLUMN_ANSWERS_COUNT + " INTEGER, " 
				+ COLUMN_LOGEABLE_TYPE + " TEXT, " 
				+ COLUMN_CREATED_AT_IN_MILLIS + " INTEGER, "
				+ COLUMN_LECTURE_ALREADY_SEEN + " TEXT NOT NULL, "
				+ COLUMN_LAST_SEEN + " INTEGER, "
				+ COLUMN_LAST_SEEN_AT_IN_MILLIS + " INTEGER, "
				+ COLUMN_TEXT + " TEXT NOT NULL, "
				+ COLUMN_IGNORABLE_IN_SYNC + " INTEGER, "
				+ "FOREIGN KEY(" + COLUMN_APP_USER_ID + ") REFERENCES " + AppUserTable.NAME + "(" + AppUserTable.COLUMN_ID + "), "
				+ "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + UserTable.NAME + "(" + UserTable.COLUMN_ID + "));";
	}

	// TABLE LINK
	private static class LinkTable {
		public static final String NAME = "Link";
		
		public static final String COLUMN_STATUS_ID = "status_id";
		public static final String COLUMN_REL = "rel";
		public static final String COLUMN_HREF = "href";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_PERMALINK = "permalink";
		
		public static final String CREATE = "CREATE TABLE "
				+ NAME + "(" 
				+ COLUMN_STATUS_ID + " TEXT, " 
				+ COLUMN_REL + " TEXT, " 
				+ COLUMN_HREF + " TEXT, " 
				+ COLUMN_NAME + " TEXT, " 
				+ COLUMN_PERMALINK + " TEXT, " 
				+ "FOREIGN KEY(" + COLUMN_STATUS_ID + ") REFERENCES " + StatusTable.NAME + "(" + StatusTable.COLUMN_ID + "), "
				+ "PRIMARY KEY(" + COLUMN_STATUS_ID + ", " + COLUMN_REL + "));";
	}
	
	// TABLE THUMBNAIL
	private static class ThumbnailTable {
		public static final String NAME = "Thumbnail";
		
		public static final String COLUMN_USER_ID = "user_id";
		public static final String COLUMN_HREF = "href";
		public static final String COLUMN_SIZE = "size";
		
		public static final String CREATE = "CREATE TABLE "
				+ NAME + "(" 
				+ COLUMN_USER_ID + " TEXT, " 
				+ COLUMN_HREF + " TEXT, " 
				+ COLUMN_SIZE + " TEXT, " 
				+ "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + UserTable.NAME + "(" + UserTable.COLUMN_ID + "), "
				+ "PRIMARY KEY(" + COLUMN_USER_ID + ", " + COLUMN_SIZE + "));";
	}	
	
	// TABLE USER
	private static class UserTable {
		public static final String NAME = "User";
		
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_FIRST_NAME = "first_name";
		public static final String COLUMN_LAST_NAME = "last_name";
		
		private static final String CREATE = "CREATE TABLE "
				+ NAME + "(" 
				+ COLUMN_ID + " INTEGER, " 
				+ COLUMN_FIRST_NAME + " TEXT, " 
				+ COLUMN_LAST_NAME + " TEXT );";
	}
	
	private static DbHelper instance;
	
	private DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }  

	public static DbHelper getInstance(Context context) {
		if(instance == null) {
			instance = new DbHelper(context);
		}
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(StatusTable.CREATE);
		database.execSQL(UserTable.CREATE);
		database.execSQL(LinkTable.CREATE);
		database.execSQL(ThumbnailTable.CREATE);
		database.execSQL(AppUserTable.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DbHelper.class.getName(), 
				"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + StatusTable.NAME);
		db.execSQL("DROP TABLE IF EXISTS " + LinkTable.NAME);
		db.execSQL("DROP TABLE IF EXISTS " + UserTable.NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ThumbnailTable.NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AppUserTable.NAME);
		onCreate(db);
	}
	
	public void addDbHelperListener(DbHelperListener listener) {
		if(mListeners == null) {
			mListeners = new ArrayList<WeakReference<DbHelperListener>>();
		}
		mListeners.add(new WeakReference<DbHelperListener>(listener));
	}
	
	synchronized public Status getStatus(String id) {
		Status status = null;
		
		SQLiteDatabase db = this.getReadableDatabase();  
	
		Cursor cursor;  
		cursor = db.query(StatusTable.NAME, null, 
				StatusTable.COLUMN_ID + " = ?", new String[]{id}, 
				null, null, null, null);
		
		if(cursor.moveToFirst()) {			
			status = getStatus(db, cursor);
		}
		
		cursor.close();
		db.close();
		
		return status;
	}
	
	synchronized public List<Status> getStatuses(long timestamp, boolean olderThan, int count) {
		List<Status> statuses = new ArrayList<Status>(count);  

		SQLiteDatabase db = this.getReadableDatabase();  
        Cursor cursor;  
  
        cursor = db.query(StatusTable.NAME, null, 
        		StatusTable.COLUMN_CREATED_AT_IN_MILLIS + (olderThan ? "<" : ">") + "?", 
        		new String[] {String.valueOf(timestamp)}, 
        		null, null, 
        		StatusTable.COLUMN_CREATED_AT_IN_MILLIS + " DESC", 
        		String.valueOf(count));  
  
    	while(cursor.moveToNext()) {
			Status status = getStatus(db, cursor);
			statuses.add(status);
    	}
        
        cursor.close();  
        db.close();
  
        return statuses;  
	}
	
	synchronized public List<Status> getNewLecturesStatuses(long timestamp, boolean olderThan, int count) {
		List<Status> statuses = new ArrayList<Status>(count);  
		
		SQLiteDatabase db = this.getReadableDatabase();  
		Cursor cursor;  
		
		cursor = db.query(StatusTable.NAME, null, 
				StatusTable.COLUMN_CREATED_AT_IN_MILLIS + (olderThan ? "<" : ">") + " ? AND " + StatusTable.COLUMN_LOGEABLE_TYPE + " = ?", 
				new String[] {String.valueOf(timestamp), Status.LOGEABLE_TYPE_LECTURE}, 
				null, null, 
				StatusTable.COLUMN_CREATED_AT_IN_MILLIS + " DESC", 
				String.valueOf(count));  
		
		while(cursor.moveToNext()) {
			Status status = getStatus(db, cursor);
			statuses.add(status);
		}
		
		cursor.close();  
		db.close();
		
		return statuses;  
	}
	
	synchronized public List<Status> getLastSeenStatuses(long timestamp, boolean olderThan, int count) {
		List<Status> statuses = new ArrayList<Status>(count);  
		
		SQLiteDatabase db = this.getReadableDatabase();  
		Cursor cursor;  
		
		cursor = db.query(StatusTable.NAME, null, 
				StatusTable.COLUMN_LAST_SEEN_AT_IN_MILLIS + (olderThan ? "<" : ">") + "? AND " + StatusTable.COLUMN_LAST_SEEN + " = 1", 
				new String[] {String.valueOf(timestamp)}, 
				null, null, 
				StatusTable.COLUMN_LAST_SEEN_AT_IN_MILLIS + " DESC", 
				String.valueOf(count));  
		
		while(cursor.moveToNext()) {
			Status status = getStatus(db, cursor);
			statuses.add(status);
		}
		
		cursor.close();
		db.close();
		
		return statuses;  
	}
	
	synchronized public List<Status> getStatusesBySpace(long timestamp, boolean olderThan, int count, String spaceId) {
		List<Status> statuses = new ArrayList<Status>(count);  
		
		SQLiteDatabase db = this.getReadableDatabase();  
		Cursor cursor;  

		String query = "SELECT * FROM " + StatusTable.NAME + 
				" WHERE " + StatusTable.COLUMN_CREATED_AT_IN_MILLIS + (olderThan ? "<" : ">") + timestamp + " AND " + 
				StatusTable.COLUMN_ID + " IN " + 
					"(SELECT " + LinkTable.COLUMN_STATUS_ID + " FROM " + LinkTable.NAME + 
						" WHERE " + LinkTable.COLUMN_REL + " = \"" + Link.REL_SPACE + "\" AND " + 
						LinkTable.COLUMN_HREF + " = " + "\"http://www.redu.com.br/api/spaces/" + spaceId + "\")" + 
				" ORDER BY " + StatusTable.COLUMN_CREATED_AT_IN_MILLIS + " DESC" +
				" LIMIT " + count; 
		
		cursor = db.rawQuery(query, null);
		
		while(cursor.moveToNext()) {
			Status status = getStatus(db, cursor);
			statuses.add(status);
		}
		
		cursor.close();
		db.close();
		
		return statuses;  
	}
	
	synchronized public List<Status> getStatusesByLecture(long timestamp, boolean olderThan, int count, int lectureId) {
		List<Status> statuses = new ArrayList<Status>(count);  
		
		SQLiteDatabase db = this.getReadableDatabase();  
		Cursor cursor;  
		
		String query = "SELECT * FROM " + StatusTable.NAME + 
				" WHERE " + StatusTable.COLUMN_CREATED_AT_IN_MILLIS + (olderThan ? "<" : ">") + timestamp + " AND " + 
				StatusTable.COLUMN_ID + " IN " + 
				"(SELECT " + LinkTable.COLUMN_STATUS_ID + " FROM " + LinkTable.NAME + 
				" WHERE " + LinkTable.COLUMN_REL + " = \"" + Link.REL_LECTURE + "\" AND " + 
				LinkTable.COLUMN_HREF + " LIKE " + "\"http://www.redu.com.br/api/lectures/" + lectureId + "%\")" + 
				"ORDER BY " + StatusTable.COLUMN_CREATED_AT_IN_MILLIS + " DESC " +
				"LIMIT " + count; 
		
		cursor = db.rawQuery(query, null);
		
		while(cursor.moveToNext()) {
			Status status = getStatus(db, cursor);
			statuses.add(status);
		}
		
		cursor.close();
		db.close();
		
		return statuses;  
	}
	
	private Status getStatusInCursor(Cursor cursor) {
		Status status = new Status();

		status.id = cursor.getString(cursor.getColumnIndex(StatusTable.COLUMN_ID));
		status.type = cursor.getString(cursor.getColumnIndex(StatusTable.COLUMN_TYPE));
		status.answers_count = cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_ANSWERS_COUNT));
		status.logeable_type = cursor.getString(cursor.getColumnIndex(StatusTable.COLUMN_LOGEABLE_TYPE));
		status.text = cursor.getString(cursor.getColumnIndex(StatusTable.COLUMN_TEXT));
		status.createdAtInMillis = cursor.getLong(cursor.getColumnIndex(StatusTable.COLUMN_CREATED_AT_IN_MILLIS));
		status.lastSeenAtInMillis = cursor.getLong(cursor.getColumnIndex(StatusTable.COLUMN_LAST_SEEN_AT_IN_MILLIS));
		
		int lectureAreadySeen = cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_TEXT));
		status.lectureAreadySeen = (lectureAreadySeen != 0);
		
		int lastSeen = cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_LAST_SEEN));
		status.lastSeen = (lastSeen != 0);
		
		int ignorableInSync = cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_IGNORABLE_IN_SYNC));
		status.ignorableInSync = (ignorableInSync != 0);
		
		status.user = new User();
		status.user.id = cursor.getInt(cursor.getColumnIndex(StatusTable.COLUMN_USER_ID));
		
		return status;
	}
	
	private Status getStatus(SQLiteDatabase db, Cursor cursor) {
		Status status = getStatusInCursor(cursor);
		status.user = getUser(db, status);
		status.links = getLinks(db, status);
		return status;
	}
	
	private User getUser(SQLiteDatabase db, Status status) {
		User user = new User();
		
		Cursor cursor;
		cursor = db.query(UserTable.NAME, null, 
				UserTable.COLUMN_ID + " = ?", 
				new String[] {String.valueOf(status.user.id)}, 
				null, null, null, null);
		
		if(cursor.moveToFirst()) {
			user.id = cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_ID));
			user.first_name = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_FIRST_NAME));
			user.last_name = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_LAST_NAME));
			user.thumbnails = getThumbnails(db, status.user);
    	}
		
		cursor.close();
		
		return user;
	}
	
	private List<Thumbnail> getThumbnails(SQLiteDatabase db, User user) {
		List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
		
		Cursor cursor;
		cursor = db.query(ThumbnailTable.NAME, null, 
				ThumbnailTable.COLUMN_USER_ID + " = ?", 
				new String[] {String.valueOf(user.id)}, 
				null, null, null, null);
		
		while(cursor.moveToNext()) {
			Thumbnail thumbnail = new Thumbnail();
			thumbnail.size = cursor.getString(cursor.getColumnIndex(ThumbnailTable.COLUMN_SIZE));
			thumbnail.href = cursor.getString(cursor.getColumnIndex(ThumbnailTable.COLUMN_HREF));

			thumbnails.add(thumbnail);
		}
		
		cursor.close();
		
		return thumbnails;
	}
	
	private List<Link> getLinks(SQLiteDatabase db, Status status) {
		List<Link> links = new ArrayList<Link>();
		
		Cursor cursor;
		cursor = db.query(LinkTable.NAME, null, 
				LinkTable.COLUMN_STATUS_ID + " = ?", 
				new String[] {String.valueOf(status.id)}, 
				null, null, null, null);
		
		while(cursor.moveToNext()) {
			Link link = new Link();
			link.rel = cursor.getString(cursor.getColumnIndex(LinkTable.COLUMN_REL));
			link.href = cursor.getString(cursor.getColumnIndex(LinkTable.COLUMN_HREF));
			link.name = cursor.getString(cursor.getColumnIndex(LinkTable.COLUMN_NAME));
			link.permalink = cursor.getString(cursor.getColumnIndex(LinkTable.COLUMN_PERMALINK));
			
			links.add(link);
    	}
		
		cursor.close();
		
		return links;
	}
	
	synchronized public long putAppUser(User appUser) {
		SQLiteDatabase db = this.getWritableDatabase();  
		
		ContentValues values = new ContentValues();
		values.put(AppUserTable.COLUMN_ID , appUser.id);
		
		long id = db.insertWithOnConflict(AppUserTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		
		db.close();
		
		return id;
	}
	
	synchronized public long putStatus(Status status, String appUserId) {
		long id = -1;
		
		Status storedStatus = getStatus(status.id);

		if(storedStatus == null) {
			SQLiteDatabase db = this.getWritableDatabase();  
			id = putStatus(db, status, appUserId);
			db.close();
			notifyListenters(true);
			
		} else {
			if (storedStatus.ignorableInSync == true && status.ignorableInSync == false) {
				id = updateStatusIgnorableInSync(status.id, false);
			}
			
			if (storedStatus.answers_count != status.answers_count) {
				id = updateStatusAnswersCount(status.id, status.answers_count);
			}
		}
		
		return id;
	}
	
	synchronized public List<Long> putAllStatuses(List<Status> statuses, String appUserId) {
		if(statuses == null || statuses.size() == 0) {
			return null;
		}
		
		SQLiteDatabase db = this.getWritableDatabase();  
		
		List<Long> ids = new ArrayList<Long>();
		
		for(Status status : statuses) {
			ids.add(putStatus(db, status, appUserId));
		}
		
		db.close();

		notifyListenters(true);
		
		return ids;
	}
	
	private Long putStatus(SQLiteDatabase db, Status status, String appUserId) {
		if(status == null) { 
			return 0L;
		}
		
		putUser(db, status.user);
		
		// putting Status datas
		ContentValues values = new ContentValues();
		values.put(StatusTable.COLUMN_TEXT, status.text);
		values.put(StatusTable.COLUMN_ID, status.id);
		values.put(StatusTable.COLUMN_APP_USER_ID, appUserId);
		values.put(StatusTable.COLUMN_USER_ID, status.user.id);
		values.put(StatusTable.COLUMN_TYPE, status.type);
		values.put(StatusTable.COLUMN_ANSWERS_COUNT, status.answers_count);
		values.put(StatusTable.COLUMN_LOGEABLE_TYPE, status.logeable_type);
		
		if(status.createdAtInMillis == 0) {
			try {
				status.createdAtInMillis = DateUtil.dfIn.parse(status.created_at).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		values.put(StatusTable.COLUMN_CREATED_AT_IN_MILLIS, status.createdAtInMillis);
		
		values.put(StatusTable.COLUMN_LECTURE_ALREADY_SEEN, (status.lectureAreadySeen ? 1 : 0));
		values.put(StatusTable.COLUMN_LAST_SEEN , (status.lastSeen ? 1 : 0));
		values.put(StatusTable.COLUMN_IGNORABLE_IN_SYNC , (status.ignorableInSync ? 1 : 0));
		
		long id = db.insertWithOnConflict(StatusTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		
		// putting links datas
		for(Link link : status.links) {
			putLink(db, link, status.id);
		}
		
		return id;
	}

	private long putLink(SQLiteDatabase db, Link link, String statusId) {
		ContentValues values = new ContentValues();
		values.put(LinkTable.COLUMN_STATUS_ID, statusId);
		values.put(LinkTable.COLUMN_REL, link.rel);
		values.put(LinkTable.COLUMN_HREF, link.href);
		values.put(LinkTable.COLUMN_NAME, link.name);
		values.put(LinkTable.COLUMN_PERMALINK, link.permalink);
		
		return db.insertWithOnConflict(LinkTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);	
	}

	private long putUser(SQLiteDatabase db, User user) {
		ContentValues values = new ContentValues();
		values.put(UserTable.COLUMN_ID, user.id);
		values.put(UserTable.COLUMN_FIRST_NAME, user.first_name);
		values.put(UserTable.COLUMN_LAST_NAME, user.last_name);
		
		long id = db.insertWithOnConflict(UserTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);	

		// putting thumbnails datas
		for(Thumbnail thumbnail : user.thumbnails) {
			putThumbnail(db, thumbnail, user.id);
		}
		
		return id;
	}
	
	private long putThumbnail(SQLiteDatabase db, Thumbnail thumbnail, int userId) {
		ContentValues values = new ContentValues();
		values.put(ThumbnailTable.COLUMN_USER_ID, userId);
		values.put(ThumbnailTable.COLUMN_HREF, thumbnail.href);
		values.put(ThumbnailTable.COLUMN_SIZE, thumbnail.size);
		
		return db.insertWithOnConflict(ThumbnailTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);	
	}
	
	synchronized public long setStatusAsLastSeen(Status status) {
		SQLiteDatabase db = this.getWritableDatabase();  

        ContentValues statusValues = new ContentValues();
        statusValues.put(StatusTable.COLUMN_LAST_SEEN, 1);
        statusValues.put(StatusTable.COLUMN_LAST_SEEN_AT_IN_MILLIS, System.currentTimeMillis());
        
        long id = db.update(StatusTable.NAME, statusValues, StatusTable.COLUMN_ID + " = ?", new String[] {status.id});
        
        db.close();

        notifyListenters(false);
        
        return id;
	}
	
	synchronized public long updateStatusAnswersCount(String statusId, int answerCount) {
		SQLiteDatabase db = this.getWritableDatabase();  
		
		ContentValues values = new ContentValues();
		values.put(StatusTable.COLUMN_ANSWERS_COUNT, answerCount);
		
		long id = db.update(StatusTable.NAME, values, AppUserTable.COLUMN_ID + " = ?", new String[] {statusId});
		
		db.close();
		
		return id;
	}
	
	synchronized public long updateStatusIgnorableInSync(String statusId, boolean ignorableInSync) {
		SQLiteDatabase db = this.getWritableDatabase();  
		
		ContentValues values = new ContentValues();
		values.put(StatusTable.COLUMN_IGNORABLE_IN_SYNC, (ignorableInSync ? 1 : 0));
		
		long id = db.update(StatusTable.NAME, values, AppUserTable.COLUMN_ID + " = ?", new String[] {statusId});
		
		db.close();
		
		return id;
	}
	
	synchronized public long setOldestStatusesWereDownloaded(String appUserId) {
		SQLiteDatabase db = this.getWritableDatabase();  
		
		ContentValues values = new ContentValues();
		values.put(AppUserTable.COLUMN_OLDEST_STATUSES_WERE_DOWNLOADED, 1);
		
		long id = db.update(AppUserTable.NAME, values, AppUserTable.COLUMN_ID + " = ?", new String[] {appUserId});
		
		db.close();
		
		return id;
	}
	
	synchronized public boolean getOldestStatusesWereDownloaded(String appUserId) {
		boolean oledestStatusesWereDownloaded = false;
		
		SQLiteDatabase db = this.getReadableDatabase();  
		
		Cursor cursor = db.query(AppUserTable.NAME, null, AppUserTable.COLUMN_ID + " = ?", new String[] {appUserId}, null, null, null);
        if (cursor.moveToFirst()) {  
        	int value = cursor.getInt(cursor.getColumnIndex(AppUserTable.COLUMN_OLDEST_STATUSES_WERE_DOWNLOADED));
        	oledestStatusesWereDownloaded = (value != 0);
        }
        
        cursor.close();  
        db.close();
        
        return oledestStatusesWereDownloaded;
	}

	private void notifyListenters(boolean inserting) {
		if(mListeners != null) {
			for(WeakReference<DbHelperListener> wrListener : mListeners) {
				DbHelperListener listener = wrListener.get();
				if(listener != null) {
					if(inserting) {
						listener.onStatusInserted();
					} else {
						listener.onStatusUpdated();
					}
				}
			}
		}
	}
	
	/**
	 * Get the millis of the most recent Status saved at the db
	 * @return the creation time, in millis, of the most recent Status saved at the db. 
	 */
	synchronized public long getTimestamp() {
		long timestamp = 0l;
		
		SQLiteDatabase db = this.getReadableDatabase();  
        Cursor cursor;  
        
        String query = new StringBuffer("SELECT MAX(").
        		append(StatusTable.COLUMN_CREATED_AT_IN_MILLIS).
        		append(") FROM ").
        			append("( SELECT * FROM ").append(StatusTable.NAME).
        			append(" WHERE ").append(StatusTable.COLUMN_IGNORABLE_IN_SYNC).append(" = 0 )").
        		toString();
        
        cursor = db.rawQuery(query, null);
  
        if (cursor.moveToFirst()) {  
    		timestamp = cursor.getLong(0);
        }
        
        cursor.close();  
        db.close();
        
        return timestamp;
	}
	
	@Override
	public synchronized void close() {
		super.close();
		instance = null;
	}
}
