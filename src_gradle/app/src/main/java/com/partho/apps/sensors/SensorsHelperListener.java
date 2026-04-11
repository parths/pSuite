package com.partho.apps.sensors;

import java.util.Set;

public interface SensorsHelperListener {
    public abstract void onSensorsInitialized(String debugString, Set<Integer> uniqueSensorTypes);
}
