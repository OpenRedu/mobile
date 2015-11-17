package br.ufpe.cin.openredu.activities;

import br.ufpe.cin.openredu.R;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends BaseActivity {

	public static final String EXTRAS_URL = "EXTRAS_URL";
	public static final String EXTRAS_TITLE = "EXTRAS_TITLE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_web_view);
		
		Bundle extras = getIntent().getExtras();
		String url = extras.getString(EXTRAS_URL);
		String title = extras.getString(EXTRAS_TITLE);
		
		setActionBarTitle(title);
		
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb);
		
		WebView wv = (WebView) findViewById(R.id.webview);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
			}
		});
		wv.loadUrl(url);
	}
}
