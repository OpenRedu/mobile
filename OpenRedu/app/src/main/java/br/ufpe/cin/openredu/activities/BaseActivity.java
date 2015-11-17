package br.ufpe.cin.openredu.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.widgets.ActionBar;
import br.ufpe.cin.openredu.widgets.ReduProgressDialog;

public abstract class BaseActivity extends FragmentActivity {

	private ActionBar mActionBar;
	private ReduProgressDialog mDialog;

	protected void onCreate(Bundle savedInstanceState, int layoutResID) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(layoutResID);
		
		ActionBar ab = getViewById(R.id.actionBar);
		setActionBar(ab);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	public void setActionBar(ActionBar ab) {
		mActionBar = ab;
		mActionBar.setActivity(this);
	}

	protected void setUpClass(Class<? extends Activity> upClass) {
		mActionBar.setUpClass(upClass);
	}

	protected void setUpClass(Class<? extends Activity> upClass, Bundle extras) {
		mActionBar.setUpClass(upClass, extras);
	}
	
	protected View addActionToActionBar(int drawableResId,
			OnClickListener clickAction) {
		return mActionBar.addAction(drawableResId, clickAction);
	}

	protected void setActionBarTitle(String title) {
		mActionBar.setTitle(title);
	}
	
	@SuppressWarnings("unchecked")
	<T>T getViewById(int resId) {  
		return (T) findViewById(resId);
	} 
	
	public void showAlertDialog(final Activity source, String text,
			DialogInterface.OnClickListener listener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(source);
		builder.setMessage(text)
				.setCancelable(false)
				.setNeutralButton("Ok", listener)
				.create().show();
	}
	
	public ReduProgressDialog showProgressDialog(String text) {
		return showProgressDialog(text, true);
	}

	public ReduProgressDialog showProgressDialog(String text, boolean cancelable) {

		if (mDialog == null) {

			String title = getString(R.string.app_name);

			DialogInterface.OnCancelListener cancelListener = null;
			if (cancelable) {
				cancelListener = new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface d) {

						if (mDialog.isBackPressed()) {
							finish();
						}
					}
				};
			}

			mDialog = new ReduProgressDialog(this);
			mDialog.setTitle(title);
			mDialog.setMessage(text);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(cancelable);
			mDialog.setOnCancelListener(cancelListener);
			mDialog.show();

			// dialog = ProgressDialog.show(source, title, text, true,
			// cancelable, cancelListener);
		} else {

			mDialog.setMessage(text);

			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		}

		return mDialog;
	}

	public void dismissProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	
//	@Override
//	public void onBackPressed() {
//
//		finish(ActivityAnimation.SLIDE_RIGHT);
//	}
}
