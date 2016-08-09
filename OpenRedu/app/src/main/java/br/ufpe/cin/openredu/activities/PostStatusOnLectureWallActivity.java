package br.ufpe.cin.openredu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WebCachedImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Status;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.widgets.StatusComposer;
import br.ufpe.cin.openredu.widgets.StatusComposer.OnStatusComposerListener;

public class PostStatusOnLectureWallActivity extends BaseActivity {

	public static final String EXTRAS_LECTURE = "EXTRAS_LECTURE";
	public static final String EXTRAS_STATUS_IS_HELP_TYPE = "EXTRAS_STATUS_IS_HELP_TYPE";
	
	private Lecture mLecture;
	private boolean mStatusIsHelpType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_post_status);

		Bundle extras = getIntent().getExtras();
		mLecture = (Lecture) extras.get(EXTRAS_LECTURE);
		mStatusIsHelpType = extras.getBoolean(EXTRAS_STATUS_IS_HELP_TYPE);
		
		setActionBarTitle(mLecture.name);

		User user = ReduApplication.getUser(this);

		((WebCachedImageView) findViewById(R.id.iv_photo)).setImageUrl(user.getThumbnailUrl());

		StringBuffer userActionBuffer = new StringBuffer().append("<b>").append(user.getCompleteName()).append("</b>");
		View helpIcon = findViewById(R.id.iv_help_icon);
		
		if(mStatusIsHelpType) {
			userActionBuffer.append(" pediu ajuda");
			helpIcon.setVisibility(View.VISIBLE);
		} else {
			userActionBuffer.append(" comentou");
			helpIcon.setVisibility(View.GONE);
		}
		
		((TextView) findViewById(R.id.tv_user_action)).setText(Html.fromHtml(userActionBuffer.toString()));
	
		((StatusComposer) findViewById(R.id.status_composer)).setOnStatusComposerListener(new OnStatusComposerListener() {
			@Override
			public void onSendClicked(String text) {
				new PostCommentTask().execute(String.valueOf(mLecture.id), text);
			}
		});
	}
	
	class PostCommentTask extends AsyncTask<String, Void, br.com.developer.redu.models.Status> {

		@Override
		protected void onPreExecute() {
			String message;
			if(mStatusIsHelpType) {
				message = "Enviando seu pedido de ajuda…";
			} else {
				message = "Enviando seu comentário…";
			}
			showProgressDialog(message);
		}
		
		@Override
		protected br.com.developer.redu.models.Status doInBackground(String... params) {
			String lectureId = params[0];
			String text = params[1];
			
			br.com.developer.redu.models.Status status;
			try {
				DefaultReduClient redu = ReduApplication.getReduClient(PostStatusOnLectureWallActivity.this);
				status = redu.postStatusLecture(lectureId, text, getStatusType());
			} catch(Exception e) {
				status = null;
			}
			
			return status;
		}
		
		@Override
		protected void onPostExecute(br.com.developer.redu.models.Status result) {
			dismissProgressDialog();
			
			if(result == null) {
				Toast.makeText(PostStatusOnLectureWallActivity.this, "Não foi possível enviar seu comentário.", Toast.LENGTH_SHORT).show();
			} else {
				String message;
				if(mStatusIsHelpType) {
					message = "Pedido de Ajuda enviado com sucesso.";
				} else {
					message = "Comentário enviado com sucesso.";
				}
				Toast.makeText(PostStatusOnLectureWallActivity.this, message, Toast.LENGTH_SHORT).show();
				
				Intent data = new Intent();
				data.putExtra(LectureWallActivity.EXTRA_STATUS_RESULT, result);
				setResult(Activity.RESULT_OK, data);
				
				finish();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED);
		super.onBackPressed();
	}
	
	private String getStatusType() {
		return (mStatusIsHelpType) ? Status.TYPE_HELP : Status.TYPE_ACTIVITY;
	}
}
