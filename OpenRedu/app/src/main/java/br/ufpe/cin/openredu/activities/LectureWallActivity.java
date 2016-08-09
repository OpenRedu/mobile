package br.ufpe.cin.openredu.activities;

import org.scribe.exceptions.OAuthConnectionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.fragments.lectures.LectureWallFragment;

public class LectureWallActivity extends DbHelperHolderActivity {

	public static final String EXTRA_STATUS_RESULT = "RESULT_STATUS";

	public static final String EXTRAS_LECTURE = "EXTRAS_LECTURE";

	public static final String EXTRAS_SUBJECT_ID = "EXTRAS_SUBJECT_ID";
	public static final String EXTRAS_LECTURE_ID = "EXTRAS_LECTURE_ID";
	public static final String EXTRAS_SPACE_ID = "EXTRAS_SPACE_ID";
	public static final String EXTRAS_ENVIRONMENT_PATH = "EXTRAS_ENVIRONMENT_PATH";

	private Lecture mLecture;
	private LectureWallFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_lecture_wall);

		Bundle extras = getIntent().getExtras();
		mLecture = (Lecture) extras.getSerializable(EXTRAS_LECTURE);

		final String lectureId = extras.getString(EXTRAS_LECTURE_ID);
		final String subjectId = extras.getString(EXTRAS_SUBJECT_ID);
		final String spaceId = extras.getString(EXTRAS_SPACE_ID);
		final String environmentPath = extras
				.getString(EXTRAS_ENVIRONMENT_PATH);

		if (mLecture != null) {
			init();
		} else {
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					try {
						DefaultReduClient redu = ReduApplication.getReduClient(LectureWallActivity.this);
						mLecture = redu.getLecture(lectureId);
						return null;
					} catch (OAuthConnectionException e) {
						e.printStackTrace();
						return null;
					}
				}

				protected void onPostExecute(Void param) {
					if (mLecture != null) {
						Bundle extrasToUp = new Bundle();
						extrasToUp.putSerializable(
								LectureActivity.EXTRAS_SUBJECT_ID, subjectId);
						extrasToUp.putSerializable(
								LectureActivity.EXTRAS_LECTURE_ID, lectureId);
						extrasToUp.putSerializable(
								LectureActivity.EXTRAS_SPACE_ID, spaceId);
						extrasToUp.putSerializable(
								LectureActivity.EXTRAS_ENVIRONMENT_PATH,
								environmentPath);
						setUpClass(LectureActivity.class, extrasToUp);

						init();
					}
				};
			}.execute();
		}

	}

	private void init() {
		setActionBarTitle(mLecture.name);

		Bundle args = new Bundle();
		args.putSerializable(LectureWallFragment.EXTRAS_LECTURE, mLecture);

		mFragment = new LectureWallFragment();
		mFragment.setArguments(args);

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Status status = (Status) data.getExtras().getSerializable(EXTRA_STATUS_RESULT);
			mFragment.addPostedStatus(status);
		}
	}
}
