package com.bst.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bst.R;
import com.bst.bean.CheckingBean;
import com.bst.bean.User;
import com.bst.utils.BitmapUtils;

public class SelectAttAdapter extends BaseAdapter {
	private Context context;
	List<User> list = new ArrayList<User>();
	public int selectPositon = -1;

	public int getSelectPositon() {
		return selectPositon;
	}

	public void setSelectPositon(int selectPositon) {
		this.selectPositon = selectPositon;
	}

	public SelectAttAdapter(Context context, List<User> list) {
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
					R.layout.select_att_list_item, null);
			viewHoder.photo = (ImageView) convertView.findViewById(R.id.photo);
			viewHoder.checkbox = (ImageView) convertView
					.findViewById(R.id.rightIv);
			viewHoder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHoder) convertView.getTag();
		}
		if (position == selectPositon) {
			viewHoder.checkbox.setImageResource(R.drawable.checkbox_checked);
		} else {
			viewHoder.checkbox.setImageResource(R.drawable.checkbox_default);
		}
		viewHoder.name.setText(list.get(position).getName());
		viewHoder.photo.setImageBitmap(BitmapUtils.base64Tobitmap(list.get(
				position).getPhoto()));
		return convertView;
	}

	class ViewHoder {
		ImageView photo;
		ImageView checkbox;
		TextView name;
	}
}
