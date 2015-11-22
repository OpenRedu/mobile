package br.ufpe.cin.openredu.activities;

import org.scribe.exceptions.OAuthConnectionException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Course;
import br.com.developer.redu.models.Enrollment;
import br.com.developer.redu.models.Environment;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.fragments.CoursesAndSpacesFragment;
import br.ufpe.cin.openredu.fragments.CoursesAndSpacesFragment.OnSpaceSelectedListener;
import br.ufpe.cin.openredu.util.UserHelper;

public class EnvironmentActivity extends BaseActivity implements OnSpaceSelectedListener {
	
	public static final String EXTRA_ENVIRONMENT = "EXTRA_ENVIRONMENT";
	
	private Environment mEnvironment;
	
	private Space mSpace;
	private Course mCourse;
	private Enrollment mEnrollment;
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_environment);
		
		Bundle args = new Bundle();
		args.putInt(HomeActivity.ITEM_EXTRA_PARAM, HomeActivity.ITEM_ENVIRONMENTS);
		
		setUpClass(HomeActivity.class, args);
		
		Bundle extras = getIntent().getExtras();
		mEnvironment = (Environment) extras.getSerializable(EXTRA_ENVIRONMENT);
	
		args = new Bundle();
		args.putSerializable(CoursesAndSpacesFragment.EXTRAS_ENVIRONMENT, mEnvironment);

		Fragment coursesAndServicesFragment = new CoursesAndSpacesFragment();
		coursesAndServicesFragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragment_container, coursesAndServicesFragment, CoursesAndSpacesFragment.class.getName());
		transaction.commit();		
	}

	@Override
	public void onSpaceSelected(Space space, Course course) {

		mSpace = space;
		mCourse = course;
		
		new AsyncTask<Void, Void, Void>() {

			private ProgressDialog mProgressDialog;

			protected void onPreExecute() {
				mProgressDialog = showProgressDialog("Aguarde…", true);
			};
			
			@Override
			protected Void doInBackground(Void... params) {
				try {
					DefaultReduClient redu = ReduApplication.getReduClient(mContext);
					User u = ReduApplication.getUser(mContext);
					mEnrollment = redu.getEnrollmentUserAtCourse(Integer.toString(u.id), mCourse.id);
					UserHelper.setUserRoleInCourse(mContext, mEnrollment.role);
				} catch (OAuthConnectionException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				mProgressDialog.dismiss();
				//TODO VERIFICAR 
				Intent it = new Intent(EnvironmentActivity.this, SpaceActivity.class);
				it.putExtra(SpaceActivity.EXTRAS_SPACE, mSpace);
				it.putExtra(Course.class.getName(), mCourse);
				it.putExtra(Enrollment.class.getName(), mEnrollment);
				startActivity(it);
			}
			
		}.execute();
	}
}
