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

public class ScreenAddItemCategory extends ScreenBase
{
	public ScreenAddItemCategory(SQLiteDBHelper dbHelper, int layout)
	{
		super(dbHelper, layout);
	}
	
	@Override
	public void SetupUI(View v)
	{
		Button btn = (Button)v.findViewById(R.id.expense_tracker_add_category_btn);
		btn.setOnClickListener(addCategoryListener);
		RefreshCategoriesList(v);
	}
	
	public void RefreshCategoriesList(View v)
	{
		ListView lstView = (ListView)v.findViewById(R.id.expense_tracker_categories_list);
		List<SQLiteDBTableCategories.CategoryInfo> cats = ((SQLiteDBTableItemCategories)(dbHelper.getTableHandler("tbl_item_categories"))).getCategories();
		CategoryArrayAdapter catArr = new CategoryArrayAdapter(getActivity(), R.layout.expense_tracker_category_list_item, cats);
		lstView.setAdapter(catArr);
	}
	
	public void addCategory(View inView)
	{
		EditText name = (EditText)getView().findViewById(R.id.category_name);
		EditText desc = (EditText)getView().findViewById(R.id.category_desc);
		((SQLiteDBTableItemCategories)(dbHelper.getTableHandler("tbl_item_categories"))).addCategory(name.getText().toString(), desc.getText().toString());

		RefreshCategoriesList(getView());
	}
	
	
	private View.OnClickListener addCategoryListener =  new View.OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				addCategory(v);
			}
		};
}
