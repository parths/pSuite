package com.partho.apps.expensetracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.partho.apps.R;
import com.partho.utils.data.SQLiteDBHelper;
import com.partho.utils.data.ISQLiteTable;

import java.util.ArrayList;
import java.util.List;

public class ScreenAddItem extends ScreenBase
{
	
	private SQLiteDBTableCategories.CategoryInfo itemCat = null;
	
	public ScreenAddItem(SQLiteDBHelper dbHelper, int layout)
	{
		super(dbHelper, layout);
	}
	
	@Override
	public void SetupUI(View v)
	{
		RefreshCategoriesList(v);
		RefreshItemsList(v);
		Button addBtn = (Button)v.findViewById(R.id.btn_add_item);
		addBtn.setOnClickListener(addItemListener);
	}

	public void RefreshCategoriesList(View v)
	{
		Spinner lstSpinner = (Spinner)v.findViewById(R.id.expense_tracker_item_cat);
		System.out.println("dbHelper: " + (dbHelper == null));
		final List<SQLiteDBTableCategories.CategoryInfo> cats = ((SQLiteDBTableItemCategories)(dbHelper.getTableHandler("tbl_item_categories"))).getCategories();
		List<String> catNames = new ArrayList<String>();
		for(int i = 0; i < cats.size(); ++i)
			catNames.add(cats.get(i).getName());
		ArrayAdapter catArr = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, catNames);
		catArr.setDropDownViewResource(android.R.layout.simple_spinner_item);
		lstSpinner.setAdapter(catArr);
		lstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
				{
					itemCat = cats.get(position);
					System.out.println("Selected: " + position);
					System.out.println("Cat: " + itemCat.getName() + " " + itemCat.getId());
				}
				
				public void onNothingSelected(AdapterView<?> parent)
				{
				}
			});
	}
	
	public void RefreshItemsList(View v)
	{
		ListView lstView = (ListView)v.findViewById(R.id.expense_tracker_items_list);
		List<SQLiteDBTableItems.ItemInfo> cats = ((SQLiteDBTableItems)(dbHelper.getTableHandler("tbl_items"))).getItems();
		ItemsArrayAdapter itmArr = new ItemsArrayAdapter(getActivity(), R.layout.expense_tracker_items_listitem, cats);
		lstView.setAdapter(itmArr);
	}
	
	public void addItem(View v)
	{
		String name = ((EditText)getView().findViewById(R.id.txt_item_name)).getText().toString();
		int id = Integer.parseInt(((EditText)getView().findViewById(R.id.txt_item_cost)).getText().toString());
		((SQLiteDBTableItems)(dbHelper.getTableHandler("tbl_items"))).addItem(
			name, 
			itemCat.getId(), 
			id, 
			""
		);
	}

	private View.OnClickListener addItemListener =  new View.OnClickListener() 
	{
		@Override
		public void onClick(View v)
		{
			addItem(v);
		}
	};
}
