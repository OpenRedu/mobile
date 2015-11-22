package br.ufpe.cin.openredu.widgets;

import android.content.res.Resources;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import br.ufpe.cin.openredu.util.ImageUtils;

public class VerticalAnimation extends Animation {

	public static void expandOrCollapse( View view2Expand, int expandedHeightDip, int collapsedHeightDip, int duration, Resources res ) {
		
		int expandedHeight = (int) ImageUtils.dipToPx(expandedHeightDip, res);
		int collapsedHeight = (int) ImageUtils.dipToPx(collapsedHeightDip, res);
		
		if (view2Expand.getHeight() < collapsedHeight) {			
			return;
		}

		if (expandedHeight < collapsedHeight) {
			expandedHeight = collapsedHeight;
		}

		final int startHeight = view2Expand.getHeight();
		final int finishHeight = startHeight <= collapsedHeight ? expandedHeight : collapsedHeight;

		view2Expand.startAnimation(new VerticalAnimation(view2Expand, startHeight, finishHeight, duration));
	}

	public static void collapse(View view2Collapse, int duration, Resources res) {
		
		final int startHeight = view2Collapse.getHeight();
		
		if (startHeight <= 0) {
			return;
		}
		
		view2Collapse.startAnimation(new VerticalAnimation(view2Collapse, startHeight, 0, duration));
	}
	
	public static void expand(View view2Expand, int expandedHeightDip, int duration, Resources res) {
		
		final int expandedHeight = (int) ImageUtils.dipToPx(expandedHeightDip, res);
		
		if (view2Expand.getHeight() >= expandedHeight) {
			return;
		}
		
		view2Expand.startAnimation(new VerticalAnimation(view2Expand, 0, expandedHeight, duration));
	}

	private final View _view;
	private final int _startHeight;
	private final int _finishHeight;

	public VerticalAnimation( View view, int startHeight, int finishHeight, int duration ) {
		_view = view;
		_startHeight = startHeight;
		_finishHeight = finishHeight;
		setDuration(duration);
	}

	@Override
	protected void applyTransformation( float interpolatedTime, Transformation t ) {
		final int newHeight = (int)((_finishHeight - _startHeight) * interpolatedTime + _startHeight);
		_view.getLayoutParams().height = newHeight;
		_view.requestLayout();
	}

	@Override
	public void initialize( int width, int height, int parentWidth, int parentHeight ) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds( ) {
		return true;
	}
}
