package com.partho.apps.expensetracker;

import com.partho.utils.data.ISQLiteTable;
import com.partho.utils.data.SQLiteDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;

public class SQLiteDBTableCategories implements ISQLiteTable
{
	public class CategoryInfo
	{
		private int id;
		private String name;
		private String desc;
		
		public CategoryInfo(int id, String name, String desc)
		{
			this.id = id;
			this.name = name;
			this.desc = desc;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDesc()
		{
			return desc;
		}
	}
	
	private String tblName = "tbl_categories";
	private SQLiteDBHelper dbHelper;
	
	public String getName()
	{
		return tblName;
	}
	
	public String getCreateQuery()
	{
		return 
			"CREATE TABLE " 
			+ tblName + " " 
			+ "(cat_id integer primary key, cat_name text, cat_desc text)";
			
	}
	
	public String getDeleteQuery()
	{
		return 
			"DROP TABLE IF EXISTS categories ";
	}
	
	public void setDBHelper(SQLiteDBHelper helper)
	{
		dbHelper = helper;
	}
	
	public List<CategoryInfo> getCategories()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + tblName, null);
		List<CategoryInfo> cInfo = null;
		if(res.getCount() > 0)
		{
			System.out.println("Got Cursor!: " + res.getCount() + " ColCount: " + res.getColumnCount() + "\n" + 
				res.getColumnIndex("cat_id") + " " + res.getColumnIndex("cat_name") + " " + res.getColumnIndex("cat_desc"));
			
			cInfo = new ArrayList<CategoryInfo>();
			res.moveToFirst();
			while(!res.isAfterLast())
			{
				cInfo.add( new CategoryInfo(res.getInt(res.getColumnIndex("cat_id")), 
					res.getString(res.getColumnIndex("cat_name")), 
					res.getString(res.getColumnIndex("cat_desc"))) );
				res.moveToNext();
			}
			res.close();
		}
		return cInfo;
	}
	
	public void addCategory(String name, String desc)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("cat_name", name);
		values.put("cat_desc", desc);
		db.insert(tblName, null, values);
	}
}