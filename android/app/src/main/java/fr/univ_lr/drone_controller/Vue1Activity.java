package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;

import java.io.*;

public class Vue1Activity extends AppCompatActivity implements OnMapReadyCallback {

    private Button toView2;
    private Button toView3;
    private TextView vitesse;
    private TextView infosDiverses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);

        this.toView2 = (Button) findViewById(R.id.toView2);
        this.toView3 = (Button) findViewById(R.id.toView3);
        this.vitesse = (TextView) findViewById(R.id.vitesse);
        this.infosDiverses = (TextView) findViewById(R.id.infosDiverses);

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
        
        nmeaParse("tramesNMEA.txt");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng loc = new LatLng(46.1425159,-1.1444612);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,13));
    }

    public void nmeaParse(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName)));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.i("FILEREADER",line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
