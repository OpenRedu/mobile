package br.ufpe.cin.openredu.activities;

import java.util.ArrayList;

import org.scribe.exceptions.OAuthConnectionException;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Environment;
import br.com.developer.redu.models.Folder;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Status;
import br.com.developer.redu.models.Subject;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.fragments.space.MorphologyFragment;
import br.ufpe.cin.openredu.fragments.space.SpaceWallFragment;
import br.ufpe.cin.openredu.fragments.space.SupportMaterialFragment;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class SpaceActivity extends DbHelperHolderActivity {

	public interface SupportMaterialFragmentListener {
	    void onSwitchToNextFragment(Folder folder);
	    void onBackToPreviousFragment(Folder folder);
	}

	public static final int REQUEST_CODE_STATUS = 0;
	public static final int REQUEST_CODE_LECTURE = 1;
	public static final int REQUEST_CODE_MATERIALS = 2;
	
	public static final String EXTRA_STATUS_RESULT = "RESULT_STATUS";
	public static final String EXTRA_LECTURE_RESULT = "EXTRA_LECTURE_RESULT";
	public static final String EXTRA_SUBJECT_RESULT = "EXTRA_SUBJECT_RESULT";

	public static final String EXTRAS_SPACE = "EXTRAS_SPACE";
	
	public static final String EXTRAS_SPACE_ID = "EXTRAS_SPACE_ID";
	public static final String EXTRAS_ENVIRONMENT_PATH = "EXTRAS_ENVIRONMENT_PATH";
	
	public static final String EXTRAS_ITEM_CHECKED = "ITEM_CHECKED";

	private static final String[] titles = new String[]{"Aulas", "Mural", "Material de Apoio"};
	
	static final int NUM_ITEMS = 3;

	public static final int ITEM_MORPHOLOGY = 0;
	public static final int ITEM_WALL = 1;
	public static final int ITEM_SUPPORT_MATERIAL = 2;
	
    private PageIndicator mIndicator;
    private MainAdapter mAdapter;
    private ViewPager mVp;
    
    private Environment mEnvironment;
    private Space mSpace;
//    private Enrollment mEnrollment;
//
//	private Course mCourse;
//	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_space);
		
		final Bundle extras = getIntent().getExtras();
		mSpace = (Space) extras.get(EXTRAS_SPACE);
//		mCourse = (Course) extras.get(Course.class.getName());
//		mEnrollment = (Enrollment) extras.get(Enrollment.class.getName());
		
		// Se foi passado um objeto, é pq está na navegação normal, caso contrário, está no up
		if (mSpace != null) {
			init();
		} else {
			new AsyncTask<Void, Void, Boolean>() {
				protected void onPreExecute() {
					showProgressDialog("Carregando Disciplina…");
				};
				
				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						DefaultReduClient redu = ReduApplication.getReduClient(SpaceActivity.this);
						
						String spaceId = extras.getString(EXTRAS_SPACE_ID);
						String environmentId = extras.getString(EXTRAS_ENVIRONMENT_PATH);

						mSpace = redu.getSpace(spaceId);
						mEnvironment = redu.getEnvironment(environmentId);
					} catch (OAuthConnectionException e) {
						e.printStackTrace();
						return true;
					}
					return false;
				}
	
				protected void onPostExecute(Boolean hasError) {
					dismissProgressDialog();
					if(hasError || mSpace == null || mEnvironment == null) {
						showAlertDialog(SpaceActivity.this, "Não foi possível carregar essa Disciplina.", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						});
					} else {
						Bundle extras = new Bundle();
						extras.putSerializable(EnvironmentActivity.EXTRA_ENVIRONMENT, mEnvironment);
						setUpClass(EnvironmentActivity.class, extras);
						init();
					}
				};
			}.execute();
		}
		
	}
	
	private void init() {
		setActionBarTitle(mSpace.name);

		mVp = (ViewPager) findViewById(R.id.vp2);
		mAdapter = new MainAdapter(getSupportFragmentManager());
		mVp.setAdapter(mAdapter);
		
		mIndicator = (TitlePageIndicator) findViewById(R.id.titles2);
		mIndicator.setViewPager(mVp);
		
		int itemChecked = getIntent().getIntExtra(EXTRAS_ITEM_CHECKED, ITEM_WALL);
		mIndicator.setCurrentItem(itemChecked);
	}
    
	@Override
	public void onRestart() {
		super.onRestart();
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) mAdapter.notifyDataSetChanged();
	}

	class MainAdapter extends FragmentStatePagerAdapter {
		private final Fragment[] items;
		private ArrayList<SupportMaterialFragment> materialFragments;

		public MainAdapter(final FragmentManager fm) {
			super(fm);
			items = new Fragment[NUM_ITEMS];
			materialFragments = new ArrayList<SupportMaterialFragment>();
			items[ITEM_MORPHOLOGY] = new MorphologyFragment();
			items[ITEM_WALL] = new SpaceWallFragment();
			items[ITEM_SUPPORT_MATERIAL] = new SupportMaterialFragment();
			materialFragments.add((SupportMaterialFragment)items[ITEM_SUPPORT_MATERIAL]);

			Bundle args = new Bundle();
			args.putSerializable(SpaceWallFragment.SPACE_EXTRAS, mSpace);
			items[ITEM_WALL].setArguments(args);
			
			args = new Bundle();
			args.putSerializable(SupportMaterialFragment.EXTRAS_SPACE, mSpace);
			items[ITEM_SUPPORT_MATERIAL].setArguments(args);
			
			args = new Bundle();
			args.putSerializable(MorphologyFragment.EXTRAS_SPACE, mSpace);
			items[ITEM_MORPHOLOGY].setArguments(args);
			
			SupportMaterialFragmentListener smfl = new SupportMaterialFragmentListener() {
				@Override
				public void onSwitchToNextFragment(Folder folder) {
					fm.beginTransaction().remove(items[ITEM_SUPPORT_MATERIAL]).commit();
					
					Bundle args = new Bundle();
					args.putSerializable(SupportMaterialFragment.EXTRAS_FOLDER, folder);
					args.putSerializable(SupportMaterialFragment.EXTRAS_SPACE, mSpace);

					SupportMaterialFragment sm = new SupportMaterialFragment();
					sm.setArguments(args);
					sm.setListener(this);
					items[ITEM_SUPPORT_MATERIAL] = sm;
					
					materialFragments.add((SupportMaterialFragment)items[ITEM_SUPPORT_MATERIAL]);
                    
					notifyDataSetChanged();
				}
				
				public void onBackToPreviousFragment(Folder folder){
					materialFragments.remove((SupportMaterialFragment)items[ITEM_SUPPORT_MATERIAL]);
					fm.beginTransaction().remove(items[ITEM_SUPPORT_MATERIAL]).commit();
					items[ITEM_SUPPORT_MATERIAL] = materialFragments.get(materialFragments.size()-1);
					
					notifyDataSetChanged();
				}
			};
			
			((SupportMaterialFragment) items[ITEM_SUPPORT_MATERIAL]).setListener(smfl);
		}
		
		public void addLecture(Lecture lecture, Subject subject) {
			((MorphologyFragment) items[ITEM_MORPHOLOGY]).addLecture(lecture, subject);
		}

		public void addPostedStatus(Status status) {
			((SpaceWallFragment) items[ITEM_WALL]).addPostedStatus(status);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
		
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			return items[position]; 
		}
		
		@Override
		public int getItemPosition(Object object) {
			if (object instanceof SupportMaterialFragment) {
	            return POSITION_NONE;
			}
			/*if (object instanceof MorphologyFragment) {
	            return POSITION_NONE;
			}*/
	        return POSITION_UNCHANGED;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (mVp.getCurrentItem() == ITEM_SUPPORT_MATERIAL){
			if (mAdapter.materialFragments.size() > 1 ) {
				//NOTHING
			}else{
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK) {
			if(requestCode == REQUEST_CODE_LECTURE) {
				Lecture lecture = (Lecture) data.getSerializableExtra(EXTRA_LECTURE_RESULT);
				Subject subject = (Subject) data.getSerializableExtra(EXTRA_SUBJECT_RESULT);
				
				mAdapter.addLecture(lecture, subject);
				
			} else {
				Status status = (Status) data.getExtras().getSerializable(EXTRA_STATUS_RESULT);
				mAdapter.addPostedStatus(status);
			}
		}
	}
}
