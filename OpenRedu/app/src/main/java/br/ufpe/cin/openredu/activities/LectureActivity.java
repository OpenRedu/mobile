package br.ufpe.cin.openredu.activities;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.scribe.exceptions.OAuthConnectionException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Progress;
import br.com.developer.redu.models.Subject;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.util.DownloadHelper;
import br.ufpe.cin.openredu.util.UserHelper;

public class LectureActivity extends BaseActivity {

	public static final String EXTRAS_SUBJECT = "EXTRAS_SUBJECT";
	public static final String EXTRAS_LECTURE = "EXTRAS_LECTURE";

	public static final String EXTRAS_SUBJECT_ID = "EXTRAS_SUBJECT_ID";
	public static final String EXTRAS_LECTURE_ID = "EXTRAS_LECTURE_ID";
	public static final String EXTRAS_SPACE_ID = "EXTRAS_SPACE_ID";
	public static final String EXTRAS_ENVIRONMENT_PATH = "EXTRAS_ENVIRONMENT_PATH";

	private TextView mTvSubject;
	private TextView mTvLecture;
	private Button mBtRemove;
	
	private Context mContext = this;

	private Button mBtIsDone;
	private Button mBtWall;

	private Lecture mLecture;
	private Subject mSubject;

	private ProgressDialog mProgressDialog;

	private AlertDialog alertDialog;

	DownloadFile df;
	
	private Progress mProgress;
	AlertDialog dialogRemove;
	private ProgressBar pbDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_lecture);

		mTvSubject = (TextView) findViewById(R.id.tv_title_action_bar);
		mTvLecture = (TextView) findViewById(R.id.tvLecture);
		String role = UserHelper.getUserRoleInCourse(this);
		if (role != null && (role.equals("teacher") || role.equals("environment_admin"))) {
			mBtRemove = (Button) findViewById(R.id.btRemove);
			mBtRemove.setVisibility(View.VISIBLE);

			mBtRemove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Builder builder = new Builder(LectureActivity.this);
					builder.setMessage("Você tem certeza que deseja remover esta Aula?");
		        	builder.setCancelable(true);
		        	builder.setNegativeButton("Não", new CancelOnClickListener());
		        	builder.setPositiveButton("Sim", new PositiveOnClickListener());
		        	dialogRemove = builder.create();
		        	dialogRemove.show();
				}
			});
		}
		
		mBtIsDone = (Button) findViewById(R.id.btIsDone);
		mBtWall = (Button) findViewById(R.id.btWall);
		
		pbDone =  (ProgressBar) findViewById(R.id.pbDone);

		Bundle extras = getIntent().getExtras();
		mLecture = (Lecture) extras.get(EXTRAS_LECTURE);
		mSubject = (Subject) extras.get(EXTRAS_SUBJECT);

		final String lectureId = extras.getString(EXTRAS_LECTURE_ID);
		final String subjectId = extras.getString(EXTRAS_SUBJECT_ID);
		final String spaceId = extras.getString(EXTRAS_SPACE_ID);
		final String environmentPath = extras
				.getString(EXTRAS_ENVIRONMENT_PATH);

		if (mLecture != null && mSubject != null) {
			init();
		} else {
			new AsyncTask<Void, Void, Boolean>() {
				protected void onPreExecute() {
					showProgressDialog("Carregando Aula…");
				};
				
				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						DefaultReduClient redu = ReduApplication
								.getReduClient(LectureActivity.this);
						mSubject = redu.getSubject(subjectId);
						mLecture = redu.getLecture(lectureId);
					} catch (OAuthConnectionException e) {
						e.printStackTrace();
						return true;
					}
					return false;
				}

				protected void onPostExecute(Boolean hasError) {
					dismissProgressDialog();
					if(hasError || mSubject == null || mLecture == null) {
						showAlertDialog(LectureActivity.this, "Não foi possível carregar essa Aula.", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						});
					} else {
						Bundle extrasToUp = new Bundle();
						extrasToUp.putSerializable(SpaceActivity.EXTRAS_SPACE_ID,
								spaceId);
						extrasToUp.putSerializable(
								SpaceActivity.EXTRAS_ENVIRONMENT_PATH,
								environmentPath);
						setUpClass(SpaceActivity.class, extrasToUp);

						init();
					}
				};
			}.execute();
		}
	}
	
	private final class CancelOnClickListener implements DialogInterface.OnClickListener {
	    public void onClick(DialogInterface dialog, int which) {
	    	dialog.dismiss();
	    }
    }
	
	private final class PositiveOnClickListener implements DialogInterface.OnClickListener {
    	public void onClick(DialogInterface dialog, int which) {
    		new RemoveLecture().execute();
    	}
    }

	private void initDialogs() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Aguarde…");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCanceledOnTouchOutside(false);

		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						df.cancel(true);
						df.running = false;
						dialog.dismiss();
					}
				});

		Builder alertDialogBuilder = new Builder(this);
		alertDialogBuilder.setTitle("Visualização no Navegador");
		alertDialogBuilder
				.setMessage(
						"A visualização desta Aula será feita através do navegador web do seu dispositivo")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(mLecture.getSelfLink()));
						startActivity(i);
					}
				})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		alertDialog = alertDialogBuilder.create();
	}

	private void init() {
		initDialogs();
		
		//findViewById(R.id.llLecture).setVisibility(View.VISIBLE);
		//findViewById(R.id.pbLecture).setVisibility(View.GONE);

		LinearLayout layoutLecture;
		if (mLecture.type.equals(Lecture.TYPE_CANVAS)) {
			layoutLecture = (LinearLayout) findViewById(R.id.llCanvas);
			Button ibCanvas = (Button) layoutLecture
					.findViewById(R.id.ibCanvas);
			ibCanvas.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.show();
				}
			});
			layoutLecture.setVisibility(View.VISIBLE);
		} else if (mLecture.type.equals(Lecture.TYPE_EXERCISE)) {
			layoutLecture = (LinearLayout) findViewById(R.id.llExercice);
			Button ibAccess = (Button) layoutLecture
					.findViewById(R.id.btExercice);
			ibAccess.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertDialog.show();
				}
			});
			layoutLecture.setVisibility(View.VISIBLE);
		} else if (mLecture.type.equals(Lecture.TYPE_DOCUMENT)) {
			layoutLecture = (LinearLayout) findViewById(R.id.llDocument);
			ImageView ivDocument = (ImageView) layoutLecture
					.findViewById(R.id.ivDocument);
			ivDocument.setImageResource(R.drawable.ic_doc_big);
			TextView tvDocument = (TextView) layoutLecture
					.findViewById(R.id.tvDocument);
			tvDocument.setText(mLecture.name);
			Button ibDocument = (Button) layoutLecture
					.findViewById(R.id.btAcessarDocument);
			ibDocument.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Lecture[] lecture = { mLecture };

					java.io.File f = new java.io.File(DownloadHelper
							.getLecturePath(), mLecture.getFileName());
					if (f.exists()) {
						Intent it;
						try {
							it = DownloadHelper.loadDocInReader(f);
							startActivity(it);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						df = new DownloadFile();
						df.execute(lecture);
					}
				}
			});
			layoutLecture.setVisibility(View.VISIBLE);
		} else if (mLecture.type.equals(Lecture.TYPE_MEDIA)) {
			layoutLecture = (LinearLayout) findViewById(R.id.llMedia);
			ImageView ivMedia = (ImageView) layoutLecture
					.findViewById(R.id.ivMedia);
			ivMedia.setImageResource(R.drawable.ic_midia_big);
			TextView tvMedia = (TextView) layoutLecture
					.findViewById(R.id.tvMedia);
			tvMedia.setText(mLecture.name);
			Button ibMedia = (Button) layoutLecture
					.findViewById(R.id.ibAcessarMedia);
			if (mLecture.mimetype.equals("video/x-youtube")) {
				ibMedia.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(mLecture.getFilePath())));
					}
				});
			}else{
				ibMedia.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Lecture[] lecture = { mLecture };

						java.io.File f = new java.io.File(DownloadHelper
								.getLecturePath(), mLecture.getFileName());
						if (f.exists()) {
							Intent it;
							try {
								it = DownloadHelper.loadDocInReader(f);
								startActivity(it);
							} catch (ActivityNotFoundException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							df = new DownloadFile();
							df.execute(lecture);
						}
					}
				});
			}
			
			layoutLecture.setVisibility(View.VISIBLE);
		} else if (mLecture.type.equals(Lecture.TYPE_PAGE)) {
			layoutLecture = (LinearLayout) findViewById(R.id.llPage);
			TextView tvPage = (TextView) layoutLecture
					.findViewById(R.id.tvPage);
			tvPage.setText(Html.fromHtml(mLecture.content));
			layoutLecture.setVisibility(View.VISIBLE);
		}

		mTvLecture.setText(mLecture.name);
		mTvSubject.setText(mSubject.name);

		Log.i("Aula", Integer.toString(mLecture.id));

		mBtIsDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new PutProgress().execute();
			}
		});

		mBtWall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LectureActivity.this,
						LectureWallActivity.class);
				i.putExtra(LectureWallActivity.EXTRAS_LECTURE, mLecture);
				startActivity(i);
			}
		});
		
		new LoadProgress().execute();

	}

	private class DownloadFile extends
			AsyncTask<Lecture, Integer, java.io.File> {
		private volatile boolean running = true;

		@Override
		protected java.io.File doInBackground(Lecture... lecture) {
			try {

				String path = lecture[0].getFilePath();
				URL url = new URL(path);

				String[] temp = path.split("\\?")[0].split("/");
				String fileName = temp[temp.length - 1];

				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();

				String newFolder = DownloadHelper.getLecturePath();
				java.io.File myNewFolder = new java.io.File(newFolder);
				if (!myNewFolder.exists())
					myNewFolder.mkdirs();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				// java.io.File sdCard =
				// Environment.getExternalStorageDirectory();
				/*
				 * java.io.File dir = new File (sdCard.getAbsolutePath() +
				 * "/dir1/dir2"); dir.mkdirs(); File file = new File(dir,
				 * "filename");
				 */
				java.io.File filling = new java.io.File(newFolder, fileName);
				OutputStream output = new FileOutputStream(filling);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					if (!running) {
						output.flush();
						output.close();
						input.close();
						filling.delete();
						return null;
					}
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
				return filling;
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			running = false;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(java.io.File file) {
			super.onPostExecute(file);
			mProgressDialog.setProgress(0);
			mProgressDialog.dismiss();
			if (this != null && file != null) {
				try {
					Intent it = DownloadHelper.loadDocInReader(file, mLecture.mimetype);
					startActivity(it);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}
	
class LoadProgress extends AsyncTask<String, Void, Progress> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected Progress doInBackground(String... text) {
			DefaultReduClient redu = ReduApplication.getReduClient(mContext);
			User user = ReduApplication.getUser(mContext);
			List<Progress> progress = redu.getProgressByLecture(Integer.toString(mLecture.id), Integer.toString(user.id));
			if (progress.size() > 0)
				mProgress = progress.get(0);
			return mProgress;
		}

		@Override
		protected void onPostExecute(Progress progress) {
			super.onPostExecute(progress);
			if (mProgress != null){
				if (mProgress.finalized.equals("false"))
					mBtIsDone.setBackgroundResource(R.drawable.bt_bottom_green);
				if (mProgress.finalized.equals("true")){ 
					mBtIsDone.setBackgroundResource(R.drawable.bt_bottom_green_active);
					mBtIsDone.setText("Aula Finalizada");
				}
				pbDone.setVisibility(View.GONE);
				mBtIsDone.setVisibility(View.VISIBLE);
			}
		};
	}

class PutProgress extends AsyncTask<String, Void, Void> {
	
	@Override
	protected void onPreExecute() {
		mBtIsDone.setVisibility(View.GONE);
		pbDone.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	protected Void doInBackground(String... text) {
		DefaultReduClient redu = ReduApplication.getReduClient(mContext);
		redu.putProgress(mProgress);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		new LoadProgress().execute();
	};
}

	class RemoveLecture extends AsyncTask<String, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog("Removendo Aula...", true);
		}
		
		protected Void doInBackground(String... text) {
			DefaultReduClient redu = ReduApplication.getReduClient(mContext);
			redu.removeLecture(Integer.toString(mLecture.id));
			return null;
		}
	
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			finish();
		};
	}

}
