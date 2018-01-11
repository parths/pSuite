package com.partho.apps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.partho.utils.data.SQLiteDBHelper;
import com.partho.utils.data.ISQLiteTable;

public class SecretsManagerActivity extends FragmentActivity
{
 	private String tabItems[];// = {"Add Expense Entry", "Add Expense Category", "View Expenses", "Trendz", "Advice"};
	private ListView mDrawerListView = null;
	private DrawerLayout mDrawerLayout = null;
	private SQLiteDBHelper dbHelper;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		LoadLoginScreen();
    }
	
	public void LoadLoginScreen()
	{
		ClearCurrentUserData();
		if(mDrawerLayout != null)
		{
			mDrawerLayout.closeDrawer();
			mDrawerLayout = null;
		}
		setContentView(R.layout.secrets_manager_login);
	}
	
	public void LoadMainScreen()
	{
		ClearCurrentUserData();
        setContentView(R.layout.secrets_manager_main);
		PopulateTabsListView();
		InitializeDB();
	}
	
	public void ClearCurrentUserData()
	{
	}

/**
	 * Ref: https://developer.android.com/reference/android/widget/ArrayAdapter.html
	 * 		https://developer.android.com/guide/topics/ui/layout/listview.html
	 */
	private void PopulateTabsListView()
	{
		tabItems = getResources().getStringArray(R.array.secrets_nagivation_tab_labels);
		ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.utilities_suite_textview, tabItems);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.nav_ctrl);
		mDrawerListView.setAdapter(adapter);
		mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
 	}
	
	private void InitializeDB()
	{
	}
	
	private class DrawerItemClickListener implements AdapterView.OnItemClickListener 
	{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			openFragment(id);
		}
		// TODO: Change this so that we load the menu options with class name data 
		// and then load the relevant class for the selected menu (check main utilitiesSuite activity class)
		private void openFragment(long id)
		{
			Fragment frag = null;
			switch((int)id)
			{
			case 7:
				finish();
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
				if(mDrawerLayout != null)
					mDrawerLayout.closeDrawer(mDrawerListView);
			}
			else
				System.out.println("Couldn't load fragment");
		}
	}
}
