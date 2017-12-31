package com.partho.apps;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class UtilitiesSuiteActivity extends Activity
{
	private ArrayList<String> activityNames;
	private ArrayList<String> activityClassNames;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		CollectActivities();
		PopulateActivitiesListView();
	}
	
	/**
	 * Picks up activites from the current package (defined in our appmanifest).
	 * Ref: https://developer.android.com/reference/android/content/pm/PackageManager.html
	 * 		https://developer.android.com/reference/android/content/pm/PackageInfo.html
	 * 		https://developer.android.com/reference/android/content/pm/ActivityInfo.html
	 */
	private void CollectActivities()
	{
		PackageManager pm = getPackageManager();
		String packageName = getApplicationContext().getPackageName();
		
		
		try
		{
			ActivityInfo[] activities = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities;
			activityNames = new ArrayList<String>();
			activityClassNames = new ArrayList<String>();
			
			for(int i = 0; i < activities.length; ++i)
			{
				if(!activities[i].name.equals(getClass().getName()))
				{
					activityNames.add(activities[i].loadLabel(pm).toString());
					activityClassNames.add(activities[i].name);
				}
			}
		}
		catch(PackageManager.NameNotFoundException nnfe)
		{
		}
	}
	
	/**
	 * Ref: https://developer.android.com/reference/android/widget/ArrayAdapter.html
	 * 		https://developer.android.com/guide/topics/ui/layout/listview.html
	 */
	private void PopulateActivitiesListView()
	{
		ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.utilities_suite_textview, activityNames);
		ListView listView = (ListView) findViewById(R.id.app_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
			{
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					System.out.println("Clicked: " + position + " " + id + " " + view.toString());
					startApp(activityClassNames.get(position));
				}
			});
 	}
	
	public void startApp(String activityClassName)
    {
		try
		{
			Intent intent = new Intent(this, Class.forName(activityClassName));
			startActivity(intent);
			System.out.println(activityClassName + " Started!!");
		}
		catch(java.lang.ClassNotFoundException cnfe)
		{
			System.out.println("Class Not Found: " + activityClassName);
		}
    }
}
