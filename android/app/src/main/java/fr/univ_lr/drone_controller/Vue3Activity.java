package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class Vue3Activity extends AppCompatActivity implements OnMapReadyCallback {

    private Button toView1;
    private Button toView2;
    private GoogleMap gmap;
    private ArrayList<LatLng> waypoints;
    private TextView ptCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue3);

        this.toView1 = (Button) findViewById(R.id.toView1);
        this.toView2 = (Button) findViewById(R.id.toView2);
        this.waypoints = new ArrayList<>();
        this.ptCoords = (TextView) findViewById(R.id.textCoordsPoint);

        Button trace = (Button) findViewById(R.id.buttonTrace);
        Button clear = (Button) findViewById(R.id.buttonClear);

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

        trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawLines();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.gmap = map;
        LatLng loc = new LatLng(46.1425159,-1.1444612);
        this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,13));

        this.gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                gmap.addMarker(new MarkerOptions().position(point));
                addPoint(point);
            }
        });
    }

    private void addPoint(LatLng point) {
        this.waypoints.add(point);
        double longitude = point.longitude;
        double latitude = point.latitude;
        this.ptCoords.setText(String.format("Longitude : %s\nLatitude : %s",longitude,latitude));
    }

    private void drawLines() {
        for (int i=0 ; i < this.waypoints.size() - 1 ; i++ ) {
            LatLng pt1 = this.waypoints.get(i);
            LatLng pt2 = this.waypoints.get(i+1);

            this.gmap.addPolyline(new PolylineOptions()
                    .add(pt1,pt2)
                    .width(5)
                    .color(Color.RED));
        }
    }

    private void clear() {
        this.gmap.clear();
        this.waypoints = new ArrayList<>();
        this.ptCoords.setText("");
    }

}
