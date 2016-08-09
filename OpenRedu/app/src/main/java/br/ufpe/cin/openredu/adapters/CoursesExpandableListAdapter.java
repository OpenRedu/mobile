package br.ufpe.cin.openredu.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import br.com.developer.redu.models.Course;
import br.com.developer.redu.models.Space;
import br.ufpe.cin.openredu.R;

public class CoursesExpandableListAdapter extends BaseExpandableListAdapter {

	Context mContext;
	List<Course> mCourses;
	List<List<Space>> mSpaces;
	
	public CoursesExpandableListAdapter(Context context, List<Course> courses, List<List<Space>> spaces) {
		mContext = context;
		mCourses = courses;
		mSpaces = spaces;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mSpaces.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.environment_space_row, null);
		}
		
		final Space space = (Space) getChild(groupPosition, childPosition);
		
		TextView tv = (TextView) convertView.findViewById(R.id.tvSpaceName);
		tv.setText(space.name);
		
		/*convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, SpaceActivity.class);
				i.putExtra(SpaceActivity.EXTRAS_SPACE, space);
				mContext.startActivity(i);	
			}
		});*/
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mSpaces.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mCourses.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mCourses.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.environment_course_row, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.tvTitle);
		if(getChildrenCount(groupPosition) != 0)
			tv.setText(Html.fromHtml(mCourses.get(groupPosition).name + "<br/><font color=\"#CCCCCC\"><small>" + getChildrenCount(groupPosition) + " Disciplinas</small></font>"));
		else
			tv.setText(Html.fromHtml(mCourses.get(groupPosition).name + "<br/><font color=\"#CCCCCC\"><small> Ainda não há Disciplinas neste Curso</small></font>"));
		//ImageView setaCourse = (ImageView) convertView.findViewById(R.id.iv_seta_course); 
		
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
