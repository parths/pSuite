package com.partho.utils.data;

public interface ISQLiteTable
{
	public String getName();
	public String getCreateQuery();
	public String getDeleteQuery();
	public void setDBHelper(SQLiteDBHelper helper);
}