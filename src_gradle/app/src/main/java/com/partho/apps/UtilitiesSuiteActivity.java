package com.partho.apps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.partho.apps.sensors.DeviceSensorsHelper;
import com.partho.apps.sensors.SensorsHelperListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;


public class UtilitiesSuiteActivity extends Activity implements SensorEventListener, SensorsHelperListener
{
	private ArrayList<String> activityNames;
	private ArrayList<String> activityClassNames;

	private SensorManager sensorManager;
//	private Sensor ambientTemperatureSensor = null;
//	private Sensor lightSensor = null;
//	private Sensor stepCounterSensor = null;
	private DeviceSensorsHelper sensorsHelper;

	private TextView temperatureTextView;
//	private TextView logTextView;

	private LinearLayout sensorsVBox;
	private HashMap<Integer, TextView> sensorLabels = new HashMap<>(5);
	private HashMap<Integer, Sensor> sensors = new HashMap<>(5);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		CollectActivities();
		PopulateActivitiesListView();

//		logTextView = findViewById(R.id.log_text_view);
		temperatureTextView = findViewById(R.id.temperature_text_view);
		sensorsVBox = findViewById(R.id.sensor_data_container);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorsHelper = new DeviceSensorsHelper();
		sensorsHelper.initSensors(this, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if(ambientTemperatureSensor != null)
//		{
//			sensorManager.registerListener(this, ambientTemperatureSensor,
//					SensorManager.SENSOR_DELAY_NORMAL);
//		}
//		if(lightSensor != null) {
//			sensorManager.registerListener(this, lightSensor,
//					SensorManager.SENSOR_DELAY_NORMAL);
//		}
//		if(stepCounterSensor != null) {
//			sensorManager.registerListener(this, stepCounterSensor,
//					SensorManager.SENSOR_DELAY_NORMAL);
//		}
		for(Sensor sensor : sensors.values()) {
			if(sensor != null) {
				sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
//		if((ambientTemperatureSensor != null)
//			|| (lightSensor != null) || (stepCounterSensor != null))
//		{
//			sensorManager.unregisterListener(this);
//		}
		for(Sensor sensor : sensors.values()) {
			if(sensor != null) {
				sensorManager.unregisterListener(this);
				break;
			}
		}
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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.utilities_suite_textview, activityNames);
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

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE)
		{
			float ambientTemperature = event.values[0];
			String formattedTemp = event.sensor.getName() + String.format(Locale.ROOT, ": %.2f", ambientTemperature);
			temperatureTextView.setText(formattedTemp);
			if(sensorLabels.containsKey(Sensor.TYPE_AMBIENT_TEMPERATURE)) {
				TextView lbl = sensorLabels.get(Sensor.TYPE_AMBIENT_TEMPERATURE);
				lbl.setTextColor(Color.parseColor("#44AA44"));
				lbl.setText(formattedTemp);
			}
		}
		if(event.sensor.getType() == Sensor.TYPE_LIGHT)
		{
			float lux = event.values[0];
			String formattedLight = event.sensor.getName() + String.format(Locale.ROOT, ": %.2f lux", lux);
			if(sensorLabels.containsKey(Sensor.TYPE_LIGHT)) {
				TextView lbl = sensorLabels.get(Sensor.TYPE_LIGHT);
				lbl.setTextColor(Color.parseColor("#44AA44"));
				lbl.setText(formattedLight);
			}
//			temperatureTextView.setText(formattedLight);
		}
		if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER)
		{
			float steps = event.values[0];
			String formattedSteps = event.sensor.getName() + String.format(Locale.ROOT, ": %.2f steps", steps);
			if(sensorLabels.containsKey(Sensor.TYPE_STEP_COUNTER)) {
				TextView lbl = sensorLabels.get(Sensor.TYPE_STEP_COUNTER);
				lbl.setTextColor(Color.parseColor("#44AA44"));
				lbl.setText(formattedSteps);
			}
		}
		if(event.sensor.getType() == Sensor.TYPE_PROXIMITY)
		{
			float proximity = event.values[0];
			String proximityStr = event.sensor.getName() +
					String.format(Locale.ROOT, ": %.2f / %2f ",
							proximity, event.sensor.getMaximumRange());
			if(sensorLabels.containsKey(Sensor.TYPE_PROXIMITY)) {
				TextView lbl = sensorLabels.get(Sensor.TYPE_PROXIMITY);
				lbl.setTextColor(Color.parseColor("#44AA44"));
				lbl.setText(proximityStr);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	@Override
	public void onSensorsInitialized(String debugString, Set<Integer> uniqueSensorTypes) {
		logToSensorsTextView(debugString);
		for(Integer sensorType : uniqueSensorTypes) {
			switch(sensorType) {
				case Sensor.TYPE_LIGHT:
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
				case Sensor.TYPE_STEP_COUNTER:
				case Sensor.TYPE_PROXIMITY:
					sensors.put(sensorType,
						sensorManager.getDefaultSensor(sensorType));
					break;
			}
			addSensorLabel(sensorType);
//			if(sensorType == Sensor.TYPE_LIGHT) {
//				lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//				addSensorLabel(Sensor.TYPE_LIGHT);
//			}
//			else if(sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE) {
//				ambientTemperatureSensor = sensorManager.getDefaultSensor(
//						Sensor.TYPE_AMBIENT_TEMPERATURE);
//				addSensorLabel(Sensor.TYPE_AMBIENT_TEMPERATURE);
//			}
//			else if(sensorType == Sensor.TYPE_STEP_COUNTER) {
//				stepCounterSensor = sensorManager.getDefaultSensor(
//						Sensor.TYPE_STEP_COUNTER);
//				addSensorLabel(Sensor.TYPE_STEP_COUNTER);
//			}
//			else {
////				sensors.put(sensorType, sensorManager.getDefaultSensor(
////						Sensor.TYPE_STEP_COUNTER));
//				addSensorLabel(sensorType);
//			}
		}
	}

	private void addSensorLabel(Integer sensorID) {
		if(sensorLabels.containsKey(sensorID)) return;
		Sensor sensor = sensorManager.getDefaultSensor(sensorID);

		TextView label = new TextView(this);
		label.setPadding(10, 10, 10, 10);
		label.setTextSize(20);
		String s = sensor != null ? sensor.getName() : "Sensor Data pending...";
		label.setText(s);
		sensorsVBox.addView(label);
		sensorLabels.put(sensorID, label);
	}

	private void logToSensorsTextView(String msg)
	{
//		runOnUiThread(
//				() ->
//				{
//					if(logTextView != null)
//					{
//						logTextView.append(msg);
//					}
//				}
//		);
	}
}
