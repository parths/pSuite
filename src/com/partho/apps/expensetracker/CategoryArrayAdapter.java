package com.partho.apps.expensetracker;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.List;

import com.partho.apps.R;


public class CategoryArrayAdapter extends ArrayAdapter<SQLiteDBTableCategories.CategoryInfo>
{

	private Context context;
	private int textViewResourceId;
	private List<SQLiteDBTableCategories.CategoryInfo> listItems;

	public CategoryArrayAdapter(Context context, int textViewResourceId, List<SQLiteDBTableCategories.CategoryInfo> listItems)
	{
		super(context, textViewResourceId, listItems);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.listItems = listItems;
	}
	
	public SQLiteDBTableCategories.CategoryInfo getItem(int idx)
	{
		return listItems.get(idx);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if(v == null)
		{
			LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(textViewResourceId, null);
		}
		
		SQLiteDBTableCategories.CategoryInfo item = listItems.get(position);
		if(item != null)
		{
			TextView name = (TextView) v.findViewById(R.id.expense_tracker_cat_name);
			TextView desc = (TextView) v.findViewById(R.id.expense_tracker_cat_desc);
			TextView id = (TextView) v.findViewById(R.id.expense_tracker_cat_id);
			if(name != null)
				name.setText(item.getName());
			if(desc != null)
				desc.setText(item.getDesc());
			if(id != null)
				id.setText(Integer.toString(item.getId()));
		}
		return v;
	}
}
