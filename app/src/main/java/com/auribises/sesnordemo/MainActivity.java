package com.auribises.sesnordemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    TextView txtTitle;
    Button btnActivate;

    SensorManager sensorManager;
    Sensor sensor;

    String message = "I am in Emergency. Please Help";
    String[] numbers = {"+91 99999 99999","+91 88888 88888"};


    void initViews() {
        txtTitle = (TextView) findViewById(R.id.textView);
        btnActivate = (Button) findViewById(R.id.button);
        btnActivate.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // to detect shake
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            // Write the code to activate the sensor
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] values = sensorEvent.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];


        float calculation = ((x * x) + (y * y) + (z * z)) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        if (calculation > 2) {
            txtTitle.setText("Device Shaken");
            sensorManager.unregisterListener(this); // Stop Listening to changes in Sensor


            // Make a Phone Call
            // Add Permissions in Manifest

            /*Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+91999999999"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Please Grant Prmissions in Settings",Toast.LENGTH_LONG).show();
            }else {
                startActivity(intent);
            }*/


            // Needs Permission in Manifest
            // Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            //smsManager.sendTextMessage("+91 99999 99999",null,message,null,null);
            for(String num : numbers){
                smsManager.sendTextMessage(num,null,message,null,null);
            }


        }else{
            txtTitle.setText(x+" : "+y+" : "+z);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
