package fr.univ_lr.drone_controller;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        LatLng loc = new LatLng(46.1481759,-1.1694211);
        this.gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,15));

        // Gère l'ajout de waypoints(markers) sur la carte pour le tracé
        this.gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                gmap.addMarker(new MarkerOptions().position(point));
                addPoint(point);
            }
        });
    }

    /**
     * Ajoute 'point' dans l'ArrayList de points, et change le texte du TextView, affichant les coordonnées du nouveau point.
     * @param point, le point à ajouter dans l'ArrayList de waypoints
     */
    private void addPoint(LatLng point) {
        this.waypoints.add(point);
        double longitude = point.longitude;
        double latitude = point.latitude;
        this.ptCoords.setText(String.format("Longitude : %s\nLatitude : %s",longitude,latitude));
    }

    /**
     * Parcours l'ArrayList de points et affiche un tracé entre les points i et i+1, i+1 et i+2, etc...
     * Puis lance la transformation de ces points en trames NMEA.
     */
    private void drawLines() {
        for (int i=0 ; i < this.waypoints.size() - 1 ; i++ ) {
            LatLng pt1 = this.waypoints.get(i);
            LatLng pt2 = this.waypoints.get(i+1);

            this.gmap.addPolyline(new PolylineOptions()
                    .add(pt1,pt2)
                    .width(5)
                    .color(Color.BLACK));
        }
        transformIntoNMEA();
    }

    /**
     * Vide la carte de tout points et/ou tracés.
     */
    private void clear() {
        this.gmap.clear();
        this.waypoints = new ArrayList<>();
        this.ptCoords.setText("");
    }

    /**
     * Transforme les waypoints de l'utilisateur par des trames NMEA
     * TODO: 01/04/2019 CRC invalide, voir pourquoi.
     * TODO: 01/04/2019 Coordonnées après transformation incorrectes, mauvais emplacement.
     * TODO: 01/04/2019 Exportation des données vers le simulateur NMEA
     */
    private void transformIntoNMEA() {

        for(LatLng point : this.waypoints) {
            String type = "GPRMC";
            String time = "063951.774";

            String lat = convertIntoDMS(point.latitude);
            String latIndic;
            if (point.latitude < 0 )
                latIndic = "S";
            else
                latIndic = "N";

            String lon = convertIntoDMS(point.longitude);
            String lonIndic;
            if (point.longitude < 0 )
                lonIndic = "E";
            else
                lonIndic = "W";

            String speed = "30";
            String sum = "E";

            String trame = String.format("%s,%s,%s,%s,%s,%s,%s",time,lat,latIndic,lon,lonIndic,speed,sum);

            trame = ("$") + type + "," + trame + ("*") + getChecksum(trame);

            Log.d("NMEA",trame);
        }
    }

    /**
     * Génère un code hexadecimal qui validera la trame NMEA
     * @param in La chaîne de caractères à valider en CRC
     * @return String sous forme hexadécimale
     */
    private static String getChecksum(String in) {
        int checksum = 0;

        int end = in.indexOf('*');
        if (end == -1)
            end = in.length();
        for (int i = 0; i < end; i++) {
            checksum = checksum ^ in.charAt(i);
        }
        String hex = Integer.toHexString(checksum);
        if (hex.length() == 1)
            hex = "0" + hex;
        return hex.toUpperCase();
    }
    
    /**
     * Transforme des coordonnées sous format latitude/longitude en format DMS
     * Exemple : 40.7600000,-73.984000040°      --->     45' 36.000" N  73° 59' 2.400" W
     * @param coord, les coordonnées à convertir en format DMS
     * @return
     * TODO: 01/04/2019 Commenter d'avantage cette fonction
     */
    private static String convertIntoDMS(double coord) {

        coord = Math.abs(coord); // SECURITE

        int coordInteger = (int) coord; // renommer degre
        double coordDecimal = coord - coordInteger;
        String partieEntierePreCalcul;

        if(coordInteger < 10) // on rajoute un 0 pour faire 08
            partieEntierePreCalcul = "0"+coordInteger;
        else // > 10, pas besoin de 0
            partieEntierePreCalcul = String.valueOf(coordInteger);

        coordDecimal *= 60;

        int coordInteger2 = (int) coordDecimal; // minutes en int
        double coordDecimal2 = coordDecimal - coordInteger2;

        // minutes en string
        String partieDecimalePostCalcul = Double.toString(coordDecimal2); // partieDecimalePostCalcul en minutes

        String mmmm = String.valueOf(partieDecimalePostCalcul.charAt(2)) +
                partieDecimalePostCalcul.charAt(3) +
                partieDecimalePostCalcul.charAt(4) +
                partieDecimalePostCalcul.charAt(5);

        String partieEntierePostCalcul;

        if(coordInteger2 < 10) // on rajoute un 0 pour faire 08
            partieEntierePostCalcul = "0"+coordInteger2;
        else // > 10, pas besoin de 0
            partieEntierePostCalcul = String.valueOf(coordInteger2);

        String ddmm = partieEntierePreCalcul + partieEntierePostCalcul;

        return ddmm + "." + mmmm;

    }

}
