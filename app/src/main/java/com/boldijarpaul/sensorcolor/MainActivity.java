package com.boldijarpaul.sensorcolor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private View mBackground;

    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float minZ = Float.MAX_VALUE;
    private float maxX = Float.MIN_VALUE;
    private float maxY = Float.MIN_VALUE;
    private float maxZ = Float.MIN_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBackground = findViewById(R.id.background);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        minX = Math.min(x, minX);
        minY = Math.min(y, minY);
        minZ = Math.min(z, minZ);
        maxX = Math.max(x, maxX);
        maxY = Math.max(y, maxY);
        maxZ = Math.max(z, maxZ);

        x = rangePercent(minX, maxX, x);
        y = rangePercent(minY, maxY, y);
        z = rangePercent(minZ, maxZ, z);


        int xInt = (int) (x * 255);
        int yInt = (int) (y * 255);
        int zInt = (int) (z * 255);
        int color = Color.rgb(xInt, yInt, zInt);

        mBackground.setBackgroundColor(color);

    }

    // if range is 10 - 50 , if input is 10, it will be returned 0, if 50 , 100
    private float rangePercent(float min, float max, float input) {
        return ((input - min)) / (max - min);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
