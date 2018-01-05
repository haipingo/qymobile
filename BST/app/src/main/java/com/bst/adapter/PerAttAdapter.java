package com.bst.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bst.R;
import com.bst.bean.CheckingBean;

public class PerAttAdapter extends BaseAdapter {
	private Context context;
	List<CheckingBean> list = new ArrayList<CheckingBean>(); 

	public PerAttAdapter(Context context, List<CheckingBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder viewHoder = null;
		if (convertView == null) {
			viewHoder = new ViewHoder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.person_att_list_item, null);
			viewHoder.time = (TextView) convertView.findViewById(R.id.time);
			viewHoder.state = (TextView) convertView.findViewById(R.id.state);
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHoder) convertView.getTag();
		}

		CheckingBean cb = list.get(position);
		viewHoder.time.setText(cb.getCheckingtime());
		viewHoder.state.setText(cb.getCode());
		return convertView;
	}

	class ViewHoder {
		TextView time;
		TextView state;
	}
}
