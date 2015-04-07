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

/**
 * Created by Antoine on 03/04/2015.
 */
public class LocationService extends Service implements LocationListener {

    private final static String TAG = LocationService.class.getSimpleName();

    private MyThread myThread;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private int compteurArret;
    public boolean isRunning = false;
    LocationManager lm;
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate");
        compteurArret = 0;
      //  myThread = new MyThread();
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "OnStartCommand");

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
       /* if(!isRunning) {
            isRunning = true;
            myThread.start();
        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        lm.removeUpdates(this);
      /*  if(isRunning)
            myThread.interrupt();

        myThread = null;*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

        double roundLat = Math.round(location.getLatitude() * Math.pow(10,2)) / Math.pow(10,2);
        double roundLong = Math.round(location.getLongitude() * Math.pow(10,2)) / Math.pow(10,2);

        if(roundLat == this.latitude && roundLong == this.longitude)
            compteurArret++;
        else{
            compteurArret = 0;
        }
       /* if(compteurArret >= 2) {
            compteurArret = 0;
            Log.i(TAG, "Arrêt prolongé détecté");
        }*/
        this.latitude = roundLat;
        this.longitude = roundLong;

        if(compteurArret >= 2) {
            compteurArret = 0;
            Toast.makeText(this, "Arrêt prolongé détecté", Toast.LENGTH_SHORT).show();
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

    class MyThread extends Thread{

        private final static int DELAY = 2000;

        @Override
        public void run() {
            super.run();

            while(isRunning){

                Log.i(TAG, "Running");

                try {
                    sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        }
    }
}



