package com.boken_edge.lachlan.whattoeat;

/**
 * Created by Lachlan on 13/06/2016.
 */

import android.*;
import android.Manifest;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.support.v4.content.PermissionChecker;

public final class GPSTracker implements LocationListener {
    private final Context mContext;
    public boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100L;
    private static final long MIN_TIME_BW_UPDATES = 60L;
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        this.getLocation();
    }

    public Location getLocation() {
        try {
            this.locationManager = (LocationManager)this.mContext.getSystemService(Context.LOCATION_SERVICE);
            this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            Log.v("isGPSEnabled", "=" + this.isGPSEnabled);
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            Log.v("isNetworkEnabled", "=" + this.isNetworkEnabled);
            if(this.isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if(this.isNetworkEnabled) {
                    this.location = null;
                    if (PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && PermissionChecker.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {showGPSSettingsAlert();}
                    else{
                        this.locationManager.requestLocationUpdates("network", 60L, 100.0F, this);
                        Log.d("Network", "Network");
                        if (this.locationManager != null) {
                            this.location = this.locationManager.getLastKnownLocation("network");
                            if (this.location != null) {
                                this.latitude = this.location.getLatitude();
                                this.longitude = this.location.getLongitude();
                            }
                        }
                    }
                }

                if(this.isGPSEnabled) {
                    this.location = null;
                    if(this.location == null) {
                        this.locationManager.requestLocationUpdates("gps", 60L, 100.0F, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if(this.locationManager != null) {
                            this.location = this.locationManager.getLastKnownLocation("gps");
                            if(this.location != null) {
                                this.latitude = this.location.getLatitude();
                                this.longitude = this.location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return this.location;
    }

//    public void stopUsingGPS() {
//        if(this.locationManager != null) {
//            if (PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && PermissionChecker.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {showSettingsAlert();}
//            else {
//                this.locationManager.removeUpdates(this);
//            }
//        }
//
//    }

    public double getLatitude() {
        if(this.location != null) {
            this.latitude = this.location.getLatitude();
        }

        return this.latitude;
    }

    public double getLongitude() {
        if(this.location != null) {
            this.longitude = this.location.getLongitude();
        }

        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showGPSSettingsAlert() {
        Builder alertDialog = new Builder(this.mContext);
        alertDialog.setTitle("Change Settings");
        alertDialog.setMessage("GPS access is not enabled. Do you want to enable it?");
        alertDialog.setPositiveButton("Settings", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                Intent intent = new Intent(Settings.ACTION_SETTINGS);
//                GPSTracker.this.mContext.startActivity(intent);




                Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                mContext.sendBroadcast(intent);

//                if(errName == "GPS") {
//                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    GPSTracker.this.mContext.startActivity(intent);
//                }else if(errName == "Data"){
//                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    GPSTracker.this.mContext.startActivity(intent);
//                }else if(errName == "WiFi"){
//                }Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                GPSTracker.this.mContext.startActivity(intent);

            }
        });
        alertDialog.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void showNetworkSettingsAlert() {
        Builder alertDialog = new Builder(this.mContext);
        alertDialog.setTitle("Change Settings");
        alertDialog.setMessage("Internet access is not enabled. Do you want to enable it?");
        alertDialog.setPositiveButton("Settings", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent=new Intent("android.network.NETWORK_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                mContext.sendBroadcast(intent);


            }
        });
        alertDialog.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
