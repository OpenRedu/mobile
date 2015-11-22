package br.ufpe.cin.openredu.activities.lecture;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import br.com.developer.redu.models.Space;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.adapters.PopupAdapter;

public class UploadFileFolderActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_file_or_lecture);
		ListView lv = (ListView)findViewById(R.id.lvInsertFileFolder);
		String[] str = {"Arquivo de Apoio","Pasta"};
		String id = (String)getIntent().getExtras().getString("id");
		Space space = (Space)getIntent().getExtras().get(Space.class.getName());
		lv.setAdapter(new PopupAdapter(this, str, id, space));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
	}
	
}
