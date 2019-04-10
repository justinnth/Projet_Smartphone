package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Vue1Activity extends AppCompatActivity implements OnMapReadyCallback {

    private Socket socket;

    private static final int SERVERPORT = 55555;
    private static final String SERVER_IP = "10.13.29.233";

    private Button toView2;
    private Button toView3;
    private TextView vitesse;
    private TextView infosDiverses;
    private GoogleMap gmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vue1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Thread(new ClientThread()).start();

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

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.gmap = map;

        LatLng loc = new LatLng(46.1481759,-1.1694211);
        this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
        nmeaParse();
    }

    /**
     *  Fonction qui parcourt un fichier contenant des trames.
     *  A défaut de faire marcher le socket TCP, permet d'afficher un tracé tout de meme
     */
    public void nmeaParse() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getResources().openRawResource(R.raw.trames)));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("Trames",line);
                LatLng pt = convertIntoLatLong(line);
                gmap.addMarker(new MarkerOptions().position(pt));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LatLng convertIntoLatLong(String line) {
        StringBuilder sb;

        sb = new StringBuilder();
        double hour = Double.parseDouble(sb.append(line.charAt(7)).append(line.charAt(8)).toString());
        sb = new StringBuilder();
        double minutes = Double.parseDouble(sb.append(line.charAt(9)).append(line.charAt(10)).toString());
        sb = new StringBuilder();
        double secondes = Double.parseDouble(sb.append(line.charAt(12)).append(line.charAt(13)).toString());

        double lat = hour+(((minutes*60)+(secondes))/3600);


        sb = new StringBuilder();
        hour = Double.parseDouble(sb.append(line.charAt(18)).append(line.charAt(19)).toString());
        sb = new StringBuilder();
        minutes = Double.parseDouble(sb.append(line.charAt(20)).append(line.charAt(21)).toString());
        sb = new StringBuilder();
        secondes = Double.parseDouble(sb.append(line.charAt(23)).append(line.charAt(24)).toString());

        double lng = hour+(((minutes*60)+(secondes))/3600);

        Log.d("debug","lat: "+lat + " lng : "+lng);
        return new LatLng(lat,lng);
    }

    /*private void drawLines() {
        /*for (int i=0 ; i < this.waypoints.size() - 1 ; i++ ) {
            LatLng pt1 = this.waypoints.get(i);
            LatLng pt2 = this.waypoints.get(i+1);

            this.gmap.addPolyline(new PolylineOptions()
                    .add(pt1,pt2)
                    .width(5)
                    .color(Color.BLACK));
        }
    }*/

    /**
     * @// TODO: 01/04/2019 Réussir à faire fonctionner le socket TCP
     */
    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
