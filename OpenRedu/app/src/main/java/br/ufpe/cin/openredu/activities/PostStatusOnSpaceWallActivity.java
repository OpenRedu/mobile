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
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.widgets.StatusComposer;
import br.ufpe.cin.openredu.widgets.StatusComposer.OnStatusComposerListener;

public class PostStatusOnSpaceWallActivity extends BaseActivity {

	public static final String EXTRAS_SPACE = "EXTRAS_SPACE";
	
	private Space mSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_post_status);

		Bundle extras = getIntent().getExtras();
		mSpace = (Space) extras.get(EXTRAS_SPACE);

		setActionBarTitle(mSpace.name);

		User user = ReduApplication.getUser(this);
		
		((WebCachedImageView) findViewById(R.id.iv_photo)).setImageUrl(user.getThumbnailUrl());

		StringBuffer userActionBuffer = new StringBuffer().append("<b>").append(user.getCompleteName()).append("</b>").append(" comentou");
		((TextView) findViewById(R.id.tv_user_action)).setText(Html.fromHtml(userActionBuffer.toString()));

		findViewById(R.id.iv_help_icon).setVisibility(View.GONE);
	
		((StatusComposer) findViewById(R.id.status_composer)).setOnStatusComposerListener(new OnStatusComposerListener() {
			@Override
			public void onSendClicked(String text) {
				new PostCommentTask().execute(mSpace.id, text);
			}
		});
	}
	
	class PostCommentTask extends AsyncTask<String, Void, br.com.developer.redu.models.Status> {

		@Override
		protected void onPreExecute() {
			showProgressDialog("Enviando seu comentário…");
		}
		
		@Override
		protected br.com.developer.redu.models.Status doInBackground(String... params) {
			String spaceId = params[0];
			String text = params[1];
			
			br.com.developer.redu.models.Status status;
			try {
				DefaultReduClient redu = ReduApplication.getReduClient(PostStatusOnSpaceWallActivity.this);
				status = redu.postStatusSpace(spaceId, text);
			} catch(Exception e) {
				e.printStackTrace();
				status = null;
			}
			
			return status;
		}
		
		@Override
		protected void onPostExecute(br.com.developer.redu.models.Status result) {
			dismissProgressDialog();
			
			if(result == null) {
				Toast.makeText(PostStatusOnSpaceWallActivity.this, "Não foi possível enviar seu comentário.", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(PostStatusOnSpaceWallActivity.this, "Comentário enviado com sucesso.", Toast.LENGTH_SHORT).show();
				
				Intent data = new Intent();
				data.putExtra(SpaceActivity.EXTRA_STATUS_RESULT, result);
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
}
