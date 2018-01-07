package com.partho.apps;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import java.util.ArrayList;

import com.partho.utils.data.SQLiteDBHelper;
import com.partho.utils.data.ISQLiteTable;
import com.partho.apps.expensetracker.ScreenAddCategory;
import com.partho.apps.expensetracker.ScreenAddExpense;
import com.partho.apps.expensetracker.ScreenAddItemCategory;
import com.partho.apps.expensetracker.ScreenAddItem;
import com.partho.apps.R;

import com.partho.apps.expensetracker.SQLiteDBTableCategories;
import com.partho.apps.expensetracker.SQLiteDBTableExpenses;
import com.partho.apps.expensetracker.SQLiteDBTableItemCategories;
import com.partho.apps.expensetracker.SQLiteDBTableItems;

public class ExpenseTrackerActivity extends FragmentActivity
{

	private String tabItems[];// = {"Add Expense Entry", "Add Expense Category", "View Expenses", "Trendz", "Advice"};
	private ListView mDrawerListView = null;
	private DrawerLayout mDrawerLayout = null;
	private SQLiteDBHelper dbHelper;
	
	private static final String DB_NAME = "expense_tracker.db";
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_tracker_main);
		PopulateTabsListView();
		InitializeDB();
    }

	/**
	 * Ref: https://developer.android.com/reference/android/widget/ArrayAdapter.html
	 * 		https://developer.android.com/guide/topics/ui/layout/listview.html
	 */
	private void PopulateTabsListView()
	{
		tabItems = getResources().getStringArray(R.array.nagivation_tab_labels);
		ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.utilities_suite_textview, tabItems);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_drawer);
		mDrawerListView.setAdapter(adapter);
		mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
 	}
	
	private void InitializeDB()
	{
		dbHelper = new SQLiteDBHelper(this, DB_NAME, 
										new ISQLiteTable[] 
										{
											new SQLiteDBTableExpenses(), 
											new SQLiteDBTableCategories(), 
											new SQLiteDBTableItemCategories(), 
											new SQLiteDBTableItems() 
										}
									);
	}
	
	private class DrawerItemClickListener implements AdapterView.OnItemClickListener 
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			System.out.println("Clicked: " + position + " " + id + " " + view.toString());
			openFragment(id);
		}
		// TODO: Change this so that we load the menu options with class name data 
		// and then load the relevant class for the selected menu (check main utilitiesSuite activity class)
		private void openFragment(long id)
		{
			Fragment frag = null;
			switch((int)id)
			{
			case 0:
				frag = new ScreenAddExpense(dbHelper, R.layout.expense_tracker_add_expense);
				break;
			case 1:
				frag = new ScreenAddCategory(dbHelper, R.layout.expense_tracker_add_category);
				break;
			case 2:
				frag = new ScreenAddItemCategory(dbHelper, R.layout.expense_tracker_add_category);
				break;
			case 3:
				frag = new ScreenAddItem(dbHelper, R.layout.expense_tracker_add_item);
				break;
			default:
				break;
			}
			
			if(frag != null)
			{
				FragmentManager fragMgr = getSupportFragmentManager();
				fragMgr.beginTransaction().replace(R.id.content_frame, frag).commit();
				//mDrawerListView.setItemChecked(id, true);
				//mDrawerListView.setSelection(id);
				mDrawerLayout.closeDrawer(mDrawerListView);
			}
			else
				System.out.println("Couldn't load fragment");
		}
	}
	
	
}
