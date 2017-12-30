package com.example.rdvgeo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class GeoLoc extends Service {
    private LocationManager locationMgr = null;

    private LocationListener onLocationChange = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {

            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            Toast.makeText(getBaseContext(),
                    "Voici les coordonnées de votre téléphone : " + latitude + " " + longitude,
                    Toast.LENGTH_LONG).show();
            sendBroadcast(true, longitude, latitude);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
            //      0, onLocationChange);
            locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
                    onLocationChange);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationMgr.removeUpdates(onLocationChange);
    }

    private void sendBroadcast(boolean success, double longitude, double latitude) {
        Intent intent = new Intent("message");
        intent.putExtra("success", success);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
