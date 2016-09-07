package com.boken_edge.lachlan.whattoeat;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lachlan on 27/06/2016.
 */
public class LocationMark {

    LatLng latLng;
    String name;
    String vicinity;
    String rating;

    public LocationMark(LatLng _latLng, String _name, String _vicinity, String _rating){
        latLng = _latLng;
        name = _name;
        vicinity = _vicinity;
        rating = _rating;
    }

    public LatLng get_latLng() {
        return latLng;
    }

    public String get_name() {
        return name;
    }

    public String get_rating() {
        return rating;
    }

    public String get_vicinity() {
        return vicinity;
    }

    public void set_latLng(LatLng _latLng) {
        latLng = _latLng;
    }

    public void set_name(String _name) {
        name = _name;
    }

    public void set_rating(String _rating) {
        rating = _rating;
    }

    public void set_vicinity(String _vicinity) {
        vicinity = _vicinity;
    }
}
