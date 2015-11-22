package br.ufpe.cin.openredu.fragments.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import br.com.developer.redu.DefaultReduClient;
import br.com.developer.redu.models.Environment;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.activities.EnvironmentActivity;
import br.ufpe.cin.openredu.adapters.EnviromentListAdapter;
import br.ufpe.cin.openredu.fragments.TitlableFragment;

public class EnvironmentFragment extends TitlableFragment {

	private static final String ENVIRONMENTS_SAVED = "ENVIRONMENTS_SAVED";

	private ArrayList<Environment> mEnvironments;

	private ListView mListView;
	private ProgressBar mProgressBar;

	public EnvironmentFragment() {

	}
	
	@Override
	public void onNoConnectionAlertClicked() {
		loadEnvironments();
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_environment, container, false);
		mListView = (ListView) v.findViewById(R.id.lvEnviroment);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent i = new Intent(getActivity(), EnvironmentActivity.class);
				i.putExtra(EnvironmentActivity.EXTRA_ENVIRONMENT, mEnvironments.get(position));
				startActivity(i);
			}
		});

		mProgressBar = (ProgressBar) v.findViewById(R.id.pb);

		if (savedInstanceState != null && savedInstanceState.containsKey(ENVIRONMENTS_SAVED)) {
			mProgressBar.setVisibility(View.GONE);
			mEnvironments = (ArrayList<Environment>) savedInstanceState.get(ENVIRONMENTS_SAVED);
			mListView.setAdapter(new EnviromentListAdapter(getActivity(), mEnvironments));

		} else {
			loadEnvironments();
		}

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(ENVIRONMENTS_SAVED, mEnvironments);
		super.onSaveInstanceState(outState);
	}

	@Override
	public String getTitle() {
		return "Ambientes";
	}
	
	private void loadEnvironments() {
		new LoadEnvironmentsTask().execute();
	}
	
	class LoadEnvironmentsTask extends AsyncTask<Void, Void, List<Environment>> {
		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected List<Environment> doInBackground(Void... params) {
			try {
				DefaultReduClient redu = ReduApplication.getReduClient(getActivity());
				return redu.getEnvironments();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(List<Environment> environments) {
			Activity activity = getActivity();
			if (activity != null) {
				if (environments != null) {
					setRetainInstance(true);
					mEnvironments = new ArrayList<Environment>(environments);
					mListView.setAdapter(new EnviromentListAdapter(activity, mEnvironments));
				} else {
					showNoConnectionAlert();
				}
			}
			mProgressBar.setVisibility(View.GONE);
		};
	}
}
