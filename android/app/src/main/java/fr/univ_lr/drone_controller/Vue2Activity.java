package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class Vue2Activity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button toView1 = (Button) findViewById(R.id.toView1);
        Button toView3 = (Button) findViewById(R.id.toView3);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);

        mapFragment.getMapAsync(this);

        toView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue2Activity.this, Vue1Activity.class);
                startActivity(i);
                finish();
            }
        });

        toView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue2Activity.this, Vue3Activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng loc = new LatLng(46.1425159,-1.1444612);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,13));
    }
}
