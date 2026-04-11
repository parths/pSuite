package com.partho.apps.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.View;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceSensorsHelper {
    private int[] sensorTypes = new int[] {
            Sensor.TYPE_ACCELEROMETER,
//            Sensor.TYPE_ACCELEROMETER_LIMITED_AXES,
//            Sensor.TYPE_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED,
//            Sensor.TYPE_ACCELEROMETER_UNCALIBRATED,
            Sensor.TYPE_AMBIENT_TEMPERATURE,
            Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_GYROSCOPE,
//            Sensor.TYPE_GYROSCOPE_LIMITED_AXES,
//            Sensor.TYPE_GYROSCOPE_LIMITED_AXES_UNCALIBRATED,
            Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
//            Sensor.TYPE_HEADING,
//            Sensor.TYPE_HEAD_TRACKER,
            Sensor.TYPE_HEART_BEAT,
            Sensor.TYPE_HEART_RATE,
//            Sensor.TYPE_HINGE_ANGLE,
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_LINEAR_ACCELERATION,
//            Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
            Sensor.TYPE_MOTION_DETECT,
            Sensor.TYPE_POSE_6DOF,
            Sensor.TYPE_PRESSURE,
            Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_RELATIVE_HUMIDITY,
            Sensor.TYPE_ROTATION_VECTOR,
            Sensor.TYPE_SIGNIFICANT_MOTION,
            Sensor.TYPE_STATIONARY_DETECT,
            Sensor.TYPE_STEP_COUNTER,
            Sensor.TYPE_STEP_DETECTOR
    };

    private SensorManager sensorManager;
    private Sensor ambientTemperatureSensor;


    /**
     * Ref: https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#java
     */
    public void initSensors(Context context, SensorsHelperListener listener)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sb = new StringBuilder();

//        if(ambientTemperatureSensor == null)
//        {
//            sb.append("Temperature sensor not found!\n");
//            temperatureTextView.setVisibility(View.INVISIBLE);
//        }
//
        Set<Integer> availableSensorTypes = new HashSet<Integer>();
        sb.append("=====\n\n");
        for(int i = 0; i < deviceSensors.size(); i++)
        {
            Sensor sensor = deviceSensors.get(i);
            sb.append("Sensor: ");
            sb.append(sensor.getType());
            sb.append(" :: ");
            sb.append(sensor.toString());
            sb.append("\n=====\n");
            availableSensorTypes.add(sensor.getType());
        }
        System.out.println(sb.toString());
        listener.onSensorsInitialized(sb.toString(), availableSensorTypes);
    }


}
