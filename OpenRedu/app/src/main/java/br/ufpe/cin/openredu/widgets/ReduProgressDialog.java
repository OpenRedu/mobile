package br.ufpe.cin.openredu.widgets;

import android.app.ProgressDialog;
import android.content.Context;

public class ReduProgressDialog extends ProgressDialog {

	boolean mBackPressed;
	
	public ReduProgressDialog(Context context) {
		super(context);
	}
	public ReduProgressDialog(Context context, int theme) {
		super(context, theme);
	}
	
	@Override
	public void onBackPressed() {
		mBackPressed = true;
		super.onBackPressed();
	}
	
	public boolean isBackPressed() {
		boolean b = mBackPressed;
		mBackPressed = false;
		return b;
	}
	
}