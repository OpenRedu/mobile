package br.ufpe.cin.openredu.fragments.lectures;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.activities.DbHelperHolderActivity;
import br.ufpe.cin.openredu.activities.PostStatusOnLectureWallActivity;
import br.ufpe.cin.openredu.db.DbHelper;
import br.ufpe.cin.openredu.fragments.StatusListFragment;

public class LectureWallFragment extends StatusListFragment {

	public static final String EXTRAS_LECTURE = "LECTURE_EXTRAS";
	
	private Lecture mLecture;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mLecture = (Lecture) getArguments().get(EXTRAS_LECTURE);
		((DbHelperHolderActivity) activity).getDbHelper().addDbHelperListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		LinearLayout llFooter = (LinearLayout) inflater.inflate(R.layout.lecture_wall_footer, null);
		llFooter.findViewById(R.id.bt_comment).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), PostStatusOnLectureWallActivity.class);
				i.putExtra(PostStatusOnLectureWallActivity.EXTRAS_LECTURE, mLecture);
				i.putExtra(PostStatusOnLectureWallActivity.EXTRAS_STATUS_IS_HELP_TYPE, false);
				startActivityForResult(i, 0);
			}
		});
		llFooter.findViewById(R.id.bt_ask_help).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), PostStatusOnLectureWallActivity.class);
				i.putExtra(PostStatusOnLectureWallActivity.EXTRAS_LECTURE, mLecture);
				i.putExtra(PostStatusOnLectureWallActivity.EXTRAS_STATUS_IS_HELP_TYPE, true);
				startActivityForResult(i, 0);
			}
		});
		
		LinearLayout llContent = (LinearLayout) v.findViewById(R.id.ll_content);
		llContent.addView(llFooter);
		
		return v;
	}
	
	@Override
	public void onStatusInserted() {
		updateStatusesFromDb(false);
	}

	@Override
	public void onStatusUpdated() {
		// ignoring...
	}

	@Override
	protected String getEmptyListMessage() {
		return "O mural desta aula está sem Comentários ou Pedidos de Ajuda, seja o primeiro a falar algo.";
	}

	@Override
	protected List<Status> getStatuses(DbHelper dbHelper, long timestamp,
			boolean olderThan) {
		return dbHelper.getStatusesByLecture(timestamp, olderThan, NUM_STATUS_BY_PAGE_DEFAULT, mLecture.id);
	}

	@Override
	public String getTitle() {
		return null;
	}
	
	@Override
	protected long getOldestStatusTimestamp() {
		int count = mAdapter.getCount();
		if(count == 0) {
			return System.currentTimeMillis();
		}
		return ((Status) mAdapter.getItem(count-1)).createdAtInMillis;
	}
	
	@Override
	protected long getEarliestStatusTimestamp() {
		int count = mAdapter.getCount();
		if(count == 0) {
			return 0;
		}
		
		return ((Status) mAdapter.getItem(0)).createdAtInMillis;
	}

	@Override
	protected boolean isEnableGoToWallAction() {
		return false;
	}
}