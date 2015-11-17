package br.ufpe.cin.openredu.fragments.home;

import java.util.List;

import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.db.DbHelper;
import br.ufpe.cin.openredu.fragments.StatusListFragment;

public class UserWallFragment extends StatusListFragment {

	@Override
	public String getTitle() {
		return "Início";
	}

	@Override
	public String getEmptyListMessage() {
		return "Não há novidades, comece a fazer parte dos Ambientes e Cursos para interagir com outras pessoas";
	}
	
	@Override
	protected List<Status> getStatuses(DbHelper dbHelper, long timestamp, boolean olderThan) {
		return dbHelper.getStatuses(timestamp, olderThan, NUM_STATUS_BY_PAGE_DEFAULT);
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
		return true;
	}
}
