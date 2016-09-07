package com.boken_edge.lachlan.whattoeat;

/**
 * Created by Lachlan on 13/06/2016.
 */

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.boken_edge.lachlan.whattoeat.GPSTracker;
import com.boken_edge.lachlan.whattoeat.XMLReader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import com.google.android.gms.maps.model.*;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class LocationActivity extends FragmentActivity {
    private GoogleMap map;
    RatingBar ratingBar;
    SeekBar radius;
    Button nav;
    TextView distance;
    double lat = 0.0D;
    double lng = 0.0D;
    Document doc;
    RadioButton drinks;
    RadioButton food;
    RadioButton cafe;
    String type = "food";
    boolean run = false;
    LatLng ll;

    public LocationActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setUpMapIfNeeded();
        Button random = (Button) this.findViewById(R.id.random);
        this.distance = (TextView) this.findViewById(R.id.distance);
        this.ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);
        this.ratingBar.setRating(3.0F);
        this.radius = (SeekBar) this.findViewById(R.id.radiusBar);
        this.radius.setProgress(500);
        this.radius.setMax(2000);
        this.nav = (Button) this.findViewById(R.id.nav);
        this.food = (RadioButton) this.findViewById(R.id.rbFood);
        this.drinks = (RadioButton) this.findViewById(R.id.rbDrinks);
        this.cafe = (RadioButton) this.findViewById(R.id.rbCafe);
        this.distance.setText("500Metres");
        final GPSTracker tracker = new GPSTracker(this.getApplicationContext());
        if (tracker.canGetLocation) {
            tracker.getLocation();
            this.lat = tracker.getLatitude();
            this.lng = tracker.getLongitude();
            ll  = new LatLng(lat, lng);
            this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.lat, this.lng), 14.0F));
            map.addCircle(new CircleOptions().center(ll).radius(radius.getProgress()).fillColor(Color.argb(50, 0, 180, 255)).strokeColor(Color.BLUE).strokeWidth(3));
        }

        this.radius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                map.clear();
                distance.setText("" + seekBar.getProgress() + "Metres");

                float radius = seekBar.getProgress();
                map.addCircle(new CircleOptions().center(ll).radius(radius).fillColor(Color.argb(50, 0, 180, 255)).strokeColor(Color.BLUE).strokeWidth(3));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        random.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                map.clear();
                LocationMark[] markers = new LocationMark[20];
                try {
                    tracker.getLocation();
                    lat = tracker.getLatitude();
                    lng = tracker.getLongitude();
                    LatLng laln = new LatLng(lat, lng);
                    if(laln == ll){
                        run = true;
                    }
                } catch (Exception e) {
                    Log.println(Log.VERBOSE, "gps", "Can't get location");
                }
                if(run = false) {

                    int radi = LocationActivity.this.radius.getProgress();
                    float rating = LocationActivity.this.ratingBar.getRating();


                    Document docu = search(radi, rating, lat, lng);
                    XMLReader xmlr = new XMLReader();

                    markers = xmlr.getMarkers(docu, markers);
                }
                    int length = markers.length;
                    Random rand = new Random();
                    int r = rand.nextInt(length);
                    LocationMark fmark = markers[r];

                    LatLng llM = fmark.get_latLng();
                    LocationActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(fmark.get_latLng(), 14.0F));
                    Marker pin = map.addMarker(new MarkerOptions().position(llM).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(fmark.get_name()).snippet(fmark.get_vicinity() + " " + fmark.get_rating()));
                    pin.showInfoWindow();
                    nav.setVisibility(View.VISIBLE);


            }
        });
        this.nav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:q=" + LocationActivity.this.lat + "," + LocationActivity.this.lng));
                intent.addFlags(268435456);
                LocationActivity.this.startActivity(intent);
            }
        });
    }

    /*
    *
                TEMP TESTING
                FAKE GPS LAT LNG

//                lat = 40.758899;
//                lng = -73.9873197;
//                Document doc = LocationActivity.this.search(radi, rating, LocationActivity.this.lat, LocationActivity.this.lng);
//                ArrayList<LocationMark> markers;


//                LocationActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(fmark.getPosition().latitude, fmark.getPosition().longitude), 14.0F));
//                if( pin.isVisible())
//                {
//                    pin.showInfoWindow();
//                }
//                else {
//                    Log.println(Log.VERBOSE, "","");
//                }
    *
                    Marker[] marks = lmToMarker(markers);


//                Marker[] markx = (Marker[]) markers.toArray(new Marker[markers.size()]);


//                Marker fmark =  marks[r];

//                int length = markers.
//                Marker mark = (Marker) mark.get(rand.nextInt(length));
    *
    *
    *
    * */

    public void getLoc(){

    }

    public Context getCon(){
        return this.getApplicationContext();
    }

    public Marker[] lmToMarker(LocationMark[] loMark){
        Marker[] marks = new Marker[20];
//        zza[] marks = new zza[20];
//        GoogleMap mp = null;
        for(int i = 0; i <= loMark.length; i++){
//            marks[i] = ;
//            marks[i] = new Marker();
            marks[i].setPosition(loMark[i].get_latLng());
            marks[i].setTitle(loMark[i].get_name());
            marks[i].setSnippet(loMark[i].get_vicinity() + " " + loMark[i].get_rating());
//            marks[i]

//           marks[i] = mp.addMarker( (new MarkerOptions()).position(loMark[i].get_latLng()).title(loMark[i].get_name()).snippet("Address:" + loMark[i].get_vicinity() + "/n Rating: " + loMark[i].get_rating()));
        }

        return marks;
    }

    public Document search(int radius, float rating, double lat, double lng) {
        String key = this.getApplicationContext().getResources().getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location=" + lat + "," + lng + "&radius=" + radius + "&type=" + type + "&rating=" + rating + "&key=" + key;

        enableStrictMode();
        RetrieveDoc r = new RetrieveDoc();
//        new RetrieveDoc().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
//        new RetrieveDocs().execute(url);
//        RetrieveDoc retrieveDoc = new RetrieveDoc(this);
//        retrieveDoc.execute(url);
        doc = r.doInBackground(url);
//        Integer n =0;
//        try {
//            doc = new RetrieveDoc().execute(url).get();
//        }catch (Exception err){
//            Log.println(Log.VERBOSE, "HTTPS Error", "HTTPS Error");
//        }
        return doc;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbCafe:
                if(checked)
                    type = "cafe";
                break;
            case R.id.rbDrinks:
                if (checked)
                    type = "bar";
                    break;
            case R.id.rbFood:
                if (checked)
                    type = "Food";
                    break;
        }
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    protected void onResume() {
        super.onResume();
        this.setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (this.map == null) {
            this.map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (this.map != null) {
                this.setUpMap();
            }
        }

    }

    private void setUpMap() {
        this.map.addMarker((new MarkerOptions()).position(new LatLng(0.0D, 0.0D)).title("Marker"));
    }



}