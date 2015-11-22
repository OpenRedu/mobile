package br.ufpe.cin.openredu.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WebCachedImageView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Course;
import br.com.developer.redu.models.Environment;
import br.com.developer.redu.models.Space;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.adapters.CoursesExpandableListAdapter;

public class CoursesAndSpacesFragment extends NoConnectNotifiableFragment {
	
	public static final String EXTRAS_ENVIRONMENT = "EXTRAS_ENVIRONMENT";
	
	private List<Course> mEnrollmentedCourses;
	private List<List<Space>> mSpaces;

	private Environment mEnvironment;
	private Space mSpace;

	private TextView mTvEmptyList;
	
	private TextView mTvEnvironment;
	private WebCachedImageView mIvThumbnail;
	private ProgressBar mProgressBar;
	private ExpandableListView mListView;
	private CoursesExpandableListAdapter mAdapter;
	
	private OnSpaceSelectedListener mListener;

	private Course mCourse;

	public interface OnSpaceSelectedListener {
        public void onSpaceSelected(Space space, Course course);
    }
	
	public CoursesAndSpacesFragment() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mEnvironment = (Environment) getArguments().getSerializable(EXTRAS_ENVIRONMENT);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View v = inflater.inflate(R.layout.fragment_courses, container, false);

		mProgressBar = (ProgressBar) v.findViewById(R.id.pb);
		mTvEmptyList = (TextView) v.findViewById(R.id.tv_empty_list);
		mTvEnvironment = (TextView) v.findViewById(R.id.tvEnvironment);
		mIvThumbnail = (WebCachedImageView) v.findViewById(R.id.iv_thumbnail);
		
		mListView = (ExpandableListView) v.findViewById(R.id.list);
		mListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
					mSpace = (Space) mAdapter.getChild(groupPosition, childPosition);
					mCourse = (Course) mAdapter.getGroup(groupPosition);
					Log.i("SPACE2", mSpace.name);
					Log.i("COURSE2", mCourse.name);
					mListener.onSpaceSelected(mSpace, mCourse);
				return false;
			}
		});		
		
		new LoadSpacesTask().execute();
		
		return v;
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSpaceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCourseSelectedListener");
        }
    }

	@Override
	public void onNoConnectionAlertClicked() {
		new LoadSpacesTask().execute();
	}
    
	class LoadSpacesTask extends AsyncTask<Void, Void, Void> {
		
		private boolean mError;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
		protected Void doInBackground(Void... params) {
			mEnrollmentedCourses = new ArrayList<Course>();
			mSpaces = new ArrayList<List<Space>>();
			
			try {
				DefaultReduClient redu = ReduApplication.getReduClient(getActivity());
				List<Course> courses = redu.getCoursesByEnvironment(mEnvironment.path);
				if(courses != null) {
					for(Course course : courses) {
						List<Space> spacesByCourse = redu.getSpacesByCourse(course.id);
	
						if(spacesByCourse != null) {
							mEnrollmentedCourses.add(course);
							mSpaces.add(spacesByCourse);
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				mError = true;
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			if(mError) {
				showNoConnectionAlert();
			} else {
				if (getActivity() != null){
					mTvEnvironment.setText(mEnvironment.name);
					mTvEnvironment.setVisibility(View.VISIBLE);
					
					mIvThumbnail.setImageUrl(mEnvironment.getThumbnailUrl());
					mIvThumbnail.setVisibility(View.VISIBLE);
					
					if(mEnrollmentedCourses.isEmpty()) {
						mTvEmptyList.setVisibility(View.VISIBLE);
					} else {
						mAdapter = new CoursesExpandableListAdapter(getActivity(), mEnrollmentedCourses, mSpaces);
						mListView.setAdapter(mAdapter);
					}
				}
			}
			
			mProgressBar.setVisibility(View.GONE);
		};

	}

}
