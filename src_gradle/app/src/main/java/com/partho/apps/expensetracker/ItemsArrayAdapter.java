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
import java.util.ArrayList;

import com.partho.apps.R;


public class ItemsArrayAdapter extends ArrayAdapter<SQLiteDBTableItems.ItemInfo>
{

	private Context context;
	private int textViewResourceId;
	private List<SQLiteDBTableItems.ItemInfo> listItems;

	public ItemsArrayAdapter(Context context, int textViewResourceId, List<SQLiteDBTableItems.ItemInfo> listItems)
	{
		super(context, textViewResourceId, listItems);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.listItems = listItems;
	}
	
	public SQLiteDBTableItems.ItemInfo getItem(int idx)
	{
		return listItems.get(idx);
	}
	
	@Override
	public int getCount()
	{
		if(listItems != null)
			return super.getCount();
		else
			return 0;
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
		
		SQLiteDBTableItems.ItemInfo item = listItems.get(position);
		if(item != null)
		{
			TextView name = (TextView) v.findViewById(R.id.expense_tracker_item_name);
			TextView desc = (TextView) v.findViewById(R.id.expense_tracker_item_desc);
			TextView id = (TextView) v.findViewById(R.id.expense_tracker_item_id);
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
