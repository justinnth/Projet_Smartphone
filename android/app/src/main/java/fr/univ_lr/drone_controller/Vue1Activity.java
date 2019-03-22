package fr.univ_lr.drone_controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public class Vue1Activity extends AppCompatActivity {

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

        this.mapView = findViewById(R.id.mapView);
        this.toView2 = findViewById(R.id.toView2);
        this.toView3 = findViewById(R.id.toView3);
        this.vitesse = findViewById(R.id.vitesse);
        this.infosDiverses = findViewById(R.id.infosDiverses);

        //this.gmap = new GoogleMap();      paramètre d'entrée delegate

    }
}
