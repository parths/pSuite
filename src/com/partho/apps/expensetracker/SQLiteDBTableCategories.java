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
		private String date;
		
		public CategoryInfo(int id, String name, String desc, String creationDate)
		{
			this.id = id;
			this.name = name;
			this.desc = desc;
			this.date = creationDate;
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

		public String getDate()
		{
			return date;
		}
	}
	
	private SQLiteDBHelper dbHelper;
	
	public String getName()
	{
		return "tbl_categories";
	}
	
	public String getCreateQuery()
	{
		return 
			"CREATE TABLE " 
			+ getName() + " " 
			+ "(cat_id INTEGER PRIMARY KEY, cat_name TEXT, cat_desc TEXT, creation_date DATETIME DEFAULT CURRENT_TIMESTAMP);\n";
			
	}
	
	public String getDeleteQuery()
	{
		return 
			"DROP TABLE IF EXISTS " + getName();
	}
	
	public void setDBHelper(SQLiteDBHelper helper)
	{
		dbHelper = helper;
	}
	
	public List<CategoryInfo> getCategories()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + getName(), null);
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
					res.getString(res.getColumnIndex("cat_desc")), 
					res.getString(res.getColumnIndex("creation_date"))) );
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
		db.insert(getName(), null, values);
	}
}