package com.partho.utils.data;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.HashMap;
import java.util.Iterator;

public class SQLiteDBHelper extends SQLiteOpenHelper
{
	
	private HashMap <String, ISQLiteTable> tables = new HashMap<String, ISQLiteTable>();
	private String dbName;
	
	public SQLiteDBHelper(Context context, String dbName, ISQLiteTable tables[])
	{
		super(context, dbName, null, 1);
		this.dbName = dbName;
		for(int i = 0; i < tables.length; ++i)
		{
			tables[i].setDBHelper(this);
			this.tables.put(tables[i].getName(), tables[i]);
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		System.out.println("Create DB");
		Iterator<ISQLiteTable> iter = tables.values().iterator();
		while(iter.hasNext())
		{
			ISQLiteTable tbl = iter.next();
			System.out.println("Exec: " + tbl.getCreateQuery());
			db.execSQL(tbl.getCreateQuery());
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		System.out.println("Upgrade DB");
	}
	
	public ISQLiteTable getTableHandler(String tblName)
	{
		return tables.get(tblName);
	}
}