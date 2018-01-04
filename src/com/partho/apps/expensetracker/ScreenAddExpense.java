package com.partho.apps.expensetracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.partho.apps.R;
import com.partho.utils.data.SQLiteDBHelper;
import com.partho.utils.data.ISQLiteTable;

public class ScreenAddExpense extends ScreenBase
{
	private SQLiteDBHelper dbHelper;
	
	
	public ScreenAddExpense(SQLiteDBHelper dbHelper, int layout)
	{
		super(dbHelper, layout);
	}
	
	@Override
	public void SetupUI(View v)
	{
	}
}
