package br.ufpe.cin.openredu.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WebCachedImageView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.developer.redu.models.Status;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.util.DateUtil;
import br.ufpe.cin.openredu.widgets.Breadcrumb;

public class StatusWallAdapter extends BaseAdapter {

	final private LayoutInflater mInflater;
	private List<Status> mStatuses;
	
	public StatusWallAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(mStatuses == null) {
			return 0;
		}
		
		return mStatuses.size();
	}

	@Override
	public Object getItem(int position) {
		return mStatuses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void add(Status status, boolean olderThan) {
		if(mStatuses == null) {
			mStatuses = new ArrayList<Status>();
		}

		if(mStatuses.isEmpty()) {
			mStatuses.add(status);
		} else {
			if(olderThan) {
				addFromEnd(status);
			} else {
				addFromBegin(status);
			}
		}
	}
	
	private void addFromBegin(Status statusToAdd) {
		int size = mStatuses.size();
		
		int i;
		for(i = 0; i < size; i++) {
			Status status = mStatuses.get(i);
			
			if(statusToAdd.id.equals(status.id)) {
				break;
			} else if(statusToAdd.createdAtInMillis >= status.createdAtInMillis) {
				mStatuses.add(i, statusToAdd);
				break;
			}
		}
		
		if(i == size) {
			mStatuses.add(statusToAdd);
		}
	}
	
	private void addFromEnd(Status statusToAdd) {
		int i;
		for(i = mStatuses.size() - 1; i >= 0; i--) {
			Status status = mStatuses.get(i);
			
			if(statusToAdd.id.equals(status.id)) {
				break;
			} else if(statusToAdd.createdAtInMillis <= status.createdAtInMillis) {
				mStatuses.add(i, statusToAdd);
				break;
			}
		}
		
		if(i == -1) {
			mStatuses.add(0, statusToAdd);
		}
	}

	public void addAll(List<Status> statuses, boolean olderThan) {
		for(Status status : statuses) {
			add(status, olderThan);
		}
	}
	
	public void clear() {
		mStatuses = null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.status_wall_item, null);
		}
		
		Status status = mStatuses.get(position);
		
		((Breadcrumb) convertView.findViewById(R.id.tv_breadcrumb)).setStatus(status);
		
		((WebCachedImageView) convertView.findViewById(R.id.iv_photo)).setImageUrl(status.user.getThumbnailUrl());
		
		StringBuffer userActionResultBuffer = new StringBuffer();
		userActionResultBuffer.append("<b>").append(status.user.getCompleteName()).append("</b>");
		
		((TextView) convertView.findViewById(R.id.tv_date)).setText(DateUtil.getFormattedStatusCreatedAt(status));
		convertView.findViewById(R.id.iv_mark_new_status).setVisibility(status.lastSeen ? View.GONE : View.VISIBLE);
		
		if(status.isActivityType()) {
			userActionResultBuffer.append(" comentou");
			((TextView) convertView.findViewById(R.id.tv_result_name)).setVisibility(View.GONE);
			((ImageView) convertView.findViewById(R.id.iv_icon)).setVisibility(View.GONE);
			((TextView) convertView.findViewById(R.id.tv_text)).setText(status.text);
			((TextView) convertView.findViewById(R.id.tv_text)).setVisibility(View.VISIBLE);
			((TextView) convertView.findViewById(R.id.tv_answers)).setVisibility(View.GONE);
			
		} else if (status.isAnswerType()) {
			userActionResultBuffer.append(" comentou");
			((TextView) convertView.findViewById(R.id.tv_result_name)).setVisibility(View.GONE);
			((ImageView) convertView.findViewById(R.id.iv_icon)).setVisibility(View.GONE);
			((TextView) convertView.findViewById(R.id.tv_text)).setText(status.text);
			((TextView) convertView.findViewById(R.id.tv_text)).setVisibility(View.VISIBLE);
			((TextView) convertView.findViewById(R.id.tv_answers)).setVisibility(View.GONE);
		
		} else if (status.isHelpType()) {
			userActionResultBuffer.append(" pediu ajuda");
			((TextView) convertView.findViewById(R.id.tv_result_name)).setVisibility(View.GONE);
			((ImageView) convertView.findViewById(R.id.iv_icon)).setImageResource(R.drawable.ic_ajuda);
			((ImageView) convertView.findViewById(R.id.iv_icon)).setVisibility(View.VISIBLE);
			((TextView) convertView.findViewById(R.id.tv_text)).setText(status.text);
			((TextView) convertView.findViewById(R.id.tv_text)).setVisibility(View.VISIBLE);
			((TextView) convertView.findViewById(R.id.tv_answers)).setText(status.answers_count + ((status.answers_count == 1) ? " Resposta" : " Respostas")); 
			((TextView) convertView.findViewById(R.id.tv_answers)).setVisibility(View.VISIBLE);
		
		} else if (status.isLogType()) {
			String action = null;
			String result = null;
			String name = null;
			int icon = 0;
			
			if (status.isCourseLogeableType()) {
				action = " criou o";
				result = " Curso:";
				name = status.getCourseName();
				icon = R.drawable.ic_curso;
			} else if (status.isLectureLogeableType()) {
				action = " criou a";
				result = " Aula:";
				name = status.getLectureName();
				icon = R.drawable.ic_aula;

			} else if (status.isSubjectLogeableType()) {
				action = " criou o";
				result = " Módulo:";
				name = status.getSubjectName();
				icon = R.drawable.ic_modulo;
			}
			
			userActionResultBuffer.append(action).append("<b>").append(result).append("</b>");
			((TextView) convertView.findViewById(R.id.tv_result_name)).setText(name);
			((TextView) convertView.findViewById(R.id.tv_result_name)).setVisibility(View.VISIBLE);
			((ImageView) convertView.findViewById(R.id.iv_icon)).setImageResource(icon);
			((ImageView) convertView.findViewById(R.id.iv_icon)).setVisibility(View.VISIBLE);
			((TextView) convertView.findViewById(R.id.tv_text)).setVisibility(View.GONE);
			((TextView) convertView.findViewById(R.id.tv_answers)).setVisibility(View.GONE);
		}
		
		((TextView) convertView.findViewById(R.id.tv_user_action_result)).setText(Html.fromHtml(userActionResultBuffer.toString()));
		
		return convertView;
	}
}
