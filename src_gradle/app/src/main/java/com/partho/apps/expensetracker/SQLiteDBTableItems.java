package com.partho.apps.expensetracker;

import com.partho.utils.data.ISQLiteTable;
import com.partho.utils.data.SQLiteDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;

public class SQLiteDBTableItems implements ISQLiteTable
{
	public class ItemInfo
	{
		private int id;
		private int catId;
		private String name;
		private int cost;
		private String desc;
		
		public ItemInfo(int id, int catId, String name, int cost, String desc)
		{
			this.id = id;
			this.catId = catId;
			this.name = name;
			this.desc = desc;
			this.cost = cost;
		}
		
		public int getId()
		{
			return id;
		}
		
		public int getCatId()
		{
			return catId;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDesc()
		{
			return desc;
		}

		public int getCost()
		{
			return cost;
		}
	}
	
	private SQLiteDBHelper dbHelper;
	
	public String getName()
	{
		return "tbl_items";
	}
	
	public String getCreateQuery()
	{
		return 
			"CREATE TABLE " 
			+ getName() + " " 
			+ "(itm_id INTEGER PRIMARY KEY, itm_cat INTEGER, itm_name TEXT, itm_cost INT, itm_desc TEXT);\n";
			
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
	
	public List<ItemInfo> getItems()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + getName(), null);
		List<ItemInfo> itemInfo = null;
		if(res.getCount() > 0)
		{
			System.out.println("Got Cursor!: " + res.getCount() + " ColCount: " + res.getColumnCount() + "\n" + 
				res.getColumnIndex("itm_id") + " " + res.getColumnIndex("itm_name") + " " + res.getColumnIndex("itm_desc"));
			
			itemInfo = new ArrayList<ItemInfo>();
			res.moveToFirst();
			while(!res.isAfterLast())
			{
				itemInfo.add( new ItemInfo(res.getInt(res.getColumnIndex("itm_id")), 
					res.getInt(res.getColumnIndex("itm_cat")), 
					res.getString(res.getColumnIndex("itm_name")), 
					res.getInt(res.getColumnIndex("itm_cost")), 
					res.getString(res.getColumnIndex("itm_desc"))) );
				res.moveToNext();
			}
			res.close();
		}
		return itemInfo;
	}
	
	public void addItem(String name, int catId, int cost, String desc)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("itm_cat", catId);
		values.put("itm_name", name);
		values.put("itm_cost", cost);
		values.put("itm_desc", desc);
		db.insert(getName(), null, values);
	}
}