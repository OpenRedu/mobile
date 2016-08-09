package br.ufpe.cin.openredu.activities.lecture;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Subject;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.activities.BaseActivity;
import br.ufpe.cin.openredu.activities.SpaceActivity;

public class UploadStep3Activity extends BaseActivity {

	private String superId;
	private Space space;
	private Bitmap bitmap;
	private BitmapDrawable drawable;

	private Context mContext = this;

	private String type;
	private SaveFile sl;

	private EditText etTitleLecture;
	// private static final int NUM_MAX_CHARACERS = 250;
	private File mFile;
	private Subject mSubject;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_inserted_file_or_lecture);
		superId = getIntent().getExtras().getString("id");
		type = getIntent().getExtras().getString("type");
		space = (Space) getIntent().getExtras().get(Space.class.getName());
		mSubject = (Subject) getIntent().getExtras().get(Subject.class.getName());

		if (type.equals("foto")) {
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inSampleSize = 4;
			bitmap = BitmapFactory.decodeFile(getIntent().getExtras().getString("foto"), bmpFactoryOptions);
			drawable = new BitmapDrawable(bitmap);
			mFile = new File(getIntent().getExtras().getString("foto"));
		} else if (type.equals("video")) {
			mFile = new File(getIntent().getExtras().getString("video"));
		}
		superId = getIntent().getExtras().getString("id");
		space = (Space) getIntent().getExtras().get(Space.class.getName());

		ImageView ivPreview = (ImageView) findViewById(R.id.camera_preview);
		TextView tvPreviewName = (TextView) findViewById(R.id.tvImageName);
		TextView tvWhereLecture = (TextView) findViewById(R.id.tvWhereLecture);
		etTitleLecture = (EditText) findViewById(R.id.etTitleLecture);

		tvPreviewName.setText(mFile.getName() + " (" + type + ")");

		Button btAdicionarPreview = (Button) findViewById(R.id.btAdicionarPreview);

		btAdicionarPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSubject == null) {
					String[] split = mFile.getName().split("\\.");
					String extension = split[split.length - 1];
					String text = etTitleLecture.getText().toString();
					Log.i("TEXT", text);
					File newFile = new File(mFile.getParent() + "/" + text + "." + extension);
					Log.i("NEWFILE", newFile.getAbsolutePath());
					Log.i("ANTES", mFile.getAbsolutePath());
					if (!mFile.renameTo(newFile)) {
						Toast toast = Toast.makeText(mContext, "Nome de arquivo inválido.", Toast.LENGTH_LONG);
						toast.show();
					} else {
						mFile = newFile;
						Log.i("DEPOIS", mFile.getAbsolutePath());
						Object[] params = { null, mFile };
						sl = new SaveFile();
						sl.execute(params);
					}
				} else {
					String text = etTitleLecture.getText().toString();
					Lecture l = new Lecture();
					l.name = text;
					if (type.equals("foto")) {
						l.type = Lecture.TYPE_DOCUMENT;
					}
					if (type.equals("video")) {
						l.type = Lecture.TYPE_MEDIA;
					}
					Object[] params = { l, mFile };
					sl = new SaveFile();
					sl.execute(params);
				}
			}
		});

		Button btCancelarPreview = (Button) findViewById(R.id.btCancelarPreview);
		if (mSubject == null) {
			tvWhereLecture.setText("...>" + space.name);
		} else {
			tvWhereLecture.setText(Html.fromHtml("... > " + space.name + " > " + "<b>" + mSubject.name + "</b>"));
		}

		btCancelarPreview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (type.equals("foto"))
			ivPreview.setBackgroundDrawable(drawable);
		if (type.equals("video"))
			ivPreview.setBackgroundResource(R.drawable.ic_midia_big);
		if (type.equals("audio"))
			ivPreview.setImageResource(R.drawable.ic_audio_big);
	}

	class SaveFile extends AsyncTask<Object, Void, Lecture> {

		private boolean mError;

		@Override
		protected void onPreExecute() {
			showProgressDialog("Adicionando Aula...", false);
			super.onPreExecute();

		}

		protected Lecture doInBackground(Object... obj) {
			DefaultReduClient redu = ReduApplication.getReduClient(mContext);
			Lecture lecture = null;
			try {
				if (mSubject == null) {
					redu.postFile(superId, (File) obj[1]);
				} else {
					lecture = (Lecture) obj[0];
					redu.postLecture(lecture, mSubject.id, (File) obj[1]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				lecture = null;
			}
			return lecture;
		}

		@Override
		protected void onPostExecute(Lecture lecture) {
			dismissProgressDialog();
			if (mError) {
				showAlertDialog(UploadStep3Activity.this, "Houve um problema ao adicionar a aula. Verifique sua conexão com a internet e tente novamente.", null);
				setResult(Activity.RESULT_CANCELED);
			} else {
				if(lecture != null) {
					Intent data = new Intent();
					data.putExtra(SpaceActivity.EXTRA_SUBJECT_RESULT, mSubject);
					data.putExtra(SpaceActivity.EXTRA_LECTURE_RESULT, lecture);
					setResult(Activity.RESULT_OK, data);
				}
				finish();
			}
		};
	}
}
