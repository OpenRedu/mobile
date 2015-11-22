package br.ufpe.cin.openredu.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import br.ufpe.cin.openredu.R;

public class StatusComposer extends FrameLayout {
	
	public interface OnStatusComposerListener {
		public void onSendClicked(String text);
	}

	public static final int NUM_MAX_CHARACTERS = 800;

	private Context mContext;
	private EditText mEditText;
	private OnStatusComposerListener mListener;
	
	public StatusComposer(Context context) {
		super(context);
		init(context);
	}

	public StatusComposer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		mContext = context;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View content = inflater.inflate(R.layout.status_compose, null);
		
		final TextView tvTextCount = (TextView) content.findViewById(R.id.tv_text_count);
		mEditText = (EditText) content.findViewById(R.id.et_text);
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int numRemainingChars = NUM_MAX_CHARACTERS - s.length();
				tvTextCount.setText(String.valueOf(numRemainingChars));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		content.findViewById(R.id.iv_send).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null) {
					String text = mEditText.getText().toString().trim();
					if(text.length() <= StatusComposer.NUM_MAX_CHARACTERS) {
						mListener.onSendClicked(text);
						hideSoftKeyboard();
					}
				}
			}
		});
		
		addView(content, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void setOnStatusComposerListener(OnStatusComposerListener listener) {
		mListener = listener;
	}

	private void hideSoftKeyboard() {
		((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))  
        .hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
}
