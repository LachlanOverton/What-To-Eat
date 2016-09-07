package com.boken_edge.lachlan.whattoeat;

/**
 * Created by Lachlan on 13/06/2016.
 */

import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
    public static final String MODE_DRIVING = "driving";
    public static final String MODE_WALKING = "walking";

    public XMLReader() {
    }

    public String getDurationText(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("duration");
            Node node1 = e.item(0);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(this.getNodeIndex(nl2, "text"));
            Log.i("DurationText", node2.getTextContent());
            return node2.getTextContent();
        } catch (Exception var6) {
            return "0";
        }
    }

    public int getDurationValue(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("duration");
            Node node1 = e.item(0);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(this.getNodeIndex(nl2, "value"));
            Log.i("DurationValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());
        } catch (Exception var6) {
            return -1;
        }
    }

    public String getDistanceText(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("distance");
            Node node1 = e.item(e.getLength() - 1);
            NodeList nl2 = null;
            nl2 = node1.getChildNodes();
            Node node2 = nl2.item(this.getNodeIndex(nl2, "value"));
            Log.d("DistanceText", node2.getTextContent());
            return node2.getTextContent();
        } catch (Exception var6) {
            return "-1";
        }
    }

    public int getDistanceValue(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("distance");
            Node node1 = null;
            node1 = e.item(e.getLength() - 1);
            NodeList nl2 = node1.getChildNodes();
            Node node2 = nl2.item(this.getNodeIndex(nl2, "value"));
            Log.i("DistanceValue", node2.getTextContent());
            return Integer.parseInt(node2.getTextContent());
        } catch (Exception var6) {
            return -1;
        }
    }

    public String getStartAddress(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("start_address");
            Node node1 = e.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception var4) {
            return "-1";
        }
    }

    public String getEndAddress(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("end_address");
            Node node1 = e.item(0);
            Log.i("StartAddress", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception var4) {
            return "-1";
        }
    }

    public String getCopyRights(Document doc) {
        try {
            NodeList e = doc.getElementsByTagName("copyrights");
            Node node1 = e.item(0);
            Log.i("CopyRights", node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception var4) {
            return "-1";
        }
    }

    public ArrayList<LatLng> getDirection(Document doc) {
        ArrayList listGeopoints = new ArrayList();
        NodeList nl1 = doc.getElementsByTagName("step");
        if(nl1.getLength() > 0) {
            for(int i = 0; i < nl1.getLength(); ++i) {
                Node node1 = nl1.item(i);
                NodeList nl2 = node1.getChildNodes();
                Node locationNode = nl2.item(this.getNodeIndex(nl2, "start_location"));
                NodeList nl3 = locationNode.getChildNodes();
                Node latNode = nl3.item(this.getNodeIndex(nl3, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl3.item(this.getNodeIndex(nl3, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
                locationNode = nl2.item(this.getNodeIndex(nl2, "polyline"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(this.getNodeIndex(nl3, "points"));
                ArrayList arr = this.decodePoly(latNode.getTextContent());

                for(int j = 0; j < arr.size(); ++j) {
                    listGeopoints.add(new LatLng(((LatLng)arr.get(j)).latitude, ((LatLng)arr.get(j)).longitude));
                }

                locationNode = nl2.item(this.getNodeIndex(nl2, "end_location"));
                nl3 = locationNode.getChildNodes();
                latNode = nl3.item(this.getNodeIndex(nl3, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = nl3.item(this.getNodeIndex(nl3, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                listGeopoints.add(new LatLng(lat, lng));
            }
        }

        return listGeopoints;
    }
//(Document doc, Long lat, Long lng, String name, String Address, String Vicinity, String rating)
//    public ArrayList<LocationMark> getMarkers(Document doc) {
public LocationMark[] getMarkers(Document doc, LocationMark[] marker){
//        ArrayList listPlaces = new ArrayList();
//        LocationMark[] marker = new LocationMark[20];
        NodeList nl1 = doc.getElementsByTagName("result");
        if(nl1.getLength() > 0) {
            for(int i = 0; i < nl1.getLength(); ++i) {
                String rating = "";
                Node node1 = nl1.item(i);
                NodeList nl2 = node1.getChildNodes();
                Node nameNode = nl2.item(this.getNodeIndex(nl2, "name"));
                String Name = nameNode.getTextContent();
                Node vicinityNode = nl2.item(this.getNodeIndex(nl2, "vicinity"));
                String vicinity = vicinityNode.getTextContent();
                try {
                    Node ratingNode = nl2.item(this.getNodeIndex(nl2, "rating"));
                    rating = ratingNode.getTextContent();
                }catch (Exception e){Log.println(Log.VERBOSE, "rating", "No Rating");}

                Node locationNode = nl2.item(this.getNodeIndex(nl2, "geometry"));
//                Node geoNode = nl2.item(this.getNodeIndex(nl2, "geometry"));
//                Node locationNode = geoNode.getNo

                nl2 = locationNode.getChildNodes();
                Node locNode = nl2.item(this.getNodeIndex(nl2, "location"));
                nl2 = locNode.getChildNodes();
                Node latNode = nl2.item(this.getNodeIndex(nl2, "lat"));
//                Long lat = Long.parseLong(latNode.getTextContent());
                Double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = nl2.item(this.getNodeIndex(nl2, "lng"));
                Double lng = Double.parseDouble(lngNode.getTextContent());
//                Long lng = Long.parseLong(lngNode.getTextContent());
                LatLng latlng = new LatLng(lat, lng);
                 marker[i] = new LocationMark(latlng, Name ,vicinity, rating);
//                marker[i].set_latLng(latlng);
//                marker[i].set_name(Name);
//                marker[i].set_rating(rating);
//                marker[i].set_vicinity(vicinity);
//                listPlaces.add(marker);
//                Marker marker = map.addMarker((new MarkerOptions()).position(new LatLng(lat, lng)).title(Name).snippet("Address:" + vicinity + "/n Rating: " + rating).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            }
        }

        return marker;
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for(int i = 0; i < nl.getLength(); ++i) {
            if(nl.item(i).getNodeName().equals(nodename)) {
                return i;
            }
        }

        return -1;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList poly = new ArrayList();
        int index = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;

        while(index < len) {
            int shift = 0;
            int result = 0;

            int b;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 31) << shift;
                shift += 5;
            } while(b >= 32);

            int dlat = (result & 1) != 0?~(result >> 1):result >> 1;
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 31) << shift;
                shift += 5;
            } while(b >= 32);

            int dlng = (result & 1) != 0?~(result >> 1):result >> 1;
            lng += dlng;
            LatLng position = new LatLng((double)lat / 100000.0D, (double)lng / 100000.0D);
            poly.add(position);
        }

        return poly;
    }
}

