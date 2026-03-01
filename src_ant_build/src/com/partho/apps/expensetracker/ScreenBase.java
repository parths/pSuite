package com.partho.apps.expensetracker;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.partho.apps.R;
import com.partho.utils.data.SQLiteDBHelper;
import com.partho.utils.data.ISQLiteTable;

import java.util.List;

public abstract class ScreenBase extends Fragment
{
	protected SQLiteDBHelper dbHelper;
	private int layoutID;
	
	public ScreenBase(SQLiteDBHelper dbHelper, int layout)
	{
		this.dbHelper = dbHelper;
		layoutID = layout;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
	{
		View view = inflater.inflate(layoutID, container, false);
		SetupUI(view);
		return view;
	}
	
	public abstract void SetupUI(View v);
}
