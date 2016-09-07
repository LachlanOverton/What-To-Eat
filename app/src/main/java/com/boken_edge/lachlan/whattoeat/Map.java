package com.boken_edge.lachlan.whattoeat;

/**
 * Created by Lachlan on 13/06/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.boken_edge.lachlan.whattoeat.GPSTracker;
import com.boken_edge.lachlan.whattoeat.XMLReader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class Map extends Activity {
    public Map() {
    }

    static GoogleMap InitializeMap(GoogleMap map, Context contxt) {
//        GPSTracker tracker = new GPSTracker(contxt.getApplicationContext());
//        if(tracker.canGetLocation) {
//            tracker.getLocation();
//        } else {
//            Log.println(Log.VERBOSE, "Getlocation broke", "Getlocation broke");
//        }

        try {
            MapsInitializer.initialize(contxt.getApplicationContext());
        } catch (Exception var9) {
            var9.printStackTrace();
        }

//        try {
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0F));
//        } catch (Exception var8) {
//            var8.printStackTrace();
//        }

        return map;
    }

    static GoogleMap addMarker(GoogleMap map, String title, String snippet, float anchorA, float anchorB, double lat, double lng) {
        try {
            map.addMarker((new MarkerOptions()).title(title).snippet(snippet).anchor(anchorA, anchorB).position(new LatLng(lat, lng)));
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return map;
    }

    static GoogleMap addJourney(GoogleMap map, double lat1, double lng1, double lat2, double lng2, String k) {
        XMLReader j = new XMLReader();
        String path = "origin=" + lat1 + "," + lng1 + "&destination=" + lat2 + "," + lng2;
        String url = "https://maps.googleapis.com/maps/api/directions/xml?" + path + "&key=" + k;
        Map.GetMapDirections r = new Map.GetMapDirections();
        String[] urlArr = new String[]{url};
        Document doc = r.doInBackground(new String[]{url});
        ArrayList directionPoint = j.getDirection(doc);
        PolylineOptions rectLine = (new PolylineOptions()).width(5.0F).color(-16776961);

        for(int i = 0; i < directionPoint.size(); ++i) {
            rectLine.add((LatLng)directionPoint.get(i));
        }

        map.addPolyline(rectLine);
        return map;
    }

    static class GetMapDirections extends AsyncTask<String, Void, Document> {
        GetMapDirections() {
        }

        protected Document doInBackground(String... strings) {
            Document doc = null;
            String urlString = strings[0];

            try {
                URL e = new URL(urlString.toString());
                HttpURLConnection urlConnection = (HttpURLConnection)e.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(urlConnection.getInputStream());
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return doc;
        }
    }
}