package br.ufpe.cin.openredu.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.ReduApplication;
import br.ufpe.cin.openredu.data.LoadStatusesFromWebTask;
import br.ufpe.cin.openredu.fragments.TitlableFragment;
import br.ufpe.cin.openredu.fragments.home.EnvironmentFragment;
import br.ufpe.cin.openredu.fragments.home.LastSeenFragment;
import br.ufpe.cin.openredu.fragments.home.NewLecturesFragment;
import br.ufpe.cin.openredu.fragments.home.UserWallFragment;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class HomeActivity extends DbHelperHolderActivity {

	public static final String ITEM_EXTRA_PARAM = "ITEM_CHECKED";

	static final int NUM_ITEMS = 4;
	
	static final int ITEM_LAST_SEEN_STATUS = 0;
	static final int ITEM_NEW_LECTURES = 1;
	static final int ITEM_WALL = 2;
	static final int ITEM_ENVIRONMENTS = 3;
	
	private View popupMenuButton;
	private PopupWindow popupWindow;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_home);

		popupMenuButton = addActionToActionBar(R.drawable.ic_menu, new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPopupMenuClicked();
			}
		});

		LoadStatusesFromWebTask.run(this);

		final ViewPager vp = (ViewPager) findViewById(R.id.vp);
		vp.setAdapter(new MainAdapter(getSupportFragmentManager()));
		
		PageIndicator indicator = (TitlePageIndicator) findViewById(R.id.titles);
		indicator.setViewPager(vp);
		
		int itemChecked = getIntent().getIntExtra(ITEM_EXTRA_PARAM, ITEM_WALL);
		indicator.setCurrentItem(itemChecked);
		
		initializePopupMenu();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		onPopupMenuClicked();
		return false;
	}

	private void onPopupMenuClicked() {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			popupWindow.showAsDropDown(popupMenuButton);
		}
	}
	
	private void initializePopupMenu() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

		ListView popupMenuContent = (ListView) inflater.inflate(
				R.layout.popup_menu, null);

		popupMenuContent.setAdapter(new ArrayAdapter<String>(getApplicationContext(), 
				R.layout.popup_menu_item, 
				new String[] {"Configurações", "Termos de uso", "Privacidade", "Sair"}));
		
		popupMenuContent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = null;
				
				switch (position) {
				case 0:
					i = new Intent(getApplicationContext(), SettingsActivity.class);
					break;
				case 1:
					i = new Intent(getApplicationContext(), WebViewActivity.class);
					i.putExtra(WebViewActivity.EXTRAS_TITLE, "Termos de Uso");
					i.putExtra(WebViewActivity.EXTRAS_URL, "http://www.redu.com.br/paginas/termos_uso");
					break;
				case 2:
					i = new Intent(getApplicationContext(), WebViewActivity.class);
					i.putExtra(WebViewActivity.EXTRAS_TITLE, "Política de Privacidade");
					i.putExtra(WebViewActivity.EXTRAS_URL, "http://www.redu.com.br/paginas/politica_privacidade");
					break;
				case 3:
					i = new Intent(getApplicationContext(), LoginWebViewActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					ReduApplication.clear(getApplicationContext());

					CookieSyncManager.createInstance(HomeActivity.this);
//					CookieManager.getInstance().setCookie("redu.com.br", "");
//					CookieSyncManager.getInstance().sync();
					CookieManager.getInstance().removeAllCookie();
					CookieSyncManager.getInstance().sync();
					
					break;
				}

				startActivity(i);

				popupWindow.dismiss();
			}
		});

		popupWindow = new PopupWindow(getApplicationContext());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setWidth((int) dipToPx(200, getResources()));
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setContentView(popupMenuContent);
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	public float dipToPx(int dip, Resources res) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
	}
	
	class MainAdapter extends FragmentStatePagerAdapter {
		private final TitlableFragment[] fragments;

		public MainAdapter(FragmentManager fm) {
			super(fm);

			fragments = new TitlableFragment[NUM_ITEMS];

			fragments[ITEM_NEW_LECTURES] = new NewLecturesFragment();
			fragments[ITEM_LAST_SEEN_STATUS] = new LastSeenFragment();
			fragments[ITEM_WALL] = new UserWallFragment();
			fragments[ITEM_ENVIRONMENTS] = new EnvironmentFragment();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments[position].getTitle();
		}
		
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];
		}
	}
}
