package br.ufpe.cin.openredu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PinCodeHelper {

	private static final String REDU_PIN_CODE = "REDU_PIN_CODE";
	
	public static boolean has(Context context) {
		return getPrefs(context).contains(REDU_PIN_CODE);
	}
	
	public static String get(Context context) {
		return getPrefs(context).getString(REDU_PIN_CODE, null);
	}
	
	public static boolean set(Context context, String pinCode) {
		return getPrefs(context).edit().putString(REDU_PIN_CODE, pinCode).commit();
	}
	
	public static boolean clear(Context context) {
		return getPrefs(context).edit().clear().commit();
	}
	
	private static SharedPreferences getPrefs(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
