package com.jkxy.provincedemo;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpAdapter extends BaseAdapter {

	private List<String> data;
	private LayoutInflater inflater;

	public SpAdapter(Context context, List<String> data) {
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View cv, ViewGroup parent) {

		cv = inflater.inflate(R.layout.itemview, null);

		TextView tv = (TextView) cv.findViewById(R.id.tv_item);

		tv.setText(data.get(position));

		cv.setTag(data.get(position));

		return cv;
	}

}
