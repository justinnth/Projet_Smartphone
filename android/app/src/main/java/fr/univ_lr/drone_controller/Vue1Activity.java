package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
    private void nmeaParse() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getResources().openRawResource(R.raw.trames)));

            String line;
            ArrayList <LatLng> pts = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                LatLng pt = convertIntoLatLong(line);
                pts.add(pt);
            }

            if(pts.size() > 1) {
                for (int i=0 ; i < pts.size() - 1 ; i++ ) {
                    LatLng pt1 = pts.get(i);
                    LatLng pt2 = pts.get(i+1);

                    this.gmap.addPolyline(new PolylineOptions()
                            .add(pt1,pt2)
                            .width(5)
                            .color(Color.BLACK));
                }
                gmap.addMarker(new MarkerOptions().position(pts.get(0)).title("Point de départ"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit une trame NMEA en coordonnées latitude/longitude
     * @param line La ligne du fichier de trames à decoder
     * @return  Un objet LatLng contenant les coordonnées décodées depuis line
     */
    private LatLng convertIntoLatLong(String line) {

        String[] parts = line.split(",");

        if (parts[2].equals("A")) {
            String latDD = parts[3];
            String[] latData = latDD.split(Pattern.quote("."));
            String dm = latData[0];
            String d = dm.substring(0,2);
            String m = dm.substring(2,4);
            String s = latData[1];

            Double latF = Double.valueOf(d) + (Double.valueOf(m)/60) + ((Double.valueOf(s) * 60/100)/3600);

            if(parts[4].equals("S")) {
                latF = -latF;
            }

            String lngDD = parts[5];
            String[] lngData = lngDD.split(Pattern.quote("."));
            String dm2 = lngData[0];
            String d2 = dm2.substring(2,3);
            String m2 = dm2.substring(3,5);
            String s2 = lngData[1];

            Double lngF = Double.valueOf(d2) + (Double.valueOf(m2)/60) + ((Double.valueOf(s2) * 60/100)/3600);

            if(parts[6].equals("W")) {
                lngF = -lngF;
            }

            this.vitesse.setText("Vitesse : "+parts[7]+" km/h");

            return new LatLng(latF,lngF);
        }
        else return new LatLng(0,0);

    }

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
