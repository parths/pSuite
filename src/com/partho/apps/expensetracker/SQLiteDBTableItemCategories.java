package com.partho.apps.expensetracker;

import com.partho.utils.data.ISQLiteTable;
import com.partho.utils.data.SQLiteDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;

public class SQLiteDBTableItemCategories extends SQLiteDBTableCategories
{
	private String tblName = "tbl_item_categories";
	private SQLiteDBHelper dbHelper;
	
	public String getName()
	{
		return "tbl_item_categories";
	}
	
}