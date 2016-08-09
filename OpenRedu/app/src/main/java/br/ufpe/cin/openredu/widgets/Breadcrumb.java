package br.ufpe.cin.openredu.widgets;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import br.com.developer.redu.models.Status;

public class Breadcrumb extends EllipsizingTextView {

	public Breadcrumb(Context context) {
		super(context);
		init();
	}
	
	public Breadcrumb(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Breadcrumb(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setMaxLines(2);
		setEllipsize(TruncateAt.START);
	}
	
	public void setStatus(Status status) {
		List<String> crumbs = status.getBreadcrumbs();
		
		if(crumbs == null || crumbs.size() == 0) {
			setVisibility(View.GONE);
		} else {
			String breadcrumbs = composeBreadcrumbs(crumbs);
			setText(Html.fromHtml(breadcrumbs));
		}
	}
		
	static private String composeBreadcrumbs(List<String> crumbs) {
		StringBuffer sb = new StringBuffer();
		
		Iterator<String> it = crumbs.iterator();
		while(it.hasNext()) {
			String s = it.next();
			if(it.hasNext()) {
				sb.append(s).append(" > ");
			} else {
				sb.append("<b>").append(s).append("</b>");
			}
		}
		
		return sb.toString();
	}
}
