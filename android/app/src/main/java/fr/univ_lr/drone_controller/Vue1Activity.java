package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Vue1Activity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private Button toView2;
    private Button toView3;
    private TextView vitesse;
    private TextView infosDiverses;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);

        this.toView2 = findViewById(R.id.toView2);
        this.toView3 = findViewById(R.id.toView3);
        this.vitesse = findViewById(R.id.vitesse);
        this.infosDiverses = findViewById(R.id.infosDiverses);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

        mapFragment.getMapAsync(this);

        this.toView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue1Activity.this, Vue2Activity.class);
                startActivity(i);
                finish();
            }
        });

        this.toView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Vue1Activity.this, Vue3Activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.gmap = map;
    }
}
