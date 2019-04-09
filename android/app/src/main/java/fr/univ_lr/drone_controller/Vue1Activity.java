package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
        nmeaParse("trames.txt");
    }

    /**
     *  Fonction qui parcourt un fichier contenant des trames.
     *  A défaut de faire marcher le socket TCP, permet d'afficher un tracé tout de meme
     * @param fileName le nom du fichier à parcourir
     */
    public void nmeaParse(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getResources().openRawResource(R.raw.trames)));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("FILEREADER",line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
