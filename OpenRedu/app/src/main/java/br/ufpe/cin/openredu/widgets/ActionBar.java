package br.ufpe.cin.openredu.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.activities.BaseActivity;

public class ActionBar extends FrameLayout {

	private Activity mActivity;
	private final LinearLayout mContent;
	private Class<? extends Activity> mClassRef;
	private Bundle mExtras;

	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
		String title = styledAttrs.getString(R.styleable.ActionBar_title);
		boolean showCaret = styledAttrs.getBoolean(R.styleable.ActionBar_showCaret, true);
		styledAttrs.recycle();

		mContent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.action_bar, null);

		TextView tvTitle = (TextView) mContent.findViewById(R.id.tv_title_action_bar);
		tvTitle.setText(title);

		ImageButton ibUp = (ImageButton) mContent.findViewById(R.id.b_up);
		if(showCaret) {
			ibUp.setImageResource(R.drawable.ic_logo_redu_back);
			ibUp.setBackgroundResource(R.drawable.bg_action_button);
			ibUp.setEnabled(true);
		} else {
			ibUp.setImageResource(R.drawable.ic_logo_redu2);
			ibUp.setBackgroundResource(android.R.color.transparent);
			ibUp.setEnabled(false);
		}
		
		addView(mContent, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		if (!isInEditMode()) {
			if (context instanceof BaseActivity) {
				((BaseActivity) context).setActionBar(this);
			}
		}

		mContent.findViewById(R.id.b_up).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mActivity != null) {
							if (mClassRef == null) {
								mActivity.onBackPressed();
							} else {
								Intent intent = new Intent(mActivity, mClassRef);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								if(mExtras != null) {
									intent.putExtras(mExtras);
								}
								mActivity.startActivity(intent);
							}
						}
					}
				});
	}

	public void setActivity(Activity activity) {
		mActivity = activity;
	}

	public void setTitle(String text) {

		TextView tvTitle = (TextView) findViewById(R.id.tv_title_action_bar);
		tvTitle.setText(text);
	}

	public void setUpClass(Class<? extends Activity> classRef) {
		setUpClass(classRef, null);
	}
	
	public void setUpClass(Class<? extends Activity> classRef, Bundle extras) {
		mClassRef = classRef;
		mExtras = extras;
	}

	public View addAction(int drawableResId, OnClickListener clickAction) {

		ImageButton ib = new ImageButton(getContext());
		ib.setImageResource(drawableResId);
		ib.setBackgroundResource(R.drawable.bg_action_button);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT );

		ib.setOnClickListener(clickAction);

		mContent.addView(ib, params);
		
		return ib;
	}

}
