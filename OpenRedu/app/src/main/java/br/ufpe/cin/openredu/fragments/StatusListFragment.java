package br.ufpe.cin.openredu.fragments;

import java.text.ParseException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.developer.redu.models.Status;
import br.com.developer.redu.models.User;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.activities.DbHelperHolderActivity;
import br.ufpe.cin.openredu.activities.LectureActivity;
import br.ufpe.cin.openredu.activities.SpaceActivity;
import br.ufpe.cin.openredu.activities.StatusDetailActivity;
import br.ufpe.cin.openredu.adapters.StatusWallAdapter;
import br.ufpe.cin.openredu.data.LoadStatusesFromWebTask;
import br.ufpe.cin.openredu.data.OnLoadStatusesFromWebListener;
import br.ufpe.cin.openredu.db.DbHelper;
import br.ufpe.cin.openredu.db.DbHelperListener;
import br.ufpe.cin.openredu.util.DateUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class StatusListFragment extends TitlableFragment implements
		DbHelperListener, OnRefreshListener<ListView> {

	protected static final int NUM_STATUS_BY_PAGE_DEFAULT = 25;
	
	private PullToRefreshListView mListView;
	protected StatusWallAdapter mAdapter;

	private TextView mTvEmptyList;
	private LinearLayout mLlNewStatus;
	private ProgressBar mProgressBar;

	protected boolean isWaitingNotification;
	protected PullToRefreshBase<ListView> mRefreshView;

	protected abstract boolean isEnableGoToWallAction();
	protected abstract String getEmptyListMessage();
	protected abstract long getOldestStatusTimestamp();
	protected abstract long getEarliestStatusTimestamp();
	protected abstract List<Status> getStatuses(DbHelper dbHelper, long timestamp, boolean olderThan);

	private OnLoadStatusesFromWebListener onLoadStatusesFromWebListener = new OnLoadStatusesFromWebListener() {
		@Override
		public void onStart() {
			// do nothing
		}
		
		@Override
		public void onError(Exception e) {
			if(isWaitingNotification) {
				isWaitingNotification = false;
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mRefreshView != null) {
							mRefreshView.onRefreshComplete();
							showNoConnectionAlert();
						}
					}
				});
			}
		}
		
		@Override
		public void onComplete() {
			if(isWaitingNotification) {
				isWaitingNotification = false;
				updateStatusesFromDb(false);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mRefreshView != null) {
							mRefreshView.onRefreshComplete();
						}
					}
				});
			}
		}
	};
	
	public StatusListFragment() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadStatusesFromWebTask.addOnLoadStatusesFromWebListener(onLoadStatusesFromWebListener);
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mRefreshView = refreshView;
		updateStatusesFromWeb();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity != null) {
			((DbHelperHolderActivity) activity).getDbHelper().addDbHelperListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View v = inflater.inflate(R.layout.fragment_listview, container, false);

		mTvEmptyList = (TextView) v.findViewById(R.id.tv_empty_list);

		// TODO Exibir view com New Status, para notificar o usuario que o app
		// recebeu novos Status no bd
		// mLlNewStatus = (LinearLayout) v.findViewById(R.id.ll_new_status);
		// mLlNewStatus.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mLlNewStatus.setVisibility(View.GONE);
		// mListView.smoothScrollTo(0, new OnSmoothScrollFinishedListener() {
		// @Override
		// public void onSmoothScrollFinished() {
		// //mAdapter.clear();
		// //updateStatuses(true);
		// }
		// });
		// }
		// });

		if (mAdapter == null) {
			mAdapter = new StatusWallAdapter(getActivity());
		}

		mProgressBar = (ProgressBar) v.findViewById(R.id.pb);
		
		mListView = (PullToRefreshListView) v.findViewById(R.id.list);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Status status = (Status) mAdapter.getItem(position - 1);

				Intent i = null;
				if (!status.isLogType()) {
					i = new Intent(getActivity(), StatusDetailActivity.class);
					i.putExtra(StatusDetailActivity.EXTRAS_STATUS, status);
					i.putExtra(StatusDetailActivity.EXTRAS_ENABLE_GO_TO_WALL_ACTION, isEnableGoToWallAction());
				} else {
					if(status.isCourseLogeableType() || status.isSubjectLogeableType()) {
						i = new Intent(getActivity(), SpaceActivity.class);
						i.putExtra(SpaceActivity.EXTRAS_ENVIRONMENT_PATH, status.getEnvironmentPath());
						i.putExtra(SpaceActivity.EXTRAS_SPACE_ID, status.getSpaceId());
						
						if(status.isSubjectLogeableType()) {
							i.putExtra(SpaceActivity.EXTRAS_ITEM_CHECKED, SpaceActivity.ITEM_MORPHOLOGY);
						}
						
					} else if(status.isLectureLogeableType()) {
						i = new Intent(getActivity(), LectureActivity.class);
						i.putExtra(LectureActivity.EXTRAS_ENVIRONMENT_PATH, status.getEnvironmentPath());
						i.putExtra(LectureActivity.EXTRAS_SPACE_ID, status.getSpaceId());
						i.putExtra(LectureActivity.EXTRAS_LECTURE_ID, status.getLectureId());
						i.putExtra(LectureActivity.EXTRAS_SUBJECT_ID, status.getSubjectId());
					}
				}
				
				startActivity(i);
				
				DbHelper dbHelper = ((DbHelperHolderActivity) getActivity()).getDbHelper();
				dbHelper.setStatusAsLastSeen(status);

				status.lastSeen = true;
				mAdapter.notifyDataSetChanged();
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// do nothing
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (totalItemCount - (firstVisibleItem + visibleItemCount) == NUM_STATUS_BY_PAGE_DEFAULT / 5) {
					updateStatusesFromDb(true);
				}
			}
		});

		updateStatusesFromDb(false);

		return v;
	}

	protected long getTimestamp(boolean olderThan) {
		return (olderThan) ? getOldestStatusTimestamp()
				: getEarliestStatusTimestamp();
	}

	protected void showEmptyListMessage() {
		mTvEmptyList.setText(getEmptyListMessage());
		mTvEmptyList.setVisibility(View.VISIBLE);

		mListView.setVisibility(View.GONE);
	}

	protected void hideEmptyListMessage() {
		mTvEmptyList.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
	}

	protected void showNewStatusMessage() {
		mLlNewStatus.setVisibility(View.VISIBLE);
	}

	/**
	 * Run on a AsyncTask, because this method access the db.
	 * @param status Status to be inserted
	 * @param appUserId Id of the user.
	 */
	public void addPostedStatus(final Status status) {
		try {
			status.createdAtInMillis = DateUtil.dfIn.parse(status.created_at).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		status.ignorableInSync = true;
		status.lastSeen = true;
		status.lastSeenAtInMillis = status.createdAtInMillis;
		
		mAdapter.add(status, false);
		mAdapter.notifyDataSetChanged();
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				Activity activity = getActivity();
				if (activity != null && activity instanceof DbHelperHolderActivity) {
					status.ignorableInSync = true;
					try {
						User user = ReduApplication.getUser(activity);
						DbHelper dbHelper = ((DbHelperHolderActivity) activity).getDbHelper();
						dbHelper.putStatus(status, String.valueOf(String.valueOf(user.id)));	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
		
	}
	
	@Override
	public void onNoConnectionAlertClicked() {
		updateStatusesFromWeb();
	}

	protected void updateStatusesFromDb(boolean olderThan) {
		new LoadStatusesFromDbTask(olderThan).execute();
	}
	
	protected void updateStatusesFromWeb() {
		Activity activity = getActivity();
		if(activity != null) {
			isWaitingNotification = true;
			LoadStatusesFromWebTask.run(activity);
		}
	}
	
	class LoadStatusesFromDbTask extends AsyncTask<Void, Void, List<br.com.developer.redu.models.Status>> {

		boolean mOlderThan;

		public LoadStatusesFromDbTask(boolean olderThan) {
			mOlderThan = olderThan;
		}

		protected List<br.com.developer.redu.models.Status> doInBackground(Void... params) {
			List<br.com.developer.redu.models.Status> statuses = null;

			Activity activity = getActivity();
			if (activity != null && activity instanceof DbHelperHolderActivity) {
				long timestamp = getTimestamp(mOlderThan);

				DbHelper dbHelper = ((DbHelperHolderActivity) activity).getDbHelper();
				statuses = getStatuses(dbHelper, timestamp, mOlderThan);
			}

			return statuses;
		}

		protected void onPostExecute(List<br.com.developer.redu.models.Status> statuses) {
			if (getActivity() != null) {
				if (statuses != null && statuses.size() > 0) {
					mAdapter.addAll(statuses, mOlderThan);
					mAdapter.notifyDataSetChanged();
				}
				
				if (mAdapter.isEmpty()) {
					if(LoadStatusesFromWebTask.isWorking()) {
						mProgressBar.setVisibility(View.VISIBLE);
					} else {
						mProgressBar.setVisibility(View.GONE);
						showEmptyListMessage();
					}
				} else {
					mProgressBar.setVisibility(View.GONE);
					hideEmptyListMessage();
				}
			}
		};
	}
}
