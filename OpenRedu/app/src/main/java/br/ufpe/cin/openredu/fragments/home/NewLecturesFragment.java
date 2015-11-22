package br.ufpe.cin.openredu.fragments.home;

import java.util.List;

import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.db.DbHelper;
import br.ufpe.cin.openredu.fragments.StatusListFragment;

public class NewLecturesFragment extends StatusListFragment {

	@Override
	public String getTitle() {
		return "Novas Aulas";
	}

	@Override
	protected String getEmptyListMessage() {
		return "Não há Novas Aulas";
	}

	@Override
	protected List<Status> getStatuses(DbHelper dbHelper, long timestamp, boolean olderThan) {
		return dbHelper.getNewLecturesStatuses(timestamp, olderThan, NUM_STATUS_BY_PAGE_DEFAULT);
	}

	@Override
	public void onStatusInserted() {
		updateStatusesFromDb(false);
	}

	@Override
	public void onStatusUpdated() {
		// ignoring
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
