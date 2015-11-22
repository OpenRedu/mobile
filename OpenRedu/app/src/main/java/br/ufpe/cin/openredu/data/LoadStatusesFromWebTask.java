package br.ufpe.cin.openredu.data;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.activities.StatusDetailActivity;
import br.ufpe.cin.openredu.db.DbHelper;
import br.ufpe.cin.openredu.util.DateUtil;
import br.ufpe.cin.openredu.util.SettingsHelper;

import com.buzzbox.mob.android.scheduler.NotificationMessage;
import com.buzzbox.mob.android.scheduler.SchedulerManager;
import com.buzzbox.mob.android.scheduler.Task;
import com.buzzbox.mob.android.scheduler.TaskResult;

public class LoadStatusesFromWebTask implements Task {

	private static final int DELAY_TO_CHECK_NOTIFICATIONS_IN_MINUTES = 30;

	@Override
	public String getTitle() {
		return "Redu Mobile";
	}

	@Override
	public String getId() {
		return "redumobile"; // give it an ID
	}

	public static void run(Context context) {
		LoadStatusesFromWebManager.run(context);
	}
	
	@Override
	public TaskResult doWork(ContextWrapper ctx) {

		TaskResult taskResult = new TaskResult();
		
		List<Status> notifiableStatues = loadStatuses(ctx);

		for (Status status : notifiableStatues) {
			NotificationMessage notification = new NotificationMessage(getTitle(), status.text);
			notification.setNotificationId(Integer.valueOf(status.id));
			notification.setNotificationIconResource(R.drawable.ic_status_notification);
			
			Bundle extras = new Bundle();

			if(status.isActivityType() || status.isHelpType()) {
				notification.setNotificationClickIntentClass(StatusDetailActivity.class);
				extras.putSerializable(StatusDetailActivity.EXTRAS_STATUS, status);
				extras.putSerializable(StatusDetailActivity.EXTRAS_ENABLE_GO_TO_WALL_ACTION, false);
				extras.putSerializable(StatusDetailActivity.EXTRAS_IS_FROM_NOTIFICATION, true);
				notification.setNotificationClickIntentBundle(extras);
				notification.setFlagResource(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				
			} else if(status.isLogType()) {
				// TODO abrir notificacao de novas aulas 
			}
			
			taskResult.addMessage(notification);
		}
		
		return taskResult;
	}

	private List<Status> loadStatuses(Context context) {
		boolean firstRunning = false;
		
		LoadStatusesFromWebManager.notifyOnStart();

		List<Status> notifiableStatuses = new ArrayList<Status>();

		DbHelper dbHelper = DbHelper.getInstance(context);

		try {
			DefaultReduClient redu = ReduApplication.getReduClient(context);
			String userId = String.valueOf(ReduApplication.getUser(context).id);
	
			long dbTimestamp;
			if(dbHelper.getOldestStatusesWereDownloaded(userId)) {
				dbTimestamp = dbHelper.getTimestamp();
			} else {
				dbTimestamp = 0;
			}
			firstRunning = (dbTimestamp == 0) ? true : false;
	
			boolean loadNextPage = true;
	
			for (int page = 1; loadNextPage; page++) {
				String pageStr = String.valueOf(page);
				
				List<Status> statuses = new ArrayList<Status>();
				List<Status> temp;
				temp = redu.getStatusesTimelineByUser(userId, Status.TYPE_ACTIVITY, pageStr);
				if(temp != null) {
					statuses.addAll(temp);
				}
				
				temp = redu.getStatusesTimelineByUser(userId, Status.TYPE_HELP, pageStr);
				if(temp != null) {
					statuses.addAll(temp);
				}
				
				temp = redu.getStatusesTimelineLogByUser(userId, Status.LOGEABLE_TYPE_COURSE, pageStr);
				if(temp != null) {
					statuses.addAll(temp);
				}
				
				temp = redu.getStatusesTimelineLogByUser(userId, Status.LOGEABLE_TYPE_SUBJECT, pageStr);
				if(temp != null) {
					statuses.addAll(temp);
				}
				
				temp = redu.getStatusesTimelineLogByUser(userId, Status.LOGEABLE_TYPE_LECTURE, pageStr);
				if(temp != null) {
					statuses.addAll(temp);
				}
	
				if (statuses.size() == 0) {
					dbHelper.setOldestStatusesWereDownloaded(userId);
					loadNextPage = false;
				} else {
					for (Status status : statuses) {
						try {
							status.createdAtInMillis = DateUtil.dfIn.parse(status.created_at).getTime();
						} catch (ParseException e) {
							e.printStackTrace();
							status.createdAtInMillis = 0;
						}
	
						if (status.createdAtInMillis <= dbTimestamp) {
							loadNextPage = false;
						} else {
							if(checkNotifiable(context, status) && firstRunning == false) {
								notifiableStatuses.add(status);
							}
						}
					}
					
					dbHelper.putAllStatuses(statuses, userId);
				}
			}

			LoadStatusesFromWebManager.notifyOnComplete();

		} catch(Exception e) {
			e.printStackTrace();
			LoadStatusesFromWebManager.notifyOnError(e);
		}

		return notifiableStatuses;
	}

	private boolean checkNotifiable(Context ctx, Status status) {
		if(SettingsHelper.get(ctx, SettingsHelper.KEY_ACTIVATED_NOTIFICATIONS)) {
			if (status.isLogType()) {
				if(status.isLectureLogeableType() 
						&& SettingsHelper.get(ctx, SettingsHelper.KEY_NEW_LECTURES)) {
					return true;
					
				} else if (status.isCourseLogeableType() 
						&& SettingsHelper.get(ctx, SettingsHelper.KEY_NEW_COURSES)) {
					return true;
					
				} else if (status.isSubjectLogeableType()
						&& SettingsHelper.get(ctx, SettingsHelper.KEY_NEW_SUBJECTS)) {
					return true;
				}
				
			} else if (status.isActivityType() 
					&& SettingsHelper.get(ctx, SettingsHelper.KEY_WHEN_ANSWER_ME)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void addOnLoadStatusesFromWebListener(OnLoadStatusesFromWebListener listener) {
		LoadStatusesFromWebManager.add(listener);
	}
	
	public static boolean isWorking() {
		return LoadStatusesFromWebManager.isWorking();
	}
	
	private static class LoadStatusesFromWebManager {

		private static boolean mIsWorking; 
		private static final List<WeakReference<OnLoadStatusesFromWebListener>> mListeners = new ArrayList<WeakReference<OnLoadStatusesFromWebListener>>();

		public static void run(Context context) {
			if(!isWorking()) {		
				SchedulerManager.getInstance().saveTask(context, "*/" + DELAY_TO_CHECK_NOTIFICATIONS_IN_MINUTES + " * * * *", LoadStatusesFromWebTask.class);
				SchedulerManager.getInstance().runNow(context, LoadStatusesFromWebTask.class, 0);
//				SchedulerManager.getInstance().restart(context, LoadStatusesFromWebTask.class);
			}
		}
		
		public static void add(OnLoadStatusesFromWebListener listener) {
			mListeners.add(new WeakReference<OnLoadStatusesFromWebListener>(listener));
		}

		public static void notifyOnStart() {
			mIsWorking = true;
			for (WeakReference<OnLoadStatusesFromWebListener> reference : mListeners) {
				if(!reference.isEnqueued() && reference.get() != null) {
					reference.get().onStart();
				}
			}
		}

		public static void notifyOnComplete() {
			mIsWorking = false;
			for (WeakReference<OnLoadStatusesFromWebListener> reference : mListeners) {
				if(!reference.isEnqueued() && reference.get() != null) {
					reference.get().onComplete();
				}
			}
		}

		public static void notifyOnError(Exception e) {
			mIsWorking = false;
			for (WeakReference<OnLoadStatusesFromWebListener> reference : mListeners) {
				if(!reference.isEnqueued() && reference.get() != null) {
					reference.get().onError(e);
				}
			}
		}
		
		public static boolean isWorking() {
			return mIsWorking;
		}
	}
}