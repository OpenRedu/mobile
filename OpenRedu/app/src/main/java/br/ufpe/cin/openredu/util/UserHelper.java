package br.ufpe.cin.openredu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserHelper {
	
	
	/*public static boolean hasPinCode(Context context) {
		return getPrefs(context).contains(REDU_PIN_CODE);
	}*/
	
	public static String getUserId(Context context) {
		return getPrefs(context).getString("userId", null);
	}
	
	public static boolean setUserId(Context context, String id) {
		return getPrefs(context).edit().putString("userId", id).commit();
	}
	
	public static boolean setUserRoleInCourse(Context context, String role) {
		return getPrefs(context).edit().putString("role", role).commit();
	}
	
	public static String getUserRoleInCourse(Context context) {
		return getPrefs(context).getString("role", null);
	}
	
	private static SharedPreferences getPrefs(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
