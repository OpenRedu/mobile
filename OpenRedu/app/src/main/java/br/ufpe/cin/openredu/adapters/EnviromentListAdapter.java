package br.ufpe.cin.openredu.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WebCachedImageView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.developer.redu.models.Environment;
import br.ufpe.cin.openredu.R;

public class EnviromentListAdapter extends BaseAdapter {

	final private LayoutInflater mInflater;
	
	private List<Environment> mEnviroments;
	
	public EnviromentListAdapter(Context context, List<Environment> enviroments) {
		mInflater = LayoutInflater.from(context);
		mEnviroments = enviroments; 
	}
	
	@Override
	public int getCount() {
		if(mEnviroments == null) {
			return 0;
		}
				
		return mEnviroments.size();
	}

	@Override
	public Object getItem(int position) {
		return mEnviroments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
//	public void add(TextView status) {
//		listMaterials.add(status);
//	}
//
//	public void add(List<TextView> statuses) {
//		listMaterials.addAll(statuses);
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = mInflater.inflate(R.layout.enviroment_list_row, null);
		TextView tvEnvironment = (TextView) v.findViewById(R.id.tv_environment);
		WebCachedImageView ivThubmnail = (WebCachedImageView) v.findViewById(R.id.iv_thumbnail);
		
		Environment environment = mEnviroments.get(position);
		
		if (environment.courses_count.equals("0")) {
			tvEnvironment.setText(Html.fromHtml(mEnviroments.get(position).name+"<br/>"+"<font color=\"#CCCCCC\"><small>Ambiente vazio, Não há Cursos</small></font>"));
		} else { 
			tvEnvironment.setText(Html.fromHtml(mEnviroments.get(position).name+"<br/>"+"<font color=\"#CCCCCC\"><small>"+mEnviroments.get(position).courses_count+" Cursos</small></font>"));
		}
		
		ivThubmnail.setImageUrl(environment.getThumbnailUrl());
		
		return v;
	}
}
