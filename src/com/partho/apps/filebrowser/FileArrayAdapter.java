package com.partho.apps.filebrowser;

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


public class FileArrayAdapter extends ArrayAdapter<FileListItem>
{

	private Context context;
	private int textViewResourceId;
	private List<FileListItem> listItems;

	public FileArrayAdapter(Context context, int textViewResourceId, List<FileListItem> listItems)
	{
		super(context, textViewResourceId, listItems);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.listItems = listItems;
	}
	
	public FileListItem getItem(int idx)
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
		
		FileListItem item = listItems.get(position);
		if(item != null)
		{
			TextView name = (TextView) v.findViewById(R.id.item_name);
			TextView info = (TextView) v.findViewById(R.id.item_info);
			TextView date = (TextView) v.findViewById(R.id.item_date);
			ImageView icon = (ImageView) v.findViewById(R.id.item_icon);
			// TODO: String allocation may be avoided by storing icon strings locally and setting icon id's into the item.
			String uri = "@drawable/" + item.getIcon();
			int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
			Drawable image = context.getResources().getDrawable(imageResource);
			if(image != null && icon != null)
				icon.setImageDrawable(image);
			if(name != null)
				name.setText(item.getName());
			if(info != null)
				info.setText(item.getInfo());
			if(date != null)
				date.setText(item.getDateTime());
		}
		return v;
	}
}
