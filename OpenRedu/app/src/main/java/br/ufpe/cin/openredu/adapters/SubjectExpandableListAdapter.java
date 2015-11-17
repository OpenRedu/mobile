package br.ufpe.cin.openredu.adapters;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Space;
import br.com.developer.redu.models.Subject;
import br.ufpe.cin.openredu.R;
import br.ufpe.cin.openredu.activities.LectureActivity;
import br.ufpe.cin.openredu.activities.SpaceActivity;
import br.ufpe.cin.openredu.activities.lecture.UploadStep1Activity;
import br.ufpe.cin.openredu.util.UserHelper;

public class SubjectExpandableListAdapter extends BaseExpandableListAdapter {

	Activity mActivity;
	List<Subject> mSubjects;
	List<List<Lecture>> mLectures;
	Space mSpace;
	
	public SubjectExpandableListAdapter(Activity activity, List<Subject> subjects, List<List<Lecture>> lectures, Space space) {
		mActivity = activity;
		mSubjects = subjects;
		mLectures = lectures;
		mSpace = space;
	}
	
	public void addLecture(Subject subject, Lecture lecture) {
		for (int i = 0; i < mSubjects.size(); i++) {
			Subject s = mSubjects.get(i);
			if(s.id.equals(subject.id)) {
				mLectures.get(i).add(lecture);
				notifyDataSetChanged();
				break;
			}
		}
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mLectures.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.environment_lecture_row, null);
		}
		
		final Lecture lecture = (Lecture) getChild(groupPosition, childPosition);
		final Subject subject = (Subject) getGroup(groupPosition); 
		
		TextView tvOrder = (TextView) convertView.findViewById(R.id.tvOrdering);
		tvOrder.setText(Integer.toString(lecture.position));
		
		ImageView ivLecture = (ImageView) convertView.findViewById(R.id.ivLecture);
		
		if (lecture.type.equals(Lecture.TYPE_DOCUMENT)){
			ivLecture.setImageResource(R.drawable.ic_doc_mini);
		}else if(lecture.type.equals(Lecture.TYPE_MEDIA)){
			ivLecture.setImageResource(R.drawable.ic_midia_mini);
		}else if(lecture.type.equals(Lecture.TYPE_PAGE)){
			ivLecture.setImageResource(R.drawable.ic_page);
		}else if(lecture.type.equals(Lecture.TYPE_CANVAS)){
			ivLecture.setImageResource(R.drawable.ic_canvas_mini);
		}else if(lecture.type.equals(Lecture.TYPE_EXERCISE)){
			ivLecture.setImageResource(R.drawable.ic_exercice_mini);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.tvLecturesName);
		tv.setText(lecture.name);
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mActivity, LectureActivity.class);
				i.putExtra(LectureActivity.EXTRAS_LECTURE, lecture);
				i.putExtra(LectureActivity.EXTRAS_SUBJECT, subject);
				mActivity.startActivity(i);	
			}
		});
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition >= mLectures.size()) {
			return 0;
		}
		return mLectures.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mSubjects.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mSubjects.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.environment_module_row, null);
		}
		Subject subject = (Subject)getGroup(groupPosition);
		String role = UserHelper.getUserRoleInCourse(mActivity);
		if (role.equals("teacher") || role.equals("environment_admin")){
			ImageView ibAdd = (ImageView) convertView.findViewById(R.id.iv_add);
			ibAdd.setVisibility(View.VISIBLE);
			ibAdd.setTag(subject);
			ibAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent(mActivity, UploadStep1Activity.class);
					Subject subject = (Subject)v.getTag();
					it.putExtra(Subject.class.getName(), subject);
					it.putExtra(Space.class.getName(), mSpace);
					mActivity.startActivityForResult(it, SpaceActivity.REQUEST_CODE_LECTURE);
				}
			});
		}
		
		ImageView ivHelp = (ImageView) convertView.findViewById(R.id.iv_info);
		ivHelp.setTag(subject);
		ivHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder builder = new Builder(mActivity);
				Subject subject = (Subject) v.getTag();
				View v2 = LayoutInflater.from(mActivity).inflate(R.layout.popup_listview_row, null);
				TextView tvInfo = (TextView)v2.findViewById(R.id.tv_insert_file_folder);
				if (subject.description.equals("") || subject.description == null) {
					tvInfo.setText("Este Módulo não possui nenhuma descrição.");
				}else{
					tvInfo.setText(subject.description);
				}
				builder.setView(v2);
				Dialog mDialogInfo = builder.create();
		    	mDialogInfo.show();
			}
		});
		TextView tv = (TextView) convertView.findViewById(R.id.tvSuject);
		tv.setText(Html.fromHtml(subject.name+"<br/>"+"<font color=\"#CCCCCC\"><smal>"+getChildrenCount(groupPosition)+" Aulas</small></font>"));
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
