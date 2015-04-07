package com.example.antoine.envoi_requete;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Antoine on 03/04/2015.
 */
public class LocationService extends Service implements LocationListener {

    private final static String TAG = LocationService.class.getSimpleName();

    private double latitude = 0.0;
    private double longitude = 0.0;
    private int compteurArret;

    private LocationManager lm;
    private RegisterTask registerTask;

    @Override
    public void onCreate() {
        super.onCreate();

        compteurArret = 0;

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);

        Toast.makeText(this, "Service démarré", Toast.LENGTH_SHORT).show();
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {

        registerTask = new RegisterTask(this);
        registerTask.execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();

        lm.removeUpdates(this);
        Toast.makeText(this, "Service terminé", Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        double roundLat = Math.round(location.getLatitude() * Math.pow(10,3)) / Math.pow(10,3);
        double roundLong = Math.round(location.getLongitude() * Math.pow(10,3)) / Math.pow(10,3);

        if(roundLat == this.latitude && roundLong == this.longitude)
            compteurArret++;
        else{
            compteurArret = 0;
        }

        this.latitude = roundLat;
        this.longitude = roundLong;

        if(compteurArret > 2)
        {
            compteurArret = 0;
            Toast.makeText(this, "Arrêt prolongé détecté", Toast.LENGTH_SHORT).show();

            HttpTask httpTask = new HttpTask(this);
            httpTask.execute(registerTask.reg_id, latitude+"", longitude+"", "4", "123");

        }else {
            Toast.makeText(this, "Position: " + roundLat + "  " + roundLong + " => " + compteurArret, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

}



