package com.example.caleb.stepcounter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
/********************************************************************************
 * Caleb Damler
 * Assign 3
 * CSCI 428
 * 3/5/2018
 * this app counts you steps and keeps a timer of how long you have been walking,
 * it also gives you some walking progress stats in regards to how far youve walked
 *********************************************************************************/
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView tvDisplay, milesDisplay, stepsLeft;
    private SensorManager sensorManager;
    private Sensor accelerometer, stepSounter;
    private Chronometer chronometer;
    private long lastPause;
    int isActive = 0;
    int value = -1;
    Button startB, stopB, resetB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDisplay = findViewById(R.id.textView);
        milesDisplay = findViewById(R.id.miles);
        stepsLeft = findViewById(R.id.stepsleft);
        startB = findViewById(R.id.startBtn);
        stopB = findViewById(R.id.stopBtn);
        resetB = findViewById(R.id.button4);
        //chronometer / timer
        chronometer = findViewById(R.id.chronometer2);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepSounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepSounter == null){
            Toast.makeText(this, "sensor not available", Toast.LENGTH_SHORT).show();
        }
        //register listener
        //final boolean batchMode = sensorManager.registerListener(this, stepSounter,SensorManager.SENSOR_DELAY_NORMAL, 3000);//smaller for more offten latency
    }
/*
startClick()
starts the timer and the step counter
 */
    public void startClick(View view){

        isActive = 1; //set to 1 to tell the sensor later on that we do want it to start tracking steps
        //check and start the timer
        if(lastPause != 0){
            chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
        }else{
            chronometer.setBase((SystemClock.elapsedRealtime()));
        }
        chronometer.start();
        startB.setEnabled(false);
        stopB.setEnabled(true);

    }
    /*
    stopClick()
    stops the current timer if it is running
    sets isActive to 0 so the sensor knows to stop counting steps
     */
    public void stopClick(View view){
        isActive = 0;
        lastPause = SystemClock.elapsedRealtime();
        chronometer.stop();
        stopB.setEnabled(false);
        startB.setEnabled(true);
    }
    /*
    resetClick()
    reset the current timer
     */
    public void resetClick(View view){
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        lastPause = 0;
        startB.setEnabled(true);
        stopB.setEnabled(false);


    }
/*
onSensorChanged
keeps track of the steps
only counts when the isActive var is set to 1
isActive is set to 1 in startClick and set to 0
in StopClick
 */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;

        if (values.length > 0) {

            value = (int) values[0];

        }
        //make sure the user has hit the start button
        if (isActive == 1) {
            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                tvDisplay.setText("Total Steps: " + value);
                milesDisplay.setText("Miles walked: " + (double) value / 2000);
                int val2 = 2000 - value;

                stepsLeft.setText("To next mile:" + val2);
            } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                tvDisplay.setText("Step detected: " + value);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
