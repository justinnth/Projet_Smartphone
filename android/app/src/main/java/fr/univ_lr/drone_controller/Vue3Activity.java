package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Vue3Activity extends AppCompatActivity implements OnMapReadyCallback {

    private Button toView1;
    private Button toView2;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue3);

        this.toView1 = (Button) findViewById(R.id.toView1);
        this.toView2 = (Button) findViewById(R.id.toView2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView3);
        mapFragment.getMapAsync(this);

        this.toView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue3Activity.this, Vue1Activity.class);
                startActivity(i);
                finish();
            }
        });
        this.toView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue3Activity.this, Vue2Activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.gmap = map;

        LatLng pt1 = new LatLng(46.1500554,-1.1793666);
        LatLng pt2 = new LatLng(46.1524981,-1.1632486);

        LatLng loc = new LatLng(46.1425159,-1.1444612);
        this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,13));

        Polyline line = this.gmap.addPolyline(new PolylineOptions()
                .add(pt1,pt2)
                .width(5)
                .color(Color.RED));
    }
}
