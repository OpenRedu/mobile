package br.ufpe.cin.openredu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.ufpe.cin.openredu.R;

public class SettingsHelper {

	public static final int KEY_ACTIVATED_NOTIFICATIONS = R.string.activate_notifications_key;
	public static final int KEY_WHEN_ANSWER_ME = R.string.when_answer_me_key;
	public static final int KEY_NEW_LECTURES = R.string.new_lectures_key;
	public static final int KEY_NEW_SUBJECTS = R.string.new_subject_key;
	public static final int KEY_NEW_COURSES = R.string.new_courses_key;

	static public boolean get(Context ctx, int key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		return prefs.getBoolean(ctx.getString(key), true);
	}
}
