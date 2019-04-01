package fr.univ_lr.drone_controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class Vue2Activity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private Button toView1, toView3, home, urgence;
    private GoogleMap gmap;
    private SensorManager sensorManager;
    private Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.toView1 = (Button) findViewById(R.id.toView1);
        this.toView3 = (Button) findViewById(R.id.toView3);
        this.home = (Button) findViewById(R.id.buttonHome);
        this.urgence = (Button) findViewById(R.id.buttonEmergency);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);

        mapFragment.getMapAsync(this);

        this.toView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue2Activity.this, Vue1Activity.class);
                startActivity(i);
                finish();
            }
        });

        this.toView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue2Activity.this, Vue3Activity.class);
                startActivity(i);
                finish();
            }
        });

        this.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Home button pushed", Toast.LENGTH_SHORT); toast.show();
            }
        });

        this.urgence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Emergency button pushed", Toast.LENGTH_SHORT); toast.show();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.gmap = map;

        LatLng loc = new LatLng(46.1481759,-1.1694211);
        this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("Axis x",String.valueOf(event.values[0]));
        Log.d("Axis y",String.valueOf(event.values[1]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
