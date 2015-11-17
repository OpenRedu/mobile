package br.ufpe.cin.openredu.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.util.ImageUtils;
import br.ufpe.cin.openredu.widgets.VerticalAnimation;

public abstract class NoConnectNotifiableFragment extends Fragment {

	private View mNoConnectionView;
	private int mFinishedHeight;

	public abstract void onNoConnectionAlertClicked();
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		FrameLayout noConnectionContainer = (FrameLayout) view.findViewById(R.id.no_connection_container);
		if(noConnectionContainer != null) {
			mNoConnectionView = LayoutInflater.from(getActivity()).inflate(R.layout.no_connection, null);
			mNoConnectionView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					animateNoConnectionAlert(false);
					onNoConnectionAlertClicked();
				}
			});

			noConnectionContainer.addView(mNoConnectionView, new LayoutParams(LayoutParams.MATCH_PARENT, mFinishedHeight));
			
			view.invalidate();
		}
	}
	
	
	private void animateNoConnectionAlert(boolean isToShow) {
		if(getActivity() != null && mNoConnectionView != null) {
			
			int expandedHeightDip = 38;
			int collapsedHeightDip = 0;
			int duration = 500;
			Resources res = getResources();
			
			int expandedHeight = (int) ImageUtils.dipToPx(expandedHeightDip, res);
			int collapsedHeight = (int) ImageUtils.dipToPx(collapsedHeightDip, res);
			
			if (mNoConnectionView.getHeight() < collapsedHeight) {			
				return;
			}
			
			if (expandedHeight < collapsedHeight) {
				expandedHeight = collapsedHeight;
			}
	
			final int startHeight = mNoConnectionView.getHeight();
			
			if (isToShow) {
				mFinishedHeight = expandedHeight;
			} else {
				mFinishedHeight = collapsedHeight;
			}
	
			mNoConnectionView.startAnimation(new VerticalAnimation(mNoConnectionView, startHeight, mFinishedHeight, duration));
		}	
	}
	
	public void showNoConnectionAlert() {
		animateNoConnectionAlert(true);
	}
}
