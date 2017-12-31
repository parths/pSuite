package com.partho.apps.expensetracker;

import com.partho.utils.data.ISQLiteTable;
import com.partho.utils.data.SQLiteDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

public class SQLiteDBTableExpenses implements ISQLiteTable
{
	public class ExpenseInfo
	{
		private int id;
		private int catId;
		private float cost;
		private Date date;
		private String desc;
		
		public ExpenseInfo(int id, int cat_id, float cost, Date date, String desc)
		{
			this.id = id;
			this.catId = cat_id;
			this.cost = cost;
			this.date = date;
			this.desc = desc;
		}
		
		public int getId()
		{
			return id;
		}
		
		public int getCatId()
		{
			return catId;
		}
		
		public float getCost()
		{
			return cost;
		}
		
		public Date getDate()
		{
			return date;
		}
		
		public String getDesc()
		{
			return desc;
		}
	}
	
	private String tblName = "tbl_expenses";
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
			+ "(exp_id integer primary key, exp_cat INTEGER, exp_date DATETIME DEFAULT CURRENT_TIMESTAMP, exp_cost FLOAT, exp_desc TEXT)";
			
	}
	
	public String getDeleteQuery()
	{
		return 
			"DROP TABLE IF EXISTS " + tblName;
	}
	
	public void setDBHelper(SQLiteDBHelper helper)
	{
		dbHelper = helper;
	}
	
	public List<ExpenseInfo> getExpenses()
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + tblName, null);
		List<ExpenseInfo> cInfo = null;
		if(res.getCount() > 0)
		{
			cInfo = new ArrayList<ExpenseInfo>();
			res.moveToFirst();
			while(!res.isAfterLast())
			{
				cInfo.add( new ExpenseInfo(res.getInt(res.getColumnIndex("exp_id")), 
					res.getInt(res.getColumnIndex("exp_cat")), 
					res.getFloat(res.getColumnIndex("exp_cost")), 
					new Date(res.getLong(res.getColumnIndex("exp_date"))), 
					res.getString(res.getColumnIndex("exp_desc")))
				);
				res.moveToNext();
			}
			res.close();
		}
		return cInfo;
	}
	
	public void addExpense(int cat_id, Date date, float cost, String desc)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("exp_cat", cat_id);
		values.put("exp_date", "datetime('now', 'localtime')");
		values.put("exp_cost", cost);
		values.put("exp_desc", desc);
		db.insert(tblName, null, values);
	}
}