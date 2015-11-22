package br.ufpe.cin.openredu.activities.lecture;

import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Subject;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.adapters.PopupAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class UploadStep1Activity extends Activity {

	String superId;
	Space space;
	private Subject subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_file_or_lecture);
		superId = getIntent().getExtras().getString("id");
		subject = (Subject) getIntent().getExtras().get(Subject.class.getName());
		space = (Space) getIntent().getExtras().get(Space.class.getName());
		ListView lv = (ListView) findViewById(R.id.lvInsertFileFolder);
		String[] str = { "Foto", "Vídeo", "Áudio" };
		if (subject != null) {
			lv.setAdapter(new PopupAdapter(this, str, space, subject));
		} else {
			lv.setAdapter(new PopupAdapter(this, str, superId, space));
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
	}

}
