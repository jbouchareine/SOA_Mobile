package com.example.antoine.envoi_requete;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class MainActivity extends ActionBarActivity  implements LocationListener {

    private LocationManager lm;
    private double latitude = 0.0;
    private double longitude = 0.0;

    GoogleCloudMessaging gcm;
    String regId = "";
    private static final String APP_VERSION = "appVersion";
    private static final String PROJECT_NUMBER = "465384961834";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

registerGCM();
        /*GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this); // context étant le context de l'activité
        try {
            String registerId = gcm.register("465384961834");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    /**
     * Cette méthode récupère le registerId dans les SharedPreferences via
     * la méthode getRegistrationId(context).
     * S'il n'existe pas alors on enregistre le terminal via
     * la méthode registerInBackground()
     */
    public void registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
       // regId = getRegistrationId();

        if (TextUtils.isEmpty(regId)) {
            RegisterTask registerTask = new RegisterTask(this, gcm);
            registerTask.execute(PROJECT_NUMBER);
        } else {
            Toast.makeText(getApplicationContext(), "RegId existe déjà. RegId: " + regId, Toast.LENGTH_LONG).show();
        }
       // return regId;
    }

    @Override
    protected void onResume() {
        super.onResume();

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
    }

    public void sendHttp(View v){

        HttpTask httpTask = new HttpTask(getApplicationContext());
        httpTask.execute("http://10.11.161.12:8001/test/index.php?lat="+this.latitude+"&long="+this.longitude);
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
