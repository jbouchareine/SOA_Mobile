package com.example.antoine.envoi_requete;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class MainActivity extends ActionBarActivity  implements LocationListener {

    private double latitude = 0.0;
    private double longitude = 0.0;
    RegisterTask registerTask = null;
    private static final String PROJECT_NUMBER = "465384961834";
    private boolean myservice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        registerTask = new RegisterTask(getApplicationContext(), gcm);
        registerTask.execute(PROJECT_NUMBER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    public void sendHttp(View v){

        if(!registerTask.reg_id.equals("")) {
            HttpTask httpTask = new HttpTask(getApplicationContext());
            httpTask.execute("http://10.11.160.21:8001/test/index.php?id=" + registerTask.reg_id);
        }
    }

    public void serviceButton(View v){

        Button serviceButton = (Button) this.findViewById(R.id.button2);

        if(!myservice)//play the service
        {
            myservice=true;
            startService(new Intent(this,LocationService.class));
            serviceButton.setText("Started");
        }
        else//stop the service
        {
            myservice=false;
            stopService(new Intent(this,LocationService.class));
            serviceButton.setText("Stopped");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

}
