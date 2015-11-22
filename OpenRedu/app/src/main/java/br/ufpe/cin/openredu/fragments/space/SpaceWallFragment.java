package br.ufpe.cin.openredu.fragments.space;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.activities.DbHelperHolderActivity;
import br.ufpe.cin.openredu.activities.PostStatusOnSpaceWallActivity;
import br.ufpe.cin.openredu.db.DbHelper;
import br.ufpe.cin.openredu.fragments.StatusListFragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class SpaceWallFragment extends StatusListFragment {

	public static final String SPACE_EXTRAS = "SPACE_EXTRAS";
	
	private Space mSpace;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mSpace = (Space) getArguments().get(SPACE_EXTRAS);
		((DbHelperHolderActivity) activity).getDbHelper().addDbHelperListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		Button btComment = (Button) inflater.inflate(R.layout.space_wall_footer, null);
		btComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), PostStatusOnSpaceWallActivity.class);
				i.putExtra(PostStatusOnSpaceWallActivity.EXTRAS_SPACE, mSpace);
				startActivityForResult(i, 0);
			}
		});
		
		LinearLayout llContent = (LinearLayout) v.findViewById(R.id.ll_content);
		llContent.addView(btComment);
		
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
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		Activity activity = getActivity();
		if(activity != null) {
			isWaitingNotification = true;
			mAdapter.clear();
			updateStatusesFromDb(false);
			mRefreshView = refreshView;
		}
	}

	@Override
	protected String getEmptyListMessage() {
		return "O mural desta disciplina está vazio.\nSeja o primeiro a comentar.";
	}

	@Override
	protected List<Status> getStatuses(DbHelper dbHelper, long timestamp,
			boolean olderThan) {
		return dbHelper.getStatusesBySpace(timestamp, olderThan, NUM_STATUS_BY_PAGE_DEFAULT, mSpace.id);
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